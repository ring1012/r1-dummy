package com.baidu.a.a.a.b;

import com.baidu.a.a.a.b.c;
import java.util.Comparator;

/* loaded from: classes.dex */
class d implements Comparator<c.a> {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ c f38a;

    d(c cVar) {
        this.f38a = cVar;
    }

    @Override // java.util.Comparator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compare(c.a aVar, c.a aVar2) {
        int i = aVar2.b - aVar.b;
        if (i != 0) {
            return i;
        }
        if (aVar.d && aVar2.d) {
            return 0;
        }
        if (aVar.d) {
            return -1;
        }
        if (aVar2.d) {
            return 1;
        }
        return i;
    }
}
