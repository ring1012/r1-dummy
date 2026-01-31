package rx.internal.operators;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.functions.Func2;

/* loaded from: classes.dex */
public final class OperatorSkipWhile<T> implements Observable.Operator<T, T> {
    private final Func2<? super T, Integer, Boolean> predicate;

    public OperatorSkipWhile(Func2<? super T, Integer, Boolean> predicate) {
        this.predicate = predicate;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        return new Subscriber<T>(subscriber) { // from class: rx.internal.operators.OperatorSkipWhile.1
            int index;
            boolean skipping = true;

            @Override // rx.Observer
            public void onNext(T t) {
                if (this.skipping) {
                    Func2 func2 = OperatorSkipWhile.this.predicate;
                    int i = this.index;
                    this.index = i + 1;
                    if (!((Boolean) func2.call(t, Integer.valueOf(i))).booleanValue()) {
                        this.skipping = false;
                        subscriber.onNext(t);
                        return;
                    } else {
                        request(1L);
                        return;
                    }
                }
                subscriber.onNext(t);
            }

            @Override // rx.Observer
            public void onError(Throwable e) {
                subscriber.onError(e);
            }

            @Override // rx.Observer
            public void onCompleted() {
                subscriber.onCompleted();
            }
        };
    }

    public static <T> Func2<T, Integer, Boolean> toPredicate2(final Func1<? super T, Boolean> predicate) {
        return new Func2<T, Integer, Boolean>() { // from class: rx.internal.operators.OperatorSkipWhile.2
            @Override // rx.functions.Func2
            public /* bridge */ /* synthetic */ Boolean call(Object x0, Integer num) {
                return call2((AnonymousClass2) x0, num);
            }

            /* renamed from: call, reason: avoid collision after fix types in other method */
            public Boolean call2(T t1, Integer t2) {
                return (Boolean) predicate.call(t1);
            }
        };
    }
}
