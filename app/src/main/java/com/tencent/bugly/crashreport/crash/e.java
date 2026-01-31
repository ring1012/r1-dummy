package com.tencent.bugly.crashreport.crash;

import android.content.Context;
import android.os.Process;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.y;
import com.tencent.bugly.proguard.z;
import java.lang.Thread;
import java.util.HashMap;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class e implements Thread.UncaughtExceptionHandler {
    private static String h = null;
    private static final Object i = new Object();

    /* renamed from: a, reason: collision with root package name */
    private Context f165a;
    private b b;
    private com.tencent.bugly.crashreport.common.strategy.a c;
    private com.tencent.bugly.crashreport.common.info.a d;
    private Thread.UncaughtExceptionHandler e;
    private Thread.UncaughtExceptionHandler f;
    private boolean g = false;
    private int j;

    public e(Context context, b bVar, com.tencent.bugly.crashreport.common.strategy.a aVar, com.tencent.bugly.crashreport.common.info.a aVar2) {
        this.f165a = context;
        this.b = bVar;
        this.c = aVar;
        this.d = aVar2;
    }

    public final synchronized void a() {
        if (this.j >= 10) {
            x.a("java crash handler over %d, no need set.", 10);
        } else {
            this.g = true;
            Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
            if (defaultUncaughtExceptionHandler != null) {
                if (!getClass().getName().equals(defaultUncaughtExceptionHandler.getClass().getName())) {
                    if ("com.android.internal.os.RuntimeInit$UncaughtHandler".equals(defaultUncaughtExceptionHandler.getClass().getName())) {
                        x.a("backup system java handler: %s", defaultUncaughtExceptionHandler.toString());
                        this.f = defaultUncaughtExceptionHandler;
                        this.e = defaultUncaughtExceptionHandler;
                    } else {
                        x.a("backup java handler: %s", defaultUncaughtExceptionHandler.toString());
                        this.e = defaultUncaughtExceptionHandler;
                    }
                    Thread.setDefaultUncaughtExceptionHandler(this);
                    this.j++;
                    x.a("registered java monitor: %s", toString());
                }
            } else {
                Thread.setDefaultUncaughtExceptionHandler(this);
                this.j++;
                x.a("registered java monitor: %s", toString());
            }
        }
    }

    public final synchronized void b() {
        this.g = false;
        x.a("close java monitor!", new Object[0]);
        if (Thread.getDefaultUncaughtExceptionHandler().getClass().getName().contains("bugly")) {
            x.a("Java monitor to unregister: %s", toString());
            Thread.setDefaultUncaughtExceptionHandler(this.e);
            this.j--;
        }
    }

    private CrashDetailBean b(Thread thread, Throwable th, boolean z, String str, byte[] bArr) {
        String strA;
        if (th == null) {
            x.d("We can do nothing with a null throwable.", new Object[0]);
            return null;
        }
        boolean zL = c.a().l();
        String str2 = (zL && z) ? " This Crash Caused By ANR , PLS To Fix ANR , This Trace May Be Not Useful![Bugly]" : "";
        if (zL && z) {
            x.e("This Crash Caused By ANR , PLS To Fix ANR , This Trace May Be Not Useful!", new Object[0]);
        }
        CrashDetailBean crashDetailBean = new CrashDetailBean();
        crashDetailBean.B = com.tencent.bugly.crashreport.common.info.b.h();
        crashDetailBean.C = com.tencent.bugly.crashreport.common.info.b.f();
        crashDetailBean.D = com.tencent.bugly.crashreport.common.info.b.j();
        crashDetailBean.E = this.d.p();
        crashDetailBean.F = this.d.o();
        crashDetailBean.G = this.d.q();
        crashDetailBean.w = z.a(this.f165a, c.e, (String) null);
        crashDetailBean.x = y.a();
        Object[] objArr = new Object[1];
        objArr[0] = Integer.valueOf(crashDetailBean.x == null ? 0 : crashDetailBean.x.length);
        x.a("user log size:%d", objArr);
        crashDetailBean.b = z ? 0 : 2;
        crashDetailBean.e = this.d.h();
        crashDetailBean.f = this.d.j;
        crashDetailBean.g = this.d.w();
        crashDetailBean.m = this.d.g();
        String name = th.getClass().getName();
        String strB = b(th, 1000);
        if (strB == null) {
            strB = "";
        }
        Object[] objArr2 = new Object[2];
        objArr2[0] = Integer.valueOf(th.getStackTrace().length);
        objArr2[1] = Boolean.valueOf(th.getCause() != null);
        x.e("stack frame :%d, has cause %b", objArr2);
        String string = "";
        if (th.getStackTrace().length > 0) {
            string = th.getStackTrace()[0].toString();
        }
        Throwable cause = th;
        while (cause != null && cause.getCause() != null) {
            cause = cause.getCause();
        }
        if (cause != null && cause != th) {
            crashDetailBean.n = cause.getClass().getName();
            crashDetailBean.o = b(cause, 1000);
            if (crashDetailBean.o == null) {
                crashDetailBean.o = "";
            }
            if (cause.getStackTrace().length > 0) {
                crashDetailBean.p = cause.getStackTrace()[0].toString();
            }
            StringBuilder sb = new StringBuilder();
            sb.append(name).append(":").append(strB).append("\n");
            sb.append(string);
            sb.append("\n......");
            sb.append("\nCaused by:\n");
            sb.append(crashDetailBean.n).append(":").append(crashDetailBean.o).append("\n");
            strA = a(cause, c.f);
            sb.append(strA);
            crashDetailBean.q = sb.toString();
        } else {
            crashDetailBean.n = name;
            crashDetailBean.o = strB + str2;
            if (crashDetailBean.o == null) {
                crashDetailBean.o = "";
            }
            crashDetailBean.p = string;
            strA = a(th, c.f);
            crashDetailBean.q = strA;
        }
        crashDetailBean.r = System.currentTimeMillis();
        crashDetailBean.u = z.b(crashDetailBean.q.getBytes());
        try {
            crashDetailBean.y = z.a(c.f, false);
            crashDetailBean.z = this.d.d;
            crashDetailBean.A = thread.getName() + "(" + thread.getId() + ")";
            crashDetailBean.y.put(crashDetailBean.A, strA);
            crashDetailBean.H = this.d.y();
            crashDetailBean.h = this.d.v();
            crashDetailBean.i = this.d.K();
            crashDetailBean.L = this.d.f141a;
            crashDetailBean.M = this.d.a();
            crashDetailBean.O = this.d.H();
            crashDetailBean.P = this.d.I();
            crashDetailBean.Q = this.d.B();
            crashDetailBean.R = this.d.G();
        } catch (Throwable th2) {
            x.e("handle crash error %s", th2.toString());
        }
        if (z) {
            this.b.c(crashDetailBean);
        } else {
            boolean z2 = str != null && str.length() > 0;
            boolean z3 = bArr != null && bArr.length > 0;
            if (z2) {
                crashDetailBean.N = new HashMap(1);
                crashDetailBean.N.put("UserData", str);
            }
            if (z3) {
                crashDetailBean.S = bArr;
            }
        }
        return crashDetailBean;
    }

    private static boolean a(Thread thread) {
        boolean z;
        synchronized (i) {
            if (h == null || !thread.getName().equals(h)) {
                h = thread.getName();
                z = false;
            } else {
                z = true;
            }
        }
        return z;
    }

    public final void a(Thread thread, Throwable th, boolean z, String str, byte[] bArr) {
        if (z) {
            x.e("Java Crash Happen cause by %s(%d)", thread.getName(), Long.valueOf(thread.getId()));
            if (a(thread)) {
                x.a("this class has handled this exception", new Object[0]);
                if (this.f != null) {
                    x.a("call system handler", new Object[0]);
                    this.f.uncaughtException(thread, th);
                } else {
                    x.e("current process die", new Object[0]);
                    Process.killProcess(Process.myPid());
                    System.exit(1);
                }
            }
        } else {
            x.e("Java Catch Happen", new Object[0]);
        }
        try {
            try {
                if (!this.g) {
                    x.c("Java crash handler is disable. Just return.", new Object[0]);
                    if (z) {
                        if (this.e != null && a(this.e)) {
                            x.e("sys default last handle start!", new Object[0]);
                            this.e.uncaughtException(thread, th);
                            x.e("sys default last handle end!", new Object[0]);
                            return;
                        } else if (this.f != null) {
                            x.e("system handle start!", new Object[0]);
                            this.f.uncaughtException(thread, th);
                            x.e("system handle end!", new Object[0]);
                            return;
                        } else {
                            x.e("crashreport last handle start!", new Object[0]);
                            x.e("current process die", new Object[0]);
                            Process.killProcess(Process.myPid());
                            System.exit(1);
                            x.e("crashreport last handle end!", new Object[0]);
                            return;
                        }
                    }
                    return;
                }
                if (!this.c.b()) {
                    x.e("waiting for remote sync", new Object[0]);
                    int i2 = 0;
                    while (!this.c.b()) {
                        z.b(500L);
                        i2 += cn.yunzhisheng.asr.a.U;
                        if (i2 >= 3000) {
                            break;
                        }
                    }
                }
                if (!this.c.b()) {
                    x.d("no remote but still store!", new Object[0]);
                }
                if (!this.c.c().g && this.c.b()) {
                    x.e("crash report was closed by remote , will not upload to Bugly , print local for helpful!", new Object[0]);
                    b.a(z ? "JAVA_CRASH" : "JAVA_CATCH", z.a(), this.d.d, thread, z.a(th), null);
                    if (z) {
                        if (this.e != null && a(this.e)) {
                            x.e("sys default last handle start!", new Object[0]);
                            this.e.uncaughtException(thread, th);
                            x.e("sys default last handle end!", new Object[0]);
                            return;
                        } else if (this.f != null) {
                            x.e("system handle start!", new Object[0]);
                            this.f.uncaughtException(thread, th);
                            x.e("system handle end!", new Object[0]);
                            return;
                        } else {
                            x.e("crashreport last handle start!", new Object[0]);
                            x.e("current process die", new Object[0]);
                            Process.killProcess(Process.myPid());
                            System.exit(1);
                            x.e("crashreport last handle end!", new Object[0]);
                            return;
                        }
                    }
                    return;
                }
                CrashDetailBean crashDetailBeanB = b(thread, th, z, str, bArr);
                if (crashDetailBeanB == null) {
                    x.e("pkg crash datas fail!", new Object[0]);
                    if (z) {
                        if (this.e != null && a(this.e)) {
                            x.e("sys default last handle start!", new Object[0]);
                            this.e.uncaughtException(thread, th);
                            x.e("sys default last handle end!", new Object[0]);
                            return;
                        } else if (this.f != null) {
                            x.e("system handle start!", new Object[0]);
                            this.f.uncaughtException(thread, th);
                            x.e("system handle end!", new Object[0]);
                            return;
                        } else {
                            x.e("crashreport last handle start!", new Object[0]);
                            x.e("current process die", new Object[0]);
                            Process.killProcess(Process.myPid());
                            System.exit(1);
                            x.e("crashreport last handle end!", new Object[0]);
                            return;
                        }
                    }
                    return;
                }
                b.a(z ? "JAVA_CRASH" : "JAVA_CATCH", z.a(), this.d.d, thread, z.a(th), crashDetailBeanB);
                if (!this.b.a(crashDetailBeanB)) {
                    this.b.a(crashDetailBeanB, 3000L, z);
                }
                this.b.b(crashDetailBeanB);
                if (z) {
                    if (this.e != null && a(this.e)) {
                        x.e("sys default last handle start!", new Object[0]);
                        this.e.uncaughtException(thread, th);
                        x.e("sys default last handle end!", new Object[0]);
                    } else if (this.f != null) {
                        x.e("system handle start!", new Object[0]);
                        this.f.uncaughtException(thread, th);
                        x.e("system handle end!", new Object[0]);
                    } else {
                        x.e("crashreport last handle start!", new Object[0]);
                        x.e("current process die", new Object[0]);
                        Process.killProcess(Process.myPid());
                        System.exit(1);
                        x.e("crashreport last handle end!", new Object[0]);
                    }
                }
            } catch (Throwable th2) {
                if (!x.a(th2)) {
                    th2.printStackTrace();
                }
                if (z) {
                    if (this.e != null && a(this.e)) {
                        x.e("sys default last handle start!", new Object[0]);
                        this.e.uncaughtException(thread, th);
                        x.e("sys default last handle end!", new Object[0]);
                    } else if (this.f != null) {
                        x.e("system handle start!", new Object[0]);
                        this.f.uncaughtException(thread, th);
                        x.e("system handle end!", new Object[0]);
                    } else {
                        x.e("crashreport last handle start!", new Object[0]);
                        x.e("current process die", new Object[0]);
                        Process.killProcess(Process.myPid());
                        System.exit(1);
                        x.e("crashreport last handle end!", new Object[0]);
                    }
                }
            }
        } catch (Throwable th3) {
            if (z) {
                if (this.e != null && a(this.e)) {
                    x.e("sys default last handle start!", new Object[0]);
                    this.e.uncaughtException(thread, th);
                    x.e("sys default last handle end!", new Object[0]);
                } else if (this.f != null) {
                    x.e("system handle start!", new Object[0]);
                    this.f.uncaughtException(thread, th);
                    x.e("system handle end!", new Object[0]);
                } else {
                    x.e("crashreport last handle start!", new Object[0]);
                    x.e("current process die", new Object[0]);
                    Process.killProcess(Process.myPid());
                    System.exit(1);
                    x.e("crashreport last handle end!", new Object[0]);
                }
            }
            throw th3;
        }
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public final void uncaughtException(Thread thread, Throwable th) {
        synchronized (i) {
            a(thread, th, true, null, null);
        }
    }

    private static boolean a(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        if (uncaughtExceptionHandler == null) {
            return true;
        }
        String name = uncaughtExceptionHandler.getClass().getName();
        for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
            String className = stackTraceElement.getClassName();
            String methodName = stackTraceElement.getMethodName();
            if (name.equals(className) && "uncaughtException".equals(methodName)) {
                return false;
            }
        }
        return true;
    }

    public final synchronized void a(StrategyBean strategyBean) {
        if (strategyBean != null) {
            if (strategyBean.g != this.g) {
                x.a("java changed to %b", Boolean.valueOf(strategyBean.g));
                if (strategyBean.g) {
                    a();
                } else {
                    b();
                }
            }
        }
    }

    private static String a(Throwable th, int i2) {
        if (th == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        try {
            if (th.getStackTrace() != null) {
                for (StackTraceElement stackTraceElement : th.getStackTrace()) {
                    if (i2 > 0 && sb.length() >= i2) {
                        sb.append("\n[Stack over limit size :" + i2 + " , has been cutted !]");
                        return sb.toString();
                    }
                    sb.append(stackTraceElement.toString()).append("\n");
                }
            }
        } catch (Throwable th2) {
            x.e("gen stack error %s", th2.toString());
        }
        return sb.toString();
    }

    private static String b(Throwable th, int i2) {
        if (th.getMessage() == null) {
            return "";
        }
        if (th.getMessage().length() <= 1000) {
            return th.getMessage();
        }
        return th.getMessage().substring(0, 1000) + "\n[Message over limit size:1000, has been cutted!]";
    }
}
