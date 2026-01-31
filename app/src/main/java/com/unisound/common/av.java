package com.unisound.common;

/* loaded from: classes.dex */
public class av {
    private static final int c = 1;
    private static final int d = 2;

    /* renamed from: a, reason: collision with root package name */
    int f248a;
    int b;

    public av() {
        this.f248a = 0;
        this.b = 0;
    }

    public av(int i, int i2) {
        this.f248a = 0;
        this.b = 0;
        this.f248a = i2;
        this.b = i;
    }

    public void a(int i) {
        this.b = i;
    }

    public boolean a() {
        return (this.f248a & 1) != 0;
    }

    public aw b() {
        return (this.b & 1) != 0 ? aw.MAN : (this.b & 2) != 0 ? aw.FEMALE : aw.UNKOWN;
    }
}
