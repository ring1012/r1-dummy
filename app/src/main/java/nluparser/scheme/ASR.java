package nluparser.scheme;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import com.unisound.passport.c;

/* loaded from: classes.dex */
public class ASR {
    public static final String FULL = "full";
    public static final String LOCAL = "local";
    public static final String LOCAL_ASR = "local_asr";
    public static final String MIX = "mix";
    public static final String NET = "net";
    public static final String NET_ASR = "net_asr";
    public static final String PARTIAL = "partial";

    @SerializedName("engine_mode")
    @JSONField(name = "engine_mode")
    String engineMode;

    @SerializedName("recognition_result")
    @JSONField(name = "recognition_result")
    String recognitionResult;

    @SerializedName(c.l)
    @JSONField(name = c.l)
    String resultType;

    public String getEngineMode() {
        return this.engineMode;
    }

    public String getRecognitionResult() {
        return this.recognitionResult.replaceAll("\\s*", "");
    }

    public String getResultType() {
        return this.resultType;
    }

    public boolean isLocalMode() {
        return LOCAL.equals(this.engineMode);
    }

    public boolean isMixMode() {
        return MIX.equals(this.engineMode);
    }

    public boolean isNetMode() {
        return NET.equals(this.engineMode);
    }

    public void setEngineMode(String engineMode) {
        this.engineMode = engineMode;
    }

    public void setRecognitionResult(String recognitionResult) {
        this.recognitionResult = recognitionResult;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }
}
