package me.flyleft.eureka.client;

import me.flyleft.eureka.client.event.EurekaEventHandler;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author flyleft
 */
@SpringBootApplication
@EnableEurekaClient
@RestController
public class EurekaClientApplication {

    @GetMapping("/hello")
    public String home() {
        return "Hello World";
    }

    public static void main(String[] args) {
        EurekaEventHandler.getInstance().init();
        new SpringApplicationBuilder(EurekaClientApplication.class).web(true).run(args);
    }


}