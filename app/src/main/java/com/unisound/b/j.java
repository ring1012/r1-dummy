package com.unisound.b;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/* loaded from: classes.dex */
public class j {

    /* renamed from: a, reason: collision with root package name */
    public static final String f224a = "Network";
    public static final String b = "android.net.conn.CONNECTIVITY_CHANGE";
    public static final String c = "cn.yunzhisheng.intent.action.CONNECTIVITY_CHANGE";
    public static final String d = "TYPE_NULL_POINT";
    public static final String e = "TYPE_UNCONNECT";
    public static final String f = "TYPE_WIFI";
    public static final String g = "TYPE_GPRS";
    private static final int h = 0;
    private static final int i = 1;
    private static int j = 1;

    public static long a(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0L;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        try {
            return simpleDateFormat.parse(str).getTime();
        } catch (Exception e2) {
            e2.printStackTrace();
            i.a("getServerTime exception");
            return 0L;
        }
    }

    private static String a(int i2) {
        return (i2 & 255) + "." + ((i2 >> 8) & 255) + "." + ((i2 >> 16) & 255) + "." + ((i2 >> 24) & 255);
    }

    public static String a(Context context) {
        try {
            return a(((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getIpAddress());
        } catch (Exception e2) {
            i.c("Activate getIp error");
            return "";
        }
    }

    public static boolean a() {
        return j > 0;
    }

    public static boolean b(Context context) {
        int i2 = j;
        if (c(context)) {
            j = 1;
        } else {
            j = 0;
        }
        if (i2 != j && context != null) {
            context.sendBroadcast(new Intent("cn.yunzhisheng.intent.action.CONNECTIVITY_CHANGE"));
        }
        return j > 0;
    }

    public static boolean c(Context context) {
        if (context == null) {
            return false;
        }
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                if (activeNetworkInfo.isConnected()) {
                    return true;
                }
            }
        } catch (Exception e2) {
            i.d("activate isNetworkConnected error");
        }
        return false;
    }

    public static String d(Context context) {
        NetworkInfo activeNetworkInfo;
        String str;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivityManager == null || (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) == null) {
                return "TYPE_NULL_POINT";
            }
            if (!activeNetworkInfo.isConnected()) {
                return "TYPE_UNCONNECT";
            }
            switch (activeNetworkInfo.getType()) {
                case 0:
                    str = "TYPE_GPRS";
                    break;
                case 1:
                    str = "TYPE_WIFI";
                    break;
                default:
                    str = "TYPE_NULL_POINT";
                    break;
            }
            return str;
        } catch (Exception e2) {
            i.d("Activate Network judgeCurrentNetTpe error");
            return "TYPE_NULL_POINT";
        }
    }
}
