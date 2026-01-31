package dagger.internal;

import cn.yunzhisheng.common.PinyinConverter;
import dagger.internal.Binding;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/* loaded from: classes.dex */
public final class Linker {
    private static final Object UNINITIALIZED = new Object();
    private final Linker base;
    private final ErrorHandler errorHandler;
    private final Loader plugin;
    private final Queue<Binding<?>> toLink = new ArrayQueue();
    private boolean attachSuccess = true;
    private final List<String> errors = new ArrayList();
    private final Map<String, Binding<?>> bindings = new HashMap();
    private volatile Map<String, Binding<?>> linkedBindings = null;

    public interface ErrorHandler {
        public static final ErrorHandler NULL = new ErrorHandler() { // from class: dagger.internal.Linker.ErrorHandler.1
            @Override // dagger.internal.Linker.ErrorHandler
            public void handleErrors(List<String> errors) {
            }
        };

        void handleErrors(List<String> list);
    }

    public Linker(Linker base, Loader plugin, ErrorHandler errorHandler) {
        if (plugin == null) {
            throw new NullPointerException("plugin");
        }
        if (errorHandler == null) {
            throw new NullPointerException("errorHandler");
        }
        this.base = base;
        this.plugin = plugin;
        this.errorHandler = errorHandler;
    }

    public void installBindings(BindingsGroup toInstall) {
        if (this.linkedBindings != null) {
            throw new IllegalStateException("Cannot install further bindings after calling linkAll().");
        }
        for (Map.Entry<String, Binding<?>> entry : toInstall.entrySet()) {
            this.bindings.put(entry.getKey(), scope(entry.getValue()));
        }
    }

    public Map<String, Binding<?>> linkAll() {
        assertLockHeld();
        if (this.linkedBindings != null) {
            return this.linkedBindings;
        }
        for (Binding<?> binding : this.bindings.values()) {
            if (!binding.isLinked()) {
                this.toLink.add(binding);
            }
        }
        linkRequested();
        this.linkedBindings = Collections.unmodifiableMap(this.bindings);
        return this.linkedBindings;
    }

    public Map<String, Binding<?>> fullyLinkedBindings() {
        return this.linkedBindings;
    }

    public void linkRequested() {
        assertLockHeld();
        while (true) {
            Binding<?> binding = this.toLink.poll();
            if (binding != null) {
                if (binding instanceof DeferredBinding) {
                    DeferredBinding deferred = (DeferredBinding) binding;
                    String key = deferred.deferredKey;
                    boolean mustHaveInjections = deferred.mustHaveInjections;
                    if (this.bindings.containsKey(key)) {
                        continue;
                    } else {
                        try {
                            Binding<?> resolvedBinding = createBinding(key, binding.requiredBy, deferred.classLoader, mustHaveInjections);
                            resolvedBinding.setLibrary(binding.library());
                            resolvedBinding.setDependedOn(binding.dependedOn());
                            if (!key.equals(resolvedBinding.provideKey) && !key.equals(resolvedBinding.membersKey)) {
                                throw new IllegalStateException("Unable to create binding for " + key);
                            }
                            Binding<?> scopedBinding = scope(resolvedBinding);
                            this.toLink.add(scopedBinding);
                            putBinding(scopedBinding);
                        } catch (Binding.InvalidBindingException e) {
                            addError(e.type + PinyinConverter.PINYIN_SEPARATOR + e.getMessage() + " required by " + binding.requiredBy);
                            this.bindings.put(key, Binding.UNRESOLVED);
                        } catch (IllegalArgumentException e2) {
                            addError(e2.getMessage() + " required by " + binding.requiredBy);
                            this.bindings.put(key, Binding.UNRESOLVED);
                        } catch (UnsupportedOperationException e3) {
                            addError("Unsupported: " + e3.getMessage() + " required by " + binding.requiredBy);
                            this.bindings.put(key, Binding.UNRESOLVED);
                        } catch (RuntimeException e4) {
                            throw e4;
                        } catch (Exception e5) {
                            throw new RuntimeException(e5);
                        }
                    }
                } else {
                    this.attachSuccess = true;
                    binding.attach(this);
                    if (this.attachSuccess) {
                        binding.setLinked();
                    } else {
                        this.toLink.add(binding);
                    }
                }
            } else {
                try {
                    this.errorHandler.handleErrors(this.errors);
                    return;
                } finally {
                    this.errors.clear();
                }
            }
        }
    }

    private void assertLockHeld() {
        if (!Thread.holdsLock(this)) {
            throw new AssertionError();
        }
    }

    private Binding<?> createBinding(String key, Object requiredBy, ClassLoader classLoader, boolean mustHaveInjections) {
        String builtInBindingsKey = Keys.getBuiltInBindingsKey(key);
        if (builtInBindingsKey != null) {
            return new BuiltInBinding(key, requiredBy, classLoader, builtInBindingsKey);
        }
        String lazyKey = Keys.getLazyKey(key);
        if (lazyKey != null) {
            return new LazyBinding(key, requiredBy, classLoader, lazyKey);
        }
        String className = Keys.getClassName(key);
        if (className == null || Keys.isAnnotated(key)) {
            throw new IllegalArgumentException(key);
        }
        Binding<?> binding = this.plugin.getAtInjectBinding(key, className, classLoader, mustHaveInjections);
        if (binding != null) {
            return binding;
        }
        throw new Binding.InvalidBindingException(className, "could not be bound with key " + key);
    }

    @Deprecated
    public Binding<?> requestBinding(String key, Object requiredBy) {
        return requestBinding(key, requiredBy, getClass().getClassLoader(), true, true);
    }

