package com.unisound.ant.device.bean;

import java.util.List;

/* loaded from: classes.dex */
public class PhicommPlayerInfo {
    private int playIndex;
    private List<MusicItem> playList;
    private int playMode;
    private int playState;

    public int getPlayMode() {
        return this.playMode;
    }

    public void setPlayMode(int playMode) {
        this.playMode = playMode;
    }

    public int getPlayState() {
        return this.playState;
    }

    public void setPlayState(int playState) {
        this.playState = playState;
    }

    public int getPlayIndex() {
        return this.playIndex;
    }

    public void setPlayIndex(int playIndex) {
        this.playIndex = playIndex;
    }

    public List<MusicItem> getPlayList() {
        return this.playList;
    }

    public void setPlayList(List<MusicItem> playList) {
        this.playList = playList;
    }
}
