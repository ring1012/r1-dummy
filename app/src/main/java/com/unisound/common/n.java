package com.unisound.common;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/* loaded from: classes.dex */
public final class n {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ l f266a;
    private final p b;
    private boolean c;

    private n(l lVar, p pVar) {
        this.f266a = lVar;
        this.b = pVar;
    }

    /* synthetic */ n(l lVar, p pVar, m mVar) {
        this(lVar, pVar);
    }

    public InputStream a(int i) {
        synchronized (this.f266a) {
            if (this.b.e != this) {
                throw new IllegalStateException();
            }
            return !this.b.d ? null : new FileInputStream(this.b.a(i));
        }
    }

    public void a() {
        if (!this.c) {
            this.f266a.a(this, true);
        } else {
            this.f266a.a(this, false);
            this.f266a.c(this.b.b);
        }
    }

    public void a(int i, String str) throws Throwable {
        OutputStreamWriter outputStreamWriter;
        try {
            outputStreamWriter = new OutputStreamWriter(c(i), l.j);
            try {
                outputStreamWriter.write(str);
                l.a(outputStreamWriter);
            } catch (Throwable th) {
                th = th;
                l.a(outputStreamWriter);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            outputStreamWriter = null;
        }
    }

    public String b(int i) {
        InputStream inputStreamA = a(i);
        if (inputStreamA != null) {
            return l.c(inputStreamA);
        }
        return null;
    }

    public void b() {
        this.f266a.a(this, false);
    }

    public OutputStream c(int i) {
        o oVar;
        synchronized (this.f266a) {
            if (this.b.e != this) {
                throw new IllegalStateException();
            }
            oVar = new o(this, new FileOutputStream(this.b.b(i)), null);
        }
        return oVar;
    }
}
