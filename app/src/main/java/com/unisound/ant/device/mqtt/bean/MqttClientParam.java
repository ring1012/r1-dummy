package com.unisound.ant.device.mqtt.bean;

import java.util.Arrays;

/* loaded from: classes.dex */
public class MqttClientParam {
    private String clientid;
    private String connectUrl;
    private String passWord;
    private String publish;
    private String[] subscribe;
    private String userName;

    public String getConnectUrl() {
        return this.connectUrl;
    }

    public void setConnectUrl(String connectUrl) {
        this.connectUrl = connectUrl;
    }

    public String getClientid() {
        return this.clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return this.passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String[] getSubscribe() {
        return this.subscribe;
    }

    public void setSubscribe(String[] subscribe) {
        this.subscribe = subscribe;
    }

    public String getPublish() {
        return this.publish;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }

    public String toString() {
        return "MqttClientParam{connectUrl='" + this.connectUrl + "', clientid='" + this.clientid + "', userName='" + this.userName + "', passWord='" + this.passWord + "', subscribe=" + Arrays.toString(this.subscribe) + ", publish='" + this.publish + "'}";
    }
}
