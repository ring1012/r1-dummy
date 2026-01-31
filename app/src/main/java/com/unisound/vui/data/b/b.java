package com.unisound.vui.data.b;

import com.unisound.vui.data.c.c;
import com.unisound.vui.data.c.e;
import java.util.List;
import java.util.concurrent.ExecutorService;

/* loaded from: classes.dex */
public class b {

    /* renamed from: a, reason: collision with root package name */
    private ExecutorService f389a;
    private e b;
    private e c;

    public b(c.a aVar, ExecutorService executorService) {
        this.f389a = executorService;
        this.b = a(aVar);
        this.c = b(aVar);
    }

    private e a(c.a aVar) {
        return new com.unisound.vui.data.c.c(aVar.getReadableDatabase()).newSession().b();
    }

    private e b(c.a aVar) {
        return new com.unisound.vui.data.c.c(aVar.getWritableDatabase()).newSession().b();
    }

    public List<com.unisound.vui.data.entity.a.c> a() {
        return this.b.loadAll();
    }
}
