package com.unisound.ant.device.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public class AlarmReminder {
    public static final String MEMO_STATUS_CANCEL = "cancel";
    public static final String MEMO_STATUS_FINISH = "finished";
    public static final String MEMO_STATUS_RUNNING = "running";
    public static final String MEMO_STATUS_START = "start";

    @SerializedName("countDownTime")
    @JSONField(name = "countDownTime")
    private int countDown;
    private String date;
    private String id;
    private boolean isOpen;
    private String label;
    private String repeatDate;

    @SerializedName("alarmName")
    @JSONField(name = "alarmName")
    private String ringing;
    private String status;
    private String time;
    private String type;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRepeatDate() {
        return this.repeatDate;
    }

    public void setRepeatDate(String repeatDate) {
        this.repeatDate = repeatDate;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getRinging() {
        return this.ringing;
    }

    public void setRinging(String ringing) {
        this.ringing = ringing;
    }

    public int getCountDown() {
        return this.countDown;
    }

    public void setCountDown(int countDown) {
        this.countDown = countDown;
    }
}
