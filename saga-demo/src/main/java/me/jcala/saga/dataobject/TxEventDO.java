package me.jcala.saga.dataobject;

import javax.persistence.*;
import java.util.Date;

@Table(name = "TxEvent")
public class TxEventDO {

    @Transient
    public static final long MAX_TIMESTAMP = 253402214400000L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long surrogateId;

    private String serviceName;
    private String instanceId;
    private Date creationTime;
    private String globalTxId;
    private String localTxId;
    private String parentTxId;
    private String type;
    private String compensationMethod;
    private Date expiryTime;
    private String retryMethod;
    private int retries;
    private byte[] payloads;

    private TxEventDO() {
    }

    public Long getSurrogateId() {
        return surrogateId;
    }

    public void setSurrogateId(Long surrogateId) {
        this.surrogateId = surrogateId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getGlobalTxId() {
        return globalTxId;
    }

    public void setGlobalTxId(String globalTxId) {
        this.globalTxId = globalTxId;
    }

    public String getLocalTxId() {
        return localTxId;
    }

    public void setLocalTxId(String localTxId) {
        this.localTxId = localTxId;
    }

    public String getParentTxId() {
        return parentTxId;
    }

    public void setParentTxId(String parentTxId) {
        this.parentTxId = parentTxId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompensationMethod() {
        return compensationMethod;
    }

    public void setCompensationMethod(String compensationMethod) {
        this.compensationMethod = compensationMethod;
    }

    public Date getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }

    public String getRetryMethod() {
        return retryMethod;
    }

    public void setRetryMethod(String retryMethod) {
        this.retryMethod = retryMethod;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public byte[] getPayloads() {
        return payloads;
    }

    public void setPayloads(byte[] payloads) {
        this.payloads = payloads;
    }
}
