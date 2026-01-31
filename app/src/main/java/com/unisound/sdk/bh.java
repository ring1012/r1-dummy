package com.unisound.sdk;

/* loaded from: classes.dex */
class bh implements ac {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bg f313a;

    bh(bg bgVar) {
        this.f313a = bgVar;
    }

    @Override // com.unisound.sdk.ac
    public void a(int i) {
        switch (i) {
            case 1000:
                if (this.f313a.an != null) {
                    this.f313a.an.sendEmptyMessage(24);
                    break;
                }
                break;
        }
    }
}
