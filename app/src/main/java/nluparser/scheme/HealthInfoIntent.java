package nluparser.scheme;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public class HealthInfoIntent implements Intent {

    @SerializedName("properties")
    @JSONField(name = "properties")
    PropertiesInfo properties;

    public static class PropertiesInfo {

        @SerializedName("metrics")
        @JSONField(name = "metrics")
        String metrics;

        @SerializedName("person")
        @JSONField(name = "person")
        String person;

        public String getMetrics() {
            return this.metrics;
        }

        public String getPerson() {
            return this.person;
        }

        public void setMetrics(String metrics) {
            this.metrics = metrics;
        }

        public void setPerson(String person) {
            this.person = person;
        }
    }

    public PropertiesInfo getProperties() {
        return this.properties;
    }

    public void setProperties(PropertiesInfo properties) {
        this.properties = properties;
    }
}
