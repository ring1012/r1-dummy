package com.unisound.common;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/* loaded from: classes.dex */
public class ae {

    /* renamed from: a, reason: collision with root package name */
    public static final String f236a = "Network";
    public static final String b = "android.net.conn.CONNECTIVITY_CHANGE";
    public static final String c = "cn.yunzhisheng.intent.action.CONNECTIVITY_CHANGE";
    public static final String d = "TYPE_NULL_POINT";
    public static final String e = "TYPE_UNCONNECT";
    public static final String f = "TYPE_WIFI";
    public static final String g = "TYPE_GPRS";
    private static final int h = 0;
    private static final int i = 1;
    private static int j = 1;

    private static String a(int i2) {
        return (i2 & 255) + "." + ((i2 >> 8) & 255) + "." + ((i2 >> 16) & 255) + "." + ((i2 >> 24) & 255);
    }

    public static String a(Context context) {
        try {
            int ipAddress = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getIpAddress();
            return ipAddress == 0 ? b() : a(ipAddress);
        } catch (Exception e2) {
            y.c("Network getIp error");
            return "";
        }
    }

    public static boolean a() {
        return j > 0;
    }

    public static String b() throws SocketException {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                Enumeration<InetAddress> inetAddresses = networkInterfaces.nextElement().getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddressNextElement = inetAddresses.nextElement();
                    if (!inetAddressNextElement.isLoopbackAddress() && (inetAddressNextElement instanceof Inet4Address)) {
                        return inetAddressNextElement.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return "0.0.0.0";
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x001c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean b(android.content.Context r3) {
        /*
            r1 = 1
            r2 = 0
            if (r3 != 0) goto L5
        L4:
            return r2
        L5:
            java.lang.String r0 = "connectivity"
            java.lang.Object r0 = r3.getSystemService(r0)     // Catch: java.lang.Exception -> L1e
            android.net.ConnectivityManager r0 = (android.net.ConnectivityManager) r0     // Catch: java.lang.Exception -> L1e
            android.net.NetworkInfo r0 = r0.getActiveNetworkInfo()     // Catch: java.lang.Exception -> L1e
            if (r0 == 0) goto L1c
            boolean r0 = r0.isConnected()     // Catch: java.lang.Exception -> L1e
            if (r0 == 0) goto L1c
            r0 = r1
        L1a:
            r2 = r0
            goto L4
        L1c:
            r0 = r2
            goto L1a
        L1e:
            r0 = move-exception
            java.lang.Object[] r0 = new java.lang.Object[r1]
            java.lang.String r1 = "Network:isNetworkConnected error"
            r0[r2] = r1
            com.unisound.common.y.c(r0)
            goto L4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unisound.common.ae.b(android.content.Context):boolean");
    }

    public static boolean c(Context context) {
        int i2 = j;
        if (d(context)) {
            j = 1;
        } else {
            j = 0;
        }
        if (i2 != j && context != null) {
            context.sendBroadcast(new Intent("cn.yunzhisheng.intent.action.CONNECTIVITY_CHANGE"));
        }
        return j > 0;
    }

    public static boolean d(Context context) {
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
            y.c("Network:isNetworkConnected error");
        }
        return false;
    }

    public static String e(Context context) {
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
            y.c("Network:judgeCurrentNetTpe error");
            return "TYPE_NULL_POINT";
        }
    }
}
