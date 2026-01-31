package com.unisound.ant.device.bean;

import java.util.List;
import nluparser.scheme.MusicResult;

/* loaded from: classes.dex */
public class RemoteMusicInfo {
    private List<MusicResult.Music> musicinfo;
    private String voiceTip;

    public List<MusicResult.Music> getMusicinfo() {
        return this.musicinfo;
    }

    public void setMusicinfo(List<MusicResult.Music> musicinfo) {
        this.musicinfo = musicinfo;
    }

    public String getVoiceTip() {
        return this.voiceTip;
    }

    public void setVoiceTip(String voiceTip) {
        this.voiceTip = voiceTip;
    }
}
