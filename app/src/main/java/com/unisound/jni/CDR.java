package com.unisound.jni;

/* loaded from: classes.dex */
public class CDR {

    /* renamed from: a, reason: collision with root package name */
    int f277a = init();

    static {
        System.loadLibrary("cdr");
    }

    public native int init();

    public native byte[] process(byte[] bArr);

    public void release() {
        release(this.f277a);
    }

    public native void release(int i);

    public byte[] write(byte[] bArr) {
        return process(bArr);
    }
}
