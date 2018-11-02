package me.flyleft.eureka.client.event;

import com.netflix.appinfo.InstanceInfo;
import me.flyleft.eureka.client.endpoint.EurekaEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import rx.schedulers.Schedulers;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 监听eureka实例启动事件的观察者类
 */
public abstract class AbstractEurekaEventObserver implements Observer, EurekaEventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEurekaEventObserver.class);

    private final LinkedList<EurekaEventPayload> eventCache = new SynchronizedLinkedList<>(new LinkedList<>());

    private int retryTime;

    private int retryInterval;

    private int maxCacheSize;

    public AbstractEurekaEventObserver(int retryTime,
                                       int retryInterval,
                                       int maxCacheSize) {
        this.retryTime = retryTime;
        this.retryInterval = retryInterval;
        this.maxCacheSize = maxCacheSize;
        EurekaEventHandler.getInstance().getObservable().addObserver(this);
    }

    @Override
    public List<EurekaEventPayload> unfinishedEvents(String service) {
        if (StringUtils.isEmpty(service)) {
            return eventCache;
        }
        List<EurekaEventPayload> payloads = new ArrayList<>(4);
        for (EurekaEventPayload payload : eventCache) {
            if (service.equalsIgnoreCase(payload.getAppName())) {
                payloads.add(payload);
            }
        }
        return payloads;
    }

    @Override
    public List<EurekaEventPayload> retryEvents(String id, String service) {
        if (!StringUtils.isEmpty(id)) {
            return Collections.singletonList(manualRetryEventById(id));
        }
        if (!StringUtils.isEmpty(service)) {
            return manualRetryEventsByService(service);
        }
        return manualRetryAllEvents();
    }

    private EurekaEventPayload manualRetryEventById(String id) {
        List<EurekaEventPayload> payloads = new ArrayList<>(eventCache);
        for (EurekaEventPayload payload : payloads) {
            if (id.equals(payload.getId())) {
                consumerEvent(payload);
                return payload;
            }
        }
        return null;
    }

    private List<EurekaEventPayload> manualRetryEventsByService(String service) {
        List<EurekaEventPayload> returnPayloads = new ArrayList<>();
        List<EurekaEventPayload> payloads = new ArrayList<>(eventCache);
        for (EurekaEventPayload payload : payloads) {
            if (service.equalsIgnoreCase(payload.getAppName())) {
                consumerEvent(payload);
                returnPayloads.add(payload);
            }
        }
        return returnPayloads;
    }

    private List<EurekaEventPayload> manualRetryAllEvents() {
        List<EurekaEventPayload> returnPayloads = new ArrayList<>();
        List<EurekaEventPayload> payloads = new ArrayList<>(eventCache);
        for (EurekaEventPayload payload : payloads) {
            consumerEvent(payload);
            returnPayloads.add(payload);
        }
        return returnPayloads;
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
            putPayloadInCache(payload);
            consumerEventWithAutoRetry(payload);
        }
    }


    private void putPayloadInCache(final EurekaEventPayload payload) {
        if (eventCache.size() >= maxCacheSize) {
            LOGGER.info("Remove first payload because of maxCacheSize limit, remove payload: {}", eventCache.getFirst());
            eventCache.removeFirst();
        }
        eventCache.add(payload);
    }

    private void consumerEvent(final EurekaEventPayload payload) {
        if (InstanceInfo.InstanceStatus.UP.name().equals(payload.getStatus())) {
            LOGGER.debug("Consumer UP event, payload: {}", payload);
            receiveUpEvent(payload);
        } else {
            LOGGER.debug("Consumer DOWN event, payload: {}", payload);
            receiveDownEvent(payload);
        }
        eventCache.remove(payload);
    }

    private void consumerEventWithAutoRetry(final EurekaEventPayload payload) {
        rx.Observable.just(payload)
                .map(t -> {
                    consumerEvent(payload);
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
