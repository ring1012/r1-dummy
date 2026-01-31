package com.tencent.bugly.proguard;

import java.io.UnsupportedEncodingException;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class aj extends k implements Cloneable {
    private static byte[] d;

    /* renamed from: a, reason: collision with root package name */
    private byte f178a;
    private String b;
    private byte[] c;

    public aj() {
        this.f178a = (byte) 0;
        this.b = "";
        this.c = null;
    }

    public aj(byte b, String str, byte[] bArr) {
        this.f178a = (byte) 0;
        this.b = "";
        this.c = null;
        this.f178a = b;
        this.b = str;
        this.c = bArr;
    }

    @Override // com.tencent.bugly.proguard.k
    public final void a(j jVar) throws UnsupportedEncodingException {
        jVar.a(this.f178a, 0);
        jVar.a(this.b, 1);
        if (this.c != null) {
            jVar.a(this.c, 2);
        }
    }

    @Override // com.tencent.bugly.proguard.k
    public final void a(i iVar) {
        this.f178a = iVar.a(this.f178a, 0, true);
        this.b = iVar.b(1, true);
        if (d == null) {
            d = new byte[]{0};
        }
        byte[] bArr = d;
        this.c = iVar.c(2, false);
    }

    @Override // com.tencent.bugly.proguard.k
    public final void a(StringBuilder sb, int i) {
    }
}
