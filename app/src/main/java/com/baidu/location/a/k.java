package com.baidu.location.a;

import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import com.baidu.location.a.i;
import com.google.android.exoplayer2.source.chunk.ChunkedTrackBlacklistUtil;
import java.util.List;

/* loaded from: classes.dex */
public class k extends i {
    private double A;
    public i.b f;
    private double z;
    private static k i = null;
    public static boolean h = false;
    final int e = 1000;
    private boolean j = true;
    private String k = null;
    private com.baidu.location.c l = null;
    private com.baidu.location.c m = null;
    private com.baidu.location.b.f n = null;
    private com.baidu.location.b.a o = null;
    private com.baidu.location.b.f p = null;
    private com.baidu.location.b.a q = null;
    private boolean r = true;
    private volatile boolean s = false;
    private boolean t = false;
    private long u = 0;
    private long v = 0;
    private com.baidu.location.a w = null;
    private String x = null;
    private List<com.baidu.location.i> y = null;
    private boolean B = false;
    private long C = 0;
    private long D = 0;
    private a E = null;
    private boolean F = false;
    private boolean G = false;
    private boolean H = true;
    public final Handler g = new i.a();
    private boolean I = false;
    private boolean J = false;
    private b K = null;
    private boolean L = false;
    private int M = 0;
    private long N = 0;
    private boolean O = true;

    private class a implements Runnable {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ k f73a;

        @Override // java.lang.Runnable
        public void run() {
            if (this.f73a.F) {
                this.f73a.F = false;
                if (!this.f73a.G) {
                }
            }
        }
    }

