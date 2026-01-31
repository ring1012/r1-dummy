package com.unisound.ant.device.bean;

import java.util.List;
import nluparser.scheme.AudioResult;

/* loaded from: classes.dex */
public class RemoteAudioInfo {
    private List<AudioResult.Music> audioinfo;
    private String voiceTip;

    public String getVoiceTip() {
        return this.voiceTip;
    }

    public void setVoiceTip(String voiceTip) {
        this.voiceTip = voiceTip;
    }

    public List<AudioResult.Music> getAudioinfo() {
        return this.audioinfo;
    }

    public void setAudioinfo(List<AudioResult.Music> audioinfo) {
        this.audioinfo = audioinfo;
    }
}
