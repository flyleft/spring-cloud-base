package event.consumer;

/**
 * @author flyleft
 * @date 2018/4/10
 */
public class EventPayload <T> {

    private String uuid;

    private String type;

    private T t;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    @Override
    public String toString() {
        return "EventPayload{" +
                "uuid='" + uuid + '\'' +
                ", type='" + type + '\'' +
                ", t=" + t +
                '}';
    }
}