package rx.internal.operators;

import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Action0;
import rx.observers.SerializedSubscriber;
import rx.subscriptions.SerialSubscription;

/* loaded from: classes.dex */
public final class OperatorDebounceWithTime<T> implements Observable.Operator<T, T> {
    final Scheduler scheduler;
    final long timeout;
    final TimeUnit unit;

    public OperatorDebounceWithTime(long timeout, TimeUnit unit, Scheduler scheduler) {
        this.timeout = timeout;
        this.unit = unit;
        this.scheduler = scheduler;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(Subscriber<? super T> child) {
        Scheduler.Worker worker = this.scheduler.createWorker();
        SerializedSubscriber<T> s = new SerializedSubscriber<>(child);
        SerialSubscription ssub = new SerialSubscription();
        s.add(worker);
        s.add(ssub);
        return new AnonymousClass1(child, ssub, worker, s);
    }

    /* renamed from: rx.internal.operators.OperatorDebounceWithTime$1, reason: invalid class name */
    class AnonymousClass1 extends Subscriber<T> {
        final Subscriber<?> self;
        final DebounceState<T> state;
        final /* synthetic */ SerializedSubscriber val$s;
        final /* synthetic */ SerialSubscription val$ssub;
        final /* synthetic */ Scheduler.Worker val$worker;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(Subscriber subscriber, SerialSubscription serialSubscription, Scheduler.Worker worker, SerializedSubscriber serializedSubscriber) {
            super(subscriber);
            this.val$ssub = serialSubscription;
            this.val$worker = worker;
            this.val$s = serializedSubscriber;
            this.state = new DebounceState<>();
            this.self = this;
        }

        @Override // rx.Subscriber
        public void onStart() {
            request(Long.MAX_VALUE);
        }

        @Override // rx.Observer
        public void onNext(T t) {
            final int index = this.state.next(t);
            this.val$ssub.set(this.val$worker.schedule(new Action0() { // from class: rx.internal.operators.OperatorDebounceWithTime.1.1
                @Override // rx.functions.Action0
                public void call() {
                    AnonymousClass1.this.state.emit(index, AnonymousClass1.this.val$s, AnonymousClass1.this.self);
                }
            }, OperatorDebounceWithTime.this.timeout, OperatorDebounceWithTime.this.unit));
        }

        @Override // rx.Observer
        public void onError(Throwable e) {
            this.val$s.onError(e);
            unsubscribe();
            this.state.clear();
        }

        @Override // rx.Observer
        public void onCompleted() {
            this.state.emitAndComplete(this.val$s, this);
        }
    }

    static final class DebounceState<T> {
        boolean emitting;
        boolean hasValue;
        int index;
        boolean terminate;
        T value;

        DebounceState() {
        }

        public synchronized int next(T value) {
            int i;
            this.value = value;
            this.hasValue = true;
            i = this.index + 1;
            this.index = i;
            return i;
        }

        public void emit(int index, Subscriber<T> onNextAndComplete, Subscriber<?> onError) {
            synchronized (this) {
                if (!this.emitting && this.hasValue && index == this.index) {
                    T localValue = this.value;
                    this.value = null;
                    this.hasValue = false;
                    this.emitting = true;
                    try {
                        onNextAndComplete.onNext(localValue);
                        synchronized (this) {
                            if (!this.terminate) {
                                this.emitting = false;
                            } else {
                                onNextAndComplete.onCompleted();
                            }
                        }
                    } catch (Throwable e) {
                        Exceptions.throwOrReport(e, onError, localValue);
                    }
                }
            }
        }

        public void emitAndComplete(Subscriber<T> onNextAndComplete, Subscriber<?> onError) {
            synchronized (this) {
                if (this.emitting) {
                    this.terminate = true;
                    return;
                }
                T localValue = this.value;
                boolean localHasValue = this.hasValue;
                this.value = null;
                this.hasValue = false;
                this.emitting = true;
                if (localHasValue) {
                    try {
                        onNextAndComplete.onNext(localValue);
                    } catch (Throwable e) {
                        Exceptions.throwOrReport(e, onError, localValue);
                        return;
                    }
                }
                onNextAndComplete.onCompleted();
            }
        }

        public synchronized void clear() {
            this.index++;
            this.value = null;
            this.hasValue = false;
        }
    }
}
