package dagger.internal.loaders;

import dagger.internal.Binding;
import dagger.internal.Keys;
import dagger.internal.Linker;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;

/* loaded from: classes.dex */
public final class ReflectiveAtInjectBinding<T> extends Binding<T> {
    private final Constructor<T> constructor;
    private final Binding<?>[] fieldBindings;
    private final Field[] fields;
    private final String[] keys;
    private final ClassLoader loader;
    private final Binding<?>[] parameterBindings;
    private final Class<?> supertype;
    private Binding<? super T> supertypeBinding;

    private ReflectiveAtInjectBinding(String provideKey, String membersKey, boolean singleton, Class<?> type, Field[] fields, Constructor<T> constructor, int parameterCount, Class<?> supertype, String[] keys) {
        super(provideKey, membersKey, singleton, type);
        this.constructor = constructor;
        this.fields = fields;
        this.supertype = supertype;
        this.keys = keys;
        this.parameterBindings = new Binding[parameterCount];
        this.fieldBindings = new Binding[fields.length];
        this.loader = type.getClassLoader();
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        int i = 0;
        for (int i2 = 0; i2 < this.fields.length; i2++) {
            if (this.fieldBindings[i2] == null) {
                this.fieldBindings[i2] = linker.requestBinding(this.keys[i], this.fields[i2], this.loader);
            }
            i++;
        }
        if (this.constructor != null) {
            for (int i3 = 0; i3 < this.parameterBindings.length; i3++) {
                if (this.parameterBindings[i3] == null) {
                    this.parameterBindings[i3] = linker.requestBinding(this.keys[i], this.constructor, this.loader);
                }
                i++;
            }
        }
        if (this.supertype != null && this.supertypeBinding == null) {
            this.supertypeBinding = (Binding<? super T>) linker.requestBinding(this.keys[i], this.membersKey, this.loader, false, true);
        }
    }

    @Override // dagger.internal.Binding, javax.inject.Provider
    public T get() throws Throwable {
        if (this.constructor == null) {
            throw new UnsupportedOperationException();
        }
        Object[] args = new Object[this.parameterBindings.length];
        for (int i = 0; i < this.parameterBindings.length; i++) {
            args[i] = this.parameterBindings[i].get();
        }
        try {
            T result = this.constructor.newInstance(args);
            injectMembers(result);
            return result;
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InstantiationException e2) {
            throw new RuntimeException(e2);
        } catch (InvocationTargetException e3) {
            Throwable cause = e3.getCause();
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            }
            throw new RuntimeException(cause);
        }
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(T t) throws IllegalAccessException, IllegalArgumentException {
        for (int i = 0; i < this.fields.length; i++) {
            try {
                this.fields[i].set(t, this.fieldBindings[i].get());
            } catch (IllegalAccessException e) {
                throw new AssertionError(e);
            }
        }
        if (this.supertypeBinding != null) {
            this.supertypeBinding.injectMembers(t);
        }
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> get, Set<Binding<?>> injectMembers) {
        if (this.parameterBindings != null) {
            Collections.addAll(get, this.parameterBindings);
        }
        Collections.addAll(injectMembers, this.fieldBindings);
        if (this.supertypeBinding != null) {
            injectMembers.add(this.supertypeBinding);
        }
    }

    @Override // dagger.internal.Binding
    public String toString() {
        return this.provideKey != null ? this.provideKey : this.membersKey;
    }

    public static <T> Binding<T> create(Class<T> type, boolean mustHaveInjections) throws NoSuchMethodException, SecurityException {
        String provideKey;
        int parameterCount;
        boolean singleton = type.isAnnotationPresent(Singleton.class);
        List<String> keys = new ArrayList<>();
        List<Field> injectedFields = new ArrayList<>();
        for (Class<T> superclass = type; superclass != Object.class; superclass = superclass.getSuperclass()) {
            Field[] arr$ = superclass.getDeclaredFields();
            for (Field field : arr$) {
                if (field.isAnnotationPresent(Inject.class) && !Modifier.isStatic(field.getModifiers())) {
                    if ((field.getModifiers() & 2) != 0) {
                        throw new IllegalStateException("Can't inject private field: " + field);
                    }
                    field.setAccessible(true);
                    injectedFields.add(field);
                    keys.add(Keys.get(field.getGenericType(), field.getAnnotations(), field));
                }
            }
        }
        Constructor<T> injectedConstructor = null;
        Constructor<T>[] arr$2 = getConstructorsForType(type);
        for (Constructor<T> constructor : arr$2) {
            if (constructor.isAnnotationPresent(Inject.class)) {
                if (injectedConstructor != null) {
                    throw new Binding.InvalidBindingException(type.getName(), "has too many injectable constructors");
                }
                injectedConstructor = constructor;
            }
        }
        if (injectedConstructor == null) {
            if (!injectedFields.isEmpty()) {
                try {
                    injectedConstructor = type.getDeclaredConstructor(new Class[0]);
                } catch (NoSuchMethodException e) {
                }
            } else if (mustHaveInjections) {
                throw new Binding.InvalidBindingException(type.getName(), "has no injectable members. Do you want to add an injectable constructor?");
            }
        }
        if (injectedConstructor != null) {
            if ((injectedConstructor.getModifiers() & 2) != 0) {
                throw new IllegalStateException("Can't inject private constructor: " + injectedConstructor);
            }
            provideKey = Keys.get(type);
            injectedConstructor.setAccessible(true);
            Type[] types = injectedConstructor.getGenericParameterTypes();
            parameterCount = types.length;
            if (parameterCount != 0) {
                Annotation[][] annotations = injectedConstructor.getParameterAnnotations();
                for (int p = 0; p < types.length; p++) {
                    keys.add(Keys.get(types[p], annotations[p], injectedConstructor));
                }
            }
        } else {
            provideKey = null;
            parameterCount = 0;
            if (singleton) {
                throw new IllegalArgumentException("No injectable constructor on @Singleton " + type.getName());
            }
        }
        Class<? super T> supertype = type.getSuperclass();
        if (supertype != null) {
            if (Keys.isPlatformType(supertype.getName())) {
                supertype = null;
            } else {
                keys.add(Keys.getMembersKey(supertype));
            }
        }
        String membersKey = Keys.getMembersKey(type);
        return new ReflectiveAtInjectBinding(provideKey, membersKey, singleton, type, (Field[]) injectedFields.toArray(new Field[injectedFields.size()]), injectedConstructor, parameterCount, supertype, (String[]) keys.toArray(new String[keys.size()]));
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static <T> Constructor<T>[] getConstructorsForType(Class<T> type) {
        return type.getDeclaredConstructors();
    }
}
