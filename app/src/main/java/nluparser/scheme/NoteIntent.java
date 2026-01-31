package nluparser.scheme;

import android.support.annotation.Nullable;
import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public class NoteIntent implements Intent {

    @SerializedName("content")
    @Nullable
    @JSONField(name = "content")
    String content;

    @SerializedName("topic")
    @Nullable
    @JSONField(name = "topic")
    String topic;

    @Nullable
    public String getContent() {
        return this.content;
    }

    @Nullable
    public String getTopic() {
        return this.topic;
    }

    public void setContent(@Nullable String content) {
        this.content = content;
    }

    public void setTopic(@Nullable String topic) {
        this.topic = topic;
    }

    public String toString() {
        return "NoteIntent{content='" + this.content + "', topic='" + this.topic + "'}";
    }
}
