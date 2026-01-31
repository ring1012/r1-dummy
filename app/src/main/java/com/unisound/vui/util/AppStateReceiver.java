package com.unisound.vui.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/* loaded from: classes.dex */
public class AppStateReceiver extends BroadcastReceiver {
    public static final String INSTALL_APP = "android.intent.action.PACKAGE_ADDED";
    public static final String UNINSTALL_APP = "android.intent.action.PACKAGE_REMOVED";

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(INSTALL_APP)) {
            com.unisound.vui.common.network.b.a(context);
        } else if (intent.getAction().equals(UNINSTALL_APP)) {
            com.unisound.vui.common.network.b.a(context);
        }
    }
}
