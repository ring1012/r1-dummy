package nluparser.scheme;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import com.unisound.sdk.ca;
import org.litepal.util.Const;

/* loaded from: classes.dex */
public class CallIntent implements Intent {
    public static final String TYPE_ASSISTANT = "TYPE_ASSISTANT";
    public static final String TYPE_CAR = "TYPE_CAR";
    public static final String TYPE_HOME = "TYPE_HOME";
    public static final String TYPE_MOBILE = "TYPE_MOBILE";
    public static final String TYPE_WORK = "TYPE_WORK";

    @SerializedName(ca.b)
    @JSONField(name = ca.b)
    String method;

    @SerializedName(Const.TableSchema.COLUMN_NAME)
    @JSONField(name = Const.TableSchema.COLUMN_NAME)
    String name;

    @SerializedName("number")
    @JSONField(name = "number")
    String number;

    public String getMethod() {
        return this.method;
    }

    public String getName() {
        return this.name;
    }

    public String getNumber() {
        return this.number;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
