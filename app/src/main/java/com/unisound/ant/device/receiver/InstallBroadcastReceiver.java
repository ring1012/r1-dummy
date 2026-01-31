package com.unisound.ant.device.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.unisound.vui.util.LogMgr;

/* loaded from: classes.dex */
public class InstallBroadcastReceiver extends BroadcastReceiver {
    private static final String InstallFailAction = "com.unisound.pandora.installfailed";
    private static final String TAG = "InstallBroadcastReceiver";
    private InstallStateListener stateListener;

    public interface InstallStateListener {
        void onInstallFailed();
    }

    public void setStateListener(InstallStateListener listener) {
        this.stateListener = listener;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (InstallFailAction.equals(action)) {
            this.stateListener.onInstallFailed();
        }
    }

    public void registerInstallStateReceiver(Context context) {
        IntentFilter filter = new IntentFilter(InstallFailAction);
        try {
            context.registerReceiver(this, filter);
        } catch (Exception e) {
            LogMgr.e(TAG, "-->>this receiver is alreeady register ");
        }
    }

    public void unregisterInstallStateReceiver(Context context) {
        try {
            context.unregisterReceiver(this);
        } catch (Exception e) {
            LogMgr.e(TAG, "-->>this receiver is not register or all ready unregister");
        }
    }
}
