package org.litepal.util;

import android.content.SharedPreferences;
import org.litepal.LitePalApplication;

/* loaded from: classes.dex */
public class SharedUtil {
    private static final String LITEPAL_PREPS = "litepal_prefs";
    private static final String VERSION = "litepal_version";

    public static void updateVersion(int newVersion) {
        SharedPreferences.Editor sEditor = LitePalApplication.getContext().getSharedPreferences(LITEPAL_PREPS, 0).edit();
        sEditor.putInt(VERSION, newVersion);
        sEditor.commit();
    }

    public static int getLastVersion() {
        SharedPreferences sPref = LitePalApplication.getContext().getSharedPreferences(LITEPAL_PREPS, 0);
        int version = sPref.getInt(VERSION, 0);
        return version;
    }
}
