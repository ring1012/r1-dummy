package nluparser.scheme;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public class TranslateIntent implements Intent {

    @SerializedName("sentence")
    @JSONField(name = "sentence")
    String sentence;

    @SerializedName("source")
    @JSONField(name = "source")
    String source;

    @SerializedName("target")
    @JSONField(name = "target")
    String target;

    public String getSentence() {
        return this.sentence;
    }

    public String getSource() {
        return this.source;
    }

    public String getTarget() {
        return this.target;
    }

    public void setSentence(String s) {
        this.sentence = s;
    }

    public void setSource(String s) {
        this.source = s;
    }

    public void setTarget(String t) {
        this.target = t;
    }
}
