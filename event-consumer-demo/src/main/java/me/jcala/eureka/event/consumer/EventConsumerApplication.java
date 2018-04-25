package me.jcala.eureka.event.consumer;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author flyleft
 * @date 2018/4/9
 */
@SpringBootApplication
//@EnableEurekaClient
public class EventConsumerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(EventConsumerApplication.class).web(true).run(args);
    }

}
