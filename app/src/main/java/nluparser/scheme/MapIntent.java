package nluparser.scheme;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import com.unisound.sdk.ca;

/* loaded from: classes.dex */
public class MapIntent implements Intent {
    public static final String BUS = "BUS";
    public static final String CAR = "CAR";
    public static final String CURRENT_CITY = "CURRENT_CITY";
    public static final String CURRENT_LOC = "CURRENT_LOC";
    public static final String EBUS_NO_SUBWAY = "EBUS_NO_SUBWAY";
    public static final String EBUS_TRANSFER_FIRST = "EBUS_TRANSFER_FIRST";
    public static final String EBUS_WALK_FIRST = "EBUS_WALK_FIRST";
    public static final String ECAR_DIS_FIRST = "ECAR_DIS_FIRST";
    public static final String ECAR_FEE_FIRST = "ECAR_FEE_FIRST";
    public static final String LOC_HOME = "LOC_HOME";
    public static final String LOC_OFFICE = "LOC_OFFICE";
    public static final String TIME_FIRST = "TIME_FIRST";
    public static final String WALK = "WALK";

    @SerializedName("condition")
    @JSONField(name = "condition")
    String condition;

    @SerializedName("fromCity")
    @JSONField(name = "fromCity")
    String fromCity;

    @SerializedName("fromPOI")
    @JSONField(name = "fromPOI")
    String fromPOI;

    @SerializedName(ca.b)
    @JSONField(name = ca.b)
    String method;

    @SerializedName("toCity")
    @JSONField(name = "toCity")
    String toCity;

    @SerializedName("toPOI")
    @JSONField(name = "toPOI")
    String toPOI;

    public String getCondition() {
        return this.condition;
    }

    public String getFromCity() {
        return this.fromCity;
    }

    public String getFromPOI() {
        return this.fromPOI;
    }

    public String getMethod() {
        return this.method;
    }

    public String getToCity() {
        return this.toCity;
    }

    public String getToPOI() {
        return this.toPOI;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    public void setFromPOI(String fromPOI) {
        this.fromPOI = fromPOI;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setToCity(String toCity) {
        this.toCity = toCity;
    }

    public void setToPOI(String toPOI) {
        this.toPOI = toPOI;
    }
}
