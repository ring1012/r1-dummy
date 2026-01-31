package com.unisound.ant.device.mqtt.bean;

/* loaded from: classes.dex */
public class ChangeMessage extends SupMessage {
    private String msgId;
    private String msgType;
    private long ts;

    public String getMsgId() {
        return this.msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public long getTs() {
        return this.ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public String getMsgType() {
        return this.msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }
}
