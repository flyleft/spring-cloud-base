package me.jcala.cloud.spring.jar.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhipeng.zuo
 * Created on 17-12-4.
 */
@Configuration
public class TestConfig {

    @Bean
    public String str(){
        System.out.println("xxxxxxxxxxxxxxxxxx" + Thread.currentThread().getContextClassLoader());
        return "";
    }
}
