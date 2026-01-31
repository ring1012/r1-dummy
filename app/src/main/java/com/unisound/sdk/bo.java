package com.unisound.sdk;

/* loaded from: classes.dex */
class bo extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bg f320a;

    bo(bg bgVar) {
        this.f320a = bgVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        if (!this.f320a.ay.a(this.f320a.f.f293a) || this.f320a.an == null) {
            return;
        }
        this.f320a.an.sendEmptyMessage(24);
    }
}
