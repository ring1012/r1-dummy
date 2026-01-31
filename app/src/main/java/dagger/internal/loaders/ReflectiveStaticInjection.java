package dagger.internal.loaders;

import dagger.internal.Binding;
import dagger.internal.Keys;
import dagger.internal.Linker;
import dagger.internal.StaticInjection;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/* loaded from: classes.dex */
public final class ReflectiveStaticInjection extends StaticInjection {
    private Binding<?>[] bindings;
    private final Field[] fields;
    private final ClassLoader loader;

    private ReflectiveStaticInjection(ClassLoader loader, Field[] fields) {
        this.fields = fields;
        this.loader = loader;
    }

    @Override // dagger.internal.StaticInjection
    public void attach(Linker linker) {
        this.bindings = new Binding[this.fields.length];
        for (int i = 0; i < this.fields.length; i++) {
            Field field = this.fields[i];
            String key = Keys.get(field.getGenericType(), field.getAnnotations(), field);
            this.bindings[i] = linker.requestBinding(key, field, this.loader);
        }
    }

    @Override // dagger.internal.StaticInjection
    public void inject() throws IllegalAccessException, IllegalArgumentException {
        for (int f = 0; f < this.fields.length; f++) {
            try {
                this.fields[f].set(null, this.bindings[f].get());
            } catch (IllegalAccessException e) {
                throw new AssertionError(e);
            }
        }
    }

    public static StaticInjection create(Class<?> injectedClass) {
        List<Field> fields = new ArrayList<>();
        Field[] arr$ = injectedClass.getDeclaredFields();
        for (Field field : arr$) {
            if (Modifier.isStatic(field.getModifiers()) && field.isAnnotationPresent(Inject.class)) {
                field.setAccessible(true);
                fields.add(field);
            }
        }
        if (fields.isEmpty()) {
            throw new IllegalArgumentException("No static injections: " + injectedClass.getName());
        }
        return new ReflectiveStaticInjection(injectedClass.getClassLoader(), (Field[]) fields.toArray(new Field[fields.size()]));
    }
}
