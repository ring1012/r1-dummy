package com.unisound.sdk;

import com.unisound.client.ErrorCode;

/* loaded from: classes.dex */
class bj extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ String f315a;
    final /* synthetic */ String b;
    final /* synthetic */ String c;
    final /* synthetic */ bg d;

    bj(bg bgVar, String str, String str2, String str3) {
        this.d = bgVar;
        this.f315a = str;
        this.b = str2;
        this.c = str3;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        synchronized (this.d.aw) {
            int iA = this.d.f.a(this.f315a, this.b, this.c);
            com.unisound.common.y.c("SpeechUnderstanderInterface : insertVocab_ext -> inserVocabResult = ", Integer.valueOf(iA));
            if (iA == 0) {
                this.d.an.sendEmptyMessage(21);
            } else {
                com.unisound.common.y.a("SpeechUnderstanderInterface : insertVocab_ext error ");
                this.d.r(ErrorCode.ASR_SDK_FIX_INSERTVOCAB_EXT_ERROR);
            }
        }
    }
}
