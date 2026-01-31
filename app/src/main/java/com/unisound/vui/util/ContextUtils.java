package com.unisound.vui.util;

import android.content.Context;

/* loaded from: classes.dex */
public class ContextUtils {
    private static Context context;

    private ContextUtils() {
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context2) {
        context = context2.getApplicationContext();
    }
}
