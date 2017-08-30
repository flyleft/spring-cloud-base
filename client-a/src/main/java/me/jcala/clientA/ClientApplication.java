package me.jcala.clientA;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhipeng.zuo on 2017/8/28.
 */
@SpringBootApplication
@EnableEurekaClient
@RestController
public class ClientApplication {

  @GetMapping("/hello")
  public String home() {
    return "Hello World";
  }

  public static void main(String[] args) {
    new SpringApplicationBuilder (ClientApplication.class).web(true).run(args);
  }

}