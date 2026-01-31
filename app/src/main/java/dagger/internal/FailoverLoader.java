package dagger.internal;

import dagger.internal.loaders.GeneratedAdapters;
import dagger.internal.loaders.ReflectiveAtInjectBinding;
import dagger.internal.loaders.ReflectiveStaticInjection;

/* loaded from: classes.dex */
public final class FailoverLoader extends Loader {
    private final Memoizer<Class<?>, ModuleAdapter<?>> loadedAdapters = new Memoizer<Class<?>, ModuleAdapter<?>>() { // from class: dagger.internal.FailoverLoader.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // dagger.internal.Memoizer
        public ModuleAdapter<?> create(Class<?> type) {
            ModuleAdapter<?> result = (ModuleAdapter) FailoverLoader.this.instantiate(type.getName().concat(GeneratedAdapters.MODULE_ADAPTER_SUFFIX), type.getClassLoader());
            if (result == null) {
                throw new IllegalStateException("Module adapter for " + type + " could not be loaded. Please ensure that code generation was run for this module.");
            }
            return result;
        }
    };

    @Override // dagger.internal.Loader
    public <T> ModuleAdapter<T> getModuleAdapter(Class<T> type) {
        return (ModuleAdapter) this.loadedAdapters.get(type);
    }

    @Override // dagger.internal.Loader
    public Binding<?> getAtInjectBinding(String key, String className, ClassLoader classLoader, boolean mustHaveInjections) {
        Binding<?> result = (Binding) instantiate(className.concat(GeneratedAdapters.INJECT_ADAPTER_SUFFIX), classLoader);
        if (result == null) {
            Class<?> type = loadClass(classLoader, className);
            if (type.equals(Void.class)) {
                throw new IllegalStateException(String.format("Could not load class %s needed for binding %s", className, key));
            }
            if (type.isInterface()) {
                return null;
            }
            return ReflectiveAtInjectBinding.create(type, mustHaveInjections);
        }
        return result;
    }

    @Override // dagger.internal.Loader
    public StaticInjection getStaticInjection(Class<?> injectedClass) {
        StaticInjection result = (StaticInjection) instantiate(injectedClass.getName().concat(GeneratedAdapters.STATIC_INJECTION_SUFFIX), injectedClass.getClassLoader());
        return result != null ? result : ReflectiveStaticInjection.create(injectedClass);
    }
}
