package com.unisound.ant.device.bean;

/* loaded from: classes.dex */
public class AccessoryInfo {
    private String controllerFlag;
    private String token;

    public AccessoryInfo(String controllerFlag, String token) {
        this.controllerFlag = controllerFlag;
        this.token = token;
    }

    public String getControllerFlag() {
        return this.controllerFlag;
    }

    public void setControllerFlag(String controllerFlag) {
        this.controllerFlag = controllerFlag;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
