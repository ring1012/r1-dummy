package com.unisound.ant.device.bean;

/* loaded from: classes.dex */
public class OnOffLineMessage extends Parameter {
    String connAccessId;
    String phoneUdid;
    String tdid;
    String token;
    String udid;

    public String getUdid() {
        return this.udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getPhoneUdid() {
        return this.phoneUdid;
    }

    public void setPhoneUdid(String phoneUdid) {
        this.phoneUdid = phoneUdid;
    }

    public String getTdid() {
        return this.tdid;
    }

    public void setTdid(String tdid) {
        this.tdid = tdid;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getConnAccessId() {
        return this.connAccessId;
    }

    public void setConnAccessId(String connAccessId) {
        this.connAccessId = connAccessId;
    }
}
