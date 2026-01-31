package rx.subjects;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.annotations.Beta;
import rx.exceptions.Exceptions;
import rx.functions.Action1;
import rx.internal.operators.NotificationLite;
import rx.subjects.SubjectSubscriptionManager;

/* loaded from: classes.dex */
public final class BehaviorSubject<T> extends Subject<T, T> {
    private static final Object[] EMPTY_ARRAY = new Object[0];
    private final NotificationLite<T> nl;
    private final SubjectSubscriptionManager<T> state;

    public static <T> BehaviorSubject<T> create() {
        return create(null, false);
    }

    public static <T> BehaviorSubject<T> create(T defaultValue) {
        return create(defaultValue, true);
    }

    private static <T> BehaviorSubject<T> create(T defaultValue, boolean hasDefault) {
        final SubjectSubscriptionManager<T> state = new SubjectSubscriptionManager<>();
        if (hasDefault) {
            state.setLatest(NotificationLite.instance().next(defaultValue));
        }
        state.onAdded = new Action1<SubjectSubscriptionManager.SubjectObserver<T>>() { // from class: rx.subjects.BehaviorSubject.1
            @Override // rx.functions.Action1
            public void call(SubjectSubscriptionManager.SubjectObserver<T> o) {
                o.emitFirst(state.getLatest(), state.nl);
            }
        };
        state.onTerminated = state.onAdded;
        return new BehaviorSubject<>(state, state);
    }

    protected BehaviorSubject(Observable.OnSubscribe<T> onSubscribe, SubjectSubscriptionManager<T> state) {
        super(onSubscribe);
        this.nl = NotificationLite.instance();
        this.state = state;
    }

    @Override // rx.Observer
    public void onCompleted() {
        Object last = this.state.getLatest();
        if (last == null || this.state.active) {
            Object n = this.nl.completed();
            SubjectSubscriptionManager.SubjectObserver<T>[] arr$ = this.state.terminate(n);
            for (SubjectSubscriptionManager.SubjectObserver<T> bo : arr$) {
                bo.emitNext(n, this.state.nl);
            }
        }
    }

    @Override // rx.Observer
    public void onError(Throwable e) throws Throwable {
        Object last = this.state.getLatest();
        if (last == null || this.state.active) {
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
        Object last = this.state.getLatest();
        if (last == null || this.state.active) {
            Object n = this.nl.next(v);
            SubjectSubscriptionManager.SubjectObserver<T>[] arr$ = this.state.next(n);
            for (SubjectSubscriptionManager.SubjectObserver<T> bo : arr$) {
                bo.emitNext(n, this.state.nl);
            }
        }
    }

    int subscriberCount() {
        return this.state.observers().length;
    }

    @Override // rx.subjects.Subject
    public boolean hasObservers() {
        return this.state.observers().length > 0;
    }

    @Beta
    public boolean hasValue() {
        Object o = this.state.getLatest();
        return this.nl.isNext(o);
    }

    @Beta
    public boolean hasThrowable() {
        Object o = this.state.getLatest();
        return this.nl.isError(o);
    }

    @Beta
    public boolean hasCompleted() {
        Object o = this.state.getLatest();
        return this.nl.isCompleted(o);
    }

    @Beta
    public T getValue() {
        Object o = this.state.getLatest();
        if (this.nl.isNext(o)) {
            return this.nl.getValue(o);
        }
        return null;
    }

    @Beta
    public Throwable getThrowable() {
        Object o = this.state.getLatest();
        if (this.nl.isError(o)) {
            return this.nl.getError(o);
        }
        return null;
    }

    @Beta
    public T[] getValues(T[] tArr) {
        Object latest = this.state.getLatest();
        if (this.nl.isNext(latest)) {
            if (tArr.length == 0) {
                tArr = (T[]) ((Object[]) Array.newInstance(tArr.getClass().getComponentType(), 1));
            }
            tArr[0] = this.nl.getValue(latest);
            if (tArr.length > 1) {
                tArr[1] = null;
            }
        } else if (tArr.length > 0) {
            tArr[0] = null;
        }
        return tArr;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Beta
    public Object[] getValues() {
        Object[] values = getValues(EMPTY_ARRAY);
        if (values == EMPTY_ARRAY) {
            return new Object[0];
        }
        return values;
    }
}
