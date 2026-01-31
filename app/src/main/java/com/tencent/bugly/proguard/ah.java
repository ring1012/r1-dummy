package com.tencent.bugly.proguard;

import java.io.UnsupportedEncodingException;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class ah extends k implements Cloneable {

    /* renamed from: a, reason: collision with root package name */
    public String f176a = "";
    public String b = "";
    public String c = "";
    private String e = "";
    public String d = "";

    @Override // com.tencent.bugly.proguard.k
    public final void a(j jVar) throws UnsupportedEncodingException {
        jVar.a(this.f176a, 0);
        if (this.b != null) {
            jVar.a(this.b, 1);
        }
        if (this.c != null) {
            jVar.a(this.c, 2);
        }
        if (this.e != null) {
            jVar.a(this.e, 3);
        }
        if (this.d != null) {
            jVar.a(this.d, 4);
        }
    }

    @Override // com.tencent.bugly.proguard.k
    public final void a(i iVar) {
        this.f176a = iVar.b(0, true);
        this.b = iVar.b(1, false);
        this.c = iVar.b(2, false);
        this.e = iVar.b(3, false);
        this.d = iVar.b(4, false);
    }

    @Override // com.tencent.bugly.proguard.k
    public final void a(StringBuilder sb, int i) {
    }
}
