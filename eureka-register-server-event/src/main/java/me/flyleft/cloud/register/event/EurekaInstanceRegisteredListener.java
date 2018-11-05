package me.flyleft.cloud.register.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.appinfo.InstanceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class EurekaInstanceRegisteredListener implements ApplicationListener<EurekaInstanceRegisteredEvent> {

    private static final Logger logger = LoggerFactory.getLogger(EurekaInstanceRegisteredListener.class);

    private static final String INSTANCE_CHANGE_TOPIC_NAME = "register-server";

    private static final List<String> FILTER_SERVICES = Arrays.asList("api-gateway", "register-server", "eureka-server");

    private ObjectMapper objectMapper = new ObjectMapper();


    @Autowired
    private KafkaTemplate<byte[], byte[]> kafkaTemplate;

    @Override
    public void onApplicationEvent(EurekaInstanceRegisteredEvent eurekaInstanceRegisteredEvent) {
        if (eurekaInstanceRegisteredEvent.isReplication()) {
            try {
                InstanceInfo instanceInfo = eurekaInstanceRegisteredEvent.getInstanceInfo();
                boolean matchFilter = FILTER_SERVICES.stream().anyMatch(t -> instanceInfo.getAppName().toLowerCase().equals(t));
                if (matchFilter) {
                    return;
                }
                EurekaEventPayload instance = new EurekaEventPayload(instanceInfo);
                String message = objectMapper.writeValueAsString(instance);
                kafkaTemplate.send(INSTANCE_CHANGE_TOPIC_NAME, message.getBytes());
                logger.info("a instance up {}", message);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

}
