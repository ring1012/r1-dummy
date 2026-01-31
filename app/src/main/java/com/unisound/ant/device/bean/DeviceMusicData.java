package com.unisound.ant.device.bean;

import java.util.List;
import nluparser.scheme.MusicResult;

/* loaded from: classes.dex */
public class DeviceMusicData {
    private int index;
    private List<MusicResult.Music> musicList;

    public DeviceMusicData(int index, List<MusicResult.Music> musicList) {
        this.index = index;
        this.musicList = musicList;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<MusicResult.Music> getMusicList() {
        return this.musicList;
    }

    public void setMusicList(List<MusicResult.Music> musicList) {
        this.musicList = musicList;
    }
}
