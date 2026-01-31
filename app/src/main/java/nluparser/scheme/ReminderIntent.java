package nluparser.scheme;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public class ReminderIntent implements Intent {
    public static final String D1 = "D1";
    public static final String D2 = "D2";
    public static final String D3 = "D3";
    public static final String D4 = "D4";
    public static final String D5 = "D5";
    public static final String D6 = "D6";
    public static final String D7 = "D7";
    public static final String DAY = "DAY";
    public static final String MONTH = "MONTH";
    public static final String OFF = "OFF";
    public static final String WEEK = "WEEK";
    public static final String WEEKEND = "WEEKEND";
    public static final String WORKDAY = "WORKDAY";
    public static final String YEAR = "YEAR";

    @SerializedName("content")
    @JSONField(name = "content")
    String content;

    @SerializedName("dateTime")
    @JSONField(name = "dateTime")
    String dateTime;

    @SerializedName("event")
    @JSONField(name = "event")
    Event event;

    @SerializedName("eventTime")
    @JSONField(name = "eventTime")
    String eventTime;

    @SerializedName("isSubscribe")
    @JSONField(name = "isSubscribe")
    boolean isSubscribe;

    @SerializedName("repeatType")
    @JSONField(name = "repeatType")
    String repeatType;

    @SerializedName("topic")
    @JSONField(name = "topic")
    String topic;

    @SerializedName("triggerType")
    @JSONField(name = "triggerType")
    String triggerType;

    public static class Event {

        @SerializedName("location")
        @JSONField(name = "location")
        String location;

        @SerializedName("person")
        @JSONField(name = "person")
        String person;

        @SerializedName("startTime")
        @JSONField(name = "startTime")
        String startTime;

        @SerializedName("topic")
        @JSONField(name = "topic")
        String topic;

        public String getLocation() {
            return this.location;
        }

        public String getPerson() {
            return this.person;
        }

        public String getStartTime() {
            return this.startTime;
        }

        public String getTopic() {
            return this.topic;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public void setPerson(String person) {
            this.person = person;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }
    }

    public String getContent() {
        return this.content;
    }

    public String getDateTime() {
        return this.dateTime;
    }

    public Event getEvent() {
        return this.event;
    }

    public String getEventTime() {
        return this.eventTime;
    }

    public String getRepeatType() {
        return this.repeatType;
    }

    public String getTopic() {
        return this.topic;
    }

    public String getTriggerType() {
        return this.triggerType;
    }

    public boolean isSubscribe() {
        return this.isSubscribe;
    }
}
