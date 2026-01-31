package com.unisound.common;

/* loaded from: classes.dex */
public class ak {

    /* renamed from: a, reason: collision with root package name */
    public static final int f240a = -1;
    int b;
    String c;
    private boolean d = false;

    public ak(int i, String str) {
        this.b = -1;
        this.c = "";
        this.b = i;
        this.c = str;
    }

    public void a(ak akVar) {
        this.b = akVar.b;
        this.c = akVar.c;
        this.d = akVar.d;
    }

    void a(String str) {
        this.c = str;
    }

    public void a(boolean z) {
        this.d = z;
    }

    public boolean a() {
        return this.d;
    }

    public String b() {
        return this.c;
    }

    public int c() {
        return this.b;
    }
}
