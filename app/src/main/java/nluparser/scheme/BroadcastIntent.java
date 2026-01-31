package nluparser.scheme;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import java.util.Collections;
import java.util.List;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.litepal.util.Const;

/* loaded from: classes.dex */
public class BroadcastIntent implements Intent {

    @SerializedName("channelList")
    @JSONField(name = "channelList")
    List<ChannelList> channelList = Collections.emptyList();

    @SerializedName("channelType")
    @JSONField(name = "channelType")
    String channelType;

    @SerializedName("msg")
    @JSONField(name = "msg")
    String msg;

    @SerializedName("responseId")
    @JSONField(name = "responseId")
    String responseId;

    @SerializedName("station")
    @JSONField(name = "station")
    String station;

    public static class ChannelList {

        @SerializedName("channel")
        @JSONField(name = "channel")
        String channel;

        @SerializedName("frequencyList")
        @JSONField(name = "frequencyList")
        List<FrequencyList> frequencyList = Collections.emptyList();

        public static class FrequencyList {

            @SerializedName("frequency")
            @JSONField(name = "frequency")
            String frequency;

            @SerializedName(Const.TableSchema.COLUMN_TYPE)
            @JSONField(name = Const.TableSchema.COLUMN_TYPE)
            String type;

            @SerializedName("unit")
            @JSONField(name = "unit")
            String unit;

            public String getFrequency() {
                return this.frequency;
            }

            public String getType() {
                return this.type;
            }

            public String getUnit() {
                return this.unit;
            }

            public void setFrequency(String frequency) {
                this.frequency = frequency;
            }

            public void setType(String type) {
                this.type = type;
            }

            public void setUnit(String unit) {
                this.unit = unit;
            }

            public String toString() {
                return this.type + MqttTopic.MULTI_LEVEL_WILDCARD + this.frequency + MqttTopic.MULTI_LEVEL_WILDCARD + this.unit;
            }
        }

        public String getChannel() {
            return this.channel;
        }

        public List<FrequencyList> getFrequencyList() {
            return this.frequencyList;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public void setFrequencyList(List<FrequencyList> frequencyList) {
            this.frequencyList = frequencyList;
        }
    }

    public List<ChannelList> getChannelList() {
        return this.channelList;
    }

    public String getChannelType() {
        return this.channelType;
    }

    public String getMsg() {
        return this.msg;
    }

    public String getResponseId() {
        return this.responseId;
    }

    public String getStation() {
        return this.station;
    }

    public void setChannelList(List<ChannelList> channelList) {
        this.channelList = channelList;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public void setStation(String station) {
        this.station = station;
    }
}
