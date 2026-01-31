package com.baidu.b.a;

import android.util.Log;

/* loaded from: classes.dex */
class d {

    /* renamed from: a, reason: collision with root package name */
    public static boolean f40a = false;
    private static String b = "BaiduApiAuth";

    public static String a() {
        StackTraceElement stackTraceElement = new Throwable().getStackTrace()[2];
        return stackTraceElement.getFileName() + "[" + stackTraceElement.getLineNumber() + "]";
    }

    public static void a(String str) {
        if (!f40a || Thread.currentThread().getStackTrace().length == 0) {
            return;
        }
        Log.d(b, a() + ";" + str);
    }

    public static void b(String str) {
        if (Thread.currentThread().getStackTrace().length == 0) {
            return;
        }
        Log.i(b, str);
    }

    public static void c(String str) {
        if (!f40a || Thread.currentThread().getStackTrace().length == 0) {
            return;
        }
        Log.e(b, a() + ";" + str);
    }
}
