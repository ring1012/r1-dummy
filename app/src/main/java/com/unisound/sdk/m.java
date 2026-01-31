package com.unisound.sdk;

import android.content.Context;
import android.os.Looper;
import android.os.Message;
import cn.yunzhisheng.asr.VAD;
import cn.yunzhisheng.asrfix.JniAsrFix;
import com.unisound.client.ErrorCode;
import java.util.List;

/* loaded from: classes.dex */
public class m extends com.unisound.common.ab implements ak, au {

    /* renamed from: a, reason: collision with root package name */
    public static final int f348a = 50;
    private static JniAsrFix h;
    ar b;
    private z c;
    private aa d;
    private aj e;
    private k f;
    private ai g;
    private w i;
    private boolean j;
    private boolean k;
    private Looper l;
    private Context m;
    private boolean n;

    static {
        System.loadLibrary("uscasr");
    }

    public m() {
        this.c = null;
        this.d = null;
        this.e = null;
        this.f = null;
        this.g = new ai();
        this.i = null;
        this.j = true;
        this.k = false;
        this.n = true;
    }

    public m(Looper looper, Context context) {
        super(looper);
        this.c = null;
        this.d = null;
        this.e = null;
        this.f = null;
        this.g = new ai();
        this.i = null;
        this.j = true;
        this.k = false;
        this.n = true;
        this.l = looper;
        this.m = context;
    }

    private void B() {
        if (this.c != null) {
            this.c.d();
        }
    }

    private void C() {
        ar arVar = this.b;
        if (arVar != null) {
            arVar.d();
        }
    }

    private void c(String str) {
        com.unisound.common.y.a(str);
    }

    private void d(String str) {
        com.unisound.common.y.c(str);
    }

    private void g(boolean z) {
        if (this.f != null) {
            this.f.a(z);
            this.f = null;
        }
    }

    private void l(int i) {
        if (this.b != null) {
            this.b.a(i);
        }
    }

    @Override // com.unisound.sdk.ak
    public void A() {
    }

    public String a(String str, String str2) {
        return h.a(str, str2);
    }

    public void a() {
        if (this.c != null) {
            this.c.k();
            this.c = null;
        }
    }

    @Override // com.unisound.sdk.ak
    public void a(int i) {
        if (this.c != null) {
            this.c.d();
        }
        com.unisound.common.y.c("FixRecognizer MSG_RECOGNITION_ERROR");
        l(i);
    }

    @Override // com.unisound.common.ar
    public void a(int i, int i2, Object obj) {
        if (this.b != null) {
            this.b.a(i, i2, obj);
        }
    }

    @Override // com.unisound.sdk.cr
    public void a(VAD vad) {
        if ((this.c == null || !this.c.e()) && this.b != null) {
            this.b.a(vad);
        }
    }

    public void a(aj ajVar) {
        this.e = ajVar;
    }

    @Override // com.unisound.sdk.ak
    public void a(al alVar) {
        if (this.b != null) {
            this.b.a(alVar.a(), alVar.c(), alVar.f(), alVar.g());
        }
    }

    public void a(ar arVar) {
        synchronized (this.l) {
            this.b = arVar;
            setMessageLisenter(this.b);
        }
    }

    public void a(u uVar) {
        h.a(uVar);
    }

    public void a(w wVar) {
        this.i = wVar;
    }

    public void a(Boolean bool) {
        if (h != null) {
            h.a(bool);
        }
    }

    protected void a(String str) {
        com.unisound.common.y.b(str);
    }

