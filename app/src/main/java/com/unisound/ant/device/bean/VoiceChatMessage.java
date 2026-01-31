package com.unisound.ant.device.bean;

/* loaded from: classes.dex */
public class VoiceChatMessage {
    private String createTime;
    private boolean isUserEdited;
    private String logId;
    private String msg;

    public String getLogId() {
        return this.logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean isUserEdited() {
        return this.isUserEdited;
    }

    public void setUserEdited(boolean userEdited) {
        this.isUserEdited = userEdited;
    }
}
