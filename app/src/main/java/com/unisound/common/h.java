package com.unisound.common;

import android.content.Context;
import android.media.AudioManager;

/* loaded from: classes.dex */
public class h {
    public static boolean a(Context context) {
        AudioManager audioManager;
        if (b(context) && (audioManager = (AudioManager) context.getSystemService("audio")) != null) {
            audioManager.setBluetoothScoOn(true);
            audioManager.startBluetoothSco();
        }
        return true;
    }

    public static boolean b(Context context) {
        return true;
    }

    public static boolean c(Context context) {
        AudioManager audioManager;
        if (!b(context) || (audioManager = (AudioManager) context.getSystemService("audio")) == null) {
            return true;
        }
        audioManager.setBluetoothScoOn(false);
        audioManager.stopBluetoothSco();
        return true;
    }
}
