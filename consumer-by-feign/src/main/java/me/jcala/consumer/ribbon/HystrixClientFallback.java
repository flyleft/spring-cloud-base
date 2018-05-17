package me.jcala.consumer.ribbon;

import org.springframework.stereotype.Component;

/**
 * @author flyleft
 * @date 2018/5/16
 */
@Component
public class HystrixClientFallback implements TestClient {

    @Override
    public String hello() {
        throw new RuntimeException("error.event.create");
    }
}
