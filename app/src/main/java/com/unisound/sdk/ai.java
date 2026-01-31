package com.unisound.sdk;

/* loaded from: classes.dex */
public class ai {

    /* renamed from: a, reason: collision with root package name */
    public static boolean f296a = true;
    private boolean b = true;

    public void a() {
        this.b = true;
    }

    public boolean a(byte[] bArr, int i, int i2) {
        if (this.b && f296a) {
            int i3 = 0;
            while (i < i2) {
                if (bArr[i] != 0) {
                    i3++;
                }
                i++;
            }
            if (i3 > 1) {
                this.b = false;
                return true;
            }
        }
        return false;
    }

    public boolean b() {
        return (this.b && f296a) ? false : true;
    }
}
