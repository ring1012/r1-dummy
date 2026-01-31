package com.unisound.common;

import android.media.AudioRecord;
import com.unisound.client.IAudioSource;
import com.unisound.jni.Uni4micHalJNI;
import com.unisound.sdk.BlockingAudioTrack;
import com.unisound.sdk.bw;

/* loaded from: classes.dex */
public class e implements IAudioSource {

    /* renamed from: a, reason: collision with root package name */
    public static final int f258a = 16000;
    private static int i;
    private cn.yunzhisheng.asr.a l;
    private bw m;
    private Uni4micHalJNI o;
    private static int h = 16000;
    protected static int b = 16;
    protected static int c = 16;
    protected static int d = 12;
    protected static int e = 2;
    protected static int f = 4;
    protected static int g = 1;
    private AudioRecord j = null;
    private Object k = new Object();
    private BlockingAudioTrack n = null;
    private long p = -1;

    static {
        i = 6400;
        int minBufferSize = AudioRecord.getMinBufferSize(h, b, e);
        if (i < minBufferSize) {
            i = minBufferSize;
        }
    }

    public e() {
    }

    public e(cn.yunzhisheng.asr.a aVar) {
        this.l = aVar;
    }

    public e(bw bwVar) {
        this.m = bwVar;
    }

    private int a() {
        synchronized (this.k) {
            this.n = new BlockingAudioTrack(this.m.A(), this.m.y(), 2, 1);
            this.n.init();
            this.n.start();
        }
        return 0;
    }

    private int a(byte[] bArr, int i2) {
        if (this.n != null) {
            return this.n.write(bArr, 0, i2);
        }
        return 0;
    }

    private int b(byte[] bArr, int i2) {
        if (this.l.c != null && this.l.c.a() && this.l.c.s()) {
            if (this.o != null) {
                return this.o.readData(this.p, bArr, i2);
            }
            return 0;
        }
        if (this.j != null) {
            return this.j.read(bArr, 0, i2);
        }
        return 0;
    }

    private void b() {
        synchronized (this.k) {
            if (this.n != null) {
                this.n.stop();
                this.n.waitAndRelease();
                this.n = null;
            }
        }
    }

    private int c() throws IllegalStateException {
        if (this.l.c == null || !this.l.c.a()) {
            this.j = new AudioRecord(this.l.e(), this.l.f(), c, e, i);
            if (this.j.getState() == 1) {
                this.j.startRecording();
                return 0;
            }
        } else {
            if (this.l.c.s()) {
                Uni4micHalJNI uni4micHalJNIP = this.l.c.p();
                this.o = uni4micHalJNIP;
                if (uni4micHalJNIP == null) {
                    return -1;
                }
                this.p = this.o.openAudioIn(2);
                int iStartRecorder = this.o.startRecorder(this.p);
                y.c("AudioSourceImpl", "openIn uni4micHalJNI status = " + iStartRecorder);
                return iStartRecorder == 0 ? 0 : -1;
            }
            this.j = new AudioRecord(this.l.e(), this.l.f(), d, e, i);
            if (this.j.getState() == 1) {
                this.j.startRecording();
                return 0;
            }
        }
        return -1;
    }

    private void d() throws IllegalStateException {
        if (this.l.c != null && this.l.c.a() && this.l.c.s()) {
            if (this.o != null) {
                y.c("AudioSourceImpl", "closeIn uni4micHalJNI stop = " + this.o.stopRecorder(this.p) + ", close = " + this.o.closeAudioIn(this.p));
                return;
            }
            return;
        }
        if (this.j != null) {
            y.c("IAudioSource::close audioRecord.stop()");
            if (this.j.getState() == 1) {
                this.j.stop();
            }
            y.c("IAudioSource::close audioRecord.release()");
            this.j.release();
            this.j = null;
            y.c("IAudioSource::close ok");
        }
    }

    @Override // com.unisound.client.IAudioSource
    public void closeAudioIn() throws IllegalStateException {
        d();
    }

    @Override // com.unisound.client.IAudioSource
    public void closeAudioOut() {
        b();
    }

    @Override // com.unisound.client.IAudioSource
    public int openAudioIn() {
        return c();
    }

    @Override // com.unisound.client.IAudioSource
    public int openAudioOut() {
        return a();
    }

    @Override // com.unisound.client.IAudioSource
    public int readData(byte[] bArr, int i2) {
        return b(bArr, i2);
    }

    @Override // com.unisound.client.IAudioSource
    public int writeData(byte[] bArr, int i2) {
        return a(bArr, i2);
    }
}
