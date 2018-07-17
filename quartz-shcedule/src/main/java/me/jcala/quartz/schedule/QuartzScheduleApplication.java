package me.jcala.quartz.schedule;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
//@EnableEurekaClient
public class QuartzScheduleApplication {


    public static void main(String[] args) {
        new SpringApplicationBuilder(QuartzScheduleApplication.class).web(true).run(args);
    }


}
