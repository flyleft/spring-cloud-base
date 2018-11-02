package me.flyleft.eureka.client;

import me.flyleft.eureka.client.event.AbstractEurekaEventObserver;
import me.flyleft.eureka.client.event.EurekaEventPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CustomAbstractEurekaEventObserver extends AbstractEurekaEventObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAbstractEurekaEventObserver.class);

    public CustomAbstractEurekaEventObserver() {
        super(10, 5);
    }

    @Override
    public void receiveUpEvent(EurekaEventPayload payload) {
        LOGGER.info("======UP, id: {}", payload.getId());
    }

    @Override
    public void receiveDownEvent(EurekaEventPayload payload) {
        LOGGER.info("======DOWN, id: {}", payload.getId());
    }

}
