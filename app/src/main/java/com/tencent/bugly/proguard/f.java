package com.tencent.bugly.proguard;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class f extends k {
    private static byte[] k;
    private static Map<String, String> l;
    private static /* synthetic */ boolean m;
    public byte[] e;
    private Map<String, String> i;
    private Map<String, String> j;

    /* renamed from: a, reason: collision with root package name */
    public short f188a = 0;
    private byte f = 0;
    private int g = 0;
    public int b = 0;
    public String c = null;
    public String d = null;
    private int h = 0;

    static {
        m = !f.class.desiredAssertionStatus();
        k = null;
        l = null;
    }

    public final boolean equals(Object o) {
        f fVar = (f) o;
        return l.a(1, (int) fVar.f188a) && l.a(1, (int) fVar.f) && l.a(1, fVar.g) && l.a(1, fVar.b) && l.a((Object) 1, (Object) fVar.c) && l.a((Object) 1, (Object) fVar.d) && l.a((Object) 1, (Object) fVar.e) && l.a(1, fVar.h) && l.a((Object) 1, (Object) fVar.i) && l.a((Object) 1, (Object) fVar.j);
    }

    public final Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            if (m) {
                return null;
            }
            throw new AssertionError();
        }
    }

    @Override // com.tencent.bugly.proguard.k
    public final void a(j jVar) throws UnsupportedEncodingException {
        jVar.a(this.f188a, 1);
        jVar.a(this.f, 2);
        jVar.a(this.g, 3);
        jVar.a(this.b, 4);
        jVar.a(this.c, 5);
        jVar.a(this.d, 6);
        jVar.a(this.e, 7);
        jVar.a(this.h, 8);
        jVar.a((Map) this.i, 9);
        jVar.a((Map) this.j, 10);
    }

    @Override // com.tencent.bugly.proguard.k
    public final void a(i iVar) {
        try {
            this.f188a = iVar.a(this.f188a, 1, true);
            this.f = iVar.a(this.f, 2, true);
            this.g = iVar.a(this.g, 3, true);
            this.b = iVar.a(this.b, 4, true);
            this.c = iVar.b(5, true);
            this.d = iVar.b(6, true);
            if (k == null) {
                k = new byte[]{0};
            }
            byte[] bArr = k;
            this.e = iVar.c(7, true);
            this.h = iVar.a(this.h, 8, true);
            if (l == null) {
                HashMap map = new HashMap();
                l = map;
                map.put("", "");
            }
            this.i = (Map) iVar.a((i) l, 9, true);
            if (l == null) {
                HashMap map2 = new HashMap();
                l = map2;
                map2.put("", "");
            }
            this.j = (Map) iVar.a((i) l, 10, true);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("RequestPacket decode error " + e.a(this.e));
            throw new RuntimeException(e);
        }
    }

    @Override // com.tencent.bugly.proguard.k
    public final void a(StringBuilder sb, int i) {
        h hVar = new h(sb, i);
        hVar.a(this.f188a, "iVersion");
        hVar.a(this.f, "cPacketType");
        hVar.a(this.g, "iMessageType");
        hVar.a(this.b, "iRequestId");
        hVar.a(this.c, "sServantName");
        hVar.a(this.d, "sFuncName");
        hVar.a(this.e, "sBuffer");
        hVar.a(this.h, "iTimeout");
        hVar.a((Map) this.i, "context");
        hVar.a((Map) this.j, "status");
    }
}
