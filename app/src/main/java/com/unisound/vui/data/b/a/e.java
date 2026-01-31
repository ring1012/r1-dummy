package com.unisound.vui.data.b.a;

import com.unisound.vui.data.c.c;
import java.util.concurrent.ExecutorService;

/* loaded from: classes.dex */
public class e {

    /* renamed from: a, reason: collision with root package name */
    private ExecutorService f385a;
    private com.unisound.vui.data.c.a.d b;
    private com.unisound.vui.data.c.a.d c;

    public e(c.a aVar, ExecutorService executorService) {
        this.f385a = executorService;
        this.b = a(aVar);
        this.c = b(aVar);
    }

    private com.unisound.vui.data.c.a.d a(c.a aVar) {
        return new com.unisound.vui.data.c.c(aVar.getReadableDatabase()).newSession().i();
    }

    private com.unisound.vui.data.c.a.d b(c.a aVar) {
        return new com.unisound.vui.data.c.c(aVar.getWritableDatabase()).newSession().i();
    }
}
