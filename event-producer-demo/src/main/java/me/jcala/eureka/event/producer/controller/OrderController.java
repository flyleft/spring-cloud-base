package me.jcala.eureka.event.producer.controller;

import me.jcala.eureka.event.producer.domain.Order;
import me.jcala.eureka.event.producer.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author flyleft
 * @date 2018/4/9
 */
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public void createOrder() {
        Order order = new Order();
        order.setName("" + Math.random());
        orderService.createOrder(order);
    }
}
