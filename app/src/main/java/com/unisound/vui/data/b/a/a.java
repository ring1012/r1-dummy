package com.unisound.vui.data.b.a;

import com.unisound.vui.data.c.c;
import java.util.concurrent.ExecutorService;

/* loaded from: classes.dex */
public class a {

    /* renamed from: a, reason: collision with root package name */
    private ExecutorService f381a;
    private com.unisound.vui.data.c.a.a b;
    private com.unisound.vui.data.c.a.a c;

    public a(c.a aVar, ExecutorService executorService) {
        this.f381a = executorService;
        this.b = a(aVar);
        this.c = b(aVar);
    }

    private com.unisound.vui.data.c.a.a a(c.a aVar) {
        return new com.unisound.vui.data.c.c(aVar.getReadableDatabase()).newSession().g();
    }

    private com.unisound.vui.data.c.a.a b(c.a aVar) {
        return new com.unisound.vui.data.c.c(aVar.getWritableDatabase()).newSession().g();
    }
}
