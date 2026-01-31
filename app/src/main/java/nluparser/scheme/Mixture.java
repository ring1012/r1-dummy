package nluparser.scheme;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import nluparser.scheme.Intent;
import nluparser.scheme.Result;

/* loaded from: classes.dex */
public class Mixture<I extends Intent, R extends Result> {

    @SerializedName(ASR.LOCAL_ASR)
    @JSONField(name = ASR.LOCAL_ASR)
    List<LocalASR> localASRList;

    @SerializedName(ASR.NET_ASR)
    @JSONField(name = ASR.NET_ASR)
    List<NetASR> netASRList;

    @SerializedName("net_nlu")
    @JSONField(name = "net_nlu")
    List<NLU<I, R>> nluList;

    @SerializedName("tts_data")
    @JSONField(name = "tts_data")
    String ttsDataValue;

    public List<LocalASR> getLocalASRList() {
        return this.localASRList;
    }

    public List<NetASR> getNetASRList() {
        return this.netASRList;
    }

    public List<NLU<I, R>> getNluList() {
        return this.nluList;
    }

    public String getTtsDataValue() {
        return this.ttsDataValue;
    }

    public void setLocalASRList(List<LocalASR> localASRList) {
        this.localASRList = localASRList;
    }

    public void setNetASRList(List<NetASR> netASRList) {
        this.netASRList = netASRList;
    }

    public void setNluList(List<NLU<I, R>> nluList) {
        this.nluList = nluList;
    }

    public void setTtsDataValue(String ttsDataValue) {
        this.ttsDataValue = ttsDataValue;
    }
}
