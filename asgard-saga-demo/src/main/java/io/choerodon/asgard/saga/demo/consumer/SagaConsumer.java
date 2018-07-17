package io.choerodon.asgard.saga.demo.consumer;

import io.choerodon.core.saga.SagaTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SagaConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SagaConsumer.class);

    @SagaTask(code = "devopsCreateUser",
            description = "devops创建用户",
            sagaCode = "asgard-create-user",
            concurrentExecLimit = 2,
            seq = 2)
    public GitLabUser devopsCreateUser(String data) {
        LOGGER.info("devopsCreateUser {}", data);
        return null;
    }


    @SagaTask(code = "agileCreateUser",
            description = "agile创建用户",
            sagaCode = "asgard-create-user",
            seq = 2)
    public String agileCreateUser(String data) {
        LOGGER.info("agileCreateUser {}", data);
        return "agileCreateUser";
    }


    @SagaTask(code = "gitlabCreateUser",
            description = "gitlab创建用户11",
            sagaCode = "asgard-create-user",
            seq = 5)
    public String gitlabCreateUser(String data) {
        LOGGER.info("gitlabCreateUser {}", data);
        return data;

    }

    public static class GitLabUser {
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
    }


}