    public Binding<?> requestBinding(String key, Object requiredBy, ClassLoader classLoader) {
        return requestBinding(key, requiredBy, classLoader, true, true);
    }

    @Deprecated
    public Binding<?> requestBinding(String key, Object requiredBy, boolean mustHaveInjections, boolean library) {
        return requestBinding(key, requiredBy, getClass().getClassLoader(), mustHaveInjections, library);
    }

    public Binding<?> requestBinding(String key, Object requiredBy, ClassLoader classLoader, boolean mustHaveInjections, boolean library) {
        assertLockHeld();
        Binding<?> binding = null;
        Linker linker = this;
        while (true) {
            if (linker == null) {
                break;
            }
            Binding<?> binding2 = linker.bindings.get(key);
            binding = binding2;
            if (binding == null) {
                linker = linker.base;
            } else if (linker != this && !binding.isLinked()) {
                throw new AssertionError();
            }
        }
        if (binding == null) {
            Binding<?> deferredBinding = new DeferredBinding(key, classLoader, requiredBy, mustHaveInjections);
            deferredBinding.setLibrary(library);
            deferredBinding.setDependedOn(true);
            this.toLink.add(deferredBinding);
            this.attachSuccess = false;
            return null;
        }
        if (!binding.isLinked()) {
            this.toLink.add(binding);
        }
        binding.setLibrary(library);
        binding.setDependedOn(true);
        return binding;
    }

    private <T> void putBinding(Binding<T> binding) {
        if (binding.provideKey != null) {
            putIfAbsent(this.bindings, binding.provideKey, binding);
        }
        if (binding.membersKey != null) {
            putIfAbsent(this.bindings, binding.membersKey, binding);
        }
    }

    static <T> Binding<T> scope(Binding<T> binding) {
        return (!binding.isSingleton() || (binding instanceof SingletonBinding)) ? binding : new SingletonBinding<>(binding);
    }

    private <K, V> void putIfAbsent(Map<K, V> map, K key, V value) {
        V replaced = map.put(key, value);
        if (replaced != null) {
            map.put(key, replaced);
        }
    }

    private void addError(String message) {
        this.errors.add(message);
    }

    private static class SingletonBinding<T> extends Binding<T> {
        private final Binding<T> binding;
        private volatile Object onlyInstance;

        private SingletonBinding(Binding<T> binding) {
            super(binding.provideKey, binding.membersKey, true, binding.requiredBy);
            this.onlyInstance = Linker.UNINITIALIZED;
            this.binding = binding;
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.binding.attach(linker);
        }

        @Override // dagger.internal.Binding, dagger.MembersInjector
        public void injectMembers(T t) {
            this.binding.injectMembers(t);
        }

        @Override // dagger.internal.Binding, javax.inject.Provider
        public T get() {
            if (this.onlyInstance == Linker.UNINITIALIZED) {
                synchronized (this) {
                    if (this.onlyInstance == Linker.UNINITIALIZED) {
                        this.onlyInstance = this.binding.get();
                    }
                }
            }
            return (T) this.onlyInstance;
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> get, Set<Binding<?>> injectMembers) {
            this.binding.getDependencies(get, injectMembers);
        }

        @Override // dagger.internal.Binding
        public boolean isCycleFree() {
            return this.binding.isCycleFree();
        }

        @Override // dagger.internal.Binding
        public boolean isLinked() {
            return this.binding.isLinked();
        }

        @Override // dagger.internal.Binding
        public boolean isVisiting() {
            return this.binding.isVisiting();
        }

        @Override // dagger.internal.Binding
        public boolean library() {
            return this.binding.library();
        }

        @Override // dagger.internal.Binding
        public boolean dependedOn() {
            return this.binding.dependedOn();
        }

        @Override // dagger.internal.Binding
        public void setCycleFree(boolean cycleFree) {
            this.binding.setCycleFree(cycleFree);
        }

        @Override // dagger.internal.Binding
        public void setVisiting(boolean visiting) {
            this.binding.setVisiting(visiting);
        }

        @Override // dagger.internal.Binding
        public void setLibrary(boolean library) {
            this.binding.setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void setDependedOn(boolean dependedOn) {
            this.binding.setDependedOn(dependedOn);
        }

        @Override // dagger.internal.Binding
        protected boolean isSingleton() {
            return true;
        }

        @Override // dagger.internal.Binding
        protected void setLinked() {
            this.binding.setLinked();
        }

        @Override // dagger.internal.Binding
        public String toString() {
            return "@Singleton/" + this.binding.toString();
        }
    }

    private static class DeferredBinding extends Binding<Object> {
        final ClassLoader classLoader;
        final String deferredKey;
        final boolean mustHaveInjections;

        private DeferredBinding(String deferredKey, ClassLoader classLoader, Object requiredBy, boolean mustHaveInjections) {
            super(null, null, false, requiredBy);
            this.deferredKey = deferredKey;
            this.classLoader = classLoader;
            this.mustHaveInjections = mustHaveInjections;
        }

        @Override // dagger.internal.Binding, dagger.MembersInjector
        public void injectMembers(Object t) {
            throw new UnsupportedOperationException("Deferred bindings must resolve first.");
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> get, Set<Binding<?>> injectMembers) {
            throw new UnsupportedOperationException("Deferred bindings must resolve first.");
        }

        @Override // dagger.internal.Binding
        public String toString() {
            return "DeferredBinding[deferredKey=" + this.deferredKey + "]";
        }
    }
}
