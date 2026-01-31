package nluparser.scheme;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public class TrafficControlResult implements Result {

    @SerializedName("city")
    @JSONField(name = "city")
    String city;

    @SerializedName("errorCode")
    @JSONField(name = "errorCode")
    String errorCode;

    @SerializedName(ASR.LOCAL)
    @JSONField(name = ASR.LOCAL)
    String local;

    @SerializedName("nonlocal")
    @JSONField(name = "nonlocal")
    String nonlocal;

    @SerializedName("trafficControlInfos")
    @JSONField(name = "trafficControlInfos")
    List<TrafficControlInfo> trafficControlInfos = Collections.emptyList();

    public static class TrafficControlInfo {

        @SerializedName("forbiddenDate")
        @JSONField(name = "forbiddenDate")
        String forbiddenDate;

        @SerializedName("forbiddenTailNumber")
        @JSONField(name = "forbiddenTailNumber")
        String forbiddenTailNumber;

        @SerializedName("week")
        @JSONField(name = "week")
        String week;
    }

    public String getCity() {
        return this.city;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getLocal() {
        return this.local;
    }

    public String getNonlocal() {
        return this.nonlocal;
    }

    public List<TrafficControlInfo> getTrafficControlInfos() {
        return this.trafficControlInfos;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public void setNonlocal(String nonlocal) {
        this.nonlocal = nonlocal;
    }

    public void setTrafficControlInfos(List<TrafficControlInfo> trafficControlInfos) {
        this.trafficControlInfos = trafficControlInfos;
    }
}
