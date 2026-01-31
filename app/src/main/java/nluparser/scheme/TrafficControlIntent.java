package nluparser.scheme;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public class TrafficControlIntent implements Intent {

    @SerializedName("city")
    @JSONField(name = "city")
    String city;

    @SerializedName("date")
    @JSONField(name = "date")
    String date;

    @SerializedName("focus")
    @JSONField(name = "focus")
    String focus;

    @SerializedName("timeExpr")
    @JSONField(name = "timeExpr")
    String timeExpr;

    public String getCity() {
        return this.city;
    }

    public String getDate() {
        return this.date;
    }

    public String getFocus() {
        return this.focus;
    }

    public String getTimeExpr() {
        return this.timeExpr;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setFocus(String focus) {
        this.focus = focus;
    }

    public void setTimeExpr(String timeExpr) {
        this.timeExpr = timeExpr;
    }
}
