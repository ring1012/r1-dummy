package com.unisound.vui.util.b.a;

import cn.yunzhisheng.common.PinyinConverter;
import java.io.Serializable;

/* loaded from: classes.dex */
public class c<C> implements Serializable {

    /* renamed from: a, reason: collision with root package name */
    private final String f434a;
    private final C b;
    private final double c;
    private final int d;

    public String a() {
        return this.f434a;
    }

    public C b() {
        return this.b;
    }

    public double c() {
        return this.c;
    }

    public boolean equals(Object that) {
        if (!(that instanceof c)) {
            return false;
        }
        c cVar = (c) that;
        return this.f434a.equals(cVar.f434a) && this.b.equals(cVar.b) && this.c == cVar.c && this.d == cVar.d;
    }

    public int hashCode() {
        return this.f434a.hashCode() + (this.b.hashCode() * 31);
    }

    public String toString() {
        return a() + ":" + b() + PinyinConverter.PINYIN_SEPARATOR + c();
    }
}
