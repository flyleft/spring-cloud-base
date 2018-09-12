package io.choerodon.demo.quartz;

import io.choerodon.asgard.schedule.annotation.JobParam;
import io.choerodon.asgard.schedule.annotation.JobTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Task {

    private static final Logger LOGGER = LoggerFactory.getLogger(Task.class);

    @JobTask(maxRetryCount = 2, params = {
            @JobParam(name = "isInstantly", defaultValue = "true", type = Boolean.class),
            @JobParam(name = "name", defaultValue = "zh"),
            @JobParam(name = "age", type = Integer.class)
    })
    public Map<String, Object> test(Map<String, Object> data) {
        LOGGER.info("data {}", data);
        Object age = data.get("age");
        if (age != null) {
            data.put("age", (Integer)age + 1);
        }
        return data;
    }

}
