package com.baidu.location;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

/* loaded from: classes.dex */
class k implements ServiceConnection {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ g f121a;

    k(g gVar) {
        this.f121a = gVar;
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) throws RemoteException {
        this.f121a.f = new Messenger(iBinder);
        if (this.f121a.f == null) {
            return;
        }
        this.f121a.d = true;
        Log.d("baidu_location_client", "baidu location connected ...");
        if (this.f121a.v) {
            this.f121a.g.obtainMessage(2).sendToTarget();
            return;
        }
        try {
            Message messageObtain = Message.obtain((Handler) null, 11);
            messageObtain.replyTo = this.f121a.h;
            messageObtain.setData(this.f121a.e());
            this.f121a.f.send(messageObtain);
            this.f121a.d = true;
            if (this.f121a.c != null) {
                if (this.f121a.y.booleanValue()) {
                }
                this.f121a.g.obtainMessage(4).sendToTarget();
            }
        } catch (Exception e) {
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(ComponentName componentName) {
        this.f121a.f = null;
        this.f121a.d = false;
    }
}
