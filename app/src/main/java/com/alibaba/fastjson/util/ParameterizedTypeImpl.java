package com.alibaba.fastjson.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/* loaded from: classes.dex */
public class ParameterizedTypeImpl implements ParameterizedType {
    private final Type[] actualTypeArguments;
    private final Type ownerType;
    private final Type rawType;

    public ParameterizedTypeImpl(Type[] actualTypeArguments, Type ownerType, Type rawType) {
        this.actualTypeArguments = actualTypeArguments;
        this.ownerType = ownerType;
        this.rawType = rawType;
    }

    @Override // java.lang.reflect.ParameterizedType
    public Type[] getActualTypeArguments() {
        return this.actualTypeArguments;
    }

    @Override // java.lang.reflect.ParameterizedType
    public Type getOwnerType() {
        return this.ownerType;
    }

    @Override // java.lang.reflect.ParameterizedType
    public Type getRawType() {
        return this.rawType;
    }
}
