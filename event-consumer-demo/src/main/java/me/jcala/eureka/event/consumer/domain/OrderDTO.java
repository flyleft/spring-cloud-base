package me.jcala.eureka.event.consumer.domain;

/**
 * @author flyleft
 * @date 2018/4/9
 */
public class OrderDTO {

    private String uuid;

    private String type;

    private int reduceNum;

    private long orderId;

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

    public int getReduceNum() {
        return reduceNum;
    }

    public void setReduceNum(int reduceNum) {
        this.reduceNum = reduceNum;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
}
