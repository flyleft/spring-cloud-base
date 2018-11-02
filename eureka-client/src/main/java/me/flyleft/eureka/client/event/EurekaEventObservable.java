package me.flyleft.eureka.client.event;

import java.util.Observable;

public class EurekaEventObservable extends Observable {

    public void sendEvent(EurekaEventPayload payload) {
        setChanged();
        notifyObservers(payload);
    }
}
