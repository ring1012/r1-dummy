package com.unisound.sdk;

import android.content.Context;
import android.os.Looper;
import android.os.Message;
import android.util.SparseArray;
import cn.yunzhisheng.asr.VAD;
import com.unisound.client.ErrorCode;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class ao extends com.unisound.common.ab implements ak, au, cq {

    /* renamed from: a, reason: collision with root package name */
    public static int f300a = 0;
    public static int b = 0;
    private static final int r = 40;
    protected com.unisound.common.av c;
    private com.unisound.common.au d;
    private ay e;
    private ay f;
    private an g;
    private z h;
    private ad i;
    private as j;
    private String k;
    private boolean l;
    private Context m;
    private aa n;
    private Looper o;
    private ag p;
    private af q;

    static {
        System.loadLibrary("uscasr");
        f300a = 60000;
        b = 10000;
    }

    public ao(as asVar) {
        this.d = new com.unisound.common.au();
        this.e = null;
        this.f = null;
        this.g = null;
        this.h = null;
        this.i = null;
        this.j = null;
        this.k = "";
        this.l = true;
        this.n = null;
        this.p = new ap(this);
        this.c = null;
        this.j = asVar;
    }

    public ao(as asVar, Looper looper) {
        super(looper);
        this.d = new com.unisound.common.au();
        this.e = null;
        this.f = null;
        this.g = null;
        this.h = null;
        this.i = null;
        this.j = null;
        this.k = "";
        this.l = true;
        this.n = null;
        this.p = new ap(this);
        this.c = null;
        this.j = asVar;
        this.o = looper;
        this.q = new af(this.p, looper);
    }

    private void d(String str) {
        com.unisound.common.y.c("Before startRecognition :cancelRecognition()");
        d(false);
        this.j.aW = this.d.a();
        try {
            this.j.aZ = com.unisound.c.a.b();
        } catch (Exception e) {
            com.unisound.common.y.c("Recognizer:: getNetType error");
        }
        this.e = this.f;
        this.c = null;
        this.l = false;
        this.k = "";
        this.g = new an(this.j, this.m, str);
        this.g.a(this);
        this.g.setPriority(10);
        this.g.setName("usc_net_thread");
        if (!this.j.A()) {
            this.g.start();
        }
        com.unisound.common.y.c("Recognizer:: recognitionThread start");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d(boolean z) {
        if (this.g == null || this.g.d()) {
            return;
        }
        this.g.c();
    }

    private void f(int i) {
        if (i == 0) {
            this.c = new com.unisound.common.av(this.j.aY, this.d.a());
        }
        this.l = true;
        this.k = "";
        if (this.g != null) {
            this.k = this.g.e();
        }
        if (this.q.b()) {
            return;
        }
        this.q.e();
        if (this.i != null) {
            this.i.b(i);
        }
    }

    private void r() {
        com.unisound.common.y.c("Recognizer stopRecording");
        this.l = true;
        if (this.h != null) {
            this.h.d();
        }
    }

    private void s() {
        this.l = true;
        if (this.g == null || this.g.a()) {
            return;
        }
        this.g.b();
        this.q.c();
    }

    private void t() {
        if (this.h != null) {
            this.h.g();
        }
    }

    @Override // com.unisound.sdk.ak
    public void A() {
        sendMessage(40);
    }

    public String a() {
        return com.unisound.common.an.a();
    }

    @Override // com.unisound.sdk.ak
    public void a(int i) {
        r();
        t();
        f(i);
    }

    @Override // com.unisound.common.ar
    public void a(int i, int i2, Object obj) {
        if (this.i != null) {
            this.i.a(i, i2, obj);
        }
    }

    public void a(Context context, ad adVar) {
        this.m = context;
        a(adVar);
        com.unisound.c.a.a(context);
    }

    public void a(SparseArray<List<String>> sparseArray) {
        cp cpVar = new cp();
        cpVar.a(this);
        cpVar.a(this.j.aZ(), sparseArray);
    }

    @Override // com.unisound.sdk.cr
    public void a(VAD vad) {
        if (this.i != null) {
            this.i.b();
        }
    }

    public void a(ad adVar) {
        this.i = adVar;
    }

    @Override // com.unisound.sdk.ak
    public void a(al alVar) {
        if (this.i != null) {
            this.i.a(alVar);
        }
    }

    public void a(ay ayVar) {
        this.f = ayVar;
        this.e = ayVar;
    }

    public void a(z zVar, boolean z, String str, aa aaVar) {
        this.q.d();
        d(str);
        if (z) {
            this.n = aaVar;
            this.h = zVar;
            this.h.setPriority(10);
            this.h.start();
        }
    }

    public void a(String str) {
        cp cpVar = new cp();
        cpVar.a(this);
        cpVar.a(this.j.aZ(), str);
    }

    public void a(String str, int i) {
        this.j.a(new String(str), i);
    }

    @Override // com.unisound.sdk.ak
    public void a(String str, boolean z, int i, long j, long j2, int i2, int i3) {
    }

    public void a(List<byte[]> list, String str) {
        d(str);
        this.g.a(list);
        this.g.b();
    }

    public void a(Map<Integer, List<String>> map) {
        cp cpVar = new cp();
        cpVar.a(this);
        cpVar.a(this.j.aZ(), map);
    }

    public void a(boolean z) {
        this.j.K(z);
    }

    @Override // com.unisound.sdk.au
    public void a(boolean z, byte[] bArr, int i, int i2) {
        if (this.n != null) {
            this.n.a(bArr);
        }
    }

    @Override // com.unisound.common.ab
    public boolean a(Message message) {
        return true;
    }

    public int b() {
        return this.q.a();
    }

    @Override // com.unisound.sdk.cr
    public void b(int i) {
        if (this.i != null) {
            this.i.a(i);
        }
    }

    @Override // com.unisound.sdk.ak
    public void b(String str) {
        c(str);
    }

    @Override // com.unisound.sdk.au
    public void b(boolean z) {
        if (z) {
            if (this.i != null) {
                this.i.d();
            }
        } else {
            f(ErrorCode.FAILED_START_RECORDING);
            com.unisound.common.y.c("startRecognition Error:cancelRecognition()");
            d(true);
        }
    }

    @Override // com.unisound.sdk.cr
    public void b(boolean z, byte[] bArr, int i, int i2) {
        if (!this.j.A() || this.j.B()) {
            if (this.g != null && z) {
                this.g.a(bArr);
            }
            ad adVar = this.i;
            if (adVar != null) {
                adVar.a(z, bArr, i, i2);
            }
        }
    }

    public void c() {
        if (this.g != null) {
            this.g.start();
        }
    }

    public void c(int i) {
        if (i > f300a) {
            i = f300a;
        } else if (i < b) {
            i = b;
        }
        this.q.a(i);
    }

    protected void c(String str) {
        ay ayVar = this.e;
        if (ayVar != null) {
            ayVar.a(str);
        }
    }

    public void c(boolean z) {
        this.e = null;
        this.l = true;
        this.q.e();
        t();
        d(z);
        com.unisound.common.y.c("Recognizer: cancelRecognition()");
        removeSendMessage();
    }

    public void d() {
        r();
        s();
    }

    public void d(int i) {
        this.j.bd = i;
    }

    protected void e() {
        if (this.i != null) {
            this.i.g();
        }
    }

    @Override // com.unisound.sdk.cq
    public void e(int i) {
        if (this.i != null) {
            this.i.c(i);
        }
    }

    public String f() {
        return this.k;
    }

    public boolean g() {
        return this.l;
    }

    public com.unisound.common.au h() {
        return this.d;
    }

    @Override // com.unisound.sdk.ak
    public void i() {
        f(0);
    }

    @Override // com.unisound.sdk.au
    public void j() {
        this.h = null;
        s();
        if (this.i != null) {
            this.i.f();
        }
    }

    @Override // com.unisound.sdk.au
    public void k() {
        d(true);
        f(ErrorCode.RECORDING_EXCEPTION);
    }

    @Override // com.unisound.sdk.ak
    public void k(int i) {
        if (this.i != null) {
            this.i.h();
        }
    }

    @Override // com.unisound.sdk.ak
    public void l() {
    }

    @Override // com.unisound.sdk.ak
    public void m() {
        c(true);
        com.unisound.common.y.c("max_speech_timeout cancel()");
        f(ErrorCode.ASRCLIENT_MAX_SPEECH_TIMEOUT);
    }

    @Override // com.unisound.sdk.cr
    public void n() {
        e();
    }

    @Override // com.unisound.sdk.cr
    public void o() {
    }

    public com.unisound.common.av p() {
        return this.c;
    }

    public String q() {
        return this.g != null ? this.g.h() : "";
    }
}
