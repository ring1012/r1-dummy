package com.unisound.sdk;

/* loaded from: classes.dex */
public class cj {
    public String a(ae aeVar) {
        return ce.a(aeVar.a(), aeVar.l(), aeVar.b(), aeVar.c(), aeVar.e(), aeVar.g(), aeVar.f(), aeVar.d(), aeVar.h(), aeVar.i(), aeVar.j(), aeVar.k(), aeVar.m(), aeVar.n(), aeVar.o(), aeVar.p(), aeVar.q(), aeVar.r());
    }

    public String b(ae aeVar) {
        String strA = a(aeVar);
        String strA2 = com.unisound.common.u.a(strA);
        com.unisound.common.y.c("nlu url: ", strA);
        return strA2;
    }

    public String c(ae aeVar) {
        String strA = a(aeVar);
        String strA2 = com.unisound.common.u.a(strA, "");
        com.unisound.common.y.c("nlu url: ", strA);
        return strA2;
    }
}
