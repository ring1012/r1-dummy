package com.unisound.sdk;

import android.text.TextUtils;
import com.unisound.client.ErrorCode;
import com.unisound.client.IAudioSource;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public class bx extends bs {
    public static final int c = 50;
    int e;
    int f;
    private BlockingQueue<byte[]> i;
    private by j;
    private boolean k;
    private volatile int l;
    private volatile int m;
    private BlockingAudioTrack n;
    private boolean o;
    private boolean p;
    private Object q;
    private Boolean r;
    private Boolean s;
    private IAudioSource t;
    private bw u;
    private static final SimpleDateFormat g = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());
    private static int h = 50;
    public static boolean d = false;

    public bx(bw bwVar) {
        super(bwVar.r().booleanValue(), bwVar.m());
        this.i = new LinkedBlockingQueue();
        this.k = false;
        this.l = 0;
        this.m = 0;
        this.n = null;
        this.o = false;
        this.p = false;
        this.q = new Object();
        this.r = true;
        this.s = true;
        this.e = 0;
        this.f = 0;
        this.u = bwVar;
        this.e = ((bwVar.x() * bwVar.y()) * 2) / 1000;
        this.f = bwVar.H();
    }

    private synchronized void a(int i, int i2) {
        switch (i) {
            case 0:
                this.l += i2;
                break;
            case 1:
                this.l -= i2;
                break;
            case 2:
                this.l = i2;
                break;
        }
    }

    public static byte[] a(short[] sArr) {
        if (sArr == null) {
            return null;
        }
        byte[] bArr = new byte[sArr.length * 2];
        for (int i = 0; i < sArr.length; i++) {
            short s = sArr[i];
            bArr[i * 2] = (byte) (s & 255);
            bArr[(i * 2) + 1] = (byte) ((s & 65280) >> 8);
        }
        return bArr;
    }

    public static void b(boolean z) {
        d = z;
    }

    private void c(boolean z) {
        by byVar = this.j;
        if (byVar != null) {
            byVar.a(z);
        }
    }

    private void d(int i) {
        by byVar = this.j;
        if (byVar != null) {
            byVar.b(i);
        }
        this.p = false;
    }

    private synchronized void e(int i) {
        switch (i) {
            case 0:
                this.m++;
                break;
            case 1:
                this.m--;
                break;
            case 2:
                this.m = 0;
                break;
        }
    }

    public static boolean l() {
        return d;
    }

    private boolean m() {
        return this.k;
    }

    private void n() {
        by byVar = this.j;
        if (byVar != null) {
            byVar.a();
        }
    }

    private void o() {
        by byVar = this.j;
        if (byVar != null) {
            byVar.b();
        }
    }

    private void p() {
        by byVar = this.j;
        if (byVar != null) {
            byVar.c();
        }
    }

    private void q() {
        this.p = false;
        by byVar = this.j;
        if (byVar != null) {
            byVar.e();
        }
    }

    private void r() {
        by byVar = this.j;
        if (byVar != null) {
            byVar.f();
        }
    }

    private void s() {
        by byVar = this.j;
        if (byVar != null) {
            byVar.g();
        }
        this.p = false;
    }

    private String t() {
        return g.format(new Date(System.currentTimeMillis())) + ".pcm";
    }

    private boolean u() {
        return this.u.G() ? this.m > 0 : this.l > 0;
    }

    private void v() {
        if (com.unisound.common.y.l) {
            com.unisound.common.y.d("TTSPlayThread->playThreadNotify begin");
        }
        synchronized (this.q) {
            if (this.o) {
                this.o = false;
                com.unisound.common.y.c("lockObject notify..");
                this.q.notify();
                r();
            }
        }
        if (com.unisound.common.y.l) {
            com.unisound.common.y.d("TTSPlayThread->playThreadNotify end");
        }
    }

    private void w() {
        this.o = true;
    }

    public void a(IAudioSource iAudioSource) {
        this.t = iAudioSource;
        if (this.t == null) {
            this.t = new com.unisound.common.e(this.u);
        }
    }

    public void a(by byVar) {
        this.j = byVar;
    }

    public void a(Boolean bool) {
        this.r = bool;
    }

    public void a(byte[] bArr) {
        if (this.u.G()) {
            e(0);
            if (this.m >= this.f) {
                c(true);
            } else {
                c(false);
            }
        } else {
            a(0, bArr.length);
        }
        this.i.add(bArr);
        if (this.s.booleanValue()) {
            n();
            this.s = false;
        }
    }

    @Override // com.unisound.sdk.bs
    public void b() {
        super.b();
        if (this.t == null) {
            BlockingAudioTrack blockingAudioTrack = this.n;
            if (com.unisound.common.y.l) {
                com.unisound.common.y.d("TTSPlayThread->reqStop AudioTrack 1");
            }
            if (blockingAudioTrack != null) {
                v();
                if (com.unisound.common.y.l) {
                    com.unisound.common.y.d("TTSPlayThread->audioTrack.stop() begin");
                }
                blockingAudioTrack.stop();
                if (com.unisound.common.y.l) {
                    com.unisound.common.y.d("TTSPlayThread->audioTrack.stop() end");
                }
            }
            if (com.unisound.common.y.l) {
                com.unisound.common.y.d("TTSPlayThread->reqStop AudioTrack 2");
                return;
            }
            return;
        }
        if (com.unisound.common.y.l) {
            com.unisound.common.y.d("TTSPlayThread->reqStop AudioSource 1");
        }
        v();
        synchronized (this.q) {
            if (this.t != null) {
                if (com.unisound.common.y.l) {
                    com.unisound.common.y.d("TTSPlayThread->AudioSource.closeAudioOut begin");
                }
                this.t.closeAudioOut();
                if (com.unisound.common.y.l) {
                    com.unisound.common.y.d("TTSPlayThread->AudioSource.closeAudioOut end");
                }
                this.t = null;
            }
        }
        if (com.unisound.common.y.l) {
            com.unisound.common.y.d("TTSPlayThread->reqStop AudioSource 2");
        }
    }

    public void b(int i) {
        by byVar = this.j;
        if (byVar != null) {
            byVar.a(i);
        }
    }

    public void c(int i) {
        if (isAlive()) {
            j();
            try {
                super.join(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.unisound.sdk.bs
    public void d() {
        super.d();
        w();
    }

    @Override // com.unisound.sdk.bs
    public void f() {
        super.f();
        v();
    }

    public by g() {
        return this.j;
    }

    public void h() {
        this.k = true;
    }

    public boolean i() {
        return this.j == null;
    }

    public void j() {
        this.j = null;
        this.r = true;
        this.k = true;
    }

    public boolean k() {
        return this.p;
    }

    @Override // com.unisound.common.f, java.lang.Thread, java.lang.Runnable
    public void run() throws IllegalStateException, InterruptedException, IOException {
        BufferedOutputStream bufferedOutputStream;
        int i;
        super.run();
        com.unisound.common.y.b("TTSPlayThread run(): play start");
        if (com.unisound.common.y.l) {
            com.unisound.common.y.d("TTSPlayThread -> ThreadName = ", Thread.currentThread().getName(), ", ThreadId = ", Long.valueOf(Thread.currentThread().getId()));
        }
        if (!c() || TextUtils.isEmpty(this.u.s())) {
            bufferedOutputStream = null;
        } else {
            File file = new File(this.u.s(), t());
            File parentFile = file.getParentFile();
            if (parentFile != null) {
                parentFile.mkdirs();
            }
            try {
                bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file, true));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (this.t == null) {
            this.n = new BlockingAudioTrack(this.u.A(), this.u.y(), 2, 1);
            this.n.init();
            this.n.start();
        } else {
            if (this.t.openAudioOut() != 0) {
                d(ErrorCode.TTS_ERROR_AUDIOSOURCE_OPEN);
                return;
            }
            b(true);
        }
        int iY = ((this.u.y() * 2) / 1000) * 50;
        while (!this.r.booleanValue()) {
            try {
                try {
                    if (com.unisound.common.y.l) {
                        com.unisound.common.y.d("TTSPlayThread :: pause 1 ");
                    }
                    Thread.sleep(50L);
                } catch (Exception e2) {
                    e2.printStackTrace();
                    d(ErrorCode.TTS_ERROR_PLAYING_EXCEPTION);
                    if (this.t != null) {
                        if (com.unisound.common.y.l) {
                            com.unisound.common.y.d("TTSPlayThread ->reqStop--1--");
                        }
                        synchronized (this.q) {
                            if (com.unisound.common.y.l) {
                                com.unisound.common.y.d("TTSPlayThread ->reqStop--2--");
                            }
                            if (this.t != null) {
                                if (com.unisound.common.y.l) {
                                    com.unisound.common.y.d("TTSPlayThread->run:AudioSource.closeAudioOut() begin");
                                }
                                this.t.closeAudioOut();
                                if (com.unisound.common.y.l) {
                                    com.unisound.common.y.d("TTSPlayThread->run:AudioSource.closeAudioOut() end");
                                }
                                this.t = null;
                            }
                        }
                    } else {
                        if (com.unisound.common.y.l) {
                            com.unisound.common.y.d("TTSPlayThread ->reqStop--3--");
                        }
                        if (this.n != null) {
                            this.n.stop();
                            this.n.waitAndRelease();
                            this.n = null;
                        }
                    }
                    this.o = false;
                    if (bufferedOutputStream != null) {
                        try {
                            bufferedOutputStream.flush();
                            bufferedOutputStream.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    }
                }
            } catch (Throwable th) {
                if (this.t != null) {
                    if (com.unisound.common.y.l) {
                        com.unisound.common.y.d("TTSPlayThread ->reqStop--1--");
                    }
                    synchronized (this.q) {
                        if (com.unisound.common.y.l) {
                            com.unisound.common.y.d("TTSPlayThread ->reqStop--2--");
                        }
                        if (this.t != null) {
                            if (com.unisound.common.y.l) {
                                com.unisound.common.y.d("TTSPlayThread->run:AudioSource.closeAudioOut() begin");
                            }
                            this.t.closeAudioOut();
                            if (com.unisound.common.y.l) {
                                com.unisound.common.y.d("TTSPlayThread->run:AudioSource.closeAudioOut() end");
                            }
                            this.t = null;
                        }
                    }
                } else {
                    if (com.unisound.common.y.l) {
                        com.unisound.common.y.d("TTSPlayThread ->reqStop--3--");
                    }
                    if (this.n != null) {
                        this.n.stop();
                        this.n.waitAndRelease();
                        this.n = null;
                    }
                }
                this.o = false;
                if (bufferedOutputStream != null) {
                    try {
                        bufferedOutputStream.flush();
                        bufferedOutputStream.close();
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                }
                throw th;
            }
        }
        if (this.u.G()) {
            while (!a() && !m() && this.m < 1) {
                if (com.unisound.common.y.l) {
                    com.unisound.common.y.d("TTSPlayThread :: pause 2 ");
                }
                Thread.sleep(50L);
            }
        } else {
            while (!a() && !m() && this.l < this.e) {
                if (com.unisound.common.y.l) {
                    com.unisound.common.y.d("TTSPlayThread :: pause 2 ");
                }
                Thread.sleep(50L);
            }
        }
        o();
        if (u()) {
            p();
            while (!a()) {
                byte[] bArrPoll = this.i.poll(h, TimeUnit.MILLISECONDS);
                if (bArrPoll != null) {
                    if (com.unisound.common.y.l) {
                        com.unisound.common.y.d("TTSPlayThread :: buffersize =", Integer.valueOf(bArrPoll.length), " first byte = ", Byte.valueOf(bArrPoll[0]));
                    }
                    int length = bArrPoll.length;
                    if (this.u.G()) {
                        com.unisound.common.y.a("TTSPlay bufferDataTime = " + this.m);
                        if (this.m >= this.f) {
                            c(true);
                        } else {
                            c(false);
                        }
                        e(1);
                        i = 0;
                    } else {
                        if (this.l >= this.e) {
                            c(true);
                        } else {
                            c(false);
                        }
                        a(1, length);
                        i = 0;
                    }
                    while (length > 0 && !a()) {
                        synchronized (this.q) {
                            if (this.o) {
                                com.unisound.common.y.c("TTSPlayThread run(): lockObject wait...");
                                q();
                                this.q.wait();
                            } else {
                                this.p = true;
                            }
                        }
                        int i2 = iY > length ? length : iY;
                        if (this.t != null) {
                            byte[] bArr = new byte[i2];
                            System.arraycopy(bArrPoll, i, bArr, 0, i2);
                            com.unisound.common.y.f("TTSPlayThread run : before writeData ");
                            if (com.unisound.common.y.l) {
                                com.unisound.common.y.d("TTSPlayThread :: writeData ", Integer.valueOf(i2));
                            }
                            this.t.writeData(bArr, i2);
                            com.unisound.common.y.f("TTSPlayThread run : after writeData ");
                        } else {
                            this.n.write(bArrPoll, i, i2);
                        }
                        i += i2;
                        length -= i2;
                    }
                    if (bufferedOutputStream != null && c()) {
                        bufferedOutputStream.write(bArrPoll, 0, bArrPoll.length);
                    }
                } else if (m()) {
                    break;
                } else {
                    c(false);
                }
            }
            s();
        }
        if (this.t != null) {
            if (com.unisound.common.y.l) {
                com.unisound.common.y.d("TTSPlayThread ->reqStop--1--");
            }
            synchronized (this.q) {
                if (com.unisound.common.y.l) {
                    com.unisound.common.y.d("TTSPlayThread ->reqStop--2--");
                }
                if (this.t != null) {
                    if (com.unisound.common.y.l) {
                        com.unisound.common.y.d("TTSPlayThread->run:AudioSource.closeAudioOut() begin");
                    }
                    this.t.closeAudioOut();
                    if (com.unisound.common.y.l) {
                        com.unisound.common.y.d("TTSPlayThread->run:AudioSource.closeAudioOut() end");
                    }
                    this.t = null;
                }
            }
        } else {
            if (com.unisound.common.y.l) {
                com.unisound.common.y.d("TTSPlayThread ->reqStop--3--");
            }
            if (this.n != null) {
                this.n.stop();
                this.n.waitAndRelease();
                this.n = null;
            }
        }
        this.o = false;
        if (bufferedOutputStream != null) {
            try {
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
            } catch (IOException e5) {
                e5.printStackTrace();
            }
        }
        b(false);
        b(0);
        if (this.u.G()) {
            e(2);
        } else {
            a(2, 0);
        }
        com.unisound.common.y.b("TTSPlayThread run(): play end");
    }
}
