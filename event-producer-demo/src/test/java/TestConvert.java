import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.choerodon.core.exception.CommonException;
import io.choerodon.event.producer.execute.EventMessage;
import io.choerodon.event.producer.execute.EventSendMsg;
import me.jcala.eureka.event.producer.domain.RepertoryPayload;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author flyleft
 * @date 2018/4/9
 */
public class TestConvert {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestConvert.class);

    private ObjectMapper mapper = new ObjectMapper();


    private String convert(List<EventMessage> messages) {
        List<EventSendMsg> sendMsgs = messages.stream().map(t -> {
            String msg = null;
            try {
                msg = mapper.writeValueAsString(t.getPayload());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return new EventSendMsg(t.getTopic(), msg);
        }).filter(t -> t != null && t.getPayload() != null).collect(Collectors.toList());
        if (sendMsgs.size() != messages.size()) {
            throw new CommonException("error.eventProducerTemplate.convert");
        }
        try {
            return mapper.writeValueAsString(sendMsgs);
        } catch (Exception e) {
            LOGGER.warn("JsonProcessingException {}", e.getMessage());
            return null;
        }
    }

    @Test
    public void convertTest() {
        RepertoryPayload payload = new RepertoryPayload("apple", 3);

        EventMessage eventMessage = new EventMessage("orderTopic", payload);

        List<EventMessage> messages = Collections.singletonList(eventMessage);

        String json = convert(messages);

        System.out.println(json);

        List<EventSendMsg> list = convertSendMsg(json);

    }

    public List<EventSendMsg> convertSendMsg(String messages) {

        List<EventSendMsg> messageList = Collections.emptyList();
        try {
            messageList = mapper.readValue(messages, new TypeReference<List<EventSendMsg>>(){});
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.info("error.EventConvert.convertSendMsg");
        }
        return messageList;
    }


}
