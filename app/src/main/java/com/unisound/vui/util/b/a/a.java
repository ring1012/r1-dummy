package com.unisound.vui.util.b.a;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public abstract class a<C> extends AbstractSet<c<C>> implements b<C> {
    protected a() {
    }

    private List<c<C>> a(Iterator<c<C>> it) {
        ArrayList arrayList = new ArrayList();
        while (it.hasNext()) {
            arrayList.add(it.next());
        }
        return arrayList;
    }

    private c<C>[] b(Iterator<c<C>> it) {
        List<c<C>> listA = a(it);
        c<C>[] cVarArr = new c[listA.size()];
        listA.toArray(cVarArr);
        return cVarArr;
    }

    c<C>[] a() {
        return b(iterator());
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return a().length;
    }
}
