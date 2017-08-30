package me.jcala.consumer.ribbon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Created by zhipeng.zuo on 2017/8/29.
 */
@SpringBootApplication
@EnableEurekaClient
@RestController
public class RibbonConsumerApplication {
  public static void main(String[] args) {
    new SpringApplicationBuilder(RibbonConsumerApplication.class).web(true).run(args);
  }

  @Autowired
  private LoadBalancerClient loadBalancer;

  @Bean
  public RestTemplate restTemplate(){
    return new RestTemplate ();
  }

  @GetMapping("/ribbon/local")
  public String sayLocalByClientA(){
    ServiceInstance instance = loadBalancer.choose("client-server-a");
    URI storesUri = URI.create(String.format("http://%s:%s", instance.getHost(), instance.getPort()));
    return storesUri.getHost ();
  }

  @GetMapping("/ribbon/hello")
  public String sayHelloByClientA(){
    ServiceInstance instance = loadBalancer.choose("client-server-a");
    URI helloUri = URI.create(String.format("http://%s:%s/hello", instance.getHost(), instance.getPort()));
    return restTemplate ().getForEntity (helloUri,String.class).getBody();
  }
}