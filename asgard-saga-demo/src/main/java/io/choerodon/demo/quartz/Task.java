package io.choerodon.demo.quartz;

import io.choerodon.asgard.schedule.QuartzDefinition;
import io.choerodon.asgard.schedule.annotation.JobParam;
import io.choerodon.asgard.schedule.annotation.JobTask;
import io.choerodon.asgard.schedule.annotation.TaskParam;
import io.choerodon.asgard.schedule.annotation.TimedTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Task {

    private static final Logger LOGGER = LoggerFactory.getLogger(Task.class);

    @JobTask(code = "test",
            maxRetryCount = 2, params = {
            @JobParam(name = "isInstantly", defaultValue = "true", type = Boolean.class),
            @JobParam(name = "name", defaultValue = "zh"),
            @JobParam(name = "age", type = Integer.class)
    })
    @TimedTask(name = "本地测试", description = "自定义定时任务", oneExecution = true,
            repeatCount = 0, repeatInterval = 50, repeatIntervalUnit = QuartzDefinition.SimpleRepeatIntervalUnit.SECONDS,
            params = {
                    @TaskParam(name = "isInstantly", value = "true"),
                    @TaskParam(name = "name", value = "zh"),
                    @TaskParam(name = "age", value = "23")}
    )
    public Map<String, Object> test(Map<String, Object> data) {
        LOGGER.info("data {}", data);
        Object age = data.get("age");
        if (age != null) {
            data.put("age", (Integer) age + 1);
        }
        return data;
    }

}
