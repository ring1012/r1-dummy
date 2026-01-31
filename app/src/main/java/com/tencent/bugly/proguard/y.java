package com.tencent.bugly.proguard;

import android.content.Context;
import android.os.Process;
import cn.yunzhisheng.common.PinyinConverter;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class y {
    private static SimpleDateFormat b;
    private static StringBuilder d;
    private static StringBuilder e;
    private static boolean f;
    private static a g;
    private static String h;
    private static String i;
    private static Context j;
    private static String k;
    private static boolean l;
    private static int m;

    /* renamed from: a, reason: collision with root package name */
    public static boolean f209a = true;
    private static int c = 5120;
    private static final Object n = new Object();

    static /* synthetic */ boolean a(boolean z) {
        f = false;
        return false;
    }

    static {
        b = null;
        try {
            b = new SimpleDateFormat("MM-dd HH:mm:ss");
        } catch (Throwable th) {
        }
    }

    private static boolean b(String str, String str2, String str3) {
        try {
            com.tencent.bugly.crashreport.common.info.a aVarB = com.tencent.bugly.crashreport.common.info.a.b();
            if (aVarB != null && aVarB.D != null) {
                return aVarB.D.appendLogToNative(str, str2, str3);
            }
        } catch (Throwable th) {
            if (!x.a(th)) {
                th.printStackTrace();
            }
        }
        return false;
    }

    public static synchronized void a(Context context) {
        if (!l && context != null && f209a) {
            try {
                e = new StringBuilder(0);
                d = new StringBuilder(0);
                j = context;
                com.tencent.bugly.crashreport.common.info.a aVarA = com.tencent.bugly.crashreport.common.info.a.a(context);
                h = aVarA.d;
                aVarA.getClass();
                i = "";
                k = j.getFilesDir().getPath() + "/buglylog_" + h + "_" + i + ".txt";
                m = Process.myPid();
            } catch (Throwable th) {
            }
            l = true;
        }
    }

    public static void a(int i2) {
        synchronized (n) {
            c = i2;
            if (i2 < 0) {
                c = 0;
            } else if (i2 > 10240) {
                c = 10240;
            }
        }
    }

    public static void a(String str, String str2, Throwable th) {
        if (th != null) {
            String message = th.getMessage();
            if (message == null) {
                message = "";
            }
            a(str, str2, message + '\n' + z.b(th));
        }
    }

    public static synchronized void a(String str, String str2, String str3) {
        if (l && f209a) {
            b(str, str2, str3);
            long jMyTid = Process.myTid();
            d.setLength(0);
            if (str3.length() > 30720) {
                str3 = str3.substring(str3.length() - 30720, str3.length() - 1);
            }
            Date date = new Date();
            d.append(b != null ? b.format(date) : date.toString()).append(PinyinConverter.PINYIN_SEPARATOR).append(m).append(PinyinConverter.PINYIN_SEPARATOR).append(jMyTid).append(PinyinConverter.PINYIN_SEPARATOR).append(str).append(PinyinConverter.PINYIN_SEPARATOR).append(str2).append(": ").append(str3).append("\u0001\r\n");
            final String string = d.toString();
            synchronized (n) {
                e.append(string);
                if (e.length() > c) {
                    if (!f) {
                        f = true;
                        w.a().a(new Runnable() { // from class: com.tencent.bugly.proguard.y.1
                            @Override // java.lang.Runnable
                            public final void run() {
                                synchronized (y.n) {
                                    try {
                                        if (y.g == null) {
                                            a unused = y.g = new a(y.k);
                                        } else if (y.g.b == null || y.g.b.length() + y.e.length() > y.g.e) {
                                            y.g.a();
                                        }
                                        if (y.g.f211a) {
                                            y.g.a(y.e.toString());
                                            y.e.setLength(0);
                                        } else {
                                            y.e.setLength(0);
                                            y.e.append(string);
                                        }
                                        y.a(false);
                                    } catch (Throwable th) {
                                    }
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    public static byte[] a() {
        byte[] bArrA = null;
        if (f209a) {
            synchronized (n) {
                try {
                    File file = (g == null || !g.f211a) ? null : g.b;
                    if (e.length() != 0 || file != null) {
                        bArrA = z.a(file, e.toString(), "BuglyLog.txt");
                    }
                } catch (Throwable th) {
                }
            }
        }
        return bArrA;
    }

    /* compiled from: BUGLY */
    public static class a {

        /* renamed from: a, reason: collision with root package name */
        private boolean f211a;
        private File b;
        private String c;
        private long d;
        private long e = 30720;

        public a(String str) {
            if (str != null && !str.equals("")) {
                this.c = str;
                this.f211a = a();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Code restructure failed: missing block: B:19:0x001d, code lost:
        
            r0 = true;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public boolean a() {
            /*
                r3 = this;
                r0 = 0
                java.io.File r1 = new java.io.File     // Catch: java.lang.Throwable -> L2a
                java.lang.String r2 = r3.c     // Catch: java.lang.Throwable -> L2a
                r1.<init>(r2)     // Catch: java.lang.Throwable -> L2a
                r3.b = r1     // Catch: java.lang.Throwable -> L2a
                java.io.File r1 = r3.b     // Catch: java.lang.Throwable -> L2a
                boolean r1 = r1.exists()     // Catch: java.lang.Throwable -> L2a
                if (r1 == 0) goto L1e
                java.io.File r1 = r3.b     // Catch: java.lang.Throwable -> L2a
                boolean r1 = r1.delete()     // Catch: java.lang.Throwable -> L2a
                if (r1 != 0) goto L1e
                r1 = 0
                r3.f211a = r1     // Catch: java.lang.Throwable -> L2a
            L1d:
                return r0
            L1e:
                java.io.File r1 = r3.b     // Catch: java.lang.Throwable -> L2a
                boolean r1 = r1.createNewFile()     // Catch: java.lang.Throwable -> L2a
                if (r1 != 0) goto L2d
                r1 = 0
                r3.f211a = r1     // Catch: java.lang.Throwable -> L2a
                goto L1d
            L2a:
                r1 = move-exception
                r3.f211a = r0
            L2d:
                r0 = 1
                goto L1d
            */
            throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.y.a.a():boolean");
        }

        /* JADX WARN: Removed duplicated region for block: B:38:0x003c A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public final boolean a(java.lang.String r9) throws java.lang.Throwable {
            /*
                r8 = this;
                r1 = 1
                r0 = 0
                boolean r2 = r8.f211a
                if (r2 != 0) goto L7
            L6:
                return r0
            L7:
                r3 = 0
                java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch: java.lang.Throwable -> L2b java.lang.Throwable -> L38
                java.io.File r4 = r8.b     // Catch: java.lang.Throwable -> L2b java.lang.Throwable -> L38
                r5 = 1
                r2.<init>(r4, r5)     // Catch: java.lang.Throwable -> L2b java.lang.Throwable -> L38
                java.lang.String r3 = "UTF-8"
                byte[] r3 = r9.getBytes(r3)     // Catch: java.lang.Throwable -> L44 java.lang.Throwable -> L49
                r2.write(r3)     // Catch: java.lang.Throwable -> L44 java.lang.Throwable -> L49
                r2.flush()     // Catch: java.lang.Throwable -> L44 java.lang.Throwable -> L49
                r2.close()     // Catch: java.lang.Throwable -> L44 java.lang.Throwable -> L49
                long r4 = r8.d     // Catch: java.lang.Throwable -> L44 java.lang.Throwable -> L49
                int r3 = r3.length     // Catch: java.lang.Throwable -> L44 java.lang.Throwable -> L49
                long r6 = (long) r3     // Catch: java.lang.Throwable -> L44 java.lang.Throwable -> L49
                long r4 = r4 + r6
                r8.d = r4     // Catch: java.lang.Throwable -> L44 java.lang.Throwable -> L49
                r2.close()     // Catch: java.io.IOException -> L40
            L29:
                r0 = r1
                goto L6
            L2b:
                r1 = move-exception
                r1 = r3
            L2d:
                r2 = 0
                r8.f211a = r2     // Catch: java.lang.Throwable -> L46
                if (r1 == 0) goto L6
                r1.close()     // Catch: java.io.IOException -> L36
                goto L6
            L36:
                r1 = move-exception
                goto L6
            L38:
                r0 = move-exception
                r2 = r3
            L3a:
                if (r2 == 0) goto L3f
                r2.close()     // Catch: java.io.IOException -> L42
            L3f:
                throw r0
            L40:
                r0 = move-exception
                goto L29
            L42:
                r1 = move-exception
                goto L3f
            L44:
                r0 = move-exception
                goto L3a
            L46:
                r0 = move-exception
                r2 = r1
                goto L3a
            L49:
                r1 = move-exception
                r1 = r2
                goto L2d
            */
            throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.y.a.a(java.lang.String):boolean");
        }
    }
}
