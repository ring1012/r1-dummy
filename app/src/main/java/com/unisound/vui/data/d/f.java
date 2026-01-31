package com.unisound.vui.data.d;

import android.content.Context;
import java.util.List;

/* loaded from: classes.dex */
public class f extends c {
    private com.unisound.vui.data.b.c d;

    public f(Context context) {
        super(context);
        this.d = new com.unisound.vui.data.b.c(this.f394a, this.b);
    }

    public List<com.unisound.vui.data.entity.a.d> a(String str) {
        return this.d.a(str);
    }

    public void a() {
        this.d.a();
    }

    public void a(com.unisound.vui.data.entity.a.d dVar) {
        this.d.a(dVar);
    }

    public void b(com.unisound.vui.data.entity.a.d dVar) {
        this.d.b(dVar);
    }
}
