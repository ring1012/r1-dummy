package com.unisound.ant.device.bean;

import java.util.HashMap;

/* loaded from: classes.dex */
public class PhicommStatisticInfo {
    private HashMap<String, Object> data;
    private String from;
    private String key;
    private String msgId;

    public PhicommStatisticInfo(String msgId, String key, String from, HashMap<String, Object> data) {
        this.msgId = msgId;
        this.key = key;
        this.from = from;
        this.data = data;
    }

    public String getMsgId() {
        return this.msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public HashMap<String, Object> getData() {
        return this.data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }
}
