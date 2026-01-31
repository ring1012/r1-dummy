package rx.subjects;

import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.annotations.Beta;
import rx.exceptions.Exceptions;
import rx.functions.Action1;
import rx.internal.operators.NotificationLite;
import rx.subjects.SubjectSubscriptionManager;

/* loaded from: classes.dex */
public final class PublishSubject<T> extends Subject<T, T> {
    private final NotificationLite<T> nl;
    final SubjectSubscriptionManager<T> state;

    public static <T> PublishSubject<T> create() {
        final SubjectSubscriptionManager<T> state = new SubjectSubscriptionManager<>();
        state.onTerminated = new Action1<SubjectSubscriptionManager.SubjectObserver<T>>() { // from class: rx.subjects.PublishSubject.1
            @Override // rx.functions.Action1
            public void call(SubjectSubscriptionManager.SubjectObserver<T> o) {
                o.emitFirst(state.getLatest(), state.nl);
            }
        };
        return new PublishSubject<>(state, state);
    }

    protected PublishSubject(Observable.OnSubscribe<T> onSubscribe, SubjectSubscriptionManager<T> state) {
        super(onSubscribe);
        this.nl = NotificationLite.instance();
        this.state = state;
    }

    @Override // rx.Observer
    public void onCompleted() {
        if (this.state.active) {
            Object n = this.nl.completed();
            SubjectSubscriptionManager.SubjectObserver<T>[] arr$ = this.state.terminate(n);
            for (SubjectSubscriptionManager.SubjectObserver<T> bo : arr$) {
                bo.emitNext(n, this.state.nl);
            }
        }
    }

    @Override // rx.Observer
    public void onError(Throwable e) throws Throwable {
        if (this.state.active) {
            Object n = this.nl.error(e);
            List<Throwable> errors = null;
            SubjectSubscriptionManager.SubjectObserver<T>[] arr$ = this.state.terminate(n);
            for (SubjectSubscriptionManager.SubjectObserver<T> bo : arr$) {
                try {
                    bo.emitNext(n, this.state.nl);
                } catch (Throwable e2) {
                    if (errors == null) {
                        errors = new ArrayList<>();
                    }
                    errors.add(e2);
                }
            }
            Exceptions.throwIfAny(errors);
        }
    }

    @Override // rx.Observer
    public void onNext(T v) {
        SubjectSubscriptionManager.SubjectObserver<T>[] arr$ = this.state.observers();
        for (SubjectSubscriptionManager.SubjectObserver<T> bo : arr$) {
            bo.onNext(v);
        }
    }

    @Override // rx.subjects.Subject
    public boolean hasObservers() {
        return this.state.observers().length > 0;
    }

    @Beta
    public boolean hasThrowable() {
        Object o = this.state.getLatest();
        return this.nl.isError(o);
    }

    @Beta
    public boolean hasCompleted() {
        Object o = this.state.getLatest();
        return (o == null || this.nl.isError(o)) ? false : true;
    }

    @Beta
    public Throwable getThrowable() {
        Object o = this.state.getLatest();
        if (this.nl.isError(o)) {
            return this.nl.getError(o);
        }
        return null;
    }
}
