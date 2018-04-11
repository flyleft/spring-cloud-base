package me.jcala.eureka.event.producer.domain;

/**
 * @author flyleft
 * @date 2018/4/10
 */
public class MoneyPayload {

    private long moneyFromUserId;

    private long moneyToUserId;

    private long account;

    public MoneyPayload() {
    }

    public long getMoneyFromUserId() {
        return moneyFromUserId;
    }

    public void setMoneyFromUserId(long moneyFromUserId) {
        this.moneyFromUserId = moneyFromUserId;
    }

    public long getMoneyToUserId() {
        return moneyToUserId;
    }

    public void setMoneyToUserId(long moneyToUserId) {
        this.moneyToUserId = moneyToUserId;
    }

    public long getAccount() {
        return account;
    }

    public void setAccount(long account) {
        this.account = account;
    }

    public MoneyPayload(long moneyFromUserId, long moneyToUserId, long account) {
        this.moneyFromUserId = moneyFromUserId;
        this.moneyToUserId = moneyToUserId;
        this.account = account;
    }
}
