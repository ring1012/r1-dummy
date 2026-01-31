package nluparser.scheme;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public class StockResult implements Result {

    @SerializedName("mChangeAmount")
    @JSONField(name = "mChangeAmount")
    String changeAmount;

    @SerializedName("mChangeRate")
    @JSONField(name = "mChangeRate")
    String changeRate;

    @SerializedName("mChartImgUrl")
    @JSONField(name = "mChartImgUrl")
    String chartImgUrl;

    @SerializedName("mCode")
    @JSONField(name = "mCode")
    String code;

    @SerializedName("mCurrentPrice")
    @JSONField(name = "mCurrentPrice")
    String currentPrice;

    @SerializedName("mHighestPrice")
    @JSONField(name = "mHighestPrice")
    String highestPrice;

    @SerializedName("mLowestPrice")
    @JSONField(name = "mLowestPrice")
    String lowestPrice;

    @SerializedName("mName")
    @JSONField(name = "mName")
    String name;

    @SerializedName("mTodayOpeningPrice")
    @JSONField(name = "mTodayOpeningPrice")
    String todayOpeningPrice;

    @SerializedName("mtradingVolume")
    @JSONField(name = "mtradingVolume")
    String tradingVolume;

    @SerializedName("mUpdateTime")
    @JSONField(name = "mUpdateTime")
    String updateTime;

    @SerializedName("mYesterdayClosingPrice")
    @JSONField(name = "mYesterdayClosingPrice")
    String yesterdayClosingPrice;

    public String getChangeAmount() {
        return this.changeAmount;
    }

    public String getChangeRate() {
        return this.changeRate;
    }

    public String getChartImgUrl() {
        return this.chartImgUrl;
    }

    public String getCode() {
        return this.code;
    }

    public String getCurrentPrice() {
        return this.currentPrice;
    }

    public String getHighestPrice() {
        return this.highestPrice;
    }

    public String getLowestPrice() {
        return this.lowestPrice;
    }

    public String getName() {
        return this.name;
    }

    public String getTodayOpeningPrice() {
        return this.todayOpeningPrice;
    }

    public String getTradingVolume() {
        return this.tradingVolume;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public String getYesterdayClosingPrice() {
        return this.yesterdayClosingPrice;
    }

    public void setChangeAmount(String changeAmount) {
        this.changeAmount = changeAmount;
    }

    public void setChangeRate(String changeRate) {
        this.changeRate = changeRate;
    }

    public void setChartImgUrl(String chartImgUrl) {
        this.chartImgUrl = chartImgUrl;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public void setHighestPrice(String highestPrice) {
        this.highestPrice = highestPrice;
    }

    public void setLowestPrice(String lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTodayOpeningPrice(String todayOpeningPrice) {
        this.todayOpeningPrice = todayOpeningPrice;
    }

    public void setTradingVolume(String tradingVolume) {
        this.tradingVolume = tradingVolume;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void setYesterdayClosingPrice(String yesterdayClosingPrice) {
        this.yesterdayClosingPrice = yesterdayClosingPrice;
    }
}
