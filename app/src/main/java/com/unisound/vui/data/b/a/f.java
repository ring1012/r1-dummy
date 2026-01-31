package com.unisound.vui.data.b.a;

import com.unisound.vui.data.c.c;
import java.util.concurrent.ExecutorService;

/* loaded from: classes.dex */
public class f {

    /* renamed from: a, reason: collision with root package name */
    private ExecutorService f386a;
    private com.unisound.vui.data.c.a.e b;
    private com.unisound.vui.data.c.a.e c;

    public f(c.a aVar, ExecutorService executorService) {
        this.f386a = executorService;
        this.b = a(aVar);
        this.c = b(aVar);
    }

    private com.unisound.vui.data.c.a.e a(c.a aVar) {
        return new com.unisound.vui.data.c.c(aVar.getReadableDatabase()).newSession().d();
    }

    private com.unisound.vui.data.c.a.e b(c.a aVar) {
        return new com.unisound.vui.data.c.c(aVar.getWritableDatabase()).newSession().d();
    }
}
