package com.unisound.ant.device.bean;

/* loaded from: classes.dex */
public class SceneModeInfo {
    private boolean isOpen;
    private String modeState;
    private String modeTip;
    private String modeType;

    public String getModeType() {
        return this.modeType;
    }

    public void setModeType(String modeType) {
        this.modeType = modeType;
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    public void setOpen(boolean open) {
        this.isOpen = open;
    }

    public String getModeState() {
        return this.modeState;
    }

    public void setModeState(String modeState) {
        this.modeState = modeState;
    }

    public String getModeTip() {
        return this.modeTip;
    }

    public void setModeTip(String modeTip) {
        this.modeTip = modeTip;
    }

    public String toString() {
        return "SceneModeInfo{modeType='" + this.modeType + "', isOpen=" + this.isOpen + ", modeState='" + this.modeState + "', modeTip='" + this.modeTip + "'}";
    }
}
