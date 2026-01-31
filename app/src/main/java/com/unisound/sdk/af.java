package com.unisound.sdk;

import android.os.Handler;
import android.os.Looper;

/* loaded from: classes.dex */
public class af extends Handler implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    private int f295a;
    private ag b;
    private boolean c;
    private boolean d;

    public af(ag agVar, Looper looper) {
        super(looper);
        this.f295a = 30000;
        this.c = false;
        this.d = false;
        this.b = agVar;
    }

    public int a() {
        return this.f295a;
    }

    public void a(int i) {
        this.f295a = i;
    }

    public boolean b() {
        return this.c;
    }

    public void c() {
        e();
        postDelayed(this, this.f295a);
        this.c = false;
        this.d = true;
        com.unisound.common.y.a("OnTimer:start");
    }

    public void d() {
        e();
    }

    public void e() {
        if (this.d) {
            removeCallbacks(this);
            this.d = false;
        }
        this.c = false;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.c = true;
        if (this.d) {
            this.b.a();
        }
    }
}
