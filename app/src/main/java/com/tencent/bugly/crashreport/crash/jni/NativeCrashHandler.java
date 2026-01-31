package com.tencent.bugly.crashreport.crash.jni;

import android.annotation.SuppressLint;
import android.content.Context;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.crash.CrashDetailBean;
import com.tencent.bugly.crashreport.crash.c;
import com.tencent.bugly.proguard.w;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.z;
import java.io.File;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public class NativeCrashHandler implements com.tencent.bugly.crashreport.a {

    /* renamed from: a, reason: collision with root package name */
    private static NativeCrashHandler f169a;
    private static boolean l = false;
    private static boolean m = false;
    private final Context b;
    private final com.tencent.bugly.crashreport.common.info.a c;
    private final w d;
    private NativeExceptionHandler e;
    private String f;
    private final boolean g;
    private boolean h = false;
    private boolean i = false;
    private boolean j = false;
    private boolean k = false;
    private com.tencent.bugly.crashreport.crash.b n;

    protected native boolean appendNativeLog(String str, String str2, String str3);

    protected native boolean appendWholeNativeLog(String str);

    protected native String getNativeKeyValueList();

    protected native String getNativeLog();

    protected native boolean putNativeKeyValue(String str, String str2);

    protected native String regist(String str, boolean z, int i);

    protected native String removeNativeKeyValue(String str);

    protected native void setNativeInfo(int i, String str);

    protected native void testCrash();

    protected native String unregist();

    @SuppressLint({"SdCardPath"})
    private NativeCrashHandler(Context context, com.tencent.bugly.crashreport.common.info.a aVar, com.tencent.bugly.crashreport.crash.b bVar, w wVar, boolean z, String str) {
        this.b = z.a(context);
        try {
            if (z.a(str)) {
                str = context.getDir("bugly", 0).getAbsolutePath();
            }
        } catch (Throwable th) {
            str = "/data/data/" + com.tencent.bugly.crashreport.common.info.a.a(context).c + "/app_bugly";
        }
        this.n = bVar;
        this.f = str;
        this.c = aVar;
        this.d = wVar;
        this.g = z;
        this.e = new a(context, aVar, bVar, com.tencent.bugly.crashreport.common.strategy.a.a());
    }

    public static synchronized NativeCrashHandler getInstance(Context context, com.tencent.bugly.crashreport.common.info.a aVar, com.tencent.bugly.crashreport.crash.b bVar, com.tencent.bugly.crashreport.common.strategy.a aVar2, w wVar, boolean z, String str) {
        if (f169a == null) {
            f169a = new NativeCrashHandler(context, aVar, bVar, wVar, z, str);
        }
        return f169a;
    }

    public static synchronized NativeCrashHandler getInstance() {
        return f169a;
    }

    public synchronized String getDumpFilePath() {
        return this.f;
    }

    public synchronized void setDumpFilePath(String str) {
        this.f = str;
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x00aa A[Catch: all -> 0x00b2, TRY_LEAVE, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x0007, B:9:0x0011, B:11:0x0015, B:13:0x001e, B:15:0x0053, B:16:0x0066, B:18:0x0070, B:19:0x0073, B:21:0x007d, B:22:0x0080, B:24:0x0084, B:25:0x008c, B:27:0x0090, B:28:0x0098, B:41:0x00d8, B:40:0x00cf, B:37:0x00b5, B:39:0x00bb, B:32:0x00aa, B:43:0x00e1, B:45:0x00e5, B:48:0x0115, B:50:0x012b, B:52:0x0168, B:54:0x018c, B:55:0x0192, B:58:0x01b0, B:31:0x00a2), top: B:66:0x0003, inners: #2 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private synchronized void a(boolean r9) {
        /*
            Method dump skipped, instructions count: 471
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.crash.jni.NativeCrashHandler.a(boolean):void");
    }

    public synchronized void startNativeMonitor() {
        if (this.i || this.h) {
            a(this.g);
        } else {
            String str = "Bugly";
            boolean z = !z.a(this.c.m);
            String str2 = this.c.m;
            if (z) {
                str = str2;
            } else {
                this.c.getClass();
            }
            this.i = a(str, z);
            if (this.i || this.h) {
                a(this.g);
                this.d.a(new Runnable() { // from class: com.tencent.bugly.crashreport.crash.jni.NativeCrashHandler.1
                    @Override // java.lang.Runnable
                    public final void run() throws Throwable {
                        if (z.a(NativeCrashHandler.this.b, "native_record_lock", 10000L)) {
                            try {
                                NativeCrashHandler.this.setNativeAppVersion(NativeCrashHandler.this.c.j);
                                NativeCrashHandler.this.setNativeAppChannel(NativeCrashHandler.this.c.l);
                                NativeCrashHandler.this.setNativeAppPackage(NativeCrashHandler.this.c.c);
                                NativeCrashHandler.this.setNativeUserId(NativeCrashHandler.this.c.g());
                                NativeCrashHandler.this.setNativeIsAppForeground(NativeCrashHandler.this.c.a());
                                NativeCrashHandler.this.setNativeLaunchTime(NativeCrashHandler.this.c.f141a);
                            } catch (Throwable th) {
                                if (!x.a(th)) {
                                    th.printStackTrace();
                                }
                            }
                            CrashDetailBean crashDetailBeanA = b.a(NativeCrashHandler.this.b, NativeCrashHandler.this.f, NativeCrashHandler.this.e);
                            if (crashDetailBeanA != null) {
                                x.a("[Native] Get crash from native record.", new Object[0]);
                                if (!NativeCrashHandler.this.n.a(crashDetailBeanA)) {
                                    NativeCrashHandler.this.n.a(crashDetailBeanA, 3000L, false);
                                }
                                b.a(false, NativeCrashHandler.this.f);
                            }
                            NativeCrashHandler.this.a();
                            z.b(NativeCrashHandler.this.b, "native_record_lock");
                            return;
                        }
                        x.a("[Native] Failed to lock file for handling native crash record.", new Object[0]);
                    }
                });
            }
        }
    }

    private static boolean a(String str, boolean z) {
        Throwable th;
        boolean z2;
        try {
            x.a("[Native] Trying to load so: %s", str);
            if (z) {
                System.load(str);
            } else {
                System.loadLibrary(str);
            }
            try {
                x.a("[Native] Successfully loaded SO: %s", str);
                return true;
            } catch (Throwable th2) {
                th = th2;
                z2 = true;
                x.d(th.getMessage(), new Object[0]);
                x.d("[Native] Failed to load so: %s", str);
                return z2;
            }
        } catch (Throwable th3) {
            th = th3;
            z2 = false;
        }
    }

    private synchronized void b() {
        if (!this.j) {
            x.d("[Native] Native crash report has already unregistered.", new Object[0]);
        } else {
            try {
                if (unregist() != null) {
                    x.a("[Native] Successfully closed native crash report.", new Object[0]);
                    this.j = false;
                }
            } catch (Throwable th) {
                x.c("[Native] Failed to close native crash report.", new Object[0]);
            }
            try {
                z.a("com.tencent.feedback.eup.jni.NativeExceptionUpload", "enableHandler", null, new Class[]{Boolean.TYPE}, new Object[]{false});
                this.j = false;
                x.a("[Native] Successfully closed native crash report.", new Object[0]);
            } catch (Throwable th2) {
                x.c("[Native] Failed to close native crash report.", new Object[0]);
                this.i = false;
                this.h = false;
            }
        }
    }

    public void testNativeCrash() {
        if (!this.i) {
            x.d("[Native] Bugly SO file has not been load.", new Object[0]);
        } else {
            testCrash();
        }
    }

    public NativeExceptionHandler getNativeExceptionHandler() {
        return this.e;
    }

    protected final void a() {
        File[] fileArrListFiles;
        int iIndexOf;
        long jB = z.b() - c.g;
        File file = new File(this.f);
        if (file.exists() && file.isDirectory() && (fileArrListFiles = file.listFiles()) != null && fileArrListFiles.length != 0) {
            int length = "tomb_".length();
            int i = 0;
            for (File file2 : fileArrListFiles) {
                String name = file2.getName();
                if (name.startsWith("tomb_")) {
                    try {
                        iIndexOf = name.indexOf(".txt");
                    } catch (Throwable th) {
                        x.e("[Native] Tomb file format error, delete %s", name);
                    }
                    if (iIndexOf <= 0 || Long.parseLong(name.substring(length, iIndexOf)) < jB) {
                        if (file2.delete()) {
                            i++;
                        }
                    }
                }
            }
            x.c("[Native] Clean tombs %d", Integer.valueOf(i));
        }
    }

    private synchronized void b(boolean z) {
        if (z) {
            startNativeMonitor();
        } else {
            b();
        }
    }

    public synchronized boolean isUserOpened() {
        return this.k;
    }

    private synchronized void c(boolean z) {
        if (this.k != z) {
            x.a("user change native %b", Boolean.valueOf(z));
            this.k = z;
        }
    }

    public synchronized void setUserOpened(boolean z) {
        boolean z2 = true;
        synchronized (this) {
            c(z);
            boolean zIsUserOpened = isUserOpened();
            com.tencent.bugly.crashreport.common.strategy.a aVarA = com.tencent.bugly.crashreport.common.strategy.a.a();
            if (aVarA == null) {
                z2 = zIsUserOpened;
            } else if (!zIsUserOpened || !aVarA.c().g) {
                z2 = false;
            }
            if (z2 != this.j) {
                x.a("native changed to %b", Boolean.valueOf(z2));
                b(z2);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0030 A[Catch: all -> 0x0046, TRY_LEAVE, TryCatch #0 {, blocks: (B:5:0x0005, B:7:0x000b, B:8:0x001c, B:10:0x0028, B:12:0x002c, B:14:0x0030), top: B:21:0x0005 }] */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0044  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized void onStrategyChanged(com.tencent.bugly.crashreport.common.strategy.StrategyBean r7) {
        /*
            r6 = this;
            r0 = 1
            r1 = 0
            monitor-enter(r6)
            if (r7 == 0) goto L1c
            boolean r2 = r7.g     // Catch: java.lang.Throwable -> L46
            boolean r3 = r6.j     // Catch: java.lang.Throwable -> L46
            if (r2 == r3) goto L1c
            java.lang.String r2 = "server native changed to %b"
            r3 = 1
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch: java.lang.Throwable -> L46
            r4 = 0
            boolean r5 = r7.g     // Catch: java.lang.Throwable -> L46
            java.lang.Boolean r5 = java.lang.Boolean.valueOf(r5)     // Catch: java.lang.Throwable -> L46
            r3[r4] = r5     // Catch: java.lang.Throwable -> L46
            com.tencent.bugly.proguard.x.d(r2, r3)     // Catch: java.lang.Throwable -> L46
        L1c:
            com.tencent.bugly.crashreport.common.strategy.a r2 = com.tencent.bugly.crashreport.common.strategy.a.a()     // Catch: java.lang.Throwable -> L46
            com.tencent.bugly.crashreport.common.strategy.StrategyBean r2 = r2.c()     // Catch: java.lang.Throwable -> L46
            boolean r2 = r2.g     // Catch: java.lang.Throwable -> L46
            if (r2 == 0) goto L44
            boolean r2 = r6.k     // Catch: java.lang.Throwable -> L46
            if (r2 == 0) goto L44
        L2c:
            boolean r1 = r6.j     // Catch: java.lang.Throwable -> L46
            if (r0 == r1) goto L42
            java.lang.String r1 = "native changed to %b"
            r2 = 1
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch: java.lang.Throwable -> L46
            r3 = 0
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r0)     // Catch: java.lang.Throwable -> L46
            r2[r3] = r4     // Catch: java.lang.Throwable -> L46
            com.tencent.bugly.proguard.x.a(r1, r2)     // Catch: java.lang.Throwable -> L46
            r6.b(r0)     // Catch: java.lang.Throwable -> L46
        L42:
            monitor-exit(r6)
            return
        L44:
            r0 = r1
            goto L2c
        L46:
            r0 = move-exception
            monitor-exit(r6)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.crash.jni.NativeCrashHandler.onStrategyChanged(com.tencent.bugly.crashreport.common.strategy.StrategyBean):void");
    }

    @Override // com.tencent.bugly.crashreport.a
    public boolean appendLogToNative(String str, String str2, String str3) {
        boolean zBooleanValue;
        if ((this.h || this.i) && l) {
            if (str == null || str2 == null || str3 == null) {
                return false;
            }
            try {
                if (this.i) {
                    zBooleanValue = appendNativeLog(str, str2, str3);
                } else {
                    Boolean bool = (Boolean) z.a("com.tencent.feedback.eup.jni.NativeExceptionUpload", "appendNativeLog", null, new Class[]{String.class, String.class, String.class}, new Object[]{str, str2, str3});
                    zBooleanValue = bool != null ? bool.booleanValue() : false;
                }
                return zBooleanValue;
            } catch (UnsatisfiedLinkError e) {
                l = false;
                return false;
            } catch (Throwable th) {
                if (!x.a(th)) {
                    th.printStackTrace();
                }
                return false;
            }
        }
        return false;
    }

    public boolean putKeyValueToNative(String str, String str2) {
        boolean zBooleanValue;
        if ((this.h || this.i) && l) {
            if (str == null || str2 == null) {
                return false;
            }
            try {
                if (this.i) {
                    zBooleanValue = putNativeKeyValue(str, str2);
                } else {
                    Boolean bool = (Boolean) z.a("com.tencent.feedback.eup.jni.NativeExceptionUpload", "putNativeKeyValue", null, new Class[]{String.class, String.class}, new Object[]{str, str2});
                    zBooleanValue = bool != null ? bool.booleanValue() : false;
                }
                return zBooleanValue;
            } catch (UnsatisfiedLinkError e) {
                l = false;
                return false;
            } catch (Throwable th) {
                if (!x.a(th)) {
                    th.printStackTrace();
                }
                return false;
            }
        }
        return false;
    }

    private boolean a(int i, String str) {
        if (!this.i || !m) {
            return false;
        }
        try {
            setNativeInfo(i, str);
            return true;
        } catch (UnsatisfiedLinkError e) {
            m = false;
            return false;
        } catch (Throwable th) {
            if (x.a(th)) {
                return false;
            }
            th.printStackTrace();
            return false;
        }
    }

    public boolean filterSigabrtSysLog() {
        return a(998, "true");
    }

    public boolean setNativeAppVersion(String str) {
        return a(10, str);
    }

    public boolean setNativeAppChannel(String str) {
        return a(12, str);
    }

    public boolean setNativeAppPackage(String str) {
        return a(13, str);
    }

    public boolean setNativeUserId(String str) {
        return a(11, str);
    }

    @Override // com.tencent.bugly.crashreport.a
    public boolean setNativeIsAppForeground(boolean z) {
        return a(14, z ? "true" : Bugly.SDK_IS_DEV);
    }

    public boolean setNativeLaunchTime(long j) {
        try {
            return a(15, String.valueOf(j));
        } catch (NumberFormatException e) {
            if (!x.a(e)) {
                e.printStackTrace();
            }
            return false;
        }
    }
}
