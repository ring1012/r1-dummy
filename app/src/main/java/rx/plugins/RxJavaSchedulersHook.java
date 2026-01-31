package rx.plugins;

import rx.Scheduler;
import rx.functions.Action0;

/* loaded from: classes.dex */
public class RxJavaSchedulersHook {
    private static final RxJavaSchedulersHook DEFAULT_INSTANCE = new RxJavaSchedulersHook();

    protected RxJavaSchedulersHook() {
    }

    public Scheduler getComputationScheduler() {
        return null;
    }

    public Scheduler getIOScheduler() {
        return null;
    }

    public Scheduler getNewThreadScheduler() {
        return null;
    }

    public Action0 onSchedule(Action0 action) {
        return action;
    }

    public static RxJavaSchedulersHook getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }
}
