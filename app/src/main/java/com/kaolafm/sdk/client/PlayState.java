package com.kaolafm.sdk.client;

/* loaded from: classes.dex */
public enum PlayState {
    PLAYING(1),
    PAUSED(0);

    private int code;

    PlayState(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
