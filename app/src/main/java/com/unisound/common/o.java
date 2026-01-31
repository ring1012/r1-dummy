package com.unisound.common;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: classes.dex */
class o extends FilterOutputStream {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ n f267a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    private o(n nVar, OutputStream outputStream) {
        super(outputStream);
        this.f267a = nVar;
    }

    /* synthetic */ o(n nVar, OutputStream outputStream, m mVar) {
        this(nVar, outputStream);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        try {
            this.out.close();
        } catch (IOException e) {
            this.f267a.c = true;
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        try {
            this.out.flush();
        } catch (IOException e) {
            this.f267a.c = true;
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(int i) throws IOException {
        try {
            this.out.write(i);
        } catch (IOException e) {
            this.f267a.c = true;
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr, int i, int i2) throws IOException {
        try {
            this.out.write(bArr, i, i2);
        } catch (IOException e) {
            this.f267a.c = true;
        }
    }
}
