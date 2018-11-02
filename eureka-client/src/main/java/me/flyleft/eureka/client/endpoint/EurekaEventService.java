package me.flyleft.eureka.client.endpoint;

import me.flyleft.eureka.client.event.EurekaEventPayload;

import java.util.List;

public interface EurekaEventService {

    List<EurekaEventPayload> unfinishedEvents(String service);

    List<EurekaEventPayload> retryEvents(String id, String service);

}
