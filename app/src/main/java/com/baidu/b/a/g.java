package com.baidu.b.a;

/* loaded from: classes.dex */
class g implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ f f42a;

    g(f fVar) {
        this.f42a = fVar;
    }

    @Override // java.lang.Runnable
    public void run() throws Throwable {
        d.a("postWithHttps start Thread id = " + String.valueOf(Thread.currentThread().getId()));
        this.f42a.a(new j(this.f42a.f41a).a(this.f42a.b));
    }
}
