package rx.internal.schedulers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import rx.Scheduler;
import rx.Subscription;
import rx.exceptions.Exceptions;
import rx.functions.Action0;
import rx.internal.util.PlatformDependent;
import rx.internal.util.RxThreadFactory;
import rx.internal.util.SubscriptionList;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

/* loaded from: classes.dex */
public class NewThreadWorker extends Scheduler.Worker implements Subscription {
    private static final String PURGE_FORCE_KEY = "rx.scheduler.jdk6.purge-force";
    private static final String PURGE_THREAD_PREFIX = "RxSchedulerPurge-";
    private static final Object SET_REMOVE_ON_CANCEL_POLICY_METHOD_NOT_SUPPORTED;
    private static final boolean SHOULD_TRY_ENABLE_CANCEL_POLICY;
    private static volatile Object cachedSetRemoveOnCancelPolicyMethod;
    private final ScheduledExecutorService executor;
    volatile boolean isUnsubscribed;
    private final RxJavaSchedulersHook schedulersHook;
    private static final ConcurrentHashMap<ScheduledThreadPoolExecutor, ScheduledThreadPoolExecutor> EXECUTORS = new ConcurrentHashMap<>();
    private static final AtomicReference<ScheduledExecutorService> PURGE = new AtomicReference<>();
    private static final String FREQUENCY_KEY = "rx.scheduler.jdk6.purge-frequency-millis";
    public static final int PURGE_FREQUENCY = Integer.getInteger(FREQUENCY_KEY, 1000).intValue();

    static {
        boolean purgeForce = Boolean.getBoolean(PURGE_FORCE_KEY);
        int androidApiVersion = PlatformDependent.getAndroidApiVersion();
        SHOULD_TRY_ENABLE_CANCEL_POLICY = !purgeForce && (androidApiVersion == 0 || androidApiVersion >= 21);
        SET_REMOVE_ON_CANCEL_POLICY_METHOD_NOT_SUPPORTED = new Object();
    }

    public static void registerExecutor(ScheduledThreadPoolExecutor service) {
        while (true) {
            if (PURGE.get() != null) {
                break;
            }
            ScheduledExecutorService exec = Executors.newScheduledThreadPool(1, new RxThreadFactory(PURGE_THREAD_PREFIX));
            if (PURGE.compareAndSet(null, exec)) {
                exec.scheduleAtFixedRate(new Runnable() { // from class: rx.internal.schedulers.NewThreadWorker.1
                    @Override // java.lang.Runnable
                    public void run() {
                        NewThreadWorker.purgeExecutors();
                    }
                }, PURGE_FREQUENCY, PURGE_FREQUENCY, TimeUnit.MILLISECONDS);
                break;
            }
            exec.shutdownNow();
        }
        EXECUTORS.putIfAbsent(service, service);
    }

    public static void deregisterExecutor(ScheduledExecutorService service) {
        EXECUTORS.remove(service);
    }

    static void purgeExecutors() {
        try {
            Iterator<ScheduledThreadPoolExecutor> it = EXECUTORS.keySet().iterator();
            while (it.hasNext()) {
                ScheduledThreadPoolExecutor exec = it.next();
                if (!exec.isShutdown()) {
                    exec.purge();
                } else {
                    it.remove();
                }
            }
        } catch (Throwable t) {
            Exceptions.throwIfFatal(t);
            RxJavaPlugins.getInstance().getErrorHandler().handleError(t);
        }
    }

