package com.unisound.vui.util;

import android.support.annotation.Keep;
import android.text.TextUtils;
import android.util.Log;

@Keep
/* loaded from: classes.dex */
public class LogMgr {
    private static final int DEBUG = 4;
    private static final String DEFAULT_TAG = "UniCar";
    private static final int ERROR = 1;
    private static final int INFO = 3;
    private static final int SETTING_LEVEL = 1;
    private static final int VERBOSE = 5;
    private static final int WARN = 2;

    private LogMgr() {
    }

    public static void d(String message) {
        if (getLevel() <= 4) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.d(getDefaultTag(stackTraceElement), getLogInfo(stackTraceElement) + message);
        }
    }

    public static void d(String tag, String message) {
        if (getLevel() <= 4) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            if (TextUtils.isEmpty(tag)) {
                tag = getDefaultTag(stackTraceElement);
            }
            Log.d(tag, getLogInfo(stackTraceElement) + message);
        }
    }

    public static void d(String tag, String format, Object... args) {
        if (getLevel() <= 4) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            if (TextUtils.isEmpty(tag)) {
                tag = getDefaultTag(stackTraceElement);
            }
            Log.d(tag, getLogInfo(stackTraceElement) + String.format(format, args));
        }
    }

    public static void e(String message) {
        if (getLevel() <= 1) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.e(getDefaultTag(stackTraceElement), getLogInfo(stackTraceElement) + message);
        }
    }

    public static void e(String tag, String message) {
        if (getLevel() <= 1) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            if (TextUtils.isEmpty(tag)) {
                tag = getDefaultTag(stackTraceElement);
            }
            Log.e(tag, getLogInfo(stackTraceElement) + message);
        }
    }

    public static void e(String tag, String message, Throwable throwable) {
        if (getLevel() <= 1) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            if (TextUtils.isEmpty(tag)) {
                tag = getDefaultTag(stackTraceElement);
            }
            Log.e(tag, getLogInfo(stackTraceElement) + message, throwable);
        }
    }

    public static void e(String tag, String format, Object... args) {
        if (getLevel() <= 1) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            if (TextUtils.isEmpty(tag)) {
                tag = getDefaultTag(stackTraceElement);
            }
            Log.e(tag, getLogInfo(stackTraceElement) + String.format(format, args));
        }
    }

    public static String getDefaultTag(StackTraceElement stackTraceElement) {
        String str = null;
        try {
            str = stackTraceElement.getFileName().split("\\.")[0];
        } catch (Exception e) {
        }
        return "UniCar-" + str;
    }

    private static int getLevel() {
        return 1;
    }

    public static String getLogInfo(StackTraceElement stackTraceElement) {
        StringBuilder sb = new StringBuilder();
        Thread.currentThread().getName();
        Thread.currentThread().getId();
        stackTraceElement.getFileName();
        stackTraceElement.getClassName();
        stackTraceElement.getMethodName();
        int lineNumber = stackTraceElement.getLineNumber();
        sb.append("[ ");
        sb.append("lineNumber =" + lineNumber);
        sb.append(" ] ");
        return sb.toString();
    }

    public static void i(String message) {
        if (getLevel() <= 3) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.i(getDefaultTag(stackTraceElement), getLogInfo(stackTraceElement) + message);
        }
    }

    public static void i(String tag, String message) {
        if (getLevel() <= 3) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            if (TextUtils.isEmpty(tag)) {
                tag = getDefaultTag(stackTraceElement);
            }
            Log.i(tag, getLogInfo(stackTraceElement) + message);
        }
    }

    public static void v(String message) {
        if (getLevel() <= 5) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.v(getDefaultTag(stackTraceElement), getLogInfo(stackTraceElement) + message);
        }
    }

    public static void v(String tag, String message) {
        if (getLevel() <= 5) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            if (TextUtils.isEmpty(tag)) {
                tag = getDefaultTag(stackTraceElement);
            }
            Log.v(tag, getLogInfo(stackTraceElement) + message);
        }
    }

    public static void w(String message) {
        if (getLevel() <= 2) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.w(getDefaultTag(stackTraceElement), getLogInfo(stackTraceElement) + message);
        }
    }

    public static void w(String tag, String message) {
        if (getLevel() <= 2) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            if (TextUtils.isEmpty(tag)) {
                tag = getDefaultTag(stackTraceElement);
            }
            Log.w(tag, getLogInfo(stackTraceElement) + message);
        }
    }
}
