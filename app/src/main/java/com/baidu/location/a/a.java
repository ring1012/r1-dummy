package com.baidu.location.a;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import cn.yunzhisheng.common.PinyinConverter;
import com.baidu.location.Jni;
import com.unisound.vui.priority.PriorityMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class a {
    private static a d = null;
    private ArrayList<C0005a> e;
    private boolean f = false;

    /* renamed from: a, reason: collision with root package name */
    public boolean f54a = false;
    boolean b = false;
    private com.baidu.location.c g = null;
    private com.baidu.location.c h = null;
    private com.baidu.location.c i = null;
    int c = 0;
    private com.baidu.location.c j = null;
    private boolean k = false;
    private boolean l = false;
    private b m = null;

    /* renamed from: com.baidu.location.a.a$a, reason: collision with other inner class name */
    private class C0005a {

        /* renamed from: a, reason: collision with root package name */
        public String f55a;
        public Messenger b;
        public com.baidu.location.h c = new com.baidu.location.h();
        public int d = 0;
        final /* synthetic */ a e;

        public C0005a(a aVar, Message message) {
            boolean z = true;
            this.e = aVar;
            this.f55a = null;
            this.b = null;
            this.b = message.replyTo;
            this.f55a = message.getData().getString("packName");
            this.c.f = message.getData().getString("prodName");
            com.baidu.location.d.b.a().a(this.c.f, this.f55a);
            this.c.f117a = message.getData().getString("coorType");
            this.c.b = message.getData().getString("addrType");
            this.c.j = message.getData().getBoolean("enableSimulateGps", false);
            com.baidu.location.d.j.l = com.baidu.location.d.j.l || this.c.j;
            if (!com.baidu.location.d.j.g.equals("all")) {
                com.baidu.location.d.j.g = this.c.b;
            }
            this.c.c = message.getData().getBoolean("openGPS");
            this.c.d = message.getData().getInt("scanSpan");
            this.c.e = message.getData().getInt("timeOut");
            this.c.g = message.getData().getInt("priority");
            this.c.h = message.getData().getBoolean("location_change_notify");
            this.c.n = message.getData().getBoolean("needDirect", false);
            this.c.s = message.getData().getBoolean("isneedaltitude", false);
            com.baidu.location.d.j.h = com.baidu.location.d.j.h || message.getData().getBoolean("isneedaptag", false);
            if (!com.baidu.location.d.j.i && !message.getData().getBoolean("isneedaptagd", false)) {
                z = false;
            }
            com.baidu.location.d.j.i = z;
            com.baidu.location.d.j.Q = message.getData().getFloat("autoNotifyLocSensitivity", 0.5f);
            int i = message.getData().getInt("wifitimeout", PriorityMap.PRIORITY_MAX);
            if (i < com.baidu.location.d.j.ae) {
                com.baidu.location.d.j.ae = i;
            }
            int i2 = message.getData().getInt("autoNotifyMaxInterval", 0);
            if (i2 >= com.baidu.location.d.j.V) {
                com.baidu.location.d.j.V = i2;
            }
            int i3 = message.getData().getInt("autoNotifyMinDistance", 0);
            if (i3 >= com.baidu.location.d.j.X) {
                com.baidu.location.d.j.X = i3;
            }
            int i4 = message.getData().getInt("autoNotifyMinTimeInterval", 0);
            if (i4 >= com.baidu.location.d.j.W) {
                com.baidu.location.d.j.W = i4;
            }
            if (this.c.d >= 1000) {
            }
            if (this.c.n || this.c.s) {
                m.a().a(this.c.n);
                m.a().b();
            }
            aVar.b |= this.c.s;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void a(int i) throws RemoteException {
            Message messageObtain = Message.obtain((Handler) null, i);
            try {
                if (this.b != null) {
                    this.b.send(messageObtain);
                }
                this.d = 0;
            } catch (Exception e) {
                if (e instanceof DeadObjectException) {
                    this.d++;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void a(int i, Bundle bundle) throws RemoteException {
            Message messageObtain = Message.obtain((Handler) null, i);
            messageObtain.setData(bundle);
            try {
                if (this.b != null) {
                    this.b.send(messageObtain);
                }
                this.d = 0;
            } catch (Exception e) {
                if (e instanceof DeadObjectException) {
                    this.d++;
                }
                e.printStackTrace();
            }
        }

        private void a(int i, String str, com.baidu.location.c cVar) throws RemoteException {
            Bundle bundle = new Bundle();
            bundle.putParcelable(str, cVar);
            bundle.setClassLoader(com.baidu.location.c.class.getClassLoader());
            Message messageObtain = Message.obtain((Handler) null, i);
            messageObtain.setData(bundle);
            try {
                if (this.b != null) {
                    this.b.send(messageObtain);
                }
                this.d = 0;
            } catch (Exception e) {
                if (e instanceof DeadObjectException) {
                    this.d++;
                }
            }
        }

        public void a() throws RemoteException {
            if (this.c.h) {
                if (com.baidu.location.d.j.b) {
                    a(54);
                } else {
                    a(55);
                }
            }
        }

        public void a(com.baidu.location.c cVar) throws RemoteException {
            a(cVar, 21);
        }

        public void a(com.baidu.location.c cVar, int i) throws RemoteException {
            com.baidu.location.c cVar2 = new com.baidu.location.c(cVar);
            if (i == 21) {
                a(27, "locStr", cVar2);
            }
            if (this.c.f117a != null && !this.c.f117a.equals("gcj02")) {
                double dE = cVar2.e();
                double d = cVar2.d();
                if (dE != Double.MIN_VALUE && d != Double.MIN_VALUE) {
                    if ((cVar2.g() != null && cVar2.g().equals("gcj02")) || cVar2.g() == null) {
                        double[] dArrA = Jni.a(dE, d, this.c.f117a);
                        cVar2.b(dArrA[0]);
                        cVar2.a(dArrA[1]);
                        cVar2.d(this.c.f117a);
                    } else if (cVar2.g() != null && cVar2.g().equals("wgs84") && !this.c.f117a.equals("bd09ll")) {
                        double[] dArrA2 = Jni.a(dE, d, "wgs842mc");
                        cVar2.b(dArrA2[0]);
                        cVar2.a(dArrA2[1]);
                        cVar2.d("wgs84mc");
                    }
                }
            }
            a(i, "locStr", cVar2);
        }
    }

    private class b implements Runnable {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ a f56a;
        private int b;
        private boolean c;

        @Override // java.lang.Runnable
        public void run() {
            if (this.c) {
                return;
            }
            this.b++;
            this.f56a.l = false;
        }
    }

    private a() {
        this.e = null;
        this.e = new ArrayList<>();
    }

    private C0005a a(Messenger messenger) {
        if (this.e == null) {
            return null;
        }
        Iterator<C0005a> it = this.e.iterator();
        while (it.hasNext()) {
            C0005a next = it.next();
            if (next.b.equals(messenger)) {
                return next;
            }
        }
        return null;
    }

    public static a a() {
        if (d == null) {
            d = new a();
        }
        return d;
    }

    private void a(C0005a c0005a) throws RemoteException {
        if (c0005a == null) {
            return;
        }
        if (a(c0005a.b) != null) {
            c0005a.a(14);
        } else {
            this.e.add(c0005a);
            c0005a.a(13);
        }
    }

    private void b(String str) {
        Intent intent = new Intent("com.baidu.location.flp.log");
        intent.setPackage("com.baidu.baidulocationdemo");
        intent.putExtra("data", str);
        intent.putExtra("pack", com.baidu.location.d.b.d);
        intent.putExtra("tag", "state");
        com.baidu.location.f.b().sendBroadcast(intent);
    }

    private void e() throws RemoteException {
        f();
        d();
    }

    private void f() {
        Iterator<C0005a> it = this.e.iterator();
        boolean z = false;
        boolean z2 = false;
        while (it.hasNext()) {
            C0005a next = it.next();
            if (next.c.c) {
                z2 = true;
            }
            z = next.c.h ? true : z;
        }
        com.baidu.location.d.j.f112a = z;
        if (this.f != z2) {
            this.f = z2;
            com.baidu.location.b.d.a().a(this.f);
        }
    }

    public void a(Bundle bundle, int i) {
        Iterator<C0005a> it = this.e.iterator();
        while (it.hasNext()) {
            try {
                C0005a next = it.next();
                next.a(i, bundle);
                if (next.d > 4) {
                    it.remove();
                }
            } catch (Exception e) {
                return;
            }
        }
    }

    public void a(Message message) {
        if (message == null || message.replyTo == null) {
            return;
        }
        this.f54a = true;
        com.baidu.location.b.g.a().b();
        a(new C0005a(this, message));
        e();
        if (this.k) {
            b("start");
            this.c = 0;
        }
    }

    public void a(com.baidu.location.c cVar) {
        b(cVar);
    }

    public void a(String str) {
        c(new com.baidu.location.c(str));
    }

    public void b() {
        this.e.clear();
        this.g = null;
        e();
    }

    public void b(Message message) {
        C0005a c0005aA = a(message.replyTo);
        if (c0005aA != null) {
            this.e.remove(c0005aA);
        }
        m.a().c();
        e();
        if (this.k) {
            b("stop");
            this.c = 0;
        }
    }

    public void b(com.baidu.location.c cVar) {
        boolean z = k.h;
        if (z) {
            k.h = false;
        }
        if (com.baidu.location.d.j.V >= 10000 && (cVar.h() == 61 || cVar.h() == 161 || cVar.h() == 66)) {
            if (this.g != null) {
                float[] fArr = new float[1];
                Location.distanceBetween(this.g.d(), this.g.e(), cVar.d(), cVar.e(), fArr);
                if (fArr[0] <= com.baidu.location.d.j.X && !z) {
                    return;
                }
                this.g = null;
                this.g = new com.baidu.location.c(cVar);
            } else {
                this.g = new com.baidu.location.c(cVar);
            }
        }
        if (cVar == null || cVar.h() != 161 || j.a().b()) {
            Iterator<C0005a> it = this.e.iterator();
            while (it.hasNext()) {
                try {
                    C0005a next = it.next();
                    next.a(cVar);
                    if (next.d > 4) {
                        it.remove();
                    }
                } catch (Exception e) {
                    return;
                }
            }
            return;
        }
        if (this.h == null) {
            this.h = new com.baidu.location.c();
            this.h.d(505);
        }
        Iterator<C0005a> it2 = this.e.iterator();
        while (it2.hasNext()) {
            try {
                C0005a next2 = it2.next();
                next2.a(this.h);
                if (next2.d > 4) {
                    it2.remove();
                }
            } catch (Exception e2) {
                return;
            }
        }
    }

    public String c() {
        StringBuffer stringBuffer = new StringBuffer(256);
        if (this.e.isEmpty()) {
            return "&prod=" + com.baidu.location.d.b.e + ":" + com.baidu.location.d.b.d;
        }
        C0005a c0005a = this.e.get(0);
        if (c0005a.c.f != null) {
            stringBuffer.append(c0005a.c.f);
        }
        if (c0005a.f55a != null) {
            stringBuffer.append(":");
            stringBuffer.append(c0005a.f55a);
            stringBuffer.append(PinyinConverter.PINYIN_EXCLUDE);
        }
        String string = stringBuffer.toString();
        if (string == null || string.equals("")) {
            return null;
        }
        return "&prod=" + string;
    }

    public void c(com.baidu.location.c cVar) {
        com.baidu.location.a aVarA = k.c().a(cVar);
        String strF = k.c().f();
        List<com.baidu.location.i> listG = k.c().g();
        if (aVarA != null) {
            cVar.a(aVarA);
        }
        if (strF != null) {
            cVar.g(strF);
        }
        if (listG != null) {
            cVar.a(listG);
        }
        k.c().c(cVar);
        a(cVar);
    }

    public boolean c(Message message) {
        boolean z = true;
        C0005a c0005aA = a(message.replyTo);
        if (c0005aA == null) {
            return false;
        }
        int i = c0005aA.c.d;
        c0005aA.c.d = message.getData().getInt("scanSpan", c0005aA.c.d);
        if (c0005aA.c.d < 1000) {
            m.a().c();
            this.f54a = false;
        } else {
            this.f54a = true;
        }
        if (c0005aA.c.d <= 999 || i >= 1000) {
            z = false;
        } else {
            if (c0005aA.c.n || c0005aA.c.s) {
                m.a().a(c0005aA.c.n);
                m.a().b();
            }
            this.b |= c0005aA.c.s;
        }
        c0005aA.c.c = message.getData().getBoolean("openGPS", c0005aA.c.c);
        String string = message.getData().getString("coorType");
        com.baidu.location.h hVar = c0005aA.c;
        if (string == null || string.equals("")) {
            string = c0005aA.c.f117a;
        }
        hVar.f117a = string;
        String string2 = message.getData().getString("addrType");
        com.baidu.location.h hVar2 = c0005aA.c;
        if (string2 == null || string2.equals("")) {
            string2 = c0005aA.c.b;
        }
        hVar2.b = string2;
        if (!com.baidu.location.d.j.g.equals(c0005aA.c.b)) {
            k.c().i();
        }
        c0005aA.c.e = message.getData().getInt("timeOut", c0005aA.c.e);
        c0005aA.c.h = message.getData().getBoolean("location_change_notify", c0005aA.c.h);
        c0005aA.c.g = message.getData().getInt("priority", c0005aA.c.g);
        int i2 = message.getData().getInt("wifitimeout", PriorityMap.PRIORITY_MAX);
        if (i2 < com.baidu.location.d.j.ae) {
            com.baidu.location.d.j.ae = i2;
        }
        e();
        return z;
    }

    public int d(Message message) {
        C0005a c0005aA;
        if (message == null || message.replyTo == null || (c0005aA = a(message.replyTo)) == null || c0005aA.c == null) {
            return 1;
        }
        return c0005aA.c.g;
    }

    public void d() throws RemoteException {
        Iterator<C0005a> it = this.e.iterator();
        while (it.hasNext()) {
            it.next().a();
        }
    }

    public int e(Message message) {
        C0005a c0005aA;
        if (message == null || message.replyTo == null || (c0005aA = a(message.replyTo)) == null || c0005aA.c == null) {
            return 1000;
        }
        return c0005aA.c.d;
    }
}
