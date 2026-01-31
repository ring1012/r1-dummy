package dagger.internal;

import java.util.LinkedHashMap;
import java.util.Map;

/* loaded from: classes.dex */
public final class Modules {
    private Modules() {
    }

    public static Map<ModuleAdapter<?>, Object> loadModules(Loader loader, Object[] seedModulesOrClasses) {
        Map<ModuleAdapter<?>, Object> seedAdapters = new LinkedHashMap<>(seedModulesOrClasses.length);
        for (int i = 0; i < seedModulesOrClasses.length; i++) {
            if (seedModulesOrClasses[i] instanceof Class) {
                ModuleAdapter<?> adapter = loader.getModuleAdapter((Class) seedModulesOrClasses[i]);
                seedAdapters.put(adapter, adapter.newModule());
            } else {
                ModuleAdapter<?> adapter2 = loader.getModuleAdapter(seedModulesOrClasses[i].getClass());
                seedAdapters.put(adapter2, seedModulesOrClasses[i]);
            }
        }
        Map<ModuleAdapter<?>, Object> result = new LinkedHashMap<>(seedAdapters);
        Map<Class<?>, ModuleAdapter<?>> transitiveInclusions = new LinkedHashMap<>();
        for (ModuleAdapter<?> adapter3 : seedAdapters.keySet()) {
            collectIncludedModulesRecursively(loader, adapter3, transitiveInclusions);
        }
        for (ModuleAdapter<?> dependency : transitiveInclusions.values()) {
            if (!result.containsKey(dependency)) {
                result.put(dependency, dependency.newModule());
            }
        }
        return result;
    }

    private static void collectIncludedModulesRecursively(Loader plugin, ModuleAdapter<?> adapter, Map<Class<?>, ModuleAdapter<?>> result) {
        Class<?>[] arr$ = adapter.includes;
        for (Class<?> include : arr$) {
            if (!result.containsKey(include)) {
                ModuleAdapter<?> includedModuleAdapter = plugin.getModuleAdapter(include);
                result.put(include, includedModuleAdapter);
                collectIncludedModulesRecursively(plugin, includedModuleAdapter, result);
            }
        }
    }
}
