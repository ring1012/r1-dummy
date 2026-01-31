package com.unisound.sdk;

import android.content.Context;

/* loaded from: classes.dex */
public class as extends cn.yunzhisheng.asr.a {
    public static final String aB = "en";
    public static final String aC = "co";
    public static final String aD = "cn";
    public static final String aE = "oral";
    public static final String aF = "cn_en_mix";
    public static final int aL = 1;
    public static final int aM = 2;
    public static final int aN = 3;
    public static final int aO = 4;
    public static final int aP = 1;
    public static final int aQ = 4;
    public static final int aR = 8;
    public static final int aS = 5;
    public static final int aT = 9;
    public static final int aU = 12;
    public static final int aV = 13;
    public boolean aG;
    public boolean aH;
    public boolean aI;
    public boolean aJ;
    public boolean aK;
    public int aW;
    public boolean aX;
    public int aY;
    int aZ;
    public final String ax;
    private String bA;
    private i bB;
    private boolean bC;
    private com.unisound.common.ak bD;
    private com.unisound.common.ak bE;
    private cl bF;
    private ct bG;
    private boolean bH;
    int ba;
    int bb;
    int bc;
    int bd;
    public int be;
    public int bf;
    public boolean bg;
    public String bh;
    String bi;
    String bj;
    private boolean bk;
    private String bl;
    private String bm;
    private String bn;
    private String bo;
    private String bp;
    private String bq;
    private long br;
    private long bs;
    private int bt;
    private String bu;
    private boolean bv;
    private boolean bw;
    private int bx;
    private String by;
    private String bz;
    public static String ay = "http://u.hivoice.cn:8081/casr/upload";
    public static String az = i.i;
    public static String aA = i.j;
    private static com.unisound.common.a aw = new com.unisound.common.a(aq.f302a);

    public as(Context context) {
        super(context);
        this.ax = "/USCService/WebApi";
        this.aG = false;
        this.aH = false;
        this.aI = true;
        this.aJ = true;
        this.aK = true;
        this.bk = true;
        this.aW = 0;
        this.aX = false;
        this.aY = 0;
        this.bl = "";
        this.bm = "";
        this.bn = "";
        this.bo = "";
        this.bp = null;
        this.bq = null;
        this.br = 0L;
        this.bs = 0L;
        this.bt = 0;
        this.bu = "";
        this.bv = false;
        this.bw = false;
        this.aZ = 0;
        this.ba = 8;
        this.bb = 1;
        this.bc = 3000;
        this.bd = 20;
        this.by = "";
        this.bz = null;
        this.bA = "";
        this.bB = new i();
        this.bC = false;
        this.bD = new com.unisound.common.ak(-1, "");
        this.bE = new com.unisound.common.ak(-1, "");
        this.bF = new cl();
        this.bG = new ct();
        this.be = 1;
        this.bf = 16000;
        this.bg = false;
        this.bh = "";
        this.bi = "";
        this.bj = "";
        this.bH = true;
    }

    public void A(String str) {
        if (str != null) {
            String[] strArrSplit = str.split("=");
            if (strArrSplit.length != 2) {
                com.unisound.common.y.c("engineParma ->set serviceParam error");
            } else {
                this.bB.a(strArrSplit[0], strArrSplit[1]);
            }
        }
    }

    public void B(String str) {
        cp.f336a = "http://" + str + "/casr/upload";
    }

    public void C(String str) {
        cp.f336a = str;
    }

    public void D(String str) {
        this.bo = str;
    }

    public void E(String str) {
        this.bp = str;
    }

    public void F(String str) {
        if (str != null) {
            this.by = str;
        } else {
            this.by = "";
        }
    }

    public void G(String str) {
        this.bB.i(str);
        com.unisound.common.y.c("RecognizerParams:setProductLine do ", str);
    }

    public void H(String str) {
        this.bq = str;
    }

    public void I(String str) {
        this.bh = str;
    }

    public void I(boolean z) {
        this.bC = z;
    }

    public void J(boolean z) {
        this.aX = z;
    }

    public void K(boolean z) {
        this.bk = z;
    }

    public void L(boolean z) {
        this.bB.d(z ? i.d : i.e);
    }

    public void M(boolean z) {
        g(z);
    }

    public void N(int i) {
        this.bf = i;
    }

    public void N(boolean z) {
        this.aH = z;
    }

    public void O(int i) {
        this.be = i;
        if (i == 8 || i == 12 || i == 9 || i == 13) {
            this.bG.b(true);
        } else {
            this.bG.b(false);
        }
        this.bB.a(true);
    }

    public void O(boolean z) {
        this.bB.d(z);
    }

    public void P(boolean z) {
        this.bB.e(z);
    }

    public boolean P(int i) {
        if (!this.bB.a(i)) {
            return false;
        }
        super.C(i);
        return true;
    }

    public void Q(int i) {
        this.bt = i;
    }

    public void Q(boolean z) {
        this.bB.f(z);
    }

    public void R(int i) {
        this.bx = i;
    }

    public void R(boolean z) {
        this.bB.h(z);
    }

    public void S(int i) {
        this.bB.b(i);
    }

    public void S(boolean z) {
        this.bg = z;
    }

    public void T(int i) {
        this.bB.c(i);
    }

    public void T(boolean z) {
        this.bH = z;
        this.bB.g(z);
        com.unisound.common.y.c("RecognizerParams:setPunctuated ", Boolean.valueOf(z));
    }

    public void U(boolean z) {
        this.bv = z;
    }

