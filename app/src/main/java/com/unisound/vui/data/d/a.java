package com.unisound.vui.data.d;

import android.content.Context;
import com.unisound.vui.data.entity.out.UniContact;
import java.util.List;

/* loaded from: classes.dex */
public class a extends c {
    private Context d;
    private com.unisound.vui.data.b.a e;

    public a(Context context) {
        super(context);
        this.d = context;
        this.e = new com.unisound.vui.data.b.a(this.f394a, this.b);
    }

    public List<UniContact> a() {
        return this.c.a(this.e.a());
    }
}
