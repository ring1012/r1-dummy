package com.tencent.bugly.proguard;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class n {
    private Context c;
    private SharedPreferences f;
    private static n b = null;

    /* renamed from: a, reason: collision with root package name */
    public static final long f194a = System.currentTimeMillis();
    private Map<Integer, Map<String, m>> e = new HashMap();
    private String d = com.tencent.bugly.crashreport.common.info.a.b().d;

    private n(Context context) {
        this.c = context;
        this.f = context.getSharedPreferences("crashrecord", 0);
    }

    public static synchronized n a(Context context) {
        if (b == null) {
            b = new n(context);
        }
        return b;
    }

    public static synchronized n a() {
        return b;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean b(int i) {
        boolean z;
        try {
            List<m> listC = c(i);
            if (listC == null) {
                z = false;
            } else {
                long jCurrentTimeMillis = System.currentTimeMillis();
                ArrayList arrayList = new ArrayList();
                ArrayList arrayList2 = new ArrayList();
                for (m mVar : listC) {
                    if (mVar.b != null && mVar.b.equalsIgnoreCase(this.d) && mVar.d > 0) {
                        arrayList.add(mVar);
                    }
                    if (mVar.c + 86400000 < jCurrentTimeMillis) {
                        arrayList2.add(mVar);
                    }
                }
                Collections.sort(arrayList);
                if (arrayList.size() >= 2) {
                    if (arrayList.size() > 0 && ((m) arrayList.get(arrayList.size() - 1)).c + 86400000 < jCurrentTimeMillis) {
                        listC.clear();
                        a(i, (int) listC);
                        z = false;
                    } else {
                        z = true;
                    }
                } else {
                    listC.removeAll(arrayList2);
                    a(i, (int) listC);
                    z = false;
                }
            }
        } catch (Exception e) {
            x.e("isFrequentCrash failed", new Object[0]);
            z = false;
        }
        return z;
    }

    public final synchronized void a(int i, final int i2) {
        final int i3 = 1004;
        w.a().a(new Runnable() { // from class: com.tencent.bugly.proguard.n.1
            @Override // java.lang.Runnable
            public final void run() {
                m mVar;
                try {
                    if (!TextUtils.isEmpty(n.this.d)) {
                        List listC = n.this.c(i3);
                        List<m> arrayList = listC == null ? new ArrayList() : listC;
                        if (n.this.e.get(Integer.valueOf(i3)) == null) {
                            n.this.e.put(Integer.valueOf(i3), new HashMap());
                        }
                        if (((Map) n.this.e.get(Integer.valueOf(i3))).get(n.this.d) != null) {
                            m mVar2 = (m) ((Map) n.this.e.get(Integer.valueOf(i3))).get(n.this.d);
                            mVar2.d = i2;
                            mVar = mVar2;
                        } else {
                            m mVar3 = new m();
                            mVar3.f193a = i3;
                            mVar3.g = n.f194a;
                            mVar3.b = n.this.d;
                            mVar3.f = com.tencent.bugly.crashreport.common.info.a.b().j;
                            com.tencent.bugly.crashreport.common.info.a.b().getClass();
                            mVar3.e = "2.6.6";
                            mVar3.c = System.currentTimeMillis();
                            mVar3.d = i2;
                            ((Map) n.this.e.get(Integer.valueOf(i3))).put(n.this.d, mVar3);
                            mVar = mVar3;
                        }
                        ArrayList arrayList2 = new ArrayList();
                        boolean z = false;
                        for (m mVar4 : arrayList) {
                            if (mVar4.g == mVar.g && mVar4.b != null && mVar4.b.equalsIgnoreCase(mVar.b)) {
                                z = true;
                                mVar4.d = mVar.d;
                            }
                            if ((mVar4.e != null && !mVar4.e.equalsIgnoreCase(mVar.e)) || ((mVar4.f != null && !mVar4.f.equalsIgnoreCase(mVar.f)) || mVar4.d <= 0)) {
                                arrayList2.add(mVar4);
                            }
                        }
                        arrayList.removeAll(arrayList2);
                        if (!z) {
                            arrayList.add(mVar);
                        }
                        n.this.a(i3, (int) arrayList);
                    }
                } catch (Exception e) {
                    x.e("saveCrashRecord failed", new Object[0]);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:33:0x006c A[Catch: Exception -> 0x003a, all -> 0x0055, TryCatch #7 {Exception -> 0x003a, blocks: (B:4:0x0002, B:11:0x0036, B:33:0x006c, B:34:0x006f, B:29:0x0064, B:20:0x0051), top: B:46:0x0002, outer: #6 }] */
    /* JADX WARN: Type inference failed for: r2v10 */
    /* JADX WARN: Type inference failed for: r2v4, types: [boolean] */
    /* JADX WARN: Type inference failed for: r2v5, types: [java.io.ObjectInputStream] */
    /* JADX WARN: Type inference failed for: r2v6 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized <T extends java.util.List<?>> T c(int r7) {
        /*
            r6 = this;
            r1 = 0
            monitor-enter(r6)
            java.io.File r0 = new java.io.File     // Catch: java.lang.Exception -> L3a java.lang.Throwable -> L55
            android.content.Context r2 = r6.c     // Catch: java.lang.Exception -> L3a java.lang.Throwable -> L55
            java.lang.String r3 = "crashrecord"
            r4 = 0
            java.io.File r2 = r2.getDir(r3, r4)     // Catch: java.lang.Exception -> L3a java.lang.Throwable -> L55
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L3a java.lang.Throwable -> L55
            r3.<init>()     // Catch: java.lang.Exception -> L3a java.lang.Throwable -> L55
            java.lang.StringBuilder r3 = r3.append(r7)     // Catch: java.lang.Exception -> L3a java.lang.Throwable -> L55
            java.lang.String r3 = r3.toString()     // Catch: java.lang.Exception -> L3a java.lang.Throwable -> L55
            r0.<init>(r2, r3)     // Catch: java.lang.Exception -> L3a java.lang.Throwable -> L55
            boolean r2 = r0.exists()     // Catch: java.lang.Exception -> L3a java.lang.Throwable -> L55
            if (r2 != 0) goto L26
            r0 = r1
        L24:
            monitor-exit(r6)
            return r0
        L26:
            java.io.ObjectInputStream r2 = new java.io.ObjectInputStream     // Catch: java.io.IOException -> L45 java.lang.ClassNotFoundException -> L58 java.lang.Throwable -> L68
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch: java.io.IOException -> L45 java.lang.ClassNotFoundException -> L58 java.lang.Throwable -> L68
            r3.<init>(r0)     // Catch: java.io.IOException -> L45 java.lang.ClassNotFoundException -> L58 java.lang.Throwable -> L68
            r2.<init>(r3)     // Catch: java.io.IOException -> L45 java.lang.ClassNotFoundException -> L58 java.lang.Throwable -> L68
            java.lang.Object r0 = r2.readObject()     // Catch: java.lang.Throwable -> L70 java.lang.ClassNotFoundException -> L77 java.io.IOException -> L79
            java.util.List r0 = (java.util.List) r0     // Catch: java.lang.Throwable -> L70 java.lang.ClassNotFoundException -> L77 java.io.IOException -> L79
            r2.close()     // Catch: java.lang.Exception -> L3a java.lang.Throwable -> L55
            goto L24
        L3a:
            r0 = move-exception
            java.lang.String r0 = "readCrashRecord error"
            r2 = 0
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch: java.lang.Throwable -> L55
            com.tencent.bugly.proguard.x.e(r0, r2)     // Catch: java.lang.Throwable -> L55
        L43:
            r0 = r1
            goto L24
        L45:
            r0 = move-exception
            r0 = r1
        L47:
            java.lang.String r2 = "open record file error"
            r3 = 0
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch: java.lang.Throwable -> L72
            com.tencent.bugly.proguard.x.a(r2, r3)     // Catch: java.lang.Throwable -> L72
            if (r0 == 0) goto L43
            r0.close()     // Catch: java.lang.Exception -> L3a java.lang.Throwable -> L55
            goto L43
        L55:
            r0 = move-exception
            monitor-exit(r6)
            throw r0
        L58:
            r0 = move-exception
            r2 = r1
        L5a:
            java.lang.String r0 = "get object error"
            r3 = 0
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch: java.lang.Throwable -> L70
            com.tencent.bugly.proguard.x.a(r0, r3)     // Catch: java.lang.Throwable -> L70
            if (r2 == 0) goto L43
            r2.close()     // Catch: java.lang.Exception -> L3a java.lang.Throwable -> L55
            goto L43
        L68:
            r0 = move-exception
            r2 = r1
        L6a:
            if (r2 == 0) goto L6f
            r2.close()     // Catch: java.lang.Exception -> L3a java.lang.Throwable -> L55
        L6f:
            throw r0     // Catch: java.lang.Exception -> L3a java.lang.Throwable -> L55
        L70:
            r0 = move-exception
            goto L6a
        L72:
            r2 = move-exception
            r5 = r2
            r2 = r0
            r0 = r5
            goto L6a
        L77:
            r0 = move-exception
            goto L5a
        L79:
            r0 = move-exception
            r0 = r2
            goto L47
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.n.c(int):java.util.List");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0056 A[Catch: Exception -> 0x0032, all -> 0x003c, TryCatch #4 {Exception -> 0x0032, blocks: (B:6:0x0005, B:10:0x002e, B:26:0x0056, B:27:0x0059, B:22:0x004e), top: B:36:0x0005, outer: #3 }] */
    /* JADX WARN: Type inference failed for: r1v11 */
    /* JADX WARN: Type inference failed for: r1v12 */
    /* JADX WARN: Type inference failed for: r1v13 */
    /* JADX WARN: Type inference failed for: r1v9 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized <T extends java.util.List<?>> void a(int r5, T r6) {
        /*
            r4 = this;
            monitor-enter(r4)
            if (r6 != 0) goto L5
        L3:
            monitor-exit(r4)
            return
        L5:
            java.io.File r0 = new java.io.File     // Catch: java.lang.Exception -> L32 java.lang.Throwable -> L3c
            android.content.Context r1 = r4.c     // Catch: java.lang.Exception -> L32 java.lang.Throwable -> L3c
            java.lang.String r2 = "crashrecord"
            r3 = 0
            java.io.File r1 = r1.getDir(r2, r3)     // Catch: java.lang.Exception -> L32 java.lang.Throwable -> L3c
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L32 java.lang.Throwable -> L3c
            r2.<init>()     // Catch: java.lang.Exception -> L32 java.lang.Throwable -> L3c
            java.lang.StringBuilder r2 = r2.append(r5)     // Catch: java.lang.Exception -> L32 java.lang.Throwable -> L3c
            java.lang.String r2 = r2.toString()     // Catch: java.lang.Exception -> L32 java.lang.Throwable -> L3c
            r0.<init>(r1, r2)     // Catch: java.lang.Exception -> L32 java.lang.Throwable -> L3c
            r2 = 0
            java.io.ObjectOutputStream r1 = new java.io.ObjectOutputStream     // Catch: java.io.IOException -> L3f java.lang.Throwable -> L52
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch: java.io.IOException -> L3f java.lang.Throwable -> L52
            r3.<init>(r0)     // Catch: java.io.IOException -> L3f java.lang.Throwable -> L52
            r1.<init>(r3)     // Catch: java.io.IOException -> L3f java.lang.Throwable -> L52
            r1.writeObject(r6)     // Catch: java.lang.Throwable -> L5a java.io.IOException -> L5c
            r1.close()     // Catch: java.lang.Exception -> L32 java.lang.Throwable -> L3c
            goto L3
        L32:
            r0 = move-exception
            java.lang.String r0 = "writeCrashRecord error"
            r1 = 0
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch: java.lang.Throwable -> L3c
            com.tencent.bugly.proguard.x.e(r0, r1)     // Catch: java.lang.Throwable -> L3c
            goto L3
        L3c:
            r0 = move-exception
            monitor-exit(r4)
            throw r0
        L3f:
            r0 = move-exception
            r1 = r2
        L41:
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L5a
            java.lang.String r0 = "open record file error"
            r2 = 0
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch: java.lang.Throwable -> L5a
            com.tencent.bugly.proguard.x.a(r0, r2)     // Catch: java.lang.Throwable -> L5a
            if (r1 == 0) goto L3
            r1.close()     // Catch: java.lang.Exception -> L32 java.lang.Throwable -> L3c
            goto L3
        L52:
            r0 = move-exception
            r1 = r2
        L54:
            if (r1 == 0) goto L59
            r1.close()     // Catch: java.lang.Exception -> L32 java.lang.Throwable -> L3c
        L59:
            throw r0     // Catch: java.lang.Exception -> L32 java.lang.Throwable -> L3c
        L5a:
            r0 = move-exception
            goto L54
        L5c:
            r0 = move-exception
            goto L41
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.n.a(int, java.util.List):void");
    }

    public final synchronized boolean a(final int i) {
        boolean z = true;
        synchronized (this) {
            try {
                z = this.f.getBoolean(i + "_" + this.d, true);
                w.a().a(new Runnable() { // from class: com.tencent.bugly.proguard.n.2
                    @Override // java.lang.Runnable
                    public final void run() {
                        n.this.f.edit().putBoolean(i + "_" + n.this.d, !n.this.b(i)).commit();
                    }
                });
            } catch (Exception e) {
                x.e("canInit error", new Object[0]);
            }
        }
        return z;
    }
}
