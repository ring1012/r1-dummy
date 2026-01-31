package nluparser.scheme;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/* loaded from: classes.dex */
public class BroadcastResult implements Serializable, Result {

    @SerializedName("audience_count")
    @JSONField(name = "audience_count")
    String audienceCount;

    @SerializedName("channel_id")
    @JSONField(name = "channel_id")
    int channelId;

    @SerializedName("cover_url_large")
    @JSONField(name = "cover_url_large")
    String coverUrlLarge;

    @SerializedName("cover_url_small")
    @JSONField(name = "cover_url_small")
    String coverUrlSmall;

    @SerializedName("dataSourceName")
    @JSONField(name = "dataSourceName")
    String dataSourceName;

    @SerializedName("description")
    @JSONField(name = "description")
    String description;

    @SerializedName("errorCode")
    @JSONField(name = "errorCode")
    int errorCode;

    @SerializedName("program_name")
    @JSONField(name = "program_name")
    String programName;

    @SerializedName("rate24_aac_url")
    @JSONField(name = "rate24_aac_url")
    String rate24AacUrl;

    @SerializedName("rate24_ts_url")
    @JSONField(name = "rate24_ts_url")
    String rate24TsUrl;

    @SerializedName("rate64_aac_url")
    @JSONField(name = "rate64_aac_url")
    String rate64AacUrl;

    @SerializedName("rate64_ts_url")
    @JSONField(name = "rate64_ts_url")
    String rate64TsUrl;

    @SerializedName("schedule_id")
    @JSONField(name = "schedule_id")
    int scheduleId;

    @SerializedName("source")
    @JSONField(name = "source")
    int source;

    @SerializedName("support_bitrates")
    @JSONField(name = "support_bitrates")
    String supportBitrates;

    @SerializedName("title")
    @JSONField(name = "title")
    String title;

    @SerializedName("totalTime")
    @JSONField(name = "totalTime")
    int totalTime;

    @SerializedName("update_time")
    @JSONField(name = "update_time")
    String updateTime;

    public String getAudienceCount() {
        return this.audienceCount;
    }

    public int getChannelId() {
        return this.channelId;
    }

    public String getCoverUrlLarge() {
        return this.coverUrlLarge;
    }

    public String getCoverUrlSmall() {
        return this.coverUrlSmall;
    }

    public String getDataSourceName() {
        return this.dataSourceName;
    }

    public String getDescription() {
        return this.description;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getProgramName() {
        return this.programName;
    }

    public String getRate24AacUrl() {
        return this.rate24AacUrl;
    }

    public String getRate24TsUrl() {
        return this.rate24TsUrl;
    }

    public String getRate64AacUrl() {
        return this.rate64AacUrl;
    }

    public String getRate64TsUrl() {
        return this.rate64TsUrl;
    }

    public int getScheduleId() {
        return this.scheduleId;
    }

    public int getSource() {
        return this.source;
    }

    public String getSupportBitrates() {
        return this.supportBitrates;
    }

    public String getTitle() {
        return this.title;
    }

    public int getTotalTime() {
        return this.totalTime;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setAudienceCount(String audienceCount) {
        this.audienceCount = audienceCount;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public void setCoverUrlLarge(String coverUrlLarge) {
        this.coverUrlLarge = coverUrlLarge;
    }

    public void setCoverUrlSmall(String coverUrlSmall) {
        this.coverUrlSmall = coverUrlSmall;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public void setRate24AacUrl(String rate24AacUrl) {
        this.rate24AacUrl = rate24AacUrl;
    }

    public void setRate24TsUrl(String rate24TsUrl) {
        this.rate24TsUrl = rate24TsUrl;
    }

    public void setRate64AacUrl(String rate64AacUrl) {
        this.rate64AacUrl = rate64AacUrl;
    }

    public void setRate64TsUrl(String rate64TsUrl) {
        this.rate64TsUrl = rate64TsUrl;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public void setSupportBitrates(String supportBitrates) {
        this.supportBitrates = supportBitrates;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
