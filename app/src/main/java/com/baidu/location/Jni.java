package com.baidu.location;

import cn.yunzhisheng.asr.JniUscClient;

/* loaded from: classes.dex */
public class Jni {

    /* renamed from: a, reason: collision with root package name */
    private static int f51a = 0;
    private static int b = 1;
    private static int c = 2;
    private static int d = 11;
    private static int e = 12;
    private static int f = 13;
    private static int g = 14;
    private static int h = 15;
    private static int i = 1024;
    private static boolean j;

    static {
        j = false;
        try {
            System.loadLibrary("locSDK7b");
        } catch (UnsatisfiedLinkError e2) {
            e2.printStackTrace();
            j = true;
        }
    }

    public static String a(String str) {
        return j ? "err!" : b(str) + "|tp=3";
    }

    private static native String a(byte[] bArr, int i2);

    public static double[] a(double d2, double d3, String str) {
        double[] dArr = {0.0d, 0.0d};
        if (j) {
            return dArr;
        }
        int i2 = -1;
        if (str.equals("bd09")) {
            i2 = f51a;
        } else if (str.equals("bd09ll")) {
            i2 = b;
        } else if (str.equals("gcj02")) {
            i2 = c;
        } else if (str.equals("gps2gcj")) {
            i2 = d;
        } else if (str.equals("bd092gcj")) {
            i2 = e;
        } else if (str.equals("bd09ll2gcj")) {
            i2 = f;
        } else if (str.equals("wgs842mc")) {
            i2 = h;
        }
        try {
            String[] strArrSplit = b(d2, d3, str.equals("gcj2wgs") ? 16 : i2, 132456).split(":");
            dArr[0] = Double.parseDouble(strArrSplit[0]);
            dArr[1] = Double.parseDouble(strArrSplit[1]);
        } catch (UnsatisfiedLinkError e2) {
        }
        return dArr;
    }

    private static native String b(double d2, double d3, int i2, int i3);

    public static String b(String str) {
        if (j) {
            return "err!";
        }
        if (str == null) {
            return JniUscClient.az;
        }
        byte[] bytes = str.getBytes();
        byte[] bArr = new byte[i];
        int length = bytes.length;
        int i2 = length <= 740 ? length : 740;
        int i3 = 0;
        for (int i4 = 0; i4 < i2; i4++) {
            if (bytes[i4] != 0) {
                bArr[i3] = bytes[i4];
                i3++;
            }
        }
        try {
            return a(bArr, 132456);
        } catch (UnsatisfiedLinkError e2) {
            e2.printStackTrace();
            return "err!";
        }
    }

    public static Long c(String str) {
        String str2;
        if (j) {
            return null;
        }
        try {
            str2 = new String(str.getBytes(), "UTF-8");
        } catch (Exception e2) {
            str2 = "";
        }
        try {
            return Long.valueOf(murmur(str2));
        } catch (UnsatisfiedLinkError e3) {
            e3.printStackTrace();
            return null;
        }
    }

    public static String d(String str) {
        String str2;
        String strEe;
        if (j) {
            return "err!";
        }
        try {
            str2 = new String(str.getBytes(), "UTF-8");
        } catch (Exception e2) {
            str2 = "";
        }
        try {
            strEe = ee(str2, 132456);
        } catch (UnsatisfiedLinkError e3) {
            e3.printStackTrace();
            strEe = "err!";
        }
        return strEe + "|tp=4";
    }

    private static native String ee(String str, int i2);

    private static native long murmur(String str);
}
