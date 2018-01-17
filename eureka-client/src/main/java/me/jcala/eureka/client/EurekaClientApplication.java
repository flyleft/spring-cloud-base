package me.jcala.eureka.client;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhipeng.zuo on 2017/8/28.
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
    new SpringApplicationBuilder (EurekaClientApplication.class).web(true).run(args);
  }

}