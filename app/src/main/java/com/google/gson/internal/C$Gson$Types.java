package com.google.gson.internal;

import cn.yunzhisheng.asr.JniUscClient;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;

/* renamed from: com.google.gson.internal.$Gson$Types, reason: invalid class name */
/* loaded from: classes.dex */
public final class C$Gson$Types {
    static final Type[] EMPTY_TYPE_ARRAY = new Type[0];

    private C$Gson$Types() {
        throw new UnsupportedOperationException();
    }

    public static ParameterizedType newParameterizedTypeWithOwner(Type ownerType, Type rawType, Type... typeArguments) {
        return new ParameterizedTypeImpl(ownerType, rawType, typeArguments);
    }

    public static GenericArrayType arrayOf(Type componentType) {
        return new GenericArrayTypeImpl(componentType);
    }

    public static WildcardType subtypeOf(Type bound) {
        return new WildcardTypeImpl(new Type[]{bound}, EMPTY_TYPE_ARRAY);
    }

    public static WildcardType supertypeOf(Type bound) {
        return new WildcardTypeImpl(new Type[]{Object.class}, new Type[]{bound});
    }

    public static Type canonicalize(Type type) {
        if (type instanceof Class) {
            Class<?> c = (Class) type;
            return c.isArray() ? new GenericArrayTypeImpl(canonicalize(c.getComponentType())) : c;
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType p = (ParameterizedType) type;
            return new ParameterizedTypeImpl(p.getOwnerType(), p.getRawType(), p.getActualTypeArguments());
        }
        if (type instanceof GenericArrayType) {
            GenericArrayType g = (GenericArrayType) type;
            return new GenericArrayTypeImpl(g.getGenericComponentType());
        }
        if (!(type instanceof WildcardType)) {
            return type;
        }
        WildcardType w = (WildcardType) type;
        return new WildcardTypeImpl(w.getUpperBounds(), w.getLowerBounds());
    }

