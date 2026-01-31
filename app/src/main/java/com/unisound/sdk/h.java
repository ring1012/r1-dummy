package com.unisound.sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/* loaded from: classes.dex */
public class h extends BroadcastReceiver {

    /* renamed from: a, reason: collision with root package name */
    private static final String f344a = "ConnectionChangeReceiver";
    private static boolean c = false;
    private static boolean d = false;
    private bg b;

    h(bg bgVar) {
        this.b = bgVar;
    }

    public void a(boolean z) {
        c = z;
    }

    public boolean a() {
        return d;
    }

    public void b(boolean z) {
        d = z;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        try {
            com.unisound.common.y.c(com.unisound.common.y.v, "receiver onReceive intent = " + intent.getAction());
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivityManager != null) {
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(0);
                NetworkInfo networkInfo2 = connectivityManager.getNetworkInfo(1);
                if (networkInfo == null || networkInfo2 == null) {
                    com.unisound.common.y.a(f344a, "networkinfo is null!");
                } else {
                    com.unisound.common.y.a(com.unisound.common.y.v, "network status changed");
                    if (networkInfo.isConnected() || networkInfo2.isConnected()) {
                        com.unisound.common.y.c(com.unisound.common.y.v, "network isConnected");
                        if (c) {
                            this.b.refreshActivate();
                            c = false;
                        }
                    }
                }
            }
        } catch (Exception e) {
            com.unisound.common.y.c("ConnectionChangeReceiver onReceive error");
        }
    }
}
