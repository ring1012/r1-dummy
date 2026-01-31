package nluparser.scheme;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public class LocalASR extends ASR {

    @SerializedName("score")
    @JSONField(name = "score")
    float score;

    public float getScore() {
        return this.score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
