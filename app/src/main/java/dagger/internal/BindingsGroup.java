package dagger.internal;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
public abstract class BindingsGroup {
    private final Map<String, Binding<?>> bindings = new LinkedHashMap();

    public abstract Binding<?> contributeSetBinding(String str, SetBinding<?> setBinding);

    public Binding<?> contributeProvidesBinding(String key, ProvidesBinding<?> value) {
        return put(key, value);
    }

    protected Binding<?> put(String key, Binding<?> value) {
        Binding<?> clobbered = this.bindings.put(key, value);
        if (clobbered != null) {
            this.bindings.put(key, clobbered);
            throw new IllegalArgumentException("Duplicate:\n    " + clobbered + "\n    " + value);
        }
        return null;
    }

    public Binding<?> get(String key) {
        return this.bindings.get(key);
    }

    public final Set<Map.Entry<String, Binding<?>>> entrySet() {
        return this.bindings.entrySet();
    }

    public String toString() {
        return getClass().getSimpleName() + this.bindings.toString();
    }
}
