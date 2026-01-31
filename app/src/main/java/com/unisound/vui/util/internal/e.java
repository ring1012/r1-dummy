package com.unisound.vui.util.internal;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public abstract class e {

    /* renamed from: a, reason: collision with root package name */
    private static final ThreadLocal<com.unisound.vui.util.internal.a> f448a = new ThreadLocal<com.unisound.vui.util.internal.a>() { // from class: com.unisound.vui.util.internal.e.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public com.unisound.vui.util.internal.a initialValue() {
            return new com.unisound.vui.util.internal.a();
        }
    };
    private static final e b = new b();
    private static final Object c = new Object();

    private static final class a extends e {

        /* renamed from: a, reason: collision with root package name */
        private final Class<?> f449a;

        a(Class<?> cls) {
            this.f449a = cls;
        }

        @Override // com.unisound.vui.util.internal.e
        public boolean a(Object obj) {
            return this.f449a.isInstance(obj);
        }
    }

    protected e() {
    }

    public static e a(Class<?> cls) {
        Map<Class<?>, e> map = f448a.get().f446a;
        e aVar = map.get(cls);
        if (aVar == null) {
            if (cls == Object.class) {
                aVar = b;
            }
            if (aVar == null) {
                aVar = new a(cls);
            }
            map.put(cls, aVar);
        }
        return aVar;
    }

    public static e a(Object obj, Class<?> cls, String str) {
        Map<String, e> map;
        Map<Class<?>, Map<String, e>> map2 = f448a.get().b;
        Class<?> cls2 = obj.getClass();
        Map<String, e> map3 = map2.get(cls2);
        if (map3 == null) {
            HashMap map4 = new HashMap();
            map2.put(cls2, map4);
            map = map4;
        } else {
            map = map3;
        }
        e eVar = map.get(str);
        if (eVar != null) {
            return eVar;
        }
        e eVarA = a(b(obj, cls, str));
        map.put(str, eVarA);
        return eVarA;
    }

    private static Class<?> a(Class<?> cls, String str) {
        throw new IllegalStateException("cannot determine the type of the type parameter '" + str + "': " + cls);
    }

    private static Class<?> b(Object obj, Class<?> cls, String str) {
        Class<?> cls2 = obj.getClass();
        Class<?> superclass = cls2;
        while (true) {
            if (superclass.getSuperclass() == cls) {
                TypeVariable<Class<? super Object>>[] typeParameters = superclass.getSuperclass().getTypeParameters();
                int i = 0;
                while (true) {
                    if (i >= typeParameters.length) {
                        i = -1;
                        break;
                    }
                    if (str.equals(typeParameters[i].getName())) {
                        break;
                    }
                    i++;
                }
                if (i < 0) {
                    throw new IllegalStateException("unknown type parameter '" + str + "': " + cls);
                }
                Type genericSuperclass = superclass.getGenericSuperclass();
                if (!(genericSuperclass instanceof ParameterizedType)) {
                    return Object.class;
                }
                Type rawType = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[i];
                if (rawType instanceof ParameterizedType) {
                    rawType = ((ParameterizedType) rawType).getRawType();
                }
                if (rawType instanceof Class) {
                    return (Class) rawType;
                }
                if (rawType instanceof GenericArrayType) {
                    Type genericComponentType = ((GenericArrayType) rawType).getGenericComponentType();
                    if (genericComponentType instanceof ParameterizedType) {
                        genericComponentType = ((ParameterizedType) genericComponentType).getRawType();
                    }
                    if (genericComponentType instanceof Class) {
                        return Array.newInstance((Class<?>) genericComponentType, 0).getClass();
                    }
                }
                if (!(rawType instanceof TypeVariable)) {
                    return a(cls2, str);
                }
                TypeVariable typeVariable = (TypeVariable) rawType;
                if (!(typeVariable.getGenericDeclaration() instanceof Class)) {
                    return Object.class;
                }
                Class<?> cls3 = (Class) typeVariable.getGenericDeclaration();
                str = typeVariable.getName();
                if (!cls3.isAssignableFrom(cls2)) {
                    return Object.class;
                }
                superclass = cls2;
                cls = cls3;
            } else {
                superclass = superclass.getSuperclass();
                if (superclass == null) {
                    return a(cls2, str);
                }
            }
        }
    }

    public abstract boolean a(Object obj);
}
