package com.unisound.sdk;

import android.content.Context;
import cn.yunzhisheng.asrfix.JniAsrFix;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public class k extends Thread {
    protected static long h = 30;
    protected w l;
    protected JniAsrFix m;
    protected String p;
    public Context s;
    protected BlockingQueue<byte[]> i = new LinkedBlockingQueue();
    protected aj j = null;
    protected ak k = null;
    protected boolean n = false;
    protected boolean o = false;
    public List<byte[]> q = new LinkedList();
    public BlockingQueue<byte[]> r = new LinkedBlockingQueue();

    public k(JniAsrFix jniAsrFix, String str, w wVar, Context context) {
        this.l = null;
        this.m = null;
        this.p = "";
        this.s = context;
        this.m = jniAsrFix;
        this.p = str;
        this.l = wVar;
    }

    protected void a(int i) {
        com.unisound.common.y.c("RecognitionThreadInterface:doRecognitionError=", Integer.valueOf(i));
        ak akVar = this.k;
        if (akVar != null) {
            akVar.a(i);
        }
    }

    protected void a(int i, int i2, Object obj) {
        ak akVar = this.k;
        if (akVar != null) {
            akVar.a(i, i2, obj);
        }
    }

    public void a(aj ajVar) {
        this.j = ajVar;
    }

    public void a(ak akVar) {
        this.k = akVar;
    }

    protected void a(String str, boolean z, int i, int i2) {
        com.unisound.common.y.c("RecognitionThreadInterface:doRecognitionResult partial=", str + ", utteranceStartTime = " + i + ", utteranceEndTime = " + i2);
        ak akVar = this.k;
        if (akVar != null) {
            al alVar = new al();
            alVar.a(str);
            alVar.a(z);
            alVar.a(i);
            alVar.b(i2);
            akVar.a(alVar);
        }
    }

    protected void a(String str, boolean z, int i, long j, long j2, int i2, int i3) {
        com.unisound.common.y.c("RecognitionThreadInterface:doRecognitionResult partial=", str);
        ak akVar = this.k;
        if (akVar != null) {
            akVar.a(str, true, i, j, j2, i2, i3);
        }
    }

    public void a(boolean z) {
        if (z) {
            c();
        }
        if (isAlive()) {
            try {
                join(3900L);
                com.unisound.common.y.c(getName(), "waitEnd()");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected boolean a() {
        return !this.i.isEmpty();
    }

    protected void b(int i) {
        com.unisound.common.y.c("RecognitionThreadInterface: onRecognitionEvent event = " + i);
        ak akVar = this.k;
        if (akVar != null) {
            akVar.k(i);
        }
    }

    public synchronized void b(long j) {
        long j2;
        com.unisound.common.y.c("dropTime =>", Long.valueOf(j));
        long jI = this.l.i(j);
        com.unisound.common.y.c("dropCacheByteLength =>", Long.valueOf(jI));
        int i = 0;
        long length = 0;
        while (true) {
            if (i >= this.q.size()) {
                j2 = 0;
                break;
            }
            length += this.q.get(i).length;
            if (length >= jI) {
                j2 = i;
                break;
            }
            i++;
        }
        for (int i2 = 0; i2 < j2; i2++) {
            this.q.remove(0);
        }
    }

    public void b(byte[] bArr) {
        if (com.unisound.common.y.l) {
            com.unisound.common.y.d("FixRecognitionThread::addData size =", Integer.valueOf(bArr.length), " first byte ", Byte.valueOf(bArr[0]));
        }
        this.i.add(bArr);
    }

    public void c() {
        if (l()) {
            return;
        }
        com.unisound.common.y.c("RecognitionThreadInterface::cancel");
        this.k = null;
        this.n = true;
    }

    public synchronized void c(long j) {
        long j2;
        com.unisound.common.y.c("dropPcmFromByteLength =>", Long.valueOf(j));
        int i = 0;
        long length = 0;
        while (true) {
            if (i >= this.q.size()) {
                j2 = 0;
                break;
            }
            length += this.q.get(i).length;
            if (length >= j) {
                j2 = i;
                break;
            }
            i++;
        }
        for (int i2 = 0; i2 < j2; i2++) {
            this.q.remove(0);
        }
    }

    protected synchronized void c(byte[] bArr) {
        int i;
        synchronized (this) {
            this.q.add(bArr);
            int size = this.q.size() - 1;
            int i2 = 0;
            while (true) {
                if (size < 0) {
                    i = 0;
                    break;
                }
                int length = this.q.get(size).length + i2;
                if (length >= this.l.J()) {
                    i = size;
                    break;
                } else {
                    size--;
                    i2 = length;
                }
            }
            for (int i3 = 0; i3 < i; i3++) {
                this.q.remove(0);
            }
        }
    }

    public void d() {
        if (this.n) {
            return;
        }
        com.unisound.common.y.c("RecognitionThreadInterface::stopRecognition");
        this.n = true;
    }

    public void f() {
        this.j = null;
        this.k = null;
    }

    public boolean g() {
        return this.n;
    }

    protected byte[] h() {
        return this.i.poll(h, TimeUnit.MILLISECONDS);
    }

    protected void i() {
        com.unisound.common.y.c("RecognitionThreadInterface:doRecognitionMaxSpeechTimeout");
        ak akVar = this.k;
        if (akVar != null) {
            akVar.m();
        }
    }

    protected void j() {
        com.unisound.common.y.c("RecognitionThreadInterface:onRecognitionVADTimeout");
        ak akVar = this.k;
        if (akVar != null) {
            akVar.l();
        }
    }

    protected void k() {
        com.unisound.common.y.c("RecognitionThreadInterface:doOneshotStart");
        ak akVar = this.k;
        if (akVar == null || !(akVar instanceof m)) {
            return;
        }
        ((m) akVar).c();
    }

    protected boolean l() {
        return (this.m != null && this.m.c()) || this.k == null;
    }

    protected void m() {
        ak akVar = this.k;
        if (akVar != null) {
            akVar.i();
        }
    }

    protected int n() {
        if (this.j != null) {
            return this.j.a();
        }
        return 0;
    }

    public void o() {
        if (!this.l.A() || this.l.B()) {
            return;
        }
        this.q.clear();
    }

    public void p() {
        if (!this.l.A() || this.q.size() <= 0) {
            return;
        }
        if (this.q.size() <= this.l.i(this.l.O())) {
            this.r.add(com.unisound.common.j.a(this.s));
        }
        this.r.addAll(this.q);
        this.r.addAll(this.i);
        this.q.clear();
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
    }

    @Override // java.lang.Thread
    public void start() {
        super.start();
    }
}