    public void a(String str, z zVar, aa aaVar) {
        this.n = true;
        if (this.f != null) {
            this.f.a(true);
            this.f = null;
        }
        this.g.a();
        if (this.j) {
            if (!this.k) {
                com.unisound.common.y.c("FixRecognizer MSG_RECOGNITION_ERROR");
                l(ErrorCode.ASR_SDK_FIX_RECOGNIZER_NO_INIT);
                return;
            }
            if (h.h()) {
                this.f = new j(h, str, this.i, aaVar, this.m);
                this.f.a(this);
                this.f.a(this.e);
                this.f.setName("usc_fix_thread");
                this.f.setPriority(10);
                this.f.start();
                com.unisound.common.y.f("Recognition Thread Start");
            } else {
                com.unisound.common.y.c("FixRecognizer MSG_RECOGNITION_ERROR");
                l(ErrorCode.ASR_SDK_FIX_RECOGNIZER_NO_INIT);
                if (this.b == null) {
                    return;
                }
            }
        }
        this.d = aaVar;
        this.d.setName("usc_vad_thread");
        this.d.setPriority(10);
        this.d.start();
        this.c = zVar;
        this.c.setName("usc_record_thread");
        this.c.setPriority(10);
        this.c.start();
        com.unisound.common.y.f("Recording Thread Start");
        if (this.b != null) {
            this.b.e();
        }
    }

    public void a(String str, String str2, String str3, String str4) {
        if (h.h()) {
            b(str2, str3, str4);
            return;
        }
        com.unisound.common.y.a("FixRecognizer Engine is not init, wrong cmd=" + str3);
        com.unisound.common.y.c("FixRecognizer MSG_RECOGNITION_ERROR");
        l(ErrorCode.ASR_SDK_FIX_RECOGNIZER_NO_INIT);
    }

    @Override // com.unisound.sdk.ak
    public void a(String str, boolean z, int i, long j, long j2, int i2, int i3) {
        al alVar = new al();
        alVar.a(str);
        alVar.a(z);
        alVar.c(i);
        alVar.a(j);
        alVar.b(j2);
        alVar.a(i2);
        alVar.b(i3);
        if (this.b != null) {
            this.b.a(alVar.a(), alVar.c(), alVar.i(), alVar.j(), alVar.k(), alVar.f(), alVar.g());
        }
    }

    public void a(boolean z) {
        if (z && h != null) {
            h.b();
        }
        com.unisound.common.y.a("FixRecognizer Cancel and wait end +" + (this.f == null));
        a();
        if (this.d != null) {
            this.d.f();
            this.d = null;
        }
        if (this.f != null) {
            this.f.a(true);
            this.f = null;
        }
        removeSendMessage();
    }

    @Override // com.unisound.sdk.au
    public void a(boolean z, byte[] bArr, int i, int i2) {
        if (this.d != null) {
            this.d.a(bArr);
        }
    }

    @Override // com.unisound.common.ab
    public boolean a(Message message) {
        return true;
    }

    public boolean a(String str, String str2, String str3) {
        if ("init_asr" == str3) {
            int iA = h.a(str, str2, str3, this.i);
            if (iA != 0) {
                c("jac.init path=" + str + ":" + str2 + " error:" + iA);
                return false;
            }
            h.a(0, 1);
            h.a(1, 3000);
            h.a(6, 8);
            h.a(9, this.i.aZ());
        }
        return true;
    }

    public int b(String str, String str2, String str3) {
        return h.a(str, str2, this.i, str3);
    }

    public void b() {
        B();
        if (this.c == null && this.f != null && !this.f.g()) {
            if (this.b != null) {
                this.b.a();
            }
            g();
        }
        if (this.d != null) {
            this.d.a();
        }
    }

    @Override // com.unisound.sdk.cr
    public void b(int i) {
        if (this.b != null) {
            this.b.b(i);
        }
    }

    @Override // com.unisound.sdk.ak
    public void b(String str) {
    }

    @Override // com.unisound.sdk.au
    public void b(boolean z) {
        this.g.a();
        if (!z) {
            f();
            l(ErrorCode.FAILED_START_RECORDING);
        } else if (this.b != null) {
            com.unisound.common.y.c("msg onRecordingStart true");
            this.b.c();
        }
    }

    @Override // com.unisound.sdk.cr
    public void b(boolean z, byte[] bArr, int i, int i2) {
        if (this.g.a(bArr, i, i2)) {
            C();
        }
        if (z && this.g.b() && this.f != null && bArr != null && bArr.length > 0) {
            this.f.b(bArr);
        }
        ar arVar = this.b;
        if (arVar != null) {
            if (ai.f296a) {
                arVar.b(this.g.b() && z, bArr, i, i2);
            } else {
                arVar.b(z, bArr, i, i2);
            }
        }
    }

