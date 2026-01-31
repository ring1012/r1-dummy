package com.unisound.ant.device.mqtt.bean;

/* loaded from: classes.dex */
public class OnlineMessage extends SupMessage {
    private String clientId;
    private ClientInfo clientInfo;
    private int eventType;

    public String getClientId() {
        return this.clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public int getEventType() {
        return this.eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public ClientInfo getClientInfo() {
        return this.clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }
}
