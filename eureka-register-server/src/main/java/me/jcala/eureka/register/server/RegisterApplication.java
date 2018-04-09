package me.jcala.eureka.register.server;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Created by zhipeng.zuo on 2017/8/28.
 */
@SpringBootApplication
@EnableEurekaServer
public class RegisterApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(RegisterApplication.class).web(true).run(args);
    }

}
