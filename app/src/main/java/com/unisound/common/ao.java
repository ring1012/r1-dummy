package com.unisound.common;

import android.os.Handler;
import android.os.Looper;

/* loaded from: classes.dex */
public class ao extends Handler implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    private static final String f243a = "TimerThreadUtils";
    private int b;
    private ap c;
    private boolean d;
    private boolean e;

    public ao(ap apVar, Looper looper) {
        super(looper);
        this.b = 300000;
        this.d = false;
        this.e = false;
        this.c = apVar;
    }

    public int a() {
        return this.b;
    }

    public void a(int i) {
        this.b = i;
    }

    public boolean b() {
        return this.d;
    }

    public void c() {
        e();
        postDelayed(this, this.b);
        this.d = false;
        this.e = true;
    }

    public void d() {
        e();
    }

    public void e() {
        if (this.e) {
            removeCallbacks(this);
            this.e = false;
        }
        this.d = false;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.d = true;
        if (this.e) {
            this.c.a();
        }
    }
}
