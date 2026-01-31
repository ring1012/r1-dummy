package com.unisound.vui.data.b.a;

import com.unisound.vui.data.c.c;
import java.util.concurrent.ExecutorService;

/* loaded from: classes.dex */
public class b {

    /* renamed from: a, reason: collision with root package name */
    private ExecutorService f382a;
    private com.unisound.vui.data.c.a.b b;
    private com.unisound.vui.data.c.a.b c;

    public b(c.a aVar, ExecutorService executorService) {
        this.f382a = executorService;
        this.b = a(aVar);
        this.c = b(aVar);
    }

    private com.unisound.vui.data.c.a.b a(c.a aVar) {
        return new com.unisound.vui.data.c.c(aVar.getReadableDatabase()).newSession().f();
    }

    private com.unisound.vui.data.c.a.b b(c.a aVar) {
        return new com.unisound.vui.data.c.c(aVar.getWritableDatabase()).newSession().f();
    }
}
