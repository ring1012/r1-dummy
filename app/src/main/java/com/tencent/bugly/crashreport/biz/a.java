package com.tencent.bugly.crashreport.biz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.proguard.am;
import com.tencent.bugly.proguard.ar;
import com.tencent.bugly.proguard.k;
import com.tencent.bugly.proguard.o;
import com.tencent.bugly.proguard.p;
import com.tencent.bugly.proguard.t;
import com.tencent.bugly.proguard.u;
import com.tencent.bugly.proguard.w;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.z;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class a {

    /* renamed from: a, reason: collision with root package name */
    private Context f131a;
    private long b;
    private int c;
    private boolean d;

    static /* synthetic */ void a(a aVar, UserInfoBean userInfoBean, boolean z) {
        List<UserInfoBean> listA;
        if (userInfoBean != null) {
            if (!z && userInfoBean.b != 1 && (listA = aVar.a(com.tencent.bugly.crashreport.common.info.a.a(aVar.f131a).d)) != null && listA.size() >= 20) {
                x.a("[UserInfo] There are too many user info in local: %d", Integer.valueOf(listA.size()));
                return;
            }
            long jA = p.a().a("t_ui", a(userInfoBean), (o) null, true);
            if (jA >= 0) {
                x.c("[Database] insert %s success with ID: %d", "t_ui", Long.valueOf(jA));
                userInfoBean.f130a = jA;
            }
        }
    }

    public a(Context context, boolean z) {
        this.d = true;
        this.f131a = context;
        this.d = z;
    }

    public final void a(int i, boolean z, long j) {
        com.tencent.bugly.crashreport.common.strategy.a aVarA = com.tencent.bugly.crashreport.common.strategy.a.a();
        if (aVarA != null && !aVarA.c().h && i != 1 && i != 3) {
            x.e("UserInfo is disable", new Object[0]);
            return;
        }
        if (i == 1 || i == 3) {
            this.c++;
        }
        com.tencent.bugly.crashreport.common.info.a aVarA2 = com.tencent.bugly.crashreport.common.info.a.a(this.f131a);
        UserInfoBean userInfoBean = new UserInfoBean();
        userInfoBean.b = i;
        userInfoBean.c = aVarA2.d;
        userInfoBean.d = aVarA2.g();
        userInfoBean.e = System.currentTimeMillis();
        userInfoBean.f = -1L;
        userInfoBean.n = aVarA2.j;
        userInfoBean.o = i != 1 ? 0 : 1;
        userInfoBean.l = aVarA2.a();
        userInfoBean.m = aVarA2.p;
        userInfoBean.g = aVarA2.q;
        userInfoBean.h = aVarA2.r;
        userInfoBean.i = aVarA2.s;
        userInfoBean.k = aVarA2.t;
        userInfoBean.r = aVarA2.B();
        userInfoBean.s = aVarA2.G();
        userInfoBean.p = aVarA2.H();
        userInfoBean.q = aVarA2.I();
        w.a().a(new RunnableC0009a(userInfoBean, z), 0L);
    }

    public final void a() {
        this.b = z.b() + 86400000;
        w.a().a(new b(), (this.b - System.currentTimeMillis()) + DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS);
    }

    /* compiled from: BUGLY */
    /* renamed from: com.tencent.bugly.crashreport.biz.a$a, reason: collision with other inner class name */
    class RunnableC0009a implements Runnable {

        /* renamed from: a, reason: collision with root package name */
        private boolean f134a;
        private UserInfoBean b;

        public RunnableC0009a(UserInfoBean userInfoBean, boolean z) {
            this.b = userInfoBean;
            this.f134a = z;
        }

        @Override // java.lang.Runnable
        public final void run() {
            com.tencent.bugly.crashreport.common.info.a aVarB;
            try {
                if (this.b != null) {
                    UserInfoBean userInfoBean = this.b;
                    if (userInfoBean != null && (aVarB = com.tencent.bugly.crashreport.common.info.a.b()) != null) {
                        userInfoBean.j = aVarB.e();
                    }
                    x.c("[UserInfo] Record user info.", new Object[0]);
                    a.a(a.this, this.b, false);
                }
                if (this.f134a) {
                    a aVar = a.this;
                    w wVarA = w.a();
                    if (wVarA != null) {
                        wVarA.a(aVar.new AnonymousClass2());
                    }
                }
            } catch (Throwable th) {
                if (!x.a(th)) {
                    th.printStackTrace();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void c() {
        u uVarA;
        com.tencent.bugly.crashreport.common.strategy.a aVarA;
        final List<UserInfoBean> arrayList;
        boolean z;
        synchronized (this) {
            if (this.d && (uVarA = u.a()) != null && (aVarA = com.tencent.bugly.crashreport.common.strategy.a.a()) != null && (!aVarA.b() || uVarA.b(1001))) {
                String str = com.tencent.bugly.crashreport.common.info.a.a(this.f131a).d;
                ArrayList arrayList2 = new ArrayList();
                List<UserInfoBean> listA = a(str);
                if (listA != null) {
                    int size = listA.size() - 20;
                    if (size > 0) {
                        for (int i = 0; i < listA.size() - 1; i++) {
                            for (int i2 = i + 1; i2 < listA.size(); i2++) {
                                if (listA.get(i).e > listA.get(i2).e) {
                                    UserInfoBean userInfoBean = listA.get(i);
                                    listA.set(i, listA.get(i2));
                                    listA.set(i2, userInfoBean);
                                }
                            }
                        }
                        for (int i3 = 0; i3 < size; i3++) {
                            arrayList2.add(listA.get(i3));
                        }
                    }
                    Iterator<UserInfoBean> it = listA.iterator();
                    int i4 = 0;
                    while (it.hasNext()) {
                        UserInfoBean next = it.next();
                        if (next.f != -1) {
                            it.remove();
                            if (next.e < z.b()) {
                                arrayList2.add(next);
                            }
                        }
                        i4 = (next.e <= System.currentTimeMillis() - 600000 || !(next.b == 1 || next.b == 4 || next.b == 3)) ? i4 : i4 + 1;
                    }
                    if (i4 > 15) {
                        x.d("[UserInfo] Upload user info too many times in 10 min: %d", Integer.valueOf(i4));
                        z = false;
                    } else {
                        z = true;
                    }
                    arrayList = listA;
                } else {
                    arrayList = new ArrayList();
                    z = true;
                }
                if (arrayList2.size() > 0) {
                    a(arrayList2);
                }
                if (!z || arrayList.size() == 0) {
                    x.c("[UserInfo] There is no user info in local database.", new Object[0]);
                } else {
                    x.c("[UserInfo] Upload user info(size: %d)", Integer.valueOf(arrayList.size()));
                    ar arVarA = com.tencent.bugly.proguard.a.a(arrayList, this.c == 1 ? 1 : 2);
                    if (arVarA == null) {
                        x.d("[UserInfo] Failed to create UserInfoPackage.", new Object[0]);
                    } else {
                        byte[] bArrA = com.tencent.bugly.proguard.a.a((k) arVarA);
                        if (bArrA == null) {
                            x.d("[UserInfo] Failed to encode data.", new Object[0]);
                        } else {
                            am amVarA = com.tencent.bugly.proguard.a.a(this.f131a, uVarA.f202a ? 840 : 640, bArrA);
                            if (amVarA == null) {
                                x.d("[UserInfo] Request package is null.", new Object[0]);
                            } else {
                                t tVar = new t() { // from class: com.tencent.bugly.crashreport.biz.a.1
                                    @Override // com.tencent.bugly.proguard.t
                                    public final void a(boolean z2) {
                                        if (z2) {
                                            x.c("[UserInfo] Successfully uploaded user info.", new Object[0]);
                                            long jCurrentTimeMillis = System.currentTimeMillis();
                                            for (UserInfoBean userInfoBean2 : arrayList) {
                                                userInfoBean2.f = jCurrentTimeMillis;
                                                a.a(a.this, userInfoBean2, true);
                                            }
                                        }
                                    }
                                };
                                StrategyBean strategyBeanC = com.tencent.bugly.crashreport.common.strategy.a.a().c();
                                u.a().a(1001, amVarA, uVarA.f202a ? strategyBeanC.r : strategyBeanC.t, uVarA.f202a ? StrategyBean.b : StrategyBean.f143a, tVar, this.c == 1);
                            }
                        }
                    }
                }
            }
        }
    }

    public final void b() {
        w wVarA = w.a();
        if (wVarA != null) {
            wVarA.a(new AnonymousClass2());
        }
    }

    /* compiled from: BUGLY */
    /* renamed from: com.tencent.bugly.crashreport.biz.a$2, reason: invalid class name */
    final class AnonymousClass2 implements Runnable {
        AnonymousClass2() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            try {
                a.this.c();
            } catch (Throwable th) {
                x.a(th);
            }
        }
    }

    /* compiled from: BUGLY */
    class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            long jCurrentTimeMillis = System.currentTimeMillis();
            if (jCurrentTimeMillis < a.this.b) {
                w.a().a(a.this.new b(), (a.this.b - jCurrentTimeMillis) + DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS);
            } else {
                a.this.a(3, false, 0L);
                a.this.a();
            }
        }
    }

    /* compiled from: BUGLY */
    class c implements Runnable {

        /* renamed from: a, reason: collision with root package name */
        private long f136a;

        public c(long j) {
            this.f136a = 21600000L;
            this.f136a = j;
        }

        @Override // java.lang.Runnable
        public final void run() {
            a aVar = a.this;
            w wVarA = w.a();
            if (wVarA != null) {
                wVarA.a(aVar.new AnonymousClass2());
            }
            a aVar2 = a.this;
            long j = this.f136a;
            w.a().a(aVar2.new c(j), j);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x0088  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final java.util.List<com.tencent.bugly.crashreport.biz.UserInfoBean> a(java.lang.String r10) {
        /*
            r9 = this;
            r7 = 0
            boolean r0 = com.tencent.bugly.proguard.z.a(r10)     // Catch: java.lang.Throwable -> Lc4 java.lang.Throwable -> Lca
            if (r0 == 0) goto L1f
            r3 = r7
        L8:
            com.tencent.bugly.proguard.p r0 = com.tencent.bugly.proguard.p.a()     // Catch: java.lang.Throwable -> Lc4 java.lang.Throwable -> Lca
            java.lang.String r1 = "t_ui"
            r2 = 0
            r4 = 0
            r5 = 0
            r6 = 1
            android.database.Cursor r8 = r0.a(r1, r2, r3, r4, r5, r6)     // Catch: java.lang.Throwable -> Lc4 java.lang.Throwable -> Lca
            if (r8 != 0) goto L35
            if (r8 == 0) goto L1d
            r8.close()
        L1d:
            r0 = r7
        L1e:
            return r0
        L1f:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lc4 java.lang.Throwable -> Lca
            java.lang.String r1 = "_pc = '"
            r0.<init>(r1)     // Catch: java.lang.Throwable -> Lc4 java.lang.Throwable -> Lca
            java.lang.StringBuilder r0 = r0.append(r10)     // Catch: java.lang.Throwable -> Lc4 java.lang.Throwable -> Lca
            java.lang.String r1 = "'"
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch: java.lang.Throwable -> Lc4 java.lang.Throwable -> Lca
            java.lang.String r3 = r0.toString()     // Catch: java.lang.Throwable -> Lc4 java.lang.Throwable -> Lca
            goto L8
        L35:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L85
            r0.<init>()     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L85
            java.util.ArrayList r6 = new java.util.ArrayList     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L85
            r6.<init>()     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L85
        L3f:
            boolean r1 = r8.moveToNext()     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L85
            if (r1 == 0) goto L8c
            com.tencent.bugly.crashreport.biz.UserInfoBean r1 = a(r8)     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L85
            if (r1 == 0) goto L61
            r6.add(r1)     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L85
            goto L3f
        L4f:
            r0 = move-exception
            r1 = r8
        L51:
            boolean r2 = com.tencent.bugly.proguard.x.a(r0)     // Catch: java.lang.Throwable -> Lc7
            if (r2 != 0) goto L5a
            r0.printStackTrace()     // Catch: java.lang.Throwable -> Lc7
        L5a:
            if (r1 == 0) goto L5f
            r1.close()
        L5f:
            r0 = r7
            goto L1e
        L61:
            java.lang.String r1 = "_id"
            int r1 = r8.getColumnIndex(r1)     // Catch: java.lang.Throwable -> L7b java.lang.Throwable -> L85
            long r2 = r8.getLong(r1)     // Catch: java.lang.Throwable -> L7b java.lang.Throwable -> L85
            java.lang.String r1 = " or _id"
            java.lang.StringBuilder r1 = r0.append(r1)     // Catch: java.lang.Throwable -> L7b java.lang.Throwable -> L85
            java.lang.String r4 = " = "
            java.lang.StringBuilder r1 = r1.append(r4)     // Catch: java.lang.Throwable -> L7b java.lang.Throwable -> L85
            r1.append(r2)     // Catch: java.lang.Throwable -> L7b java.lang.Throwable -> L85
            goto L3f
        L7b:
            r1 = move-exception
            java.lang.String r1 = "[Database] unknown id."
            r2 = 0
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L85
            com.tencent.bugly.proguard.x.d(r1, r2)     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L85
            goto L3f
        L85:
            r0 = move-exception
        L86:
            if (r8 == 0) goto L8b
            r8.close()
        L8b:
            throw r0
        L8c:
            java.lang.String r0 = r0.toString()     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L85
            int r1 = r0.length()     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L85
            if (r1 <= 0) goto Lbc
            r1 = 4
            java.lang.String r2 = r0.substring(r1)     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L85
            com.tencent.bugly.proguard.p r0 = com.tencent.bugly.proguard.p.a()     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L85
            java.lang.String r1 = "t_ui"
            r3 = 0
            r4 = 0
            r5 = 1
            int r0 = r0.a(r1, r2, r3, r4, r5)     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L85
            java.lang.String r1 = "[Database] deleted %s error data %d"
            r2 = 2
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L85
            r3 = 0
            java.lang.String r4 = "t_ui"
            r2[r3] = r4     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L85
            r3 = 1
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L85
            r2[r3] = r0     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L85
            com.tencent.bugly.proguard.x.d(r1, r2)     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L85
        Lbc:
            if (r8 == 0) goto Lc1
            r8.close()
        Lc1:
            r0 = r6
            goto L1e
        Lc4:
            r0 = move-exception
            r8 = r7
            goto L86
        Lc7:
            r0 = move-exception
            r8 = r1
            goto L86
        Lca:
            r0 = move-exception
            r1 = r7
            goto L51
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.biz.a.a(java.lang.String):java.util.List");
    }

    private static void a(List<UserInfoBean> list) {
        if (list != null && list.size() != 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < list.size() && i < 50; i++) {
                sb.append(" or _id").append(" = ").append(list.get(i).f130a);
            }
            String string = sb.toString();
            if (string.length() > 0) {
                string = string.substring(4);
            }
            sb.setLength(0);
            try {
                x.c("[Database] deleted %s data %d", "t_ui", Integer.valueOf(p.a().a("t_ui", string, (String[]) null, (o) null, true)));
            } catch (Throwable th) {
                if (!x.a(th)) {
                    th.printStackTrace();
                }
            }
        }
    }

    private static ContentValues a(UserInfoBean userInfoBean) {
        if (userInfoBean == null) {
            return null;
        }
        try {
            ContentValues contentValues = new ContentValues();
            if (userInfoBean.f130a > 0) {
                contentValues.put("_id", Long.valueOf(userInfoBean.f130a));
            }
            contentValues.put("_tm", Long.valueOf(userInfoBean.e));
            contentValues.put("_ut", Long.valueOf(userInfoBean.f));
            contentValues.put("_tp", Integer.valueOf(userInfoBean.b));
            contentValues.put("_pc", userInfoBean.c);
            contentValues.put("_dt", z.a(userInfoBean));
            return contentValues;
        } catch (Throwable th) {
            if (x.a(th)) {
                return null;
            }
            th.printStackTrace();
            return null;
        }
    }

    private static UserInfoBean a(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        try {
            byte[] blob = cursor.getBlob(cursor.getColumnIndex("_dt"));
            if (blob == null) {
                return null;
            }
            long j = cursor.getLong(cursor.getColumnIndex("_id"));
            UserInfoBean userInfoBean = (UserInfoBean) z.a(blob, UserInfoBean.CREATOR);
            if (userInfoBean != null) {
                userInfoBean.f130a = j;
                return userInfoBean;
            }
            return userInfoBean;
        } catch (Throwable th) {
            if (!x.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }
}
