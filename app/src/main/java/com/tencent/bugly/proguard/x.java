package com.tencent.bugly.proguard;

import android.util.Log;
import cn.yunzhisheng.asr.JniUscClient;
import java.util.Locale;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class x {
    private static String c = "CrashReportInfo";

    /* renamed from: a, reason: collision with root package name */
    public static String f208a = "CrashReport";
    public static boolean b = false;

    private static boolean a(int i, String str, Object... objArr) {
        if (!b) {
            return false;
        }
        if (str == null) {
            str = JniUscClient.az;
        } else if (objArr != null && objArr.length != 0) {
            str = String.format(Locale.US, str, objArr);
        }
        switch (i) {
            case 0:
                Log.i(f208a, str);
                return true;
            case 1:
                Log.d(f208a, str);
                return true;
            case 2:
                Log.w(f208a, str);
                return true;
            case 3:
                Log.e(f208a, str);
                return true;
            case 4:
            default:
                return false;
            case 5:
                Log.i(c, str);
                return true;
        }
    }

    public static boolean a(String str, Object... objArr) {
        return a(0, str, objArr);
    }

    public static boolean a(Class cls, String str, Object... objArr) {
        return a(0, String.format(Locale.US, "[%s] %s", cls.getSimpleName(), str), objArr);
    }

    public static boolean b(String str, Object... objArr) {
        return a(5, str, objArr);
    }

    public static boolean c(String str, Object... objArr) {
        return a(1, str, objArr);
    }

    public static boolean b(Class cls, String str, Object... objArr) {
        return a(1, String.format(Locale.US, "[%s] %s", cls.getSimpleName(), str), objArr);
    }

    public static boolean d(String str, Object... objArr) {
        return a(2, str, objArr);
    }

    public static boolean a(Throwable th) {
        if (b) {
            return a(2, z.a(th), new Object[0]);
        }
        return false;
    }

    public static boolean e(String str, Object... objArr) {
        return a(3, str, objArr);
    }

    public static boolean b(Throwable th) {
        if (b) {
            return a(3, z.a(th), new Object[0]);
        }
        return false;
    }
}
