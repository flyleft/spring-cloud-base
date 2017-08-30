package me.jcala.consumer.ribbon.hystrix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Created by zhipeng.zuo on 2017/8/29.
 */
@SpringBootApplication
@EnableHystrixDashboard
@EnableEurekaClient
@EnableCircuitBreaker
@RestController
public class HystrixConsumerApplication {
  public static void main(String[] args) {
    new SpringApplicationBuilder (HystrixConsumerApplication.class).web(true).run(args);
  }
  @Autowired
  private LoadBalancerClient loadBalancer;

  @Bean
  public RestTemplate restTemplate(){
    return new RestTemplate ();
  }

  @GetMapping("/ribbon/hello")
  public String sayHelloByClientA(){
    ServiceInstance instance = loadBalancer.choose("client-server-a");
    URI helloUri = URI.create(String.format("http://%s:%s/hello", instance.getHost(), instance.getPort()));
    return restTemplate ().getForEntity (helloUri,String.class).getBody();
  }
}
