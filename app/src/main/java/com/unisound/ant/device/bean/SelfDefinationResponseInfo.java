package com.unisound.ant.device.bean;

/* loaded from: classes.dex */
public class SelfDefinationResponseInfo<T> {
    private T content;
    private String operationType;
    private int status;

    public SelfDefinationResponseInfo(String operationType, int status) {
        this.operationType = operationType;
        this.status = status;
    }

    public SelfDefinationResponseInfo(String operationType, int status, T content) {
        this.operationType = operationType;
        this.status = status;
        this.content = content;
    }

    public String getOperationType() {
        return this.operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getContent() {
        return this.content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
