package io.choerodon.demo.saga.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.choerodon.asgard.saga.annotation.Saga;
import io.choerodon.asgard.saga.dto.StartInstanceDTO;
import io.choerodon.asgard.saga.feign.SagaClient;
import io.choerodon.asgard.saga.producer.SagaTransactionalProducer;
import io.choerodon.core.exception.CommonException;
import io.choerodon.demo.saga.producer.mapper.AsgardUserMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Saga(code = "asgard-create-user", description = "创建项目", inputSchemaClass = AsgardUser.class)
public class SagaProducer {

    private SagaClient sagaClient;

    private AsgardUserMapper asgardUserMapper;

    private SagaTransactionalProducer producer;


    private ObjectMapper objectMapper = new ObjectMapper();

    public SagaProducer(SagaClient sagaClient,
                        AsgardUserMapper asgardUserMapper,
                        SagaTransactionalProducer producer) {
        this.sagaClient = sagaClient;
        this.asgardUserMapper = asgardUserMapper;
        this.producer = producer;
    }

    @PostMapping("/v1/users")
    @Transactional
    public AsgardUser createUser(@Valid @RequestBody AsgardUser user) {
        if (asgardUserMapper.insertSelective(user) != 1) {
            throw new CommonException("error.AsgardUser.create");
        }
        try {
            String input = objectMapper.writeValueAsString(asgardUserMapper.selectByPrimaryKey(user.getId()));
            sagaClient.startSaga("asgard-create-user", new StartInstanceDTO(input, "", ""));
            return user;
        } catch (JsonProcessingException e) {
            throw new CommonException("error.SagaProducer.createUser");
        }
    }


    @PostMapping("/v1/users/by_template")
    @Transactional
    public AsgardUser createUserByTemplate(@Valid @RequestBody AsgardUser user) {

    }

}
