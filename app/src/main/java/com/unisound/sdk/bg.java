package com.unisound.sdk;

import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import cn.yunzhisheng.asr.JniUscClient;
import cn.yunzhisheng.asr.VAD;
import cn.yunzhisheng.common.PinyinConverter;
import com.unisound.client.ErrorCode;
import com.unisound.client.IAudioSource;
import com.unisound.client.SpeechConstants;
import com.unisound.client.SpeechUnderstanderListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class bg extends o {
    private static final int O = 1;
    private static final int P = 5;
    private static final int Q = 6;
    private static final int R = 7;
    private static final int S = 11;
    private static final int T = 12;
    private static final int U = 13;
    private static final int V = 14;
    private static final int W = 15;
    private static final int X = 16;
    private static final int Y = 17;
    private static final int Z = 18;
    private static String aA = "4bd9354d1cf247c93db388257567d0e2";
    private static final int aG = 0;
    private static final int aH = 1;
    private static final int aI = 2;
    private static final int aJ = 3;
    private static final int aa = 20;
    private static final int ab = 21;
    private static final int ac = 22;
    private static final int ad = 23;
    private static final int ae = 24;
    private static final int af = 25;
    private static final int ag = 30;
    private static final int ah = 40;
    private static final int ai = 50;
    private cl A;
    private y B;
    private boolean C;
    private boolean D;
    private String E;
    private int F;
    private int G;
    private aw H;
    private String I;
    private String J;
    private int K;
    private boolean L;
    private boolean M;
    private boolean N;
    private String aB;
    private ac aC;
    private com.unisound.common.ag aD;
    private boolean aE;
    private ad aF;
    private com.unisound.b.a.a aK;
    private Handler aL;
    private Runnable aM;
    private com.unisound.common.ao aj;
    private Context ak;
    private HandlerThread al;
    private Handler am;
    private Handler an;
    private int ao;
    private int ap;
    private boolean aq;
    private String ar;
    private boolean as;
    private boolean at;
    private boolean au;
    private boolean av;
    private Object aw;
    private h ax;
    private g ay;
    private com.unisound.common.ba az;
    protected ao o;
    protected cw p;
    protected final int q;
    protected final int r;
    protected final int s;
    protected final int t;
    ArrayList<String> u;
    ArrayList<String> v;
    ArrayList<String> w;
    String x;
    String y;
    private SpeechUnderstanderListener z;

    protected bg(Context context, String str, String str2) {
        super(context, str);
        this.B = null;
        this.C = true;
        this.D = true;
        this.E = "";
        this.F = 1;
        this.G = -1;
        this.H = new aw();
        this.p = new cw();
        this.I = "main";
        this.J = "wakeup";
        this.q = 51;
        this.r = 52;
        this.s = 53;
        this.t = 54;
        this.K = 0;
        this.L = false;
        this.M = false;
        this.N = false;
        this.ap = 5;
        this.aq = false;
        this.ar = "";
        this.as = false;
        this.at = false;
        this.au = false;
        this.av = false;
        this.aw = new Object();
        this.aB = "";
        this.aC = new bh(this);
        this.aE = false;
        this.aF = new bk(this);
        this.aM = new bn(this);
        this.az = com.unisound.common.ba.a();
        this.ak = context;
        this.x = str;
        this.y = str2;
        this.aD = new com.unisound.common.ag(context);
        this.p.a(this.f.f293a);
        this.f.a(this.aC);
        this.A = this.b.aX();
        this.A.b(str);
        this.A.c(str2);
        this.b.o(str);
        this.b.O(1);
        this.o = new ao(this.b, this.mLooper);
        this.L = false;
        this.u = new ArrayList<>();
        this.v = new ArrayList<>();
        this.w = new ArrayList<>();
        this.aj = new com.unisound.common.ao(new bi(this), this.mLooper);
        this.aj.c();
        this.ax = new h(this);
        this.ay = this.f.h();
    }

    private void C() {
        if (this.aD.a(SpeechConstants.PERMISSION_RECORD_AUDIO) != 0) {
            com.unisound.common.y.a("no RECORD_AUDIO permission");
            if (this.z != null) {
                this.z.onEvent(SpeechConstants.GENERAL_EVENT_REQUIRE_RECORD_AUDIO_PERMISSION, (int) System.currentTimeMillis());
            }
        }
        if (this.aD.a(SpeechConstants.PERMISSION_READ_CONTACTS) != 0) {
            com.unisound.common.y.a("no READ_CONTACTS permission");
            if (this.z != null) {
                this.z.onEvent(SpeechConstants.GENERAL_EVENT_REQUIRE_READ_CONTACTS_PERMISSION, (int) System.currentTimeMillis());
            }
        }
        if (this.aD.a(SpeechConstants.PERMISSION_ACCESS_FINE_LOCATION) != 0) {
            com.unisound.common.y.a("no ACCESS_FINE_LOCATION permission");
            if (this.z != null) {
                this.z.onEvent(SpeechConstants.GENERAL_EVENT_REQUIRE_ACCESS_FINE_LOCATION_PERMISSION, (int) System.currentTimeMillis());
            }
        }
        if (this.aD.a(SpeechConstants.PERMISSION_READ_PHONE_STATE) != 0) {
            com.unisound.common.y.a("no READ_PHONE_STATE permission");
            if (this.z != null) {
                this.z.onEvent(1151, (int) System.currentTimeMillis());
            }
        }
        if (this.aD.a(SpeechConstants.PERMISSION_WRITE_EXTERNAL_STORAGE) != 0) {
            com.unisound.common.y.a("no WRITE_EXTERNAL_STORAGE permission");
            if (this.z != null) {
                this.z.onEvent(SpeechConstants.GENERAL_EVENT_REQUIRE_WRITE_EXTERNAL_STORAGE_PERMISSION, (int) System.currentTimeMillis());
            }
        }
    }

    private boolean D() {
        return this.A.y();
    }

    private int E() {
        return this.A.z();
    }

    private boolean F() {
        return this.C;
    }

    private boolean G() {
        return this.D;
    }

    private com.unisound.common.av H() {
        return this.o.p();
    }

    private com.unisound.common.au I() {
        return this.o.h();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void J() {
        if (F() && G()) {
            if (this.F == 0) {
                this.am.obtainMessage(1210, com.unisound.common.v.a(this.u, this.v, this.w)).sendToTarget();
            } else if (this.F == 2 && !this.I.equals(this.J)) {
                this.am.obtainMessage(1210, com.unisound.common.v.a(this.u, (ArrayList<String>) null, (ArrayList<String>) null)).sendToTarget();
            } else if (this.F == 1) {
                this.am.obtainMessage(1210, com.unisound.common.v.a((ArrayList<String>) null, this.v, this.w)).sendToTarget();
            }
            this.au = true;
            stop();
            if (this.av) {
                this.am.sendEmptyMessage(5);
            }
        }
    }

    private void K() {
        if (this.v.size() == 0) {
            this.v.add(com.unisound.common.v.a(this.F, "full", "", "", true, null, null, null, null, null, null));
        }
        if (this.A.w() && this.w.size() == 0) {
            this.w.add(com.unisound.common.v.a(-1, null, "", null, null, null, null, null, null, null, null));
        }
        if (this.u.size() == 0) {
            this.u.add(com.unisound.common.v.a(this.F, "full", "", null, null, Float.valueOf(-20.0f), null, null, null, null, null));
        }
    }

    private int L() {
        if (this.L) {
            return 0;
        }
        com.unisound.common.y.a("init error " + ErrorCode.toJsonMessage(ErrorCode.GENERAL_INIT_ERROR));
        if (this.z != null) {
            this.z.onError(1300, ErrorCode.toJsonMessage(ErrorCode.GENERAL_INIT_ERROR));
        }
        return -1;
    }

    private String M() {
        int iU = u();
        String strW = "";
        switch (iU) {
            case 1:
                strW = v();
                break;
            case 2:
                strW = w();
                break;
            case 3:
                strW = ("" + v() + PinyinConverter.PINYIN_EXCLUDE) + w();
                break;
        }
        return "commit=" + getFixEngineVersion() + ";authorized_status=" + iU + ":" + strW;
    }

    private boolean N() {
        if (this.b.c != null) {
            return this.b.c.a();
        }
        return false;
    }

    private int O() {
        return this.b.ad();
    }

    private int P() {
        return this.b.ae();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void Q() {
        this.aL = new Handler(this.mLooper);
        this.aL.removeCallbacks(this.aM);
        this.aL.postDelayed(this.aM, 43200000L);
        com.unisound.common.y.c(com.unisound.common.y.v, "refresh activate timer start");
    }

    private void R() {
        if (this.ax.a()) {
            try {
                com.unisound.common.y.c(com.unisound.common.y.v, "unregiste network broadcastReceiver!");
                this.ak.unregisterReceiver(this.ax);
                this.ax.b(false);
            } catch (Exception e) {
                com.unisound.common.y.a(com.unisound.common.y.v, "unRegisteBroadcast exception");
                e.printStackTrace();
            }
        }
    }

    private int S() {
        if (this.ay.a()) {
            return 0;
        }
        com.unisound.common.y.a("init error " + ErrorCode.toJsonMessage(ErrorCode.GENERAL_COMPILER_INIT_ERROR));
        if (this.z != null) {
            this.z.onError(1300, ErrorCode.toJsonMessage(ErrorCode.GENERAL_COMPILER_INIT_ERROR));
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int a(List<String> list) throws JSONException, IOException, NumberFormatException {
        int i;
        co coVar = new co(this.l, this.x);
        if (L() != 0) {
            return ErrorCode.GENERAL_INIT_ERROR;
        }
        int size = list.size();
        if (size > this.ap) {
            return ErrorCode.ASR_SDK_UPLOAD_ONESHOT_ONLINE_EXCEED_MAXLIMIT;
        }
        if (list == null || size <= 0) {
            return ErrorCode.ASR_SDK_UPLOAD_ONESHOT_ONLINE_ERROR;
        }
        for (int i2 = 0; i2 < size; i2++) {
            if (TextUtils.isEmpty(list.get(i2))) {
                return ErrorCode.ASR_SDK_UPLOAD_ONESHOT_ONLINE_EMPTY_WORD;
            }
            if (co.a(list.get(i2))) {
                return ErrorCode.ASR_SDK_UPLOAD_ONESHOT_ONLINE_ILLEGAL;
            }
        }
        String strA = coVar.a(com.unisound.c.a.a(this.x), coVar.a(list));
        if (strA.contains("status")) {
            try {
                i = Integer.parseInt(new JSONObject(strA).getString("status"));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                i = -63406;
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        } else {
            i = -63406;
        }
        if (i != 0) {
            return i;
        }
        HashSet hashSet = new HashSet();
        hashSet.addAll(list);
        coVar.a(hashSet);
        return i;
    }

    private void a(IntentFilter intentFilter) {
        if (this.ax.a()) {
            return;
        }
        try {
            com.unisound.common.y.a(com.unisound.common.y.v, "registe network broadcastReceiver!");
            this.ak.registerReceiver(this.ax, intentFilter);
            this.ax.b(true);
        } catch (Exception e) {
            com.unisound.common.y.a(com.unisound.common.y.v, "registeBroadcast exception");
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(al alVar) {
        ArrayList arrayList = new ArrayList();
        b bVar = new b();
        bVar.a(alVar);
        bVar.a(this.F);
        bVar.a(false);
        bVar.b(this.b.ao());
        arrayList.add(com.unisound.common.v.a(bVar));
        this.am.obtainMessage(1201, com.unisound.common.v.a((ArrayList<String>) null, (ArrayList<String>) arrayList, (ArrayList<String>) null)).sendToTarget();
    }

    private void a(String str, int i) {
        this.A.a(str, i);
    }

    private void a(String str, String str2, String str3, String str4, String str5, String str6) {
        new bl(this, str3, str4, str5, str6, str, str2).start();
    }

    private void a(JSONObject jSONObject) {
        this.b.n(jSONObject.toString());
        com.unisound.common.y.c(com.unisound.common.y.v, "activate params is ", jSONObject.toString());
        this.aK = com.unisound.b.a.a.a(this.ak, this.b.aV());
        this.aK.a(new bm(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(al alVar) {
        String strA;
        if (this.A.y() && this.A.w() && (strA = com.unisound.common.v.a(alVar.a(), this.A.y(), this.b).a()) != null) {
            this.w.clear();
            this.w.add(strA);
            this.am.obtainMessage(1201, com.unisound.common.v.a((ArrayList<String>) null, (ArrayList<String>) null, this.w)).sendToTarget();
        }
        if (alVar.c()) {
            if (this.A.w()) {
                String strA2 = com.unisound.common.v.a(alVar.a(), this.A.y(), this.b).a();
                if (this.w != null && strA2 != null) {
                    this.w.clear();
                    this.w.add(strA2);
                    this.am.obtainMessage(1201, com.unisound.common.v.a((ArrayList<String>) null, (ArrayList<String>) null, this.w)).sendToTarget();
                } else if (this.w != null && strA2 == null) {
                    this.w.clear();
                    q(ErrorCode.ASR_SDK_NO_NLURESULT_ERROR);
                }
            }
            this.b.k(System.currentTimeMillis());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(al alVar) {
        if (!alVar.c() || this.b.ao() || alVar.d()) {
            return;
        }
        al alVar2 = new al();
        alVar2.a(alVar.e());
        alVar2.a(com.unisound.common.v.b());
        alVar2.b(alVar.b());
        alVar2.b(alVar.d());
        alVar2.a(alVar.c());
        b bVar = new b();
        bVar.a(alVar2);
        bVar.a(this.F);
        bVar.a(true);
        bVar.b(this.b.ao());
        this.v.add(com.unisound.common.v.a(bVar));
        this.am.obtainMessage(1201, com.unisound.common.v.a((ArrayList<String>) null, this.v, this.w)).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d(String str, String str2, String str3) throws JSONException {
        String string;
        boolean z = true;
        SharedPreferences sharedPreferences = this.l.getSharedPreferences(aA, 0);
        String string2 = sharedPreferences.getString("deviceInfo", "");
        try {
            if (TextUtils.isEmpty(string2)) {
                JSONObject jSONObject = new JSONObject();
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put(ca.c, str);
                jSONObject2.put("udid", str2);
                jSONObject2.put("token", str3);
                JSONArray jSONArray = new JSONArray();
                jSONArray.put(jSONObject2);
                jSONObject.put("deviceInfo", jSONArray);
                string = jSONObject.toString();
            } else {
                JSONObject jSONObject3 = new JSONObject(string2);
                JSONArray jSONArray2 = jSONObject3.getJSONArray("deviceInfo");
                int i = 0;
                while (true) {
                    if (i >= jSONArray2.length()) {
                        z = false;
                        break;
                    }
                    JSONObject jSONObject4 = jSONArray2.getJSONObject(i);
                    if (str.equals(jSONObject4.get(ca.c)) && str2.equals(jSONObject4.get("udid"))) {
                        jSONObject4.put("token", str3);
                        break;
                    }
                    i++;
                }
                if (!z) {
                    JSONObject jSONObject5 = new JSONObject();
                    jSONObject5.put(ca.c, str);
                    jSONObject5.put("udid", str2);
                    jSONObject5.put("token", str3);
                    jSONArray2.put(jSONObject5);
                    jSONObject3.put("deviceInfo", jSONArray2);
                }
                string = jSONObject3.toString();
            }
            com.unisound.common.y.c("saveTokenInfo deviceContent = " + string);
            SharedPreferences.Editor editorEdit = sharedPreferences.edit();
            editorEdit.putString("deviceInfo", string);
            editorEdit.commit();
        } catch (Exception e) {
        }
    }

    private void e(String str) {
        this.A.k(str);
    }

    private void e(String str, String str2) {
        JniUscClient jniUscClient = new JniUscClient();
        com.unisound.common.a aVarBb = this.b.bb();
        long jA = jniUscClient.a(aVarBb.a(), aVarBb.c());
        jniUscClient.a(9, str);
        jniUscClient.a(204, str2);
        com.unisound.common.y.c("SpeechUnderstanderInterface", "server :", aVarBb.a(), " port: ", Integer.valueOf(aVarBb.c()));
        com.unisound.common.y.c("SpeechUnderstanderInterface", "juc.create() returns ", Long.valueOf(jA));
    }

    private void e(boolean z) {
        this.A.a(z);
    }

    private void f(String str) {
        this.A.h(str);
    }

    private void f(boolean z) {
        this.A.b(z);
    }

    private int g(boolean z) {
        if (!this.f.g && !a(z)) {
            com.unisound.common.y.a("loadModel::isInit=false");
            return -1;
        }
        return b((this.f.f293a + "wakeup.dat") + "," + (this.f.f293a + this.f.d + ".dat"), "init_asr");
    }

    private void g(String str) {
        this.A.i(str);
    }

    private void h(String str) {
        this.A.j(str);
    }

    private void h(boolean z) {
        this.b.o(z);
    }

    private void i(String str) {
        this.A.e(str);
    }

    private void i(boolean z) {
        this.b.p(z);
    }

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivityManager == null) {
                com.unisound.common.y.a(com.unisound.common.y.v, " isNetworkAvailable cm is null!");
            } else {
                NetworkInfo[] allNetworkInfo = connectivityManager.getAllNetworkInfo();
                if (allNetworkInfo != null) {
                    for (NetworkInfo networkInfo : allNetworkInfo) {
                        if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            com.unisound.common.y.c("SpeechUnderstander isNetworkAvailable error");
        }
        return false;
    }

    private String j(boolean z) {
        return this.b.t(true);
    }

    private void j(String str) {
        this.A.d(str);
    }

    private void k(String str) {
        this.A.f(str);
    }

    private void k(boolean z) {
        this.b.u(z);
    }

    private boolean l(String str) {
        if (this.b.r(str)) {
            return true;
        }
        com.unisound.common.y.a("setNetEngine::error: unkown param " + str);
        return false;
    }

    private boolean l(boolean z) {
        if (this.b.c == null) {
            return false;
        }
        this.b.c.h(z);
        return true;
    }

    private boolean m(String str) {
        if (this.b.s(str)) {
            return true;
        }
        com.unisound.common.y.a("setNetEngineSubModel::error: unkown param " + this.F);
        return false;
    }

    private void n(int i) {
        this.A.a(i);
    }

    private void n(String str) {
        this.b.p(str);
    }

    private String o(String str) {
        if (str == null || str.equals("")) {
            return "";
        }
        String str2 = new String(Base64.decode(str, 0));
        com.unisound.common.y.c("SpeechUnderstanderInterface -> getversion : SDK_Version = ", str2);
        return str2;
    }

    private void o(int i) {
        this.b.P(i);
    }

    private void p(int i) {
        this.o.c(i);
    }

    private boolean p(String str) throws NoSuchAlgorithmException {
        String strA = com.unisound.common.aa.a();
        if (str == null || strA == null) {
            return false;
        }
        if (strA.equals("appkey_md5")) {
            return true;
        }
        String[] strArrSplit = strA.split(MqttTopic.MULTI_LEVEL_WILDCARD);
        String strA2 = com.unisound.common.aa.a(str);
        for (String str2 : strArrSplit) {
            if (!TextUtils.isEmpty(str2)) {
                com.unisound.common.y.c("checkAppkeyMd5 : value = " + str2 + ", appkey = " + str + ", appkeyMd5 = " + strA2);
                if (str2.equals(strA2)) {
                    return true;
                }
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void q(int i) {
        Message message = new Message();
        message.what = 54;
        message.obj = Integer.valueOf(i);
        this.am.sendMessage(message);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void r(int i) {
        Message message = new Message();
        message.what = 54;
        message.obj = Integer.valueOf(i);
        if (this.an != null) {
            this.an.sendMessage(message);
        }
    }

    private void s(int i) {
        this.b.v(i);
    }

    protected int A() {
        return this.K;
    }

    protected String B() {
        return this.p.a();
    }

    @Override // com.unisound.sdk.o
    protected void a(int i) {
        if (this.am != null) {
            this.am.sendEmptyMessage(i);
        }
    }

    @Override // com.unisound.sdk.o
    protected void a(VAD vad) {
        if (this.I.equals(this.J) && this.b.at()) {
            return;
        }
        this.am.sendEmptyMessage(18);
    }

    protected void a(ay ayVar) {
        this.o.a(ayVar);
    }

    @Override // com.unisound.sdk.o
    protected void a(String str, boolean z, int i, int i2) {
        this.aE = true;
        if (this.z != null && this.f349a.a(str, false, this.b.aH()) && this.mSpeechUnderstanderParams.i() != 1) {
            this.u.clear();
            int i3 = 0;
            while (true) {
                int i4 = i3;
                if (i4 >= this.f349a.f347a.size()) {
                    break;
                }
                com.unisound.common.y.c("SpeechUnderstanderInterface : recognizeResult.item = ", this.f349a.f347a.get(i4), " , times=  ", Integer.valueOf(i4));
                if (this.b.aO()) {
                    this.u.add(com.unisound.common.v.a(this.F, "full", this.f349a.f347a.get(i4), null, null, Float.valueOf(this.f349a.e), null, null, null, Integer.valueOf(i), Integer.valueOf(i2)));
                } else {
                    this.u.add(com.unisound.common.v.a(this.F, "full", this.f349a.f347a.get(i4), null, null, Float.valueOf(this.f349a.e), null, null, null, null, null));
                }
                i3 = i4 + 1;
            }
            this.am.obtainMessage(1202, com.unisound.common.v.a(this.u, (ArrayList<String>) null, (ArrayList<String>) null)).sendToTarget();
        }
        this.aE = false;
    }

    @Override // com.unisound.sdk.o
    protected void a(String str, boolean z, int i, long j, long j2, int i2, int i3) {
        com.unisound.common.y.c("SpeechUnderstandInterface doWakeupResult => ", str);
        this.aE = true;
        if (this.z != null && this.f349a.a(str, true, this.b.aH())) {
            ArrayList arrayList = new ArrayList();
            if (this.b.aO()) {
                arrayList.add(com.unisound.common.v.a(1000, "full", this.f349a.f347a.get(0), null, null, Float.valueOf(this.f349a.e), Integer.valueOf(i), Long.valueOf(j), Long.valueOf(j2), Integer.valueOf(i2), Integer.valueOf(i3)));
            } else {
                arrayList.add(com.unisound.common.v.a(1000, "full", this.f349a.f347a.get(0), null, null, Float.valueOf(this.f349a.e), Integer.valueOf(i), Long.valueOf(j), Long.valueOf(j2), null, null));
            }
            this.am.obtainMessage(3201, com.unisound.common.v.a((ArrayList<String>) arrayList, (ArrayList<String>) null, (ArrayList<String>) null)).sendToTarget();
            this.am.sendEmptyMessage(1);
        }
        this.aE = false;
    }

    @Override // com.unisound.sdk.o
    protected void a(boolean z, byte[] bArr, int i, int i2) {
        super.a(z, bArr, i, i2);
        if (this.B != null) {
            this.o.b(z, bArr, i, i2);
        }
        if (this.H.a(z, i2)) {
        }
    }

    @Override // com.unisound.sdk.o
    protected void b(int i) {
        if (i == -61002 && this.o != null) {
            this.o.d();
        }
        this.aE = true;
        if (this.z != null) {
            if (i != 0) {
                q(i);
            }
            this.D = true;
            this.am.sendEmptyMessage(6);
            J();
        }
        this.aE = false;
    }

    @Override // com.unisound.sdk.o
    protected void b(int i, int i2) {
        if (this.an != null) {
            this.an.sendEmptyMessage(i);
        }
    }

    protected int c(String str) {
        return 0;
    }

    protected int c(String str, String str2) {
        return 0;
    }

    protected int c(String str, String str2, String str3) {
        return 0;
    }

    @Override // com.unisound.sdk.o
    protected void c(int i) {
        this.am.sendEmptyMessage(17);
        this.mSpeechUnderstanderParams.b(i);
    }

    @Override // com.unisound.sdk.o
    protected void c(int i, int i2) {
        super.c(i, i2);
        Message message = new Message();
        message.what = i;
        message.obj = Integer.valueOf(i2);
        if (this.an != null) {
            this.an.sendMessage(message);
        }
    }

    @Override // com.unisound.sdk.o
    protected void cancel() {
        synchronized (this.mLooper) {
            this.as = true;
            this.C = true;
            this.D = true;
            this.o.c(true);
            super.cancel();
            while (this.aE) {
                try {
                    Thread.sleep(1L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.am.removeCallbacksAndMessages(null);
            if (this.o != null) {
                this.o.removeCallbacksAndMessages(null);
            }
            if (this.e != null) {
                this.e.removeCallbacksAndMessages(null);
            }
            this.z.onEvent(1117, (int) System.currentTimeMillis());
            com.unisound.common.y.c("SpeechunderStanderInterface: cancel called");
        }
    }

    protected String convertToArabicNumber(String str) {
        try {
            return com.unisound.common.i.b(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected int d(String str) {
        return 0;
    }

    protected int d(String str, String str2) {
        return 0;
    }

    @Override // com.unisound.sdk.o
    protected void d(int i) {
        super.d(i);
        this.K = i;
    }

    protected boolean d(boolean z) {
        this.b.L(z);
        return true;
    }

    public void destoryCompiler() {
        if (this.b.aJ()) {
            return;
        }
        this.ay.b();
    }

    @Override // com.unisound.sdk.o
    protected int e(int i) {
        return super.e(i);
    }

    @Override // com.unisound.sdk.o
    protected int f(int i) {
        return super.f(i);
    }

    @Override // com.unisound.sdk.o
    protected String getFixEngineVersion() {
        return this.F != 1 ? super.getFixEngineVersion() : "";
    }

    @Override // com.unisound.sdk.o
    protected Object getOption(int i) {
        switch (i) {
            case 1001:
                return Integer.valueOf(this.mSpeechUnderstanderParams.i());
            case 1003:
                return Boolean.valueOf(this.mSpeechUnderstanderParams.c());
            case 1004:
                return this.mSpeechUnderstanderParams.b();
            case 1008:
                return this.mSpeechUnderstanderParams.d();
            case 1009:
                return this.mSpeechUnderstanderParams.a();
            case 1010:
                return Integer.valueOf(this.b.u());
            case 1011:
                return Integer.valueOf(this.b.v());
            case 1012:
                return this.b.bj();
            case 1014:
                return Integer.valueOf(this.o.b());
            case 1020:
                return Boolean.valueOf(this.A.w());
            case 1021:
                return this.A.n();
            case 1023:
                return this.A.x();
            case 1030:
                return this.A.i();
            case 1031:
                return this.A.j();
            case 1032:
                return this.A.m();
            case 1036:
                return com.unisound.c.a.a(this.x);
            case 1044:
                return Integer.valueOf(this.b.f());
            case 1045:
                return Integer.valueOf(this.mSpeechUnderstanderParams.h());
            case SpeechConstants.ASR_OPT_ONESHOT_CACHE_TIME /* 1069 */:
                return Integer.valueOf(this.b.I());
            case 1076:
                return Boolean.valueOf(this.b.aH);
            case 1077:
                return this.b.ax();
            case 1078:
                return this.b.K();
            case 1082:
                return Integer.valueOf(this.b.aQ());
            case 1083:
                return Integer.valueOf(this.b.ay());
            case 1084:
                return Integer.valueOf(this.b.aA());
            case 1086:
                return this.b.az();
            case 1087:
                return this.mSpeechUnderstanderParams.k();
            case 1088:
                return M();
            case SpeechConstants.WAKEUP_WORK_ENGINE /* 1090 */:
            case SpeechConstants.ASR_OPT_LOCAL_WORK_ENGINE /* 10011 */:
                return Integer.valueOf(x());
            case SpeechConstants.ASR_OPT_MAX_ONLINE_WAKEUPWORDS_NUM /* 1094 */:
                return Integer.valueOf(this.ap);
            case 3150:
                return Float.valueOf(this.b.E());
            case SpeechConstants.ASR_OPT_GET_COMPILE_ERRORINFO /* 10005 */:
                return this.f.g();
            case SpeechConstants.ASR_OPT_ACTIVATE_TOKEN /* 10006 */:
                return com.unisound.c.a.a(this.x, com.unisound.c.a.a(this.x), this.ak);
            case SpeechConstants.ASR_OPT_USE_PATIAL_NLU /* 10021 */:
                return Boolean.valueOf(D());
            case SpeechConstants.ASR_OPT_PUNCTUATED /* 10034 */:
                return Boolean.valueOf(this.b.bx());
            case 10196:
                return Integer.valueOf(O());
            case 10197:
                return (this.b.c == null || !this.b.c.s()) ? Integer.valueOf(P()) : j(true);
            case 10199:
                return Boolean.valueOf(N());
            default:
                return super.getOption(i);
        }
    }

    @Override // com.unisound.sdk.o
    protected String getVersion() throws IOException {
        AssetManager assets = this.l.getAssets();
        StringBuffer stringBuffer = new StringBuffer();
        try {
            InputStream inputStreamOpen = assets.open("version/data");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStreamOpen));
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    String strO = o(stringBuffer.toString());
                    inputStreamOpen.close();
                    bufferedReader.close();
                    return strO;
                }
                stringBuffer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return super.getVersion();
        }
    }

    @Override // com.unisound.sdk.o
    protected void h() {
        this.am.sendEmptyMessage(13);
    }

    @Override // com.unisound.sdk.o
    protected void i() {
        this.am.sendEmptyMessage(20);
    }

    protected void init(String str) throws JSONException {
        C();
        try {
            com.unisound.c.a.a(this.ak);
        } catch (Exception e) {
            if (this.z != null) {
                this.z.onError(1300, ErrorCode.toJsonMessage(ErrorCode.GENERAL_NO_READ_PHONE_STATE_PERMISSION));
            }
        }
        if ((this.G != -1 && this.G != 2) || this.F != 2) {
            try {
                this.o.a(this.ak, this.aF);
            } catch (Exception e2) {
                if (this.z != null) {
                    this.z.onError(1300, ErrorCode.toJsonMessage(ErrorCode.GENERAL_NO_READ_PHONE_STATE_PERMISSION));
                }
            }
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put(com.unisound.common.x.f272a, this.x);
                jSONObject.put("appSecret", this.y);
                if (str != null && str.contains("activate")) {
                    JSONObject jSONObject2 = new JSONObject(str);
                    Iterator<String> itKeys = jSONObject2.keys();
                    while (itKeys.hasNext()) {
                        String next = itKeys.next();
                        String string = jSONObject2.getString(next);
                        jSONObject.put(next, string);
                        if (next.equals("deviceSn") && com.unisound.c.a.b(string) == -1) {
                            q(ErrorCode.ILLEGAL_DEVICESN);
                            return;
                        }
                    }
                }
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            a(jSONObject);
            refreshActivate();
        }
        if (str == null) {
            str = "";
        }
        if (str == "" || str.contains("activate")) {
            com.unisound.common.y.c("SpeechUnderStanderInterface : init json is an empty string!");
        } else {
            Map<Integer, Object> mapA = com.unisound.common.v.a(str, this.mSpeechUnderstanderParams.l());
            Iterator<Integer> it = mapA.keySet().iterator();
            while (it.hasNext()) {
                int iIntValue = it.next().intValue();
                if (mapA.get(Integer.valueOf(iIntValue)) != null) {
                    setOption(iIntValue, mapA.get(Integer.valueOf(iIntValue)));
                }
            }
        }
        if (this.aq) {
            this.al = new HandlerThread("ht_outer");
            this.al.start();
            this.am = new bp(this, this.al.getLooper());
            this.an = new bp(this, this.al.getLooper());
        } else {
            this.am = new bp(this, this.ak.getMainLooper());
            this.an = new bp(this, this.ak.getMainLooper());
        }
        z();
    }

    public void initCompiler() {
        if (this.b.aJ() || this.ay.a()) {
            return;
        }
        new bo(this).start();
    }

    protected int insertVocab(List<String> list, String str) {
        if (L() != 0) {
            return ErrorCode.GENERAL_INIT_ERROR;
        }
        if (S() != 0) {
            return ErrorCode.GENERAL_COMPILER_INIT_ERROR;
        }
        if (list == null || list.size() < 1) {
            return ErrorCode.ASR_INSERT_VOCABCONTENT_ERROR;
        }
        if (str == "" || !str.contains(MqttTopic.MULTI_LEVEL_WILDCARD) || str.split(MqttTopic.MULTI_LEVEL_WILDCARD).length != 2) {
            return ErrorCode.ASR_INSERT_VOCABNAME_ERROR;
        }
        String[] strArrSplit = str.split(MqttTopic.MULTI_LEVEL_WILDCARD);
        String str2 = strArrSplit[0];
        String str3 = strArrSplit[1];
        com.unisound.common.y.c("SpeechUnderstanderInterface :", "inserVocab --> modelTag = ", str2, ", tagName = ", str3);
        a("command", str2, this.f.b(str2), this.f.f(str2), this.f.a(str3, list), this.f.g(str2));
        return 0;
    }

    protected int insertVocab(Map<String, List<String>> map, String str) {
        if (L() != 0) {
            return ErrorCode.GENERAL_INIT_ERROR;
        }
        if (S() != 0) {
            return ErrorCode.GENERAL_COMPILER_INIT_ERROR;
        }
        if (map == null || str == "") {
            com.unisound.common.y.a("SpeechUnderstanderInterface : insertVocab parmas error!");
            return -1;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            stringBuffer.append(this.f.a(entry.getKey(), entry.getValue()) + "\n");
        }
        a("command", str, this.f.b(str), this.f.f(str), stringBuffer.toString(), this.f.g(str));
        return 0;
    }

    protected int insertVocab_ext(String str, String str2, String str3) {
        if (L() != 0) {
            return ErrorCode.GENERAL_INIT_ERROR;
        }
        if (S() != 0) {
            return ErrorCode.GENERAL_COMPILER_INIT_ERROR;
        }
        new bj(this, str, str2, str3).start();
        return 0;
    }

    protected int insertVocab_ext(String str, String str2, Map<String, List<String>> map) {
        map.entrySet();
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            stringBuffer.append(this.f.a(entry.getKey(), entry.getValue()) + "\n");
        }
        String string = stringBuffer.toString();
        com.unisound.common.y.c("SpeechUnderstanderInterface --> insertVocab_ext2 : vocabContent = ", string);
        return insertVocab_ext(str, str2, string);
    }

    @Override // com.unisound.sdk.o
    protected void j() {
        if (this.F != 2 && this.b.M()) {
            this.b.F(com.unisound.c.a.a(this.x, this.ak));
            this.o.c();
        }
        if (this.F == 1 && this.b.M()) {
            super.f();
        }
    }

    @Override // com.unisound.sdk.o
    protected void l() {
        this.o.d();
        this.am.sendEmptyMessage(12);
    }

    protected void l(int i) {
        if (this.am == null) {
            com.unisound.common.y.a("SpeechUnderstander -> doUploadUserData handler is null");
        } else if (i == 0) {
            this.am.sendEmptyMessage(16);
        } else {
            q(i);
        }
    }

    protected int loadCompiledJsgf(String str, String str2) {
        return L() != 0 ? ErrorCode.GENERAL_INIT_ERROR : this.f.a(str, str2);
    }

    protected int loadGrammar(String str, String str2) {
        if (L() != 0) {
            return ErrorCode.GENERAL_INIT_ERROR;
        }
        b(str2, "command", str);
        return 0;
    }

    protected void m(int i) {
        if (i == 0) {
            return;
        }
        Message message = new Message();
        message.what = 53;
        message.obj = Integer.valueOf(i);
        this.am.sendMessage(message);
    }

    @Override // com.unisound.sdk.o
    protected void n() {
        if (!this.D || !this.C) {
            if (this.z != null) {
            }
            return;
        }
        this.e.a(false);
        this.o.c(false);
        com.unisound.common.y.a("SpeechUnderstander fixend&netend doRecordingStart cancel");
    }

    @Override // com.unisound.sdk.o
    protected void o() {
    }

    @Override // com.unisound.sdk.o
    protected void p() {
        this.am.sendEmptyMessage(14);
    }

    @Override // com.unisound.sdk.o
    public void postRecordingStartStatus() {
        super.postRecordingStartStatus();
        this.am.sendEmptyMessage(11);
    }

    public synchronized void refreshActivate() {
        com.unisound.common.y.c(com.unisound.common.y.v, "refreshActivate called!");
        if (isNetworkAvailable(this.ak)) {
            this.ax.a(false);
            R();
            String strA = com.unisound.c.a.a(this.x, com.unisound.c.a.a(this.x), this.l);
            if (strA.equals("")) {
                this.aK.a(0);
            } else {
                this.at = true;
                this.aK.q(strA);
                this.aK.a(1);
            }
        } else {
            com.unisound.common.y.a(com.unisound.common.y.v, "refreshActivate network not available!");
            IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
            this.ax.a(true);
            a(intentFilter);
        }
    }

    protected int release(int i, String str) {
        if (this.aL != null) {
            this.aL.removeCallbacks(this.aM);
        }
        R();
        if (this.n != null) {
            this.n.getLooper().quit();
        }
        if (this.mSpeechUnderstanderParams.o()) {
            this.b.c.r();
        }
        switch (i) {
            case 1401:
                if (this.M) {
                    this.M = false;
                    super.q();
                    break;
                }
                break;
        }
        return -1;
    }

    @Override // com.unisound.sdk.o
    protected int setAudioSource(IAudioSource iAudioSource) {
        return super.setAudioSource(iAudioSource);
    }

    protected void setListener(SpeechUnderstanderListener speechUnderstanderListener) {
        this.z = speechUnderstanderListener;
    }

    protected String setOnlineWakeupWord(List<String> list) {
        new bq(this).execute(list);
        return "";
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // com.unisound.sdk.o
    protected void setOption(int i, Object obj) {
        switch (i) {
            case 1001:
                try {
                    int iIntValue = ((Integer) obj).intValue();
                    switch (iIntValue) {
                        case 0:
                        case 1:
                        case 2:
                            if (this.F != iIntValue) {
                                if (this.G == -1) {
                                    this.L = false;
                                }
                                this.F = iIntValue;
                            }
                            this.mSpeechUnderstanderParams.c(iIntValue);
                            break;
                        default:
                            com.unisound.common.y.a("USCMixRecognizer.setOption unkown value " + iIntValue);
                            break;
                    }
                } catch (Exception e) {
                    com.unisound.common.y.a("set asr_service_mode Error.");
                    return;
                }
            case 1003:
                try {
                    boolean zBooleanValue = ((Boolean) obj).booleanValue();
                    d(zBooleanValue);
                    this.mSpeechUnderstanderParams.a(zBooleanValue);
                    break;
                } catch (Exception e2) {
                    com.unisound.common.y.a("set asr_voice_field Error.");
                    return;
                }
            case 1004:
                try {
                    String str = (String) obj;
                    n(str);
                    this.mSpeechUnderstanderParams.b(str);
                    break;
                } catch (Exception e3) {
                    com.unisound.common.y.a("set asr_language Error.");
                    return;
                }
            case 1007:
                try {
                    String str2 = (String) obj;
                    this.b.u(str2);
                    this.mSpeechUnderstanderParams.f(str2);
                    break;
                } catch (Exception e4) {
                    com.unisound.common.y.a("set asr_online_oneshot_server_address Error.");
                    return;
                }
            case 1008:
                try {
                    String str3 = (String) obj;
                    l(str3);
                    this.mSpeechUnderstanderParams.c(str3);
                    break;
                } catch (Exception e5) {
                    com.unisound.common.y.a("set asr_domain Error.");
                    return;
                }
            case 1009:
                try {
                    String str4 = (String) obj;
                    if (this.b.t(str4)) {
                        this.mSpeechUnderstanderParams.a(str4);
                    } else {
                        q(ErrorCode.ASR_SDK_SET_ASR_SERVER_ADDR_ERROR);
                    }
                    break;
                } catch (Exception e6) {
                    com.unisound.common.y.a("set asr_server_address Error.");
                    return;
                }
            case 1014:
                try {
                    p(((Integer) obj).intValue());
                    break;
                } catch (Exception e7) {
                    com.unisound.common.y.a("set asr_net_timeOut Error.");
                    return;
                }
            case 1017:
                try {
                    this.b.l((String) obj);
                    break;
                } catch (Exception e8) {
                    com.unisound.common.y.a("set ASR_OPT_SET_POST_PROCESS_PARAMS Error.");
                    return;
                }
            case 1018:
                try {
                    this.b.b((List<String>) obj);
                    break;
                } catch (Exception e9) {
                    com.unisound.common.y.a("set ASR_OPT_SET_ONESHOT_WAKEUPWORD Error.");
                    return;
                }
            case 1019:
                try {
                    this.b.k((String) obj);
                    break;
                } catch (Exception e10) {
                    com.unisound.common.y.a("set ASR_OPT_ADDITIONAL_SERVICE Error.");
                    return;
                }
            case 1020:
                try {
                    e(((Boolean) obj).booleanValue());
                    break;
                } catch (Exception e11) {
                    com.unisound.common.y.a("set nlu_enable Error.");
                    return;
                }
            case 1021:
                try {
                    e((String) obj);
                    break;
                } catch (Exception e12) {
                    com.unisound.common.y.a("set nlu_scenario Error.");
                    return;
                }
            case 1024:
                try {
                    j(String.valueOf(obj));
                    break;
                } catch (Exception e13) {
                    com.unisound.common.y.a("set nlu_ver Error.");
                    return;
                }
            case 1025:
                try {
                    k(String.valueOf(obj));
                    break;
                } catch (Exception e14) {
                    com.unisound.common.y.a("set nlu_appver Error.");
                    return;
                }
            case 1030:
                try {
                    f((String) obj);
                    break;
                } catch (Exception e15) {
                    com.unisound.common.y.a("set history Error.");
                    return;
                }
            case 1031:
                try {
                    g((String) obj);
                    break;
                } catch (Exception e16) {
                    com.unisound.common.y.a("set city Error.");
                    return;
                }
            case 1032:
                try {
                    h((String) obj);
                    break;
                } catch (Exception e17) {
                    com.unisound.common.y.a("set voiceID Error.");
                    return;
                }
            case 1033:
                try {
                    i((String) obj);
                    break;
                } catch (Exception e18) {
                    com.unisound.common.y.a("set gps Error.");
                    return;
                }
            case 1044:
                try {
                    o(((Integer) obj).intValue());
                    break;
                } catch (Exception e19) {
                    com.unisound.common.y.a("set asr_sampling_rate Error.");
                    return;
                }
            case 1060:
                try {
                    this.b.f(((Integer) obj).intValue());
                    this.mSpeechUnderstanderParams.a(((Integer) obj).intValue());
                    break;
                } catch (Exception e20) {
                    com.unisound.common.y.a("set asr_front_cache_time Error.");
                    return;
                }
            case 1061:
                try {
                    this.mSpeechUnderstanderParams.c(((Boolean) obj).booleanValue());
                    break;
                } catch (Exception e21) {
                    com.unisound.common.y.a("set wakeup_vad_enable Error.");
                    return;
                }
            case SpeechConstants.ASR_OPT_ONESHOT_CACHE_TIME /* 1069 */:
            case SpeechConstants.ASR_OPT_LOCAL_CONTINUE_RECOGNIZE /* 10012 */:
                break;
            case 1070:
                try {
                    this.b.g(((Integer) obj).intValue());
                    break;
                } catch (Exception e22) {
                    com.unisound.common.y.a("set Front_reset_cache_byte_time Error.");
                    return;
                }
            case 1071:
                try {
                    com.unisound.common.y.t = ((Boolean) obj).booleanValue();
                    break;
                } catch (Exception e23) {
                    com.unisound.common.y.a("set DEBUG_SAVELOG Error.");
                    return;
                }
            case 1072:
                try {
                    com.unisound.common.y.u = ((Boolean) obj).booleanValue();
                } catch (Exception e24) {
                    com.unisound.common.y.a("set DEBUG_POSTLOG Error.");
                }
                try {
                    this.aq = ((Boolean) obj).booleanValue();
                    break;
                } catch (Exception e25) {
                    com.unisound.common.y.a("set USE_HANDLERTHREAD Error.");
                    return;
                }
            case 1073:
                this.aq = ((Boolean) obj).booleanValue();
                break;
            case 1074:
                try {
                    this.b.b((String) obj);
                    break;
                } catch (Exception e26) {
                    com.unisound.common.y.a("set SAVE_AFTERVAD_RECORDING_DATA Error.");
                    return;
                }
            case 1075:
                try {
                    this.b.i(((Boolean) obj).booleanValue());
                    break;
                } catch (Exception e27) {
                    com.unisound.common.y.a("set MARK_VAD Error.");
                    return;
                }
            case 1076:
                try {
                    this.b.N(((Boolean) obj).booleanValue());
                    break;
                } catch (Exception e28) {
                    com.unisound.common.y.a("set TEMP_RESULT Error.");
                    return;
                }
            case 1079:
                try {
                    this.b.e(((Boolean) obj).booleanValue());
                    break;
                } catch (Exception e29) {
                    com.unisound.common.y.a("set setRecognizeFrontVADEnable Error.");
                    return;
                }
            case SpeechConstants.ASR_OPT_RECORDING_PAC_SIZE /* 1080 */:
                try {
                    this.b.x(((Integer) obj).intValue());
                    break;
                } catch (Exception e30) {
                    com.unisound.common.y.a("set ASR_OPT_RECORDING_PAC_SIZE Error");
                    return;
                }
            case 1081:
                try {
                    this.b.h(((Integer) obj).intValue());
                    break;
                } catch (Exception e31) {
                    com.unisound.common.y.a("set OneShot VAD back sil time Error.");
                    return;
                }
            case 1082:
                try {
                    this.b.O(((Integer) obj).intValue());
                    break;
                } catch (Exception e32) {
                    com.unisound.common.y.a("set RECOGNIZE_SCENE Error.");
                    return;
                }
            case 1083:
                try {
                    this.b.D(((Integer) obj).intValue());
                    com.unisound.common.y.c("SpeechUnderstanderInterface", "set recognize modelId ", obj);
                    break;
                } catch (Exception e33) {
                    com.unisound.common.y.a("set RECOGNIZE_MODEL_ID Error.");
                    return;
                }
            case 1084:
                try {
                    this.b.E(((Integer) obj).intValue());
                    com.unisound.common.y.c("SpeechUnderstanderInterface", "set wakeup modelId ", obj);
                    break;
                } catch (Exception e34) {
                    com.unisound.common.y.a("set WAKEUP_MODEL_ID Error.");
                    return;
                }
            case 1085:
                try {
                    this.b.P(((Boolean) obj).booleanValue());
                    break;
                } catch (Exception e35) {
                    com.unisound.common.y.a("set ALREAD_AWPE Error.");
                    return;
                }
            case 1087:
                try {
                    String str5 = (String) obj;
                    m(str5);
                    this.mSpeechUnderstanderParams.g(str5);
                    break;
                } catch (Exception e36) {
                    com.unisound.common.y.a("set asr_subdomain Error.");
                    return;
                }
            case 1089:
                try {
                    this.b.m((String) obj);
                    break;
                } catch (Exception e37) {
                    com.unisound.common.y.a("set ASR_OPT_ACTIVATE_MEMO Error.");
                    return;
                }
            case SpeechConstants.WAKEUP_WORK_ENGINE /* 1090 */:
            case SpeechConstants.ASR_OPT_LOCAL_WORK_ENGINE /* 10011 */:
                try {
                    int iIntValue2 = ((Integer) obj).intValue();
                    this.b.n(iIntValue2);
                    this.b.o(iIntValue2);
                    break;
                } catch (Exception e38) {
                    com.unisound.common.y.a("set WAKEUP_WORK_ENGINE Error.");
                    return;
                }
            case SpeechConstants.WUW_WAKEUP_THRESHOLD /* 1091 */:
                try {
                    this.b.G(((Integer) obj).intValue());
                    break;
                } catch (Exception e39) {
                    com.unisound.common.y.a("set WUW_WAKEUP_THRESHOLD Error");
                    return;
                }
            case SpeechConstants.ASR_OPT_IGNORE_RESULT_TAG /* 1092 */:
                try {
                    this.b.i((String) obj);
                    break;
                } catch (Exception e40) {
                    com.unisound.common.y.a("set ASR_OPT_IGNORE_RESULT_TAG Error.");
                    return;
                }
            case SpeechConstants.ASR_OPT_CONTINUE_RECOGNIZE /* 1093 */:
                try {
                    this.b.x(((Boolean) obj).booleanValue());
                    break;
                } catch (Exception e41) {
                    com.unisound.common.y.a("set ASR_OPT_CONTINUE_RECOGNIZE Error.");
                    return;
                }
            case SpeechConstants.ASR_OPT_MAX_ONLINE_WAKEUPWORDS_NUM /* 1094 */:
                try {
                    this.ap = ((Integer) obj).intValue();
                    break;
                } catch (Exception e42) {
                    com.unisound.common.y.a("set ASR_OPT_MAX_ONLINE_WAKEUPWORDS_NUM Error.");
                    return;
                }
            case 1095:
                try {
                    this.b.I(((Boolean) obj).booleanValue());
                    break;
                } catch (Exception e43) {
                    com.unisound.common.y.a("set setWxServiceEnabled Error.");
                    return;
                }
            case SpeechConstants.ASR_OPT_FILTER_URL /* 1096 */:
                try {
                    this.b.D((String) obj);
                    break;
                } catch (Exception e44) {
                    com.unisound.common.y.a("set ASR_OPT_FILTERURL Error.");
                    return;
                }
            case 1097:
                try {
                    this.b.E((String) obj);
                    break;
                } catch (Exception e45) {
                    com.unisound.common.y.a("set ASR_OPT_SUB_SERVICE_PARAM Error.");
                    return;
                }
            case SpeechConstants.ASR_OPT_SERVICE_PARAMETER /* 1098 */:
                try {
                    this.b.A((String) obj);
                    break;
                } catch (Exception e46) {
                    com.unisound.common.y.a("set ASR_OPT_SERVICE_PARAMETER Error");
                    return;
                }
            case SpeechConstants.ASR_OPT_ONESHOT_WAKEUP_BUFFER_LENGTH /* 1099 */:
                try {
                    this.b.y(((Integer) obj).intValue());
                    break;
                } catch (Exception e47) {
                    com.unisound.common.y.a("set wakeup buffer length error!");
                    return;
                }
            case SpeechConstants.ASR_OPT_REQ_NLU_LENGTH /* 1100 */:
                try {
                    this.b.R(((Integer) obj).intValue());
                    break;
                } catch (Exception e48) {
                    com.unisound.common.y.a("set ASR_OPT_REQ_NLU_LENGTH Error.");
                    return;
                }
            case SpeechConstants.ASR_BEST_RESULT_RETURN /* 1200 */:
                try {
                    this.b.R(((Boolean) obj).booleanValue());
                    break;
                } catch (Exception e49) {
                    com.unisound.common.y.a("set ASR_BEST_RESULT_RETURN Error.");
                    return;
                }
            case 1203:
                try {
                    this.b.S(((Integer) obj).intValue());
                    break;
                } catch (Exception e50) {
                    com.unisound.common.y.a("set ASR_FALSE_ALARM Error.");
                    return;
                }
            case 1204:
                try {
                    this.b.T(((Integer) obj).intValue());
                } catch (Exception e51) {
                    com.unisound.common.y.a("set ASR_DOMAINS_PENALTY Error.");
                }
                super.setOption(i, obj);
                break;
            case 3150:
                try {
                    this.b.a(((Float) obj).floatValue());
                    break;
                } catch (Exception e52) {
                    com.unisound.common.y.a("set wakeup_threshold_value Error.");
                    return;
                }
            case 6000:
                try {
                    this.b.B((String) obj);
                    break;
                } catch (Exception e53) {
                    com.unisound.common.y.a("set UploadUserDataServer Error.");
                    return;
                }
            case 6001:
                try {
                    this.b.C((String) obj);
                    break;
                } catch (Exception e54) {
                    com.unisound.common.y.a("set setUploadUserDataServerUrl Error.");
                    return;
                }
            case 9000:
                try {
                    this.b.n(((Boolean) obj).booleanValue());
                    break;
                } catch (Exception e55) {
                    com.unisound.common.y.a("set ContinueReadData Error.");
                    return;
                }
            case 9001:
                try {
                    this.b.l(((Integer) obj).intValue());
                    break;
                } catch (Exception e56) {
                    com.unisound.common.y.a("set OneshotRecognitionBacksil Error.");
                    return;
                }
            case SpeechConstants.ASR_OPT_SGN_SETTING /* 10001 */:
                try {
                    this.b.z((String) obj);
                    break;
                } catch (Exception e57) {
                    com.unisound.common.y.a("set ASR_OPT_SGN_SETTING Error");
                    return;
                }
            case SpeechConstants.ASR_INIT_MODE /* 10002 */:
                try {
                    int iIntValue3 = ((Integer) obj).intValue();
                    this.G = iIntValue3 >= 0 ? iIntValue3 > 2 ? 1 : iIntValue3 : -1;
                    break;
                } catch (Exception e58) {
                    com.unisound.common.y.a("set ASR_OPT_INIT_MODE Error.");
                    return;
                }
            case SpeechConstants.ASR_OPT_SET_COMPILE_MAX_PRONUNCIATION /* 10003 */:
                try {
                    if (this.f.a(((Integer) obj).intValue()) != 0) {
                    }
                } catch (Exception e59) {
                    com.unisound.common.y.a("set ASR_OPT_SET_COMPILE_MAX_PRONUNCIATION Error.");
                    return;
                }
                break;
            case SpeechConstants.ASR_OPT_SET_OVER_MAX_PRONUNCIATION_INSERT /* 10004 */:
                try {
                    if (this.f.a(((Boolean) obj).booleanValue()) != 0) {
                        com.unisound.common.y.a("set ASR_OPT_SET_OVER_MAX_PRONUNCIATION_INSERT Error.");
                        break;
                    }
                } catch (Exception e60) {
                    com.unisound.common.y.a("set ASR_OPT_SET_OVER_MAX_PRONUNCIATION_INSERT Error.");
                    return;
                }
                break;
            case SpeechConstants.ASR_OPT_WUW_NET0_THRESHOLD /* 10007 */:
                try {
                    this.b.H(((Integer) obj).intValue());
                    break;
                } catch (Exception e61) {
                    com.unisound.common.y.a("set ASR_OPT_WUW_NET0_THRESHOLD Error.");
                    return;
                }
            case SpeechConstants.ASR_OPT_WUW_NET1_THRESHOLD /* 10008 */:
                try {
                    this.b.I(((Integer) obj).intValue());
                    break;
                } catch (Exception e62) {
                    com.unisound.common.y.a("set ASR_OPT_WUW_NET1_THRESHOLD Error.");
                    return;
                }
            case SpeechConstants.ASR_OPT_WUW_NET2_THRESHOLD /* 10009 */:
                try {
                    this.b.J(((Integer) obj).intValue());
                    break;
                } catch (Exception e63) {
                    com.unisound.common.y.a("set ASR_OPT_WUW_NET2_THRESHOLD Error.");
                    return;
                }
            case SpeechConstants.ASR_OPT_WUW_ACTIVE_NET /* 10010 */:
                try {
                    this.b.K(((Integer) obj).intValue());
                    break;
                } catch (Exception e64) {
                    com.unisound.common.y.a("set ASR_OPT_WUW_ACTIVE_NET Error.");
                    return;
                }
            case SpeechConstants.ASR_OPT_RECOGNIZE_VAD_ENABLE /* 10013 */:
                try {
                    this.mSpeechUnderstanderParams.d(((Boolean) obj).booleanValue());
                    break;
                } catch (Exception e65) {
                    com.unisound.common.y.a("set ASR_OPT_RECOGNIZE_VAD_ENABLE Error.");
                    return;
                }
            case SpeechConstants.ASR_OPT_USER_ID /* 10014 */:
                try {
                    this.b.H((String) obj);
                    break;
                } catch (Exception e66) {
                    com.unisound.common.y.a("set ASR_OPT_USER_ID Error.");
                    return;
                }
            case SpeechConstants.ASR_OPT_RETURN_ORIGIN_FORMAT /* 10019 */:
                try {
                    this.b.y(((Boolean) obj).booleanValue());
                    break;
                } catch (Exception e67) {
                    com.unisound.common.y.a("set ASR_OPT_RETURN_ORIGIN_FORMAT Error.");
                    return;
                }
            case SpeechConstants.ASR_OPT_USE_PATIAL_NLU /* 10021 */:
                try {
                    f(((Boolean) obj).booleanValue());
                    break;
                } catch (Exception e68) {
                    com.unisound.common.y.a("set ASR_OPT_USE_PATIAL_NLU Error.");
                    return;
                }
            case SpeechConstants.ASR_OPT_APPEND_LENGTH /* 10022 */:
                try {
                    n(((Integer) obj).intValue());
                    break;
                } catch (Exception e69) {
                    com.unisound.common.y.a("set ASR_OPT_APPEND_LENGTH Error.");
                    return;
                }
            case SpeechConstants.ASR_OPT_ADVANCE_INIT_COMPILER /* 10023 */:
                try {
                    this.b.E(((Boolean) obj).booleanValue());
                    break;
                } catch (Exception e70) {
                    com.unisound.common.y.a("set ASR_OPT_ADVANCE_INIT_COMPILER Error.");
                    return;
                }
            case SpeechConstants.ASR_OPT_AUDIO_FORMAT /* 10030 */:
                try {
                    this.b.g((String) obj);
                    break;
                } catch (Exception e71) {
                    com.unisound.common.y.a("set ASR_OPT_AUDIO_FORMAT Error.");
                    return;
                }
            case SpeechConstants.ASR_OPT_SAVE_SESSION_PCM_ENABLE /* 10031 */:
                try {
                    this.b.A(((Boolean) obj).booleanValue());
                    break;
                } catch (Exception e72) {
                    com.unisound.common.y.a("set ASR_OPT_SAVE_SESSION_PCM_ENABLE Error.");
                    return;
                }
            case SpeechConstants.ASR_OPT_SAVE_SESSION_PCM_DIR /* 10032 */:
                try {
                    this.b.h((String) obj);
                    break;
                } catch (Exception e73) {
                    com.unisound.common.y.a("set ASR_OPT_SAVE_SESSION_PCM_DIR Error.");
                    return;
                }
            case SpeechConstants.ASR_OPT_RECORDING_WAV /* 10033 */:
                try {
                    this.b.a(((Boolean) obj).booleanValue());
                    break;
                } catch (Exception e74) {
                    com.unisound.common.y.a("set ASR_OPT_RECORDING_WAV Error.");
                    return;
                }
            case SpeechConstants.ASR_OPT_PUNCTUATED /* 10034 */:
                try {
                    this.b.T(((Boolean) obj).booleanValue());
                    break;
                } catch (Exception e75) {
                    com.unisound.common.y.a("set ASR_OPT_PUNCTUATED Error.");
                    return;
                }
            case SpeechConstants.ASR_OPT_OPEN_AUDIOURL /* 10035 */:
                try {
                    this.b.U(((Boolean) obj).booleanValue());
                    break;
                } catch (Exception e76) {
                    com.unisound.common.y.a("set ASR_OPT_OPEN_FULL_DUPLEX Error.");
                    return;
                }
            case SpeechConstants.ASR_OPT_OPEN_FULL_DUPLEX /* 10036 */:
                try {
                    this.b.V(((Boolean) obj).booleanValue());
                    break;
                } catch (Exception e77) {
                    com.unisound.common.y.a("set ASR_OPT_OPEN_FULL_DUPLEX Error.");
                    return;
                }
            case SpeechConstants.ASR_OPT_MAX_WAKEUP_END /* 10037 */:
                try {
                    this.b.L(((Integer) obj).intValue());
                    break;
                } catch (Exception e78) {
                    com.unisound.common.y.a("set ASR_OPT_MAX_WAKEUP_END Error.");
                    return;
                }
            case SpeechConstants.ASR_OPT_INHIBIT_FRONT_WAKEUP /* 10038 */:
                try {
                    this.b.F(((Boolean) obj).booleanValue());
                    break;
                } catch (Exception e79) {
                    com.unisound.common.y.a("set ASR_OPT_INHIBIT_FRONT_WAKEUP Error.");
                    return;
                }
            case SpeechConstants.ASR_OPT_INHIBIT_BACK_WAKEUP /* 10039 */:
                try {
                    this.b.G(((Boolean) obj).booleanValue());
                    break;
                } catch (Exception e80) {
                    com.unisound.common.y.a("set ASR_OPT_INHIBIT_BACK_WAKEUP Error.");
                    return;
                }
            case SpeechConstants.ASR_OPT_LOCAL_RESULT_CONTAINS_UTTERANCETIME /* 10040 */:
                try {
                    this.b.H(((Boolean) obj).booleanValue());
                    break;
                } catch (Exception e81) {
                    com.unisound.common.y.a("set ASR_OPT_LOCAL_RESULT_CONTAINS_UTTERANCETIME Error.");
                    return;
                }
            case SpeechConstants.ASR_OPT_VAD_AFFECT_ASR /* 10041 */:
                try {
                    this.b.B(((Boolean) obj).booleanValue());
                    break;
                } catch (Exception e82) {
                    com.unisound.common.y.a("set ASR_OPT_VAD_AFFECT_ASR Error.");
                    return;
                }
            case SpeechConstants.ASR_OPT_INPUT_SAMPLE_RATE /* 10042 */:
                try {
                    this.b.N(((Integer) obj).intValue());
                    break;
                } catch (Exception e83) {
                    com.unisound.common.y.a("set ASR_OPT_INPUT_SAMPLE_RATE Error.");
                    return;
                }
            case SpeechConstants.ASR_OPT_RK_THREAD_NUM /* 10043 */:
                try {
                    this.b.M(((Integer) obj).intValue());
                    break;
                } catch (Exception e84) {
                    com.unisound.common.y.a("set ASR_OPT_RK_THREAD_NUM Error.");
                    return;
                }
            case SpeechConstants.ASR_OPT_MAXWELL_ENABLE /* 10060 */:
                try {
                    this.b.S(((Boolean) obj).booleanValue());
                    break;
                } catch (Exception e85) {
                    com.unisound.common.y.a("set ASR_OPT_MAXWELL_ENABLE Error.");
                    return;
                }
            case SpeechConstants.ASR_OPT_RET_TYPE /* 10061 */:
                try {
                    this.b.I((String) obj);
                    break;
                } catch (Exception e86) {
                    com.unisound.common.y.a("set ASR_OPT_RET_TYPE Error.");
                    return;
                }
            case SpeechConstants.ASR_OPT_PRINT_NET_ENGINE_LOG /* 10062 */:
                try {
                    this.b.z(((Boolean) obj).booleanValue());
                    break;
                } catch (Exception e87) {
                    com.unisound.common.y.a("set ASR_OPT_PRINT_NET_ENGINE_LOG Error.");
                    return;
                }
            case 10100:
                try {
                    this.mSpeechUnderstanderParams.g(((Boolean) obj).booleanValue());
                    break;
                } catch (Exception e88) {
                    com.unisound.common.y.a("set ASR_FORMIC_CLOSE_4MICALGORITHM Error.");
                    return;
                }
            case SpeechConstants.ASR_FOURMIC_OUT_SET_4MICWAKEUPFLAG /* 10101 */:
                try {
                    this.b.q(((Integer) obj).intValue());
                    break;
                } catch (Exception e89) {
                    com.unisound.common.y.a("set ASR_FOURMIC_OUT_SET_4MICWAKEUPFLAG Error.");
                    return;
                }
            case SpeechConstants.ASR_FOURMIC_OUT_SET_4MICUTTERANCETIME /* 10102 */:
                try {
                    this.b.s(((Integer) obj).intValue());
                    break;
                } catch (Exception e90) {
                    com.unisound.common.y.a("set ASR_FOURMIC_OUT_SET_4MICUTTERANCETIME Error.");
                    return;
                }
            case SpeechConstants.ASR_FOURMIC_OUT_SET_4MICDELAYTIME /* 10103 */:
                try {
                    this.b.u(((Integer) obj).intValue());
                    break;
                } catch (Exception e91) {
                    com.unisound.common.y.a("set ASR_FOURMIC_OUT_SET_4MICDELAYTIME Error.");
                    return;
                }
            case SpeechConstants.ASR_FOURMIC_OUT_SET_4MICWAKEUPSTATUS /* 10104 */:
                try {
                    this.b.q(((Boolean) obj).booleanValue());
                    break;
                } catch (Exception e92) {
                    com.unisound.common.y.a("set ASR_FOURMIC_OUT_SET_4MICWAKEUPSTATUS Error.");
                    return;
                }
            case SpeechConstants.ASR_FOURMIC_OUT_SET_4MICUTTERANCETIMESTATUS /* 10105 */:
                try {
                    this.b.r(((Boolean) obj).booleanValue());
                    break;
                } catch (Exception e93) {
                    com.unisound.common.y.a("set ASR_FOURMIC_OUT_SET_4MICUTTERANCETIMESTATUS Error.");
                    return;
                }
            case SpeechConstants.ASR_FOURMIC_OUT_SET_4MICDELAYTIMESTATUS /* 10106 */:
                try {
                    this.b.s(((Boolean) obj).booleanValue());
                    break;
                } catch (Exception e94) {
                    com.unisound.common.y.a("set ASR_FOURMIC_OUT_SET_4MICDELAYTIMESTATUS Error.");
                    return;
                }
            case SpeechConstants.ASR_FOURMIC_IS_RK_PLATFORM /* 10107 */:
                try {
                    this.b.c.i(((Boolean) obj).booleanValue());
                    break;
                } catch (Exception e95) {
                    com.unisound.common.y.a("set ASR_FOURMIC_IS_RK_PLATFORM Error.");
                    return;
                }
            case SpeechConstants.ASR_FOURMIC_IS_RK_SINGALCHANEL /* 10108 */:
                try {
                    this.b.c.j(((Boolean) obj).booleanValue());
                    break;
                } catch (Exception e96) {
                    com.unisound.common.y.a("set ASR_FOURMIC_IS_RK_SINGALCHANEL Error.");
                    return;
                }
            case SpeechConstants.ASR_OPT_FOURMIC_PCM_TEST /* 10192 */:
                try {
                    this.b.C(((Boolean) obj).booleanValue());
                    break;
                } catch (Exception e97) {
                    com.unisound.common.y.a("set ASR_OPT_FOURMIC_PCM_TEST Error.");
                    return;
                }
            case SpeechConstants.ASR_FOURMIC_USE_ASR_AS_VAD /* 10193 */:
                try {
                    if (!l(((Boolean) obj).booleanValue())) {
                        com.unisound.common.y.a("set ASR_FOURMIC_USE_ASR_AS_VAD Error.");
                    }
                } catch (Exception e98) {
                    com.unisound.common.y.a("set ASR_FOURMIC_USE_ASR_AS_VAD Error.");
                }
                this.b.H(((Integer) obj).intValue());
                break;
            case 10194:
                try {
                    s(((Integer) obj).intValue());
                    break;
                } catch (Exception e99) {
                    com.unisound.common.y.a("set ASR_FOURMIC_CHANGE_CHANNAL Error.");
                    return;
                }
            case 10198:
                try {
                    this.mSpeechUnderstanderParams.f(((Boolean) obj).booleanValue());
                    break;
                } catch (Exception e100) {
                    com.unisound.common.y.a("set ASR_FOURMIC_ISDEBUG Error.");
                    return;
                }
            case 10199:
                try {
                    this.mSpeechUnderstanderParams.e(((Boolean) obj).booleanValue());
                    break;
                } catch (Exception e101) {
                    com.unisound.common.y.a("set ASR_FOURMIC Error.");
                    return;
                }
            case 20170503:
                try {
                    com.unisound.common.y.r = ((Boolean) obj).booleanValue();
                    break;
                } catch (Exception e102) {
                    com.unisound.common.y.a("set DEBUG_SAVE_OFFLINEASR_SESSION_PCM Error.");
                    return;
                }
            default:
                super.setOption(i, obj);
                break;
        }
    }

    protected int setWakeupWord(List<String> list) {
        if (L() != 0) {
            return ErrorCode.GENERAL_INIT_ERROR;
        }
        if (S() != 0) {
            return ErrorCode.GENERAL_COMPILER_INIT_ERROR;
        }
        if (list == null || list.size() <= 0) {
            return -1;
        }
        this.p.b(list);
        this.p.a(true);
        if (!this.p.c()) {
            return -1;
        }
        a("wakeup", "wakeup", this.f.b("wakeup"), this.p.b(), this.p.d(), this.p.a());
        this.p.a(false);
        return 0;
    }

    protected int setWakeupWord(Map<String, List<String>> map) {
        List<String> listA = this.b.a(map);
        if (listA != null) {
            com.unisound.common.y.c("SpeechUnderstanderInterface: setWakeupWord");
            return setWakeupWord(listA);
        }
        com.unisound.common.y.c("SpeechUnderstanderInterface: setWakeupWord error");
        return -1;
    }

    @Override // com.unisound.sdk.o
    protected void start() {
        start(this.f.e);
    }

    @Override // com.unisound.sdk.o
    protected void start(String str) {
        com.unisound.common.y.c(com.unisound.common.y.v, "start recognition tag is ", str);
        C();
        if (str == null) {
            q(ErrorCode.ASR_SDK_START_ERROR);
            return;
        }
        if (this.F != 2 && !this.N) {
            q(ErrorCode.ASR_SDK_APPKEY_MD5_CHECK_ERROR);
            return;
        }
        this.as = false;
        this.b.O(false);
        this.b.F(com.unisound.c.a.a(this.x, this.ak));
        if (this.mSpeechUnderstanderParams.o()) {
            if (this.b.c.s()) {
                this.b.c.q();
            }
            h(true);
            i(this.mSpeechUnderstanderParams.p());
            k(this.mSpeechUnderstanderParams.q());
        }
        if (y() > 1) {
            e(this.b.Y());
        }
        if (this.b.Y() == 0) {
            if (getFixEngineVersion().contains("V3.")) {
                h(this.b.aD());
                i(this.b.aE());
                j(this.b.aF());
                k(this.b.aG());
            } else {
                g(this.b.aC());
            }
        }
        b(this.b.aL());
        c(this.b.aM());
        if (this.am != null) {
            this.am.removeCallbacksAndMessages(null);
        }
        if (this.o != null) {
            this.o.removeCallbacksAndMessages(null);
        }
        if (this.e != null) {
            this.e.removeCallbacksAndMessages(null);
        }
        if (!this.L) {
            com.unisound.common.y.a("init error " + ErrorCode.toJsonMessage(ErrorCode.GENERAL_INIT_ERROR));
            this.z.onError(1300, ErrorCode.toJsonMessage(ErrorCode.GENERAL_INIT_ERROR));
            return;
        }
        this.u.clear();
        this.v.clear();
        this.w.clear();
        com.unisound.common.v.a();
        this.au = false;
        this.av = false;
        com.unisound.common.y.a();
        if (this.b.X() != this.b.v()) {
            setOption(1011, Integer.valueOf(this.b.X()));
        }
        if (str.contains("oneshot:")) {
            this.b.j(true);
            str = str.split(":")[1];
        } else {
            this.b.j(false);
        }
        if (str.equals("wakeup:netasr")) {
            this.mSpeechUnderstanderParams.h(true);
            str = str.split(":")[0];
        }
        this.I = str;
        int i = this.F;
        if (this.b.A()) {
            this.b.O(true);
            if (this.mSpeechUnderstanderParams.i() != 2) {
                this.F = 0;
            }
            this.b.M(this.mSpeechUnderstanderParams.m());
            if (this.b.Y() == 0) {
                this.b.F(this.b.aA());
                f(100);
                this.b.h(true);
            } else {
                this.b.h(true);
                this.b.F(this.b.aA());
            }
        } else if (str.equals(this.J)) {
            if (this.mSpeechUnderstanderParams.r()) {
                this.F = 0;
                this.b.M(this.mSpeechUnderstanderParams.n());
            } else {
                this.F = 2;
                this.b.M(this.mSpeechUnderstanderParams.m());
            }
            if (this.b.Y() == 0) {
                this.b.F(this.b.aA());
                f(300);
                this.b.h(true);
            } else {
                this.b.h(true);
                this.b.F(this.b.aA());
            }
        } else {
            this.F = this.mSpeechUnderstanderParams.i();
            this.b.d(this.b.k());
            this.b.M(this.mSpeechUnderstanderParams.n());
            this.b.h(false);
            this.b.F(this.b.ay());
        }
        this.b.w(this.mSpeechUnderstanderParams.n());
        this.H.a();
        this.E = "";
        this.C = false;
        this.D = false;
        this.B = null;
        switch (this.F) {
            case 0:
                this.B = new y(this.b, this.o);
                this.e.c(true);
                this.e.d(this.M);
                this.o.a((z) this.B, false, this.ar, (aa) null);
                break;
            case 1:
                this.D = true;
                this.e.c(false);
                this.B = new y(this.b, this.o);
                this.o.a((z) this.B, false, this.ar, (aa) null);
                break;
            default:
                this.C = true;
                this.e.c(true);
                this.e.d(this.M);
                break;
        }
        if (this.b.A()) {
            this.F = i;
        }
        super.start(str);
    }

    @Override // com.unisound.sdk.o
    protected void stop() {
        super.stop();
        this.B = null;
        this.o.d();
    }

    @Override // com.unisound.sdk.o
    protected int u() {
        return super.u();
    }

    protected int unloadGrammar(String str) {
        return L() != 0 ? ErrorCode.GENERAL_INIT_ERROR : this.f.h(str);
    }

    protected void uploadUserData(Map<Integer, List<String>> map) {
        this.o.a(map);
    }

    @Override // com.unisound.sdk.o
    protected String v() {
        return super.v();
    }

    @Override // com.unisound.sdk.o
    protected String w() {
        return super.w();
    }

    @Override // com.unisound.sdk.o
    protected int x() {
        return super.x();
    }

    @Override // com.unisound.sdk.o
    protected int y() {
        return super.y();
    }

    protected int z() {
        if ((this.G != -1 && this.G != 2) || this.F != 2) {
            if (this.F != 2) {
                if (p(this.x)) {
                    this.N = true;
                } else {
                    q(ErrorCode.ASR_SDK_APPKEY_MD5_CHECK_ERROR);
                }
            }
            if (this.b != null) {
                this.b.G("normal");
            }
        }
        if (this.L) {
            return -1;
        }
        if ((this.G == -1 || this.G == 1) && this.F == 1) {
            this.L = true;
            if (this.am == null) {
                return 0;
            }
            this.am.sendEmptyMessage(1129);
            return 0;
        }
        this.L = true;
        r();
        int iG = g(this.b.aJ());
        this.M = true;
        com.unisound.common.y.c("SpeechUnderstanderInterface : loadResult = ", Integer.valueOf(iG));
        return iG;
    }
}
