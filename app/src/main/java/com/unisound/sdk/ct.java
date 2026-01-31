package com.unisound.sdk;

/* loaded from: classes.dex */
public class ct {

    /* renamed from: a, reason: collision with root package name */
    public static final int f338a = 1;
    public static final int b = 2;
    public static final int c = 3;
    public static final double d = 1.0d;
    public static final int e = 0;
    public static final String f = "vprv3.hivoice.cn:80";
    private int g = 1;
    private boolean h = false;
    private String i = "";
    private String j = "0";

    public static double c() {
        return 1.0d;
    }

    public String a() {
        return this.j;
    }

    public void a(int i) {
        this.g = i;
    }

    public void a(String str) {
        this.i = str;
    }

    public void a(boolean z) {
        if (z) {
            this.j = "1";
        } else {
            this.j = "0";
        }
    }

    public String b() {
        return this.i;
    }

    public void b(boolean z) {
        this.h = z;
    }

    public boolean d() {
        return this.h;
    }

    public int e() {
        return this.g;
    }

    public String f() {
        return f;
    }
}
