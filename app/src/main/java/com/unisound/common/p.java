package com.unisound.common;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/* loaded from: classes.dex */
final class p {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ l f268a;
    private final String b;
    private final long[] c;
    private boolean d;
    private n e;
    private long f;

    private p(l lVar, String str) {
        this.f268a = lVar;
        this.b = str;
        this.c = new long[lVar.q];
    }

    /* synthetic */ p(l lVar, String str, m mVar) {
        this(lVar, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String[] strArr) throws IOException {
        if (strArr.length != this.f268a.q) {
            throw b(strArr);
        }
        for (int i = 0; i < strArr.length; i++) {
            try {
                this.c[i] = Long.parseLong(strArr[i]);
            } catch (NumberFormatException e) {
                throw b(strArr);
            }
        }
    }

    private IOException b(String[] strArr) throws IOException {
        throw new IOException("unexpected journal line: " + Arrays.toString(strArr));
    }

    public File a(int i) {
        return new File(this.f268a.l, this.b + "." + i);
    }

    public String a() {
        StringBuilder sb = new StringBuilder();
        for (long j : this.c) {
            sb.append(' ').append(j);
        }
        return sb.toString();
    }

    public File b(int i) {
        return new File(this.f268a.l, this.b + "." + i + ".tmp");
    }
}
