package rx.android.schedulers;

import android.os.Handler;
import android.os.Looper;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;

/* loaded from: classes.dex */
public final class AndroidSchedulers {
    private AndroidSchedulers() {
        throw new AssertionError("No instances");
    }

    private static class MainThreadSchedulerHolder {
        static final Scheduler MAIN_THREAD_SCHEDULER = new HandlerScheduler(new Handler(Looper.getMainLooper()));

        private MainThreadSchedulerHolder() {
        }
    }

    public static Scheduler mainThread() {
        Scheduler scheduler = RxAndroidPlugins.getInstance().getSchedulersHook().getMainThreadScheduler();
        return scheduler != null ? scheduler : MainThreadSchedulerHolder.MAIN_THREAD_SCHEDULER;
    }
}
