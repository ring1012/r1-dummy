package com.phicomm.speaker.device.utils;

import android.content.Context;
import com.unisound.vui.util.SharedPreferencesHelper;

/* loaded from: classes.dex */
public class PhicommPerferenceUtil {
    private static final String DEVICE_AMBIENT_LIGHT_STATE = "ambientLightStatus";

    public static boolean getAmbientLightState(Context context) {
        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(context, "unicar_user_settings");
        return sharedPreferencesHelper.getBooleanValue(DEVICE_AMBIENT_LIGHT_STATE, false);
    }

    public static void setAmbientLightState(Context context, boolean isAmbientLightOn) {
        SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance(context, "unicar_user_settings");
        sharedPreferencesHelper.saveBooleanValue(DEVICE_AMBIENT_LIGHT_STATE, isAmbientLightOn);
    }
}
