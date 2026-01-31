package rx.internal.operators;

import rx.Observable;
import rx.Producer;
import rx.Subscriber;
import rx.internal.producers.ProducerArbiter;
import rx.subscriptions.SerialSubscription;

/* loaded from: classes.dex */
public final class OperatorSwitchIfEmpty<T> implements Observable.Operator<T, T> {
    private final Observable<? extends T> alternate;

    public OperatorSwitchIfEmpty(Observable<? extends T> alternate) {
        this.alternate = alternate;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(Subscriber<? super T> child) {
        SerialSubscription ssub = new SerialSubscription();
        ProducerArbiter arbiter = new ProducerArbiter();
        ParentSubscriber<T> parent = new ParentSubscriber<>(child, ssub, arbiter, this.alternate);
        ssub.set(parent);
        child.add(ssub);
        child.setProducer(arbiter);
        return parent;
    }

    private static final class ParentSubscriber<T> extends Subscriber<T> {
        private final Observable<? extends T> alternate;
        private final ProducerArbiter arbiter;
        private final Subscriber<? super T> child;
        private boolean empty = true;
        private final SerialSubscription ssub;

        ParentSubscriber(Subscriber<? super T> child, SerialSubscription ssub, ProducerArbiter arbiter, Observable<? extends T> alternate) {
            this.child = child;
            this.ssub = ssub;
            this.arbiter = arbiter;
            this.alternate = alternate;
        }

        @Override // rx.Subscriber
        public void setProducer(Producer producer) {
            this.arbiter.setProducer(producer);
        }

        @Override // rx.Observer
        public void onCompleted() {
            if (!this.empty) {
                this.child.onCompleted();
            } else if (!this.child.isUnsubscribed()) {
                subscribeToAlternate();
            }
        }

        private void subscribeToAlternate() {
            AlternateSubscriber<T> as = new AlternateSubscriber<>(this.child, this.arbiter);
            this.ssub.set(as);
            this.alternate.unsafeSubscribe(as);
        }

        @Override // rx.Observer
        public void onError(Throwable e) {
            this.child.onError(e);
        }

        @Override // rx.Observer
        public void onNext(T t) {
            this.empty = false;
            this.child.onNext(t);
            this.arbiter.produced(1L);
        }
    }

    private static final class AlternateSubscriber<T> extends Subscriber<T> {
        private final ProducerArbiter arbiter;
        private final Subscriber<? super T> child;

        AlternateSubscriber(Subscriber<? super T> child, ProducerArbiter arbiter) {
            this.child = child;
            this.arbiter = arbiter;
        }

        @Override // rx.Subscriber
        public void setProducer(Producer producer) {
            this.arbiter.setProducer(producer);
        }

        @Override // rx.Observer
        public void onCompleted() {
            this.child.onCompleted();
        }

        @Override // rx.Observer
        public void onError(Throwable e) {
            this.child.onError(e);
        }

        @Override // rx.Observer
        public void onNext(T t) {
            this.child.onNext(t);
            this.arbiter.produced(1L);
        }
    }
}
