package rx.internal.operators;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.internal.operators.OperatorDebounceWithTime;
import rx.observers.SerializedSubscriber;
import rx.subscriptions.SerialSubscription;

/* loaded from: classes.dex */
public final class OperatorDebounceWithSelector<T, U> implements Observable.Operator<T, T> {
    final Func1<? super T, ? extends Observable<U>> selector;

    public OperatorDebounceWithSelector(Func1<? super T, ? extends Observable<U>> selector) {
        this.selector = selector;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(Subscriber<? super T> child) {
        SerializedSubscriber<T> s = new SerializedSubscriber<>(child);
        SerialSubscription ssub = new SerialSubscription();
        child.add(ssub);
        return new AnonymousClass1(child, s, ssub);
    }

    /* renamed from: rx.internal.operators.OperatorDebounceWithSelector$1, reason: invalid class name */
    class AnonymousClass1 extends Subscriber<T> {
        final Subscriber<?> self;
        final OperatorDebounceWithTime.DebounceState<T> state;
        final /* synthetic */ SerializedSubscriber val$s;
        final /* synthetic */ SerialSubscription val$ssub;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(Subscriber subscriber, SerializedSubscriber serializedSubscriber, SerialSubscription serialSubscription) {
            super(subscriber);
            this.val$s = serializedSubscriber;
            this.val$ssub = serialSubscription;
            this.state = new OperatorDebounceWithTime.DebounceState<>();
            this.self = this;
        }

        @Override // rx.Subscriber
        public void onStart() {
            request(Long.MAX_VALUE);
        }

        @Override // rx.Observer
        public void onNext(T t) {
            try {
                Observable<U> debouncer = OperatorDebounceWithSelector.this.selector.call(t);
                final int index = this.state.next(t);
                Subscriber<U> debounceSubscriber = new Subscriber<U>() { // from class: rx.internal.operators.OperatorDebounceWithSelector.1.1
                    @Override // rx.Observer
                    public void onNext(U t2) {
                        onCompleted();
                    }

                    @Override // rx.Observer
                    public void onError(Throwable e) {
                        AnonymousClass1.this.self.onError(e);
                    }

                    @Override // rx.Observer
                    public void onCompleted() {
                        AnonymousClass1.this.state.emit(index, AnonymousClass1.this.val$s, AnonymousClass1.this.self);
                        unsubscribe();
                    }
                };
                this.val$ssub.set(debounceSubscriber);
                debouncer.unsafeSubscribe(debounceSubscriber);
            } catch (Throwable e) {
                Exceptions.throwOrReport(e, this);
            }
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
}
