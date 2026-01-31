package dagger.internal;

import butterknife.internal.ButterKnifeProcessor;
import dagger.Lazy;
import dagger.MembersInjector;
import java.lang.annotation.Annotation;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;
import javax.inject.Provider;
import javax.inject.Qualifier;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/* loaded from: classes.dex */
public final class Keys {
    private static final String PROVIDER_PREFIX = Provider.class.getCanonicalName() + "<";
    private static final String MEMBERS_INJECTOR_PREFIX = MembersInjector.class.getCanonicalName() + "<";
    private static final String LAZY_PREFIX = Lazy.class.getCanonicalName() + "<";
    private static final String SET_PREFIX = Set.class.getCanonicalName() + "<";
    private static final Memoizer<Class<? extends Annotation>, Boolean> IS_QUALIFIER_ANNOTATION = new Memoizer<Class<? extends Annotation>, Boolean>() { // from class: dagger.internal.Keys.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // dagger.internal.Memoizer
        public Boolean create(Class<? extends Annotation> annotationType) {
            return Boolean.valueOf(annotationType.isAnnotationPresent(Qualifier.class));
        }
    };

    Keys() {
    }

    public static String get(Type type) {
        return get(type, null);
    }

    public static String getMembersKey(Class<?> key) {
        return "members/".concat(key.getName());
    }

    private static String get(Type type, Annotation annotation) {
        Type type2 = boxIfPrimitive(type);
        if (annotation == null && (type2 instanceof Class) && !((Class) type2).isArray()) {
            return ((Class) type2).getName();
        }
        StringBuilder result = new StringBuilder();
        if (annotation != null) {
            result.append(annotation).append(MqttTopic.TOPIC_LEVEL_SEPARATOR);
        }
        typeToString(type2, result, true);
        return result.toString();
    }

    public static String getSetKey(Type type, Annotation[] annotations, Object subject) {
        Annotation qualifier = extractQualifier(annotations, subject);
        Type type2 = boxIfPrimitive(type);
        StringBuilder result = new StringBuilder();
        if (qualifier != null) {
            result.append(qualifier).append(MqttTopic.TOPIC_LEVEL_SEPARATOR);
        }
        result.append(SET_PREFIX);
        typeToString(type2, result, true);
        result.append(">");
        return result.toString();
    }

    public static String get(Type type, Annotation[] annotations, Object subject) {
        return get(type, extractQualifier(annotations, subject));
    }

    private static Annotation extractQualifier(Annotation[] annotations, Object subject) {
        Annotation qualifier = null;
        for (Annotation a2 : annotations) {
            if (IS_QUALIFIER_ANNOTATION.get(a2.annotationType()).booleanValue()) {
                if (qualifier != null) {
                    throw new IllegalArgumentException("Too many qualifier annotations on " + subject);
                }
                qualifier = a2;
            }
        }
        return qualifier;
    }

    private static void typeToString(Type type, StringBuilder result, boolean topLevel) {
        if (type instanceof Class) {
            Class<?> c = (Class) type;
            if (c.isArray()) {
                typeToString(c.getComponentType(), result, false);
                result.append("[]");
                return;
            } else {
                if (c.isPrimitive()) {
                    if (topLevel) {
                        throw new UnsupportedOperationException("Uninjectable type " + c.getName());
                    }
                    result.append(c.getName());
                    return;
                }
                result.append(c.getName());
                return;
            }
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            typeToString(parameterizedType.getRawType(), result, true);
            Type[] arguments = parameterizedType.getActualTypeArguments();
            result.append("<");
            for (int i = 0; i < arguments.length; i++) {
                if (i != 0) {
                    result.append(", ");
                }
                typeToString(arguments[i], result, true);
            }
            result.append(">");
            return;
        }
        if (type instanceof GenericArrayType) {
            GenericArrayType genericArrayType = (GenericArrayType) type;
            typeToString(genericArrayType.getGenericComponentType(), result, false);
            result.append("[]");
            return;
        }
        throw new UnsupportedOperationException("Uninjectable type " + type);
    }

    static String getBuiltInBindingsKey(String key) {
        int start = startOfType(key);
        if (substringStartsWith(key, start, PROVIDER_PREFIX)) {
            return extractKey(key, start, key.substring(0, start), PROVIDER_PREFIX);
        }
        if (substringStartsWith(key, start, MEMBERS_INJECTOR_PREFIX)) {
            return extractKey(key, start, "members/", MEMBERS_INJECTOR_PREFIX);
        }
        return null;
    }

    static String getLazyKey(String key) {
        int start = startOfType(key);
        if (substringStartsWith(key, start, LAZY_PREFIX)) {
            return extractKey(key, start, key.substring(0, start), LAZY_PREFIX);
        }
        return null;
    }

    private static int startOfType(String key) {
        if (key.startsWith("@")) {
            return key.lastIndexOf(47) + 1;
        }
        return 0;
    }

    private static String extractKey(String key, int start, String delegatePrefix, String prefix) {
        return delegatePrefix + key.substring(prefix.length() + start, key.length() - 1);
    }

    private static boolean substringStartsWith(String string, int offset, String substring) {
        return string.regionMatches(offset, substring, 0, substring.length());
    }

    public static boolean isAnnotated(String key) {
        return key.startsWith("@");
    }

    public static String getClassName(String key) {
        int start = 0;
        if (key.startsWith("@") || key.startsWith("members/")) {
            start = key.lastIndexOf(47) + 1;
        }
        if (key.indexOf(60, start) == -1 && key.indexOf(91, start) == -1) {
            return key.substring(start);
        }
        return null;
    }

    public static boolean isPlatformType(String name) {
        return name.startsWith(ButterKnifeProcessor.JAVA_PREFIX) || name.startsWith("javax.") || name.startsWith(ButterKnifeProcessor.ANDROID_PREFIX);
    }

    private static Type boxIfPrimitive(Type type) {
        return type == Byte.TYPE ? Byte.class : type == Short.TYPE ? Short.class : type == Integer.TYPE ? Integer.class : type == Long.TYPE ? Long.class : type == Character.TYPE ? Character.class : type == Boolean.TYPE ? Boolean.class : type == Float.TYPE ? Float.class : type == Double.TYPE ? Double.class : type == Void.TYPE ? Void.class : type;
    }
}
