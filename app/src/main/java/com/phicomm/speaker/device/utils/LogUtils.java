package com.phicomm.speaker.device.utils;

import android.util.Log;
import com.phicomm.speaker.device.BuildConfig;
import java.util.Locale;

/* loaded from: classes.dex */
public class LogUtils {
    private static final String TAG = "default_tag";

    public static void d(String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format(Locale.CHINA, "[%s]", msg));
        }
    }

    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, String.format(Locale.CHINA, "[%s]", msg));
        }
    }

    public static void d(String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format(Locale.CHINA, "[%s]", msg), tr);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, String.format(Locale.CHINA, "[%s]", msg), tr);
        }
    }

    public static void iAnyhow(String msg) {
        Log.i(TAG, String.format(Locale.CHINA, "[%s]", msg));
    }

    public static void i(String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, String.format(Locale.CHINA, "[%s]", msg));
        }
    }

    public static void i(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, String.format(Locale.CHINA, "[%s]", msg));
        }
    }

    public static void w(String msg) {
        if (BuildConfig.DEBUG) {
            Log.w(TAG, String.format(Locale.CHINA, "[%s]", msg));
        }
    }

    public static void w(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, String.format(Locale.CHINA, "[%s]", msg));
        }
    }

    public static void w(String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            Log.w(TAG, String.format(Locale.CHINA, "[%s]", msg), tr);
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, String.format(Locale.CHINA, "[%s]", msg), tr);
        }
    }

    public static void e(Throwable tr) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "[error]", tr);
        }
    }

    public static void e(String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, String.format(Locale.CHINA, "[%s]", msg), tr);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, String.format(Locale.CHINA, "[%s]", msg), tr);
        }
    }
}
