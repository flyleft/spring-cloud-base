package me.flyleft.cloud.register.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.netflix.appinfo.InstanceInfo;

import java.util.Date;

public class EurekaEventPayload {

    private static final String VERSION_STR = "VERSION";

    private static final String DEFAULT_VERSION_NAME = "unknown";

    private String status;

    private String appName;

    private String version;

    private String instanceAddress;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;

    public EurekaEventPayload(InstanceInfo instanceInfo) {
        this.status = instanceInfo.getStatus().name();
        this.appName = instanceInfo.getAppName().toLowerCase();
        this.version = instanceInfo.getMetadata().get(VERSION_STR);
        this.version = version == null ? DEFAULT_VERSION_NAME : version;
        this.instanceAddress = instanceInfo.getIPAddr() + ":" + instanceInfo.getPort();
        this.createTime = new Date();
    }

    public EurekaEventPayload() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getInstanceAddress() {
        return instanceAddress;
    }

    public void setInstanceAddress(String instanceAddress) {
        this.instanceAddress = instanceAddress;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Instance{" +
                "status='" + status + '\''
                + ", appName='" + appName + '\''
                + ", version='" + version + '\''
                + ", instanceAddress='" + instanceAddress + '\''
                + ", createTime=" + createTime
                + '}';
    }
}
