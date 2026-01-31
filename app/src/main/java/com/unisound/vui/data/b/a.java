package com.unisound.vui.data.b;

import com.unisound.vui.data.c.c;
import java.util.List;
import java.util.concurrent.ExecutorService;

/* loaded from: classes.dex */
public class a {

    /* renamed from: a, reason: collision with root package name */
    private ExecutorService f380a;
    private com.unisound.vui.data.c.b b;
    private com.unisound.vui.data.c.b c;

    public a(c.a aVar, ExecutorService executorService) {
        this.f380a = executorService;
        this.b = a(aVar);
        this.c = b(aVar);
    }

    private com.unisound.vui.data.c.b a(c.a aVar) {
        return new com.unisound.vui.data.c.c(aVar.getReadableDatabase()).newSession().a();
    }

    private com.unisound.vui.data.c.b b(c.a aVar) {
        return new com.unisound.vui.data.c.c(aVar.getWritableDatabase()).newSession().a();
    }

    public List<com.unisound.vui.data.entity.a.b> a() {
        return this.b.loadAll();
    }
}
