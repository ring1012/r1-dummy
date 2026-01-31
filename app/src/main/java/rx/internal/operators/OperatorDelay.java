package rx.internal.operators;

import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Action0;

/* loaded from: classes.dex */
public final class OperatorDelay<T> implements Observable.Operator<T, T> {
    final long delay;
    final Scheduler scheduler;
    final TimeUnit unit;

    public OperatorDelay(long delay, TimeUnit unit, Scheduler scheduler) {
        this.delay = delay;
        this.unit = unit;
        this.scheduler = scheduler;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(Subscriber<? super T> child) {
        Scheduler.Worker worker = this.scheduler.createWorker();
        child.add(worker);
        return new AnonymousClass1(child, worker, child);
    }

    /* renamed from: rx.internal.operators.OperatorDelay$1, reason: invalid class name */
    class AnonymousClass1 extends Subscriber<T> {
        boolean done;
        final /* synthetic */ Subscriber val$child;
        final /* synthetic */ Scheduler.Worker val$worker;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(Subscriber subscriber, Scheduler.Worker worker, Subscriber subscriber2) {
            super(subscriber);
            this.val$worker = worker;
            this.val$child = subscriber2;
        }

        @Override // rx.Observer
        public void onCompleted() {
            this.val$worker.schedule(new Action0() { // from class: rx.internal.operators.OperatorDelay.1.1
                @Override // rx.functions.Action0
                public void call() {
                    if (!AnonymousClass1.this.done) {
                        AnonymousClass1.this.done = true;
                        AnonymousClass1.this.val$child.onCompleted();
                    }
                }
            }, OperatorDelay.this.delay, OperatorDelay.this.unit);
        }

        @Override // rx.Observer
        public void onError(final Throwable e) {
            this.val$worker.schedule(new Action0() { // from class: rx.internal.operators.OperatorDelay.1.2
                @Override // rx.functions.Action0
                public void call() {
                    if (!AnonymousClass1.this.done) {
                        AnonymousClass1.this.done = true;
                        AnonymousClass1.this.val$child.onError(e);
                        AnonymousClass1.this.val$worker.unsubscribe();
                    }
                }
            });
        }

        @Override // rx.Observer
        public void onNext(final T t) {
            this.val$worker.schedule(new Action0() { // from class: rx.internal.operators.OperatorDelay.1.3
                /* JADX WARN: Multi-variable type inference failed */
                @Override // rx.functions.Action0
                public void call() {
                    if (!AnonymousClass1.this.done) {
                        AnonymousClass1.this.val$child.onNext(t);
                    }
                }
            }, OperatorDelay.this.delay, OperatorDelay.this.unit);
        }
    }
}
