package com.unisound.vui.common.location.strategy.baidu;

import com.baidu.location.c;
import com.unisound.vui.common.location.bean.LocationInfo;

/* loaded from: classes.dex */
class BaiduLocationInfoParser {
    BaiduLocationInfoParser() {
    }

    static LocationInfo parser(c bdLocation) {
        LocationInfo locationInfo = new LocationInfo();
        transportToUniLocationInfo(locationInfo, bdLocation);
        return locationInfo;
    }

    private static void transportToUniLocationInfo(LocationInfo uniLocatonInfo, c bdLocation) {
        uniLocatonInfo.setType(bdLocation.h());
        uniLocatonInfo.setName(bdLocation.l());
        uniLocatonInfo.setCountry(bdLocation.p());
        uniLocatonInfo.setProvince(bdLocation.m());
        uniLocatonInfo.setCity(bdLocation.n());
        uniLocatonInfo.setCityCode(bdLocation.o());
        uniLocatonInfo.setDistrict(bdLocation.q());
        uniLocatonInfo.setAddress(bdLocation.l());
        uniLocatonInfo.setLatitude(bdLocation.d());
        uniLocatonInfo.setLongitude(bdLocation.e());
    }
}
