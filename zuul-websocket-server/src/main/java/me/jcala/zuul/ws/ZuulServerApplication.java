package me.jcala.zuul.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * Created by zhipeng.zuo on 2017/8/28.
 */
@EnableZuulProxy
@EnableEurekaClient
@EnableZuulWebsocket
@SpringBootApplication
public class ZuulServerApplication {
  public static void main(String[] args) {
    SpringApplication.run(ZuulServerApplication.class, args);
  }
}
