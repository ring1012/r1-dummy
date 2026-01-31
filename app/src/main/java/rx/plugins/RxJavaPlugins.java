package rx.plugins;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

/* loaded from: classes.dex */
public class RxJavaPlugins {
    private final AtomicReference<RxJavaErrorHandler> errorHandler = new AtomicReference<>();
    private final AtomicReference<RxJavaObservableExecutionHook> observableExecutionHook = new AtomicReference<>();
    private final AtomicReference<RxJavaSchedulersHook> schedulersHook = new AtomicReference<>();
    private static final RxJavaPlugins INSTANCE = new RxJavaPlugins();
    static final RxJavaErrorHandler DEFAULT_ERROR_HANDLER = new RxJavaErrorHandler() { // from class: rx.plugins.RxJavaPlugins.1
    };

    public static RxJavaPlugins getInstance() {
        return INSTANCE;
    }

    RxJavaPlugins() {
    }

    void reset() {
        INSTANCE.errorHandler.set(null);
        INSTANCE.observableExecutionHook.set(null);
        INSTANCE.schedulersHook.set(null);
    }

    public RxJavaErrorHandler getErrorHandler() throws ClassNotFoundException {
        if (this.errorHandler.get() == null) {
            Object impl = getPluginImplementationViaProperty(RxJavaErrorHandler.class, System.getProperties());
            if (impl == null) {
                this.errorHandler.compareAndSet(null, DEFAULT_ERROR_HANDLER);
            } else {
                this.errorHandler.compareAndSet(null, (RxJavaErrorHandler) impl);
            }
        }
        return this.errorHandler.get();
    }

    public void registerErrorHandler(RxJavaErrorHandler impl) {
        if (!this.errorHandler.compareAndSet(null, impl)) {
            throw new IllegalStateException("Another strategy was already registered: " + this.errorHandler.get());
        }
    }

    public RxJavaObservableExecutionHook getObservableExecutionHook() throws ClassNotFoundException {
        if (this.observableExecutionHook.get() == null) {
            Object impl = getPluginImplementationViaProperty(RxJavaObservableExecutionHook.class, System.getProperties());
            if (impl == null) {
                this.observableExecutionHook.compareAndSet(null, RxJavaObservableExecutionHookDefault.getInstance());
            } else {
                this.observableExecutionHook.compareAndSet(null, (RxJavaObservableExecutionHook) impl);
            }
        }
        return this.observableExecutionHook.get();
    }

    public void registerObservableExecutionHook(RxJavaObservableExecutionHook impl) {
        if (!this.observableExecutionHook.compareAndSet(null, impl)) {
            throw new IllegalStateException("Another strategy was already registered: " + this.observableExecutionHook.get());
        }
    }

    static Object getPluginImplementationViaProperty(Class<?> pluginClass, Properties props) throws ClassNotFoundException {
        String classSimpleName = pluginClass.getSimpleName();
        String defaultKey = "rxjava.plugin." + classSimpleName + ".implementation";
        String implementingClass = props.getProperty(defaultKey);
        if (implementingClass == null) {
            Iterator i$ = props.entrySet().iterator();
            while (true) {
                if (!i$.hasNext()) {
                    break;
                }
                Map.Entry<Object, Object> e = (Map.Entry) i$.next();
                String key = e.getKey().toString();
                if (key.startsWith("rxjava.plugin.") && key.endsWith(".class")) {
                    String value = e.getValue().toString();
                    if (classSimpleName.equals(value)) {
                        String index = key.substring(0, key.length() - ".class".length()).substring("rxjava.plugin.".length());
                        String implKey = "rxjava.plugin." + index + ".impl";
                        implementingClass = props.getProperty(implKey);
                        if (implementingClass == null) {
                            throw new RuntimeException("Implementing class declaration for " + classSimpleName + " missing: " + implKey);
                        }
                    }
                }
            }
        }
        if (implementingClass != null) {
            try {
                Class<?> cls = Class.forName(implementingClass);
                return cls.asSubclass(pluginClass).newInstance();
            } catch (ClassCastException e2) {
                throw new RuntimeException(classSimpleName + " implementation is not an instance of " + classSimpleName + ": " + implementingClass);
            } catch (ClassNotFoundException e3) {
                throw new RuntimeException(classSimpleName + " implementation class not found: " + implementingClass, e3);
            } catch (IllegalAccessException e4) {
                throw new RuntimeException(classSimpleName + " implementation not able to be accessed: " + implementingClass, e4);
            } catch (InstantiationException e5) {
                throw new RuntimeException(classSimpleName + " implementation not able to be instantiated: " + implementingClass, e5);
            }
        }
        return null;
    }

    public RxJavaSchedulersHook getSchedulersHook() throws ClassNotFoundException {
        if (this.schedulersHook.get() == null) {
            Object impl = getPluginImplementationViaProperty(RxJavaSchedulersHook.class, System.getProperties());
            if (impl == null) {
                this.schedulersHook.compareAndSet(null, RxJavaSchedulersHook.getDefaultInstance());
            } else {
                this.schedulersHook.compareAndSet(null, (RxJavaSchedulersHook) impl);
            }
        }
        return this.schedulersHook.get();
    }

    public void registerSchedulersHook(RxJavaSchedulersHook impl) {
        if (!this.schedulersHook.compareAndSet(null, impl)) {
            throw new IllegalStateException("Another strategy was already registered: " + this.schedulersHook.get());
        }
    }
}
