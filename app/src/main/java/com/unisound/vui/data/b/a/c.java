package com.unisound.vui.data.b.a;

import com.unisound.vui.data.c.c;
import java.util.concurrent.ExecutorService;

/* loaded from: classes.dex */
public class c {

    /* renamed from: a, reason: collision with root package name */
    private ExecutorService f383a;
    private com.unisound.vui.data.c.a.c b;
    private com.unisound.vui.data.c.a.c c;

    public c(c.a aVar, ExecutorService executorService) {
        this.f383a = executorService;
        this.b = a(aVar);
        this.c = b(aVar);
    }

    private com.unisound.vui.data.c.a.c a(c.a aVar) {
        return new com.unisound.vui.data.c.c(aVar.getReadableDatabase()).newSession().j();
    }

    private com.unisound.vui.data.c.a.c b(c.a aVar) {
        return new com.unisound.vui.data.c.c(aVar.getWritableDatabase()).newSession().j();
    }
}
