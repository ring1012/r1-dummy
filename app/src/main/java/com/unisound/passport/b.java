package com.unisound.passport;

import android.util.Log;
import com.unisound.common.y;

/* loaded from: classes.dex */
public class b {

    /* renamed from: a, reason: collision with root package name */
    public static boolean f282a = true;
    public static boolean b = true;
    public static String c = y.v;

    public static void a(String str) {
        if (f282a) {
            Log.v(c, str);
            f(str);
        }
    }

    public static void a(String str, String str2) {
        if (f282a) {
            Log.v(str, str2);
            f(str2);
        }
    }

    public static void a(String str, String str2, Throwable th) {
        if (f282a) {
            Log.e(str, str2, th);
        }
    }

    public static void a(String str, Throwable th) {
        a(c, str, th);
    }

    public static void b(String str) {
        if (f282a) {
            Log.i(c, str);
            f(str);
        }
    }

    public static void b(String str, String str2) {
        if (f282a) {
            Log.i(str, str2);
            f(str2);
        }
    }

    public static void c(String str) {
        if (f282a) {
            Log.d(c, str);
            f(str);
        }
    }

    public static void c(String str, String str2) {
        if (f282a) {
            Log.d(str, str2);
            f(str2);
        }
    }

    public static void d(String str) {
        if (f282a) {
            Log.w(c, str);
            f(str);
        }
    }

    public static void d(String str, String str2) {
        if (f282a) {
            Log.w(str, str2);
            f(str2);
        }
    }

    public static void e(String str) {
        Log.e(c, str);
        f(str);
    }

    public static void e(String str, String str2) {
        if (f282a) {
            Log.e(str, str2);
            f(str2);
        }
    }

    private static void f(String str) {
        if (b) {
        }
    }
}
