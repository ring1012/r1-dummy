package com.unisound.vui.data.entity.out;

/* loaded from: classes.dex */
public class a {

    /* renamed from: a, reason: collision with root package name */
    private String f407a;
    private String b;
    private String c;
    private String d;

    public String a() {
        return this.b;
    }

    public void a(String str) {
        this.f407a = str;
    }

    public String b() {
        return this.c;
    }

    public void b(String str) {
        this.b = str;
    }

    public String c() {
        return this.d;
    }

    public void c(String str) {
        this.c = str;
    }

    public void d(String str) {
        this.d = str;
    }

    public String toString() {
        return "UniUserAuth{appKey='" + this.f407a + "', udid='" + this.b + "', passportId='" + this.c + "', passportToken='" + this.d + "'}";
    }
}
