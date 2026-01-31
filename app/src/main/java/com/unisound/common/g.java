package com.unisound.common;

import android.content.Context;
import java.io.File;

/* loaded from: classes.dex */
public class g {

    /* renamed from: a, reason: collision with root package name */
    private static String f260a = "asr_start_beep.mp3";
    private static String b = "wake_up_success.wav";
    private as c;
    private String d;
    private String e;

    private boolean a(String str) throws IllegalStateException {
        if (str == null) {
            return false;
        }
        this.c.a(str);
        this.c.d();
        return true;
    }

    public void a(Context context) {
        this.c = new as(context);
        this.d = (context.getFilesDir().toString() + File.separator) + f260a;
        j.a(context, "usc" + File.separatorChar + f260a, this.d, b);
    }

    public boolean a() {
        return a(this.d);
    }

    public boolean b() {
        return a(this.e);
    }
}
