package com.baidu.location.a;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import com.baidu.location.Jni;
import com.google.android.exoplayer2.text.ttml.TtmlNode;
import java.io.File;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class h {
    private static Object c = new Object();
    private static h d = null;
    private static final String e = com.baidu.location.d.j.h() + "/hst.db";
    private SQLiteDatabase f = null;
    private boolean g = false;

    /* renamed from: a, reason: collision with root package name */
    a f67a = null;
    a b = null;

    class a extends com.baidu.location.d.e {
        private String b = null;
        private String c = null;
        private boolean d = true;
        private boolean e = false;

        a() {
            this.k = new HashMap();
        }

        @Override // com.baidu.location.d.e
        public void a() {
            this.i = 1;
            this.h = com.baidu.location.d.j.c();
            String strD = Jni.d(this.c);
            this.c = null;
            this.k.put("bloc", strD);
        }

        public void a(String str, String str2) {
            if (h.this.g) {
                return;
            }
            h.this.g = true;
            this.b = str;
            this.c = str2;
            b(com.baidu.location.d.j.f);
        }

        @Override // com.baidu.location.d.e
        public void a(boolean z) throws JSONException, NumberFormatException {
            if (z && this.j != null) {
                try {
                    String str = this.j;
                    if (this.d) {
                        JSONObject jSONObject = new JSONObject(str);
                        JSONObject jSONObject2 = jSONObject.has("content") ? jSONObject.getJSONObject("content") : null;
                        if (jSONObject2 != null && jSONObject2.has("imo")) {
                            Long lValueOf = Long.valueOf(jSONObject2.getJSONObject("imo").getString("mac"));
                            int i = jSONObject2.getJSONObject("imo").getInt("mv");
                            if (Jni.c(this.b).longValue() == lValueOf.longValue()) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(TtmlNode.TAG_TT, Integer.valueOf((int) (System.currentTimeMillis() / 1000)));
                                contentValues.put("hst", Integer.valueOf(i));
                                try {
                                    if (h.this.f.update("hstdata", contentValues, "id = \"" + lValueOf + "\"", null) <= 0) {
                                        contentValues.put(TtmlNode.ATTR_ID, lValueOf);
                                        h.this.f.insert("hstdata", null, contentValues);
                                    }
                                } catch (Exception e) {
                                }
                                Bundle bundle = new Bundle();
                                bundle.putByteArray("mac", this.b.getBytes());
                                bundle.putInt("hotspot", i);
                                h.this.a(bundle);
                            }
                        }
                    }
                } catch (Exception e2) {
                }
            } else if (this.d) {
                h.this.f();
            }
            if (this.k != null) {
                this.k.clear();
            }
            h.this.g = false;
        }
    }

    public static h a() {
        h hVar;
        synchronized (c) {
            if (d == null) {
                d = new h();
            }
            hVar = d;
        }
        return hVar;
    }

    private String a(boolean z) {
        com.baidu.location.bfold.a aVarF = com.baidu.location.b.b.a().f();
        com.baidu.location.bfold.f fVarO = com.baidu.location.b.g.a().o();
        StringBuffer stringBuffer = new StringBuffer(1024);
        if (aVarF != null && aVarF.b()) {
            stringBuffer.append(aVarF.g());
        }
        if (fVarO != null && fVarO.a() > 1) {
            stringBuffer.append(fVarO.a(15));
        } else if (com.baidu.location.b.g.a().l() != null) {
            stringBuffer.append(com.baidu.location.b.g.a().l());
        }
        if (z) {
            stringBuffer.append("&imo=1");
        }
        stringBuffer.append(com.baidu.location.d.b.a().a(false));
        stringBuffer.append(com.baidu.location.a.a.a().c());
        return stringBuffer.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(Bundle bundle) {
        com.baidu.location.a.a.a().a(bundle, 406);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f() {
        Bundle bundle = new Bundle();
        bundle.putInt("hotspot", -1);
        a(bundle);
    }

    public void a(String str) throws JSONException, NumberFormatException {
        if (this.g) {
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            JSONObject jSONObject2 = jSONObject.has("content") ? jSONObject.getJSONObject("content") : null;
            if (jSONObject2 == null || !jSONObject2.has("imo")) {
                return;
            }
            Long lValueOf = Long.valueOf(jSONObject2.getJSONObject("imo").getString("mac"));
            int i = jSONObject2.getJSONObject("imo").getInt("mv");
            ContentValues contentValues = new ContentValues();
            contentValues.put(TtmlNode.TAG_TT, Integer.valueOf((int) (System.currentTimeMillis() / 1000)));
            contentValues.put("hst", Integer.valueOf(i));
            try {
                if (this.f.update("hstdata", contentValues, "id = \"" + lValueOf + "\"", null) <= 0) {
                    contentValues.put(TtmlNode.ATTR_ID, lValueOf);
                    this.f.insert("hstdata", null, contentValues);
                }
            } catch (Exception e2) {
            }
        } catch (Exception e3) {
        }
    }

    public void b() {
        try {
            File file = new File(e);
            if (!file.exists()) {
                file.createNewFile();
            }
            if (file.exists()) {
                this.f = SQLiteDatabase.openOrCreateDatabase(file, (SQLiteDatabase.CursorFactory) null);
                this.f.execSQL("CREATE TABLE IF NOT EXISTS hstdata(id Long PRIMARY KEY,hst INT,tt INT);");
                this.f.setVersion(1);
            }
        } catch (Exception e2) {
            this.f = null;
        }
    }

    public void c() {
        if (this.f != null) {
            try {
                this.f.close();
            } catch (Exception e2) {
            } finally {
                this.f = null;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x0066  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int d() throws java.lang.Throwable {
        /*
            r7 = this;
            r1 = 0
            r0 = -3
            boolean r2 = r7.g
            if (r2 == 0) goto L7
        L6:
            return r0
        L7:
            boolean r2 = com.baidu.location.b.g.i()     // Catch: java.lang.Exception -> L7b
            if (r2 == 0) goto L6
            android.database.sqlite.SQLiteDatabase r2 = r7.f     // Catch: java.lang.Exception -> L7b
            if (r2 == 0) goto L6
            com.baidu.location.b.g r2 = com.baidu.location.b.g.a()     // Catch: java.lang.Exception -> L7b
            android.net.wifi.WifiInfo r2 = r2.k()     // Catch: java.lang.Exception -> L7b
            if (r2 == 0) goto L6
            java.lang.String r3 = r2.getBSSID()     // Catch: java.lang.Exception -> L7b
            if (r3 == 0) goto L6
            java.lang.String r2 = r2.getBSSID()     // Catch: java.lang.Exception -> L7b
            java.lang.String r3 = ":"
            java.lang.String r4 = ""
            java.lang.String r2 = r2.replace(r3, r4)     // Catch: java.lang.Exception -> L7b
            java.lang.Long r2 = com.baidu.location.Jni.c(r2)     // Catch: java.lang.Exception -> L7b
            android.database.sqlite.SQLiteDatabase r3 = r7.f     // Catch: java.lang.Exception -> L68 java.lang.Throwable -> L71
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L68 java.lang.Throwable -> L71
            r4.<init>()     // Catch: java.lang.Exception -> L68 java.lang.Throwable -> L71
            java.lang.String r5 = "select * from hstdata where id = \""
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch: java.lang.Exception -> L68 java.lang.Throwable -> L71
            java.lang.StringBuilder r2 = r4.append(r2)     // Catch: java.lang.Exception -> L68 java.lang.Throwable -> L71
            java.lang.String r4 = "\";"
            java.lang.StringBuilder r2 = r2.append(r4)     // Catch: java.lang.Exception -> L68 java.lang.Throwable -> L71
            java.lang.String r2 = r2.toString()     // Catch: java.lang.Exception -> L68 java.lang.Throwable -> L71
            r4 = 0
            android.database.Cursor r1 = r3.rawQuery(r2, r4)     // Catch: java.lang.Exception -> L68 java.lang.Throwable -> L71
            if (r1 == 0) goto L66
            boolean r2 = r1.moveToFirst()     // Catch: java.lang.Exception -> L68 java.lang.Throwable -> L7f
            if (r2 == 0) goto L66
            r2 = 1
            int r0 = r1.getInt(r2)     // Catch: java.lang.Exception -> L68 java.lang.Throwable -> L7f
        L5e:
            if (r1 == 0) goto L6
            r1.close()     // Catch: java.lang.Exception -> L64
            goto L6
        L64:
            r1 = move-exception
            goto L6
        L66:
            r0 = -2
            goto L5e
        L68:
            r2 = move-exception
            if (r1 == 0) goto L6
            r1.close()     // Catch: java.lang.Exception -> L6f
            goto L6
        L6f:
            r1 = move-exception
            goto L6
        L71:
            r2 = move-exception
            r6 = r2
            r2 = r1
            r1 = r6
        L75:
            if (r2 == 0) goto L7a
            r2.close()     // Catch: java.lang.Exception -> L7d
        L7a:
            throw r1     // Catch: java.lang.Exception -> L7b
        L7b:
            r1 = move-exception
            goto L6
        L7d:
            r2 = move-exception
            goto L7a
        L7f:
            r2 = move-exception
            r6 = r2
            r2 = r1
            r1 = r6
            goto L75
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.a.h.d():int");
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x007c A[Catch: Exception -> 0x0097, TRY_ENTER, TryCatch #2 {Exception -> 0x0097, blocks: (B:5:0x0007, B:7:0x000d, B:9:0x0011, B:11:0x001b, B:13:0x0021, B:26:0x007c, B:28:0x0080, B:29:0x0087, B:31:0x008b, B:48:0x00c4, B:49:0x00c5, B:50:0x00ca, B:15:0x0032, B:17:0x0054, B:19:0x005a, B:35:0x009a), top: B:62:0x0007, inners: #5 }] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00b2  */
    /* JADX WARN: Removed duplicated region for block: B:69:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void e() {
        /*
            r10 = this;
            r2 = 0
            r0 = 1
            boolean r1 = r10.g
            if (r1 == 0) goto L7
        L6:
            return
        L7:
            boolean r1 = com.baidu.location.b.g.i()     // Catch: java.lang.Exception -> L97
            if (r1 == 0) goto Lca
            android.database.sqlite.SQLiteDatabase r1 = r10.f     // Catch: java.lang.Exception -> L97
            if (r1 == 0) goto Lca
            com.baidu.location.b.g r1 = com.baidu.location.b.g.a()     // Catch: java.lang.Exception -> L97
            android.net.wifi.WifiInfo r1 = r1.k()     // Catch: java.lang.Exception -> L97
            if (r1 == 0) goto Lc5
            java.lang.String r3 = r1.getBSSID()     // Catch: java.lang.Exception -> L97
            if (r3 == 0) goto Lc5
            java.lang.String r1 = r1.getBSSID()     // Catch: java.lang.Exception -> L97
            java.lang.String r3 = ":"
            java.lang.String r4 = ""
            java.lang.String r3 = r1.replace(r3, r4)     // Catch: java.lang.Exception -> L97
            java.lang.Long r4 = com.baidu.location.Jni.c(r3)     // Catch: java.lang.Exception -> L97
            r1 = 0
            android.database.sqlite.SQLiteDatabase r5 = r10.f     // Catch: java.lang.Exception -> Lb4 java.lang.Throwable -> Lbe
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> Lb4 java.lang.Throwable -> Lbe
            r6.<init>()     // Catch: java.lang.Exception -> Lb4 java.lang.Throwable -> Lbe
            java.lang.String r7 = "select * from hstdata where id = \""
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch: java.lang.Exception -> Lb4 java.lang.Throwable -> Lbe
            java.lang.StringBuilder r4 = r6.append(r4)     // Catch: java.lang.Exception -> Lb4 java.lang.Throwable -> Lbe
            java.lang.String r6 = "\";"
            java.lang.StringBuilder r4 = r4.append(r6)     // Catch: java.lang.Exception -> Lb4 java.lang.Throwable -> Lbe
            java.lang.String r4 = r4.toString()     // Catch: java.lang.Exception -> Lb4 java.lang.Throwable -> Lbe
            r6 = 0
            android.database.Cursor r2 = r5.rawQuery(r4, r6)     // Catch: java.lang.Exception -> Lb4 java.lang.Throwable -> Lbe
            if (r2 == 0) goto Lb2
            boolean r4 = r2.moveToFirst()     // Catch: java.lang.Throwable -> Lbe java.lang.Exception -> Ld3
            if (r4 == 0) goto Lb2
            r4 = 1
            int r4 = r2.getInt(r4)     // Catch: java.lang.Throwable -> Lbe java.lang.Exception -> Ld3
            r5 = 2
            int r5 = r2.getInt(r5)     // Catch: java.lang.Throwable -> Lbe java.lang.Exception -> Ld3
            long r6 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Throwable -> Lbe java.lang.Exception -> Ld3
            r8 = 1000(0x3e8, double:4.94E-321)
            long r6 = r6 / r8
            long r8 = (long) r5
            long r6 = r6 - r8
            r8 = 259200(0x3f480, double:1.28062E-318)
            int r5 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r5 <= 0) goto L9a
        L74:
            r1 = r0
        L75:
            if (r2 == 0) goto L7a
            r2.close()     // Catch: java.lang.Exception -> Lcf
        L7a:
            if (r1 == 0) goto L6
            com.baidu.location.a.h$a r0 = r10.f67a     // Catch: java.lang.Exception -> L97
            if (r0 != 0) goto L87
            com.baidu.location.a.h$a r0 = new com.baidu.location.a.h$a     // Catch: java.lang.Exception -> L97
            r0.<init>()     // Catch: java.lang.Exception -> L97
            r10.f67a = r0     // Catch: java.lang.Exception -> L97
        L87:
            com.baidu.location.a.h$a r0 = r10.f67a     // Catch: java.lang.Exception -> L97
            if (r0 == 0) goto L6
            com.baidu.location.a.h$a r0 = r10.f67a     // Catch: java.lang.Exception -> L97
            r1 = 1
            java.lang.String r1 = r10.a(r1)     // Catch: java.lang.Exception -> L97
            r0.a(r3, r1)     // Catch: java.lang.Exception -> L97
            goto L6
        L97:
            r0 = move-exception
            goto L6
        L9a:
            android.os.Bundle r0 = new android.os.Bundle     // Catch: java.lang.Throwable -> Lbe java.lang.Exception -> Ld3
            r0.<init>()     // Catch: java.lang.Throwable -> Lbe java.lang.Exception -> Ld3
            java.lang.String r5 = "mac"
            byte[] r6 = r3.getBytes()     // Catch: java.lang.Throwable -> Lbe java.lang.Exception -> Ld3
            r0.putByteArray(r5, r6)     // Catch: java.lang.Throwable -> Lbe java.lang.Exception -> Ld3
            java.lang.String r5 = "hotspot"
            r0.putInt(r5, r4)     // Catch: java.lang.Throwable -> Lbe java.lang.Exception -> Ld3
            r10.a(r0)     // Catch: java.lang.Throwable -> Lbe java.lang.Exception -> Ld3
            r0 = r1
            goto L74
        Lb2:
            r1 = r0
            goto L75
        Lb4:
            r0 = move-exception
            r0 = r2
        Lb6:
            if (r0 == 0) goto L7a
            r0.close()     // Catch: java.lang.Exception -> Lbc
            goto L7a
        Lbc:
            r0 = move-exception
            goto L7a
        Lbe:
            r0 = move-exception
            if (r2 == 0) goto Lc4
            r2.close()     // Catch: java.lang.Exception -> Ld1
        Lc4:
            throw r0     // Catch: java.lang.Exception -> L97
        Lc5:
            r10.f()     // Catch: java.lang.Exception -> L97
            goto L6
        Lca:
            r10.f()     // Catch: java.lang.Exception -> L97
            goto L6
        Lcf:
            r0 = move-exception
            goto L7a
        Ld1:
            r1 = move-exception
            goto Lc4
        Ld3:
            r0 = move-exception
            r0 = r2
            goto Lb6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.a.h.e():void");
    }
}
