package me.jcala.eureka.event.producer.service;

import me.jcala.eureka.event.producer.domain.Order;

/**
 * @author flyleft
 * @date 2018/4/9
 */
public interface OrderService {

    void createOrder(Order order) ;

    boolean exist(String uuid);
}
