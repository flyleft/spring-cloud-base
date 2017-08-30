package me.jcala.consumer.ribbon.hystrix.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Created by zhipeng.zuo on 2017/8/29.
 */
@Service
public class HelloService {
  private LoadBalancerClient loadBalancer;
  private RestTemplate restTemplate;

  @Autowired
  public HelloService(LoadBalancerClient loadBalancer, RestTemplate restTemplate) {
    this.loadBalancer = loadBalancer;
    this.restTemplate = restTemplate;
  }

  @HystrixCommand(fallbackMethod = "getHelloByClientAFallback")
  public String getHelloByClientA(){
    ServiceInstance instance = loadBalancer.choose("client-server-a");
    URI helloUri = URI.create(String.format("http://%s:%s/hello", instance.getHost(), instance.getPort()));
    return restTemplate.getForEntity (helloUri,String.class).getBody();
  }

  public String getHelloByClientAFallback(){
    return "error";
  }
}
