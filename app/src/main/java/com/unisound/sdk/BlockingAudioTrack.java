package com.unisound.sdk;

import android.media.AudioTrack;

/* loaded from: classes.dex */
public class BlockingAudioTrack {

    /* renamed from: a, reason: collision with root package name */
    private static final String f290a = "TTS.BlockingAudioTrack";
    private static final boolean b = false;
    private static final long c = 20;
    private static final long d = 2500;
    private static final long e = 2500;
    private static final int f = 8192;
    private final int g;
    private final int h;
    private final int i;
    private final int j;
    private final int k;
    private int n;
    private final Object q = new Object();
    private boolean l = false;
    private int m = 0;
    private AudioTrack o = null;
    private volatile boolean p = false;

    public BlockingAudioTrack(int i, int i2, int i3, int i4) {
        this.n = 0;
        this.g = i;
        this.h = i2;
        this.i = i3;
        this.j = i4;
        this.k = c(this.i) * this.j;
        this.n = 0;
    }

    static int a(int i) {
        if (i == 1) {
            return 4;
        }
        return i == 2 ? 12 : 0;
    }

    private static int a(AudioTrack audioTrack, byte[] bArr) throws IllegalStateException {
        int iWrite;
        if (audioTrack.getPlayState() != 3) {
            audioTrack.play();
        }
        int i = 0;
        while (i < bArr.length && (iWrite = audioTrack.write(bArr, i, bArr.length)) > 0) {
            i += iWrite;
        }
        return i;
    }

    private static final long a(long j, long j2, long j3) {
        return j < j2 ? j2 : j > j3 ? j3 : j;
    }

    private AudioTrack a() {
        int iA = a(this.j);
        int iMax = Math.max(8192, AudioTrack.getMinBufferSize(this.h, iA, this.i));
        AudioTrack audioTrack = new AudioTrack(this.g, this.h, iA, this.i, iMax, 1);
        if (audioTrack.getState() == 1) {
            this.m = iMax;
            return audioTrack;
        }
        com.unisound.common.y.e(f290a, "Unable to create audio track.");
        audioTrack.release();
        return null;
    }

    private void a(AudioTrack audioTrack) throws InterruptedException {
        if (this.n <= 0) {
            return;
        }
        if (this.l) {
            b();
        } else {
            b(audioTrack);
        }
    }

    private void b() throws InterruptedException {
        try {
            Thread.sleep(((this.n / this.k) * 1000) / this.h);
        } catch (InterruptedException e2) {
        }
    }

    private void b(AudioTrack audioTrack) throws InterruptedException {
        long j;
        int i = this.n / this.k;
        long j2 = 0;
        int i2 = -1;
        while (true) {
            int playbackHeadPosition = audioTrack.getPlaybackHeadPosition();
            if (playbackHeadPosition >= i || audioTrack.getPlayState() != 3 || this.p) {
                return;
            }
            long jA = a(((i - playbackHeadPosition) * 1000) / audioTrack.getSampleRate(), c, 2500L);
            if (playbackHeadPosition == i2) {
                j = j2 + jA;
                if (j > 2500) {
                    com.unisound.common.y.e(f290a, "Waited unsuccessfully for ", 2500L, "ms ", "for AudioTrack to make progress, Aborting");
                    return;
                }
            } else {
                j = 0;
            }
            try {
                Thread.sleep(jA);
                j2 = j;
                i2 = playbackHeadPosition;
            } catch (InterruptedException e2) {
                return;
            }
        }
    }

    private static int c(int i) {
        if (i == 3) {
            return 1;
        }
        return i != 2 ? -1 : 2;
    }

    long b(int i) {
        return ((i / this.k) * 1000) / this.h;
    }

    public void init() {
        AudioTrack audioTrackA = a();
        synchronized (this.q) {
            this.o = audioTrackA;
        }
    }

    public void start() throws IllegalStateException {
        if (this.o == null || this.o.getPlayState() == 3) {
            return;
        }
        this.o.play();
    }

    public void stop() {
        synchronized (this.q) {
            if (this.o != null) {
                this.o.stop();
            }
        }
        this.p = true;
    }

    public void waitAndRelease() throws IllegalStateException, InterruptedException {
        if (this.o == null) {
            return;
        }
        if (this.n < this.m && !this.p) {
            this.l = true;
            this.o.stop();
        }
        if (!this.p) {
            a(this.o);
        }
        synchronized (this.q) {
            this.o.release();
            this.o = null;
        }
    }

    public int write(byte[] bArr) throws IllegalStateException {
        if (this.o == null || this.p) {
            return -1;
        }
        int iA = a(this.o, bArr);
        this.n += iA;
        return iA;
    }

    public int write(byte[] bArr, int i, int i2) {
        if (this.o == null || this.p) {
            return -1;
        }
        return this.o.write(bArr, i, i2);
    }
}
