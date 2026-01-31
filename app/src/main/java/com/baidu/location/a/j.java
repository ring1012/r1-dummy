package com.baidu.location.a;

import android.content.Context;
import android.util.Log;
import java.util.Hashtable;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class j implements com.baidu.b.a.c {

    /* renamed from: a, reason: collision with root package name */
    private static Object f72a = new Object();
    private static j b = null;
    private int c = 0;
    private Context d = null;
    private long e = 0;
    private String f = null;

    public static j a() {
        j jVar;
        synchronized (f72a) {
            if (b == null) {
                b = new j();
            }
            jVar = b;
        }
        return jVar;
    }

    public static String b(Context context) {
        try {
            return com.baidu.b.a.b.a(context).b(context);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String c(Context context) {
        try {
            return com.baidu.b.a.b.a(context).a();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override // com.baidu.b.a.c
    public void a(int i, String str) {
        this.c = i;
        if (this.c == 0) {
            Log.i(com.baidu.location.d.a.f101a, "LocationAuthManager Authentication AUTHENTICATE_SUCC");
        } else {
            Log.i(com.baidu.location.d.a.f101a, "LocationAuthManager Authentication Error errorcode = " + i + " , msg = " + str);
        }
        if (str != null) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                if (jSONObject == null || jSONObject.getString("token") == null) {
                    return;
                }
                this.f = jSONObject.getString("token");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void a(Context context) {
        this.d = context;
        com.baidu.b.a.b.a(this.d).a(false, "lbs_locsdk", (Hashtable<String, String>) null, (com.baidu.b.a.c) this);
        this.e = System.currentTimeMillis();
    }

    public boolean b() {
        boolean z = true;
        if (this.c != 0 && this.c != 602 && this.c != 601 && this.c != -10 && this.c != -11) {
            z = false;
        }
        if (this.d != null) {
            long jCurrentTimeMillis = System.currentTimeMillis() - this.e;
            if (z) {
                if (jCurrentTimeMillis > 86400000) {
                    com.baidu.b.a.b.a(this.d).a(false, "lbs_locsdk", (Hashtable<String, String>) null, (com.baidu.b.a.c) this);
                    this.e = System.currentTimeMillis();
                }
            } else if (jCurrentTimeMillis < 0 || jCurrentTimeMillis > 10000) {
                com.baidu.b.a.b.a(this.d).a(false, "lbs_locsdk", (Hashtable<String, String>) null, (com.baidu.b.a.c) this);
                this.e = System.currentTimeMillis();
            }
        }
        return z;
    }
}
