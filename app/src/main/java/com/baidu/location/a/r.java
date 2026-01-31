package com.baidu.location.a;

/* loaded from: classes.dex */
class r extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ n f81a;

    r(n nVar) {
        this.f81a = nVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() throws Throwable {
        if (this.f81a.i()) {
            this.f81a.j();
        }
    }
}
