package com.baidu.location;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import com.baidu.location.a.c;
import com.google.android.exoplayer2.text.ttml.TtmlNode;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public final class g implements c.b {
    private h c;
    private Context e;
    private boolean z;

    /* renamed from: a, reason: collision with root package name */
    private long f114a = 0;
    private String b = null;
    private boolean d = false;
    private Messenger f = null;
    private ArrayList<d> i = null;
    private ArrayList<com.baidu.location.b> j = null;
    private c k = null;
    private boolean l = false;
    private boolean m = false;
    private boolean n = false;
    private b o = null;
    private boolean p = false;
    private final Object q = new Object();
    private long r = 0;
    private long s = 0;
    private String t = null;
    private boolean u = false;
    private boolean v = true;
    private Boolean w = false;
    private Boolean x = false;
    private Boolean y = true;
    private com.baidu.location.a.c A = null;
    private boolean B = false;
    private boolean C = false;
    private boolean D = false;
    private ServiceConnection E = new k(this);
    private a g = new a(Looper.getMainLooper());
    private final Messenger h = new Messenger(this.g);

    /* JADX INFO: Access modifiers changed from: private */
    class a extends Handler {
        a(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) throws RemoteException {
            switch (message.what) {
                case 1:
                    g.this.c();
                    break;
                case 2:
                    g.this.d();
                    break;
                case 3:
                    g.this.a(message);
                    break;
                case 4:
                    g.this.f();
                    break;
                case 5:
                    g.this.b(message);
                    break;
                case 6:
                    g.this.e(message);
                    break;
                case 21:
                    Bundle data = message.getData();
                    data.setClassLoader(c.class.getClassLoader());
                    c cVar = (c) data.getParcelable("locStr");
                    if (g.this.C || !g.this.B || cVar.h() != 66) {
                        if (!g.this.C && g.this.B) {
                            g.this.C = true;
                            break;
                        } else {
                            if (!g.this.C) {
                                g.this.C = true;
                            }
                            g.this.a(message, 21);
                            break;
                        }
                    }
                    break;
                case 54:
                    if (g.this.c.h) {
                        g.this.p = true;
                        break;
                    }
                    break;
                case 55:
                    if (g.this.c.h) {
                        g.this.p = false;
                        break;
                    }
                    break;
                case 303:
                    try {
                        Bundle data2 = message.getData();
                        int i = data2.getInt("loctype");
                        int i2 = data2.getInt("diagtype");
                        byte[] byteArray = data2.getByteArray("diagmessage");
                        if (i > 0 && i2 > 0 && byteArray != null && g.this.j != null) {
                            Iterator it = g.this.j.iterator();
                            while (it.hasNext()) {
                                ((com.baidu.location.b) it.next()).onLocDiagnosticMessage(i, i2, new String(byteArray, "UTF-8"));
                            }
                            break;
                        }
                    } catch (Exception e) {
                        return;
                    }
                    break;
                case 406:
                    try {
                        Bundle data3 = message.getData();
                        byte[] byteArray2 = data3.getByteArray("mac");
                        String str = byteArray2 != null ? new String(byteArray2, "UTF-8") : null;
                        int i3 = data3.getInt("hotspot", -1);
                        if (g.this.j != null) {
                            Iterator it2 = g.this.j.iterator();
                            while (it2.hasNext()) {
                                ((com.baidu.location.b) it2.next()).onConnectHotSpotMessage(str, i3);
                            }
                            break;
                        }
                    } catch (Exception e2) {
                        return;
                    }
                    break;
                case 701:
                    g.this.b((c) message.obj);
                    break;
                case 703:
                    try {
                        Bundle data4 = message.getData();
                        int i4 = data4.getInt(TtmlNode.ATTR_ID, 0);
                        if (i4 > 0) {
                            g.this.a(i4, (Notification) data4.getParcelable("notification"));
                            break;
                        }
                    } catch (Exception e3) {
                        return;
                    }
                    break;
                case 704:
                    try {
                        g.this.a(message.getData().getBoolean("removenotify"));
                        break;
                    } catch (Exception e4) {
                        return;
                    }
                case 1300:
                    g.this.c(message);
                    break;
                case 1400:
                    g.this.d(message);
                    break;
                default:
                    super.handleMessage(message);
                    break;
            }
        }
    }

    private class b implements Runnable {
        private b() {
        }

        /* synthetic */ b(g gVar, k kVar) {
            this();
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (g.this.q) {
                g.this.n = false;
                if (g.this.f == null || g.this.h == null) {
                    return;
                }
                if ((g.this.i == null || g.this.i.size() < 1) && (g.this.j == null || g.this.j.size() < 1)) {
                    return;
                }
                if (!g.this.m) {
                    g.this.g.obtainMessage(4).sendToTarget();
                    return;
                }
                if (g.this.o == null) {
                    g.this.o = g.this.new b();
                }
                g.this.g.postDelayed(g.this.o, g.this.c.d);
            }
        }
    }

    public g(Context context, h hVar) {
        this.c = new h();
        this.e = null;
        this.e = context;
        this.c = hVar;
    }

    private void a(int i) {
        if (this.k.g() == null) {
            this.k.d(this.c.f117a);
        }
        if (this.l || ((this.c.h && this.k.h() == 61) || this.k.h() == 66 || this.k.h() == 67 || this.u || this.k.h() == 161)) {
            if (this.i != null) {
                Iterator<d> it = this.i.iterator();
                while (it.hasNext()) {
                    it.next().a(this.k);
                }
            }
            if (this.j != null) {
                Iterator<com.baidu.location.b> it2 = this.j.iterator();
                while (it2.hasNext()) {
                    it2.next().onReceiveLocation(this.k);
                }
            }
            if (this.k.h() == 66 || this.k.h() == 67) {
                return;
            }
            this.l = false;
            this.s = System.currentTimeMillis();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(int i, Notification notification) {
        try {
            Intent intent = new Intent(this.e, (Class<?>) f.class);
            intent.putExtra("notification", notification);
            intent.putExtra(TtmlNode.ATTR_ID, i);
            intent.putExtra("command", 1);
            if (Build.VERSION.SDK_INT >= 26) {
                this.e.startForegroundService(intent);
            } else {
                this.e.startService(intent);
            }
            this.D = true;
        } catch (Exception e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(Message message) throws RemoteException {
        this.m = false;
        if (message == null || message.obj == null) {
            return;
        }
        h hVar = (h) message.obj;
        if (this.c.a(hVar)) {
            return;
        }
        if (this.c.d != hVar.d) {
            try {
                synchronized (this.q) {
                    if (this.n) {
                        this.g.removeCallbacks(this.o);
                        this.n = false;
                    }
                    if (hVar.d >= 1000 && !this.n) {
                        if (this.o == null) {
                            this.o = new b(this, null);
                        }
                        this.g.postDelayed(this.o, hVar.d);
                        this.n = true;
                    }
                }
            } catch (Exception e) {
            }
        }
        this.c = new h(hVar);
        if (this.f != null) {
            try {
                Message messageObtain = Message.obtain((Handler) null, 15);
                messageObtain.replyTo = this.h;
                messageObtain.setData(e());
                this.f.send(messageObtain);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(Message message, int i) {
        if (this.d) {
            try {
                Bundle data = message.getData();
                data.setClassLoader(c.class.getClassLoader());
                this.k = (c) data.getParcelable("locStr");
                if (this.k.h() == 61) {
                    this.r = System.currentTimeMillis();
                }
                a(i);
            } catch (Exception e) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(boolean z) {
        try {
            Intent intent = new Intent(this.e, (Class<?>) f.class);
            intent.putExtra("removenotify", z);
            intent.putExtra("command", 2);
            this.e.startService(intent);
            this.D = true;
        } catch (Exception e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(Message message) {
        if (message == null || message.obj == null) {
            return;
        }
        d dVar = (d) message.obj;
        if (this.i == null) {
            this.i = new ArrayList<>();
        }
        if (this.i.contains(dVar)) {
            return;
        }
        this.i.add(dVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(c cVar) {
        if (this.v) {
            return;
        }
        this.k = cVar;
        if (!this.C && cVar.h() == 161) {
            this.B = true;
        }
        if (this.i != null) {
            Iterator<d> it = this.i.iterator();
            while (it.hasNext()) {
                it.next().a(cVar);
            }
        }
        if (this.j != null) {
            Iterator<com.baidu.location.b> it2 = this.j.iterator();
            while (it2.hasNext()) {
                it2.next().onReceiveLocation(cVar);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        if (this.d) {
            return;
        }
        if (this.y.booleanValue()) {
            new l(this).start();
            this.y = false;
        }
        this.b = this.e.getPackageName();
        this.t = this.b + "_bdls_v2.9";
        Intent intent = new Intent(this.e, (Class<?>) f.class);
        try {
            intent.putExtra("debug_dev", this.z);
        } catch (Exception e) {
        }
        if (this.c == null) {
            this.c = new h();
        }
        intent.putExtra("cache_exception", this.c.l);
        intent.putExtra("kill_process", this.c.m);
        try {
            this.e.bindService(intent, this.E, 1);
        } catch (Exception e2) {
            e2.printStackTrace();
            this.d = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(Message message) {
        if (message == null || message.obj == null) {
            return;
        }
        com.baidu.location.b bVar = (com.baidu.location.b) message.obj;
        if (this.j == null) {
            this.j = new ArrayList<>();
        }
        if (this.j.contains(bVar)) {
            return;
        }
        this.j.add(bVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() throws RemoteException {
        if (!this.d || this.f == null) {
            return;
        }
        Message messageObtain = Message.obtain((Handler) null, 12);
        messageObtain.replyTo = this.h;
        try {
            this.f.send(messageObtain);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.e.unbindService(this.E);
            if (this.D) {
                try {
                    this.e.stopService(new Intent(this.e, (Class<?>) f.class));
                } catch (Exception e2) {
                }
                this.D = false;
            }
        } catch (Exception e3) {
        }
        synchronized (this.q) {
            try {
                if (this.n) {
                    this.g.removeCallbacks(this.o);
                    this.n = false;
                }
            } catch (Exception e4) {
            }
        }
        this.f = null;
        this.m = false;
        this.u = false;
        this.d = false;
        this.B = false;
        this.C = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d(Message message) {
        if (message == null || message.obj == null) {
            return;
        }
        com.baidu.location.b bVar = (com.baidu.location.b) message.obj;
        if (this.j == null || !this.j.contains(bVar)) {
            return;
        }
        this.j.remove(bVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Bundle e() {
        if (this.c == null) {
            return null;
        }
        Bundle bundle = new Bundle();
        bundle.putString("packName", this.b);
        bundle.putString("prodName", this.c.f);
        bundle.putString("coorType", this.c.f117a);
        bundle.putString("addrType", this.c.b);
        bundle.putBoolean("openGPS", this.c.c);
        bundle.putBoolean("location_change_notify", this.c.h);
        bundle.putInt("scanSpan", this.c.d);
        bundle.putBoolean("enableSimulateGps", this.c.j);
        bundle.putInt("timeOut", this.c.e);
        bundle.putInt("priority", this.c.g);
        bundle.putBoolean("map", this.w.booleanValue());
        bundle.putBoolean("import", this.x.booleanValue());
        bundle.putBoolean("needDirect", this.c.n);
        bundle.putBoolean("isneedaptag", this.c.o);
        bundle.putBoolean("isneedpoiregion", this.c.q);
        bundle.putBoolean("isneedregular", this.c.r);
        bundle.putBoolean("isneedaptagd", this.c.p);
        bundle.putBoolean("isneedaltitude", this.c.s);
        bundle.putInt("autoNotifyMaxInterval", this.c.c());
        bundle.putInt("autoNotifyMinTimeInterval", this.c.d());
        bundle.putInt("autoNotifyMinDistance", this.c.e());
        bundle.putFloat("autoNotifyLocSensitivity", this.c.f());
        bundle.putInt("wifitimeout", this.c.y);
        return bundle;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e(Message message) {
        if (message == null || message.obj == null) {
            return;
        }
        d dVar = (d) message.obj;
        if (this.i == null || !this.i.contains(dVar)) {
            return;
        }
        this.i.remove(dVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f() throws RemoteException {
        if (this.f == null) {
            return;
        }
        if ((System.currentTimeMillis() - this.r > 3000 || !this.c.h || this.m) && (!this.u || System.currentTimeMillis() - this.s > 20000 || this.m)) {
            Message messageObtain = Message.obtain((Handler) null, 22);
            if (this.m) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isWaitingLocTag", this.m);
                this.m = false;
                messageObtain.setData(bundle);
            }
            try {
                messageObtain.replyTo = this.h;
                this.f.send(messageObtain);
                this.f114a = System.currentTimeMillis();
                this.l = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        synchronized (this.q) {
            if (this.c != null && this.c.d >= 1000 && !this.n) {
                if (this.o == null) {
                    this.o = new b(this, null);
                }
                this.g.postDelayed(this.o, this.c.d);
                this.n = true;
            }
        }
    }

    public void a() {
        this.v = false;
        this.g.obtainMessage(1).sendToTarget();
    }

    public void a(com.baidu.location.b bVar) {
        if (bVar == null) {
            throw new IllegalStateException("please set a non-null listener");
        }
        Message messageObtainMessage = this.g.obtainMessage(1300);
        messageObtainMessage.obj = bVar;
        messageObtainMessage.sendToTarget();
    }

    @Override // com.baidu.location.a.c.b
    public void a(c cVar) {
        if ((!this.C || this.B) && cVar != null) {
            Message messageObtainMessage = this.g.obtainMessage(701);
            messageObtainMessage.obj = cVar;
            messageObtainMessage.sendToTarget();
        }
    }

    public void b() {
        this.v = true;
        this.g.obtainMessage(2).sendToTarget();
        this.A = null;
    }
}
