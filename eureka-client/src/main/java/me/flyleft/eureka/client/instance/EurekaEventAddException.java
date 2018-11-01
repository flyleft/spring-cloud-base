package me.flyleft.eureka.client.instance;

public class EurekaEventAddException extends RuntimeException {

    public EurekaEventAddException(Throwable cause) {
        super("Add eureka event error By javassist", cause);
    }
}
