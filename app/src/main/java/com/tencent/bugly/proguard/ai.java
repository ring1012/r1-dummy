package com.tencent.bugly.proguard;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class ai extends k implements Cloneable {
    private static ArrayList<String> c;

    /* renamed from: a, reason: collision with root package name */
    private String f177a = "";
    private ArrayList<String> b = null;

    @Override // com.tencent.bugly.proguard.k
    public final void a(j jVar) throws UnsupportedEncodingException {
        jVar.a(this.f177a, 0);
        if (this.b != null) {
            jVar.a((Collection) this.b, 1);
        }
    }

    @Override // com.tencent.bugly.proguard.k
    public final void a(i iVar) {
        this.f177a = iVar.b(0, true);
        if (c == null) {
            c = new ArrayList<>();
            c.add("");
        }
        this.b = (ArrayList) iVar.a((i) c, 1, false);
    }

    @Override // com.tencent.bugly.proguard.k
    public final void a(StringBuilder sb, int i) {
    }
}
