package rx.android.plugins;

import java.util.concurrent.atomic.AtomicReference;
import rx.annotations.Beta;

/* loaded from: classes.dex */
public final class RxAndroidPlugins {
    private static final RxAndroidPlugins INSTANCE = new RxAndroidPlugins();
    private final AtomicReference<RxAndroidSchedulersHook> schedulersHook = new AtomicReference<>();

    public static RxAndroidPlugins getInstance() {
        return INSTANCE;
    }

    RxAndroidPlugins() {
    }

    @Beta
    public void reset() {
        this.schedulersHook.set(null);
    }

    public RxAndroidSchedulersHook getSchedulersHook() {
        if (this.schedulersHook.get() == null) {
            this.schedulersHook.compareAndSet(null, RxAndroidSchedulersHook.getDefaultInstance());
        }
        return this.schedulersHook.get();
    }

    public void registerSchedulersHook(RxAndroidSchedulersHook impl) {
        if (!this.schedulersHook.compareAndSet(null, impl)) {
            throw new IllegalStateException("Another strategy was already registered: " + this.schedulersHook.get());
        }
    }
}
