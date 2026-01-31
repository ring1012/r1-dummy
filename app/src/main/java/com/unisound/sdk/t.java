package com.unisound.sdk;

import android.os.Message;
import cn.yunzhisheng.asr.VAD;

/* loaded from: classes.dex */
class t implements ar {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ o f354a;

    private t(o oVar) {
        this.f354a = oVar;
    }

    /* synthetic */ t(o oVar, p pVar) {
        this(oVar);
    }

    @Override // com.unisound.sdk.ar
    public void a() {
        this.f354a.i.b();
        this.f354a.k.c();
        this.f354a.l();
    }

    @Override // com.unisound.sdk.ar
    public void a(int i) {
        this.f354a.i.a(i);
        this.f354a.b(i);
    }

    @Override // com.unisound.common.ar
    public void a(int i, int i2, Object obj) {
        this.f354a.a(i, i2, obj);
    }

    @Override // com.unisound.sdk.cr
    public void a(VAD vad) {
        com.unisound.common.y.c("FixRecognizerInterface onVADTimeout");
        this.f354a.i.e();
        this.f354a.a(vad);
    }

    @Override // com.unisound.sdk.ar
    public void a(String str, boolean z, int i, int i2) {
        this.f354a.i.a(str, z);
        this.f354a.a(str, z, i, i2);
    }

    @Override // com.unisound.sdk.ar
    public void a(String str, boolean z, int i, long j, long j2, int i2, int i3) {
        this.f354a.a(str, z, i, j, j2, i2, i3);
    }

    @Override // com.unisound.common.ac
    public boolean a(Message message) {
        return this.f354a.a(message);
    }

    @Override // com.unisound.sdk.ar
    public void b() {
        this.f354a.m();
    }

    @Override // com.unisound.sdk.cr
    public void b(int i) {
        this.f354a.i.b(i);
        this.f354a.c(i);
        this.f354a.k.a(i);
    }

    @Override // com.unisound.sdk.cr
    public void b(boolean z, byte[] bArr, int i, int i2) {
        this.f354a.a(z, bArr, i, i2);
    }

    @Override // com.unisound.sdk.ar
    public void c() {
        this.f354a.i.c();
        this.f354a.k.b();
        this.f354a.n();
    }

    @Override // com.unisound.sdk.ar
    public void c(int i) {
    }

    @Override // com.unisound.sdk.ar
    public void d() {
        this.f354a.o();
    }

    @Override // com.unisound.sdk.ar
    public void d(int i) {
        this.f354a.a(i);
    }

    @Override // com.unisound.sdk.ar
    public void e() {
        this.f354a.p();
    }

    @Override // com.unisound.sdk.ar
    public void f() {
        this.f354a.j();
    }

    @Override // com.unisound.sdk.cr
    public void n() {
        this.f354a.i.d();
        this.f354a.h();
    }

    @Override // com.unisound.sdk.cr
    public void o() {
        this.f354a.i();
    }
}
