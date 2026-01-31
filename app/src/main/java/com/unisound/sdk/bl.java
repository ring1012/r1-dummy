package com.unisound.sdk;

import com.unisound.client.ErrorCode;

/* loaded from: classes.dex */
class bl extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ String f317a;
    final /* synthetic */ String b;
    final /* synthetic */ String c;
    final /* synthetic */ String d;
    final /* synthetic */ String e;
    final /* synthetic */ String f;
    final /* synthetic */ bg g;

    bl(bg bgVar, String str, String str2, String str3, String str4, String str5, String str6) {
        this.g = bgVar;
        this.f317a = str;
        this.b = str2;
        this.c = str3;
        this.d = str4;
        this.e = str5;
        this.f = str6;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        boolean z = false;
        synchronized (this.g.aw) {
            int iA = this.g.a(this.f317a, this.b, this.c, this.d);
            if (iA == 0) {
                if (this.e.equals("wakeup")) {
                    this.g.an.sendEmptyMessage(23);
                } else {
                    this.g.an.sendEmptyMessage(22);
                }
                this.g.b(this.d, this.e, this.f);
            } else if (iA == -102) {
                this.g.b.a(this.f, false);
                this.g.b(this.d, this.e, this.f);
                com.unisound.common.y.a("Compile vocab error: " + iA);
                z = true;
            } else {
                this.g.b.a(this.f, false);
                com.unisound.common.y.a("Compile vocab error: " + iA);
                z = true;
            }
            if (z) {
                if (this.e.equals("wakeup")) {
                    this.g.r(ErrorCode.ASR_SDK_WAKEUP_COMPILE_ERROR);
                } else {
                    this.g.r(ErrorCode.ASR_SDK_FIX_COMPILE_ERROR);
                }
            }
        }
    }
}
