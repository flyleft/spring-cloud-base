package me.flyleft.eureka.client;

import me.flyleft.eureka.client.instance.AbstractEurekaInstanceObserver;
import me.flyleft.eureka.client.instance.CloudInstanceChangePayload;
import me.flyleft.eureka.client.instance.EurekaListenerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CustomEurekaInstanceObserver extends AbstractEurekaInstanceObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomEurekaInstanceObserver.class);

    public CustomEurekaInstanceObserver() {
        EurekaListenerHandler.getInstance().getObservable().addObserver(this);
    }

    @Override
    public void receiveUp(CloudInstanceChangePayload payload) {
        LOGGER.info("======UP");

    }

    @Override
    public void receiveDown(CloudInstanceChangePayload payload) {
        LOGGER.info("======DOWN");
    }

}
