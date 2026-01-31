package com.unisound.sdk;

import com.unisound.client.ErrorCode;

/* loaded from: classes.dex */
class bb implements by {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ az f309a;

    bb(az azVar) {
        this.f309a = azVar;
    }

    @Override // com.unisound.sdk.by
    public void a() {
        az.sendEmptyMsg(this.f309a.p, 104);
    }

    @Override // com.unisound.sdk.by
    public void a(int i) {
        az.sendEmptyMsg(this.f309a.p, 107);
        this.f309a.i = 2504;
    }

    @Override // com.unisound.sdk.by
    public void a(boolean z) {
        if (this.f309a.c != null) {
            this.f309a.c.b(z);
        }
    }

    @Override // com.unisound.sdk.by
    public void b() {
        az.sendEmptyMsg(this.f309a.p, 105);
    }

    @Override // com.unisound.sdk.by
    public void b(int i) {
        az.sendMsg(this.f309a.p, com.unisound.common.ad.f, ErrorCode.toJsonMessage(i));
    }

    @Override // com.unisound.sdk.by
    public void c() {
        az.sendEmptyMsg(this.f309a.p, 106);
        this.f309a.i = 2502;
    }

    @Override // com.unisound.sdk.by
    public void d() {
        az.sendEmptyMsg(this.f309a.p, 110);
    }

    @Override // com.unisound.sdk.by
    public void e() {
        az.sendEmptyMsg(this.f309a.p, 108);
        this.f309a.i = 2503;
    }

    @Override // com.unisound.sdk.by
    public void f() {
        az.sendEmptyMsg(this.f309a.p, 109);
    }

    @Override // com.unisound.sdk.by
    public void g() {
        az.sendEmptyMsg(this.f309a.p, com.unisound.common.ad.r);
    }
}
