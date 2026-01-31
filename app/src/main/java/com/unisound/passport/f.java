package com.unisound.passport;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;

/* loaded from: classes.dex */
class f extends BroadcastReceiver {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ d f286a;

    f(d dVar) {
        this.f286a = dVar;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
        boolean z = networkInfo != null && networkInfo.isConnected();
        this.f286a.a("Connectivity changed: connected=" + z);
        if (z) {
            this.f286a.o();
        } else if (this.f286a.c != null) {
            this.f286a.c.a();
            this.f286a.n();
            this.f286a.c = null;
        }
    }
}
