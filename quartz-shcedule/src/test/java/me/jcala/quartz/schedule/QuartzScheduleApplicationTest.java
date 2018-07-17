package me.jcala.quartz.schedule;

import org.junit.Rule;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.context.ConfigurableApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;


public class QuartzScheduleApplicationTest {

    @Rule
    public OutputCapture outputCapture = new OutputCapture();

    @Test
    public void quartzJobIsTriggered() throws InterruptedException {
        try (ConfigurableApplicationContext context = SpringApplication
                .run(QuartzScheduleApplication.class)) {
            long end = System.currentTimeMillis() + 5000;
            while ((!this.outputCapture.toString().contains("Hello World!"))
                    && System.currentTimeMillis() < end) {
                Thread.sleep(100);
            }
            assertThat(this.outputCapture.toString()).contains("Hello World!");
        }
    }

}
