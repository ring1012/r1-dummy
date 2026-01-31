package com.unisound.sdk;

import android.text.TextUtils;
import com.unisound.client.SpeechConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class bw {
    public static final boolean d = false;
    public static final int e = 0;
    public static final int f = 300;
    public static final int g = 50;
    public static final int h = 50;
    public static final int i = 50;
    public static final String j = "xiaoli";
    public static final int k = 22050;
    public static final boolean l = false;
    public static final int m = 100;
    public static final int n = 100;
    public static final int o = 2;
    public static final int p = 15;
    public static final int q = 5;
    public static final int r = 50;
    public static final int s = -1;
    public static final boolean t = false;
    public static final boolean u = false;
    private static bw w;
    private String H;
    private String I;
    private String y;
    private com.unisound.common.a v = new com.unisound.common.a("ttsv3.hivoice.cn", 80, "117.121.49.41", 80);

    /* renamed from: a, reason: collision with root package name */
    public int f325a = 0;
    public String b = null;
    public c c = new c();
    private Boolean x = false;
    private int z = 300;
    private int A = 50;
    private int B = 50;
    private int C = 50;
    private String D = j;
    private int E = k;
    private List<Integer> F = new ArrayList();
    private int G = 3;
    private Integer J = 2;
    private int K = 0;
    private Boolean L = false;
    private int M = 100;
    private int N = 100;
    private int O = 15;
    private int P = 5;
    private int Q = 50;
    private int R = -1;
    private boolean S = false;
    private boolean T = false;
    private boolean U = false;
    private boolean V = false;
    private String W = "";
    private String X = "";
    private String Y = "";
    private String Z = "";
    private long aa = 500;

    private bw() {
    }

    private boolean F(Object obj) {
        if (obj == null) {
            com.unisound.common.y.a("TTSParams object2Boolean: obj is null");
            return false;
        }
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue();
        }
        com.unisound.common.y.a("TTSParams object2Boolean: obj is not change to boolean");
        return false;
    }

    private int a(Integer num) {
        int iIntValue = Integer.valueOf(num.intValue()).intValue();
        if (iIntValue < 0) {
            com.unisound.common.y.e("TTSParams checkParams: value < 0");
            return 1;
        }
        if (iIntValue <= 100) {
            return iIntValue;
        }
        com.unisound.common.y.e("TTSParams checkParams: value > 100");
        return 100;
    }

    public static bw a() {
        if (w == null) {
            w = new bw();
        }
        return w;
    }

    private Integer b(int i2) {
        int i3 = 1000;
        int i4 = 0;
        if (i2 > 1000) {
            com.unisound.common.y.e("TTSParams checkSilenceTime: silence > 1000 invoked silence = 1000");
        } else {
            i3 = i2;
        }
        if (i3 < 0) {
            com.unisound.common.y.e("TTSParams checkSilenceTime: silence < 0 invoked silence = 0");
        } else {
            i4 = i3;
        }
        return Integer.valueOf(i4);
    }

    private boolean d(String str) {
        return this.v.c(str);
    }

    private boolean e(String str) {
        if (!TextUtils.isEmpty(str) && TextUtils.isDigitsOnly(str)) {
            return true;
        }
        com.unisound.common.y.a("TTSParams isNumberUseable: value is empty or unusable");
        return false;
    }

    private boolean f(String str) {
        if (!TextUtils.isEmpty(str)) {
            return true;
        }
        com.unisound.common.y.a("TTSParams isStringUseable: value is empty");
        return false;
    }

    private boolean g(String str) {
        String[] strArrSplit = str.split(":");
        if (strArrSplit.length != 2) {
            return false;
        }
        if (i(strArrSplit[1])) {
            return true;
        }
        com.unisound.common.y.a("TTSParams checkAddress: port unusable");
        return false;
    }

    private boolean h(String str) {
        return Pattern.compile("(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})").matcher(str).matches();
    }

    private boolean i(String str) {
        try {
            int iIntValue = Integer.valueOf(str).intValue();
            if (iIntValue > 0 && iIntValue < 65535) {
                return true;
            }
            com.unisound.common.y.a("TTSParams checkPort: port Illegal");
            return false;
        } catch (Exception e2) {
            e2.printStackTrace();
            com.unisound.common.y.a("TTSParams checkPort: port changeTo integer exception");
            return false;
        }
    }

    public int A() {
        return this.G;
    }

    public void A(Object obj) {
        this.P = w(obj).intValue();
    }

    public String B() {
        return this.H;
    }

    public void B(Object obj) {
        this.V = F(obj);
    }

    public String C() {
        return this.I;
    }

    public void C(Object obj) {
        this.Q = a(w(obj));
    }

    public Integer D() {
        return this.J;
    }

    public void D(Object obj) {
        this.R = a(w(obj));
    }

    public Map<String, Integer> E() {
        HashMap map = new HashMap();
        map.put(SpeechConstants.TTS_KEY_VOICE_SPEED_JSONKEY, 2001);
        map.put(SpeechConstants.TTS_KEY_VOICE_PITCH_JSONKEY, 2002);
        map.put(SpeechConstants.TTS_KEY_VOICE_VOLUME_JSONKEY, 2003);
        map.put(SpeechConstants.TTS_KEY_SAMPLE_RATE_JSONKEY, 2004);
        map.put(SpeechConstants.TTS_KEY_VOICE_NAME_JSONKEY, 2005);
        map.put(SpeechConstants.TTS_KEY_SERVER_ADDR_JSONKEY, 2011);
        map.put(SpeechConstants.TTS_KEY_PLAY_START_BUFFER_TIME_JSONKEY, 2012);
        map.put(SpeechConstants.TTS_KEY_STREAM_TYPE_JSONKEY, 2013);
        map.put(SpeechConstants.TTS_KEY_IS_DEBUG_JSONKEY, 2014);
        map.put(SpeechConstants.TTS_SERVICE_MODE_JSONKEY, 2020);
        return map;
    }

    public void E(Object obj) {
        int iIntValue = w(obj).intValue();
        this.aa = iIntValue <= 1000 ? iIntValue < 100 ? 100 : iIntValue : 1000;
    }

    public int F() {
        return this.O;
    }

    public boolean G() {
        return this.U;
    }

    public int H() {
        return this.P;
    }

    public boolean I() {
        return this.V;
    }

    public int J() {
        return this.Q;
    }

    public int K() {
        return this.R;
    }

    public long L() {
        return this.aa;
    }

    public Object a(int i2) {
        return null;
    }

    public void a(Object obj) {
        this.X = r(obj);
    }

    public void a(String str) {
        this.b = str;
    }

    public void a(String str, int i2) {
        this.v.a(str);
        this.v.a(i2);
        this.v.b(str);
        this.v.b(i2);
    }

    public boolean a(int i2, Object obj) {
        return false;
    }

    public c b() {
        return this.c;
    }

    public void b(Object obj) {
        this.W = r(obj);
    }

    public void b(String str) {
        this.H = str;
    }

    public String c() {
        return this.v.a();
    }

    public void c(Object obj) {
        this.Y = r(obj);
    }

    public void c(String str) {
        this.I = str;
    }

    public int d() {
        return this.v.c();
    }

    public void d(Object obj) {
        this.Z = r(obj);
    }

    public void e() {
        this.v.e();
    }

    public void e(Object obj) {
        this.T = F(obj);
    }

    public com.unisound.common.a f() {
        return this.v;
    }

    public void f(Object obj) {
        this.S = F(obj);
    }

    public String g() {
        return this.X;
    }

    public void g(Object obj) {
        this.L = Boolean.valueOf(F(obj));
    }

    public String h() {
        return this.W;
    }

    public void h(Object obj) {
        this.M = b(w(obj).intValue()).intValue();
    }

    public String i() {
        return this.Y;
    }

    public void i(Object obj) {
        this.N = b(w(obj).intValue()).intValue();
    }

    public String j() {
        return this.Z;
    }

    public void j(Object obj) {
        this.K = w(obj).intValue();
    }

    public String k() {
        return this.b;
    }

    public void k(Object obj) {
        this.x = Boolean.valueOf(F(obj));
    }

    public void l(Object obj) {
        String strR = r(obj);
        if (strR == null) {
            com.unisound.common.y.a("TTSParams setDebugDir: mDebugDir is null");
        } else {
            this.y = strR;
        }
    }

    public boolean l() {
        return this.T;
    }

    public void m(Object obj) {
        String strR = r(obj);
        if (strR == null) {
            com.unisound.common.y.a("TTSParams setVoiceName: voiceName is null");
        } else if (f(strR)) {
            this.D = strR;
        } else {
            com.unisound.common.y.a("TTSParams setVoiceName: voiceName unusable");
        }
    }

    public boolean m() {
        return this.S;
    }

    public Boolean n() {
        return this.L;
    }

    public void n(Object obj) {
        this.A = a(w(obj));
    }

    public int o() {
        return this.M;
    }

    public void o(Object obj) {
        this.B = a(w(obj));
    }

    public int p() {
        return this.N;
    }

    public void p(Object obj) {
        this.C = a(w(obj));
    }

    public int q() {
        return this.K;
    }

    public void q(Object obj) {
        Integer numW = w(obj);
        if (numW.intValue() > 15000) {
            numW = 15000;
            com.unisound.common.y.e("TTSParams setPlayStartBufferTime: playStartBufferTime > 15000 ", Integer.valueOf(numW.intValue()));
        }
        if (numW.intValue() <= 0) {
            numW = 1;
            com.unisound.common.y.e("TTSParams setPlayStartBufferTime: playStartBufferTime < 0 ", Integer.valueOf(numW.intValue()));
        }
        this.z = numW.intValue();
    }

    public Boolean r() {
        return this.x;
    }

    public String r(Object obj) {
        if (obj == null) {
            com.unisound.common.y.a("TTSParams object2String: obj is null");
            return null;
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        com.unisound.common.y.a("TTSParams object2String: obj can not change to String");
        return null;
    }

    public String s() {
        return this.y;
    }

    public void s(Object obj) {
        String strR = r(obj);
        if (strR == null) {
            com.unisound.common.y.a("TTSParams setSampleRate: mSampleRate is null");
        }
        if (!e(strR)) {
            com.unisound.common.y.a("TTSParams setSampleRate: mSampleRate unusable");
            return;
        }
        int iIntValue = Integer.valueOf(strR).intValue();
        switch (iIntValue) {
            case 16000:
                this.E = iIntValue;
                break;
            case k /* 22050 */:
                this.E = iIntValue;
                break;
            case 24000:
                this.E = iIntValue;
                break;
            case 48000:
                this.E = iIntValue;
                break;
        }
    }

    public String t() {
        return this.D;
    }

    public void t(Object obj) {
        String strR = r(obj);
        if (strR == null) {
            com.unisound.common.y.a("TTSParams addField: value is null");
            return;
        }
        try {
            this.F.add(Integer.valueOf(Integer.valueOf(strR).intValue()));
        } catch (Exception e2) {
            e2.printStackTrace();
            com.unisound.common.y.a("TTSParams addField: value can not change to integer");
        }
    }

    public int u() {
        return this.A;
    }

    public boolean u(Object obj) {
        String strR = r(obj);
        if (g(strR)) {
            return d(strR);
        }
        com.unisound.common.y.a("TTSParams setServerAddress: address unusable");
        return false;
    }

    public int v() {
        return this.B;
    }

    public void v(Object obj) {
        Integer numW = w(obj);
        if (numW.intValue() < 0 || numW.intValue() > 8) {
            com.unisound.common.y.a("TTSParams setStreamType: mStreamType unusable");
        } else {
            this.G = numW.intValue();
        }
    }

    public int w() {
        return this.C;
    }

    public Integer w(Object obj) {
        if (obj == null) {
            com.unisound.common.y.a("TTSParams object2Integer: obj is null");
            return 1;
        }
        if (obj instanceof Integer) {
            return (Integer) obj;
        }
        com.unisound.common.y.a("TTSParams object2Integer: obj can not change to integer");
        return 1;
    }

    public int x() {
        return this.z;
    }

    public void x(Object obj) {
        Integer numW = w(obj);
        if (numW.intValue() == 1 || numW.intValue() == 2 || numW.intValue() == 3) {
            this.J = numW;
        } else {
            com.unisound.common.y.a("TTSParams setMode: mMode unusable");
        }
    }

    public int y() {
        return this.E;
    }

    public void y(Object obj) {
        int iIntValue = w(obj).intValue();
        this.O = iIntValue <= 30 ? iIntValue <= 0 ? 1 : iIntValue : 30;
    }

    public List<Integer> z() {
        return this.F;
    }

    public void z(Object obj) {
        this.U = F(obj);
    }
}
