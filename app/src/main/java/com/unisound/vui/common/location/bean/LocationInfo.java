package com.unisound.vui.common.location.bean;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.util.Const;

/* loaded from: classes.dex */
public class LocationInfo {
    public static final int GPS_BAIDU = 4;
    public static final int GPS_GAODE = 5;
    public static final int GPS_GOOGLE = 2;
    public static final int GPS_ORIGIN = 0;
    public double latitude;
    public float mAccuracy;
    public String mAddress;
    public String mAddressDetail;
    public double mAltitude;
    public float mBearing;
    public String mCity;
    public String mCityCode;
    public String mCondition;
    public String mCountry;
    public String mDistrict;
    public boolean mHasAccuracy;
    public boolean mHasAltitude;
    public boolean mHasBearing;
    public boolean mHasSpeed;
    public double mLongitude;
    public String mName;
    public String mPathPoints;
    public String mProvince;
    public float mSpeed;
    public String mStreet;
    public long mTime;
    public int type;

    public LocationInfo() {
        this.type = 4;
        this.latitude = 0.0d;
        this.mLongitude = 0.0d;
        this.mTime = 0L;
        this.mHasAltitude = false;
        this.mAltitude = 0.0d;
        this.mHasSpeed = false;
        this.mSpeed = 0.0f;
        this.mHasBearing = false;
        this.mBearing = 0.0f;
        this.mHasAccuracy = false;
        this.mAccuracy = 0.0f;
    }

    public LocationInfo(LocationInfo l) {
        this.type = 4;
        this.latitude = 0.0d;
        this.mLongitude = 0.0d;
        this.mTime = 0L;
        this.mHasAltitude = false;
        this.mAltitude = 0.0d;
        this.mHasSpeed = false;
        this.mSpeed = 0.0f;
        this.mHasBearing = false;
        this.mBearing = 0.0f;
        this.mHasAccuracy = false;
        this.mAccuracy = 0.0f;
        if (l == null) {
            return;
        }
        this.type = l.type;
        this.latitude = l.latitude;
        this.mLongitude = l.mLongitude;
        this.mName = l.mName;
        this.mProvince = l.mProvince;
        this.mCity = l.mCity;
        this.mCityCode = l.mCityCode;
        this.mDistrict = l.mDistrict;
        this.mStreet = l.mStreet;
        this.mAddress = l.mAddress;
        this.mAddressDetail = l.mAddressDetail;
        this.mCondition = l.mCondition;
        this.mPathPoints = l.mPathPoints;
    }

    public float getAccuracy() {
        return this.mAccuracy;
    }

    public String getAddress() {
        return this.mAddress;
    }

    public String getAddressDetail() {
        return this.mAddressDetail;
    }

    public double getAltitude() {
        return this.mAltitude;
    }

    public float getBearing() {
        return this.mBearing;
    }

    public String getCity() {
        return this.mCity;
    }

    public String getCityCode() {
        return this.mCityCode;
    }

    public String getDistrict() {
        return this.mDistrict;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.mLongitude;
    }

    public String getName() {
        return this.mName;
    }

    public String getProvider() {
        return this.mProvince;
    }

    public String getProvince() {
        return this.mProvince;
    }

    public float getSpeed() {
        return this.mSpeed;
    }

    public String getStreet() {
        return this.mStreet;
    }

    public long getTime() {
        return this.mTime;
    }

    public int getType() {
        return this.type;
    }

    public String getmCondition() {
        return this.mCondition;
    }

    public String getmCountry() {
        return this.mCountry;
    }

    public String getmPathPoints() {
        return this.mPathPoints;
    }

    public boolean hasAccuracy() {
        return this.mHasAccuracy;
    }

    public boolean hasAltitude() {
        return this.mHasAltitude;
    }

    public boolean hasBearing() {
        return this.mHasBearing;
    }

    public boolean hasSpeed() {
        return this.mHasSpeed;
    }

    public JSONObject parse2JSONObj() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(Const.TableSchema.COLUMN_TYPE, this.type);
            jSONObject.put(Const.TableSchema.COLUMN_NAME, this.mName);
            jSONObject.put("country", this.mCountry);
            jSONObject.put("province", this.mProvince);
            jSONObject.put("city", this.mCity);
            jSONObject.put("cityCode", this.mCityCode);
            jSONObject.put("destrict", this.mDistrict);
            jSONObject.put("street", this.mStreet);
            jSONObject.put("address", this.mAddress);
            jSONObject.put("addressDetail", this.mAddressDetail);
            jSONObject.put("condition", this.mCondition);
            jSONObject.put("pathPoints", this.mPathPoints);
            jSONObject.put("lat", getLatitude());
            jSONObject.put("lng", getLongitude());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject;
    }

    public void removeAltitude() {
        this.mAltitude = 0.0d;
        this.mHasAltitude = false;
    }

    public void removeBearing() {
        this.mBearing = 0.0f;
        this.mHasBearing = false;
    }

    public void removeSpeed() {
        this.mSpeed = 0.0f;
        this.mHasSpeed = false;
    }

    public void setAccuracy(float accuracy) {
        this.mAccuracy = accuracy;
        this.mHasAccuracy = true;
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }

    public void setAddressDetail(String address) {
        this.mAddressDetail = address;
    }

    public void setAltitude(double altitude) {
        this.mAltitude = altitude;
        this.mHasAltitude = true;
    }

    public void setBearing(float bearing) {
        while (bearing < 0.0f) {
            bearing += 360.0f;
        }
        while (bearing >= 360.0f) {
            bearing -= 360.0f;
        }
        this.mBearing = bearing;
        this.mHasBearing = true;
    }

    public void setCity(String city) {
        if (city != null && city.lastIndexOf("市") > 0) {
            city = city.substring(0, city.lastIndexOf("市"));
        }
        this.mCity = city;
    }

    public void setCityCode(String mCityCode) {
        this.mCityCode = mCityCode;
    }

    public void setCountry(String mCountry) {
        this.mCountry = mCountry;
    }

    public void setDistrict(String mDistrict) {
        this.mDistrict = mDistrict;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setProvider(String provider) {
        this.mProvince = provider;
    }

    public void setProvince(String province) {
        this.mProvince = province;
    }

    public void setSpeed(float speed) {
        this.mSpeed = speed;
        this.mHasSpeed = true;
    }

    public void setStreet(String mStreet) {
        this.mStreet = mStreet;
    }

    public void setTime(long time) {
        this.mTime = time;
    }

    public void setType(int type) {
    }

    public void setmCondition(String mCondition) {
        this.mCondition = mCondition;
    }

    public void setmPathPoints(String mPathPoints) {
        this.mPathPoints = mPathPoints;
    }

    public String toString() {
        return parse2JSONObj().toString();
    }
}
