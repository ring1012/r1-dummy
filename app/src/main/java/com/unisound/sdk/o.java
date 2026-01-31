package com.unisound.sdk;

import android.content.Context;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import cn.yunzhisheng.asr.VAD;
import cn.yunzhisheng.asrfix.JniAsrFix;
import cn.yunzhisheng.nlu.OfflineNlu;
import com.unisound.client.IAudioSource;
import java.io.IOException;
import java.util.List;

/* loaded from: classes.dex */
public class o {
    public static final int OPT_SET_FIX_RESULT_NLU = 5;
    public static final int OPT_SET_FIX_RESULT_NLU_CONFIGPATH = 6;
    public static final int SET_USER_DATA_ERROR = -100;
    public static final int SET_USER_DATA_OK = 0;
    public static final int SET_USER_DATA_WARNING = -200;
    protected w b;
    protected m e;
    protected com.unisound.common.ar g;
    protected Context l;
    public Looper mLooper;
    public br mSpeechUnderstanderParams;
    private t v;
    private boolean o = false;
    private boolean p = true;

    /* renamed from: a, reason: collision with root package name */
    protected l f349a = new l();
    protected y c = null;
    protected aa d = null;
    private com.unisound.common.g q = new com.unisound.common.g();
    protected ab f = new ab();
    protected n h = new n();
    protected a i = new a();
    protected cs j = new cs();
    protected av k = new av();
    private boolean r = false;
    protected float m = -8.0f;
    private String s = "";
    private IAudioSource t = null;
    private boolean u = false;
    private com.unisound.common.d w = new q(this);
    private aj x = new r(this);
    protected HandlerThread n = new HandlerThread("ht_NetAndFix");

    protected o(Context context, String str) {
        this.b = new w(context);
        this.n.start();
        this.mLooper = this.n.getLooper();
        this.e = new m(this.mLooper, context);
        this.i.a(this.w);
        this.l = context;
        this.mSpeechUnderstanderParams = new br();
        this.f.a(context.getApplicationContext().getFilesDir() + "/YunZhiSheng/asrfix");
        this.f349a.c(context.getApplicationContext().getFilesDir() + "/YunZhiSheng/");
        this.q.a(context);
        this.b.f(false);
        this.b.a(3000, 1000);
        this.b.o(str);
        this.e.a(this.b);
        this.v = new t(this, null);
        this.e.a(this.v);
        ab.m = 1;
        a(new p(this));
    }

    protected static boolean b(String str) {
        return ab.d(str);
    }

    protected int a(String str, String str2, String str3) {
        return this.e.b(str, str2, str3);
    }

    protected int a(String str, String str2, String str3, String str4) {
        if (this.f.a(this.l)) {
            return this.f.a(str, str2, str3, str4);
        }
        return -100;
    }

    protected as a() {
        return this.b;
    }

    protected String a(String str, String str2) {
        return this.e.a(str, str2);
    }

    protected void a(int i) {
    }

    protected void a(int i, int i2) {
        this.b.a(i, i2);
    }

    protected void a(int i, int i2, Object obj) {
        if (this.g != null) {
            this.g.a(i, i2, obj);
        }
    }

    protected void a(VAD vad) {
        this.h.c();
    }

    protected void a(com.unisound.common.ah ahVar) {
        this.k.a(ahVar);
    }

    protected void a(com.unisound.common.b bVar) {
        this.i.a(bVar);
    }

    protected void a(v vVar) {
        this.h.a(vVar);
    }

    protected void a(String str, boolean z, int i, int i2) {
    }

    protected void a(String str, boolean z, int i, long j, long j2, int i2, int i3) {
    }

    protected void a(boolean z, byte[] bArr, int i, int i2) {
    }

    protected boolean a(Context context) {
        return com.unisound.common.h.a(context);
    }

    protected boolean a(Message message) {
        return false;
    }

    protected boolean a(String str) {
        return false;
    }

    protected boolean a(String str, boolean z) throws IOException {
        this.f.a((str == null || str.length() == 0) ? this.l.getApplicationContext().getFilesDir() + "/YunZhiSheng/asrfix" : str + "/YunZhiSheng/asrfix");
        this.f.a(this.l, ab.f);
        if (this.f.a(this.l, z)) {
            return true;
        }
        com.unisound.common.y.a("USCFixRecognizer.initByModelDir init data fail!");
        return false;
    }

