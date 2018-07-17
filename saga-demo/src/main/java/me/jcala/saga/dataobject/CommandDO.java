package me.jcala.saga.dataobject;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "Command")
public class CommandDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long surrogateId;

    private long eventId;
    private String serviceName;
    private String instanceId;
    private String globalTxId;
    private String localTxId;
    private String parentTxId;
    private String compensationMethod;
    private byte[] payloads;
    private String status;

    private Date lastModified;

    public Long getSurrogateId() {
        return surrogateId;
    }

    public void setSurrogateId(Long surrogateId) {
        this.surrogateId = surrogateId;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
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

    public String getCompensationMethod() {
        return compensationMethod;
    }

    public void setCompensationMethod(String compensationMethod) {
        this.compensationMethod = compensationMethod;
    }

    public byte[] getPayloads() {
        return payloads;
    }

    public void setPayloads(byte[] payloads) {
        this.payloads = payloads;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

}
