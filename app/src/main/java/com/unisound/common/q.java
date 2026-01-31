package com.unisound.common;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public final class q implements Closeable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ l f269a;
    private final String b;
    private final long c;
    private final InputStream[] d;

    private q(l lVar, String str, long j, InputStream[] inputStreamArr) {
        this.f269a = lVar;
        this.b = str;
        this.c = j;
        this.d = inputStreamArr;
    }

    /* synthetic */ q(l lVar, String str, long j, InputStream[] inputStreamArr, m mVar) {
        this(lVar, str, j, inputStreamArr);
    }

    public n a() {
        return this.f269a.a(this.b, this.c);
    }

    public InputStream a(int i) {
        return this.d[i];
    }

    public String b(int i) {
        return l.c(a(i));
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        for (InputStream inputStream : this.d) {
            l.a((Closeable) inputStream);
        }
    }
}
