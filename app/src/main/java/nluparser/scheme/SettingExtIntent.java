package nluparser.scheme;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import java.util.Collections;
import java.util.List;
import nluparser.scheme.MusicResult;

/* loaded from: classes.dex */
public class SettingExtIntent implements Intent {

    @SerializedName("voiceTip")
    @JSONField(name = "voiceTip")
    String voiceTip;

    @SerializedName("operations")
    @JSONField(name = "operations")
    List<SettingIntent> operations = Collections.emptyList();

    @SerializedName("musicInfo")
    @JSONField(name = "musicInfo")
    List<MusicResult.Music> musicInfo = Collections.emptyList();

    public List<MusicResult.Music> getMusicInfo() {
        return this.musicInfo;
    }

    public List<SettingIntent> getOperations() {
        return this.operations;
    }

    public String getVoiceTip() {
        return this.voiceTip;
    }

    public void setMusicInfo(List<MusicResult.Music> musicInfo) {
        this.musicInfo = musicInfo;
    }

    public void setOperations(List<SettingIntent> operations) {
        this.operations = operations;
    }

    public void setVoiceTip(String voiceTip) {
        this.voiceTip = voiceTip;
    }
}
