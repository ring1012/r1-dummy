package com.unisound.sdk;

/* loaded from: classes.dex */
public class c {

    /* renamed from: a, reason: collision with root package name */
    public static final String f326a = "x-wav";
    public static final String b = "pcm";
    public static final String c = "opus";
    public static final int d = 16;
    public static final int e = 16000;
    private String f = f326a;
    private String g = b;
    private String h = "opus";
    private int i = 16;
    private int j = 16000;
    private boolean k = true;

    public String a() {
        return this.h;
    }

    public void a(boolean z) {
        this.k = z;
    }

    public String b() {
        return "audio/" + this.f;
    }

    public boolean c() {
        return this.k;
    }
}
