package com.unisound.ant.device.bean;

/* loaded from: classes.dex */
public class DevicePlayingType {
    private String currentState;

    public DevicePlayingType(String currentState) {
        this.currentState = currentState;
    }

    public String getCurrentState() {
        return this.currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }
}
