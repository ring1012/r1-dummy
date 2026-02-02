package com.baidu.location.c;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.os.RemoteException;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import com.baidu.location.a.h;
import com.baidu.location.a.j;
import com.baidu.location.a.k;
import com.baidu.location.a.l;
import com.baidu.location.a.n;
import com.baidu.location.a.t;
import com.baidu.location.a.u;
import com.baidu.location.bfold.g;
import com.baidu.location.e;
import com.unisound.client.SpeechConstants;
import java.io.IOException;

/* loaded from: classes.dex */
public class a extends Service implements e {

    /* renamed from: a, reason: collision with root package name */
    static HandlerC0008a f99a = null;
    private static long f = 0;
    private Looper c;
    private HandlerThread d;
    Messenger b = null;
    private boolean e = false;

    /* renamed from: com.baidu.location.c.a$a, reason: collision with other inner class name */
    public class HandlerC0008a extends Handler {
        public HandlerC0008a(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) throws RemoteException, IOException {
            if (f.f) {
                switch (message.what) {
                    case 11:
                        a.this.a(message);
                        break;
                    case 12:
                        a.this.b(message);
                        break;
                    case 15:
                        a.this.c(message);
                        break;
                    case 22:
                        k.c().b(message);
                        break;
                    case MotionEventCompat.AXIS_GENERIC_10 /* 41 */:
                        k.c().h();
                        break;
                    case SpeechConstants.FOURMIC_MODEL /* 401 */:
                        try {
                            message.getData();
                            break;
                        } catch (Exception e) {
                            break;
                        }
                    case 405:
                        byte[] byteArray = message.getData().getByteArray("errorid");
                        if (byteArray != null && byteArray.length > 0) {
                            new String(byteArray);
                            break;
                        }
                        break;
                    case 406:
                        h.a().e();
                        break;
                }
            }
            if (message.what == 1) {
                a.this.c();
            }
            if (message.what == 0) {
                a.this.b();
            }
            super.handleMessage(message);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(Message message) throws RemoteException {
        Log.d("baidu_location_service", "baidu location service register ...");
        com.baidu.location.a.a.a().a(message);
        n.b().c();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        j.a().a(f.b());
        l.a().b();
        h.a().b();
        d.a().b();
        b.a().b();
        com.baidu.location.d.b.a();
        k.c().d();
        g.a().c();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(Message message) throws RemoteException {
        com.baidu.location.a.a.a().b(message);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() throws IOException, RemoteException {
        g.a().d();
        d.a().e();
        b.a().c();
        k.c().e();
        h.a().c();
        u.e();
        com.baidu.location.a.a.a().b();
        com.baidu.location.a.d.a().b();
        l.a().c();
        Log.d("baidu_location_service", "baidu location service has stoped ...");
        if (this.e) {
            return;
        }
        Process.killProcess(Process.myPid());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(Message message) throws RemoteException {
        com.baidu.location.a.a.a().c(message);
    }

    @Override // com.baidu.location.e
    public double a() {
        return 7.320000171661377d;
    }

    @Override // com.baidu.location.e
    public void a(Context context) {
        f = System.currentTimeMillis();
        this.d = t.a();
        this.c = this.d.getLooper();
        if (this.c == null) {
            f99a = new HandlerC0008a(Looper.getMainLooper());
        } else {
            f99a = new HandlerC0008a(this.c);
        }
        this.b = new Messenger(f99a);
        f99a.sendEmptyMessage(0);
        Log.d("baidu_location_service", "baidu location service start1 ...20171027..." + Process.myPid());
    }

    @Override // com.baidu.location.e
    public boolean a(Intent intent) {
        return false;
    }

    @Override // android.app.Service, com.baidu.location.e
    public IBinder onBind(Intent intent) {
        Bundle extras = intent.getExtras();
        boolean z = false;
        if (extras != null) {
            com.baidu.location.d.b.g = extras.getString("key");
            com.baidu.location.d.b.f = extras.getString("sign");
            this.e = extras.getBoolean("kill_process");
            z = extras.getBoolean("cache_exception");
        }
        if (!z) {
        }
        return this.b.getBinder();
    }

    @Override // android.app.Service, com.baidu.location.e
    public void onDestroy() throws IOException, RemoteException {
        try {
            f99a.sendEmptyMessage(1);
        } catch (Exception e) {
            Log.d("baidu_location_service", "baidu location service stop exception...");
            c();
            Process.killProcess(Process.myPid());
        }
        Log.d("baidu_location_service", "baidu location service stop ...");
    }

    @Override // android.app.Service, com.baidu.location.e
    public int onStartCommand(Intent intent, int i, int i2) {
        return 1;
    }

    @Override // android.app.Service, com.baidu.location.e
    public void onTaskRemoved(Intent intent) {
        Log.d("baidu_location_service", "baidu location service remove task...");
    }
}
