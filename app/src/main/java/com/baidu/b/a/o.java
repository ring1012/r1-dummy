package com.baidu.b.a;

import android.os.Handler;
import android.os.Looper;

/* loaded from: classes.dex */
class o extends Thread {

    /* renamed from: a, reason: collision with root package name */
    Handler f50a;
    private Object b;
    private boolean c;

    o() {
        this.f50a = null;
        this.b = new Object();
        this.c = false;
    }

    o(String str) {
        super(str);
        this.f50a = null;
        this.b = new Object();
        this.c = false;
    }

    public void a() {
        if (d.f40a) {
            d.a("Looper thread quit()");
        }
        this.f50a.getLooper().quit();
    }

    public void b() {
        synchronized (this.b) {
            try {
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!this.c) {
                this.b.wait();
            }
        }
    }

    public void c() {
        synchronized (this.b) {
            this.c = true;
            this.b.notifyAll();
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        Looper.prepare();
        this.f50a = new Handler();
        if (d.f40a) {
            d.a("new Handler() finish!!");
        }
        Looper.loop();
        if (d.f40a) {
            d.a("LooperThread run() thread id:" + String.valueOf(Thread.currentThread().getId()));
        }
    }
}
