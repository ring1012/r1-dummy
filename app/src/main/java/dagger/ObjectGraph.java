package dagger;

import dagger.internal.Binding;
import dagger.internal.BindingsGroup;
import dagger.internal.FailoverLoader;
import dagger.internal.Keys;
import dagger.internal.Linker;
import dagger.internal.Loader;
import dagger.internal.ModuleAdapter;
import dagger.internal.Modules;
import dagger.internal.ProblemDetector;
import dagger.internal.SetBinding;
import dagger.internal.StaticInjection;
import dagger.internal.ThrowingErrorHandler;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public abstract class ObjectGraph {
    public abstract <T> T get(Class<T> cls);

    public abstract <T> T inject(T t);

    public abstract void injectStatics();

    public abstract ObjectGraph plus(Object... objArr);

    public abstract void validate();

    ObjectGraph() {
    }

    public static ObjectGraph create(Object... modules) {
        return DaggerObjectGraph.makeGraph(null, new FailoverLoader(), modules);
    }

    static ObjectGraph createWith(Loader loader, Object... modules) {
        return DaggerObjectGraph.makeGraph(null, loader, modules);
    }

    static class DaggerObjectGraph extends ObjectGraph {
        private final DaggerObjectGraph base;
        private final Map<String, Class<?>> injectableTypes;
        private final Linker linker;
        private final Loader plugin;
        private final List<SetBinding<?>> setBindings;
        private final Map<Class<?>, StaticInjection> staticInjections;

        DaggerObjectGraph(DaggerObjectGraph base, Linker linker, Loader plugin, Map<Class<?>, StaticInjection> staticInjections, Map<String, Class<?>> injectableTypes, List<SetBinding<?>> setBindings) {
            this.base = base;
            this.linker = (Linker) checkNotNull(linker, "linker");
            this.plugin = (Loader) checkNotNull(plugin, "plugin");
            this.staticInjections = (Map) checkNotNull(staticInjections, "staticInjections");
            this.injectableTypes = (Map) checkNotNull(injectableTypes, "injectableTypes");
            this.setBindings = (List) checkNotNull(setBindings, "setBindings");
        }

        private static <T> T checkNotNull(T object, String label) {
            if (object == null) {
                throw new NullPointerException(label);
            }
            return object;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static ObjectGraph makeGraph(DaggerObjectGraph base, Loader plugin, Object... modules) {
            Map<String, Class<?>> injectableTypes = new LinkedHashMap<>();
            Map<Class<?>, StaticInjection> staticInjections = new LinkedHashMap<>();
            StandardBindings baseBindings = base == null ? new StandardBindings() : new StandardBindings(base.setBindings);
            BindingsGroup overrideBindings = new OverridesBindings();
            Map<ModuleAdapter<?>, Object> loadedModules = Modules.loadModules(plugin, modules);
            for (Map.Entry<ModuleAdapter<?>, Object> loadedModule : loadedModules.entrySet()) {
                ModuleAdapter<Object> moduleAdapter = loadedModule.getKey();
                for (int i = 0; i < moduleAdapter.injectableTypes.length; i++) {
                    injectableTypes.put(moduleAdapter.injectableTypes[i], moduleAdapter.moduleClass);
                }
                for (int i2 = 0; i2 < moduleAdapter.staticInjections.length; i2++) {
                    staticInjections.put(moduleAdapter.staticInjections[i2], null);
                }
                try {
                    BindingsGroup addTo = moduleAdapter.overrides ? overrideBindings : baseBindings;
                    moduleAdapter.getBindings(addTo, loadedModule.getValue());
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException(moduleAdapter.moduleClass.getSimpleName() + ": " + e.getMessage(), e);
                }
            }
            Linker linker = new Linker(base != null ? base.linker : null, plugin, new ThrowingErrorHandler());
            linker.installBindings(baseBindings);
            linker.installBindings(overrideBindings);
            return new DaggerObjectGraph(base, linker, plugin, staticInjections, injectableTypes, baseBindings.setBindings);
        }

        @Override // dagger.ObjectGraph
        public ObjectGraph plus(Object... modules) {
            linkEverything();
            return makeGraph(this, this.plugin, modules);
        }

        private void linkStaticInjections() {
            for (Map.Entry<Class<?>, StaticInjection> entry : this.staticInjections.entrySet()) {
                StaticInjection staticInjection = entry.getValue();
                if (staticInjection == null) {
                    staticInjection = this.plugin.getStaticInjection(entry.getKey());
                    entry.setValue(staticInjection);
                }
                staticInjection.attach(this.linker);
            }
        }

        private void linkInjectableTypes() {
            for (Map.Entry<String, Class<?>> entry : this.injectableTypes.entrySet()) {
                this.linker.requestBinding(entry.getKey(), entry.getValue(), entry.getValue().getClassLoader(), false, true);
            }
        }

        @Override // dagger.ObjectGraph
        public void validate() {
            Map<String, Binding<?>> allBindings = linkEverything();
            new ProblemDetector().detectProblems(allBindings.values());
        }

        private Map<String, Binding<?>> linkEverything() {
            Map<String, Binding<?>> mapLinkAll;
            Map<String, Binding<?>> bindings = this.linker.fullyLinkedBindings();
            if (bindings != null) {
                return bindings;
            }
            synchronized (this.linker) {
                Map<String, Binding<?>> bindings2 = this.linker.fullyLinkedBindings();
                if (bindings2 != null) {
                    mapLinkAll = bindings2;
                } else {
                    linkStaticInjections();
                    linkInjectableTypes();
                    mapLinkAll = this.linker.linkAll();
                }
            }
            return mapLinkAll;
        }

        @Override // dagger.ObjectGraph
        public void injectStatics() {
            synchronized (this.linker) {
                linkStaticInjections();
                this.linker.linkRequested();
                linkStaticInjections();
            }
            for (Map.Entry<Class<?>, StaticInjection> entry : this.staticInjections.entrySet()) {
                entry.getValue().inject();
            }
        }

        @Override // dagger.ObjectGraph
        public <T> T get(Class<T> cls) {
            String str = Keys.get(cls);
            return (T) getInjectableTypeBinding(cls.getClassLoader(), cls.isInterface() ? str : Keys.getMembersKey(cls), str).get();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // dagger.ObjectGraph
        public <T> T inject(T t) {
            String membersKey = Keys.getMembersKey(t.getClass());
            ClassLoader classLoader = t.getClass().getClassLoader();
            getInjectableTypeBinding(classLoader, membersKey, membersKey).injectMembers(t);
            return t;
        }

        private Binding<?> getInjectableTypeBinding(ClassLoader classLoader, String injectableKey, String key) {
            Binding<?> binding;
            Class<?> moduleClass = null;
            for (DaggerObjectGraph graph = this; graph != null; graph = graph.base) {
                Class<?> moduleClass2 = graph.injectableTypes.get(injectableKey);
                moduleClass = moduleClass2;
                if (moduleClass != null) {
                    break;
                }
            }
            if (moduleClass == null) {
                throw new IllegalArgumentException("No inject registered for " + injectableKey + ". You must explicitly add it to the 'injects' option in one of your modules.");
            }
            synchronized (this.linker) {
                binding = this.linker.requestBinding(key, moduleClass, classLoader, false, true);
                if (binding == null || !binding.isLinked()) {
                    this.linker.linkRequested();
                    binding = this.linker.requestBinding(key, moduleClass, classLoader, false, true);
                }
            }
            return binding;
        }
    }

    private static final class StandardBindings extends BindingsGroup {
        private final List<SetBinding<?>> setBindings;

        public StandardBindings() {
            this.setBindings = new ArrayList();
        }

        public StandardBindings(List<SetBinding<?>> baseSetBindings) {
            this.setBindings = new ArrayList(baseSetBindings.size());
            for (SetBinding<?> sb : baseSetBindings) {
                SetBinding<?> child = new SetBinding<>(sb);
                this.setBindings.add(child);
                put(child.provideKey, child);
            }
        }

        @Override // dagger.internal.BindingsGroup
        public Binding<?> contributeSetBinding(String key, SetBinding<?> value) {
            this.setBindings.add(value);
            return super.put(key, value);
        }
    }

    private static final class OverridesBindings extends BindingsGroup {
        OverridesBindings() {
        }

        @Override // dagger.internal.BindingsGroup
        public Binding<?> contributeSetBinding(String key, SetBinding<?> value) {
            throw new IllegalArgumentException("Module overrides cannot contribute set bindings.");
        }
    }
}
