package com.tencent.bugly.proguard;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class aq extends k {
    private static Map<String, String> i = new HashMap();

    /* renamed from: a, reason: collision with root package name */
    public long f185a = 0;
    public byte b = 0;
    public String c = "";
    public String d = "";
    public String e = "";
    public Map<String, String> f = null;
    public String g = "";
    public boolean h = true;

    @Override // com.tencent.bugly.proguard.k
    public final void a(j jVar) throws UnsupportedEncodingException {
        jVar.a(this.f185a, 0);
        jVar.a(this.b, 1);
        if (this.c != null) {
            jVar.a(this.c, 2);
        }
        if (this.d != null) {
            jVar.a(this.d, 3);
        }
        if (this.e != null) {
            jVar.a(this.e, 4);
        }
        if (this.f != null) {
            jVar.a((Map) this.f, 5);
        }
        if (this.g != null) {
            jVar.a(this.g, 6);
        }
        jVar.a(this.h, 7);
    }

    static {
        i.put("", "");
    }

    @Override // com.tencent.bugly.proguard.k
    public final void a(i iVar) {
        this.f185a = iVar.a(this.f185a, 0, true);
        this.b = iVar.a(this.b, 1, true);
        this.c = iVar.b(2, false);
        this.d = iVar.b(3, false);
        this.e = iVar.b(4, false);
        this.f = (Map) iVar.a((i) i, 5, false);
        this.g = iVar.b(6, false);
        boolean z = this.h;
        this.h = iVar.a(7, false);
    }
}
