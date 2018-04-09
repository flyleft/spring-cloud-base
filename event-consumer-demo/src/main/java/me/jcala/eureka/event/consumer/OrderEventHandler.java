package me.jcala.eureka.event.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.choerodon.core.exception.CommonException;
import io.choerodon.event.consumer.annotation.Topic;
import me.jcala.eureka.event.consumer.domain.OrderDTO;
import me.jcala.eureka.event.consumer.domain.Repertory;
import me.jcala.eureka.event.consumer.mapper.RepertoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

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

    @Topic("order-topic")
    public void messgae(String message) {
        try {
            OrderDTO dto = mapper.readValue(message, OrderDTO.class);
            Repertory repertory = new Repertory();
            repertory.setType(dto.getType());
            Repertory selectRep = repertoryMapper.selectOne(repertory);
            if (selectRep == null) {
                throw new CommonException("error.repertory.notExist");
            }
            if (selectRep.getNum() < dto.getReduceNum()) {
                throw new CommonException("error.repertory.notEnough");
            }
            selectRep.setNum(selectRep.getNum() - dto.getReduceNum());
            repertoryMapper.updateByPrimaryKey(selectRep);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
