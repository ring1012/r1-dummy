package com.unisound.sdk;

import android.content.Context;
import android.text.TextUtils;
import cn.yunzhisheng.asr.JniUscClient;
import com.unisound.client.ErrorCode;
import com.unisound.client.SpeechConstants;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class an extends Thread {

    /* renamed from: a, reason: collision with root package name */
    private static final int f299a = 30;
    private as e;
    private Context k;
    private ak b = null;
    private BlockingQueue<byte[]> c = new LinkedBlockingQueue();
    private ai d = new ai();
    private String f = "";
    private volatile boolean g = false;
    private String h = "";
    private String i = "";
    private String j = "";
    private String l = "";
    private boolean m = true;

    public an(as asVar, Context context, String str) {
        this.k = context;
        this.e = asVar;
        ai.f296a = true;
    }

    private void a(int i) {
        com.unisound.common.y.c("NetRecognition --> RecognitionEvent = " + i);
        ak akVar = this.b;
        if (akVar != null) {
            akVar.k(i);
        }
    }

    private void a(int i, JniUscClient jniUscClient) {
        ak akVar = this.b;
        b();
        com.unisound.common.y.a(jniUscClient);
        if (akVar != null) {
            akVar.a(i);
        }
    }

    private void a(JniUscClient jniUscClient) {
        if (this.e.ap()) {
            jniUscClient.a(208, 3);
            jniUscClient.a(209, 3);
        }
        if (com.unisound.common.y.l) {
            jniUscClient.a(208, 3);
            jniUscClient.a(209, 3);
        }
        com.unisound.common.y.a(com.unisound.common.y.y, (String) null, (Map<String, String>) null, (String) null, (String) null, (String) null);
        com.unisound.common.a aVarBb = this.e.bb();
        long jA = jniUscClient.a(aVarBb.b(), aVarBb.c());
        com.unisound.common.y.c("NetRecognition -- > server = ", aVarBb.b(), " ip = ", aVarBb.a(), " port = ", Integer.valueOf(aVarBb.c()));
        HashMap map = new HashMap();
        map.put("server", aVarBb.a());
        map.put("port", String.valueOf(aVarBb.c()));
        com.unisound.common.y.a(com.unisound.common.y.z, (String) null, map, (String) null, (String) null, (String) null);
        if (jA == 0) {
            com.unisound.common.y.a(com.unisound.common.y.z, com.unisound.common.y.I, map, (String) null, (String) null, "handle=0");
            com.unisound.common.y.c(toString(), "juc.create() returns ", Long.valueOf(jA));
        }
        jniUscClient.a(0, this.e.bb);
        jniUscClient.a(1, this.e.bc);
        jniUscClient.a(6, this.e.ba);
        jniUscClient.a(4, this.e.bd);
        HashMap map2 = new HashMap();
        map2.put("enable_vad", String.valueOf(this.e.bb));
        map2.put("vad_timeout", String.valueOf(this.e.bc));
        map2.put("pcm_compress", String.valueOf(this.e.ba));
        map2.put("result_timeout", String.valueOf(this.e.bd));
        com.unisound.common.y.a(com.unisound.common.y.z, (String) null, map2, (String) null, (String) null, (String) null);
        b(jniUscClient);
        d(jniUscClient);
        c(jniUscClient);
        if (!this.e.an()) {
            a(jniUscClient, "Start");
            StringBuilder sb = new StringBuilder();
            if (this.e.aX().y()) {
                if (this.e.aW().d() && !this.e.aX().w()) {
                    sb.append(this.i);
                } else if (!this.e.aW().d() && this.e.aX().w()) {
                    sb.append(this.j);
                } else if (this.e.aW().d() && this.e.aX().w()) {
                    sb.append(this.i).append(this.j);
                }
                sb.append("filterName=nlu2;");
            } else if (this.e.aW().d() && !this.e.aX().w()) {
                sb.append(this.i).append("filterName=vpr;");
            } else if (!this.e.aW().d() && this.e.aX().w()) {
                sb.append(this.j).append("filterName=search;");
            } else if (this.e.aW().d() && this.e.aX().w()) {
                sb.append(this.i).append(this.j).append("filterName=vpr,search;");
            }
            if (this.e.aS() != null && !"".equals(this.e.aS())) {
                sb.append("additionalService=" + this.e.aS() + ";");
            } else if (this.e.aR()) {
                sb.append("additionalService=wx_adapt;");
            }
            if (this.e.bt() > 0) {
                sb.append("req_nlu_length=").append(this.e.bt() + ";");
            }
            if (this.e.aX().z() != -1) {
                sb.append("appendLength=").append(this.e.aX().z() + ";");
            }
            if (this.e.bv()) {
                sb.append("maxWell=").append(this.e.bv() + ";");
            }
            if (this.e.bw() != null && !this.e.bw().equals("")) {
                sb.append("retType=").append(this.e.bw() + ";");
            }
            sb.append("fullDuplex=").append(this.e.bz() + ";");
            sb.append("audioUrl=").append(this.e.by() + ";");
            sb.append("filterUrl=").append(this.e.bq() + ";");
            if (this.e.aT() == null || this.e.aT().equals("")) {
                jniUscClient.a(201, sb.toString());
                com.unisound.common.y.c("juc init traficParams = ", sb.toString());
            }
        }
        if (this.e.aT() != null && !this.e.aT().equals("")) {
            jniUscClient.a(201, this.e.aT());
            com.unisound.common.y.c("juc init external setting traficParams = ", this.e.aT());
        }
        if (this.e.aP() != this.e.be()) {
            jniUscClient.a(JniUscClient.au, this.e.aP());
            jniUscClient.a(JniUscClient.av, this.e.be());
        }
        com.unisound.common.y.x = 0;
        com.unisound.common.y.c("juc init success");
    }

    private void a(JniUscClient jniUscClient, com.unisound.common.ak akVar) {
        if (akVar == null || !akVar.a()) {
            return;
        }
        com.unisound.common.y.a("updateAsrScene " + akVar.c() + " res : " + jniUscClient.a(31, akVar.c()));
        akVar.a(false);
    }

    private void a(JniUscClient jniUscClient, String str) {
        cl clVarAX = this.e.aX();
        clVarAX.b(System.currentTimeMillis());
        if (clVarAX == null || !clVarAX.w()) {
            return;
        }
        this.j = clVarAX.A();
        com.unisound.common.y.c("NetRecognition --> ", str, " NluParams : ", clVarAX.A());
        HashMap map = new HashMap();
        map.put("nlu_sendParams", clVarAX.A());
        com.unisound.common.y.a(com.unisound.common.y.B, (String) null, map, (String) null, (String) null, str);
    }

    private void a(JniUscClient jniUscClient, String str, String str2, boolean z) {
        if (str2.equals(JniUscClient.az)) {
            return;
        }
        al alVar = new al();
        alVar.a(am.NET);
        alVar.b(str);
        String strC = jniUscClient.c(61);
        alVar.a(strC);
        if (str2.equals(JniUscClient.aB)) {
            alVar.b(true);
        } else {
            if (z) {
                String strC2 = jniUscClient.c(62);
                if (TextUtils.isEmpty(strC2) || Integer.valueOf(strC2).intValue() <= 0) {
                    alVar.a(true);
                } else {
                    alVar.a(false);
                }
            }
            alVar.b(false);
        }
        a(alVar);
        if (!TextUtils.isEmpty(strC) && b(strC).a() != 0) {
            this.m = false;
        }
        if (this.m) {
            return;
        }
        a(ErrorCode.ASR_TOKEN_ERROR, jniUscClient);
    }

    private void a(JniUscClient jniUscClient, String str, boolean z) {
        while (true) {
            String strC = jniUscClient.c(60);
            if (strC.equals(JniUscClient.aA)) {
                return;
            } else {
                a(jniUscClient, str, strC, z);
            }
        }
    }

    private void a(al alVar) {
        com.unisound.common.y.c("NetRecognition --> onRecognitionResult=>" + alVar.toString());
        ak akVar = this.b;
        if (akVar != null) {
            akVar.a(alVar);
        }
    }

    private String[] a(String str) {
        int i = 0;
        if (str.contains("}{\"asr_recongize\"")) {
            String[] strArrSplit = str.split("\\}\\{\"asr_recongize\"");
            while (i < strArrSplit.length) {
                if (i == 0) {
                    strArrSplit[i] = strArrSplit[i] + "}";
                } else if (i == strArrSplit.length - 1) {
                    strArrSplit[i] = "{\"asr_recongize\"" + strArrSplit[i];
                } else {
                    strArrSplit[i] = "{\"asr_recongize\"" + strArrSplit[i] + "}";
                }
                i++;
            }
            return strArrSplit;
        }
        if (!str.contains("}{\"gender\"")) {
            return new String[]{str};
        }
        String[] strArrSplit2 = str.split("\\}\\{\"gender\"");
        while (i < strArrSplit2.length) {
            if (i == 0) {
                strArrSplit2[i] = strArrSplit2[i] + "}";
            } else if (i == strArrSplit2.length - 1) {
                strArrSplit2[i] = "{\"gender\"" + strArrSplit2[i];
            } else {
                strArrSplit2[i] = "{\"gender\"" + strArrSplit2[i] + "}";
            }
            i++;
        }
        return strArrSplit2;
    }

    private cu b(String str) throws JSONException {
        cu cuVar = new cu();
        com.unisound.common.y.d("partial=", str);
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (str.contains("returnCode")) {
                int i = jSONObject.getInt("returnCode");
                com.unisound.common.y.d("USCDEBUG", "returnCode=" + i);
                cuVar.a(i);
                if (i == 0) {
                    com.unisound.common.y.d("errorInfo=");
                    cuVar.a("");
                } else {
                    String string = jSONObject.getString("errorInfo");
                    com.unisound.common.y.d("USCDEBUG", "errorInfo=" + string);
                    cuVar.a(string);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cuVar;
    }

    private void b(JniUscClient jniUscClient) {
        ct ctVarAW = this.e.aW();
        if (ctVarAW.d()) {
            jniUscClient.a(1015, 8);
            jniUscClient.a(1020, 1);
            jniUscClient.a(1019, this.e.bi());
            StringBuilder sb = new StringBuilder();
            sb.append("type=");
            if (ctVarAW.e() == 1) {
                sb.append("register");
            } else if (ctVarAW.e() == 2) {
                sb.append("matchSingle");
            }
            sb.append(";");
            sb.append("userName=").append(ctVarAW.b()).append(";");
            sb.append("appkey=").append(this.e.aZ()).append(";");
            sb.append("returnType=").append("json").append(";");
            sb.append("scene=").append(ctVarAW.a()).append(";");
            com.unisound.common.y.c("vpr params:  ", sb.toString());
            this.i = sb.toString();
            HashMap map = new HashMap();
            map.put("vpr_init", String.valueOf(8));
            map.put("vpr_md5_check", String.valueOf(1));
            map.put("vpr_secret", this.e.bi());
            map.put("vpr_sendParams", sb.toString());
            com.unisound.common.y.a(com.unisound.common.y.A, (String) null, map, (String) null, (String) null, (String) null);
        }
    }

    private void c(JniUscClient jniUscClient) {
        StringBuilder sb = new StringBuilder();
        sb.append("PN=" + com.unisound.c.a.s).append(":");
        sb.append("OS=0").append(":");
        sb.append("CR=" + com.unisound.c.a.r).append(":");
        sb.append("NT=" + this.e.aZ).append(":");
        sb.append("MD=" + com.unisound.c.a.t).append(":");
        sb.append("SV=" + com.unisound.common.an.a()).append(":");
        sb.append("RPT=" + this.e.bm()).append(":");
        sb.append("SID=" + this.e.bj()).append(":");
        sb.append("NPT=" + this.e.bo()).append(":");
        sb.append("IP=" + com.unisound.common.ae.a(this.k)).append(":");
        sb.append("EC=" + this.e.bn());
        sb.append("\t" + com.unisound.common.y.x + ":" + JniUscClient.l + ":" + JniUscClient.m);
        jniUscClient.a(15, sb.toString());
        this.e.Q(0);
        this.e.k(0L);
        this.e.x("");
        HashMap map = new HashMap();
        map.put("nlu_sendParams", sb.toString());
        com.unisound.common.y.c("collected_info = ", sb.toString());
        com.unisound.common.y.a(com.unisound.common.y.C, (String) null, map, (String) null, (String) null, (String) null);
    }

    private void d(JniUscClient jniUscClient) {
        i iVarBa = this.e.ba();
        if (iVarBa.a()) {
            HashMap map = new HashMap();
            jniUscClient.a(34, iVarBa.toString());
            map.put("engine_parameter", iVarBa.toString());
            com.unisound.common.y.c("NetRecognition --> AsrParams : ", iVarBa.toString().replaceAll(":", "=").replaceAll("\\n", ";"));
            a(jniUscClient, this.e.aY());
            if (this.e.aG) {
                if (this.e.aJ) {
                    jniUscClient.a(20, JniUscClient.r);
                } else {
                    jniUscClient.a(20, JniUscClient.s);
                }
            }
            if (this.e.aW != 0) {
                jniUscClient.a(32, this.e.aW);
            }
            jniUscClient.a(this.e.bf());
            if (!TextUtils.isEmpty(this.e.aq()) && c.b.equals(this.e.aq())) {
                com.unisound.common.y.c("NetRecognition --> setAudioFormat : ", Integer.valueOf(jniUscClient.a(c.b)));
            }
        }
        jniUscClient.a(9, this.e.aZ());
        jniUscClient.a(8, com.unisound.c.a.q);
        String strA = com.unisound.c.a.a(this.e.aZ());
        jniUscClient.a(14, this.e.bu() != null ? this.e.bu() : strA);
        jniUscClient.a(22, strA);
        com.unisound.common.y.c("NetRecognition --> appkey = ", this.e.aZ(), ", imei = ", com.unisound.c.a.q, ", userId = ", strA, ", udid = ", strA);
        if (this.e.bh()) {
            jniUscClient.a(17, JniUscClient.p);
        }
        if (this.e.br() != null) {
            jniUscClient.a(JniUscClient.ac, this.e.br());
        }
    }

    private void e(JniUscClient jniUscClient) {
        ak akVar = this.b;
        b();
        com.unisound.common.y.a(jniUscClient);
        if (akVar != null) {
            akVar.i();
        }
    }

    private void f(JniUscClient jniUscClient) {
        ak akVar = this.b;
        b();
        com.unisound.common.y.a(jniUscClient);
        if (akVar != null) {
            akVar.m();
        }
    }

    private int g(JniUscClient jniUscClient) throws Throwable {
        com.unisound.common.y.c("NetRecognition --> continue recognize");
        jniUscClient.b();
        if (this.e.ar()) {
            String strAs = this.e.as();
            if (!TextUtils.isEmpty(strAs)) {
                com.unisound.common.j.a(strAs + File.separator + this.e.bj() + ".wav", 1, this.e.be());
            }
        }
        a(jniUscClient, this.f, true);
        if (!this.m) {
            return -1;
        }
        this.f = "";
        com.unisound.common.y.c("NetRecognition --> start called ");
        a(jniUscClient);
        com.unisound.common.y.c("RecognitionThread : Service Mode =  ", Integer.valueOf(this.e.aQ()));
        jniUscClient.a(1015, this.e.aQ());
        jniUscClient.a(JniUscClient.al, this.e.bs());
        int iA = jniUscClient.a();
        this.f = jniUscClient.c(21);
        this.e.x(this.f);
        a(SpeechConstants.ASR_EVENT_SESSIONID_CHANGED);
        com.unisound.common.y.c("NetRecognition --> sessionId = ", this.f);
        this.l = "";
        if (this.f != null && this.f.length() > 10) {
            this.l = this.f.substring(0, 10);
        }
        if (iA == 0) {
            return 0;
        }
        com.unisound.common.y.a("start", com.unisound.common.y.I, (Map<String, String>) null, (String) null, String.valueOf(iA), (String) null);
        com.unisound.common.y.c("NetRecognition --> continue Recognition start error occured! , startCode = ", Integer.valueOf(iA), ", sessionId = ", this.f);
        a(iA, jniUscClient);
        jniUscClient.e();
        this.e.bc();
        return -1;
    }

    public void a(ak akVar) {
        this.b = akVar;
    }

    public void a(Collection<? extends byte[]> collection) {
        this.c.addAll(collection);
    }

    public void a(List<byte[]> list) {
        Iterator<byte[]> it = list.iterator();
        while (it.hasNext()) {
            this.c.add(it.next());
        }
    }

    public void a(byte[] bArr) {
        this.d.a(bArr, 0, bArr.length);
        if (this.d.b()) {
            this.c.add(bArr);
        }
    }

    public boolean a() {
        return this.g;
    }

    public void b() {
        this.g = true;
    }

    public void c() {
        this.b = null;
        this.g = true;
    }

    public boolean d() {
        return this.b == null;
    }

    public String e() {
        return this.f;
    }

    public void f() {
        ak akVar = this.b;
        if (akVar != null) {
            akVar.A();
        }
    }

    public void g() {
        c();
        if (isAlive()) {
            try {
                join(3900L);
                com.unisound.common.y.c("RecognitionThread::waitEnd()");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public String h() {
        return this.h;
    }

    /* JADX WARN: Code restructure failed: missing block: B:106:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:108:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:109:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0149, code lost:
    
        r15.e.j(java.lang.System.currentTimeMillis());
        r15.h = "";
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x015c, code lost:
    
        if (r15.e.an() != false) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x015e, code lost:
    
        a(r11, "Stop");
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0163, code lost:
    
        com.unisound.common.y.c("NetRecognition --> stop called , bufferLength = ", java.lang.Long.valueOf(r0), ", sessionId = ", r15.l);
        r6 = r11.b();
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0185, code lost:
    
        if (r15.e.ar() == false) goto L36;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0187, code lost:
    
        r0 = r15.e.as();
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0191, code lost:
    
        if (android.text.TextUtils.isEmpty(r0) != false) goto L36;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0193, code lost:
    
        com.unisound.common.j.a(r0 + java.io.File.separator + r15.e.bj() + ".wav", 1, r15.e.be());
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x01bf, code lost:
    
        if (r6 >= 0) goto L91;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x01c1, code lost:
    
        com.unisound.common.y.c("NetRecognition --> stop error occured! , stopCode = ", java.lang.Integer.valueOf(r6), ", sessionId = ", r15.f);
        com.unisound.common.y.a("stop", com.unisound.common.y.I, (java.util.Map<java.lang.String, java.lang.String>) null, (java.lang.String) null, java.lang.String.valueOf(r6), (java.lang.String) null);
        a(r6, r11);
        r11.e();
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x030b, code lost:
    
        com.unisound.common.y.a(com.unisound.common.y.E, com.unisound.common.y.I, (java.util.Map<java.lang.String, java.lang.String>) null, (java.lang.String) null, java.lang.String.valueOf(r9), (java.lang.String) null);
        com.unisound.common.y.c("NetRecognition --> error:", java.lang.Integer.valueOf(r9));
        a(r9, r11);
        r11.e();
        r15.e.bc();
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x035f, code lost:
    
        com.unisound.common.y.a("stop", com.unisound.common.y.J, (java.util.Map<java.lang.String, java.lang.String>) null, (java.lang.String) null, (java.lang.String) null, (java.lang.String) null);
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x036d, code lost:
    
        if (r15.e.aW == 0) goto L94;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x036f, code lost:
    
        r15.e.aY = com.unisound.common.t.a(r11.c(25));
        com.unisound.common.y.c("NetRecognition --> asrRspSpeakerInfo=", java.lang.Integer.valueOf(r15.e.aY));
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x0390, code lost:
    
        a(r11, r15.f, true);
        e(r11);
        com.unisound.common.y.c("NetRecognition --> released");
        r11.e();
        r15.c.clear();
        com.unisound.common.y.g("RecognitionThread stop");
        com.unisound.common.y.c("NetRecognition --> run stop");
     */
    /* JADX WARN: Removed duplicated region for block: B:104:0x033e A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0253 A[Catch: Exception -> 0x0280, TryCatch #0 {Exception -> 0x0280, blocks: (B:17:0x0121, B:19:0x012f, B:21:0x0132, B:23:0x0139, B:25:0x0140, B:38:0x01ee, B:40:0x01f4, B:41:0x01fa, B:43:0x0202, B:45:0x020e, B:46:0x0234, B:48:0x0238, B:49:0x023d, B:53:0x0245, B:54:0x024b, B:56:0x0253, B:58:0x025b, B:60:0x0267, B:62:0x026b, B:64:0x0273, B:86:0x0338, B:88:0x033e, B:71:0x02ad, B:74:0x02bf, B:77:0x02de, B:80:0x02fb, B:82:0x0303, B:85:0x030b), top: B:97:0x0121 }] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0267 A[Catch: Exception -> 0x0280, TryCatch #0 {Exception -> 0x0280, blocks: (B:17:0x0121, B:19:0x012f, B:21:0x0132, B:23:0x0139, B:25:0x0140, B:38:0x01ee, B:40:0x01f4, B:41:0x01fa, B:43:0x0202, B:45:0x020e, B:46:0x0234, B:48:0x0238, B:49:0x023d, B:53:0x0245, B:54:0x024b, B:56:0x0253, B:58:0x025b, B:60:0x0267, B:62:0x026b, B:64:0x0273, B:86:0x0338, B:88:0x033e, B:71:0x02ad, B:74:0x02bf, B:77:0x02de, B:80:0x02fb, B:82:0x0303, B:85:0x030b), top: B:97:0x0121 }] */
    /* JADX WARN: Removed duplicated region for block: B:90:0x035c A[LOOP:0: B:97:0x0121->B:90:0x035c, LOOP_END] */
    @Override // java.lang.Thread, java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void run() throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 963
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unisound.sdk.an.run():void");
    }
}
