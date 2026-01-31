package com.unisound.ant.platform.smarthome.bean;

/* loaded from: classes.dex */
public class SmartDeviceStatus {
    private String deviceCode;
    private String deviceName;
    private String deviceType;
    private String homeCode;
    private String homeName;
    private String roomCode;
    private String roomName;
    private SmartStateParamter stateInfo;
    private String vendorName;
    private String zoneCode;
    private String zoneName;

    public String getDeviceCode() {
        return this.deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getVendorName() {
        return this.vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public SmartStateParamter getStateInfo() {
        return this.stateInfo;
    }

    public void setStateInfo(SmartStateParamter stateInfo) {
        this.stateInfo = stateInfo;
    }

    public String getHomeName() {
        return this.homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public String getHomeCode() {
        return this.homeCode;
    }

    public void setHomeCode(String homeCode) {
        this.homeCode = homeCode;
    }

    public String getZoneName() {
        return this.zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getZoneCode() {
        return this.zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public String getRoomName() {
        return this.roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomCode() {
        return this.roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getDeviceType() {
        return this.deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}