    public int c(int i) {
        if (h != null) {
            return h.b(i);
        }
        return -1;
    }

    protected void c() {
        if (this.b != null) {
            this.b.f();
        }
    }

    public void c(boolean z) {
        this.j = z;
    }

    public int d(int i) {
        if (h != null) {
            return h.c(i);
        }
        return -1;
    }

    protected void d() {
        ar arVar = this.b;
        if (arVar != null) {
            arVar.n();
        }
    }

    public void d(boolean z) {
        this.k = z;
    }

    public int e(int i) {
        if (h != null) {
            return h.d(i);
        }
        return -1;
    }

    public int e(boolean z) {
        if (h != null) {
            return h.a(z);
        }
        return -1;
    }

    protected void e() {
        ar arVar = this.b;
        if (arVar != null) {
            arVar.o();
        }
    }

    public int f(int i) {
        if (h != null) {
            return h.e(i);
        }
        return -1;
    }

    public int f(boolean z) {
        if (h != null) {
            return h.b(z);
        }
        return -1;
    }

    protected void f() {
        if (this.f != null) {
            this.f.c();
        }
    }

    public int g(int i) {
        if (h != null) {
            return h.f(i);
        }
        return -1;
    }

    public void g() {
        if (this.f != null) {
            this.f.d();
        }
    }

    public int h(int i) {
        if (h != null) {
            return h.g(i);
        }
        return -1;
    }

    protected void h() {
        if (this.c != null) {
            this.c.g();
        }
    }

    public int i(int i) {
        if (h != null) {
            return h.h(i);
        }
        return -1;
    }

    @Override // com.unisound.sdk.ak
    public void i() {
        com.unisound.common.y.c("FixRecognizer MSG_RECOGNITION_STOP");
        l(0);
    }

    public int j(int i) {
        if (h != null) {
            return h.i(i);
        }
        return -1;
    }

    @Override // com.unisound.sdk.au
    public void j() {
        if (this.b != null) {
            this.b.a();
        }
        g();
    }

    @Override // com.unisound.sdk.au
    public void k() {
        f();
        l(ErrorCode.RECORDING_EXCEPTION);
    }

    @Override // com.unisound.sdk.ak
    public void k(int i) {
        ar arVar = this.b;
        if (arVar != null) {
            arVar.d(i);
        }
    }

    @Override // com.unisound.sdk.ak
    public void l() {
        if (this.c != null) {
        }
    }

    @Override // com.unisound.sdk.ak
    public void m() {
        f();
        B();
        l(ErrorCode.ASR_FIXENGINE_MAX_SPEECH_TIMEOUT);
    }

    @Override // com.unisound.sdk.cr
    public void n() {
        d();
    }

    @Override // com.unisound.sdk.cr
    public void o() {
        e();
    }

    public void p() {
        synchronized (h) {
            if (this.c != null) {
                this.c.d();
            }
            h.d();
            if (this.c != null) {
                this.c.k();
            }
            if (this.d != null) {
                this.d.f();
            }
            g(true);
            h.i();
            com.unisound.common.y.c("jac.unLoad();");
        }
    }

    public boolean q() {
        return h.h();
    }

    public boolean r() {
        if (this.f != null) {
            return this.f.isAlive();
        }
        return false;
    }

    public void s() {
        h = JniAsrFix.a();
    }

    public int t() {
        if (h != null) {
            return h.k();
        }
        return 1;
    }

    public List<Integer> u() {
        if (h != null) {
            return h.m();
        }
        return null;
    }

    public int v() {
        if (h != null) {
            return h.n();
        }
        return -1;
    }

    public String w() {
        return h != null ? h.o() : "";
    }

    public String x() {
        return h != null ? h.p() : "";
    }

    public int y() {
        if (h != null) {
            return h.r();
        }
        return -1;
    }

    public int z() {
        if (h != null) {
            return h.s();
        }
        return -1;
    }
}
