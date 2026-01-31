package com.baidu.location.a;

import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import cn.yunzhisheng.asr.JniUscClient;
import com.baidu.location.Jni;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

/* loaded from: classes.dex */
public abstract class i {
    public static String c = null;

    /* renamed from: a, reason: collision with root package name */
    public com.baidu.location.b.f f69a = null;
    public com.baidu.location.b.a b = null;
    private boolean e = true;
    private boolean f = true;
    private boolean g = false;
    final Handler d = new a();
    private String h = null;
    private String i = null;

    public class a extends Handler {
        public a() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (com.baidu.location.f.f) {
                switch (message.what) {
                    case 21:
                        i.this.a(message);
                        break;
                    case JniUscClient.ay /* 62 */:
                    case 63:
                        i.this.a();
                        break;
                }
            }
        }
    }

    class b extends com.baidu.location.d.e {

        /* renamed from: a, reason: collision with root package name */
        String f71a = null;
        String b = null;

        public b() {
            this.k = new HashMap();
        }

        @Override // com.baidu.location.d.e
        public void a() {
            this.h = com.baidu.location.d.j.c();
            if ((com.baidu.location.d.j.h || com.baidu.location.d.j.i) && i.this.h != null && i.this.i != null) {
                this.b += String.format(Locale.CHINA, "&ki=%s&sn=%s", i.this.h, i.this.i);
            }
            String strD = Jni.d(this.b);
            this.b = null;
            if (this.f71a == null) {
                this.f71a = u.b();
            }
            this.k.put("bloc", strD);
            if (this.f71a != null) {
                this.k.put("up", this.f71a);
            }
            this.k.put("trtm", String.format(Locale.CHINA, "%d", Long.valueOf(System.currentTimeMillis())));
        }

        public void a(String str) {
            this.b = str;
            b(com.baidu.location.d.j.f);
        }

        @Override // com.baidu.location.d.e
        public void a(boolean z) {
            com.baidu.location.c cVar;
            if (!z || this.j == null) {
                Message messageObtainMessage = i.this.d.obtainMessage(63);
                messageObtainMessage.obj = "HttpStatus error";
                messageObtainMessage.sendToTarget();
            } else {
                try {
                    String str = this.j;
                    i.c = str;
                    try {
                        cVar = new com.baidu.location.c(str);
                        if (cVar.h() == 161) {
                            h.a().a(str);
                        }
                        cVar.g(com.baidu.location.b.b.a().h());
                        if (m.a().d()) {
                            cVar.c(m.a().e());
                        }
                    } catch (Exception e) {
                        cVar = new com.baidu.location.c();
                        cVar.d(0);
                    }
                    this.f71a = null;
                    if (cVar.h() == 0 && cVar.d() == Double.MIN_VALUE && cVar.e() == Double.MIN_VALUE) {
                        Message messageObtainMessage2 = i.this.d.obtainMessage(63);
                        messageObtainMessage2.obj = "HttpStatus error";
                        messageObtainMessage2.sendToTarget();
                    } else {
                        Message messageObtainMessage3 = i.this.d.obtainMessage(21);
                        messageObtainMessage3.obj = cVar;
                        messageObtainMessage3.sendToTarget();
                    }
                } catch (Exception e2) {
                    Message messageObtainMessage4 = i.this.d.obtainMessage(63);
                    messageObtainMessage4.obj = "HttpStatus error";
                    messageObtainMessage4.sendToTarget();
                }
            }
            if (this.k != null) {
                this.k.clear();
            }
        }
    }

    public String a(String str) throws IOException {
        String strL;
        if (this.h == null) {
            this.h = j.b(com.baidu.location.f.b());
        }
        if (this.i == null) {
            this.i = j.c(com.baidu.location.f.b());
        }
        if (this.b == null || !this.b.a()) {
            this.b = com.baidu.location.b.b.a().f();
        }
        if (this.f69a == null || !this.f69a.h()) {
            this.f69a = com.baidu.location.b.g.a().o();
        }
        Location locationG = com.baidu.location.b.d.a().i() ? com.baidu.location.b.d.a().g() : null;
        if ((this.b == null || this.b.d() || this.b.c()) && ((this.f69a == null || this.f69a.a() == 0) && locationG == null)) {
            return null;
        }
        String strB = b();
        if (h.a().d() == -2) {
            strB = strB + "&imo=1";
        }
        int iB = com.baidu.location.d.j.b(com.baidu.location.f.b());
        if (iB >= 0) {
            strB = strB + "&lmd=" + iB;
        }
        String str2 = ((this.f69a == null || this.f69a.a() == 0) && (strL = com.baidu.location.b.g.a().l()) != null) ? strL + strB : strB;
        if (!this.f) {
            return com.baidu.location.d.j.a(this.b, this.f69a, locationG, str2, 0);
        }
        this.f = false;
        return com.baidu.location.d.j.a(this.b, this.f69a, locationG, str2, 0, true);
    }

    public abstract void a();

    public abstract void a(Message message);

    public String b() throws IOException {
        String strC = com.baidu.location.a.a.a().c();
        String str = com.baidu.location.b.g.i() ? "&cn=32" : String.format(Locale.CHINA, "&cn=%d", Integer.valueOf(com.baidu.location.b.b.a().e()));
        if (this.e) {
            this.e = false;
            String strQ = com.baidu.location.b.g.a().q();
            if (!TextUtils.isEmpty(strQ) && !strQ.equals("02:00:00:00:00:00")) {
                str = String.format(Locale.CHINA, "%s&mac=%s", str, strQ.replace(":", ""));
            }
            if (Build.VERSION.SDK_INT > 17) {
            }
        } else if (!this.g) {
            String strF = u.f();
            if (strF != null) {
                str = str + strF;
            }
            this.g = true;
        }
        return str + strC;
    }
}
