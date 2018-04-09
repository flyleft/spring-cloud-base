package me.jcala.eureka.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhipeng.zuo
 */
@SpringBootApplication
//@EnableEurekaClient
@RestController
public class EurekaClientApplication {

  private static final Logger logger = LoggerFactory.getLogger(EurekaClientApplication.class);

  @GetMapping("/hello")
  public String home() {
    return "Hello World";
  }

  @GetMapping("/skip/{sleep}")
  public String gatewayTest(@PathVariable("sleep") Long sleep) {
    logger.info("============skip============ ");
    try {
      Thread.sleep(sleep);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return "Hello World";
  }

  public static void main(String[] args) {
    new SpringApplicationBuilder (EurekaClientApplication.class).web(true).run(args);
  }

}