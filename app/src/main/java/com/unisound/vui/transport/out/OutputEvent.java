package com.unisound.vui.transport.out;

/* loaded from: classes.dex */
public class OutputEvent<T> {
    private T data;
    private int type;

    public OutputEvent(int type) {
        this.type = type;
    }

    public OutputEvent(int type, T data) {
        this.type = type;
        this.data = data;
    }

    public T getData() {
        return this.data;
    }

    public int getType() {
        return this.type;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setType(int type) {
        this.type = type;
    }
}
