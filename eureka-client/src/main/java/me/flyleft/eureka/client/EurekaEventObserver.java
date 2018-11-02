package me.flyleft.eureka.client;

import me.flyleft.eureka.client.event.AbstractEurekaEventObserver;
import me.flyleft.eureka.client.event.EurekaEventPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EurekaEventObserver extends AbstractEurekaEventObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(EurekaEventObserver.class);

    public EurekaEventObserver() {
        super(5, 5, 2 << 8);
    }

    @Override
    public void receiveUpEvent(EurekaEventPayload payload) {
        LOGGER.info("======UP, id: {}", payload.getId());
        if (payload.getAppName().equalsIgnoreCase("eureka-client")) {
            throw new RuntimeException("测试异常");
        }
    }

    @Override
    public void receiveDownEvent(EurekaEventPayload payload) {
        LOGGER.info("======DOWN, id: {}", payload.getId());
    }

}
