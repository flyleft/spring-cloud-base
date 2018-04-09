package me.jcala.eureka.client;

import org.springframework.web.bind.annotation.RestController;

/**
 * @author flyleft
 * @date 2018/4/8
 */
@RestController
public class EventBackCheckController implements EventBackCheckControllerInter {

    @Override
    public String backCheck() {
        return "/hello";
    }

}