    protected boolean a(boolean z) {
        return a((String) null, z);
    }

    protected int b(String str, String str2) {
        return !this.e.a(this.f.f293a, str, str2) ? -1 : 0;
    }

    protected int b(boolean z) {
        return this.e.e(z);
    }

    protected void b() {
        this.v = new t(this, null);
        this.e.a(this.x);
        this.e.a(this.v);
        this.e.a(this.b);
    }

    protected void b(int i) {
        if (i != 0) {
            this.e.a(false);
        }
    }

    protected void b(int i, int i2) {
    }

    protected void b(String str, String str2, String str3) {
        this.e.a(this.f.f293a, str, str2, str3);
    }

    protected boolean b(Context context) {
        return com.unisound.common.h.c(context);
    }

    protected int c(boolean z) {
        return this.e.f(z);
    }

    protected void c(int i) {
        this.h.a(i);
    }

    protected void c(int i, int i2) {
    }

    protected boolean c() {
        return this.e.r();
    }

    protected void cancel() {
        this.v = null;
        this.e.a((ar) null);
        this.e.a(true);
    }

    protected void d(int i) {
    }

    protected boolean d() {
        return this.e.q();
    }

    protected int e(int i) {
        return this.e.c(i);
    }

    protected boolean e() {
        return this.f.c();
    }