    public static boolean tryEnableCancelPolicy(ScheduledExecutorService executor) throws IllegalAccessException, SecurityException, IllegalArgumentException, InvocationTargetException {
        Method methodToCall;
        if (SHOULD_TRY_ENABLE_CANCEL_POLICY) {
            boolean isInstanceOfScheduledThreadPoolExecutor = executor instanceof ScheduledThreadPoolExecutor;
            if (isInstanceOfScheduledThreadPoolExecutor) {
                Object localSetRemoveOnCancelPolicyMethod = cachedSetRemoveOnCancelPolicyMethod;
                if (localSetRemoveOnCancelPolicyMethod == SET_REMOVE_ON_CANCEL_POLICY_METHOD_NOT_SUPPORTED) {
                    return false;
                }
                if (localSetRemoveOnCancelPolicyMethod == null) {
                    Method method = findSetRemoveOnCancelPolicyMethod(executor);
                    cachedSetRemoveOnCancelPolicyMethod = method != null ? method : SET_REMOVE_ON_CANCEL_POLICY_METHOD_NOT_SUPPORTED;
                    methodToCall = method;
                } else {
                    methodToCall = (Method) localSetRemoveOnCancelPolicyMethod;
                }
            } else {
                methodToCall = findSetRemoveOnCancelPolicyMethod(executor);
            }
            if (methodToCall != null) {
                try {
                    methodToCall.invoke(executor, true);
                    return true;
                } catch (Exception e) {
                    RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
                }
            }
        }
        return false;
    }

    static Method findSetRemoveOnCancelPolicyMethod(ScheduledExecutorService executor) throws SecurityException {
        Method[] arr$ = executor.getClass().getMethods();
        for (Method method : arr$) {
            if (method.getName().equals("setRemoveOnCancelPolicy")) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 1 && parameterTypes[0] == Boolean.TYPE) {
                    return method;
                }
            }
        }
        return null;
    }

    public NewThreadWorker(ThreadFactory threadFactory) throws IllegalAccessException, SecurityException, IllegalArgumentException, InvocationTargetException {
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1, threadFactory);
        boolean cancelSupported = tryEnableCancelPolicy(exec);
        if (!cancelSupported && (exec instanceof ScheduledThreadPoolExecutor)) {
            registerExecutor((ScheduledThreadPoolExecutor) exec);
        }
        this.schedulersHook = RxJavaPlugins.getInstance().getSchedulersHook();
        this.executor = exec;
    }

    @Override // rx.Scheduler.Worker
    public Subscription schedule(Action0 action) {
        return schedule(action, 0L, null);
    }

    @Override // rx.Scheduler.Worker
    public Subscription schedule(Action0 action, long delayTime, TimeUnit unit) {
        return this.isUnsubscribed ? Subscriptions.unsubscribed() : scheduleActual(action, delayTime, unit);
    }

    public ScheduledAction scheduleActual(Action0 action, long delayTime, TimeUnit unit) {
        Future<?> f;
        Action0 decoratedAction = this.schedulersHook.onSchedule(action);
        ScheduledAction run = new ScheduledAction(decoratedAction);
        if (delayTime <= 0) {
            f = this.executor.submit(run);
        } else {
            f = this.executor.schedule(run, delayTime, unit);
        }
        run.add(f);
        return run;
    }

    public ScheduledAction scheduleActual(Action0 action, long delayTime, TimeUnit unit, CompositeSubscription parent) {
        Future<?> f;
        Action0 decoratedAction = this.schedulersHook.onSchedule(action);
        ScheduledAction run = new ScheduledAction(decoratedAction, parent);
        parent.add(run);
        if (delayTime <= 0) {
            f = this.executor.submit(run);
        } else {
            f = this.executor.schedule(run, delayTime, unit);
        }
        run.add(f);
        return run;
    }

    public ScheduledAction scheduleActual(Action0 action, long delayTime, TimeUnit unit, SubscriptionList parent) {
        Future<?> f;
        Action0 decoratedAction = this.schedulersHook.onSchedule(action);
        ScheduledAction run = new ScheduledAction(decoratedAction, parent);
        parent.add(run);
        if (delayTime <= 0) {
            f = this.executor.submit(run);
        } else {
            f = this.executor.schedule(run, delayTime, unit);
        }
        run.add(f);
        return run;
    }

    @Override // rx.Subscription
    public void unsubscribe() {
        this.isUnsubscribed = true;
        this.executor.shutdownNow();
        deregisterExecutor(this.executor);
    }

    @Override // rx.Subscription
    public boolean isUnsubscribed() {
        return this.isUnsubscribed;
    }
}
