package rx.internal.operators;

import rx.Observable;
import rx.Producer;
import rx.Subscriber;

/* loaded from: classes.dex */
public final class OperatorSkip<T> implements Observable.Operator<T, T> {
    final int toSkip;

    public OperatorSkip(int n) {
        this.toSkip = n;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        return new Subscriber<T>(subscriber) { // from class: rx.internal.operators.OperatorSkip.1
            int skipped = 0;

            @Override // rx.Observer
            public void onCompleted() {
                subscriber.onCompleted();
            }

            @Override // rx.Observer
            public void onError(Throwable e) {
                subscriber.onError(e);
            }

            @Override // rx.Observer
            public void onNext(T t) {
                if (this.skipped >= OperatorSkip.this.toSkip) {
                    subscriber.onNext(t);
                } else {
                    this.skipped++;
                }
            }

            @Override // rx.Subscriber
            public void setProducer(Producer producer) {
                subscriber.setProducer(producer);
                producer.request(OperatorSkip.this.toSkip);
            }
        };
    }
}
