package nluparser.scheme;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public class TrafficIntent implements Intent {
    public static final String CURRENT_CITY = "CURRENT_CITY";
    public static final String EAST_TO_WEST = "EAST_TO_WEST";
    public static final String INNER_CIRCLE = "INNER_CIRCLE";
    public static final String INTO_TOWN = "INTO_TOWN";
    public static final String NORTH_TO_SOUTH = "NORTH_TO_SOUTH";
    public static final String OUTER_RING = "OUTER_RING";
    public static final String OUT_OF_TOWN = "OUT_OF_TOWN";
    public static final String ROAD_AHEAD = "ROAD_AHEAD";
    public static final String ROAD_SURROUNDING = "ROAD_SURROUNDING";
    public static final String SOUTH_TO_NORTH = "SOUTH_TO_NORTH";
    public static final String WEST_TO_EAST = "WEST_TO_EAST";

    @SerializedName("city")
    @JSONField(name = "city")
    String city;

    @SerializedName("direction")
    @JSONField(name = "direction")
    String direction;

    @SerializedName("road")
    @JSONField(name = "road")
    String road;

    public String getCity() {
        return this.city;
    }

    public String getDirection() {
        return this.direction;
    }

    public String getRoad() {
        return this.road;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setRoad(String road) {
        this.road = road;
    }
}
