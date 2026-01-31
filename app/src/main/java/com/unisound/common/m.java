package com.unisound.common;

import java.util.concurrent.Callable;

/* loaded from: classes.dex */
class m implements Callable<Void> {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ l f265a;

    m(l lVar) {
        this.f265a = lVar;
    }

    @Override // java.util.concurrent.Callable
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public Void call() {
        synchronized (this.f265a) {
            if (this.f265a.s != null) {
                this.f265a.n();
                if (this.f265a.l()) {
                    this.f265a.k();
                    this.f265a.u = 0;
                }
            }
        }
        return null;
    }
}
