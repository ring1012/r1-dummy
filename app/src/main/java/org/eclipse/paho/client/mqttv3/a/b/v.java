package org.eclipse.paho.client.mqttv3.a.b;

import java.io.InputStream;

/* loaded from: classes.dex */
public class v extends InputStream {

    /* renamed from: a, reason: collision with root package name */
    private byte[] f486a;
    private int b;
    private int c;
    private byte[] d;
    private int e;
    private int f;
    private int g = 0;

    public v(byte[] bArr, int i, int i2, byte[] bArr2, int i3, int i4) {
        this.f486a = bArr;
        this.d = bArr2;
        this.b = i;
        this.e = i3;
        this.c = i2;
        this.f = i4;
    }

    @Override // java.io.InputStream
    public int read() {
        int i;
        if (this.g < this.c) {
            i = this.f486a[this.b + this.g];
        } else {
            if (this.g >= this.c + this.f) {
                return -1;
            }
            i = this.d[(this.e + this.g) - this.c];
        }
        if (i < 0) {
            i += 256;
        }
        this.g++;
        return i;
    }
}
