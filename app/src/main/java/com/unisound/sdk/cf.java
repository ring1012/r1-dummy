package com.unisound.sdk;

/* loaded from: classes.dex */
public class cf {

    /* renamed from: a, reason: collision with root package name */
    cg f330a;
    private Object[] c;
    private boolean d = false;
    ch b = null;

    public cf(cg cgVar) {
        this.f330a = cgVar;
    }

    public void a() {
        b();
        this.b = new ch(this, this.f330a);
        if (this.c != null) {
            this.b.execute(this.c);
        }
    }

    public void a(Object... objArr) {
        this.c = objArr;
    }

    public void b() {
        if (this.b != null) {
            this.b.a();
            this.b = null;
        }
    }

    public boolean c() {
        return this.d;
    }
}
