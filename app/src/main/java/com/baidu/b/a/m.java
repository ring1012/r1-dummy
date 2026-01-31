package com.baidu.b.a;

import com.baidu.b.a.f;

/* loaded from: classes.dex */
class m implements f.a<String> {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ String f48a;
    final /* synthetic */ b b;

    m(b bVar, String str) {
        this.b = bVar;
        this.f48a = str;
    }

    @Override // com.baidu.b.a.f.a
    public void a(String str) {
        this.b.a(str, this.f48a);
    }
}
