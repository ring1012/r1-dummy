package com.tencent.bugly.crashreport.crash.jni;

import android.content.Context;
import com.tencent.bugly.crashreport.common.info.AppInfo;
import com.tencent.bugly.crashreport.crash.CrashDetailBean;
import com.tencent.bugly.crashreport.crash.c;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.y;
import com.tencent.bugly.proguard.z;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class a implements NativeExceptionHandler {

    /* renamed from: a, reason: collision with root package name */
    private final Context f171a;
    private final com.tencent.bugly.crashreport.crash.b b;
    private final com.tencent.bugly.crashreport.common.info.a c;
    private final com.tencent.bugly.crashreport.common.strategy.a d;

    public a(Context context, com.tencent.bugly.crashreport.common.info.a aVar, com.tencent.bugly.crashreport.crash.b bVar, com.tencent.bugly.crashreport.common.strategy.a aVar2) {
        this.f171a = context;
        this.b = bVar;
        this.c = aVar;
        this.d = aVar2;
    }

    @Override // com.tencent.bugly.crashreport.crash.jni.NativeExceptionHandler
    public final CrashDetailBean packageCrashDatas(String str, String str2, long j, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, byte[] bArr, Map<String, String> map, boolean z) throws IOException {
        int length;
        String str11;
        int iIndexOf;
        boolean zL = c.a().l();
        if (zL) {
            x.e("This Crash Caused By ANR , PLS To Fix ANR , This Trace May Be Not Useful!", new Object[0]);
        }
        CrashDetailBean crashDetailBean = new CrashDetailBean();
        crashDetailBean.b = 1;
        crashDetailBean.e = this.c.h();
        crashDetailBean.f = this.c.j;
        crashDetailBean.g = this.c.w();
        crashDetailBean.m = this.c.g();
        crashDetailBean.n = str3;
        crashDetailBean.o = zL ? " This Crash Caused By ANR , PLS To Fix ANR , This Trace May Be Not Useful![Bugly]" : "";
        crashDetailBean.p = str4;
        if (str5 == null) {
            str5 = "";
        }
        crashDetailBean.q = str5;
        crashDetailBean.r = j;
        crashDetailBean.u = z.b(crashDetailBean.q.getBytes());
        crashDetailBean.z = str;
        crashDetailBean.A = str2;
        crashDetailBean.H = this.c.y();
        crashDetailBean.h = this.c.v();
        crashDetailBean.i = this.c.K();
        crashDetailBean.v = str8;
        String dumpFilePath = null;
        NativeCrashHandler nativeCrashHandler = NativeCrashHandler.getInstance();
        if (nativeCrashHandler != null) {
            dumpFilePath = nativeCrashHandler.getDumpFilePath();
        }
        String strA = b.a(dumpFilePath, str8);
        if (!z.a(strA)) {
            crashDetailBean.T = strA;
        }
        crashDetailBean.U = b.b(dumpFilePath);
        crashDetailBean.w = b.a(str9, c.e, (String) null);
        crashDetailBean.I = str7;
        crashDetailBean.J = str6;
        crashDetailBean.K = str10;
        crashDetailBean.E = this.c.p();
        crashDetailBean.F = this.c.o();
        crashDetailBean.G = this.c.q();
        if (z) {
            crashDetailBean.B = com.tencent.bugly.crashreport.common.info.b.h();
            crashDetailBean.C = com.tencent.bugly.crashreport.common.info.b.f();
            crashDetailBean.D = com.tencent.bugly.crashreport.common.info.b.j();
            if (crashDetailBean.w == null) {
                crashDetailBean.w = z.a(this.f171a, c.e, (String) null);
            }
            crashDetailBean.x = y.a();
            crashDetailBean.L = this.c.f141a;
            crashDetailBean.M = this.c.a();
            crashDetailBean.O = this.c.H();
            crashDetailBean.P = this.c.I();
            crashDetailBean.Q = this.c.B();
            crashDetailBean.R = this.c.G();
            crashDetailBean.y = z.a(c.f, false);
            int iIndexOf2 = crashDetailBean.q.indexOf("java:\n");
            if (iIndexOf2 > 0 && (length = iIndexOf2 + "java:\n".length()) < crashDetailBean.q.length()) {
                String strSubstring = crashDetailBean.q.substring(length, crashDetailBean.q.length() - 1);
                if (strSubstring.length() > 0 && crashDetailBean.y.containsKey(crashDetailBean.A) && (iIndexOf = (str11 = crashDetailBean.y.get(crashDetailBean.A)).indexOf(strSubstring)) > 0) {
                    String strSubstring2 = str11.substring(iIndexOf);
                    crashDetailBean.y.put(crashDetailBean.A, strSubstring2);
                    crashDetailBean.q = crashDetailBean.q.substring(0, length);
                    crashDetailBean.q += strSubstring2;
                }
            }
            if (str == null) {
                crashDetailBean.z = this.c.d;
            }
            this.b.c(crashDetailBean);
        } else {
            crashDetailBean.B = -1L;
            crashDetailBean.C = -1L;
            crashDetailBean.D = -1L;
            if (crashDetailBean.w == null) {
                crashDetailBean.w = "this crash is occurred at last process! Log is miss, when get an terrible ABRT Native Exception etc.";
            }
            crashDetailBean.L = -1L;
            crashDetailBean.O = -1;
            crashDetailBean.P = -1;
            crashDetailBean.Q = map;
            crashDetailBean.R = this.c.G();
            crashDetailBean.y = null;
            if (str == null) {
                crashDetailBean.z = "unknown(record)";
            }
            if (bArr != null) {
                crashDetailBean.x = bArr;
            }
        }
        return crashDetailBean;
    }

    @Override // com.tencent.bugly.crashreport.crash.jni.NativeExceptionHandler
    public final void handleNativeException(int i, int i2, long j, long j2, String str, String str2, String str3, String str4, int i3, String str5, int i4, int i5, int i6, String str6, String str7) {
        x.a("Native Crash Happen v1", new Object[0]);
        handleNativeException2(i, i2, j, j2, str, str2, str3, str4, i3, str5, i4, i5, i6, str6, str7, null);
    }

    @Override // com.tencent.bugly.crashreport.crash.jni.NativeExceptionHandler
    public final void handleNativeException2(int i, int i2, long j, long j2, String str, String str2, String str3, String str4, int i3, String str5, int i4, int i5, int i6, String str6, String str7, String[] strArr) {
        String str8;
        String str9;
        String str10;
        String str11;
        x.a("Native Crash Happen v2", new Object[0]);
        try {
            if (!this.d.b()) {
                x.e("waiting for remote sync", new Object[0]);
                int i7 = 0;
                while (!this.d.b()) {
                    z.b(500L);
                    i7 += cn.yunzhisheng.asr.a.U;
                    if (i7 >= 3000) {
                        break;
                    }
                }
            }
            String strA = b.a(str3);
            String strA2 = "UNKNOWN";
            if (i3 > 0) {
                str8 = "KERNEL";
                str9 = str + "(" + str5 + ")";
            } else {
                if (i4 > 0) {
                    Context context = this.f171a;
                    strA2 = AppInfo.a(i4);
                }
                if (strA2.equals(String.valueOf(i4))) {
                    str8 = str5;
                    str9 = str;
                } else {
                    strA2 = strA2 + "(" + i4 + ")";
                    str8 = str5;
                    str9 = str;
                }
            }
            if (!this.d.b()) {
                x.d("no remote but still store!", new Object[0]);
            }
            if (!this.d.c().g && this.d.b()) {
                x.e("crash report was closed by remote , will not upload to Bugly , print local for helpful!", new Object[0]);
                com.tencent.bugly.crashreport.crash.b.a("NATIVE_CRASH", z.a(), this.c.d, Thread.currentThread(), str9 + "\n" + str2 + "\n" + strA, null);
                z.b(str4);
                return;
            }
            HashMap map = new HashMap();
            if (strArr != null) {
                for (String str12 : strArr) {
                    String[] strArrSplit = str12.split("=");
                    if (strArrSplit.length == 2) {
                        map.put(strArrSplit[0], strArrSplit[1]);
                    } else {
                        x.d("bad extraMsg %s", str12);
                    }
                }
            } else {
                x.c("not found extraMsg", new Object[0]);
            }
            String str13 = (String) map.get("ExceptionProcessName");
            if (str13 == null || str13.length() == 0) {
                str10 = this.c.d;
            } else {
                x.c("crash process name change to %s", str13);
                str10 = str13;
            }
            String str14 = (String) map.get("ExceptionThreadName");
            if (str14 == null || str14.length() == 0) {
                Thread threadCurrentThread = Thread.currentThread();
                str11 = threadCurrentThread.getName() + "(" + threadCurrentThread.getId() + ")";
            } else {
                x.c("crash thread name change to %s", str14);
                Iterator<Thread> it = Thread.getAllStackTraces().keySet().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        str11 = str14;
                        break;
                    }
                    Thread next = it.next();
                    if (next.getName().equals(str14)) {
                        str11 = str14 + "(" + next.getId() + ")";
                        break;
                    }
                }
            }
            CrashDetailBean crashDetailBeanPackageCrashDatas = packageCrashDatas(str10, str11, (j2 / 1000) + (1000 * j), str9, str2, strA, str8, strA2, str4, (String) map.get("SysLogPath"), str7, null, null, true);
            if (crashDetailBeanPackageCrashDatas == null) {
                x.e("pkg crash datas fail!", new Object[0]);
                return;
            }
            com.tencent.bugly.crashreport.crash.b.a("NATIVE_CRASH", z.a(), this.c.d, Thread.currentThread(), str9 + "\n" + str2 + "\n" + strA, crashDetailBeanPackageCrashDatas);
            if (!this.b.a(crashDetailBeanPackageCrashDatas, i3)) {
                this.b.a(crashDetailBeanPackageCrashDatas, 3000L, true);
            }
            this.b.b(crashDetailBeanPackageCrashDatas);
            String dumpFilePath = null;
            NativeCrashHandler nativeCrashHandler = NativeCrashHandler.getInstance();
            if (nativeCrashHandler != null) {
                dumpFilePath = nativeCrashHandler.getDumpFilePath();
            }
            b.a(true, dumpFilePath);
        } catch (Throwable th) {
            if (!x.a(th)) {
                th.printStackTrace();
            }
        }
    }
}
