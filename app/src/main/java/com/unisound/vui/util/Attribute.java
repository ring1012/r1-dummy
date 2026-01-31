package com.unisound.vui.util;

/* loaded from: classes.dex */
public interface Attribute<T> {
    boolean compareAndSet(T t, T t2);

    T get();

    T getAndRemove();

    T getAndSet(T t);

    AttributeKey<T> key();

    void remove();

    void set(T t);

    T setIfAbsent(T t);
}
