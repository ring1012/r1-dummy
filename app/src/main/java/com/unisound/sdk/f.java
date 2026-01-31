package com.unisound.sdk;

import java.util.ArrayList;

/* loaded from: classes.dex */
public class f {

    /* renamed from: a, reason: collision with root package name */
    private static final int f342a = 1024;
    private short[] b = new short[1024];
    private int c;

    /* JADX WARN: Multi-variable type inference failed */
    public ArrayList<short[]> a(short[] sArr, int i) {
        ArrayList<short[]> arrayList = new ArrayList<>(4);
        int i2 = 0;
        while (i > 0) {
            if (i <= 1024 - this.c) {
                System.arraycopy(sArr, i2, this.b, this.c, i);
                this.c += i;
                i2 += i;
                i -= i;
            } else {
                int i3 = 1024 - this.c;
                System.arraycopy(sArr, i2, this.b, this.c, i3);
                arrayList.add(this.b.clone());
                this.c = 0;
                i2 += i3;
                i -= i3;
            }
        }
        return arrayList;
    }

    public short[] a() {
        if (this.c <= 0) {
            return null;
        }
        short[] sArr = new short[this.c];
        System.arraycopy(this.b, 0, sArr, 0, this.c);
        return sArr;
    }
}
