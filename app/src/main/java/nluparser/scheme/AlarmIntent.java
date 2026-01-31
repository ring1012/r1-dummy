package nluparser.scheme;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public class AlarmIntent implements Intent {
    public static final String D1 = "D1";
    public static final String D2 = "D2";
    public static final String D3 = "D3";
    public static final String D4 = "D4";
    public static final String D5 = "D5";
    public static final String D6 = "D6";
    public static final String D7 = "D7";
    public static final String OFF = "OFF";
    public static final int TYPE_ALARM = 0;
    public static final int TYPE_COUNT_DOWN = 1;
    public static final String WEEKEND = "WEEKEND";
    public static final String WORKDAY = "WORKDAY";

    @SerializedName("countDownTime")
    @JSONField(name = "countDownTime")
    int countDown;

    @SerializedName("date")
    @Nullable
    @JSONField(name = "date")
    String date;

    @SerializedName("label")
    @Nullable
    @JSONField(name = "label")
    String label;

    @SerializedName("repeatDate")
    @NonNull
    @JSONField(name = "repeatDate")
    String repeatDate;

    @SerializedName("time")
    @Nullable
    @JSONField(name = "time")
    String time;
    private int type = 0;

    public int getCountDown() {
        return this.countDown;
    }

    @Nullable
    public String getDate() {
        return this.date;
    }

    @Nullable
    public String getLabel() {
        return this.label;
    }

    @NonNull
    public String getRepeatDate() {
        return this.repeatDate;
    }

    @Nullable
    public String getTime() {
        return this.time;
    }

    public int getType() {
        return this.type;
    }

    public void setCountDown(int countDown) {
        this.countDown = countDown;
    }

    public void setDate(@Nullable String date) {
        this.date = date;
    }

    public void setLabel(@Nullable String label) {
        this.label = label;
    }

    public void setRepeatDate(@NonNull String repeatDate) {
        this.repeatDate = repeatDate;
    }

    public void setTime(@Nullable String time) {
        this.time = time;
    }

    public void setType(int type) {
        this.type = type;
    }
}
