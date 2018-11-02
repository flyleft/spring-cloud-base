package me.flyleft.eureka.client.event;

public class EurekaEventException extends RuntimeException {

    public EurekaEventException(Throwable cause) {
        super("Add eureka event error By javassist", cause);
    }
}
