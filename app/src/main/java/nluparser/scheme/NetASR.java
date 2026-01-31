package nluparser.scheme;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import com.unisound.common.x;

/* loaded from: classes.dex */
public class NetASR extends ASR {

    @SerializedName("last_result")
    @JSONField(name = "last_result")
    boolean lastResult;

    @SerializedName(x.c)
    @JSONField(name = x.c)
    String sessionID;

    public String getSessionID() {
        return this.sessionID;
    }

    public boolean isLastResult() {
        return this.lastResult;
    }

    public void setLastResult(boolean lastResult) {
        this.lastResult = lastResult;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }
}
