package me.jcala.eureka.event.consumer.domain;

/**
 * @author flyleft
 * @date 2018/4/10
 */
public class RepertoryPayload {

    private String type;

    private int reduceNum;

    private long orderId;

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

    public RepertoryPayload() {
    }

    public RepertoryPayload(String type, int reduceNum) {
        this.type = type;
        this.reduceNum = reduceNum;
    }

    @Override
    public String toString() {
        return "RepertoryPayload{" +
                "type='" + type + '\'' +
                ", reduceNum=" + reduceNum +
                ", orderId=" + orderId +
                '}';
    }
}
