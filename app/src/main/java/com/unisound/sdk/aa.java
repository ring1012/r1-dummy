package com.unisound.sdk;

import android.content.Context;
import cn.yunzhisheng.asr.VAD;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public class aa extends Thread {
    protected static long g = 30;

    /* renamed from: a, reason: collision with root package name */
    protected volatile boolean f292a;
    protected au b;
    protected cn.yunzhisheng.asr.a c;
    protected VAD d;
    protected BlockingQueue<byte[]> e;
    byte[] f;
    private Context h;

    public aa(Context context, cn.yunzhisheng.asr.a aVar, au auVar) {
        this.f292a = false;
        this.b = null;
        this.c = null;
        this.d = null;
        this.e = new LinkedBlockingQueue();
        this.h = context;
        this.c = aVar;
        this.b = auVar;
        com.unisound.common.y.c("InputVadThread::VAD new");
        this.d = new VAD(aVar, auVar);
        this.d.b();
    }

    public aa(Context context, cn.yunzhisheng.asr.a aVar, au auVar, boolean z) {
        this.f292a = false;
        this.b = null;
        this.c = null;
        this.d = null;
        this.e = new LinkedBlockingQueue();
        this.h = context;
        this.c = aVar;
        this.b = auVar;
    }

    public void a() {
        if (b()) {
            return;
        }
        com.unisound.common.y.c("InputVadThread::stopVad");
        this.f292a = true;
    }

    public void a(Collection<byte[]> collection) {
        if (com.unisound.common.y.l) {
            com.unisound.common.y.d("inputVADThread::addAll");
        }
        com.unisound.common.y.c("inputVADThread::addAll");
        this.e.addAll(collection);
    }

    public void a(byte[] bArr) {
        if (com.unisound.common.y.l) {
            com.unisound.common.y.d("inputVADThread::addData size =", Integer.valueOf(bArr.length), " first byte ", Byte.valueOf(bArr[0]), " thread = ", Long.valueOf(Thread.currentThread().getId()));
        }
        this.e.add(bArr);
    }

    public boolean b() {
        return this.f292a;
    }

    public boolean c() {
        return this.b == null;
    }

    public void d() {
        if (c()) {
            return;
        }
        a();
        com.unisound.common.y.c("InputVadThread::cancel");
        this.e.clear();
        this.b = null;
    }

    public boolean e() {
        return this.f292a;
    }

    public void f() {
        d();
        if (isAlive()) {
            try {
                join(4000L);
                com.unisound.common.y.c("InputVadThread::waitEnd()");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void g() {
        if (this.c.T()) {
            return;
        }
        if (!this.e.isEmpty()) {
            this.e.clear();
        }
        if (this.d.g.isEmpty()) {
            return;
        }
        this.d.g.clear();
    }

    public void h() {
        com.unisound.common.y.c("InputVadThread setVadEnable = " + this.c.am());
        this.d.a(this.c.am());
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        com.unisound.common.y.g("InputVadThread start");
        if (com.unisound.common.y.l) {
            com.unisound.common.y.d("InputVadThread -> run ThreadName = ", Thread.currentThread().getName(), ", ThreadId = ", Long.valueOf(Thread.currentThread().getId()));
        }
        while (true) {
            try {
                try {
                    if ((b() && this.e.isEmpty()) || c()) {
                        break;
                    }
                    if (this.c.B() || this.d.g.size() <= 0 || !this.d.h) {
                        byte[] bArrPoll = this.e.poll(g, TimeUnit.MILLISECONDS);
                        this.f = bArrPoll;
                        if (bArrPoll == null) {
                            Thread.sleep(20L);
                        } else {
                            if (this.f.length == 1 && this.f[0] == 100) {
                                d();
                                break;
                            }
                            if (this.f.length == 2 && this.f[0] == -56 && this.f[1] == -56) {
                                if (this.c.z()) {
                                    com.unisound.common.j.b(true, this.c.c());
                                    com.unisound.common.j.b(true, this.c.c());
                                    com.unisound.common.j.b(true, this.c.c());
                                }
                                this.c.S();
                            } else {
                                if (com.unisound.common.y.l) {
                                    com.unisound.common.y.d("inputVADThread::write size =", Integer.valueOf(this.f.length), " first byte ", Byte.valueOf(this.f[0]), " thread = ", Long.valueOf(Thread.currentThread().getId()));
                                }
                                this.d.a(this.f, 0, this.f.length);
                            }
                        }
                    } else {
                        byte[] bArrRemove = this.d.g.remove(0);
                        this.d.a(this.d.h, bArrRemove, 0, bArrRemove.length);
                        if (com.unisound.common.y.l) {
                            com.unisound.common.y.d("inputVADThread::ondata size=", Integer.valueOf(bArrRemove.length), " first byte ", Byte.valueOf(bArrRemove[0]), " thread = ", Long.valueOf(Thread.currentThread().getId()));
                        }
                    }
                } catch (Exception e) {
                    com.unisound.common.y.c("InputVadThread::" + e.getMessage());
                    e.printStackTrace();
                    this.b.k();
                    com.unisound.common.y.c("InputVadThread::VAD destory");
                    this.d.d();
                    this.e.clear();
                }
            } catch (Throwable th) {
                com.unisound.common.y.c("InputVadThread::VAD destory");
                this.d.d();
                this.e.clear();
                throw th;
            }
        }
        if (this.b != null) {
            this.d.e();
        }
        com.unisound.common.y.c("InputVadThread::VAD destory");
        this.d.d();
        this.e.clear();
        com.unisound.common.y.g("InputVadThread stop");
    }
}
