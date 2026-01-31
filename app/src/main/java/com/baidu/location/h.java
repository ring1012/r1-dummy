package com.baidu.location;

import com.unisound.vui.priority.PriorityMap;

/* loaded from: classes.dex */
public final class h {

    /* renamed from: a, reason: collision with root package name */
    public String f117a;
    public String b;
    public boolean c;
    public int d;
    public int e;
    public String f;
    public int g;
    public boolean h;
    public boolean i;
    public boolean j;
    public String k;
    public boolean l;
    public boolean m;
    public boolean n;
    public boolean o;
    public boolean p;
    public boolean q;
    public boolean r;
    public boolean s;
    protected a t;
    public int u;
    public float v;
    public int w;
    public int x;
    public int y;

    public enum a {
        Hight_Accuracy,
        Battery_Saving,
        Device_Sensors
    }

    public h() {
        this.f117a = "gcj02";
        this.b = "detail";
        this.c = false;
        this.d = 0;
        this.e = 12000;
        this.f = "SDK6.0";
        this.g = 1;
        this.h = false;
        this.i = true;
        this.j = false;
        this.k = "com.baidu.location.service_v2.9";
        this.l = false;
        this.m = true;
        this.n = false;
        this.o = false;
        this.p = false;
        this.q = false;
        this.r = false;
        this.s = false;
        this.u = 0;
        this.v = 0.5f;
        this.w = 0;
        this.x = 0;
        this.y = PriorityMap.PRIORITY_MAX;
    }

    public h(h hVar) {
        this.f117a = "gcj02";
        this.b = "detail";
        this.c = false;
        this.d = 0;
        this.e = 12000;
        this.f = "SDK6.0";
        this.g = 1;
        this.h = false;
        this.i = true;
        this.j = false;
        this.k = "com.baidu.location.service_v2.9";
        this.l = false;
        this.m = true;
        this.n = false;
        this.o = false;
        this.p = false;
        this.q = false;
        this.r = false;
        this.s = false;
        this.u = 0;
        this.v = 0.5f;
        this.w = 0;
        this.x = 0;
        this.y = PriorityMap.PRIORITY_MAX;
        this.f117a = hVar.f117a;
        this.b = hVar.b;
        this.c = hVar.c;
        this.d = hVar.d;
        this.e = hVar.e;
        this.f = hVar.f;
        this.g = hVar.g;
        this.h = hVar.h;
        this.k = hVar.k;
        this.i = hVar.i;
        this.l = hVar.l;
        this.m = hVar.m;
        this.j = hVar.j;
        this.t = hVar.t;
        this.o = hVar.o;
        this.p = hVar.p;
        this.q = hVar.q;
        this.r = hVar.r;
        this.n = hVar.n;
        this.s = hVar.s;
        this.u = hVar.u;
        this.v = hVar.v;
        this.w = hVar.w;
        this.x = hVar.x;
        this.y = hVar.y;
    }

    public String a() {
        return this.f117a;
    }

    public void a(int i) {
        this.d = i;
    }

    public void a(a aVar) {
        switch (aVar) {
            case Hight_Accuracy:
                this.c = true;
                this.g = 1;
                break;
            case Battery_Saving:
                this.c = false;
                this.g = 2;
                break;
            case Device_Sensors:
                this.g = 3;
                this.c = true;
                break;
            default:
                throw new IllegalArgumentException("Illegal this mode : " + aVar);
        }
        this.t = aVar;
    }

    public void a(boolean z) {
        if (z) {
            this.b = "all";
        } else {
            this.b = "noaddr";
        }
    }

    public boolean a(h hVar) {
        return this.f117a.equals(hVar.f117a) && this.b.equals(hVar.b) && this.c == hVar.c && this.d == hVar.d && this.e == hVar.e && this.f.equals(hVar.f) && this.h == hVar.h && this.g == hVar.g && this.i == hVar.i && this.l == hVar.l && this.m == hVar.m && this.o == hVar.o && this.p == hVar.p && this.q == hVar.q && this.r == hVar.r && this.n == hVar.n && this.u == hVar.u && this.v == hVar.v && this.w == hVar.w && this.x == hVar.x && this.y == hVar.y && this.s == hVar.s && this.t == hVar.t;
    }

    public String b() {
        return this.b;
    }

    public void b(boolean z) {
        this.n = z;
    }

    int c() {
        return this.u;
    }

    public int d() {
        return this.w;
    }

    public int e() {
        return this.x;
    }

    float f() {
        return this.v;
    }
}
