package com.unisound.vui.util;

import android.content.Context;
import android.content.pm.PackageInfo;

/* loaded from: classes.dex */
public class PackageInfoUtil {
    private PackageInfoUtil() {
    }

    private static PackageInfo getPackageInfo(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 16384);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    public static boolean isZhSystem(Context context) {
        return context.getResources().getConfiguration().locale.getLanguage().endsWith("zh");
    }
}
