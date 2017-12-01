package me.jcala.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhipeng.zuo on 2017/8/30.
 */
@SpringBootApplication(exclude = { RabbitAutoConfiguration.class })
@EnableConfigServer
@EnableEurekaClient
public class ConfigServerApplication {

  public static void main(String[] args) {
    new SpringApplicationBuilder (ConfigServerApplication.class).web(true).run(args);
  }

}