    protected int f(int i) {
        return this.e.d(i);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void f() {
        if (this.e != null) {
            this.e.g();
        }
    }

    protected int g() {
        return 0;
    }

    protected int g(int i) {
        return this.e.e(i);
    }

    protected String getFixEngineVersion() {
        return JniAsrFix.getVersion();
    }

    protected Object getOption(int i) {
        if (1055 == i) {
            return Boolean.valueOf(this.b.y());
        }
        if (1056 == i) {
            return Boolean.valueOf(this.b.j());
        }
        if (1010 == i) {
            return Integer.valueOf(this.b.u());
        }
        if (1011 == i) {
            return Integer.valueOf(this.b.v());
        }
        if (1015 == i) {
            return Boolean.valueOf(this.b.P());
        }
        if (1016 == i) {
            return Float.valueOf(this.b.Q());
        }
        return null;
    }

    protected String getVersion() {
        return com.unisound.common.an.a();
    }

    protected int h(int i) {
        return this.e.f(i);
    }

    protected void h() {
    }

    protected int i(int i) {
        return this.e.g(i);
    }

    protected void i() {
    }

    protected int j(int i) {
        return this.e.h(i);
    }

    protected void j() {
    }

    protected int k(int i) {
        return this.e.i(i);
    }

    protected boolean k() {
        return false;
    }

    protected void l() {
    }

    protected void m() {
    }

    protected void n() {
        if (this.b.l()) {
            this.q.a();
        }
        this.h.b();
    }

    protected void o() {
    }

    protected void p() {
    }

    public void postRecordingStartStatus() {
    }

    protected void q() {
        this.f.f();
        this.e.p();
    }

    protected void r() throws IOException {
        com.unisound.common.y.c("FixRecognizerInterFace : createJniAsrFix");
        this.f.a(this.l, ab.f);
        this.e.s();
        this.e.a(new s(this));
    }

    protected int s() {
        return this.e.t();
    }

    protected int setAudioSource(IAudioSource iAudioSource) {
        this.t = iAudioSource;
        if (this.t != null) {
            return 0;
        }
        this.t = new com.unisound.common.e(this.b);
        return 0;
    }

    protected void setOption(int i, Object obj) {
        if (1051 == i) {
            try {
                this.f349a.b = ((Boolean) obj).booleanValue();
                return;
            } catch (Exception e) {
                com.unisound.common.y.a("set asr_result_filter Error.");
                return;
            }
        }
        if (1053 == i) {
            try {
                this.p = ((Boolean) obj).booleanValue();
                return;
            } catch (Exception e2) {
                com.unisound.common.y.a("set asr_recording_enabled Error.");
                return;
            }
        }
        if (1054 == i) {
            try {
                com.unisound.common.y.k = ((Boolean) obj).booleanValue();
                com.unisound.c.a.b(((Boolean) obj).booleanValue());
                return;
            } catch (Exception e3) {
                com.unisound.common.y.a("set asr_print_log Error.");
                return;
            }
        }
        if (1063 == i) {
            try {
                com.unisound.common.y.m = ((Boolean) obj).booleanValue();
                return;
            } catch (Exception e4) {
                com.unisound.common.y.a("set asr_print_log Error.");
                return;
            }
        }
        if (1055 != i) {
            if (1058 == i) {
                try {
                    this.b.a((String) obj);
                    return;
                } catch (Exception e5) {
                    com.unisound.common.y.a("set asr_save_recording_data Error.");
                    return;
                }
            }
            if (1059 == i) {
                try {
                    this.f349a.c = ((Boolean) obj).booleanValue();
                    return;
                } catch (Exception e6) {
                    com.unisound.common.y.a("set asr_result_json Error.");
                    return;
                }
            }
            if (1010 == i) {
                try {
                    this.b.d(((Integer) obj).intValue());
                    return;
                } catch (Exception e7) {
                    com.unisound.common.y.a("set asr_vad_timeout_frontsil Error.");
                    return;
                }
            }
            if (1011 == i) {
                try {
                    a(this.b.u(), ((Integer) obj).intValue());
                    this.b.m(((Integer) obj).intValue());
                    this.b.e(((Integer) obj).intValue());
                    return;
                } catch (Exception e8) {
                    com.unisound.common.y.a("set asr_vad_timeout_backsil Error.");
                    return;
                }
            }
            if (5 == i) {
                try {
                    this.f349a.f = ((Boolean) obj).booleanValue();
                    if (this.f349a.f && this.f349a.h == null) {
                        this.f349a.h = new OfflineNlu();
                        return;
                    }
                    return;
                } catch (Exception e9) {
                    com.unisound.common.y.a("set asr_fix_result_nlu Error.");
                    return;
                }
            }
            if (6 == i) {
                try {
                    this.s = (String) obj;
                    this.f349a.g = (String) obj;
                    if (this.f349a.h == null || this.s.equals("")) {
                        return;
                    }
                    this.f349a.h.b(this.s, "");
                    return;
                } catch (Exception e10) {
                    com.unisound.common.y.a("set asr_fix_result_nlu_configpath Error.");
                    return;
                }
            }
            if (1062 == i) {
                try {
                    this.b.D(((Boolean) obj).booleanValue());
                    this.e.a((Boolean) obj);
                    return;
                } catch (Exception e11) {
                    com.unisound.common.y.a("set asr_print_engine_log Error.");
                    return;
                }
            }
            if (5000 == i) {
                try {
                    this.b.c(((Boolean) obj).booleanValue());
                    return;
                } catch (Exception e12) {
                    com.unisound.common.y.a("set setFarFeildEnabled Error. 5000 ");
                    return;
                }
            }
            if (5001 == i) {
                try {
                    this.b.y.a(((Float) obj).floatValue());
                    return;
                } catch (Exception e13) {
                    com.unisound.common.y.a("set min back energy Error. 5001 ");
                    return;
                }
            }
            if (5002 == i) {
                try {
                    this.b.z.a(((Float) obj).floatValue());
                    return;
                } catch (Exception e14) {
                    com.unisound.common.y.a("set min back energy higher TH Error. 5002 ");
                    return;
                }
            }
            if (5003 == i) {
                try {
                    this.b.A.a(((Float) obj).floatValue());
                    return;
                } catch (Exception e15) {
                    com.unisound.common.y.a("set pitch threshold Error. 5003 ");
                    return;
                }
            }
            if (5004 == i) {
                try {
                    this.b.B.a(((Integer) obj).intValue());
                    return;
                } catch (Exception e16) {
                    com.unisound.common.y.a("set pitch persist length for start usage Error. 5004 ");
                    return;
                }
            }
            if (5005 == i) {
                try {
                    this.b.C.a(((Integer) obj).intValue());
                    return;
                } catch (Exception e17) {
                    com.unisound.common.y.a("set pitch drop length for end usage Error. 5005 ");
                    return;
                }
            }
            if (5006 == i) {
                try {
                    this.b.D.a(((Float) obj).floatValue());
                    return;
                } catch (Exception e18) {
                    com.unisound.common.y.a("set high freq energy vs low freq energy Error. 5006 ");
                    return;
                }
            }
            if (5007 == i) {
                try {
                    this.b.E.a(((Integer) obj).intValue());
                    return;
                } catch (Exception e19) {
                    com.unisound.common.y.a("set min signal length for speech Error. 5007 ");
                    return;
                }
            }
            if (5008 == i) {
                try {
                    this.b.F.a(((Integer) obj).intValue());
                    return;
                } catch (Exception e20) {
                    com.unisound.common.y.a("set max silence length Error. 5008 ");
                    return;
                }
            }
            if (5009 == i) {
                try {
                    this.b.G.a(((Float) obj).floatValue());
                    return;
                } catch (Exception e21) {
                    com.unisound.common.y.a("set max single point max in spectral Error. 5009 ");
                    return;
                }
            }
            if (5010 == i) {
                try {
                    this.b.H.a(((Float) obj).floatValue());
                    return;
                } catch (Exception e22) {
                    com.unisound.common.y.a("set gloable noise to signal value threshold Error. 5010 ");
                    return;
                }
            }
            if (5011 == i) {
                try {
                    this.b.I.a(((Float) obj).floatValue());
                    return;
                } catch (Exception e23) {
                    com.unisound.common.y.a("set gloable noise to signal value threshold for vowel part Error. 5011 ");
                    return;
                }
            }
            if (5012 == i) {
                try {
                    this.b.J.a(((Float) obj).floatValue());
                    return;
                } catch (Exception e24) {
                    com.unisound.common.y.a("set voice freq domain prob Th Error. 5012 ");
                    return;
                }
            }
            if (5013 == i) {
                try {
                    this.b.K.a(((Integer) obj).intValue());
                    return;
                } catch (Exception e25) {
                    com.unisound.common.y.a("set use pitch or peak Error. 5013 ");
                    return;
                }
            }
            if (5014 == i) {
                try {
                    this.b.L.a(((Integer) obj).intValue());
                    return;
                } catch (Exception e26) {
                    com.unisound.common.y.a("set noise to y ratio, start point in freq domain Error. 5014 ");
                    return;
                }
            }
            if (5017 == i) {
                try {
                    this.b.M.a(((Integer) obj).intValue());
                    return;
                } catch (Exception e27) {
                    com.unisound.common.y.a("set PITCHLASTTH Error. 5017 ");
                    return;
                }
            }
            if (5021 == i) {
                try {
                    this.b.n((String) obj);
                    return;
                } catch (Exception e28) {
                    com.unisound.common.y.a("set activate info Error.");
                    return;
                }
            }
            if (1016 == i) {
                try {
                    this.b.b(((Float) obj).floatValue());
                } catch (Exception e29) {
                    com.unisound.common.y.a("set vad musicth info Error!");
                }
            } else if (1015 == i) {
                try {
                    this.b.m(((Boolean) obj).booleanValue());
                } catch (Exception e30) {
                    com.unisound.common.y.a("set vad detectMusic Error!");
                }
            } else if (20120629 == i) {
                try {
                    com.unisound.common.y.l = ((Boolean) obj).booleanValue();
                } catch (Exception e31) {
                    com.unisound.common.y.a("set activate info Error.");
                }
            }
        }
    }

    protected void start() {
        start(this.f.e);
    }

    protected void start(String str) {
        b();
        this.c = null;
        this.d = null;
        if (this.p) {
            ax.n();
            if (this.t == null) {
                this.t = new com.unisound.common.e(this.b);
            }
            com.unisound.common.y.g("FixRecognizerInterface recognizer start");
            this.e.a(str, new ax(this.b, this.e, this.t), new aa(this.l, this.b, this.e));
        }
        this.b.b(com.unisound.common.j.a(this.b.c()));
        if (this.b.b()) {
            this.r = com.unisound.common.j.b(this.b.c());
        }
    }

    protected void stop() {
        this.c = null;
        this.e.b();
        if (this.p) {
            return;
        }
        this.e.g();
    }

    protected List<Integer> t() {
        return this.e.u();
    }

    protected int u() {
        return this.e.v();
    }

    protected String v() {
        return this.e.w();
    }

    protected String w() {
        return this.e.x();
    }

    protected int x() {
        return this.e.y();
    }

    protected int y() {
        return this.e.z();
    }
}
