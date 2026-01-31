package com.phicomm.speaker.device.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.phicomm.speaker.device.ui.MainActivity;
import com.unisound.vui.util.LogMgr;
import com.unisound.vui.util.SharedPreferencesHelper;
import com.unisound.vui.util.UserPerferenceUtil;
import org.eclipse.paho.client.mqttv3.a.d;

/* loaded from: classes.dex */
public class MessageReceiver extends BroadcastReceiver {
    public static final String SP_SYSTEM_BOOTLOADER = "systemBootloader";
    private static final String TAG = "MessageReceiver";
    private static Boolean sSystemBootloader = null;

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if ("android.intent.action.BOOT_COMPLETED".equals(action)) {
            Log.e(TAG, "action boot complted");
            setSystemBootloader(context, true);
            Intent mintent = new Intent(context, (Class<?>) MainActivity.class);
            mintent.addFlags(d.f489a);
            context.startActivity(mintent);
            if (UserPerferenceUtil.getData(context, UserPerferenceUtil.CAR_THEFT_UPLOAD, false)) {
                UserPerferenceUtil.putData(context, UserPerferenceUtil.CAR_THEFT_UPLOAD, (Boolean) false);
            } else {
                LogMgr.e(TAG, "CarTheft err ");
            }
        }
    }

    public static void setSystemBootloader(Context context, boolean isBootloader) {
        sSystemBootloader = Boolean.valueOf(isBootloader);
        SharedPreferencesHelper.getInstance(context).saveBooleanValue(SP_SYSTEM_BOOTLOADER, isBootloader);
    }

    public static boolean isSystemBootloader(Context context) {
        if (sSystemBootloader == null) {
            sSystemBootloader = Boolean.valueOf(SharedPreferencesHelper.getInstance(context).getBooleanValue(SP_SYSTEM_BOOTLOADER, false));
        }
        return sSystemBootloader.booleanValue();
    }
}
