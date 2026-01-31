package com.unisound.vui.transport.out;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

@Deprecated
/* loaded from: classes.dex */
public class TTSContent {

    @SerializedName("play_text")
    @JSONField(name = "play_text")
    private String playContent;

    public TTSContent() {
    }

    public TTSContent(String playContent) {
        this.playContent = playContent;
    }

    public String getPlayContent() {
        return this.playContent;
    }

    public void setPlayContent(String playContent) {
        this.playContent = playContent;
    }
}
