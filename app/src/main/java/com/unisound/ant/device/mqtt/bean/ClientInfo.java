package com.unisound.ant.device.mqtt.bean;

/* loaded from: classes.dex */
public class ClientInfo {
    private String appKey;
    private String extras;
    private long passportId;
    private int subsystemId;
    private String udid;

    public String getExtras() {
        return this.extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

    public String getAppKey() {
        return this.appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getUdid() {
        return this.udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public int getSubsystemId() {
        return this.subsystemId;
    }

    public void setSubsystemId(int subsystemId) {
        this.subsystemId = subsystemId;
    }

    public long getPassportId() {
        return this.passportId;
    }

    public void setPassportId(long passportId) {
        this.passportId = passportId;
    }
}
