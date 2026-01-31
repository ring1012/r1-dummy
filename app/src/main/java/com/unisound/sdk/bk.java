package com.unisound.sdk;

import com.unisound.client.ErrorCode;

/* loaded from: classes.dex */
class bk implements ad {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bg f316a;

    bk(bg bgVar) {
        this.f316a = bgVar;
    }

    @Override // com.unisound.sdk.e
    public void a() {
    }

    @Override // com.unisound.sdk.e
    public void a(int i) {
    }

    @Override // com.unisound.common.ar
    public void a(int i, int i2, Object obj) {
    }

    @Override // com.unisound.sdk.ah
    public void a(al alVar) {
        this.f316a.aE = true;
        String strB = alVar.b();
        if (!this.f316a.aB.equals(strB)) {
            com.unisound.common.v.a();
            this.f316a.v.clear();
        }
        this.f316a.aB = strB;
        this.f316a.a(alVar);
        this.f316a.b(alVar);
        this.f316a.c(alVar);
        this.f316a.aE = false;
    }

    @Override // com.unisound.sdk.ad
    public void a(boolean z, byte[] bArr, int i, int i2) {
    }

    @Override // com.unisound.sdk.e
    public void b() {
    }

    @Override // com.unisound.sdk.ad
    public void b(int i) {
        this.f316a.aE = true;
        this.f316a.E = this.f316a.o.f();
        if (i != 0) {
            this.f316a.b.Q(i);
            if (i == -69001) {
                com.unisound.c.a.a(true);
                this.f316a.refreshActivate();
            }
            this.f316a.q(i);
            if (this.f316a.D) {
                this.f316a.e.b();
            }
        }
        if (this.f316a.z != null) {
            this.f316a.C = true;
            this.f316a.am.sendEmptyMessage(7);
            this.f316a.J();
        }
        if (i != 0) {
            com.unisound.common.y.a(this.f316a.l, true, this.f316a.x, this.f316a.E, i, ErrorCode.toMessage(i));
        } else {
            com.unisound.common.y.a(this.f316a.l, false, this.f316a.x, this.f316a.E, i, ErrorCode.toMessage(i));
        }
        this.f316a.aE = false;
    }

    @Override // com.unisound.sdk.e
    public void c() {
        if (this.f316a.am != null) {
            this.f316a.am.sendEmptyMessage(25);
        }
    }

    @Override // com.unisound.sdk.ad
    public void c(int i) {
        this.f316a.l(i);
    }

    @Override // com.unisound.sdk.ad
    public void d() {
    }

    @Override // com.unisound.sdk.ad
    public void d(int i) {
        if (this.f316a.z != null) {
            this.f316a.q(i);
        }
    }

    @Override // com.unisound.sdk.ad
    public void e() {
    }

    @Override // com.unisound.sdk.ad
    public void f() {
    }

    @Override // com.unisound.sdk.ad
    public void g() {
    }

    @Override // com.unisound.sdk.ad
    public void h() {
        if (this.f316a.z != null) {
            this.f316a.am.sendEmptyMessage(30);
        }
    }
}
