package me.jcala.eureka.event.producer.service;

import io.choerodon.core.exception.CommonException;
import io.choerodon.event.producer.execute.EventMessage;
import io.choerodon.event.producer.execute.EventProducerTemplate;
import me.jcala.eureka.event.producer.domain.Order;
import me.jcala.eureka.event.producer.domain.RepertoryPayload;
import me.jcala.eureka.event.producer.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        RepertoryPayload payload = new RepertoryPayload("apple", 3);

        boolean result = producerTemplate.execute("order","order-topic", payload,
                (String uuid, List<EventMessage> messageList) -> {
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

    @Override
    public boolean exist(String uuid) {
        Order order = new Order();
        order.setUuid(uuid);
        return orderMapper.selectCount(order) > 0;
    }
}
