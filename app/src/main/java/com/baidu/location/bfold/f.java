package com.baidu.location.bfold;

import android.net.wifi.ScanResult;
import android.text.TextUtils;
import cn.yunzhisheng.common.PinyinConverter;
import com.baidu.location.d.j;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import java.util.List;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class f {

    /* renamed from: a, reason: collision with root package name */
    public List<ScanResult> f95a;
    private long b;
    private long c;
    private boolean d = false;
    private boolean e;

    public f(List<ScanResult> list, long j) {
        this.f95a = null;
        this.b = 0L;
        this.c = 0L;
        this.b = j;
        this.f95a = list;
        this.c = System.currentTimeMillis();
        k();
    }

    private boolean a(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return Pattern.compile("wpa|wep", 2).matcher(str).find();
    }

    private String b(String str) {
        return str != null ? (str.contains("&") || str.contains(";")) ? str.replace("&", "_").replace(";", "_") : str : str;
    }

    private void k() {
        boolean z;
        if (a() < 1) {
            return;
        }
        boolean z2 = true;
        for (int size = this.f95a.size() - 1; size >= 1 && z2; size--) {
            int i = 0;
            z2 = false;
            while (i < size) {
                if (this.f95a.get(i).level < this.f95a.get(i + 1).level) {
                    ScanResult scanResult = this.f95a.get(i + 1);
                    this.f95a.set(i + 1, this.f95a.get(i));
                    this.f95a.set(i, scanResult);
                    z = true;
                } else {
                    z = z2;
                }
                i++;
                z2 = z;
            }
        }
    }

    public int a() {
        if (this.f95a == null) {
            return 0;
        }
        return this.f95a.size();
    }

    public String a(int i) {
        return a(i, false, false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:134:0x039f  */
    /* JADX WARN: Removed duplicated region for block: B:137:0x03a9 A[PHI: r4
  0x03a9: PHI (r4v17 long) = (r4v1 long), (r4v4 long) binds: [B:16:0x005a, B:19:0x0067] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Type inference failed for: r2v106 */
    /* JADX WARN: Type inference failed for: r2v107 */
    /* JADX WARN: Type inference failed for: r2v108 */
    /* JADX WARN: Type inference failed for: r2v128 */
    @android.annotation.SuppressLint({"NewApi"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String a(int r27, boolean r28, boolean r29) {
        /*
            Method dump skipped, instructions count: 956
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.b.f.a(int, boolean, boolean):java.lang.String");
    }

    /* JADX WARN: Removed duplicated region for block: B:43:0x009a A[PHI: r8
  0x009a: PHI (r8v12 long) = (r8v0 long), (r8v3 long) binds: [B:3:0x000c, B:6:0x0019] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean a(long r20) {
        /*
            r19 = this;
            r10 = 0
            r8 = 0
            r6 = 0
            r4 = 0
            r2 = 0
            int r3 = android.os.Build.VERSION.SDK_INT
            r11 = 17
            if (r3 < r11) goto L9a
            long r8 = android.os.SystemClock.elapsedRealtimeNanos()     // Catch: java.lang.Error -> L21 java.lang.Exception -> L25
            r12 = 1000(0x3e8, double:4.94E-321)
            long r8 = r8 / r12
        L15:
            r12 = 0
            int r3 = (r8 > r12 ? 1 : (r8 == r12 ? 0 : -1))
            if (r3 <= 0) goto L9a
            r2 = 1
            r12 = r2
            r14 = r8
        L1e:
            if (r12 != 0) goto L29
        L20:
            return r10
        L21:
            r3 = move-exception
            r8 = 0
            goto L15
        L25:
            r3 = move-exception
            r8 = 0
            goto L15
        L29:
            r0 = r19
            java.util.List<android.net.wifi.ScanResult> r2 = r0.f95a
            if (r2 == 0) goto L20
            r0 = r19
            java.util.List<android.net.wifi.ScanResult> r2 = r0.f95a
            int r2 = r2.size()
            if (r2 == 0) goto L20
            r0 = r19
            java.util.List<android.net.wifi.ScanResult> r2 = r0.f95a
            int r2 = r2.size()
            r3 = 16
            if (r2 <= r3) goto L98
            r2 = 16
            r3 = r2
        L48:
            r2 = 0
            r11 = r2
        L4a:
            if (r11 >= r3) goto L82
            r0 = r19
            java.util.List<android.net.wifi.ScanResult> r2 = r0.f95a
            java.lang.Object r2 = r2.get(r11)
            android.net.wifi.ScanResult r2 = (android.net.wifi.ScanResult) r2
            int r2 = r2.level
            if (r2 != 0) goto L5e
        L5a:
            int r2 = r11 + 1
            r11 = r2
            goto L4a
        L5e:
            if (r12 == 0) goto L5a
            r0 = r19
            java.util.List<android.net.wifi.ScanResult> r2 = r0.f95a     // Catch: java.lang.Exception -> L7a java.lang.Error -> L7e
            java.lang.Object r2 = r2.get(r11)     // Catch: java.lang.Exception -> L7a java.lang.Error -> L7e
            android.net.wifi.ScanResult r2 = (android.net.wifi.ScanResult) r2     // Catch: java.lang.Exception -> L7a java.lang.Error -> L7e
            long r8 = r2.timestamp     // Catch: java.lang.Exception -> L7a java.lang.Error -> L7e
            long r8 = r14 - r8
            r16 = 1000000(0xf4240, double:4.940656E-318)
            long r8 = r8 / r16
        L73:
            long r4 = r4 + r8
            int r2 = (r8 > r6 ? 1 : (r8 == r6 ? 0 : -1))
            if (r2 <= 0) goto L5a
            r6 = r8
            goto L5a
        L7a:
            r2 = move-exception
            r8 = 0
            goto L73
        L7e:
            r2 = move-exception
            r8 = 0
            goto L73
        L82:
            long r2 = (long) r3
            long r2 = r4 / r2
            r4 = 1000(0x3e8, double:4.94E-321)
            long r4 = r4 * r6
            int r4 = (r4 > r20 ? 1 : (r4 == r20 ? 0 : -1))
            if (r4 > 0) goto L93
            r4 = 1000(0x3e8, double:4.94E-321)
            long r2 = r2 * r4
            int r2 = (r2 > r20 ? 1 : (r2 == r20 ? 0 : -1))
            if (r2 <= 0) goto L96
        L93:
            r2 = 1
        L94:
            r10 = r2
            goto L20
        L96:
            r2 = r10
            goto L94
        L98:
            r3 = r2
            goto L48
        L9a:
            r12 = r2
            r14 = r8
            goto L1e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.b.f.a(long):boolean");
    }

    public boolean a(f fVar) {
        if (this.f95a == null || fVar == null || fVar.f95a == null) {
            return false;
        }
        int size = this.f95a.size() < fVar.f95a.size() ? this.f95a.size() : fVar.f95a.size();
        for (int i = 0; i < size; i++) {
            if (!this.f95a.get(i).BSSID.equals(fVar.f95a.get(i).BSSID)) {
                return false;
            }
        }
        return true;
    }

    public int b(int i) {
        if (i <= 2400 || i >= 2500) {
            return (i <= 4900 || i >= 5900) ? 0 : 5;
        }
        return 2;
    }

    public String b() {
        try {
            return a(j.N, true, true);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean b(f fVar) {
        if (this.f95a == null || fVar == null || fVar.f95a == null) {
            return false;
        }
        int size = this.f95a.size() < fVar.f95a.size() ? this.f95a.size() : fVar.f95a.size();
        for (int i = 0; i < size; i++) {
            String str = this.f95a.get(i).BSSID;
            int i2 = this.f95a.get(i).level;
            String str2 = fVar.f95a.get(i).BSSID;
            int i3 = fVar.f95a.get(i).level;
            if (!str.equals(str2) || i2 != i3) {
                return false;
            }
        }
        return true;
    }

    public String c() {
        try {
            return a(j.N, true, false);
        } catch (Exception e) {
            return null;
        }
    }

    public String c(int i) {
        int i2;
        int i3 = 0;
        if (i == 0 || a() < 1) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer(256);
        int size = this.f95a.size();
        int i4 = size > j.N ? j.N : size;
        int i5 = 1;
        int i6 = 0;
        while (i6 < i4) {
            if ((i5 & i) == 0 || this.f95a.get(i6).BSSID == null) {
                i2 = i3;
            } else {
                if (i3 == 0) {
                    stringBuffer.append("&ssid=");
                } else {
                    stringBuffer.append(PinyinConverter.PINYIN_EXCLUDE);
                }
                stringBuffer.append(this.f95a.get(i6).BSSID.replace(":", ""));
                stringBuffer.append(";");
                stringBuffer.append(b(this.f95a.get(i6).SSID));
                i2 = i3 + 1;
            }
            i5 <<= 1;
            i6++;
            i3 = i2;
        }
        return stringBuffer.toString();
    }

    public boolean c(f fVar) {
        return g.a(fVar, this);
    }

    public String d() {
        try {
            return a(15);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean e() {
        return a(j.ae);
    }

    public int f() {
        for (int i = 0; i < a(); i++) {
            int i2 = -this.f95a.get(i).level;
            if (i2 > 0) {
                return i2;
            }
        }
        return 0;
    }

    public boolean g() {
        return this.d;
    }

    public boolean h() {
        return System.currentTimeMillis() - this.c > 0 && System.currentTimeMillis() - this.c < DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS;
    }

    public boolean i() {
        return System.currentTimeMillis() - this.c > 0 && System.currentTimeMillis() - this.c < DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS;
    }

    public boolean j() {
        return System.currentTimeMillis() - this.c > 0 && System.currentTimeMillis() - this.b < DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS;
    }
}
