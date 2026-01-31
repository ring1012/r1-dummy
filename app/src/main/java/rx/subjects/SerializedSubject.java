package rx.subjects;

import rx.Observable;
import rx.Subscriber;
import rx.observers.SerializedObserver;

/* loaded from: classes.dex */
public class SerializedSubject<T, R> extends Subject<T, R> {
    private final Subject<T, R> actual;
    private final SerializedObserver<T> observer;

    public SerializedSubject(final Subject<T, R> actual) {
        super(new Observable.OnSubscribe<R>() { // from class: rx.subjects.SerializedSubject.1
            @Override // rx.functions.Action1
            public void call(Subscriber<? super R> child) {
                actual.unsafeSubscribe(child);
            }
        });
        this.actual = actual;
        this.observer = new SerializedObserver<>(actual);
    }

    @Override // rx.Observer
    public void onCompleted() {
        this.observer.onCompleted();
    }

    @Override // rx.Observer
    public void onError(Throwable e) {
        this.observer.onError(e);
    }

    @Override // rx.Observer
    public void onNext(T t) {
        this.observer.onNext(t);
    }

    @Override // rx.subjects.Subject
    public boolean hasObservers() {
        return this.actual.hasObservers();
    }
}
