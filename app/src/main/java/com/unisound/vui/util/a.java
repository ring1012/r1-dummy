package com.unisound.vui.util;

import com.unisound.vui.util.a;

/* loaded from: classes.dex */
public abstract class a<T extends a<T>> implements b<T> {
    private final int id;
    private final String name;
    private volatile long uniquifier;

    protected a(int i, String str) {
        this.id = i;
        this.name = str;
    }

    private long uniquifier() {
        long j = this.uniquifier;
        if (j == 0) {
            synchronized (this) {
                j = this.uniquifier;
                while (j == 0) {
                    this.uniquifier = com.unisound.vui.util.internal.d.b().nextLong();
                }
            }
        }
        return j;
    }

    @Override // java.lang.Comparable
    public final int compareTo(T o) {
        if (this == o) {
            return 0;
        }
        int iHashCode = hashCode() - o.hashCode();
        if (iHashCode != 0) {
            return iHashCode;
        }
        long jUniquifier = uniquifier();
        long jUniquifier2 = o.uniquifier();
        if (jUniquifier < jUniquifier2) {
            return -1;
        }
        if (jUniquifier > jUniquifier2) {
            return 1;
        }
        throw new Error("failed to compare two different constants");
    }

    public final boolean equals(Object obj) {
        return super.equals(obj);
    }

    public final int hashCode() {
        return super.hashCode();
    }

    public final int id() {
        return this.id;
    }

    public final String name() {
        return this.name;
    }

    public final String toString() {
        return name();
    }
}
