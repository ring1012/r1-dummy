package com.unisound.ant.device.bean;

import java.util.UUID;

/* loaded from: classes.dex */
public class NoteInfo {
    private String createTime;
    private String id = UUID.randomUUID().toString();
    private String msg;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
