package nluparser.scheme;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.android.exoplayer2.text.ttml.TtmlNode;
import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public class FlightIntent implements Intent {
    public static final String BIZ_CLASS = "BIZ_CLASS";
    public static final String CURRENT_CITY = "CURRENT_CITY";
    public static final String DEPART_TIME_ASC = "DEPART_TIME_ASC";
    public static final String DEPART_TIME_DESC = "DEPART_TIME_DESC";
    public static final String ECONOMY_CLASS = "ECONOMY_CLASS";
    public static final String FIRST_CLASS = "FIRST_CLASS";
    public static final String PRICE_ASC = "PRICE_ASC";
    public static final String PRICE_DESC = "PRICE_DESC";

    @SerializedName("airlineCode")
    @JSONField(name = "airlineCode")
    String airlineCode;

    @SerializedName("departDate")
    @JSONField(name = "departDate")
    String departDate;

    @SerializedName("departT")
    @JSONField(name = "departT")
    String departT;

    @SerializedName("destination")
    @JSONField(name = "destination")
    String destination;

    @SerializedName("flightNo")
    @JSONField(name = "flightNo")
    String flightNo;

    @SerializedName(TtmlNode.ATTR_TTS_ORIGIN)
    @JSONField(name = TtmlNode.ATTR_TTS_ORIGIN)
    String origin;

    @SerializedName("returnDate")
    @JSONField(name = "returnDate")
    String returnDate;

    @SerializedName("seat")
    @JSONField(name = "seat")
    String seat;

    @SerializedName("sort")
    @JSONField(name = "sort")
    String sort;

    public String getAirlineCode() {
        return this.airlineCode;
    }

    public String getDepartDate() {
        return this.departDate;
    }

    public String getDepartT() {
        return this.departT;
    }

    public String getDestination() {
        return this.destination;
    }

    public String getFlightNo() {
        return this.flightNo;
    }

    public String getOrigin() {
        return this.origin;
    }

    public String getReturnDate() {
        return this.returnDate;
    }

    public String getSeat() {
        return this.seat;
    }

    public String getSort() {
        return this.sort;
    }

    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }

    public void setDepartDate(String departDate) {
        this.departDate = departDate;
    }

    public void setDepartT(String departT) {
        this.departT = departT;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
