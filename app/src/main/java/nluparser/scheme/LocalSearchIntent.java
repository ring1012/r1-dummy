package nluparser.scheme;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.android.exoplayer2.text.ttml.TtmlNode;
import com.google.gson.annotations.SerializedName;
import com.unisound.sdk.i;

/* loaded from: classes.dex */
public class LocalSearchIntent implements Intent {
    public static final int BUSINESS_SEARCH_SORT_DEFAULT = 1;
    public static final int BUSINESS_SEARCH_SORT_DISTANCE_ASC = 7;
    public static final int BUSINESS_SEARCH_SORT_ENVIRONMENT_DESC = 4;
    public static final int BUSINESS_SEARCH_SORT_PRODUCT_DESC = 3;
    public static final int BUSINESS_SEARCH_SORT_REVIEWS_DESC = 6;
    public static final int BUSINESS_SEARCH_SORT_SERVE_DESC = 5;
    public static final int BUSINESS_SEARCH_SORT_STAR_DESC = 2;
    public static final String CURRENT_CITY = "CURRENT_CITY";
    public static final String CURRENT_LOC = "CURRENT_LOC";
    public static final int DEAL_SEARCH_SORT_DEADLINE_ASC = 6;
    public static final int DEAL_SEARCH_SORT_DEFAULT = 1;
    public static final int DEAL_SEARCH_SORT_DISTANCE_ASC = 7;
    public static final int DEAL_SEARCH_SORT_LASTEST_ASC = 5;
    public static final int DEAL_SEARCH_SORT_PRICE_ASC = 2;
    public static final int DEAL_SEARCH_SORT_PRICE_DESC = 3;
    public static final int DEAL_SEARCH_SORT_PURCHASE_DESC = 4;

    @SerializedName("category")
    @JSONField(name = "category")
    String category;

    @SerializedName("city")
    @JSONField(name = "city")
    String city;

    @SerializedName("hasCoupon")
    @JSONField(name = "hasCoupon")
    int hasCoupon;

    @SerializedName("hasDeal")
    @JSONField(name = "hasDeal")
    int hasDeal;

    @SerializedName("keyword")
    @JSONField(name = "keyword")
    String keyword;

    @SerializedName(i.j)
    @JSONField(name = "city")
    String poi;

    @SerializedName("priceHigh")
    @JSONField(name = "priceHigh")
    int priceHigh;

    @SerializedName("priceLow")
    @JSONField(name = "priceLow")
    int priceLow;

    @SerializedName("radius")
    @JSONField(name = "radius")
    int radius;

    @SerializedName(TtmlNode.TAG_REGION)
    @JSONField(name = TtmlNode.TAG_REGION)
    String region;

    @SerializedName("sort")
    @JSONField(name = "sort")
    int sort;

    public String getCategory() {
        return this.category;
    }

    public String getCity() {
        return this.city;
    }

    public int getHasCoupon() {
        return this.hasCoupon;
    }

    public int getHasDeal() {
        return this.hasDeal;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public String getPoi() {
        return this.poi;
    }

    public int getPriceHigh() {
        return this.priceHigh;
    }

    public int getPriceLow() {
        return this.priceLow;
    }

    public int getRadius() {
        return this.radius;
    }

    public String getRegion() {
        return this.region;
    }

    public int getSort() {
        return this.sort;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setHasCoupon(int hasCoupon) {
        this.hasCoupon = hasCoupon;
    }

    public void setHasDeal(int hasDeal) {
        this.hasDeal = hasDeal;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setPoi(String poi) {
        this.poi = poi;
    }

    public void setPriceHigh(int priceHigh) {
        this.priceHigh = priceHigh;
    }

    public void setPriceLow(int priceLow) {
        this.priceLow = priceLow;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
