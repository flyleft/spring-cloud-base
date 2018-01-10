package me.jcala.consumer.ribbon;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Created by jiatong.li on 10/10/17.
 */
@FeignClient(name = "${event.store.service.name:hap-event-store-service}")
public interface EventClient {
    @PostMapping("/v1/events")
    String createEvent(@RequestBody String event);

    @PutMapping("/v1/events/{eventId}/confirm")
    void confirmEvent(@PathVariable("eventId") String eventId);

    @PutMapping("/v1/events/{eventId}/cancel")
    void cancelEvent(@PathVariable("eventId") String eventId);
}