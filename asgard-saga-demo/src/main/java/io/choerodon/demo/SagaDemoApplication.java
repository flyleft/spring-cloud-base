package io.choerodon.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("io.choerodon")
@EnableEurekaClient
public class SagaDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SagaDemoApplication.class, args);
    }

}
