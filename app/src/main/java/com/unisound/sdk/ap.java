package com.unisound.sdk;

import com.unisound.client.ErrorCode;

/* loaded from: classes.dex */
class ap implements ag {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ ao f301a;

    ap(ao aoVar) {
        this.f301a = aoVar;
    }

    @Override // com.unisound.sdk.ag
    public void a() {
        com.unisound.common.y.a("Recognizer timeout(" + this.f301a.q.a() + ")");
        com.unisound.common.y.c("Ontimer:cancelRecognition()");
        this.f301a.d(true);
        if (this.f301a.i != null) {
            this.f301a.i.b(ErrorCode.RECOGNITION_TIMEOUT);
        }
    }
}
