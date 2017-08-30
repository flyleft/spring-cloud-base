package me.jcala.config.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhipeng.zuo on 2017/8/30.
 */
@SpringBootApplication
@RefreshScope
@RestController
@EnableEurekaClient
public class ConfigClientApplication {

  @Value("${site.info}")
  private String info;

  @Autowired
  private Environment env;

  public static void main(String[] args) {
    new SpringApplicationBuilder (ConfigClientApplication.class).web(true).run(args);
  }

  @GetMapping("/info_by_value")
  public String getInfoByValue(){
    return this.info;
  }

  @GetMapping("/info_by_env")
  public String getInfoByEnv(){
    return env.getProperty("site.info","undefined");
  }
}
