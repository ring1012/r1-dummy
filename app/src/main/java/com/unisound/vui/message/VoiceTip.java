package com.unisound.vui.message;

/* loaded from: classes.dex */
public class VoiceTip {
    private String content;
    private boolean isShowLoading;
    private String sessionName;
    private String tip;
    private VoiceType voiceType;

    public String getContent() {
        return this.content;
    }

    public String getSessionName() {
        return this.sessionName;
    }

    public String getTip() {
        return this.tip;
    }

    public VoiceType getVoiceType() {
        return this.voiceType;
    }

    public boolean isShowLoading() {
        return this.isShowLoading;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public void setShowLoading(boolean showLoading) {
        this.isShowLoading = showLoading;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public void setVoiceType(VoiceType voiceType) {
        this.voiceType = voiceType;
    }
}
