package io.choerodon.demo.saga.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.choerodon.asgard.saga.SagaDefinition;
import io.choerodon.asgard.saga.annotation.SagaTask;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.demo.saga.producer.AsgardUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;

import java.io.IOException;

@Component
public class SagaConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SagaConsumer.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @SagaTask(code = "devopsCreateUser",
            description = "devops创建用户",
            sagaCode = "asgard-create-user",
            concurrentLimitNum = 2,
            concurrentLimitPolicy = SagaDefinition.ConcurrentLimitPolicy.TYPE,
            seq = 2)
    public DevopsUser devopsCreateUser(String data) throws IOException {
        AsgardUser asgardUser = objectMapper.readValue(data, AsgardUser.class);
        DevopsUser devopsUser = new DevopsUser();
        devopsUser.setId(asgardUser.getId());
        devopsUser.setGroup("test");
        LOGGER.info("11111111 userDetails {}", DetailsHelper.getUserDetails());
        return devopsUser;
    }


    @SagaTask(code = "agileCreateUser",
            description = "agile创建用户",
            sagaCode = "asgard-create-user",
            outputSchemaClass = AsgardUser.class,
            transactionIsolation = Isolation.READ_COMMITTED,
            seq = 2)
    public String agileCreateUser(String data) throws IOException {
        AsgardUser asgardUser = objectMapper.readValue(data, AsgardUser.class);
        LOGGER.info("2222222222 userDetails {}", DetailsHelper.getUserDetails());
        return null;
    }


    @SagaTask(code = "gitlabCreateUser",
            description = "gitlab创建用户11",
            sagaCode = "asgard-create-user",
            outputSchemaClass = DevopsUser.class,
            seq = 5)
    public String gitlabCreateUser(String data) throws IOException {
        DevopsUser devopsUser = objectMapper.readValue(data, DevopsUser.class);
        LOGGER.info("33333333333 userDetails {}", DetailsHelper.getUserDetails());
        return data;
    }

    public static class DevopsUser {
        private Long id;
        private String group;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        @Override
        public String toString() {
            return "DevopsUser{" +
                    "id=" + id +
                    ", group='" + group + '\'' +
                    '}';
        }
    }


}
