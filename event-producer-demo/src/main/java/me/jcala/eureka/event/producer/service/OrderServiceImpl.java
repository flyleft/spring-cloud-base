package me.jcala.eureka.event.producer.service;

import io.choerodon.core.exception.CommonException;
import io.choerodon.event.producer.execute.EventProducerTemplate;
import me.jcala.eureka.event.producer.domain.MoneyPayload;
import me.jcala.eureka.event.producer.domain.Order;
import me.jcala.eureka.event.producer.domain.RepertoryPayload;
import me.jcala.eureka.event.producer.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author flyleft
 * @date 2018/4/9
 */
@Service
public class OrderServiceImpl  implements OrderService {

    @Autowired
    private EventProducerTemplate producerTemplate;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public void createOrder(Order order) {
        createOrderOne(order);
        createOrderTwo(order);
    }

    private void createOrderOne(Order order) {
        RepertoryPayload payload = new RepertoryPayload("apple", 3);
        /**
         * producerType: 用于接口回查
         * consumerType: 用于消费时区分不同业务类型
         * topic： 以当前服务名命名。
         * 如果producerType和consumerType一样，可以调用execute(String type, String topic, Object payload, EventExecuter executer)
         */
        boolean result = producerTemplate.execute("order", "reduceStock" ,
                "event-producer-demo", payload,
                (String uuid) -> {
                    order.setUuid(uuid);
                    order.setName(uuid);
                    if (orderMapper.insert(order) != 1) {
                        throw new CommonException("error.order.create.insert");
                    }
                    //根据业务处理结果，重新设置payload的值
                    payload.setOrderId(order.getId());
                });

        if (!result) {
            throw new CommonException("error.order.create");
        }
    }

    private void createOrderTwo(Order order) {
        MoneyPayload moneyPayload = new MoneyPayload(10L, 8L, 233L);
        boolean result = producerTemplate.execute("order", "money" ,
                "event-producer-demo", moneyPayload,
                (String uuid) -> {
                    order.setId(null);
                    order.setUuid(uuid);
                    order.setName(uuid);
                    if (orderMapper.insert(order) != 1) {
                        throw new CommonException("error.order.create.insert");
                    }
                });

        if (!result) {
            throw new CommonException("error.order.create");
        }
    }

    @Override
    public boolean exist(String uuid) {
        Order order = new Order();
        order.setUuid(uuid);
        return orderMapper.selectCount(order) > 0;
    }
}
