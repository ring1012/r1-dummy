package org.greenrobot.greendao;

import android.util.Log;

/* loaded from: classes.dex */
public class DaoLog {
    public static final int ASSERT = 7;
    public static final int DEBUG = 3;
    public static final int ERROR = 6;
    public static final int INFO = 4;
    private static final String TAG = "greenDAO";
    public static final int VERBOSE = 2;
    public static final int WARN = 5;

    public static boolean isLoggable(int level) {
        return Log.isLoggable(TAG, level);
    }

    public static String getStackTraceString(Throwable th) {
        return Log.getStackTraceString(th);
    }

    public static int println(int level, String msg) {
        return Log.println(level, TAG, msg);
    }

    public static int v(String msg) {
        return Log.v(TAG, msg);
    }

    public static int v(String msg, Throwable th) {
        return Log.v(TAG, msg, th);
    }

    public static int d(String msg) {
        return Log.d(TAG, msg);
    }

    public static int d(String msg, Throwable th) {
        return Log.d(TAG, msg, th);
    }

    public static int i(String msg) {
        return Log.i(TAG, msg);
    }

    public static int i(String msg, Throwable th) {
        return Log.i(TAG, msg, th);
    }

    public static int w(String msg) {
        return Log.w(TAG, msg);
    }

    public static int w(String msg, Throwable th) {
        return Log.w(TAG, msg, th);
    }

    public static int w(Throwable th) {
        return Log.w(TAG, th);
    }

    public static int e(String msg) {
        return Log.w(TAG, msg);
    }

    public static int e(String msg, Throwable th) {
        return Log.e(TAG, msg, th);
    }
}
