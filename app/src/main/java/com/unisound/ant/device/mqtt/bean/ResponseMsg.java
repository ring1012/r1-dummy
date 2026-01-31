package com.unisound.ant.device.mqtt.bean;

/* loaded from: classes.dex */
public class ResponseMsg {
    private String clientId;
    private int eventType;
    private String msgId;
    private String sendStatus;

    public int getEventType() {
        return this.eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public String getClientId() {
        return this.clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSendStatus() {
        return this.sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getMsgId() {
        return this.msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}
