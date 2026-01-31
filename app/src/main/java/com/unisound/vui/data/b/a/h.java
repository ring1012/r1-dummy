package com.unisound.vui.data.b.a;

import com.unisound.vui.data.c.c;
import java.util.concurrent.ExecutorService;

/* loaded from: classes.dex */
public class h {

    /* renamed from: a, reason: collision with root package name */
    private ExecutorService f388a;
    private com.unisound.vui.data.c.a.g b;
    private com.unisound.vui.data.c.a.g c;

    public h(c.a aVar, ExecutorService executorService) {
        this.f388a = executorService;
        this.b = a(aVar);
        this.c = b(aVar);
    }

    private com.unisound.vui.data.c.a.g a(c.a aVar) {
        return new com.unisound.vui.data.c.c(aVar.getReadableDatabase()).newSession().h();
    }

    private com.unisound.vui.data.c.a.g b(c.a aVar) {
        return new com.unisound.vui.data.c.c(aVar.getWritableDatabase()).newSession().h();
    }
}
