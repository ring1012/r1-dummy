package com.unisound.b;

import com.unisound.vui.handler.session.memo.utils.MemoConstants;
import java.text.SimpleDateFormat;

/* loaded from: classes.dex */
public class l {
    private long b;
    private long d;

    /* renamed from: a, reason: collision with root package name */
    private String f225a = "";
    private String c = "";
    private String e = "";

    private void b(String str) {
        this.c = str;
    }

    private String c(long j) {
        return new SimpleDateFormat(MemoConstants.DATE_FORMATE_YMDHMS).format(Long.valueOf(j));
    }

    private void c(String str) {
        this.e = str;
    }

    public String a() {
        return this.f225a;
    }

    public void a(long j) {
        this.b = j;
        b(c(j));
        i.d("responseHeaderTime = " + j);
    }

    public void a(String str) {
        this.f225a = str;
    }

    public long b() {
        return this.b;
    }

    public void b(long j) {
        this.d = j;
        c(c(j));
        i.d("responseCurrentSystemTime = " + j);
    }

    public String c() {
        return this.c;
    }

    public long d() {
        return this.d;
    }

    public String e() {
        return this.e;
    }
}
