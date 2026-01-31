package com.baidu.location;

/* loaded from: classes.dex */
class l extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ g f122a;

    l(g gVar) {
        this.f122a = gVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        if (this.f122a.A == null) {
            this.f122a.A = new com.baidu.location.a.c(this.f122a.e, this.f122a.c, this.f122a);
        }
        this.f122a.A.c();
    }
}
