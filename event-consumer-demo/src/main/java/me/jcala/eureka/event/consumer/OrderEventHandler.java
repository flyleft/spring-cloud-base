package me.jcala.eureka.event.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.choerodon.core.event.EventPayload;
import io.choerodon.core.exception.CommonException;
import io.choerodon.event.consumer.annotation.EventListener;
import me.jcala.eureka.event.consumer.domain.MoneyPayload;
import me.jcala.eureka.event.consumer.domain.PersonPayload;
import me.jcala.eureka.event.consumer.domain.Repertory;
import me.jcala.eureka.event.consumer.domain.RepertoryPayload;
import me.jcala.eureka.event.consumer.mapper.RepertoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author flyleft
 * @date 2018/4/9
 */
@Component
public class OrderEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderEventHandler.class);

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private RepertoryMapper repertoryMapper;


    @EventListener(topic = "event-producer-demo",
            businessType = "money")
    public void messssgae(EventPayload<MoneyPayload> payload) {
        MoneyPayload data = payload.getData();
        LOGGER.info("data: {}", data);
    }

    @EventListener(topic = "event-producer-demo",
            businessType = "reduceStock",
            retryTimes = 3,
            firstInterval = 30000,
            retryInterval = 10000)
    public void messgae(EventPayload<RepertoryPayload> payload) {
        RepertoryPayload data = payload.getData();
        LOGGER.info("data: {}", data);
        Repertory repertory = new Repertory();
        repertory.setItemType(data.getType());
        Repertory selectRep = repertoryMapper.selectOne(repertory);
        if (selectRep == null) {
            throw new CommonException("error.repertory.notExist");
        }
        if (selectRep.getNum() < data.getReduceNum()) {
            throw new CommonException("error.repertory.notEnough");
        }
        selectRep.setNum(selectRep.getNum() - data.getReduceNum());
        repertoryMapper.updateByPrimaryKey(selectRep);
    }

    @EventListener(topic = "event-producer-demo", businessType = "person")
    public void messssssgae(EventPayload<PersonPayload> payload) {
        PersonPayload data = payload.getData();
//        throw new RuntimeException("test");
        LOGGER.info("data: {}", data);
    }


}
