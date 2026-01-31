package nluparser.scheme;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public class SMSIntent extends CallIntent {

    @SerializedName("content")
    @JSONField(name = "content")
    String content;

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
