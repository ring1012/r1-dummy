package com.unisound.sdk;

import com.unisound.client.ErrorCode;

/* loaded from: classes.dex */
class ba implements bz {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ az f308a;

    ba(az azVar) {
        this.f308a = azVar;
    }

    @Override // com.unisound.sdk.bz
    public void a() {
        az.sendEmptyMsg(this.f308a.p, 102);
        this.f308a.i = 2501;
    }

    @Override // com.unisound.sdk.bz
    public void a(int i) {
        az.sendMsg(this.f308a.p, com.unisound.common.ad.d, ErrorCode.toJsonMessage(i));
    }

    @Override // com.unisound.sdk.bz
    public void a(String str) {
        this.f308a.b(str);
    }

    @Override // com.unisound.sdk.bz
    public void a(byte[] bArr, int i) {
        byte[] bArr2 = new byte[i];
        System.arraycopy(bArr, 0, bArr2, 0, i);
        if (this.f308a.e != null) {
            this.f308a.e.a(bArr2);
        }
    }

    @Override // com.unisound.sdk.bz
    public void b() {
        if (this.f308a.e != null) {
            this.f308a.e.h();
            az.sendEmptyMsg(this.f308a.p, 103);
        }
    }

    @Override // com.unisound.sdk.bz
    public void c() {
        az.sendEmptyMsg(this.f308a.p, com.unisound.common.ad.s);
    }
}
