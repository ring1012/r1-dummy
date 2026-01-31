package com.unisound.sdk;

/* loaded from: classes.dex */
public class aw {

    /* renamed from: a, reason: collision with root package name */
    private int f305a = 0;
    private int b = -1;
    private boolean c = false;

    public void a() {
        this.f305a = 0;
    }

    public void a(int i) {
        a();
        if (i == -1) {
            this.c = false;
        } else {
            this.c = true;
            this.b = i * 32;
        }
    }

    public boolean a(boolean z, int i) {
        if (z && this.c) {
            this.f305a += i;
            if (this.f305a > this.b) {
                return true;
            }
        }
        return false;
    }
}
