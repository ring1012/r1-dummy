package rx.observers;

import rx.Observer;
import rx.Subscriber;

/* loaded from: classes.dex */
public class SerializedSubscriber<T> extends Subscriber<T> {
    private final Observer<T> s;

    public SerializedSubscriber(Subscriber<? super T> s) {
        this(s, true);
    }

    public SerializedSubscriber(Subscriber<? super T> s, boolean shareSubscriptions) {
        super(s, shareSubscriptions);
        this.s = new SerializedObserver(s);
    }

    @Override // rx.Observer
    public void onCompleted() {
        this.s.onCompleted();
    }

    @Override // rx.Observer
    public void onError(Throwable e) {
        this.s.onError(e);
    }

    @Override // rx.Observer
    public void onNext(T t) {
        this.s.onNext(t);
    }
}
