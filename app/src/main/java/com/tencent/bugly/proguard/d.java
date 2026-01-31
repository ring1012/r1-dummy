package com.tencent.bugly.proguard;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class d extends c {
    private static HashMap<String, byte[]> f = null;
    private static HashMap<String, HashMap<String, byte[]>> g = null;
    private f e = new f();

    public d() {
        this.e.f188a = (short) 2;
    }

    @Override // com.tencent.bugly.proguard.c, com.tencent.bugly.proguard.a
    public final <T> void a(String str, T t) throws UnsupportedEncodingException {
        if (str.startsWith(".")) {
            throw new IllegalArgumentException("put name can not startwith . , now is " + str);
        }
        super.a(str, (String) t);
    }

    @Override // com.tencent.bugly.proguard.c
    public final void b() {
        super.b();
        this.e.f188a = (short) 3;
    }

    @Override // com.tencent.bugly.proguard.c, com.tencent.bugly.proguard.a
    public final byte[] a() throws UnsupportedEncodingException {
        if (this.e.f188a == 2) {
            if (this.e.c.equals("")) {
                throw new IllegalArgumentException("servantName can not is null");
            }
            if (this.e.d.equals("")) {
                throw new IllegalArgumentException("funcName can not is null");
            }
        } else {
            if (this.e.c == null) {
                this.e.c = "";
            }
            if (this.e.d == null) {
                this.e.d = "";
            }
        }
        j jVar = new j(0);
        jVar.a(this.b);
        if (this.e.f188a == 2) {
            jVar.a((Map) this.f173a, 0);
        } else {
            jVar.a((Map) this.d, 0);
        }
        this.e.e = l.a(jVar.a());
        j jVar2 = new j(0);
        jVar2.a(this.b);
        this.e.a(jVar2);
        byte[] bArrA = l.a(jVar2.a());
        int length = bArrA.length;
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(length + 4);
        byteBufferAllocate.putInt(length + 4).put(bArrA).flip();
        return byteBufferAllocate.array();
    }

    @Override // com.tencent.bugly.proguard.c, com.tencent.bugly.proguard.a
    public final void a(byte[] bArr) {
        if (bArr.length < 4) {
            throw new IllegalArgumentException("decode package must include size head");
        }
        try {
            i iVar = new i(bArr, 4);
            iVar.a(this.b);
            this.e.a(iVar);
            if (this.e.f188a == 3) {
                i iVar2 = new i(this.e.e);
                iVar2.a(this.b);
                if (f == null) {
                    HashMap<String, byte[]> map = new HashMap<>();
                    f = map;
                    map.put("", new byte[0]);
                }
                this.d = iVar2.a((Map) f, 0, false);
                return;
            }
            i iVar3 = new i(this.e.e);
            iVar3.a(this.b);
            if (g == null) {
                g = new HashMap<>();
                HashMap<String, byte[]> map2 = new HashMap<>();
                map2.put("", new byte[0]);
                g.put("", map2);
            }
            this.f173a = iVar3.a((Map) g, 0, false);
            new HashMap();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public final void b(String str) {
        this.e.c = str;
    }

    public final void c(String str) {
        this.e.d = str;
    }

    public final void b(int i) {
        this.e.b = 1;
    }
}
