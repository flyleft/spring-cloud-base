package me.jcala.consumer.ribbon;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author flyleft
 * @date 2018/5/16
 */
@FeignClient(name = "eureka-client", fallback = HystrixClientFallback.class)
public interface TestClient {

    @GetMapping("/hello")
    String hello();
}
