package com.unisound.sdk;

/* loaded from: classes.dex */
public class av {

    /* renamed from: a, reason: collision with root package name */
    private com.unisound.common.ah f304a = null;

    public void a(int i) {
        com.unisound.common.ah ahVar = this.f304a;
        if (ahVar != null) {
            ahVar.a(i);
        }
    }

    public void a(com.unisound.common.ah ahVar) {
        this.f304a = ahVar;
    }

    public boolean a() {
        return this.f304a != null;
    }

    public void b() {
        com.unisound.common.ah ahVar = this.f304a;
        if (ahVar != null) {
            ahVar.a();
        }
    }

    public void c() {
        com.unisound.common.ah ahVar = this.f304a;
        if (ahVar != null) {
            ahVar.b();
        }
    }
}
