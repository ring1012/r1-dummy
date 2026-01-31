package cn.yunzhisheng.asr;

import com.unisound.common.y;

/* loaded from: classes.dex */
public class JniUscClient {
    public static final int A = 7;
    public static final int B = 8;
    public static final int C = 9;
    public static final int D = 10;
    public static final int E = 11;
    public static final int F = 12;
    public static final int G = 13;
    public static final int H = 14;
    public static final int I = 15;
    public static final int J = 16;
    public static final int K = 17;
    public static final int L = 18;
    public static final int M = 19;
    public static final int N = 31;
    public static final int O = 21;
    public static final int P = 22;
    public static final int Q = 25;
    public static final int R = 26;
    public static final int S = 0;
    public static final int T = 1;
    public static final int U = 20;
    public static final int V = 21;
    public static final int W = 22;
    public static final int X = 24;
    public static final int Y = 32;
    public static final int Z = 33;

    /* renamed from: a, reason: collision with root package name */
    public static final int f24a = 0;
    public static final String aA = "no";
    public static final String aB = "variable";
    public static final String aC = "formal";
    public static final int aa = 34;
    public static final int ab = 35;
    public static final int ac = 151;
    public static final int ad = 23;
    public static final int ae = 26;
    public static final int af = 27;
    public static final int ag = 28;
    public static final int ah = 34;
    public static final int ai = 201;
    public static final int aj = 206;
    public static final int ak = 204;
    public static final int al = 207;
    public static final int am = 0;
    public static final int an = 1;
    public static final int ao = 0;
    public static final int ap = 1;
    public static final int aq = 2;
    public static final int ar = 3;
    public static final int as = 4;
    public static final int at = 1015;
    public static final int au = 320;
    public static final int av = 321;
    public static final int aw = 60;
    public static final int ax = 61;
    public static final int ay = 62;
    public static final String az = "null";
    public static final int b = 0;
    public static final int c = 0;
    public static final int d = 1;
    public static final int e = 2;
    public static final int f = 5;
    public static final int g = 1002;
    public static final int h = 1015;
    public static final int i = 201;
    public static final int j = 1020;
    public static final int k = 1019;
    public static int l = 0;
    public static int m = 0;
    public static final String n = "opus";
    public static final String o = "opus-nb";
    public static final String p = "req_audio_url";
    public static final String q = "get_variable";
    public static final String r = "open";
    public static final String s = "close";
    public static final int t = 0;
    public static final int u = 1;
    public static final int v = 2;
    public static final int w = 3;
    public static final int x = 4;
    public static final int y = 5;
    public static final int z = 6;
    private long aD = 0;

    public static String b(int i2) {
        switch (i2) {
        }
        return "NETWORK_TYPE_NONE";
    }

    private native int cancel(long j2);

    private native long createNative(String str, int i2);

    private native void destroyNative(long j2);

    private native int getLastErrno(long j2);

    private native String getOptionValue(long j2, int i2);

    private native String getResult(long j2);

    private native int login(long j2);

    private native int recognize(long j2, byte[] bArr, int i2);

    private native int setOptionInt(long j2, int i2, int i3);

    private native int setOptionString(long j2, int i2, String str);

    private native int start(long j2);

    private native int stop(long j2);

    public int a() {
        if (this.aD == 0) {
            return -1;
        }
        int iStart = start(this.aD);
        a(iStart);
        return iStart;
    }

    public int a(int i2, int i3) {
        if (this.aD == 0) {
            return -1;
        }
        y.d("key = " + i2, "value = " + i3);
        return setOptionInt(this.aD, i2, i3);
    }

    public int a(int i2, String str) {
        if (this.aD == 0) {
            return -1;
        }
        if (str == null) {
            y.a("JniUscClient setOptionString error : s is null!");
            return -1;
        }
        y.d("key = " + i2, "value = " + str);
        return setOptionString(this.aD, i2, str);
    }

    public int a(String str) {
        if (!str.equals(com.unisound.sdk.c.b)) {
            return -1;
        }
        a(24, 0);
        a(101, 4800);
        a(16, str);
        return 0;
    }

    public int a(boolean z2) {
        if (z2) {
            return a(35, q);
        }
        return 0;
    }

    public int a(byte[] bArr, int i2) {
        if (this.aD == 0) {
            return -1;
        }
        int iRecognize = recognize(this.aD, bArr, i2);
        a(iRecognize);
        return iRecognize;
    }

    public long a(String str, int i2) {
        if (this.aD == 0) {
            this.aD = createNative(str, i2);
        }
        return this.aD;
    }

    public void a(int i2) {
        l = i2;
        if (l < 0) {
            m = f();
        } else {
            m = 0;
        }
    }

    public int b() {
        if (this.aD == 0) {
            return -1;
        }
        int iStop = stop(this.aD);
        a(iStop);
        return iStop;
    }

    public String c() {
        return this.aD != 0 ? getResult(this.aD) : "";
    }

    public String c(int i2) {
        return this.aD != 0 ? getOptionValue(this.aD, i2) : "";
    }

    public int d() {
        if (this.aD != 0) {
            return cancel(this.aD);
        }
        return 0;
    }

    public void e() {
        if (this.aD != 0) {
            destroyNative(this.aD);
            this.aD = 0L;
        }
    }

    public int f() {
        if (this.aD != 0) {
            return getLastErrno(this.aD);
        }
        return 0;
    }

    public int g() {
        if (this.aD != 0) {
            return login(this.aD);
        }
        return -1;
    }
}
