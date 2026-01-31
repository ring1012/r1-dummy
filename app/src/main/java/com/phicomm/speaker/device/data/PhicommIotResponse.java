package com.phicomm.speaker.device.data;

/* loaded from: classes.dex */
public class PhicommIotResponse {
    private String message;
    private int status;
    private String text;

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
