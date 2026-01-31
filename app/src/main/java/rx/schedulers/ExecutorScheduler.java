package rx.schedulers;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import rx.internal.schedulers.GenericScheduledExecutorService;
import rx.internal.schedulers.ScheduledAction;
import rx.plugins.RxJavaPlugins;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.MultipleAssignmentSubscription;
import rx.subscriptions.Subscriptions;

/* loaded from: classes.dex */
final class ExecutorScheduler extends Scheduler {
    final Executor executor;

    public ExecutorScheduler(Executor executor) {
        this.executor = executor;
    }

    @Override // rx.Scheduler
    public Scheduler.Worker createWorker() {
        return new ExecutorSchedulerWorker(this.executor);
    }

    static final class ExecutorSchedulerWorker extends Scheduler.Worker implements Runnable {
        final Executor executor;
        final ConcurrentLinkedQueue<ScheduledAction> queue = new ConcurrentLinkedQueue<>();
        final AtomicInteger wip = new AtomicInteger();
        final CompositeSubscription tasks = new CompositeSubscription();

        public ExecutorSchedulerWorker(Executor executor) {
            this.executor = executor;
        }

        @Override // rx.Scheduler.Worker
        public Subscription schedule(Action0 action) {
            if (isUnsubscribed()) {
                return Subscriptions.unsubscribed();
            }
            ScheduledAction ea = new ScheduledAction(action, this.tasks);
            this.tasks.add(ea);
            this.queue.offer(ea);
            if (this.wip.getAndIncrement() == 0) {
                try {
                    this.executor.execute(this);
                    return ea;
                } catch (RejectedExecutionException t) {
                    this.tasks.remove(ea);
                    this.wip.decrementAndGet();
                    RxJavaPlugins.getInstance().getErrorHandler().handleError(t);
                    throw t;
                }
            }
            return ea;
        }

        @Override // java.lang.Runnable
        public void run() throws Throwable {
            do {
                ScheduledAction sa = this.queue.poll();
                if (!sa.isUnsubscribed()) {
                    sa.run();
                }
            } while (this.wip.decrementAndGet() > 0);
        }

        @Override // rx.Scheduler.Worker
        public Subscription schedule(final Action0 action, long delayTime, TimeUnit unit) {
            ScheduledExecutorService service;
            if (delayTime <= 0) {
                return schedule(action);
            }
            if (isUnsubscribed()) {
                return Subscriptions.unsubscribed();
            }
            if (this.executor instanceof ScheduledExecutorService) {
                service = (ScheduledExecutorService) this.executor;
            } else {
                service = GenericScheduledExecutorService.getInstance();
            }
            MultipleAssignmentSubscription first = new MultipleAssignmentSubscription();
            final MultipleAssignmentSubscription mas = new MultipleAssignmentSubscription();
            mas.set(first);
            this.tasks.add(mas);
            final Subscription removeMas = Subscriptions.create(new Action0() { // from class: rx.schedulers.ExecutorScheduler.ExecutorSchedulerWorker.1
                @Override // rx.functions.Action0
                public void call() {
                    ExecutorSchedulerWorker.this.tasks.remove(mas);
                }
            });
            ScheduledAction ea = new ScheduledAction(new Action0() { // from class: rx.schedulers.ExecutorScheduler.ExecutorSchedulerWorker.2
                @Override // rx.functions.Action0
                public void call() {
                    if (!mas.isUnsubscribed()) {
                        Subscription s2 = ExecutorSchedulerWorker.this.schedule(action);
                        mas.set(s2);
                        if (s2.getClass() == ScheduledAction.class) {
                            ((ScheduledAction) s2).add(removeMas);
                        }
                    }
                }
            });
            first.set(ea);
            try {
                Future<?> f = service.schedule(ea, delayTime, unit);
                ea.add(f);
                return removeMas;
            } catch (RejectedExecutionException t) {
                RxJavaPlugins.getInstance().getErrorHandler().handleError(t);
                throw t;
            }
        }

        @Override // rx.Subscription
        public boolean isUnsubscribed() {
            return this.tasks.isUnsubscribed();
        }

        @Override // rx.Subscription
        public void unsubscribe() throws Throwable {
            this.tasks.unsubscribe();
        }
    }
}
