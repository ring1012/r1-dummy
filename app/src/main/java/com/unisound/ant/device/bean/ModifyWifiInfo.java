package com.unisound.ant.device.bean;

/* loaded from: classes.dex */
public class ModifyWifiInfo {
    private String password;
    private String ssid;
    private String udid;

    public String getUdid() {
        return this.udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getSsid() {
        return this.ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
