package nluparser.scheme;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public class CookBookIntent implements Intent {

    @SerializedName("dish")
    @JSONField(name = "dish")
    String dish;

    public String getDish() {
        return this.dish;
    }

    public void setDish(String dish) {
        this.dish = dish;
    }
}
