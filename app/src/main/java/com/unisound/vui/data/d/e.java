package com.unisound.vui.data.d;

import android.content.Context;
import com.unisound.vui.data.b.a.g;
import com.unisound.vui.data.b.a.h;

/* loaded from: classes.dex */
public class e extends c {
    private com.unisound.vui.data.b.a.a d;
    private com.unisound.vui.data.b.a.b e;
    private com.unisound.vui.data.b.a.c f;
    private com.unisound.vui.data.b.a.e g;
    private com.unisound.vui.data.b.a.f h;
    private g i;
    private h j;
    private com.unisound.vui.data.b.a.d k;

    public e(Context context) {
        super(context);
        a();
    }

    private void a() {
        this.d = new com.unisound.vui.data.b.a.a(this.f394a, this.b);
        this.e = new com.unisound.vui.data.b.a.b(this.f394a, this.b);
        this.f = new com.unisound.vui.data.b.a.c(this.f394a, this.b);
        this.g = new com.unisound.vui.data.b.a.e(this.f394a, this.b);
        this.h = new com.unisound.vui.data.b.a.f(this.f394a, this.b);
        this.i = new g(this.f394a, this.b);
        this.j = new h(this.f394a, this.b);
        b();
    }

    private void b() {
        this.k = new com.unisound.vui.data.b.a.d();
        this.k.a(this.h);
        this.k.a(this.i);
        this.k.a(this.d);
        this.k.a(this.g);
        this.k.a(this.f);
        this.k.a(this.e);
        this.k.a(this.j);
    }
}
