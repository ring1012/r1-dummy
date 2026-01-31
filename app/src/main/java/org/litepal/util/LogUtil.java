package org.litepal.util;

import android.util.Log;

/* loaded from: classes.dex */
public final class LogUtil {
    public static final int DEBUG = 2;
    public static final int ERROR = 5;
    public static int level = 5;

    public static void d(String tagName, String message) {
        if (level <= 2) {
            Log.d(tagName, message);
        }
    }

    public static void e(String tagName, Exception e) {
        if (level <= 5) {
            Log.e(tagName, e.getMessage(), e);
        }
    }
}
