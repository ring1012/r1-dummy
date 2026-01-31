package com.tencent.bugly.proguard;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class al extends k implements Cloneable {
    private static ArrayList<ak> b;

    /* renamed from: a, reason: collision with root package name */
    public ArrayList<ak> f180a = null;

    @Override // com.tencent.bugly.proguard.k
    public final void a(j jVar) throws UnsupportedEncodingException {
        jVar.a((Collection) this.f180a, 0);
    }

    @Override // com.tencent.bugly.proguard.k
    public final void a(i iVar) {
        if (b == null) {
            b = new ArrayList<>();
            b.add(new ak());
        }
        this.f180a = (ArrayList) iVar.a((i) b, 0, true);
    }

    @Override // com.tencent.bugly.proguard.k
    public final void a(StringBuilder sb, int i) {
    }
}
