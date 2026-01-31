package com.unisound.vui.message;

/* loaded from: classes.dex */
public class MessageBeanHandlerGui<T> {
    private String action;
    private T data;
    private int duration;
    private String extra;
    private String sessionName;
    private VoiceTip voiceTip;

    public String getAction() {
        return this.action;
    }

    public T getData() {
        return this.data;
    }

    public int getDuration() {
        return this.duration;
    }

    public String getExtra() {
        return this.extra;
    }

    public String getSessionName() {
        return this.sessionName;
    }

    public VoiceTip getVoiceTip() {
        return this.voiceTip;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public void setVoiceTip(VoiceTip voiceTip) {
        this.voiceTip = voiceTip;
    }
}
