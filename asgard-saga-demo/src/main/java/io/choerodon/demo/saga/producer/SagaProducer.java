package io.choerodon.demo.saga.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.choerodon.asgard.saga.annotation.Saga;
import io.choerodon.asgard.saga.dto.StartInstanceDTO;
import io.choerodon.asgard.saga.feign.SagaClient;
import io.choerodon.asgard.saga.producer.StartSagaBuilder;
import io.choerodon.asgard.saga.producer.TransactionalProducer;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.demo.saga.producer.mapper.AsgardUserMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class SagaProducer {

    private static final String SAGA_CREATE_USER = "asgard-create-user";

    private static final String CREATE_USER_ERROR = "error.AsgardUser.create";

    private SagaClient sagaClient;

    private AsgardUserMapper asgardUserMapper;

    private TransactionalProducer transactionalProducer;

    private ObjectMapper objectMapper = new ObjectMapper();

    public SagaProducer(SagaClient sagaClient,
                        AsgardUserMapper asgardUserMapper,
                        TransactionalProducer producer) {
        this.sagaClient = sagaClient;
        this.asgardUserMapper = asgardUserMapper;
        this.transactionalProducer = producer;
    }

    @PostMapping("/v1/users/old")
    @Transactional
    public AsgardUser createUser(@Valid @RequestBody AsgardUser user) {
        if (asgardUserMapper.insertSelective(user) != 1) {
            throw new CommonException(CREATE_USER_ERROR);
        }
        try {
            String input = objectMapper.writeValueAsString(asgardUserMapper.selectByPrimaryKey(user.getId()));
            sagaClient.startSaga(SAGA_CREATE_USER, new StartInstanceDTO(input, "user", user.getId() + "", "site", 0L));
            return user;
        } catch (JsonProcessingException e) {
            throw new CommonException("error.SagaProducer.createUser");
        }
    }


    @PostMapping("/v1/projects/{project_id}/users/new")
    @Saga(code = SAGA_CREATE_USER, description = "创建项目", inputSchemaClass = AsgardUser.class)
    public AsgardUser projectLevelCreateUser(@PathVariable("project_id") long projectId,
                                             @Valid @RequestBody AsgardUser user) {
        //通过StartSagaBuilder.newBuilder()设置saga的参数
        //level和sourceId：表示所在层级和资源id，此处即为项目层和项目id
        //RefId和RefType表示关联的类型和关联的id，用于并发策略限制
        //
        return transactionalProducer.applyAndReturn(StartSagaBuilder.newBuilder().withLevel(ResourceLevel.PROJECT)
                        .withSourceId(projectId)
                        .withRefType("user")
                        .withPayloadAndSerialize(user)
                        .withSagaCode(SAGA_CREATE_USER),
                builder -> {
                    // 业务执行代码
                    if (asgardUserMapper.insertSelective(user) != 1) {
                        throw new CommonException(CREATE_USER_ERROR);
                    }
                    // user插入之后id被回写
                    // 使用withPayloadAndSerialize和withJson方法设置saga参数
                    builder.withPayloadAndSerialize(user).withRefId(user.getId() + "");
                    return user;
                });
    }

    @PostMapping("/v1/users/new")
    public AsgardUser siteLevelCreateUser(@Valid @RequestBody AsgardUser user) {
        transactionalProducer.apply(StartSagaBuilder.newBuilder().withLevel(ResourceLevel.SITE).withSagaCode(SAGA_CREATE_USER),
                builder -> {
                    if (asgardUserMapper.insertSelective(user) != 1) {
                        throw new CommonException(CREATE_USER_ERROR);
                    }
                    builder.withPayloadAndSerialize(user);
                });
        return user;
    }
}