    public static Class<?> getRawType(Type type) {
        if (type instanceof Class) {
            return (Class) type;
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            C$Gson$Preconditions.checkArgument(rawType instanceof Class);
            return (Class) rawType;
        }
        if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return Array.newInstance(getRawType(componentType), 0).getClass();
        }
        if (type instanceof TypeVariable) {
            return Object.class;
        }
        if (type instanceof WildcardType) {
            return getRawType(((WildcardType) type).getUpperBounds()[0]);
        }
        String className = type == null ? JniUscClient.az : type.getClass().getName();
        throw new IllegalArgumentException("Expected a Class, ParameterizedType, or GenericArrayType, but <" + type + "> is of type " + className);
    }

    static boolean equal(Object a2, Object b) {
        return a2 == b || (a2 != null && a2.equals(b));
    }

    public static boolean equals(Type a2, Type b) {
        if (a2 == b) {
            return true;
        }
        if (a2 instanceof Class) {
            return a2.equals(b);
        }
        if (a2 instanceof ParameterizedType) {
            if (!(b instanceof ParameterizedType)) {
                return false;
            }
            ParameterizedType pa = (ParameterizedType) a2;
            ParameterizedType pb = (ParameterizedType) b;
            return equal(pa.getOwnerType(), pb.getOwnerType()) && pa.getRawType().equals(pb.getRawType()) && Arrays.equals(pa.getActualTypeArguments(), pb.getActualTypeArguments());
        }
        if (a2 instanceof GenericArrayType) {
            if (!(b instanceof GenericArrayType)) {
                return false;
            }
            GenericArrayType ga = (GenericArrayType) a2;
            GenericArrayType gb = (GenericArrayType) b;
            return equals(ga.getGenericComponentType(), gb.getGenericComponentType());
        }
        if (a2 instanceof WildcardType) {
            if (!(b instanceof WildcardType)) {
                return false;
            }
            WildcardType wa = (WildcardType) a2;
            WildcardType wb = (WildcardType) b;
            return Arrays.equals(wa.getUpperBounds(), wb.getUpperBounds()) && Arrays.equals(wa.getLowerBounds(), wb.getLowerBounds());
        }
        if (!(a2 instanceof TypeVariable) || !(b instanceof TypeVariable)) {
            return false;
        }
        TypeVariable<?> va = (TypeVariable) a2;
        TypeVariable<?> vb = (TypeVariable) b;
        return va.getGenericDeclaration() == vb.getGenericDeclaration() && va.getName().equals(vb.getName());
    }

    static int hashCodeOrZero(Object o) {
        if (o != null) {
            return o.hashCode();
        }
        return 0;
    }

    public static String typeToString(Type type) {
        return type instanceof Class ? ((Class) type).getName() : type.toString();
    }

    static Type getGenericSupertype(Type context, Class<?> rawType, Class<?> toResolve) {
        if (toResolve != rawType) {
            if (toResolve.isInterface()) {
                Class<?>[] interfaces = rawType.getInterfaces();
                int length = interfaces.length;
                for (int i = 0; i < length; i++) {
                    if (interfaces[i] == toResolve) {
                        Type context2 = rawType.getGenericInterfaces()[i];
                        return context2;
                    }
                    if (toResolve.isAssignableFrom(interfaces[i])) {
                        Type context3 = getGenericSupertype(rawType.getGenericInterfaces()[i], interfaces[i], toResolve);
                        return context3;
                    }
                }
            }
            if (!rawType.isInterface()) {
                while (rawType != Object.class) {
                    Class<?> rawSupertype = rawType.getSuperclass();
                    if (rawSupertype == toResolve) {
                        Type context4 = rawType.getGenericSuperclass();
                        return context4;
                    }
                    if (toResolve.isAssignableFrom(rawSupertype)) {
                        Type context5 = getGenericSupertype(rawType.getGenericSuperclass(), rawSupertype, toResolve);
                        return context5;
                    }
                    rawType = rawSupertype;
                }
            }
            return toResolve;
        }
        return context;
    }

    static Type getSupertype(Type context, Class<?> contextRawType, Class<?> supertype) {
        C$Gson$Preconditions.checkArgument(supertype.isAssignableFrom(contextRawType));
        return resolve(context, contextRawType, getGenericSupertype(context, contextRawType, supertype));
    }

    public static Type getArrayComponentType(Type array) {
        if (array instanceof GenericArrayType) {
            return ((GenericArrayType) array).getGenericComponentType();
        }
        return ((Class) array).getComponentType();
    }

    public static Type getCollectionElementType(Type context, Class<?> contextRawType) {
        Type collectionType = getSupertype(context, contextRawType, Collection.class);
        if (collectionType instanceof WildcardType) {
            collectionType = ((WildcardType) collectionType).getUpperBounds()[0];
        }
        return collectionType instanceof ParameterizedType ? ((ParameterizedType) collectionType).getActualTypeArguments()[0] : Object.class;
    }

    public static Type[] getMapKeyAndValueTypes(Type context, Class<?> contextRawType) {
        if (context == Properties.class) {
            return new Type[]{String.class, String.class};
        }
        Type mapType = getSupertype(context, contextRawType, Map.class);
        if (mapType instanceof ParameterizedType) {
            ParameterizedType mapParameterizedType = (ParameterizedType) mapType;
            return mapParameterizedType.getActualTypeArguments();
        }
        return new Type[]{Object.class, Object.class};
    }

    public static Type resolve(Type type, Class<?> cls, Type type2) {
        while (type2 instanceof TypeVariable) {
            TypeVariable typeVariable = (TypeVariable) type2;
            type2 = resolveTypeVariable(type, cls, typeVariable);
            if (type2 == typeVariable) {
                return type2;
            }
        }
        if ((type2 instanceof Class) && ((Class) type2).isArray()) {
            Class cls2 = (Class) type2;
            Class<?> componentType = cls2.getComponentType();
            Type typeResolve = resolve(type, cls, componentType);
            Type typeArrayOf = cls2;
            if (componentType != typeResolve) {
                typeArrayOf = arrayOf(typeResolve);
            }
            return typeArrayOf;
        }
        if (type2 instanceof GenericArrayType) {
            GenericArrayType genericArrayType = (GenericArrayType) type2;
            Type genericComponentType = genericArrayType.getGenericComponentType();
            Type typeResolve2 = resolve(type, cls, genericComponentType);
            if (genericComponentType != typeResolve2) {
                return arrayOf(typeResolve2);
            }
            return genericArrayType;
        }
        if (type2 instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type2;
            Type ownerType = parameterizedType.getOwnerType();
            Type typeResolve3 = resolve(type, cls, ownerType);
            boolean z = typeResolve3 != ownerType;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            int length = actualTypeArguments.length;
            for (int i = 0; i < length; i++) {
                Type typeResolve4 = resolve(type, cls, actualTypeArguments[i]);
                if (typeResolve4 != actualTypeArguments[i]) {
                    if (!z) {
                        actualTypeArguments = (Type[]) actualTypeArguments.clone();
                        z = true;
                    }
                    actualTypeArguments[i] = typeResolve4;
                }
            }
            if (z) {
                return newParameterizedTypeWithOwner(typeResolve3, parameterizedType.getRawType(), actualTypeArguments);
            }
            return parameterizedType;
        }
        if (!(type2 instanceof WildcardType)) {
            return type2;
        }
        WildcardType wildcardType = (WildcardType) type2;
        Type[] lowerBounds = wildcardType.getLowerBounds();
        Type[] upperBounds = wildcardType.getUpperBounds();
        if (lowerBounds.length == 1) {
            Type typeResolve5 = resolve(type, cls, lowerBounds[0]);
            if (typeResolve5 != lowerBounds[0]) {
                return supertypeOf(typeResolve5);
            }
            return wildcardType;
        }
        if (upperBounds.length == 1) {
            Type typeResolve6 = resolve(type, cls, upperBounds[0]);
            if (typeResolve6 != upperBounds[0]) {
                return subtypeOf(typeResolve6);
            }
            return wildcardType;
        }
        return wildcardType;
    }

    static Type resolveTypeVariable(Type context, Class<?> contextRawType, TypeVariable<?> unknown) {
        Class<?> declaredByRaw = declaringClassOf(unknown);
        if (declaredByRaw != null) {
            Type declaredBy = getGenericSupertype(context, contextRawType, declaredByRaw);
            if (declaredBy instanceof ParameterizedType) {
                int index = indexOf(declaredByRaw.getTypeParameters(), unknown);
                return ((ParameterizedType) declaredBy).getActualTypeArguments()[index];
            }
            return unknown;
        }
        return unknown;
    }

    private static int indexOf(Object[] array, Object toFind) {
        for (int i = 0; i < array.length; i++) {
            if (toFind.equals(array[i])) {
                return i;
            }
        }
        throw new NoSuchElementException();
    }

    private static Class<?> declaringClassOf(TypeVariable<?> typeVariable) {
        GenericDeclaration genericDeclaration = typeVariable.getGenericDeclaration();
        if (genericDeclaration instanceof Class) {
            return (Class) genericDeclaration;
        }
        return null;
    }

    static void checkNotPrimitive(Type type) {
        C$Gson$Preconditions.checkArgument(((type instanceof Class) && ((Class) type).isPrimitive()) ? false : true);
    }

    /* compiled from: $Gson$Types.java */
    /* renamed from: com.google.gson.internal.$Gson$Types$ParameterizedTypeImpl */
    private static final class ParameterizedTypeImpl implements ParameterizedType, Serializable {
        private static final long serialVersionUID = 0;
        private final Type ownerType;
        private final Type rawType;
        private final Type[] typeArguments;

        public ParameterizedTypeImpl(Type ownerType, Type rawType, Type... typeArguments) {
            if (rawType instanceof Class) {
                Class<?> rawTypeAsClass = (Class) rawType;
                boolean isStaticOrTopLevelClass = Modifier.isStatic(rawTypeAsClass.getModifiers()) || rawTypeAsClass.getEnclosingClass() == null;
                C$Gson$Preconditions.checkArgument(ownerType != null || isStaticOrTopLevelClass);
            }
            this.ownerType = ownerType == null ? null : C$Gson$Types.canonicalize(ownerType);
            this.rawType = C$Gson$Types.canonicalize(rawType);
            this.typeArguments = (Type[]) typeArguments.clone();
            for (int t = 0; t < this.typeArguments.length; t++) {
                C$Gson$Preconditions.checkNotNull(this.typeArguments[t]);
                C$Gson$Types.checkNotPrimitive(this.typeArguments[t]);
                this.typeArguments[t] = C$Gson$Types.canonicalize(this.typeArguments[t]);
            }
        }

        @Override // java.lang.reflect.ParameterizedType
        public Type[] getActualTypeArguments() {
            return (Type[]) this.typeArguments.clone();
        }

        @Override // java.lang.reflect.ParameterizedType
        public Type getRawType() {
            return this.rawType;
        }

        @Override // java.lang.reflect.ParameterizedType
        public Type getOwnerType() {
            return this.ownerType;
        }

        public boolean equals(Object other) {
            return (other instanceof ParameterizedType) && C$Gson$Types.equals(this, (ParameterizedType) other);
        }

        public int hashCode() {
            return (Arrays.hashCode(this.typeArguments) ^ this.rawType.hashCode()) ^ C$Gson$Types.hashCodeOrZero(this.ownerType);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder((this.typeArguments.length + 1) * 30);
            stringBuilder.append(C$Gson$Types.typeToString(this.rawType));
            if (this.typeArguments.length == 0) {
                return stringBuilder.toString();
            }
            stringBuilder.append("<").append(C$Gson$Types.typeToString(this.typeArguments[0]));
            for (int i = 1; i < this.typeArguments.length; i++) {
                stringBuilder.append(", ").append(C$Gson$Types.typeToString(this.typeArguments[i]));
            }
            return stringBuilder.append(">").toString();
        }
    }

    /* compiled from: $Gson$Types.java */
    /* renamed from: com.google.gson.internal.$Gson$Types$GenericArrayTypeImpl */
    private static final class GenericArrayTypeImpl implements GenericArrayType, Serializable {
        private static final long serialVersionUID = 0;
        private final Type componentType;

        public GenericArrayTypeImpl(Type componentType) {
            this.componentType = C$Gson$Types.canonicalize(componentType);
        }

        @Override // java.lang.reflect.GenericArrayType
        public Type getGenericComponentType() {
            return this.componentType;
        }

        public boolean equals(Object o) {
            return (o instanceof GenericArrayType) && C$Gson$Types.equals(this, (GenericArrayType) o);
        }

        public int hashCode() {
            return this.componentType.hashCode();
        }

        public String toString() {
            return C$Gson$Types.typeToString(this.componentType) + "[]";
        }
    }

    /* compiled from: $Gson$Types.java */
    /* renamed from: com.google.gson.internal.$Gson$Types$WildcardTypeImpl */
    private static final class WildcardTypeImpl implements WildcardType, Serializable {
        private static final long serialVersionUID = 0;
        private final Type lowerBound;
        private final Type upperBound;

        public WildcardTypeImpl(Type[] upperBounds, Type[] lowerBounds) {
            C$Gson$Preconditions.checkArgument(lowerBounds.length <= 1);
            C$Gson$Preconditions.checkArgument(upperBounds.length == 1);
            if (lowerBounds.length == 1) {
                C$Gson$Preconditions.checkNotNull(lowerBounds[0]);
                C$Gson$Types.checkNotPrimitive(lowerBounds[0]);
                C$Gson$Preconditions.checkArgument(upperBounds[0] == Object.class);
                this.lowerBound = C$Gson$Types.canonicalize(lowerBounds[0]);
                this.upperBound = Object.class;
                return;
            }
            C$Gson$Preconditions.checkNotNull(upperBounds[0]);
            C$Gson$Types.checkNotPrimitive(upperBounds[0]);
            this.lowerBound = null;
            this.upperBound = C$Gson$Types.canonicalize(upperBounds[0]);
        }

        @Override // java.lang.reflect.WildcardType
        public Type[] getUpperBounds() {
            return new Type[]{this.upperBound};
        }

        @Override // java.lang.reflect.WildcardType
        public Type[] getLowerBounds() {
            return this.lowerBound != null ? new Type[]{this.lowerBound} : C$Gson$Types.EMPTY_TYPE_ARRAY;
        }

        public boolean equals(Object other) {
            return (other instanceof WildcardType) && C$Gson$Types.equals(this, (WildcardType) other);
        }

        public int hashCode() {
            return (this.lowerBound != null ? this.lowerBound.hashCode() + 31 : 1) ^ (this.upperBound.hashCode() + 31);
        }

        public String toString() {
            if (this.lowerBound != null) {
                return "? super " + C$Gson$Types.typeToString(this.lowerBound);
            }
            if (this.upperBound == Object.class) {
                return "?";
            }
            return "? extends " + C$Gson$Types.typeToString(this.upperBound);
        }
    }
}
