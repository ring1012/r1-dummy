package nluparser.scheme;

import android.support.annotation.NonNull;
import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public class Error {

    @SerializedName("code")
    @NonNull
    @JSONField(name = "code")
    String code;

    @SerializedName("message")
    @NonNull
    @JSONField(name = "message")
    String message;

    @NonNull
    public String getCode() {
        return this.code;
    }

    @NonNull
    public String getMessage() {
        return this.message;
    }

    public void setCode(@NonNull String code) {
        this.code = code;
    }

    public void setMessage(@NonNull String message) {
        this.message = message;
    }
}
