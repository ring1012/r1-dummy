package com.unisound.sdk;

/* loaded from: classes.dex */
class cc implements ck {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ cb f329a;

    cc(cb cbVar) {
        this.f329a = cbVar;
    }

    @Override // com.unisound.sdk.ck
    public void a(int i) {
        this.f329a.sendMessage(101, Integer.valueOf(i));
    }

    @Override // com.unisound.sdk.ck
    public void a(String str) {
        this.f329a.sendMessage(100, str);
    }

    @Override // com.unisound.sdk.ck
    public void b(int i) {
        this.f329a.sendMessage(102, Integer.valueOf(i));
    }
}
