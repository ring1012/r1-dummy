package com.unisound.sdk;

/* loaded from: classes.dex */
public class a {

    /* renamed from: a, reason: collision with root package name */
    private com.unisound.common.b f291a = null;
    private com.unisound.common.d b = null;

    public void a(int i) {
        com.unisound.common.b bVar = this.f291a;
        if (bVar != null) {
            bVar.b(i);
        }
    }

    public void a(com.unisound.common.b bVar) {
        this.f291a = bVar;
        if (this.f291a != null) {
            this.f291a.a(this.b);
        }
    }

    public void a(com.unisound.common.d dVar) {
        if (this.f291a != null) {
            this.f291a.a(dVar);
        }
        this.b = dVar;
    }

    public void a(String str, boolean z) {
        com.unisound.common.b bVar = this.f291a;
        if (bVar != null) {
            bVar.a(str, z);
        }
    }

    public boolean a() {
        return this.f291a != null;
    }

    public void b() {
        com.unisound.common.b bVar = this.f291a;
        if (bVar != null) {
            bVar.f();
        }
    }

    public void b(int i) {
        com.unisound.common.b bVar = this.f291a;
        if (bVar != null) {
            bVar.a(i);
        }
    }

    public void c() {
        com.unisound.common.b bVar = this.f291a;
        if (bVar != null) {
            bVar.g();
        }
    }

    public void d() {
        com.unisound.common.b bVar = this.f291a;
        if (bVar != null) {
            bVar.h();
        }
    }

    public void e() {
        com.unisound.common.b bVar = this.f291a;
        if (bVar != null) {
            bVar.d();
        }
    }

    public void f() {
        com.unisound.common.b bVar = this.f291a;
        if (bVar != null) {
            bVar.i();
        }
    }

    public void g() {
        com.unisound.common.b bVar = this.f291a;
        if (bVar != null) {
            bVar.e();
        }
    }
}
