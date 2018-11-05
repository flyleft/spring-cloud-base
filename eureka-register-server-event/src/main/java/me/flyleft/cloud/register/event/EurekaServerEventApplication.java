package me.flyleft.cloud.register.event;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.netflix.eureka.server.EurekaServerAutoConfiguration;
import org.springframework.context.annotation.Import;

@EnableEurekaServer
@SpringBootApplication
@Import(EurekaServerAutoConfiguration.class)
public class EurekaServerEventApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerEventApplication.class, args);
    }

}