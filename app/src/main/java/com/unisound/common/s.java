package com.unisound.common;

import android.media.AudioManager;
import android.os.Environment;
import android.util.Log;
import com.unisound.jni.Uni4micHalJNI;
import java.io.File;
import java.lang.reflect.Array;

/* loaded from: classes.dex */
public class s {

    /* renamed from: a, reason: collision with root package name */
    public static final int f270a = 100000;
    public static final int b = 100001;
    public static final int c = 100002;
    public static final int d = 100003;
    public static final int e = 100004;
    public static final int f = 100005;
    public static final int g = 100006;
    public static final int h = 100007;
    public static final int i = 100008;
    public static final int j = 100009;
    public static final int k = 100010;
    private static final String r = "USCFourMic";
    private static final boolean s = false;
    private AudioManager t;
    private Uni4micHalJNI u;
    private boolean v = false;
    private boolean w = true;
    public boolean l = false;
    public boolean m = false;
    int n = 0;
    int o = 0;
    public String p = Environment.getExternalStorageDirectory().getPath() + "/YunZhiSheng/4mic/";
    public String q = "";
    private boolean x = false;
    private int y = 0;
    private boolean z = false;

    public s(AudioManager audioManager) {
        this.t = audioManager;
    }

    private void b(String str) {
        int iLastIndexOf;
        if (str != null && (iLastIndexOf = str.lastIndexOf(47)) >= 0) {
            new File(str.substring(0, iLastIndexOf)).mkdirs();
        }
    }

    private void c(String str) {
        if (this.l && this.m) {
            Log.d(r, str);
        }
    }

    private boolean u() {
        return 1 == d();
    }

    private boolean v() {
        return e().contains("UNI_4MIC_HAL_ANDROID");
    }

    public int a(int i2, int i3) {
        return ((i2 / 2) * 1000) / i3;
    }

    public void a(int i2) {
        if (a() && u()) {
            c("set4MicWakeup -> " + i2);
            this.t.adjustStreamVolume(100000, 0, i2);
        }
        c("print debug log set4MicWakeup -> " + i2);
    }

    public void a(String str) {
        this.q = str;
    }

    public void a(boolean z) {
        this.l = z;
    }

