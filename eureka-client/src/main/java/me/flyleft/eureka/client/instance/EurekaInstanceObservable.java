package me.flyleft.eureka.client.instance;

import java.util.Observable;

public class EurekaInstanceObservable extends Observable {

    public void sendEvent(CloudInstanceChangePayload payload) {
        setChanged();
        notifyObservers(payload);
    }
}
