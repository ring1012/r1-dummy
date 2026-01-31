package dagger.internal;

import dagger.MembersInjector;
import java.util.Set;
import javax.inject.Provider;

/* loaded from: classes.dex */
public abstract class Binding<T> implements Provider<T>, MembersInjector<T> {
    private static final int CYCLE_FREE = 8;
    private static final int DEPENDED_ON = 16;
    protected static final boolean IS_SINGLETON = true;
    private static final int LIBRARY = 32;
    private static final int LINKED = 2;
    protected static final boolean NOT_SINGLETON = false;
    private static final int SINGLETON = 1;
    public static final Binding<Object> UNRESOLVED;
    private static final int VISITING = 4;
    private int bits;
    public final String membersKey;
    public final String provideKey;
    public final Object requiredBy;

    static {
        String str = null;
        UNRESOLVED = new Binding<Object>(str, str, false, str) { // from class: dagger.internal.Binding.1
            @Override // dagger.internal.Binding, javax.inject.Provider
            public Object get() {
                throw new AssertionError("Unresolved binding should never be called to inject.");
            }

            @Override // dagger.internal.Binding, dagger.MembersInjector
            public void injectMembers(Object t) {
                throw new AssertionError("Unresolved binding should never be called to inject.");
            }
        };
    }

    protected Binding(String provideKey, String membersKey, boolean singleton, Object requiredBy) {
        if (singleton && provideKey == null) {
            throw new InvalidBindingException(Keys.getClassName(membersKey), "is exclusively members injected and therefore cannot be scoped");
        }
        this.provideKey = provideKey;
        this.membersKey = membersKey;
        this.requiredBy = requiredBy;
        this.bits = singleton ? 1 : 0;
    }

    public void attach(Linker linker) {
    }

    @Override // dagger.MembersInjector
    public void injectMembers(T t) {
    }

    @Override // javax.inject.Provider
    public T get() {
        throw new UnsupportedOperationException("No injectable constructor on " + getClass().getName());
    }

    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
    }

    void setLinked() {
        this.bits |= 2;
    }

    public boolean isLinked() {
        return (this.bits & 2) != 0;
    }

    boolean isSingleton() {
        return (this.bits & 1) != 0;
    }

    public boolean isVisiting() {
        return (this.bits & 4) != 0;
    }

    public void setVisiting(boolean visiting) {
        this.bits = visiting ? this.bits | 4 : this.bits & (-5);
    }

    public boolean isCycleFree() {
        return (this.bits & 8) != 0;
    }

    public void setCycleFree(boolean cycleFree) {
        this.bits = cycleFree ? this.bits | 8 : this.bits & (-9);
    }

    public void setLibrary(boolean library) {
        this.bits = library ? this.bits | 32 : this.bits & (-33);
    }

    public boolean library() {
        return (this.bits & 32) != 0;
    }

    public void setDependedOn(boolean dependedOn) {
        this.bits = dependedOn ? this.bits | 16 : this.bits & (-17);
    }

    public boolean dependedOn() {
        return (this.bits & 16) != 0;
    }

    public String toString() {
        return getClass().getSimpleName() + "[provideKey=\"" + this.provideKey + "\", memberskey=\"" + this.membersKey + "\"]";
    }

    public static class InvalidBindingException extends RuntimeException {
        public final String type;

        public InvalidBindingException(String type, String error) {
            super(error);
            this.type = type;
        }

        public InvalidBindingException(String type, String error, Throwable cause) {
            super("Binding for " + type + " was invalid: " + error, cause);
            this.type = type;
        }
    }
}
