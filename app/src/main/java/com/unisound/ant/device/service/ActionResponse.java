package com.unisound.ant.device.service;

/* loaded from: classes.dex */
public class ActionResponse {
    private String actionResponseId;
    private int actionStatus;
    private String actionTimestamp;
    private String detailInfo;

    public String getActionResponseId() {
        return this.actionResponseId;
    }

    public void setActionResponseId(String actionResponseId) {
        this.actionResponseId = actionResponseId;
    }

    public String getActionTimestamp() {
        return this.actionTimestamp;
    }

    public void setActionTimestamp(String actionTimestamp) {
        this.actionTimestamp = actionTimestamp;
    }

    public int getActionStatus() {
        return this.actionStatus;
    }

    public void setActionStatus(int actionStatus) {
        this.actionStatus = actionStatus;
    }

    public String getDetailInfo() {
        return this.detailInfo;
    }

    public void setDetailInfo(String detailInfo) {
        this.detailInfo = detailInfo;
    }

    public String toString() {
        return "ActionResponse{actionResponseId='" + this.actionResponseId + "', actionTimestamp='" + this.actionTimestamp + "', actionStatus=" + this.actionStatus + ", detailInfo='" + this.detailInfo + "'}";
    }
}
