package com.unisound.vui.util.b.a;

import com.unisound.vui.util.b.a.e;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

/* loaded from: classes.dex */
public class h<C> extends com.unisound.vui.util.b.a.a<C> implements Serializable {

    /* renamed from: a, reason: collision with root package name */
    f<C> f439a = new f<>();

    private static class a<D> extends e.a<c<D>> {
        c<D>[] b;

        /* renamed from: a, reason: collision with root package name */
        LinkedList<f<D>> f440a = new LinkedList<>();
        int c = -1;

        a(f<D> fVar) {
            this.f440a.add(fVar);
        }

        void a(f<D>[] fVarArr) {
            int length = fVarArr.length;
            while (true) {
                length--;
                if (length < 0) {
                    return;
                } else {
                    this.f440a.addFirst(fVarArr[length]);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.unisound.vui.util.b.a.e.a
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public c<D> a() {
            while (this.b == null && !this.f440a.isEmpty()) {
                f<D> fVarRemoveFirst = this.f440a.removeFirst();
                a(fVarRemoveFirst.f);
                if (fVarRemoveFirst.d.length > 0) {
                    this.b = fVarRemoveFirst.d;
                    this.c = 0;
                }
            }
            if (this.b == null) {
                return null;
            }
            c<D>[] cVarArr = this.b;
            int i = this.c;
            this.c = i + 1;
            c<D> cVar = cVarArr[i];
            if (this.c < this.b.length) {
                return cVar;
            }
            this.b = null;
            return cVar;
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public Iterator<c<C>> iterator() {
        return new a(this.f439a);
    }
}
