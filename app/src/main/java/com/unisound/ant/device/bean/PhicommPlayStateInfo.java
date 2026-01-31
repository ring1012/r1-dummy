package com.unisound.ant.device.bean;

/* loaded from: classes.dex */
public class PhicommPlayStateInfo {
    public static final int STATE_PAUSE = 2;
    public static final int STATE_PLAYING = 1;
    public static final int STATE_STOP = 0;
    private int playState;

    public int getPlayState() {
        return this.playState;
    }

    public void setPlayState(int playState) {
        this.playState = playState;
    }
}
