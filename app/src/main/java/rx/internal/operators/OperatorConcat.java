package rx.internal.operators;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable;
import rx.Producer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.internal.producers.ProducerArbiter;
import rx.observers.SerializedSubscriber;
import rx.subscriptions.SerialSubscription;
import rx.subscriptions.Subscriptions;

/* loaded from: classes.dex */
public final class OperatorConcat<T> implements Observable.Operator<T, Observable<? extends T>> {

    private static final class Holder {
        static final OperatorConcat<Object> INSTANCE = new OperatorConcat<>();

        private Holder() {
        }
    }

    public static <T> OperatorConcat<T> instance() {
        return (OperatorConcat<T>) Holder.INSTANCE;
    }

    private OperatorConcat() {
    }

    @Override // rx.functions.Func1
    public Subscriber<? super Observable<? extends T>> call(Subscriber<? super T> child) {
        SerializedSubscriber<T> s = new SerializedSubscriber<>(child);
        SerialSubscription current = new SerialSubscription();
        child.add(current);
        ConcatSubscriber<T> cs = new ConcatSubscriber<>(s, current);
        ConcatProducer<T> cp = new ConcatProducer<>(cs);
        child.setProducer(cp);
        return cs;
    }

    static final class ConcatProducer<T> implements Producer {
        final ConcatSubscriber<T> cs;

        ConcatProducer(ConcatSubscriber<T> cs) {
            this.cs = cs;
        }

        @Override // rx.Producer
        public void request(long n) {
            this.cs.requestFromChild(n);
        }
    }

    static final class ConcatSubscriber<T> extends Subscriber<Observable<? extends T>> {
        private final ProducerArbiter arbiter;
        private final Subscriber<T> child;
        private final SerialSubscription current;
        volatile ConcatInnerSubscriber<T> currentSubscriber;
        final NotificationLite<Observable<? extends T>> nl;
        final ConcurrentLinkedQueue<Object> queue;
        private final AtomicLong requested;
        final AtomicInteger wip;

        public ConcatSubscriber(Subscriber<T> s, SerialSubscription current) {
            super(s);
            this.nl = NotificationLite.instance();
            this.wip = new AtomicInteger();
            this.requested = new AtomicLong();
            this.child = s;
            this.current = current;
            this.arbiter = new ProducerArbiter();
            this.queue = new ConcurrentLinkedQueue<>();
            add(Subscriptions.create(new Action0() { // from class: rx.internal.operators.OperatorConcat.ConcatSubscriber.1
                @Override // rx.functions.Action0
                public void call() {
                    ConcatSubscriber.this.queue.clear();
                }
            }));
        }

        @Override // rx.Subscriber
        public void onStart() {
            request(2L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void requestFromChild(long n) {
            if (n > 0) {
                long previous = BackpressureUtils.getAndAddRequest(this.requested, n);
                this.arbiter.request(n);
                if (previous == 0 && this.currentSubscriber == null && this.wip.get() > 0) {
                    subscribeNext();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void decrementRequested() {
            this.requested.decrementAndGet();
        }

        @Override // rx.Observer
        public void onNext(Observable<? extends T> t) {
            this.queue.add(this.nl.next(t));
            if (this.wip.getAndIncrement() == 0) {
                subscribeNext();
            }
        }

        @Override // rx.Observer
        public void onError(Throwable e) {
            this.child.onError(e);
            unsubscribe();
        }

        @Override // rx.Observer
        public void onCompleted() {
            this.queue.add(this.nl.completed());
            if (this.wip.getAndIncrement() == 0) {
                subscribeNext();
            }
        }

        void completeInner() {
            this.currentSubscriber = null;
            if (this.wip.decrementAndGet() > 0) {
                subscribeNext();
            }
            request(1L);
        }

        void subscribeNext() {
            if (this.requested.get() > 0) {
                Object o = this.queue.poll();
                if (this.nl.isCompleted(o)) {
                    this.child.onCompleted();
                    return;
                } else {
                    if (o != null) {
                        Observable<? extends T> obs = this.nl.getValue(o);
                        this.currentSubscriber = new ConcatInnerSubscriber<>(this, this.child, this.arbiter);
                        this.current.set(this.currentSubscriber);
                        obs.unsafeSubscribe(this.currentSubscriber);
                        return;
                    }
                    return;
                }
            }
            if (this.nl.isCompleted(this.queue.peek())) {
                this.child.onCompleted();
            }
        }
    }

    static class ConcatInnerSubscriber<T> extends Subscriber<T> {
        private final ProducerArbiter arbiter;
        private final Subscriber<T> child;
        private final AtomicInteger once = new AtomicInteger();
        private final ConcatSubscriber<T> parent;

        public ConcatInnerSubscriber(ConcatSubscriber<T> parent, Subscriber<T> child, ProducerArbiter arbiter) {
            this.parent = parent;
            this.child = child;
            this.arbiter = arbiter;
        }

        @Override // rx.Observer
        public void onNext(T t) {
            this.child.onNext(t);
            this.parent.decrementRequested();
            this.arbiter.produced(1L);
        }

        @Override // rx.Observer
        public void onError(Throwable e) {
            if (this.once.compareAndSet(0, 1)) {
                this.parent.onError(e);
            }
        }

        @Override // rx.Observer
        public void onCompleted() {
            if (this.once.compareAndSet(0, 1)) {
                this.parent.completeInner();
            }
        }

        @Override // rx.Subscriber
        public void setProducer(Producer producer) {
            this.arbiter.setProducer(producer);
        }
    }
}
