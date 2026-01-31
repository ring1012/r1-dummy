package nluparser.scheme;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public class SettingIntent implements Intent {

    @SerializedName("anchorTime")
    @JSONField(name = "anchorTime")
    String anchorTime;

    @SerializedName("confirm")
    @JSONField(name = "confirm")
    String confirm;

    @SerializedName("datatype")
    @JSONField(name = "datatype")
    String datatype;

    @SerializedName("device")
    @JSONField(name = "device")
    String device;

    @SerializedName("deviceExpr")
    @JSONField(name = "deviceExpr")
    String deviceExpr;

    @SerializedName("deviceType")
    @JSONField(name = "deviceType")
    String deviceType;

    @SerializedName("duration")
    @JSONField(name = "duration")
    String duration;

    @SerializedName("endTime")
    @JSONField(name = "endTime")
    String endTime;

    @SerializedName("home")
    @JSONField(name = "home")
    String home;

    @SerializedName("homeExpr")
    @JSONField(name = "homeExpr")
    String homeExpr;

    @SerializedName("offsetTime")
    @JSONField(name = "offsetTime")
    String offsetTime;

    @SerializedName("operands")
    @JSONField(name = "operands")
    String operands;

    @SerializedName("operator")
    @JSONField(name = "operator")
    String operator;

    @SerializedName("percentValue")
    @JSONField(name = "percentValue")
    String percentValue;

    @SerializedName("percentValueDelta")
    @JSONField(name = "percentValueDelta")
    String percentValueDelta;

    @SerializedName("repeat")
    @JSONField(name = "repeat")
    String repeat;

    @SerializedName("room")
    @JSONField(name = "room")
    String room;

    @SerializedName("roomExpr")
    @JSONField(name = "roomExpr")
    String roomExpr;

    @SerializedName("roomType")
    @JSONField(name = "roomType")
    String roomType;

    @SerializedName("time")
    @JSONField(name = "time")
    String time;

    @SerializedName("timeDelta")
    @JSONField(name = "timeDelta")
    String timeDelta;

    @SerializedName("timeExpr")
    @JSONField(name = "timeExpr")
    String timeExpr;

    @SerializedName("value")
    @JSONField(name = "value")
    String value;

    @SerializedName("valueDelta")
    @JSONField(name = "valueDelta")
    String valueDelta;

    @SerializedName("valueDeltaExpr")
    @JSONField(name = "valueDeltaExpr")
    String valueDeltaExpr;

    @SerializedName("valueExpr")
    @JSONField(name = "valueExpr")
    String valueExpr;

    @SerializedName("zone")
    @JSONField(name = "zone")
    String zone;

    @SerializedName("zoneExpr")
    @JSONField(name = "zoneExpr")
    String zoneExpr;

    @SerializedName("zoneType")
    @JSONField(name = "zoneType")
    String zoneType;

    public String getAnchorTime() {
        return this.anchorTime;
    }

    public String getConfirm() {
        return this.confirm;
    }

    public String getDatatype() {
        return this.datatype;
    }

    public String getDevice() {
        return this.device;
    }

    public String getDeviceExpr() {
        return this.deviceExpr;
    }

    public String getDeviceType() {
        return this.deviceType;
    }

    public String getDuration() {
        return this.duration;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public String getHome() {
        return this.home;
    }

    public String getHomeExpr() {
        return this.homeExpr;
    }

    public String getOffsetTime() {
        return this.offsetTime;
    }

    public String getOperands() {
        return this.operands;
    }

    public String getOperator() {
        return this.operator;
    }

    public String getPercentValue() {
        return this.percentValue;
    }

    public String getPercentValueDelta() {
        return this.percentValueDelta;
    }

    public String getRepeat() {
        return this.repeat;
    }

    public String getRoom() {
        return this.room;
    }

    public String getRoomExpr() {
        return this.roomExpr;
    }

    public String getRoomType() {
        return this.roomType;
    }

    public String getTime() {
        return this.time;
    }

    public String getTimeDelta() {
        return this.timeDelta;
    }

    public String getTimeExpr() {
        return this.timeExpr;
    }

    public String getValue() {
        return this.value;
    }

    public String getValueDelta() {
        return this.valueDelta;
    }

    public String getValueDeltaExpr() {
        return this.valueDeltaExpr;
    }

    public String getValueExpr() {
        return this.valueExpr;
    }

    public String getZone() {
        return this.zone;
    }

    public String getZoneExpr() {
        return this.zoneExpr;
    }

    public String getZoneType() {
        return this.zoneType;
    }

    public void setAnchorTime(String anchorTime) {
        this.anchorTime = anchorTime;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public void setDeviceExpr(String deviceExpr) {
        this.deviceExpr = deviceExpr;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public void setHomeExpr(String homeExpr) {
        this.homeExpr = homeExpr;
    }

    public void setOffsetTime(String offsetTime) {
        this.offsetTime = offsetTime;
    }

    public void setOperands(String operands) {
        this.operands = operands;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public void setPercentValue(String percentValue) {
        this.percentValue = percentValue;
    }

    public void setPercentValueDelta(String percentValueDelta) {
        this.percentValueDelta = percentValueDelta;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setRoomExpr(String roomExpr) {
        this.roomExpr = roomExpr;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTimeDelta(String timeDelta) {
        this.timeDelta = timeDelta;
    }

    public void setTimeExpr(String timeExpr) {
        this.timeExpr = timeExpr;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setValueDelta(String valueDelta) {
        this.valueDelta = valueDelta;
    }

    public void setValueDeltaExpr(String valueDeltaExpr) {
        this.valueDeltaExpr = valueDeltaExpr;
    }

    public void setValueExpr(String valueExpr) {
        this.valueExpr = valueExpr;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public void setZoneExpr(String zoneExpr) {
        this.zoneExpr = zoneExpr;
    }

    public void setZoneType(String zoneType) {
        this.zoneType = zoneType;
    }
}
