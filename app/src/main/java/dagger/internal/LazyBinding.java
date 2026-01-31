package dagger.internal;

import dagger.Lazy;

/* loaded from: classes.dex */
final class LazyBinding<T> extends Binding<Lazy<T>> {
    private static final Object NOT_PRESENT = new Object();
    private Binding<T> delegate;
    private final String lazyKey;
    private final ClassLoader loader;

    public LazyBinding(String key, Object requiredBy, ClassLoader loader, String lazyKey) {
        super(key, null, false, requiredBy);
        this.loader = loader;
        this.lazyKey = lazyKey;
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.delegate = (Binding<T>) linker.requestBinding(this.lazyKey, this.requiredBy, this.loader);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(Lazy<T> t) {
        throw new UnsupportedOperationException();
    }

    @Override // dagger.internal.Binding, javax.inject.Provider
    public Lazy<T> get() {
        return new Lazy<T>() { // from class: dagger.internal.LazyBinding.1
            private volatile Object cacheValue = LazyBinding.NOT_PRESENT;

            @Override // dagger.Lazy
            public T get() {
                if (this.cacheValue == LazyBinding.NOT_PRESENT) {
                    synchronized (this) {
                        if (this.cacheValue == LazyBinding.NOT_PRESENT) {
                            this.cacheValue = LazyBinding.this.delegate.get();
                        }
                    }
                }
                return (T) this.cacheValue;
            }
        };
    }
}