    private class b implements Runnable {
        private b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (k.this.L) {
                k.this.L = false;
            }
            if (k.this.t) {
                k.this.t = false;
                k.this.h(null);
            }
        }
    }

    private k() {
        this.f = null;
        this.f = new i.b();
    }

    private boolean a(com.baidu.location.b.a aVar) {
        this.b = com.baidu.location.b.b.a().f();
        if (this.b == aVar) {
            return false;
        }
        return this.b == null || aVar == null || !aVar.a(this.b);
    }

    private boolean a(com.baidu.location.b.f fVar) {
        this.f69a = com.baidu.location.b.g.a().o();
        if (fVar == this.f69a) {
            return false;
        }
        return this.f69a == null || fVar == null || !fVar.c(this.f69a);
    }

    public static synchronized k c() {
        if (i == null) {
            i = new k();
        }
        return i;
    }

    private void c(Message message) {
        boolean z = message.getData().getBoolean("isWaitingLocTag", false);
        if (z) {
            h = true;
        }
        if (z) {
        }
        int iD = com.baidu.location.a.a.a().d(message);
        switch (iD) {
            case 1:
                d(message);
                return;
            case 2:
                g(message);
                return;
            case 3:
                if (com.baidu.location.b.d.a().i()) {
                    e(message);
                    return;
                }
                return;
            default:
                throw new IllegalArgumentException(String.format("this type %d is illegal", Integer.valueOf(iD)));
        }
    }

    private void d(Message message) {
        if (com.baidu.location.b.d.a().i()) {
            e(message);
            m.a().c();
        } else {
            g(message);
            m.a().b();
        }
    }

    private void e(Message message) {
        com.baidu.location.c cVar = new com.baidu.location.c(com.baidu.location.b.d.a().f());
        if (com.baidu.location.d.j.g.equals("all") || com.baidu.location.d.j.h || com.baidu.location.d.j.i) {
            float[] fArr = new float[2];
            Location.distanceBetween(this.A, this.z, cVar.d(), cVar.e(), fArr);
            if (fArr[0] < 100.0f) {
                if (this.w != null) {
                    cVar.a(this.w);
                }
                if (this.x != null) {
                    cVar.g(this.x);
                }
                if (this.y != null) {
                    cVar.a(this.y);
                }
            } else {
                this.B = true;
                g(null);
            }
        }
        this.l = cVar;
        this.m = null;
        com.baidu.location.a.a.a().a(cVar);
    }

    private void f(Message message) {
        if (!com.baidu.location.b.g.a().f()) {
            h(message);
            return;
        }
        this.t = true;
        if (this.K == null) {
            this.K = new b();
        }
        if (this.L && this.K != null) {
            this.g.removeCallbacks(this.K);
        }
        this.g.postDelayed(this.K, 3500L);
        this.L = true;
    }

    private void g(Message message) {
        this.M = 0;
        if (!this.r) {
            f(message);
            this.D = SystemClock.uptimeMillis();
            return;
        }
        this.M = 1;
        this.D = SystemClock.uptimeMillis();
        if (com.baidu.location.b.g.a().j()) {
            f(message);
        } else {
            h(message);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h(Message message) {
        long j = 0;
        long jCurrentTimeMillis = System.currentTimeMillis() - this.u;
        if (!this.s || jCurrentTimeMillis > 12000) {
            if (System.currentTimeMillis() - this.u > 0 && System.currentTimeMillis() - this.u < 1000) {
                if (this.l != null) {
                    com.baidu.location.a.a.a().a(this.l);
                }
                k();
                return;
            }
            this.s = true;
            this.j = a(this.o);
            if (!a(this.n) && !this.j && this.l != null && !this.B) {
                if (this.m != null && System.currentTimeMillis() - this.v > 30000) {
                    this.l = this.m;
                    this.m = null;
                }
                if (m.a().d()) {
                    this.l.c(m.a().e());
                }
                if (this.l.h() == 62) {
                    long jCurrentTimeMillis2 = System.currentTimeMillis() - this.N;
                    if (jCurrentTimeMillis2 > 0) {
                        j = jCurrentTimeMillis2;
                    }
                }
                if (this.l.h() == 61 || this.l.h() == 161 || (this.l.h() == 62 && j < 15000)) {
                    com.baidu.location.a.a.a().a(this.l);
                    k();
                    return;
                }
            }
            this.u = System.currentTimeMillis();
            String strA = a((String) null);
            this.J = false;
            if (strA == null) {
                this.J = true;
                this.N = System.currentTimeMillis();
                String[] strArrJ = j();
                long jCurrentTimeMillis3 = System.currentTimeMillis();
                if (jCurrentTimeMillis3 - this.C > ChunkedTrackBlacklistUtil.DEFAULT_TRACK_BLACKLIST_MS) {
                    this.C = jCurrentTimeMillis3;
                }
                String strL = com.baidu.location.b.g.a().l();
                strA = strL != null ? strL + b() + strArrJ[0] : "" + b() + strArrJ[0];
                if (this.b != null && this.b.g() != null) {
                    strA = this.b.g() + strA;
                }
                String strA2 = com.baidu.location.d.b.a().a(true);
                if (strA2 != null) {
                    strA = strA + strA2;
                }
            }
            if (this.k != null) {
                strA = strA + this.k;
                this.k = null;
            }
            this.f.a(strA);
            this.o = this.b;
            this.n = this.f69a;
            if (this.r) {
                this.r = false;
                if (!com.baidu.location.b.g.i() || message == null || com.baidu.location.a.a.a().e(message) < 1000) {
                }
            }
            if (this.M > 0) {
                if (this.M == 2) {
                    com.baidu.location.b.g.a().f();
                }
                this.M = 0;
            }
        }
    }

    private String[] j() {
        boolean z;
        String[] strArr = {"", "Location failed beacuse we can not get any loc information!"};
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("&apl=");
        int iA = com.baidu.location.d.j.a(com.baidu.location.f.b());
        if (iA == 1) {
            strArr[1] = "Location failed beacuse we can not get any loc information in airplane mode, you can turn it off and try again!!";
        }
        stringBuffer.append(iA);
        String strC = com.baidu.location.d.j.c(com.baidu.location.f.b());
        if (strC.contains("0|0|")) {
            strArr[1] = "Location failed beacuse we can not get any loc information without any location permission!";
        }
        stringBuffer.append(strC);
        if (Build.VERSION.SDK_INT >= 23) {
            stringBuffer.append("&loc=");
            int iB = com.baidu.location.d.j.b(com.baidu.location.f.b());
            if (iB == 0) {
                strArr[1] = "Location failed beacuse we can not get any loc information with the phone loc mode is off, you can turn it on and try again!";
                z = true;
            } else {
                z = false;
            }
            stringBuffer.append(iB);
        } else {
            z = false;
        }
        if (Build.VERSION.SDK_INT >= 19) {
            stringBuffer.append("&lmd=");
            int iB2 = com.baidu.location.d.j.b(com.baidu.location.f.b());
            if (iB2 >= 0) {
                stringBuffer.append(iB2);
            }
        }
        String strG = com.baidu.location.b.b.a().g();
        String strG2 = com.baidu.location.b.g.a().g();
        stringBuffer.append(strG2);
        stringBuffer.append(strG);
        stringBuffer.append(com.baidu.location.d.j.d(com.baidu.location.f.b()));
        if (iA == 1) {
            com.baidu.location.a.b.a().a(62, 7, "Location failed beacuse we can not get any loc information in airplane mode, you can turn it off and try again!!");
        } else if (strC.contains("0|0|")) {
            com.baidu.location.a.b.a().a(62, 4, "Location failed beacuse we can not get any loc information without any location permission!");
        } else if (z) {
            com.baidu.location.a.b.a().a(62, 5, "Location failed beacuse we can not get any loc information with the phone loc mode is off, you can turn it on and try again!");
        } else if (strG == null || strG2 == null || !strG.equals("&sim=1") || strG2.equals("&wifio=1")) {
            com.baidu.location.a.b.a().a(62, 9, "Location failed beacuse we can not get any loc information!");
        } else {
            com.baidu.location.a.b.a().a(62, 6, "Location failed beacuse we can not get any loc information , you can insert a sim card or open wifi and try again!");
        }
        strArr[0] = stringBuffer.toString();
        return strArr;
    }

    private void k() {
        this.s = false;
        this.G = false;
        this.H = false;
        this.B = false;
        l();
        if (this.O) {
            this.O = false;
        }
    }

    private void l() {
        if (this.l != null) {
            u.a().c();
        }
    }

    public com.baidu.location.a a(com.baidu.location.c cVar) {
        if (com.baidu.location.d.j.g.equals("all") || com.baidu.location.d.j.h || com.baidu.location.d.j.i) {
            float[] fArr = new float[2];
            Location.distanceBetween(this.A, this.z, cVar.d(), cVar.e(), fArr);
            if (fArr[0] >= 100.0f) {
                this.x = null;
                this.y = null;
                this.B = true;
                g(null);
            } else if (this.w != null) {
                return this.w;
            }
        }
        return null;
    }

    @Override // com.baidu.location.a.i
    public void a() {
        if (this.E != null && this.F) {
            this.F = false;
            this.g.removeCallbacks(this.E);
        }
        if (!com.baidu.location.b.d.a().i()) {
            if (this.G) {
                k();
                return;
            }
            if (this.j || this.l == null) {
                com.baidu.location.c cVar = new com.baidu.location.c();
                cVar.d(63);
                this.l = null;
                com.baidu.location.a.a.a().a(cVar);
            } else {
                com.baidu.location.a.a.a().a(this.l);
            }
            this.m = null;
            k();
            return;
        }
        com.baidu.location.c cVar2 = new com.baidu.location.c(com.baidu.location.b.d.a().f());
        if (com.baidu.location.d.j.g.equals("all") || com.baidu.location.d.j.h || com.baidu.location.d.j.i) {
            float[] fArr = new float[2];
            Location.distanceBetween(this.A, this.z, cVar2.d(), cVar2.e(), fArr);
            if (fArr[0] < 100.0f) {
                if (this.w != null) {
                    cVar2.a(this.w);
                }
                if (this.x != null) {
                    cVar2.g(this.x);
                }
                if (this.y != null) {
                    cVar2.a(this.y);
                }
            }
        }
        com.baidu.location.a.a.a().a(cVar2);
        k();
    }

    @Override // com.baidu.location.a.i
    public void a(Message message) {
        if (this.E != null && this.F) {
            this.F = false;
            this.g.removeCallbacks(this.E);
        }
        com.baidu.location.c cVar = (com.baidu.location.c) message.obj;
        if (cVar != null && cVar.h() == 167 && this.J) {
            cVar.d(62);
        }
        b(cVar);
    }

    public void b(Message message) {
        if (this.I) {
            c(message);
        }
    }

    public void b(com.baidu.location.c cVar) {
        String strG;
        int iB;
        boolean z = true;
        new com.baidu.location.c(cVar);
        if (cVar.j()) {
            this.w = cVar.k();
            this.z = cVar.e();
            this.A = cVar.d();
        }
        if (cVar.r() != null) {
            this.x = cVar.r();
            this.z = cVar.e();
            this.A = cVar.d();
        }
        if (cVar.a() != null) {
            this.y = cVar.a();
            this.z = cVar.e();
            this.A = cVar.d();
        }
        if (com.baidu.location.b.d.a().i()) {
            com.baidu.location.c cVar2 = new com.baidu.location.c(com.baidu.location.b.d.a().f());
            if (com.baidu.location.d.j.g.equals("all") || com.baidu.location.d.j.h || com.baidu.location.d.j.i) {
                float[] fArr = new float[2];
                Location.distanceBetween(this.A, this.z, cVar2.d(), cVar2.e(), fArr);
                if (fArr[0] < 100.0f) {
                    if (this.w != null) {
                        cVar2.a(this.w);
                    }
                    if (this.x != null) {
                        cVar2.g(this.x);
                    }
                    if (this.y != null) {
                        cVar2.a(this.y);
                    }
                }
            }
            com.baidu.location.a.a.a().a(cVar2);
            k();
            return;
        }
        if (this.G) {
            float[] fArr2 = new float[2];
            if (this.l != null) {
                Location.distanceBetween(this.l.d(), this.l.e(), cVar.d(), cVar.e(), fArr2);
            }
            if (fArr2[0] > 10.0f) {
                this.l = cVar;
                if (!this.H) {
                    this.H = false;
                    com.baidu.location.a.a.a().a(cVar);
                }
            } else if (cVar.b() > -1) {
                this.l = cVar;
                com.baidu.location.a.a.a().a(cVar);
            }
            k();
            return;
        }
        if (cVar.h() == 167) {
            com.baidu.location.a.b.a().a(167, 8, "NetWork location failed because baidu location service can not caculate the location!");
        } else if (cVar.h() == 161) {
            if (Build.VERSION.SDK_INT >= 19 && ((iB = com.baidu.location.d.j.b(com.baidu.location.f.b())) == 0 || iB == 2)) {
                com.baidu.location.a.b.a().a(161, 1, "NetWork location successful, open gps will be better!");
            } else if (cVar.f() >= 100.0f && cVar.s() != null && cVar.s().equals("cl") && (strG = com.baidu.location.b.g.a().g()) != null && !strG.equals("&wifio=1")) {
                com.baidu.location.a.b.a().a(161, 2, "NetWork location successful, open wifi will be better!");
            }
        }
        this.m = null;
        if (cVar.h() == 161 && "cl".equals(cVar.s()) && this.l != null && this.l.h() == 161 && "wf".equals(this.l.s()) && System.currentTimeMillis() - this.v < 30000) {
            this.m = cVar;
        } else {
            z = false;
        }
        if (z) {
            com.baidu.location.a.a.a().a(this.l);
        } else {
            com.baidu.location.a.a.a().a(cVar);
            this.v = System.currentTimeMillis();
        }
        if (!com.baidu.location.d.j.a(cVar)) {
            this.l = null;
        } else if (!z) {
            this.l = cVar;
        }
        int iA = com.baidu.location.d.j.a(c, "ssid\":\"", "\"");
        if (iA == Integer.MIN_VALUE || this.n == null) {
            this.k = null;
        } else {
            this.k = this.n.c(iA);
        }
        if (com.baidu.location.b.g.i()) {
        }
        k();
    }

    public void c(com.baidu.location.c cVar) {
        this.l = new com.baidu.location.c(cVar);
    }

    public void d() {
        this.r = true;
        this.s = false;
        this.I = true;
    }

    public void e() {
        this.s = false;
        this.t = false;
        this.G = false;
        this.H = true;
        i();
        this.I = false;
    }

    public String f() {
        return this.x;
    }

    public List<com.baidu.location.i> g() {
        return this.y;
    }

    public void h() {
        if (this.t) {
            h(null);
            this.t = false;
        }
    }

    public void i() {
        this.l = null;
    }
}
