package me.flyleft.eureka.client.instance;

import com.netflix.appinfo.InstanceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Observable;
import java.util.Observer;

/**
 * 监听eureka实例启动事件的观察者类
 */
public abstract class AbstractEurekaInstanceObserver implements Observer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEurekaInstanceObserver.class);

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof CloudInstanceChangePayload) {
            CloudInstanceChangePayload payload = (CloudInstanceChangePayload) arg;
            if (InstanceInfo.InstanceStatus.UP.name().equals(payload.getStatus())) {
                LOGGER.info("Receive instance up message, payload: {}", payload);
                receiveUp(payload);
            } else {
                LOGGER.info("Receive instance down message, payload: {}", payload);
                receiveDown(payload);
            }
        }

    }

    public abstract void receiveUp(CloudInstanceChangePayload payload);

    public abstract void receiveDown(CloudInstanceChangePayload payload);
}
