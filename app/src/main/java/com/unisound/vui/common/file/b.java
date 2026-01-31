package com.unisound.vui.common.file;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/* loaded from: classes.dex */
public class b {
    public int b;
    public int e;
    public short f;
    public short g;
    public int h;
    public int i;
    public short j;
    public short k;
    public int m;

    /* renamed from: a, reason: collision with root package name */
    public final char[] f367a = {'R', 'I', 'F', 'F'};
    public char[] c = {'W', 'A', 'V', 'E'};
    public char[] d = {'f', 'm', 't', ' '};
    public char[] l = {'d', 'a', 't', 'a'};

    private void a(ByteArrayOutputStream byteArrayOutputStream, int i) {
        byteArrayOutputStream.write(new byte[]{(byte) ((i << 24) >> 24), (byte) ((i << 16) >> 24)});
    }

    private void a(ByteArrayOutputStream byteArrayOutputStream, char[] cArr) {
        for (char c : cArr) {
            byteArrayOutputStream.write(c);
        }
    }

    private void b(ByteArrayOutputStream byteArrayOutputStream, int i) {
        byteArrayOutputStream.write(new byte[]{(byte) ((i << 24) >> 24), (byte) ((i << 16) >> 24), (byte) ((i << 8) >> 24), (byte) (i >> 24)});
    }

    public byte[] a() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        a(byteArrayOutputStream, this.f367a);
        b(byteArrayOutputStream, this.b);
        a(byteArrayOutputStream, this.c);
        a(byteArrayOutputStream, this.d);
        b(byteArrayOutputStream, this.e);
        a(byteArrayOutputStream, this.f);
        a(byteArrayOutputStream, this.g);
        b(byteArrayOutputStream, this.h);
        b(byteArrayOutputStream, this.i);
        a(byteArrayOutputStream, this.j);
        a(byteArrayOutputStream, this.k);
        a(byteArrayOutputStream, this.l);
        b(byteArrayOutputStream, this.m);
        byteArrayOutputStream.flush();
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return byteArray;
    }
}
