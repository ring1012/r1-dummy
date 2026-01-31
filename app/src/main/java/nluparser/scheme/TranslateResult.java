package nluparser.scheme;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public class TranslateResult implements Result {

    @SerializedName("errorCode")
    @JSONField(name = "errorCode")
    int errorCode;

    @SerializedName("sententce")
    @JSONField(name = "sententce")
    String sententce;

    @SerializedName("target")
    @JSONField(name = "target")
    String target;

    @SerializedName("translated")
    @JSONField(name = "translated")
    String translated;

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getSententce() {
        return this.sententce;
    }

    public String getTarget() {
        return this.target;
    }

    public String getTranslated() {
        return this.translated;
    }

    public void setErrorCode(int c) {
        this.errorCode = c;
    }

    public void setSententce(String s) {
        this.sententce = s;
    }

    public void setTarget(String s) {
        this.target = s;
    }

    public void setTranslated(String s) {
        this.translated = s;
    }
}
