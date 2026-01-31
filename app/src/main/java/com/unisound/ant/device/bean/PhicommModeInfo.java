package com.unisound.ant.device.bean;

/* loaded from: classes.dex */
public class PhicommModeInfo {
    public static final int MODE_LIST_LOOP = 2;
    public static final int MODE_LIST_SINGLE_LOOP = 3;
    public static final int MODE_RANDOM = 1;
    private int playMode;

    public int getPlayMode() {
        return this.playMode;
    }

    public void setPlayMode(int playMode) {
        this.playMode = playMode;
    }
}
