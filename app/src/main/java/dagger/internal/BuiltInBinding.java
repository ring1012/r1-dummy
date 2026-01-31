package dagger.internal;

/* loaded from: classes.dex */
final class BuiltInBinding<T> extends Binding<T> {
    private final ClassLoader classLoader;
    private Binding<?> delegate;
    private final String delegateKey;

    public BuiltInBinding(String key, Object requiredBy, ClassLoader classLoader, String delegateKey) {
        super(key, null, false, requiredBy);
        this.classLoader = classLoader;
        this.delegateKey = delegateKey;
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.delegate = linker.requestBinding(this.delegateKey, this.requiredBy, this.classLoader);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(T t) {
        throw new UnsupportedOperationException();
    }

    @Override // dagger.internal.Binding, javax.inject.Provider
    public T get() {
        return (T) this.delegate;
    }

    public Binding<?> getDelegate() {
        return this.delegate;
    }
}
