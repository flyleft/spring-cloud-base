package io.choerodon.asgard.saga.demo.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.choerodon.asgard.saga.SagaDefinition;
import io.choerodon.asgard.saga.annotation.SagaTask;
import io.choerodon.asgard.saga.demo.producer.AsgardUser;
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
        LOGGER.info("===== asgardUser {}", asgardUser);
        DevopsUser devopsUser = new DevopsUser();
        devopsUser.setId(asgardUser.getId());
        devopsUser.setGroup("test");
        LOGGER.info("===== devopsCreateUser {}", devopsUser);
        return devopsUser;
    }


    @SagaTask(code = "agileCreateUser",
            description = "agile创建用户",
            sagaCode = "asgard-create-user",
            outputSchemaClass = AsgardUser.class,
            transactionIsolation = Isolation.READ_COMMITTED,
            transactionReadOnly = true,
            transactionTimeout = 100,
            seq = 2)
    public String agileCreateUser(String data) throws IOException {
        AsgardUser asgardUser = objectMapper.readValue(data, AsgardUser.class);
        LOGGER.info("***** asgardUser {}", asgardUser);
        return null;
    }


    @SagaTask(code = "gitlabCreateUser",
            description = "gitlab创建用户11",
            sagaCode = "asgard-create-user",
            enabledDbRecord = true,
            outputSchemaClass = DevopsUser.class,
            seq = 5)
    public String gitlabCreateUser(String data) throws IOException {
        LOGGER.info("##### data {}", data);
        DevopsUser devopsUser = objectMapper.readValue(data, DevopsUser.class);
        LOGGER.info("##### devopsUser {}", devopsUser);
        return data;
    }

    @SagaTask(code = "test", sagaCode = "iam-create-project", seq = 1, outputSchemaClass = AsgardUser.class)
    public String iamCreateUser(String data) {
        LOGGER.info("&&&&& data {}", data);
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
