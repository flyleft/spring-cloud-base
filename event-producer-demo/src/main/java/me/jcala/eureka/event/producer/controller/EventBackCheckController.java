package me.jcala.eureka.event.producer.controller;

import io.choerodon.core.event.EventBackCheckRecord;
import io.choerodon.core.event.EventStatus;
import io.choerodon.event.producer.check.EventBackCheckControllerInter;
import me.jcala.eureka.event.producer.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import static me.jcala.eureka.event.producer.EventProducerApplication.EVENT_TYPE_ORDER;

/**
 * @author flyleft
 * @date 2018/4/9
 */
@RestController
public class EventBackCheckController implements EventBackCheckControllerInter {

    @Autowired
    private OrderService orderService;

    @Override
    public EventBackCheckRecord queryEventStatus(String uuid, String type) {
        boolean exist = false;
        if (EVENT_TYPE_ORDER.equals(type)) {
            exist = orderService.exist(uuid);
        }
        //...
        if (exist) {
            return new EventBackCheckRecord(uuid, EventStatus.CONFIRMED);
        }
        return new EventBackCheckRecord(uuid, EventStatus.CANCELED);
    }
}
