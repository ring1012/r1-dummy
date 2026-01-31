package com.unisound.sdk;

import cn.yunzhisheng.asrfix.JniAsrFix;

/* loaded from: classes.dex */
public class g {

    /* renamed from: a, reason: collision with root package name */
    public static final int f343a = 0;
    public static final int b = -1;
    private static ab d;
    private long c = 0;
    private JniAsrFix e;

    public int a(int i) {
        int iA = -1;
        if (this.c == 0) {
            com.unisound.common.y.c("compile  setCompileMaxPronunciation fail handle=0");
        } else {
            iA = this.e.a(this.c, i);
            if (iA == 0) {
                com.unisound.common.y.c("compile  setCompileMaxPronunciation ok");
            } else {
                com.unisound.common.y.a("compile  setCompileMaxPronunciation fail code = " + iA);
            }
        }
        return iA;
    }

    public int a(String str, String str2) {
        return this.e.a(this.c, str, str2);
    }

    public int a(String str, String str2, String str3) {
        return this.e.a(this.c, str, str2, str3);
    }

    public int a(String str, String str2, String str3, String str4) {
        if (this.c == 0) {
            com.unisound.common.y.c("compile  compileUserData fail handle=0");
        }
        int iPartialCompileUserData = this.e.partialCompileUserData(this.c, str, str2, str3, str4, str);
        com.unisound.common.y.c("compile  code = ", Integer.valueOf(iPartialCompileUserData));
        if (iPartialCompileUserData == 0) {
            com.unisound.common.y.c("compile  compileUserData ok");
            return iPartialCompileUserData;
        }
        if (iPartialCompileUserData == -10) {
            com.unisound.common.y.a("compile compileUserData partialfile error, autofix ok");
            return 0;
        }
        com.unisound.common.y.a("compile  compileUserData fail code = " + iPartialCompileUserData);
        return iPartialCompileUserData;
    }

    public int a(String str, String str2, String str3, String str4, String str5) {
        return this.e.a(str, str2, str3, str4, str5, str);
    }

    public void a(ab abVar) {
        d = abVar;
    }

    public boolean a() {
        return this.c != 0;
    }

    public boolean a(String str) {
        this.e = JniAsrFix.a();
        this.c = this.e.initUserDataCompiler(str);
        if (com.unisound.common.y.l) {
            com.unisound.common.y.c("compile  initUserDataCompiler handle=", Long.valueOf(this.c), "path = ", str);
        } else {
            com.unisound.common.y.c("compile  initUserDataCompiler handle=", Long.valueOf(this.c));
        }
        return this.c != 0 || this.c == 0;
    }

    public int b(int i) {
        int iB = -1;
        if (this.c == 0) {
            com.unisound.common.y.c("compile  setCompileOverMaxPronunciationInsertAction fail handle=0");
        } else {
            iB = this.e.b(this.c, i);
            if (iB == 0) {
                com.unisound.common.y.c("compile  setCompileOverMaxPronunciationInsertAction ok");
            } else {
                com.unisound.common.y.a("compile  setCompileOverMaxPronunciationInsertAction fail code = " + iB);
            }
        }
        return iB;
    }

    public int b(String str) {
        return this.e.unloadGrammar(str);
    }

    public void b() {
        if (this.c != 0) {
            com.unisound.common.y.c("compile  destroyUserDataCompiler");
            this.e.destroyUserDataCompiler(this.c);
            this.e.v();
            this.c = 0L;
        }
    }

    public String c() {
        if (this.c != 0) {
            return this.e.a(this.c);
        }
        com.unisound.common.y.c("compile  getCompileErrorDetailInfo fail handle=0");
        return "";
    }
}
