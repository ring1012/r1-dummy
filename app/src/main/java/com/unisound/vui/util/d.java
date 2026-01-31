package com.unisound.vui.util;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/* loaded from: classes.dex */
public class d implements AttributeMap {
    private static final int BUCKET_SIZE = 4;
    private static final int MASK = 3;
    private static final AtomicReferenceFieldUpdater<d, AtomicReferenceArray> UPDATER = AtomicReferenceFieldUpdater.newUpdater(d.class, AtomicReferenceArray.class, "attributes");
    private volatile AtomicReferenceArray<a<?>> attributes;

    private static final class a<T> extends AtomicReference<T> implements Attribute<T> {

        /* renamed from: a, reason: collision with root package name */
        private final a<?> f443a;
        private final AttributeKey<T> b;
        private a<?> c;
        private a<?> d;
        private volatile boolean e;

        /* JADX WARN: Multi-variable type inference failed */
        a(AttributeKey<T> attributeKey) {
            this.f443a = this;
            this.b = attributeKey;
        }

        a(a<?> aVar, AttributeKey<T> attributeKey) {
            this.f443a = aVar;
            this.b = attributeKey;
        }

        private void a() {
            synchronized (this.f443a) {
                if (this.c != null) {
                    this.c.d = this.d;
                    if (this.d != null) {
                        this.d.c = this.c;
                    }
                }
            }
        }

        @Override // com.unisound.vui.util.Attribute
        public T getAndRemove() {
            this.e = true;
            T andSet = getAndSet(null);
            a();
            return andSet;
        }

        @Override // com.unisound.vui.util.Attribute
        public AttributeKey<T> key() {
            return this.b;
        }

        @Override // com.unisound.vui.util.Attribute
        public void remove() {
            this.e = true;
            set(null);
            a();
        }

        @Override // com.unisound.vui.util.Attribute
        public T setIfAbsent(T value) {
            while (!compareAndSet(null, value)) {
                T t = get();
                if (t != null) {
                    return t;
                }
            }
            return null;
        }
    }

    private static int index(AttributeKey<?> key) {
        return key.id() & 3;
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x003f  */
    @Override // com.unisound.vui.util.AttributeMap
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public <T> com.unisound.vui.util.Attribute<T> attr(com.unisound.vui.util.AttributeKey<T> r5) {
        /*
            r4 = this;
            r3 = 0
            if (r5 != 0) goto Lb
            java.lang.NullPointerException r0 = new java.lang.NullPointerException
            java.lang.String r1 = "key"
            r0.<init>(r1)
            throw r0
        Lb:
            java.util.concurrent.atomic.AtomicReferenceArray<com.unisound.vui.util.d$a<?>> r0 = r4.attributes
            if (r0 != 0) goto L6a
            java.util.concurrent.atomic.AtomicReferenceArray r0 = new java.util.concurrent.atomic.AtomicReferenceArray
            r1 = 4
            r0.<init>(r1)
            java.util.concurrent.atomic.AtomicReferenceFieldUpdater<com.unisound.vui.util.d, java.util.concurrent.atomic.AtomicReferenceArray> r1 = com.unisound.vui.util.d.UPDATER
            boolean r1 = r1.compareAndSet(r4, r3, r0)
            if (r1 != 0) goto L68
            java.util.concurrent.atomic.AtomicReferenceArray<com.unisound.vui.util.d$a<?>> r0 = r4.attributes
            r1 = r0
        L20:
            int r2 = index(r5)
            java.lang.Object r0 = r1.get(r2)
            com.unisound.vui.util.d$a r0 = (com.unisound.vui.util.d.a) r0
            if (r0 != 0) goto L3e
            com.unisound.vui.util.d$a r0 = new com.unisound.vui.util.d$a
            r0.<init>(r5)
            boolean r3 = r1.compareAndSet(r2, r3, r0)
            if (r3 == 0) goto L38
        L37:
            return r0
        L38:
            java.lang.Object r0 = r1.get(r2)
            com.unisound.vui.util.d$a r0 = (com.unisound.vui.util.d.a) r0
        L3e:
            monitor-enter(r0)
            r1 = r0
        L40:
            boolean r2 = com.unisound.vui.util.d.a.a(r1)     // Catch: java.lang.Throwable -> L65
            if (r2 != 0) goto L4f
            com.unisound.vui.util.AttributeKey r2 = com.unisound.vui.util.d.a.b(r1)     // Catch: java.lang.Throwable -> L65
            if (r2 != r5) goto L4f
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L65
            r0 = r1
            goto L37
        L4f:
            com.unisound.vui.util.d$a r2 = com.unisound.vui.util.d.a.c(r1)     // Catch: java.lang.Throwable -> L65
            if (r2 != 0) goto L63
            com.unisound.vui.util.d$a r2 = new com.unisound.vui.util.d$a     // Catch: java.lang.Throwable -> L65
            r2.<init>(r0, r5)     // Catch: java.lang.Throwable -> L65
            com.unisound.vui.util.d.a.a(r1, r2)     // Catch: java.lang.Throwable -> L65
            com.unisound.vui.util.d.a.b(r2, r1)     // Catch: java.lang.Throwable -> L65
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L65
            r0 = r2
            goto L37
        L63:
            r1 = r2
            goto L40
        L65:
            r1 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L65
            throw r1
        L68:
            r1 = r0
            goto L20
        L6a:
            r1 = r0
            goto L20
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unisound.vui.util.d.attr(com.unisound.vui.util.AttributeKey):com.unisound.vui.util.Attribute");
    }

    @Override // com.unisound.vui.util.AttributeMap
    public <T> boolean hasAttr(AttributeKey<T> key) {
        a<?> aVar;
        if (key == null) {
            throw new NullPointerException("key");
        }
        AtomicReferenceArray<a<?>> atomicReferenceArray = this.attributes;
        if (atomicReferenceArray != null && (aVar = atomicReferenceArray.get(index(key))) != null) {
            if (((a) aVar).b == key && !((a) aVar).e) {
                return true;
            }
            synchronized (aVar) {
                for (a aVar2 = ((a) aVar).d; aVar2 != null; aVar2 = aVar2.d) {
                    if (!aVar2.e && aVar2.b == key) {
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }
}
