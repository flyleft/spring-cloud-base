package me.flyleft.eureka.client.event;

import com.netflix.appinfo.InstanceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.web.client.RestClientException;
import rx.schedulers.Schedulers;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 监听eureka实例启动事件的观察者类
 */
public abstract class AbstractEurekaEventObserver implements Observer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEurekaEventObserver.class);

    private final List<EurekaEventPayload> eventCache = Collections.synchronizedList(new LinkedList<>());

    private int retryTime;

    private int retryInterval;

    public AbstractEurekaEventObserver(int retryTime, int retryInterval) {
        this.retryTime = retryTime;
        this.retryInterval = retryInterval;
        EurekaEventHandler.getInstance().getObservable().addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof EurekaEventPayload) {
            EurekaEventPayload payload = (EurekaEventPayload) arg;
            if (InstanceInfo.InstanceStatus.UP.name().equals(payload.getStatus())) {
                LOGGER.info("Receive UP event, payload: {}", payload);
            } else {
                LOGGER.info("Receive DOWN event, payload: {}", payload);
            }
            eventCache.add(payload);
            consumerEvent(payload);
        }
    }

    private void consumerEvent(final EurekaEventPayload payload) {
        rx.Observable.just(payload)
                .map(t -> {
                    if (InstanceInfo.InstanceStatus.UP.name().equals(payload.getStatus())) {
                        LOGGER.debug("Consumer UP event, payload: {}", payload);
                        receiveUpEvent(payload);
                    } else {
                        LOGGER.debug("Consumer DOWN event, payload: {}", payload);
                        receiveDownEvent(payload);
                    }
                    eventCache.remove(payload);
                    return payload;
                }).retryWhen(x -> x.zipWith(rx.Observable.range(1, retryTime),
                (t, retryCount) -> {
                    if (retryCount >= retryTime) {
                        if (t instanceof RemoteAccessException || t instanceof RestClientException) {
                            LOGGER.warn("error.eurekaEventObserver.fetchError, payload {}", payload, t);
                        } else {
                            LOGGER.warn("error.eurekaEventObserver.consumerError, payload {}", payload, t);
                        }
                    }
                    return retryCount;
                }).flatMap(y -> rx.Observable.timer(retryInterval, TimeUnit.SECONDS)))
                .subscribeOn(Schedulers.io())
                .subscribe((EurekaEventPayload payload1) -> {
                });
    }

    public abstract void receiveUpEvent(EurekaEventPayload payload);

    public abstract void receiveDownEvent(EurekaEventPayload payload);

}
