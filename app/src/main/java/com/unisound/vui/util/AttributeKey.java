package com.unisound.vui.util;

/* loaded from: classes.dex */
public final class AttributeKey<T> extends a<AttributeKey<T>> {
    private static final c<AttributeKey<Object>> POOL = new c<AttributeKey<Object>>() { // from class: com.unisound.vui.util.AttributeKey.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.unisound.vui.util.c
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public AttributeKey<Object> b(int i, String str) {
            return new AttributeKey<>(i, str);
        }
    };

    private AttributeKey(int id, String name) {
        super(id, name);
    }

    public static boolean exists(String name) {
        return POOL.b(name);
    }

    public static <T> AttributeKey<T> newInstance(String name) {
        return (AttributeKey) POOL.c(name);
    }

    public static <T> AttributeKey<T> valueOf(Class<?> firstNameComponent, String secondNameComponent) {
        return (AttributeKey) POOL.a(firstNameComponent, secondNameComponent);
    }

    public static <T> AttributeKey<T> valueOf(String name) {
        return (AttributeKey) POOL.a(name);
    }
}
