package com.phicomm.speaker.device.custom.message;

import android.os.Parcelable;

/* loaded from: classes.dex */
public class PhicommMessage {
    private Parcelable data;
    private int msgId;
    private int replyType;
    private int subReplyType;
    private int subType;
    private int type;

    public PhicommMessage(int type, int subType) {
        this.type = type;
        this.subType = subType;
    }

    public PhicommMessage(int type, int subType, int msgId) {
        this.type = type;
        this.subType = subType;
        this.msgId = msgId;
    }

    public PhicommMessage(int type, int subType, int msgId, Parcelable data) {
        this.type = type;
        this.subType = subType;
        this.msgId = msgId;
        this.data = data;
    }

    public PhicommMessage(int type, int subType, int msgId, Parcelable data, int replyType, int subReplyType) {
        this.type = type;
        this.subType = subType;
        this.msgId = msgId;
        this.data = data;
        this.replyType = replyType;
        this.subReplyType = subReplyType;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSubType() {
        return this.subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }

    public int getMsgId() {
        return this.msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public Parcelable getData() {
        return this.data;
    }

    public void setData(Parcelable data) {
        this.data = data;
    }

    public int getReplyType() {
        return this.replyType;
    }

    public void setReplyType(int replyType) {
        this.replyType = replyType;
    }

    public int getSubReplyType() {
        return this.subReplyType;
    }

    public void setSubReplyType(int subReplyType) {
        this.subReplyType = subReplyType;
    }

    public String toString() {
        return "PhicommMessage{type=" + this.type + ", subType=" + this.subType + ", msgId=" + this.msgId + ", data=" + this.data + ", replyType=" + this.replyType + ", subReplyType=" + this.subReplyType + '}';
    }
}
