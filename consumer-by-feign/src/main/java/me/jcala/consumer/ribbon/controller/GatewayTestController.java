package me.jcala.consumer.ribbon.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author flyleft
 * @date 2018/5/7
 */
@RestController
public class GatewayTestController {

    private static final Logger logger = LoggerFactory.getLogger(GatewayTestController.class);

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

}
