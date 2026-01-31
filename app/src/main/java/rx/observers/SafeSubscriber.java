package rx.observers;

import java.util.Arrays;
import rx.Subscriber;
import rx.exceptions.CompositeException;
import rx.exceptions.Exceptions;
import rx.exceptions.OnCompletedFailedException;
import rx.exceptions.OnErrorFailedException;
import rx.exceptions.OnErrorNotImplementedException;
import rx.exceptions.UnsubscribeFailedException;
import rx.internal.util.RxJavaPluginUtils;

/* loaded from: classes.dex */
public class SafeSubscriber<T> extends Subscriber<T> {
    private final Subscriber<? super T> actual;
    boolean done;

    public SafeSubscriber(Subscriber<? super T> actual) {
        super(actual);
        this.done = false;
        this.actual = actual;
    }

    @Override // rx.Observer
    public void onCompleted() {
        UnsubscribeFailedException unsubscribeFailedException;
        if (!this.done) {
            this.done = true;
            try {
                try {
                    this.actual.onCompleted();
                    try {
                        unsubscribe();
                    } finally {
                    }
                } catch (Throwable th) {
                    try {
                        unsubscribe();
                        throw th;
                    } finally {
                    }
                }
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                RxJavaPluginUtils.handleException(e);
                throw new OnCompletedFailedException(e.getMessage(), e);
            }
        }
    }

    @Override // rx.Observer
    public void onError(Throwable e) throws Throwable {
        Exceptions.throwIfFatal(e);
        if (!this.done) {
            this.done = true;
            _onError(e);
        }
    }

    @Override // rx.Observer
    public void onNext(T args) throws Throwable {
        try {
            if (!this.done) {
                this.actual.onNext(args);
            }
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            onError(e);
        }
    }

    protected void _onError(Throwable e) throws Throwable {
        RxJavaPluginUtils.handleException(e);
        try {
            this.actual.onError(e);
            try {
                unsubscribe();
            } catch (RuntimeException unsubscribeException) {
                RxJavaPluginUtils.handleException(unsubscribeException);
                throw new OnErrorFailedException(unsubscribeException);
            }
        } catch (Throwable e2) {
            if (e2 instanceof OnErrorNotImplementedException) {
                try {
                    unsubscribe();
                    throw ((OnErrorNotImplementedException) e2);
                } catch (Throwable unsubscribeException2) {
                    RxJavaPluginUtils.handleException(unsubscribeException2);
                    throw new RuntimeException("Observer.onError not implemented and error while unsubscribing.", new CompositeException(Arrays.asList(e, unsubscribeException2)));
                }
            }
            RxJavaPluginUtils.handleException(e2);
            try {
                unsubscribe();
                throw new OnErrorFailedException("Error occurred when trying to propagate error to Observer.onError", new CompositeException(Arrays.asList(e, e2)));
            } catch (Throwable unsubscribeException3) {
                RxJavaPluginUtils.handleException(unsubscribeException3);
                throw new OnErrorFailedException("Error occurred when trying to propagate error to Observer.onError and during unsubscription.", new CompositeException(Arrays.asList(e, e2, unsubscribeException3)));
            }
        }
    }

    public Subscriber<? super T> getActual() {
        return this.actual;
    }
}
