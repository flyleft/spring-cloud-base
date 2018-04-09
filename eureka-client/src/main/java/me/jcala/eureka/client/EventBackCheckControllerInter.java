package me.jcala.eureka.client;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author flyleft
 * @date 2018/4/8
 */
public interface EventBackCheckControllerInter {

    @GetMapping("/v1/events")
    String backCheck();

}
