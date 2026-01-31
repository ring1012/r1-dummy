package com.unisound.ant.platform.smarthome.bean;

import java.util.List;
import nluparser.scheme.MusicResult;

/* loaded from: classes.dex */
public class SmartAction {
    private String anchorTime;
    private String datatype;
    private String device;
    private String deviceCode;
    private String deviceExpr;
    private String deviceType;
    private String duration;
    private String endTime;
    private String home;
    private String homeExpr;
    private String offsetTime;
    private String operands;
    private String operator;
    private String percentValue;
    private String percentValueDelta;
    private String repeat;
    private String room;
    private String roomExpr;
    private String roomType;
    private String time;
    private String timeDelta;
    private String timeExpr;
    private List<MusicResult.Music> value;
    private String valueDelta;
    private String vendorName;
    private String zone;
    private String zoneExpr;
    private String zoneType;

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperands() {
        return this.operands;
    }

    public void setOperands(String operands) {
        this.operands = operands;
    }

    public List<MusicResult.Music> getValue() {
        return this.value;
    }

    public void setValue(List<MusicResult.Music> value) {
        this.value = value;
    }

    public String getValueDelta() {
        return this.valueDelta;
    }

    public void setValueDelta(String valueDelta) {
        this.valueDelta = valueDelta;
    }

    public String getDatatype() {
        return this.datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public String getPercentValue() {
        return this.percentValue;
    }

    public void setPercentValue(String percentValue) {
        this.percentValue = percentValue;
    }

    public String getPercentValueDelta() {
        return this.percentValueDelta;
    }

    public void setPercentValueDelta(String percentValueDelta) {
        this.percentValueDelta = percentValueDelta;
    }

    public String getHome() {
        return this.home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getHomeExpr() {
        return this.homeExpr;
    }

    public void setHomeExpr(String homeExpr) {
        this.homeExpr = homeExpr;
    }

    public String getZone() {
        return this.zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getZoneType() {
        return this.zoneType;
    }

    public void setZoneType(String zoneType) {
        this.zoneType = zoneType;
    }

    public String getZoneExpr() {
        return this.zoneExpr;
    }

    public void setZoneExpr(String zoneExpr) {
        this.zoneExpr = zoneExpr;
    }

    public String getRoom() {
        return this.room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getRoomType() {
        return this.roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomExpr() {
        return this.roomExpr;
    }

    public void setRoomExpr(String roomExpr) {
        this.roomExpr = roomExpr;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimeDelta() {
        return this.timeDelta;
    }

    public void setTimeDelta(String timeDelta) {
        this.timeDelta = timeDelta;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRepeat() {
        return this.repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTimeExpr() {
        return this.timeExpr;
    }

    public void setTimeExpr(String timeExpr) {
        this.timeExpr = timeExpr;
    }

    public String getOffsetTime() {
        return this.offsetTime;
    }

    public void setOffsetTime(String offsetTime) {
        this.offsetTime = offsetTime;
    }

    public String getAnchorTime() {
        return this.anchorTime;
    }

    public void setAnchorTime(String anchorTime) {
        this.anchorTime = anchorTime;
    }

    public String getDevice() {
        return this.device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getDeviceExpr() {
        return this.deviceExpr;
    }

    public void setDeviceExpr(String deviceExpr) {
        this.deviceExpr = deviceExpr;
    }

    public String getDeviceType() {
        return this.deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

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
}
