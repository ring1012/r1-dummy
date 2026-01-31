package nluparser.scheme;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public class WeatherIntent implements Intent {

    @SerializedName("city")
    @JSONField(name = "city")
    String city;

    @SerializedName("cityCode")
    @JSONField(name = "cityCode")
    String cityCode;

    @SerializedName("focusDate")
    @JSONField(name = "focusDate")
    String focusDate;

    @SerializedName("province")
    @JSONField(name = "province")
    String province;

    @SerializedName("topic")
    @JSONField(name = "topic")
    String topic;

    public String getCity() {
        return this.city;
    }

    public String getCityCode() {
        return this.cityCode;
    }

    public String getFocusDate() {
        return this.focusDate;
    }

    public String getProvince() {
        return this.province;
    }

    public String getTopic() {
        return this.topic;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public void setFocusDate(String focusDate) {
        this.focusDate = focusDate;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
