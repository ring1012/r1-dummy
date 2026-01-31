package com.unisound.sdk;

import java.util.HashMap;

/* loaded from: classes.dex */
public class ce {
    public static String a(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13, String str14, String str15, String str16, String str17, String str18) {
        HashMap map = new HashMap();
        map.put(ca.c, str3);
        map.put(ca.b, str2);
        map.put("ver", str8);
        map.put("udid", str5);
        map.put("gps", str7);
        map.put("appver", str6);
        map.put("text", str9);
        map.put("history", str10);
        map.put("city", str11);
        map.put("time", str12);
        map.put(ca.m, str13);
        map.put("scenario", str14);
        map.put("screen", str15);
        map.put("dpi", str16);
        map.put(ca.q, str17);
        map.put(ca.r, str18);
        map.put(ca.j, com.unisound.common.aj.a(map, str4));
        return com.unisound.common.aj.a(str, map);
    }
}
