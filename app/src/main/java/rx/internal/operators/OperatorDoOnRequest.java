package rx.internal.operators;

import rx.Observable;
import rx.Producer;
import rx.Subscriber;
import rx.functions.Action1;

/* loaded from: classes.dex */
public class OperatorDoOnRequest<T> implements Observable.Operator<T, T> {
    private final Action1<Long> request;

    public OperatorDoOnRequest(Action1<Long> request) {
        this.request = request;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(Subscriber<? super T> child) {
        final ParentSubscriber<T> parent = new ParentSubscriber<>(child);
        child.setProducer(new Producer() { // from class: rx.internal.operators.OperatorDoOnRequest.1
            @Override // rx.Producer
            public void request(long n) {
                OperatorDoOnRequest.this.request.call(Long.valueOf(n));
                parent.requestMore(n);
            }
        });
        child.add(parent);
        return parent;
    }

    private static final class ParentSubscriber<T> extends Subscriber<T> {
        private final Subscriber<? super T> child;

        private ParentSubscriber(Subscriber<? super T> child) {
            this.child = child;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void requestMore(long n) {
            request(n);
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
        }
    }
}