    public boolean a() {
        return this.l;
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x0042 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean a(byte[] r5, java.lang.String r6) throws java.lang.Throwable {
        /*
            r4 = this;
            boolean r0 = r4.a()
            if (r0 == 0) goto L37
            boolean r0 = r4.f()
            if (r0 == 0) goto L37
            r4.b(r6)
            r2 = 0
            java.io.RandomAccessFile r1 = new java.io.RandomAccessFile     // Catch: java.lang.Exception -> L2d java.lang.Throwable -> L3e
            java.lang.String r0 = "rw"
            r1.<init>(r6, r0)     // Catch: java.lang.Exception -> L2d java.lang.Throwable -> L3e
            long r2 = r1.length()     // Catch: java.lang.Throwable -> L4b java.lang.Exception -> L4d
            r1.seek(r2)     // Catch: java.lang.Throwable -> L4b java.lang.Exception -> L4d
            r1.write(r5)     // Catch: java.lang.Throwable -> L4b java.lang.Exception -> L4d
            r0 = 1
            if (r1 == 0) goto L27
            r1.close()     // Catch: java.io.IOException -> L28
        L27:
            return r0
        L28:
            r1 = move-exception
            r1.printStackTrace()
            goto L27
        L2d:
            r0 = move-exception
            r1 = r2
        L2f:
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L4b
            if (r1 == 0) goto L37
            r1.close()     // Catch: java.io.IOException -> L39
        L37:
            r0 = 0
            goto L27
        L39:
            r0 = move-exception
            r0.printStackTrace()
            goto L37
        L3e:
            r0 = move-exception
            r1 = r2
        L40:
            if (r1 == 0) goto L45
            r1.close()     // Catch: java.io.IOException -> L46
        L45:
            throw r0
        L46:
            r1 = move-exception
            r1.printStackTrace()
            goto L45
        L4b:
            r0 = move-exception
            goto L40
        L4d:
            r0 = move-exception
            goto L2f
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unisound.common.s.a(byte[], java.lang.String):boolean");
    }

    public byte[][] a(byte[] bArr) {
        byte[][] bArr2 = (byte[][]) Array.newInstance((Class<?>) Byte.TYPE, 2, bArr.length / 2);
        for (int i2 = 0; (i2 * 4) + 2 < bArr.length; i2++) {
            System.arraycopy(bArr, i2 * 4, bArr2[0], i2 * 2, 2);
            System.arraycopy(bArr, (i2 * 4) + 2, bArr2[1], i2 * 2, 2);
        }
        return bArr2;
    }

    public int b() {
        if (a() && u()) {
            return this.t.getVibrateSetting(c);
        }
        return -1;
    }

    public void b(int i2) {
        if (a() && v()) {
            c("_set4MicWakeup -> " + i2);
            this.u.set4MicWakeUpStatus(i2);
        }
        c("print debug log _set4MicWakeup -> " + i2);
    }

    public void b(boolean z) {
        if (a() && u()) {
            this.t.adjustStreamVolume(e, 0, z ? 1 : 0);
            c("setDebugMode set To board  -> " + z);
        }
        c("setDebugMode  -> " + z);
        this.m = z;
    }

    public void b(byte[] bArr) throws Throwable {
        if (a() && f()) {
            a(bArr, this.p + this.q + "_real.pcm");
        }
    }

    public int c() {
        if (a() && v()) {
            return this.u.get4MicDoaResult();
        }
        return -1;
    }

    public void c(int i2) {
        if (a() && u()) {
            c("set4MicDoaTimeLen -> " + i2);
            this.t.adjustStreamVolume(b, 0, i2);
        }
    }

    public void c(boolean z) {
        if (a() && v()) {
            this.u.set4MicDebugMode(z ? 1 : 0);
            c("setDebugMode set To board  -> " + z);
        }
        c("setDebugMode  -> " + z);
        this.m = z;
    }

    public void c(byte[] bArr) throws Throwable {
        if (a() && f()) {
            a(bArr, this.p + this.q + "_asr.pcm");
        }
    }

    public int d() {
        if (a()) {
            return this.t.getVibrateSetting(d);
        }
        return -1;
    }

    public void d(int i2) {
        if (a() && v()) {
            c("set4MicDoaTimeLen -> " + i2);
            this.u.set4MicUtteranceTimeLen(i2);
        }
        c("print debug log _set4MicDoaTimeLen -> " + i2);
    }

    public void d(boolean z) {
        if (u()) {
            this.t.adjustStreamVolume(g, 0, z ? 0 : 1);
            c("close4MicAlgorithm set To board  -> " + z);
        }
    }

    public void d(byte[] bArr) throws Throwable {
        if (a() && f()) {
            a(bArr, this.p + this.q + "_vad.pcm");
        }
    }

    public String e() {
        return a() ? this.u.get4MicBoardVersion() : "";
    }

    public void e(int i2) {
        this.n += i2;
    }

    public void e(boolean z) {
        if (v()) {
            int i2 = z ? 1 : 0;
            this.u.close4MicAlgorithm(i2);
            y.c("FourMicUtil", "close4MicAlgorithm flag = " + i2);
            c("_close4MicAlgorithm set To board  -> " + z);
        }
    }

    public void f(int i2) {
        if (a() && u()) {
            if (i2 < 0) {
                i2 = 0;
            }
            this.t.adjustStreamVolume(f, 0, i2);
            c("setDelayTime -> " + i2);
        }
    }

    public void f(boolean z) {
        if (!a() || !u()) {
            c("setOneShotReadyFor4Mic error not 4mic");
            return;
        }
        this.x = z;
        int i2 = z ? 1 : 0;
        m(i2);
        c("setOneShotReadyFor4Mic -> " + i2);
    }

    public boolean f() {
        return this.m;
    }

    public void g() {
        this.n = 0;
    }

    public void g(int i2) {
        if (a() && v()) {
            if (i2 < 0) {
                i2 = 0;
            }
            this.u.set4MicDelayTime(i2);
            c("setDelayTime -> " + i2);
        }
    }

    public void g(boolean z) {
        if (!a() || !v()) {
            c("setOneShotReadyFor4Mic error not 4mic");
            return;
        }
        this.x = z;
        int i2 = z ? 1 : 0;
        n(i2);
        c("setOneShotReadyFor4Mic -> " + i2);
    }

    public int h() {
        return this.n;
    }

    public void h(int i2) {
        this.o = i2;
    }

    public void h(boolean z) {
        this.z = z;
    }

    public int i() {
        return this.o;
    }

    public void i(int i2) {
        if (!a() || !u()) {
            c("setOneShotTimeStart error not 4mic");
        } else {
            c("setOneShotTimeStart -> " + i2);
            this.t.adjustStreamVolume(h, 0, i2);
        }
    }

    public void i(boolean z) {
        this.v = z;
    }

    public int j() {
        if (a() && u()) {
            return this.t.getVibrateSetting(k);
        }
        return -1;
    }

    public void j(int i2) {
        if (!a() || !v()) {
            c("_setOneShotTimeStart error not 4mic");
        } else {
            c("setOneShotTimeStart -> " + i2);
            this.u.set4MicOneShotStartLen(i2);
        }
    }

    public void j(boolean z) {
        this.w = z;
    }

    public int k() {
        if (a() && v()) {
            this.u.get4MicOneShotReady();
        }
        return -1;
    }

    public void k(int i2) {
        if (!a() || !u()) {
            c("setStartWakeupTimeLen error not 4mic");
            return;
        }
        if (i2 < 0) {
            c("setStartWakeupTimeLen -> timeLen min");
            i2 = 0;
        } else if (i2 > Integer.MAX_VALUE) {
            c("setStartWakeupTimeLen -> timeLen max");
            i2 = Integer.MAX_VALUE;
        }
        c("setStartWakeupTimeLen -> " + i2);
        this.t.adjustStreamVolume(i, 0, i2);
    }

    public void l(int i2) {
        if (!a() || !v()) {
            c("setStartWakeupTimeLen error not 4mic");
            return;
        }
        if (i2 < 0) {
            i2 = 0;
            c("setStartWakeupTimeLen -> timeLen min");
        } else if (i2 > Integer.MAX_VALUE) {
            c("setStartWakeupTimeLen -> timeLen max");
            i2 = Integer.MAX_VALUE;
        }
        c("setStartWakeupTimeLen -> " + i2);
        this.u.set4MicWakeupStartLen(i2);
    }

    public boolean l() {
        return this.x;
    }

    public int m() {
        return this.y;
    }

    public void m(int i2) {
        if (!a() || !u()) {
            c("setOneshotReady error not 4mic");
        } else {
            c("setOneshotReady -> " + i2);
            this.t.adjustStreamVolume(j, 0, i2);
        }
    }

    public void n() {
        this.y = 0;
    }

    public void n(int i2) {
        if (!a() || !v()) {
            c("setOneshotReady error not 4mic");
        } else {
            c("setOneshotReady -> " + i2);
            this.u.set4MicOneShotReady(i2);
        }
    }

    public void o(int i2) {
        this.y = i2;
    }

    public boolean o() {
        return this.z;
    }

    public Uni4micHalJNI p() {
        return this.u;
    }

    public void p(int i2) {
        this.y += i2;
    }

    public int q() {
        this.u = Uni4micHalJNI.getInstance();
        int iInit = this.u.init(1);
        y.c("FourMicUtil", "initFourMic status = " + iInit);
        return iInit;
    }

    public int r() {
        if (this.u != null) {
            return this.u.release();
        }
        return -1;
    }

    public boolean s() {
        return this.v;
    }

    public boolean t() {
        return this.w;
    }
}
