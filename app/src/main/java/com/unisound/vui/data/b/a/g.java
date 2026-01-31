package com.unisound.vui.data.b.a;

import com.unisound.vui.data.c.c;
import java.util.concurrent.ExecutorService;

/* loaded from: classes.dex */
public class g {

    /* renamed from: a, reason: collision with root package name */
    private ExecutorService f387a;
    private com.unisound.vui.data.c.a.f b;
    private com.unisound.vui.data.c.a.f c;

    public g(c.a aVar, ExecutorService executorService) {
        this.f387a = executorService;
        this.b = a(aVar);
        this.c = b(aVar);
    }

    private com.unisound.vui.data.c.a.f a(c.a aVar) {
        return new com.unisound.vui.data.c.c(aVar.getReadableDatabase()).newSession().e();
    }

    private com.unisound.vui.data.c.a.f b(c.a aVar) {
        return new com.unisound.vui.data.c.c(aVar.getWritableDatabase()).newSession().e();
    }
}
