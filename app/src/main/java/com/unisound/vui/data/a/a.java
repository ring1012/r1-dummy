package com.unisound.vui.data.a;

import android.content.Context;
import com.unisound.vui.data.d.b;
import com.unisound.vui.data.entity.a.c;
import com.unisound.vui.data.entity.a.d;
import com.unisound.vui.data.entity.out.UniContact;
import java.util.List;

/* loaded from: classes.dex */
public class a {

    /* renamed from: a, reason: collision with root package name */
    public static final String f379a = a.class.getSimpleName();
    private static a b;
    private b c;
    private Context d;

    private a(Context context) {
        this.d = context;
        this.c = new b(context);
    }

    public static a a(Context context) {
        if (b == null) {
            synchronized (a.class) {
                if (b == null) {
                    b = new a(context);
                }
            }
        }
        return b;
    }

    public List<UniContact> a() {
        return this.c.a().a();
    }

    public List<d> a(String str) {
        return this.c.c().a(str);
    }

    public void a(d dVar) {
        this.c.c().a(dVar);
    }

    public List<c> b() {
        return this.c.b().a();
    }

    public void b(d dVar) {
        this.c.c().b(dVar);
    }

    public void c() {
        this.c.c().a();
    }
}