    public void V(boolean z) {
        this.bw = z;
    }

    public void a(com.unisound.common.ak akVar) {
        this.bD.a(akVar);
        this.bE.a(akVar);
    }

    public void a(at atVar) {
        if (atVar == at.VPR) {
            this.bB.a(false);
            this.bF.a(false);
            this.bG.b(true);
        } else if (atVar == at.ASR_NLU) {
            this.bB.a(true);
            this.bG.b(false);
            this.bF.a(true);
        } else {
            this.bB.a(false);
            this.bF.a(false);
            this.bG.b(false);
        }
    }

    void a(String str, int i) {
        aw.b(str);
        aw.b(i);
    }

    public int aP() {
        return this.bf;
    }

    public int aQ() {
        return this.be;
    }

    public boolean aR() {
        return this.bC;
    }

    public String aS() {
        return this.bi;
    }

    public String aT() {
        return this.bj;
    }

    public String aU() {
        return this.bA;
    }

    public String aV() {
        return this.bz;
    }

    public ct aW() {
        return this.bG;
    }

    public cl aX() {
        return this.bF;
    }

    public com.unisound.common.ak aY() {
        return this.bD;
    }

    public String aZ() {
        return this.bl;
    }

    void b(String str, int i) {
        aw.a(str);
        aw.a(i);
        com.unisound.common.y.c("RecognizerParams:setDefaultServer server ", str, ",port ", Integer.valueOf(i));
    }

    public i ba() {
        return this.bB;
    }

    com.unisound.common.a bb() {
        return aw;
    }

    void bc() {
        aw.e();
    }

    public boolean bd() {
        return this.bk;
    }

    public int be() {
        return this.bB.f();
    }

    public boolean bf() {
        return this.aH;
    }

    public com.unisound.common.ak bg() {
        return this.bE;
    }

    public boolean bh() {
        return this.aI;
    }

    public String bi() {
        return this.bm;
    }

    public String bj() {
        return this.bn;
    }

    public long bk() {
        return this.br;
    }

    public long bl() {
        return this.bs;
    }

    public long bm() {
        long j = this.bs - this.br;
        if (j >= 3600000 || j <= 0) {
            return 0L;
        }
        return j;
    }

    public int bn() {
        return this.bt;
    }

    public String bo() {
        return this.bu;
    }

    public String bp() {
        return cp.f336a;
    }

    public String bq() {
        return this.bo;
    }

    public String br() {
        return this.bp;
    }

    public String bs() {
        return this.by;
    }

    public int bt() {
        return this.bx;
    }

    public String bu() {
        return this.bq;
    }

    public boolean bv() {
        return this.bg;
    }

    public String bw() {
        return this.bh;
    }

    public boolean bx() {
        return this.bH;
    }

    public boolean by() {
        return this.bv;
    }

    public boolean bz() {
        return this.bw;
    }

    @Override // cn.yunzhisheng.asr.a
    public void d(String str) {
        super.d(str);
        this.bB.f(str);
        this.bF.p(f(str));
    }

    public void j(long j) {
        this.br = j;
    }

    public void k(long j) {
        this.bs = j;
    }

    public void k(String str) {
        this.bi = str;
    }

    public void l(String str) {
        this.bj = str;
    }

    public void m(String str) {
        this.bA = str;
    }

    public void n(String str) {
        this.bz = str;
    }

    public void o(String str) {
        this.bl = str;
    }

    public void p(String str) {
        if (str == null) {
            com.unisound.common.y.a("RecognizerParams:setLanguage error language == null ");
            return;
        }
        com.unisound.common.y.c("RecognizerParams:setLanguage in ", str);
        aw.a(aq.a(str));
        this.af = false;
        cp.f336a = ay;
        if (str.equals("en") || str.equals("co") || str.equals(aE)) {
            return;
        }
        if (str.equals(aF)) {
            cp.f336a = "http://" + aw.b() + ":9006/casr/upload";
        } else {
            com.unisound.common.y.c("RecognizerParams:setLanguage do ", "cn");
        }
    }

    public void q(String str) {
        this.bB.a(str);
        com.unisound.common.y.c("RecognizerParams:setLanguage do ", str);
    }

    public boolean r(String str) {
        return this.bB.b(str);
    }

    public boolean s(String str) {
        return this.bB.c(str);
    }

    public boolean t(String str) {
        if (str == null) {
            return false;
        }
        String[] strArrSplit = str.split(":");
        if (strArrSplit.length != 2 || strArrSplit[0].length() == 0) {
            return false;
        }
        try {
            short sIntValue = (short) Integer.valueOf(strArrSplit[1]).intValue();
            a(strArrSplit[0], sIntValue);
            b(strArrSplit[0], sIntValue);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean u(String str) {
        if (str == null) {
            return false;
        }
        String[] strArrSplit = str.split(":");
        if (strArrSplit.length != 2 || strArrSplit[0].length() == 0) {
            return false;
        }
        try {
            co.f335a = "http://" + strArrSplit[0] + ":" + ((int) ((short) Integer.valueOf(strArrSplit[1]).intValue()));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void v(String str) {
        this.bB.d(str);
    }

    public void w(String str) {
        this.bm = str;
    }

    public void x(String str) {
        if (str.equals("")) {
            this.bn = com.unisound.c.a.q + System.currentTimeMillis();
        } else {
            this.bn = str;
        }
    }

    public void y(String str) {
        this.bu = str;
    }

    public void z(String str) {
        this.bB.h(str);
    }
}
