package rx.internal.operators;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func0;
import rx.observers.Subscribers;

/* loaded from: classes.dex */
public final class OnSubscribeDefer<T> implements Observable.OnSubscribe<T> {
    final Func0<? extends Observable<? extends T>> observableFactory;

    public OnSubscribeDefer(Func0<? extends Observable<? extends T>> observableFactory) {
        this.observableFactory = observableFactory;
    }

    @Override // rx.functions.Action1
    public void call(Subscriber<? super T> s) {
        try {
            Observable<? extends T> o = this.observableFactory.call();
            o.unsafeSubscribe(Subscribers.wrap(s));
        } catch (Throwable t) {
            Exceptions.throwOrReport(t, s);
        }
    }
}
