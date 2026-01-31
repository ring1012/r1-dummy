package com.unisound.b;

import android.util.Log;

/* loaded from: classes.dex */
public class i {

    /* renamed from: a, reason: collision with root package name */
    public static boolean f223a = false;
    private static final String b = "USCActivate";

    public static void a(String str) {
        if (f223a) {
            Log.e(b, str);
        }
    }

    public static void a(String str, String str2) {
        if (f223a) {
            Log.e(str, str2);
        }
    }

    public static void b(String str) {
        if (f223a) {
            Log.w(b, str);
        }
    }

    public static void b(String str, String str2) {
        if (f223a) {
            Log.w(str, str2);
        }
    }

    public static void c(String str) {
        if (f223a) {
            Log.i(b, str);
        }
    }

    public static void c(String str, String str2) {
        if (f223a) {
            Log.i(str, str2);
        }
    }

    public static void d(String str) {
        if (f223a) {
            Log.d(b, str);
        }
    }

    public static void d(String str, String str2) {
        if (f223a) {
            Log.d(str, str2);
        }
    }
}
