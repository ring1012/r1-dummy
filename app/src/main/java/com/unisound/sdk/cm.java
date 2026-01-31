package com.unisound.sdk;

import com.unisound.client.ErrorCode;

/* loaded from: classes.dex */
public class cm extends Thread {

    /* renamed from: a, reason: collision with root package name */
    public static int f333a = 153600;
    private String b = "";
    private boolean c = false;
    private ck d;
    private cl e;

    public cm(cl clVar) {
        this.e = clVar;
    }

    private void b(int i) {
        if (this.d != null) {
            this.d.a(i);
        }
    }

    private void b(String str) {
        if (this.d != null) {
            this.d.a(str);
        }
    }

    private void c(int i) {
        if (this.d != null) {
            this.d.b(i);
        }
    }

    private boolean c(String str) {
        return str == null || str.length() == 0;
    }

    private ae e() {
        if (this.e == null) {
            return null;
        }
        ae aeVar = new ae(this.e.b(), this.e.c());
        aeVar.f(this.e.g());
        return aeVar;
    }

    private String f() {
        if (c(this.b)) {
            return null;
        }
        com.unisound.common.y.c("NLU processing begin");
        ae aeVarE = e();
        aeVarE.a(this.e.t());
        aeVarE.h(this.e.i());
        aeVarE.i(this.e.j());
        aeVarE.e(this.e.f());
        aeVarE.k(this.e.n());
        aeVarE.b(System.currentTimeMillis());
        aeVarE.o(this.e.r());
        aeVarE.d(this.e.d());
        aeVarE.g(this.b);
        return new cj().c(aeVarE);
    }

    public void a() {
        this.c = true;
    }

    public void a(int i) {
        c();
        if (isAlive()) {
            try {
                join(i);
                com.unisound.common.y.c("USCNluThread::waitEnd()");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void a(ck ckVar) {
        this.d = ckVar;
    }

    public void a(String str) {
        this.b = str;
    }

    public boolean b() {
        return this.c;
    }

    public void c() {
        a();
        this.d = null;
    }

    public boolean d() {
        return this.d == null;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        super.run();
        if (this.c) {
            return;
        }
        try {
            b(1002);
            String strF = f();
            if (strF == "") {
                c(ErrorCode.NLU_REQUEST_EMPTY);
            } else if (strF.equals("{}")) {
                c(ErrorCode.NLU_SERVER_ERROR);
            } else if (strF.equals("Error")) {
                c(ErrorCode.NLU_OTHER_ERROR);
            } else {
                b(strF);
            }
        } catch (Exception e) {
            e.printStackTrace();
            c(ErrorCode.NLU_OTHER_ERROR);
        }
        b(1001);
    }
}
