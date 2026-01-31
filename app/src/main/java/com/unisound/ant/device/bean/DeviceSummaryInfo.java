package com.unisound.ant.device.bean;

/* loaded from: classes.dex */
public class DeviceSummaryInfo extends Parameter {
    String deviceNickName;
    String environmentLocation;

    public DeviceSummaryInfo(String deviceNickName, String environmentLocation) {
        this.deviceNickName = deviceNickName;
        this.environmentLocation = environmentLocation;
    }

    public String getDeviceNickName() {
        return this.deviceNickName;
    }

    public void setDeviceNickName(String deviceNickName) {
        this.deviceNickName = deviceNickName;
    }

    public String getEnvironmentLocation() {
        return this.environmentLocation;
    }

    public void setEnvironmentLocation(String environmentLocation) {
        this.environmentLocation = environmentLocation;
    }
}
