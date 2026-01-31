package com.tencent.bugly.proguard;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class ar extends k implements Cloneable {
    private static ArrayList<aq> f;
    private static Map<String, String> g;

    /* renamed from: a, reason: collision with root package name */
    public byte f186a = 0;
    public String b = "";
    public String c = "";
    public ArrayList<aq> d = null;
    public Map<String, String> e = null;

    @Override // com.tencent.bugly.proguard.k
    public final void a(j jVar) throws UnsupportedEncodingException {
        jVar.a(this.f186a, 0);
        if (this.b != null) {
            jVar.a(this.b, 1);
        }
        if (this.c != null) {
            jVar.a(this.c, 2);
        }
        if (this.d != null) {
            jVar.a((Collection) this.d, 3);
        }
        if (this.e != null) {
            jVar.a((Map) this.e, 4);
        }
    }

    @Override // com.tencent.bugly.proguard.k
    public final void a(i iVar) {
        this.f186a = iVar.a(this.f186a, 0, true);
        this.b = iVar.b(1, false);
        this.c = iVar.b(2, false);
        if (f == null) {
            f = new ArrayList<>();
            f.add(new aq());
        }
        this.d = (ArrayList) iVar.a((i) f, 3, false);
        if (g == null) {
            g = new HashMap();
            g.put("", "");
        }
        this.e = (Map) iVar.a((i) g, 4, false);
    }

    @Override // com.tencent.bugly.proguard.k
    public final void a(StringBuilder sb, int i) {
    }
}
