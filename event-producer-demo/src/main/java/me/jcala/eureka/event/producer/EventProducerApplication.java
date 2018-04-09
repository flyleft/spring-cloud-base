package me.jcala.eureka.event.producer;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * @author flyleft
 * @date 2018/4/9
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients("io.choerodon")
public class EventProducerApplication {

    public static final String EVENT_TYPE_ORDER = "order";

    public static void main(String[] args) {
        new SpringApplicationBuilder(EventProducerApplication.class).web(true).run(args);
    }

}
