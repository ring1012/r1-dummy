package com.unisound.vui.data.d;

import android.content.Context;
import com.unisound.vui.data.c.c;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* loaded from: classes.dex */
public abstract class c {

    /* renamed from: a, reason: collision with root package name */
    public c.a f394a;
    public com.unisound.vui.data.e.b c = new com.unisound.vui.data.e.b();
    public ExecutorService b = Executors.newCachedThreadPool();

    public c(Context context) {
        this.f394a = new c.a(context, "uniInfo-db", null);
    }
}
