package me.jcala.consumer.ribbon.controller;

import com.netflix.client.ClientException;
import me.jcala.consumer.ribbon.TestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author flyleft
 * @date 2018/5/7
 */
@RestController
public class GatewayTestController {

    private static final Logger logger = LoggerFactory.getLogger(GatewayTestController.class);

    private TestClient testClient;

    @Autowired
    public GatewayTestController(TestClient testClient) {
        this.testClient = testClient;
    }

    @GetMapping("/test")
    public String test() {
        try {
            logger.info("=== {}", System.currentTimeMillis());
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "test";
    }

    @GetMapping("/hello")
    public String hello() {
        try {
            return testClient.hello();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return "default";
        } catch (Exception e) {
            e.printStackTrace();
            return "hhhhhhhhhhhh";
        }
    }

}
