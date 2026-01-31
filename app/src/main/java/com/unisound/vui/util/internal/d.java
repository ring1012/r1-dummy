package com.unisound.vui.util.internal;

import com.unisound.vui.util.LogMgr;
import java.lang.Thread;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/* loaded from: classes.dex */
public final class d extends Random {
    private static AtomicLong b = new AtomicLong();
    private static volatile long c = 0;
    private static Thread d;
    private static BlockingQueue<byte[]> e;
    private static long f;
    private static volatile long g;
    private static ThreadLocal<d> i;

    /* renamed from: a, reason: collision with root package name */
    boolean f447a;
    private long h;

    static {
        if (c == 0) {
            d = new Thread("initialSeedUniquifierGenerator") { // from class: com.unisound.vui.util.internal.d.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    byte[] bArrGenerateSeed = new SecureRandom().generateSeed(8);
                    long unused = d.g = System.nanoTime();
                    d.e.add(bArrGenerateSeed);
                }
            };
            d.setDaemon(true);
            d.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() { // from class: com.unisound.vui.util.internal.d.2
                @Override // java.lang.Thread.UncaughtExceptionHandler
                public void uncaughtException(Thread t, Throwable e2) {
                    LogMgr.e("ThreadLocalRandom", e2 + "An exception has been raised by %s", t.getName());
                }
            });
            e = new LinkedBlockingQueue();
            f = System.nanoTime();
            d.start();
        } else {
            d = null;
            e = null;
            f = 0L;
        }
        i = new ThreadLocal<d>() { // from class: com.unisound.vui.util.internal.d.3
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // java.lang.ThreadLocal
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public d initialValue() {
                return new d();
            }
        };
    }

    d() {
        super(d());
        this.f447a = true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x0079, code lost:
    
        r0 = ((((((((r0[0] & 255) << 56) | ((r0[1] & 255) << 48)) | ((r0[2] & 255) << 40)) | ((r0[3] & 255) << 32)) | ((r0[4] & 255) << 24)) | ((r0[5] & 255) << 16)) | ((r0[6] & 255) << 8)) | (r0[7] & 255);
        r2 = false;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static long a() {
        /*
            Method dump skipped, instructions count: 230
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unisound.vui.util.internal.d.a():long");
    }

    public static d b() {
        return i.get();
    }

    private static long d() {
        long j;
        long jA;
        long j2;
        do {
            j = b.get();
            jA = j != 0 ? j : a();
            j2 = 181783497276652981L * jA;
        } while (!b.compareAndSet(j, j2));
        if (j == 0) {
            if (g != 0) {
                LogMgr.d("ThreadLocalRandom", String.format("-Dio.netty.initialSeedUniquifier: 0x%016x (took %d ms)", Long.valueOf(jA), Long.valueOf(TimeUnit.NANOSECONDS.toMillis(g - f))));
            } else {
                LogMgr.d("ThreadLocalRandom", String.format("-Dio.netty.initialSeedUniquifier: 0x%016x", Long.valueOf(jA)));
            }
        }
        return System.nanoTime() ^ j2;
    }

    @Override // java.util.Random
    protected int next(int bits) {
        this.h = ((this.h * 25214903917L) + 11) & 281474976710655L;
        return (int) (this.h >>> (48 - bits));
    }

    @Override // java.util.Random
    public void setSeed(long seed) {
        if (this.f447a) {
            throw new UnsupportedOperationException();
        }
        this.h = (25214903917L ^ seed) & 281474976710655L;
    }
}
