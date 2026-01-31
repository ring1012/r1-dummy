package com.tencent.bugly.proguard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class p {

    /* renamed from: a, reason: collision with root package name */
    private static p f197a = null;
    private static q b = null;
    private static boolean c = false;

    private p(Context context, List<com.tencent.bugly.a> list) {
        b = new q(context, list);
    }

    public static synchronized p a(Context context, List<com.tencent.bugly.a> list) {
        if (f197a == null) {
            f197a = new p(context, list);
        }
        return f197a;
    }

    public static synchronized p a() {
        return f197a;
    }

    public final long a(String str, ContentValues contentValues, o oVar, boolean z) {
        return a(str, contentValues, (o) null);
    }

    public final Cursor a(String str, String[] strArr, String str2, String[] strArr2, o oVar, boolean z) {
        return a(false, str, strArr, str2, null, null, null, null, null, null);
    }

    public final int a(String str, String str2, String[] strArr, o oVar, boolean z) {
        return a(str, str2, (String[]) null, (o) null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized long a(String str, ContentValues contentValues, o oVar) {
        long j = 0;
        synchronized (this) {
            try {
                try {
                    SQLiteDatabase writableDatabase = b.getWritableDatabase();
                    if (writableDatabase != null && contentValues != null) {
                        long jReplace = writableDatabase.replace(str, "_id", contentValues);
                        if (jReplace >= 0) {
                            x.c("[Database] insert %s success.", str);
                        } else {
                            x.d("[Database] replace %s error.", str);
                        }
                        j = jReplace;
                    }
                    if (oVar != null) {
                        Long.valueOf(j);
                    }
                } catch (Throwable th) {
                    if (!x.a(th)) {
                        th.printStackTrace();
                    }
                    if (oVar != null) {
                        Long.valueOf(0L);
                    }
                }
            } catch (Throwable th2) {
                if (oVar != null) {
                    Long.valueOf(0L);
                }
                throw th2;
            }
        }
        return j;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized Cursor a(boolean z, String str, String[] strArr, String str2, String[] strArr2, String str3, String str4, String str5, String str6, o oVar) {
        Cursor cursorQuery;
        try {
            try {
                SQLiteDatabase writableDatabase = b.getWritableDatabase();
                cursorQuery = writableDatabase != null ? writableDatabase.query(z, str, strArr, str2, strArr2, str3, str4, str5, str6) : null;
                if (oVar != null) {
                }
            } catch (Throwable th) {
                if (!x.a(th)) {
                    th.printStackTrace();
                }
                cursorQuery = oVar != null ? null : null;
            }
        } finally {
        }
        return cursorQuery;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized int a(String str, String str2, String[] strArr, o oVar) {
        synchronized (this) {
            try {
                try {
                    SQLiteDatabase writableDatabase = b.getWritableDatabase();
                    iDelete = writableDatabase != null ? writableDatabase.delete(str, str2, strArr) : 0;
                    if (oVar != null) {
                        Integer.valueOf(iDelete);
                    }
                } catch (Throwable th) {
                    if (!x.a(th)) {
                        th.printStackTrace();
                    }
                    if (oVar != null) {
                        Integer.valueOf(0);
                    }
                }
            } catch (Throwable th2) {
                if (oVar != null) {
                    Integer.valueOf(0);
                }
                throw th2;
            }
        }
        return iDelete;
    }

    public final boolean a(int i, String str, byte[] bArr, o oVar, boolean z) {
        if (z) {
            return a(i, str, bArr, (o) null);
        }
        a aVar = new a(4, null);
        aVar.a(i, str, bArr);
        w.a().a(aVar);
        return true;
    }

    public final Map<String, byte[]> a(int i, o oVar, boolean z) {
        return a(i, (o) null);
    }

    public final boolean a(int i, String str, o oVar, boolean z) {
        return a(555, str, (o) null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean a(int i, String str, byte[] bArr, o oVar) {
        boolean zB = false;
        try {
            try {
                r rVar = new r();
                rVar.f200a = i;
                rVar.f = str;
                rVar.e = System.currentTimeMillis();
                rVar.g = bArr;
                zB = b(rVar);
            } catch (Throwable th) {
                if (!x.a(th)) {
                    th.printStackTrace();
                }
                if (oVar != null) {
                    Boolean.valueOf(false);
                }
            }
            return zB;
        } finally {
            if (oVar != null) {
                Boolean.valueOf(false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Map<String, byte[]> a(int i, o oVar) {
        Throwable th;
        HashMap map;
        try {
            List<r> listC = c(i);
            if (listC == null) {
                map = null;
            } else {
                HashMap map2 = new HashMap();
                try {
                    for (r rVar : listC) {
                        byte[] bArr = rVar.g;
                        if (bArr != null) {
                            map2.put(rVar.f, bArr);
                        }
                    }
                    map = map2;
                } catch (Throwable th2) {
                    map = map2;
                    th = th2;
                    if (!x.a(th)) {
                        th.printStackTrace();
                    }
                    if (oVar != null) {
                    }
                    return map;
                }
            }
            if (oVar != null) {
            }
        } catch (Throwable th3) {
            th = th3;
            map = null;
        }
        return map;
    }

    public final synchronized boolean a(r rVar) {
        ContentValues contentValuesC;
        boolean z = false;
        synchronized (this) {
            if (rVar != null) {
                try {
                    try {
                        SQLiteDatabase writableDatabase = b.getWritableDatabase();
                        if (writableDatabase != null && (contentValuesC = c(rVar)) != null) {
                            long jReplace = writableDatabase.replace("t_lr", "_id", contentValuesC);
                            if (jReplace >= 0) {
                                x.c("[Database] insert %s success.", "t_lr");
                                rVar.f200a = jReplace;
                                z = true;
                            }
                        }
                    } catch (Throwable th) {
                        if (!x.a(th)) {
                            th.printStackTrace();
                        }
                    }
                } finally {
                }
            }
        }
        return z;
    }

    private synchronized boolean b(r rVar) {
        ContentValues contentValuesD;
        boolean z = false;
        synchronized (this) {
            if (rVar != null) {
                try {
                    try {
                        SQLiteDatabase writableDatabase = b.getWritableDatabase();
                        if (writableDatabase != null && (contentValuesD = d(rVar)) != null) {
                            long jReplace = writableDatabase.replace("t_pf", "_id", contentValuesD);
                            if (jReplace >= 0) {
                                x.c("[Database] insert %s success.", "t_pf");
                                rVar.f200a = jReplace;
                                z = true;
                            }
                        }
                    } catch (Throwable th) {
                        if (!x.a(th)) {
                            th.printStackTrace();
                        }
                    }
                } finally {
                }
            }
        }
        return z;
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x0054 A[Catch: all -> 0x00c1, TRY_LEAVE, TryCatch #0 {all -> 0x00c1, blocks: (B:25:0x004e, B:27:0x0054), top: B:55:0x004e }] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0059 A[Catch: all -> 0x0089, TRY_ENTER, TRY_LEAVE, TryCatch #3 {, blocks: (B:4:0x0002, B:11:0x002a, B:47:0x00b8, B:29:0x0059, B:38:0x0085, B:39:0x0088), top: B:59:0x0002 }] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0085 A[Catch: all -> 0x0089, TRY_ENTER, TryCatch #3 {, blocks: (B:4:0x0002, B:11:0x002a, B:47:0x00b8, B:29:0x0059, B:38:0x0085, B:39:0x0088), top: B:59:0x0002 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final synchronized java.util.List<com.tencent.bugly.proguard.r> a(int r10) {
        /*
            r9 = this;
            r8 = 0
            monitor-enter(r9)
            com.tencent.bugly.proguard.q r0 = com.tencent.bugly.proguard.p.b     // Catch: java.lang.Throwable -> L89
            android.database.sqlite.SQLiteDatabase r0 = r0.getWritableDatabase()     // Catch: java.lang.Throwable -> L89
            if (r0 == 0) goto L5c
            if (r10 < 0) goto L30
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lbe java.lang.Throwable -> Lc4
            java.lang.String r2 = "_tp = "
            r1.<init>(r2)     // Catch: java.lang.Throwable -> Lbe java.lang.Throwable -> Lc4
            java.lang.StringBuilder r1 = r1.append(r10)     // Catch: java.lang.Throwable -> Lbe java.lang.Throwable -> Lc4
            java.lang.String r3 = r1.toString()     // Catch: java.lang.Throwable -> Lbe java.lang.Throwable -> Lc4
        L1b:
            java.lang.String r1 = "t_lr"
            r2 = 0
            r4 = 0
            r5 = 0
            r6 = 0
            r7 = 0
            android.database.Cursor r2 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch: java.lang.Throwable -> Lbe java.lang.Throwable -> Lc4
            if (r2 != 0) goto L32
            if (r2 == 0) goto L2d
            r2.close()     // Catch: java.lang.Throwable -> L89
        L2d:
            r0 = r8
        L2e:
            monitor-exit(r9)
            return r0
        L30:
            r3 = r8
            goto L1b
        L32:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L4c java.lang.Throwable -> L82
            r3.<init>()     // Catch: java.lang.Throwable -> L4c java.lang.Throwable -> L82
            java.util.ArrayList r1 = new java.util.ArrayList     // Catch: java.lang.Throwable -> L4c java.lang.Throwable -> L82
            r1.<init>()     // Catch: java.lang.Throwable -> L4c java.lang.Throwable -> L82
        L3c:
            boolean r4 = r2.moveToNext()     // Catch: java.lang.Throwable -> L4c java.lang.Throwable -> L82
            if (r4 == 0) goto L8c
            com.tencent.bugly.proguard.r r4 = a(r2)     // Catch: java.lang.Throwable -> L4c java.lang.Throwable -> L82
            if (r4 == 0) goto L5e
            r1.add(r4)     // Catch: java.lang.Throwable -> L4c java.lang.Throwable -> L82
            goto L3c
        L4c:
            r0 = move-exception
            r1 = r2
        L4e:
            boolean r2 = com.tencent.bugly.proguard.x.a(r0)     // Catch: java.lang.Throwable -> Lc1
            if (r2 != 0) goto L57
            r0.printStackTrace()     // Catch: java.lang.Throwable -> Lc1
        L57:
            if (r1 == 0) goto L5c
            r1.close()     // Catch: java.lang.Throwable -> L89
        L5c:
            r0 = r8
            goto L2e
        L5e:
            java.lang.String r4 = "_id"
            int r4 = r2.getColumnIndex(r4)     // Catch: java.lang.Throwable -> L78 java.lang.Throwable -> L82
            long r4 = r2.getLong(r4)     // Catch: java.lang.Throwable -> L78 java.lang.Throwable -> L82
            java.lang.String r6 = " or _id"
            java.lang.StringBuilder r6 = r3.append(r6)     // Catch: java.lang.Throwable -> L78 java.lang.Throwable -> L82
            java.lang.String r7 = " = "
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch: java.lang.Throwable -> L78 java.lang.Throwable -> L82
            r6.append(r4)     // Catch: java.lang.Throwable -> L78 java.lang.Throwable -> L82
            goto L3c
        L78:
            r4 = move-exception
            java.lang.String r4 = "[Database] unknown id."
            r5 = 0
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch: java.lang.Throwable -> L4c java.lang.Throwable -> L82
            com.tencent.bugly.proguard.x.d(r4, r5)     // Catch: java.lang.Throwable -> L4c java.lang.Throwable -> L82
            goto L3c
        L82:
            r0 = move-exception
        L83:
            if (r2 == 0) goto L88
            r2.close()     // Catch: java.lang.Throwable -> L89
        L88:
            throw r0     // Catch: java.lang.Throwable -> L89
        L89:
            r0 = move-exception
            monitor-exit(r9)
            throw r0
        L8c:
            java.lang.String r3 = r3.toString()     // Catch: java.lang.Throwable -> L4c java.lang.Throwable -> L82
            int r4 = r3.length()     // Catch: java.lang.Throwable -> L4c java.lang.Throwable -> L82
            if (r4 <= 0) goto Lb6
            r4 = 4
            java.lang.String r3 = r3.substring(r4)     // Catch: java.lang.Throwable -> L4c java.lang.Throwable -> L82
            java.lang.String r4 = "t_lr"
            r5 = 0
            int r0 = r0.delete(r4, r3, r5)     // Catch: java.lang.Throwable -> L4c java.lang.Throwable -> L82
            java.lang.String r3 = "[Database] deleted %s illegal data %d"
            r4 = 2
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch: java.lang.Throwable -> L4c java.lang.Throwable -> L82
            r5 = 0
            java.lang.String r6 = "t_lr"
            r4[r5] = r6     // Catch: java.lang.Throwable -> L4c java.lang.Throwable -> L82
            r5 = 1
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch: java.lang.Throwable -> L4c java.lang.Throwable -> L82
            r4[r5] = r0     // Catch: java.lang.Throwable -> L4c java.lang.Throwable -> L82
            com.tencent.bugly.proguard.x.d(r3, r4)     // Catch: java.lang.Throwable -> L4c java.lang.Throwable -> L82
        Lb6:
            if (r2 == 0) goto Lbb
            r2.close()     // Catch: java.lang.Throwable -> L89
        Lbb:
            r0 = r1
            goto L2e
        Lbe:
            r0 = move-exception
            r2 = r8
            goto L83
        Lc1:
            r0 = move-exception
            r2 = r1
            goto L83
        Lc4:
            r0 = move-exception
            r1 = r8
            goto L4e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.p.a(int):java.util.List");
    }

    public final synchronized void a(List<r> list) {
        SQLiteDatabase writableDatabase;
        if (list != null) {
            if (list.size() != 0 && (writableDatabase = b.getWritableDatabase()) != null) {
                StringBuilder sb = new StringBuilder();
                Iterator<r> it = list.iterator();
                while (it.hasNext()) {
                    sb.append(" or _id").append(" = ").append(it.next().f200a);
                }
                String string = sb.toString();
                if (string.length() > 0) {
                    string = string.substring(4);
                }
                sb.setLength(0);
                try {
                    x.c("[Database] deleted %s data %d", "t_lr", Integer.valueOf(writableDatabase.delete("t_lr", string, null)));
                } catch (Throwable th) {
                    if (!x.a(th)) {
                        th.printStackTrace();
                    }
                }
            }
        }
    }

    public final synchronized void b(int i) {
        String str = null;
        synchronized (this) {
            SQLiteDatabase writableDatabase = b.getWritableDatabase();
            if (writableDatabase != null) {
                if (i >= 0) {
                    try {
                        str = "_tp = " + i;
                    } catch (Throwable th) {
                        if (!x.a(th)) {
                            th.printStackTrace();
                        }
                    }
                }
                x.c("[Database] deleted %s data %d", "t_lr", Integer.valueOf(writableDatabase.delete("t_lr", str, null)));
            }
        }
    }

    private static ContentValues c(r rVar) {
        if (rVar == null) {
            return null;
        }
        try {
            ContentValues contentValues = new ContentValues();
            if (rVar.f200a > 0) {
                contentValues.put("_id", Long.valueOf(rVar.f200a));
            }
            contentValues.put("_tp", Integer.valueOf(rVar.b));
            contentValues.put("_pc", rVar.c);
            contentValues.put("_th", rVar.d);
            contentValues.put("_tm", Long.valueOf(rVar.e));
            if (rVar.g != null) {
                contentValues.put("_dt", rVar.g);
            }
            return contentValues;
        } catch (Throwable th) {
            if (x.a(th)) {
                return null;
            }
            th.printStackTrace();
            return null;
        }
    }

    private static r a(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        try {
            r rVar = new r();
            rVar.f200a = cursor.getLong(cursor.getColumnIndex("_id"));
            rVar.b = cursor.getInt(cursor.getColumnIndex("_tp"));
            rVar.c = cursor.getString(cursor.getColumnIndex("_pc"));
            rVar.d = cursor.getString(cursor.getColumnIndex("_th"));
            rVar.e = cursor.getLong(cursor.getColumnIndex("_tm"));
            rVar.g = cursor.getBlob(cursor.getColumnIndex("_dt"));
            return rVar;
        } catch (Throwable th) {
            if (x.a(th)) {
                return null;
            }
            th.printStackTrace();
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:35:0x0081 A[Catch: all -> 0x0085, TRY_ENTER, TryCatch #1 {, blocks: (B:9:0x0028, B:44:0x00bf, B:26:0x0055, B:35:0x0081, B:36:0x0084), top: B:52:0x0002 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private synchronized java.util.List<com.tencent.bugly.proguard.r> c(int r10) {
        /*
            r9 = this;
            r8 = 0
            monitor-enter(r9)
            com.tencent.bugly.proguard.q r0 = com.tencent.bugly.proguard.p.b     // Catch: java.lang.Throwable -> Lc5 java.lang.Throwable -> Lcb
            android.database.sqlite.SQLiteDatabase r0 = r0.getWritableDatabase()     // Catch: java.lang.Throwable -> Lc5 java.lang.Throwable -> Lcb
            if (r0 == 0) goto L58
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lc5 java.lang.Throwable -> Lcb
            java.lang.String r2 = "_id = "
            r1.<init>(r2)     // Catch: java.lang.Throwable -> Lc5 java.lang.Throwable -> Lcb
            java.lang.StringBuilder r1 = r1.append(r10)     // Catch: java.lang.Throwable -> Lc5 java.lang.Throwable -> Lcb
            java.lang.String r3 = r1.toString()     // Catch: java.lang.Throwable -> Lc5 java.lang.Throwable -> Lcb
            java.lang.String r1 = "t_pf"
            r2 = 0
            r4 = 0
            r5 = 0
            r6 = 0
            r7 = 0
            android.database.Cursor r2 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch: java.lang.Throwable -> Lc5 java.lang.Throwable -> Lcb
            if (r2 != 0) goto L2e
            if (r2 == 0) goto L2b
            r2.close()     // Catch: java.lang.Throwable -> L85
        L2b:
            r0 = r8
        L2c:
            monitor-exit(r9)
            return r0
        L2e:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L48 java.lang.Throwable -> L7e
            r4.<init>()     // Catch: java.lang.Throwable -> L48 java.lang.Throwable -> L7e
            java.util.ArrayList r1 = new java.util.ArrayList     // Catch: java.lang.Throwable -> L48 java.lang.Throwable -> L7e
            r1.<init>()     // Catch: java.lang.Throwable -> L48 java.lang.Throwable -> L7e
        L38:
            boolean r5 = r2.moveToNext()     // Catch: java.lang.Throwable -> L48 java.lang.Throwable -> L7e
            if (r5 == 0) goto L88
            com.tencent.bugly.proguard.r r5 = b(r2)     // Catch: java.lang.Throwable -> L48 java.lang.Throwable -> L7e
            if (r5 == 0) goto L5a
            r1.add(r5)     // Catch: java.lang.Throwable -> L48 java.lang.Throwable -> L7e
            goto L38
        L48:
            r0 = move-exception
            r1 = r2
        L4a:
            boolean r2 = com.tencent.bugly.proguard.x.a(r0)     // Catch: java.lang.Throwable -> Lc8
            if (r2 != 0) goto L53
            r0.printStackTrace()     // Catch: java.lang.Throwable -> Lc8
        L53:
            if (r1 == 0) goto L58
            r1.close()     // Catch: java.lang.Throwable -> L85
        L58:
            r0 = r8
            goto L2c
        L5a:
            java.lang.String r5 = "_tp"
            int r5 = r2.getColumnIndex(r5)     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> L7e
            java.lang.String r5 = r2.getString(r5)     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> L7e
            java.lang.String r6 = " or _tp"
            java.lang.StringBuilder r6 = r4.append(r6)     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> L7e
            java.lang.String r7 = " = "
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> L7e
            r6.append(r5)     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> L7e
            goto L38
        L74:
            r5 = move-exception
            java.lang.String r5 = "[Database] unknown id."
            r6 = 0
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch: java.lang.Throwable -> L48 java.lang.Throwable -> L7e
            com.tencent.bugly.proguard.x.d(r5, r6)     // Catch: java.lang.Throwable -> L48 java.lang.Throwable -> L7e
            goto L38
        L7e:
            r0 = move-exception
        L7f:
            if (r2 == 0) goto L84
            r2.close()     // Catch: java.lang.Throwable -> L85
        L84:
            throw r0     // Catch: java.lang.Throwable -> L85
        L85:
            r0 = move-exception
            monitor-exit(r9)
            throw r0
        L88:
            int r5 = r4.length()     // Catch: java.lang.Throwable -> L48 java.lang.Throwable -> L7e
            if (r5 <= 0) goto Lbd
            java.lang.String r5 = " and _id"
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch: java.lang.Throwable -> L48 java.lang.Throwable -> L7e
            java.lang.String r5 = " = "
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch: java.lang.Throwable -> L48 java.lang.Throwable -> L7e
            r4.append(r10)     // Catch: java.lang.Throwable -> L48 java.lang.Throwable -> L7e
            r4 = 4
            java.lang.String r3 = r3.substring(r4)     // Catch: java.lang.Throwable -> L48 java.lang.Throwable -> L7e
            java.lang.String r4 = "t_pf"
            r5 = 0
            int r0 = r0.delete(r4, r3, r5)     // Catch: java.lang.Throwable -> L48 java.lang.Throwable -> L7e
            java.lang.String r3 = "[Database] deleted %s illegal data %d."
            r4 = 2
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch: java.lang.Throwable -> L48 java.lang.Throwable -> L7e
            r5 = 0
            java.lang.String r6 = "t_pf"
            r4[r5] = r6     // Catch: java.lang.Throwable -> L48 java.lang.Throwable -> L7e
            r5 = 1
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch: java.lang.Throwable -> L48 java.lang.Throwable -> L7e
            r4[r5] = r0     // Catch: java.lang.Throwable -> L48 java.lang.Throwable -> L7e
            com.tencent.bugly.proguard.x.d(r3, r4)     // Catch: java.lang.Throwable -> L48 java.lang.Throwable -> L7e
        Lbd:
            if (r2 == 0) goto Lc2
            r2.close()     // Catch: java.lang.Throwable -> L85
        Lc2:
            r0 = r1
            goto L2c
        Lc5:
            r0 = move-exception
            r2 = r8
            goto L7f
        Lc8:
            r0 = move-exception
            r2 = r1
            goto L7f
        Lcb:
            r0 = move-exception
            r1 = r8
            goto L4a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.p.c(int):java.util.List");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean a(int i, String str, o oVar) {
        String str2;
        boolean z = false;
        synchronized (this) {
            try {
                try {
                    SQLiteDatabase writableDatabase = b.getWritableDatabase();
                    if (writableDatabase != null) {
                        if (z.a(str)) {
                            str2 = "_id = " + i;
                        } else {
                            str2 = "_id = " + i + " and _tp = \"" + str + "\"";
                        }
                        int iDelete = writableDatabase.delete("t_pf", str2, null);
                        x.c("[Database] deleted %s data %d", "t_pf", Integer.valueOf(iDelete));
                        z = iDelete > 0;
                    }
                    if (oVar != null) {
                        Boolean.valueOf(z);
                    }
                } catch (Throwable th) {
                    if (!x.a(th)) {
                        th.printStackTrace();
                    }
                    if (oVar != null) {
                        Boolean.valueOf(false);
                    }
                }
            } catch (Throwable th2) {
                if (oVar != null) {
                    Boolean.valueOf(false);
                }
                throw th2;
            }
        }
        return z;
    }

    private static ContentValues d(r rVar) {
        if (rVar == null || z.a(rVar.f)) {
            return null;
        }
        try {
            ContentValues contentValues = new ContentValues();
            if (rVar.f200a > 0) {
                contentValues.put("_id", Long.valueOf(rVar.f200a));
            }
            contentValues.put("_tp", rVar.f);
            contentValues.put("_tm", Long.valueOf(rVar.e));
            if (rVar.g != null) {
                contentValues.put("_dt", rVar.g);
                return contentValues;
            }
            return contentValues;
        } catch (Throwable th) {
            if (!x.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    private static r b(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        try {
            r rVar = new r();
            rVar.f200a = cursor.getLong(cursor.getColumnIndex("_id"));
            rVar.e = cursor.getLong(cursor.getColumnIndex("_tm"));
            rVar.f = cursor.getString(cursor.getColumnIndex("_tp"));
            rVar.g = cursor.getBlob(cursor.getColumnIndex("_dt"));
            return rVar;
        } catch (Throwable th) {
            if (x.a(th)) {
                return null;
            }
            th.printStackTrace();
            return null;
        }
    }

    /* compiled from: BUGLY */
    class a extends Thread {

        /* renamed from: a, reason: collision with root package name */
        private int f198a;
        private o b;
        private String c;
        private ContentValues d;
        private boolean e;
        private String[] f;
        private String g;
        private String[] h;
        private String i;
        private String j;
        private String k;
        private String l;
        private String m;
        private String[] n;
        private int o;
        private String p;
        private byte[] q;

        public a(int i, o oVar) {
            this.f198a = i;
            this.b = oVar;
        }

        public final void a(boolean z, String str, String[] strArr, String str2, String[] strArr2, String str3, String str4, String str5, String str6) {
            this.e = z;
            this.c = str;
            this.f = strArr;
            this.g = str2;
            this.h = strArr2;
            this.i = str3;
            this.j = str4;
            this.k = str5;
            this.l = str6;
        }

        public final void a(int i, String str, byte[] bArr) {
            this.o = i;
            this.p = str;
            this.q = bArr;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public final void run() {
            switch (this.f198a) {
                case 1:
                    p.this.a(this.c, this.d, this.b);
                    break;
                case 2:
                    p.this.a(this.c, this.m, this.n, this.b);
                    break;
                case 3:
                    p.this.a(this.e, this.c, this.f, this.g, this.h, this.i, this.j, this.k, this.l, this.b);
                    break;
                case 4:
                    p.this.a(this.o, this.p, this.q, this.b);
                    break;
                case 5:
                    p.this.a(this.o, this.b);
                    break;
                case 6:
                    p.this.a(this.o, this.p, this.b);
                    break;
            }
        }
    }
}
