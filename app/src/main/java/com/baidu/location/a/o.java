package com.baidu.location.a;

/* loaded from: classes.dex */
class o implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ n f78a;

    o(n nVar) {
        this.f78a = nVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (com.baidu.location.b.g.i() || this.f78a.a(com.baidu.location.f.b())) {
            this.f78a.d();
        }
    }
}
