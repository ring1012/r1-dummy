package com.unisound.vui.util.b.a;

import java.util.Iterator;
import java.util.NoSuchElementException;

/* loaded from: classes.dex */
public class e {

    /* renamed from: a, reason: collision with root package name */
    static final Iterator<Object> f436a = new b();

    public static abstract class a<E> implements Iterator<E> {

        /* renamed from: a, reason: collision with root package name */
        private E f437a;

        protected a() {
        }

        protected abstract E a();

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.f437a == null) {
                E eA = a();
                this.f437a = eA;
                if (eA == null) {
                    return false;
                }
            }
            return true;
        }

        @Override // java.util.Iterator
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            E e = this.f437a;
            this.f437a = null;
            return e;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    static class b<E> implements Iterator<E> {
        @Override // java.util.Iterator
        public boolean hasNext() {
            return false;
        }

        @Override // java.util.Iterator
        public E next() {
            throw new NoSuchElementException("No elements in empty iterator.");
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new IllegalStateException("No elements to remove in empty iterator.");
        }
    }
}
