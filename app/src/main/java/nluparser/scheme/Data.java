package nluparser.scheme;

import android.support.annotation.Nullable;
import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import nluparser.scheme.Result;

/* loaded from: classes.dex */
public class Data<R extends Result> {

    @SerializedName("header")
    @Nullable
    @JSONField(name = "header")
    String header;

    @SerializedName("headerTts")
    @Nullable
    @JSONField(name = "headerTts")
    String headerTts;

    @SerializedName("result")
    @Nullable
    @JSONField(name = "result")
    R result;

    @Nullable
    public String getHeader() {
        return this.header;
    }

    @Nullable
    public String getHeaderTts() {
        return this.headerTts;
    }

    @Nullable
    public R getResult() {
        return this.result;
    }

    public void setHeader(@Nullable String header) {
        this.header = header;
    }

    public void setHeaderTts(@Nullable String headerTts) {
        this.headerTts = headerTts;
    }

    public void setResult(@Nullable R result) {
        this.result = result;
    }
}
