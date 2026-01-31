package com.tencent.bugly.proguard;

import android.content.Context;
import android.os.Process;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class v implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    private int f206a;
    private int b;
    private final Context c;
    private final int d;
    private final byte[] e;
    private final com.tencent.bugly.crashreport.common.info.a f;
    private final com.tencent.bugly.crashreport.common.strategy.a g;
    private final s h;
    private final u i;
    private final int j;
    private final t k;
    private final t l;
    private String m;
    private final String n;
    private final Map<String, String> o;
    private int p;
    private long q;
    private long r;
    private boolean s;
    private boolean t;

    public v(Context context, int i, int i2, byte[] bArr, String str, String str2, t tVar, boolean z, boolean z2) {
        this(context, i, i2, bArr, str, str2, tVar, z, 2, 30000, z2, null);
    }

    public v(Context context, int i, int i2, byte[] bArr, String str, String str2, t tVar, boolean z, int i3, int i4, boolean z2, Map<String, String> map) {
        this.f206a = 2;
        this.b = 30000;
        this.m = null;
        this.p = 0;
        this.q = 0L;
        this.r = 0L;
        this.s = true;
        this.t = false;
        this.c = context;
        this.f = com.tencent.bugly.crashreport.common.info.a.a(context);
        this.e = bArr;
        this.g = com.tencent.bugly.crashreport.common.strategy.a.a();
        this.h = s.a(context);
        this.i = u.a();
        this.j = i;
        this.m = str;
        this.n = str2;
        this.k = tVar;
        u uVar = this.i;
        this.l = null;
        this.s = z;
        this.d = i2;
        if (i3 > 0) {
            this.f206a = i3;
        }
        if (i4 > 0) {
            this.b = i4;
        }
        this.t = z2;
        this.o = map;
    }

    private void a(an anVar, boolean z, int i, String str, int i2) {
        String strValueOf;
        switch (this.d) {
            case 630:
            case 830:
                strValueOf = "crash";
                break;
            case 640:
            case 840:
                strValueOf = "userinfo";
                break;
            default:
                strValueOf = String.valueOf(this.d);
                break;
        }
        if (z) {
            x.a("[Upload] Success: %s", strValueOf);
        } else {
            x.e("[Upload] Failed to upload(%d) %s: %s", Integer.valueOf(i), strValueOf, str);
            if (this.s) {
                this.i.a(i2, (an) null);
            }
        }
        if (this.q + this.r > 0) {
            this.i.a(this.i.a(this.t) + this.q + this.r, this.t);
        }
        if (this.k != null) {
            t tVar = this.k;
            int i3 = this.d;
            long j = this.q;
            long j2 = this.r;
            tVar.a(z);
        }
        if (this.l != null) {
            t tVar2 = this.l;
            int i4 = this.d;
            long j3 = this.q;
            long j4 = this.r;
            tVar2.a(z);
        }
    }

    private static boolean a(an anVar, com.tencent.bugly.crashreport.common.info.a aVar, com.tencent.bugly.crashreport.common.strategy.a aVar2) throws NumberFormatException {
        if (anVar == null) {
            x.d("resp == null!", new Object[0]);
            return false;
        }
        if (anVar.f182a != 0) {
            x.e("resp result error %d", Byte.valueOf(anVar.f182a));
            return false;
        }
        try {
            if (!z.a(anVar.d) && !com.tencent.bugly.crashreport.common.info.a.b().i().equals(anVar.d)) {
                p.a().a(com.tencent.bugly.crashreport.common.strategy.a.f144a, "key_ip", anVar.d.getBytes("UTF-8"), (o) null, true);
                aVar.d(anVar.d);
            }
            if (!z.a(anVar.f) && !com.tencent.bugly.crashreport.common.info.a.b().j().equals(anVar.f)) {
                p.a().a(com.tencent.bugly.crashreport.common.strategy.a.f144a, "key_imei", anVar.f.getBytes("UTF-8"), (o) null, true);
                aVar.e(anVar.f);
            }
        } catch (Throwable th) {
            x.a(th);
        }
        aVar.i = anVar.e;
        if (anVar.b == 510) {
            if (anVar.c == null) {
                x.e("[Upload] Strategy data is null. Response cmd: %d", Integer.valueOf(anVar.b));
                return false;
            }
            ap apVar = (ap) a.a(anVar.c, ap.class);
            if (apVar == null) {
                x.e("[Upload] Failed to decode strategy from server. Response cmd: %d", Integer.valueOf(anVar.b));
                return false;
            }
            aVar2.a(apVar);
        }
        return true;
    }

    @Override // java.lang.Runnable
    public final void run() {
        byte[] bArrA;
        Map<String, String> map;
        byte[] bArrB;
        boolean z;
        try {
            this.p = 0;
            this.q = 0L;
            this.r = 0L;
            byte[] bArrA2 = this.e;
            if (com.tencent.bugly.crashreport.common.info.b.f(this.c) == null) {
                a(null, false, 0, "network is not available", 0);
                return;
            }
            if (bArrA2 == null || bArrA2.length == 0) {
                a(null, false, 0, "request package is empty!", 0);
                return;
            }
            long jA = this.i.a(this.t);
            if (bArrA2.length + jA >= CacheDataSource.DEFAULT_MAX_CACHE_FILE_SIZE) {
                x.e("[Upload] Upload too much data, try next time: %d/%d", Long.valueOf(jA), Long.valueOf(CacheDataSource.DEFAULT_MAX_CACHE_FILE_SIZE));
                a(null, false, 0, "over net consume: 2048K", 0);
                return;
            }
            x.c("[Upload] Run upload task with cmd: %d", Integer.valueOf(this.d));
            if (this.c == null || this.f == null || this.g == null || this.h == null) {
                a(null, false, 0, "illegal access error", 0);
                return;
            }
            StrategyBean strategyBeanC = this.g.c();
            if (strategyBeanC == null) {
                a(null, false, 0, "illegal local strategy", 0);
                return;
            }
            int i = 0;
            HashMap map2 = new HashMap();
            map2.put("prodId", this.f.f());
            map2.put("bundleId", this.f.c);
            map2.put("appVer", this.f.j);
            if (this.o != null) {
                map2.putAll(this.o);
            }
            if (this.s) {
                map2.put("cmd", Integer.toString(this.d));
                map2.put("platformId", Byte.toString((byte) 1));
                this.f.getClass();
                map2.put("sdkVer", "2.6.6");
                map2.put("strategylastUpdateTime", Long.toString(strategyBeanC.p));
                if (!this.i.a(map2)) {
                    a(null, false, 0, "failed to add security info to HTTP headers", 0);
                    return;
                }
                byte[] bArrA3 = z.a(bArrA2, 2);
                if (bArrA3 == null) {
                    a(null, false, 0, "failed to zip request body", 0);
                    return;
                }
                bArrA2 = this.i.a(bArrA3);
                if (bArrA2 == null) {
                    a(null, false, 0, "failed to encrypt request body", 0);
                    return;
                }
            }
            byte[] bArr = bArrA2;
            this.i.a(this.j, System.currentTimeMillis());
            if (this.k != null) {
                t tVar = this.k;
                int i2 = this.d;
            }
            if (this.l != null) {
                t tVar2 = this.l;
                int i3 = this.d;
            }
            int i4 = -1;
            int i5 = 0;
            String str = this.m;
            while (true) {
                int i6 = i5 + 1;
                if (i5 < this.f206a) {
                    if (i6 > 1) {
                        x.d("[Upload] Failed to upload last time, wait and try(%d) again.", Integer.valueOf(i6));
                        z.b(this.b);
                        if (i6 == this.f206a) {
                            x.d("[Upload] Use the back-up url at the last time: %s", this.n);
                            str = this.n;
                        }
                    }
                    x.c("[Upload] Send %d bytes", Integer.valueOf(bArr.length));
                    String strA = this.s ? a(str) : str;
                    x.c("[Upload] Upload to %s with cmd %d (pid=%d | tid=%d).", strA, Integer.valueOf(this.d), Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
                    bArrA = this.h.a(strA, bArr, this, map2);
                    if (bArrA == null) {
                        x.e("[Upload] Failed to upload(%d): %s", 1, "Failed to upload for no response!");
                        i = 1;
                        i5 = i6;
                        str = strA;
                    } else {
                        map = this.h.f201a;
                        if (!this.s) {
                            break;
                        }
                        if (map == null || map.size() == 0) {
                            x.d("[Upload] Headers is empty.", new Object[0]);
                            z = false;
                        } else if (!map.containsKey("status")) {
                            x.d("[Upload] Headers does not contain %s", "status");
                            z = false;
                        } else if (map.containsKey("Bugly-Version")) {
                            String str2 = map.get("Bugly-Version");
                            if (str2.contains("bugly")) {
                                x.c("[Upload] Bugly version from headers is: %s", str2);
                                z = true;
                            } else {
                                x.d("[Upload] Bugly version is not valid: %s", str2);
                                z = false;
                            }
                        } else {
                            x.d("[Upload] Headers does not contain %s", "Bugly-Version");
                            z = false;
                        }
                        if (!z) {
                            x.c("[Upload] Headers from server is not valid, just try again (pid=%d | tid=%d).", Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
                            x.e("[Upload] Failed to upload(%d): %s", 1, "[Upload] Failed to upload for no status header.");
                            if (map != null) {
                                for (Map.Entry<String, String> entry : map.entrySet()) {
                                    x.c(String.format("[key]: %s, [value]: %s", entry.getKey(), entry.getValue()), new Object[0]);
                                }
                            }
                            x.c("[Upload] Failed to upload for no status header.", new Object[0]);
                            i = 1;
                            i5 = i6;
                            str = strA;
                        } else {
                            try {
                                i4 = Integer.parseInt(map.get("status"));
                                x.c("[Upload] Status from server is %d (pid=%d | tid=%d).", Integer.valueOf(i4), Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
                                if (i4 != 0) {
                                    if (i4 == 2) {
                                        if (this.q + this.r > 0) {
                                            this.i.a(this.i.a(this.t) + this.q + this.r, this.t);
                                        }
                                        this.i.a(i4, (an) null);
                                        x.a("[Upload] Session ID is invalid, will try again immediately (pid=%d | tid=%d).", Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
                                        this.i.a(this.j, this.d, this.e, this.m, this.n, this.k, this.f206a, this.b, true, this.o);
                                        return;
                                    }
                                    a(null, false, 1, "status of server is " + i4, i4);
                                    return;
                                }
                            } catch (Throwable th) {
                                x.e("[Upload] Failed to upload(%d): %s", 1, "[Upload] Failed to upload for format of status header is invalid: " + Integer.toString(i4));
                                i = 1;
                                i5 = i6;
                                str = strA;
                            }
                        }
                    }
                } else {
                    a(null, false, i, "failed after many attempts", 0);
                    return;
                }
            }
            x.c("[Upload] Received %d bytes", Integer.valueOf(bArrA.length));
            if (!this.s) {
                bArrB = bArrA;
            } else {
                if (bArrA.length == 0) {
                    for (Map.Entry<String, String> entry2 : map.entrySet()) {
                        x.c("[Upload] HTTP headers from server: key = %s, value = %s", entry2.getKey(), entry2.getValue());
                    }
                    a(null, false, 1, "response data from server is empty", 0);
                    return;
                }
                byte[] bArrB2 = this.i.b(bArrA);
                if (bArrB2 == null) {
                    a(null, false, 1, "failed to decrypt response from server", 0);
                    return;
                }
                bArrB = z.b(bArrB2, 2);
                if (bArrB == null) {
                    a(null, false, 1, "failed unzip(Gzip) response from server", 0);
                    return;
                }
            }
            an anVarA = a.a(bArrB, this.s);
            if (anVarA == null) {
                a(null, false, 1, "failed to decode response package", 0);
                return;
            }
            if (this.s) {
                this.i.a(i4, anVarA);
            }
            Object[] objArr = new Object[2];
            objArr[0] = Integer.valueOf(anVarA.b);
            objArr[1] = Integer.valueOf(anVarA.c == null ? 0 : anVarA.c.length);
            x.c("[Upload] Response cmd is: %d, length of sBuffer is: %d", objArr);
            if (!a(anVarA, this.f, this.g)) {
                a(anVarA, false, 2, "failed to process response package", 0);
            } else {
                a(anVarA, true, 2, "successfully uploaded", 0);
            }
        } catch (Throwable th2) {
            if (!x.a(th2)) {
                th2.printStackTrace();
            }
        }
    }

    public final void a(long j) {
        this.p++;
        this.q += j;
    }

    public final void b(long j) {
        this.r += j;
    }

    private static String a(String str) {
        if (!z.a(str)) {
            try {
                return String.format("%s?aid=%s", str, UUID.randomUUID().toString());
            } catch (Throwable th) {
                x.a(th);
                return str;
            }
        }
        return str;
    }
}
