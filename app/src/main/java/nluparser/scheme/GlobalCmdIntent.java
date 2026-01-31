package nluparser.scheme;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public class GlobalCmdIntent implements Intent {

    @SerializedName("confirm")
    @JSONField(name = "confirm")
    String confirm;

    public String getConfirm() {
        return this.confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }
}
