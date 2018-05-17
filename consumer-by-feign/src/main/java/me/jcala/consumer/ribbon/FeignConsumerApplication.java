package me.jcala.consumer.ribbon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhipeng.zuo on 2017/8/29.
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableHystrixDashboard
@RestController
public class FeignConsumerApplication {

  @Autowired
  private EventClient eventClient;

  public static void main(String[] args) {
    new SpringApplicationBuilder(FeignConsumerApplication.class).web(true).run(args);
  }


  @GetMapping("/feign/local")
  public String sayLocalByClientA(){

    return eventClient.createEvent("");
  }

}