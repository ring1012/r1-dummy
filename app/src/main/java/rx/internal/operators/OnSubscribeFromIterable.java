package rx.internal.operators;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable;
import rx.Producer;
import rx.Subscriber;

/* loaded from: classes.dex */
public final class OnSubscribeFromIterable<T> implements Observable.OnSubscribe<T> {
    final Iterable<? extends T> is;

    public OnSubscribeFromIterable(Iterable<? extends T> iterable) {
        if (iterable == null) {
            throw new NullPointerException("iterable must not be null");
        }
        this.is = iterable;
    }

    @Override // rx.functions.Action1
    public void call(Subscriber<? super T> o) {
        Iterator<? extends T> it = this.is.iterator();
        if (!it.hasNext() && !o.isUnsubscribed()) {
            o.onCompleted();
        } else {
            o.setProducer(new IterableProducer(o, it));
        }
    }

    private static final class IterableProducer<T> extends AtomicLong implements Producer {
        private static final long serialVersionUID = -8730475647105475802L;
        private final Iterator<? extends T> it;
        private final Subscriber<? super T> o;

        private IterableProducer(Subscriber<? super T> o, Iterator<? extends T> it) {
            this.o = o;
            this.it = it;
        }

        @Override // rx.Producer
        public void request(long n) {
            if (get() == Long.MAX_VALUE) {
                return;
            }
            if (n == Long.MAX_VALUE && compareAndSet(0L, Long.MAX_VALUE)) {
                fastpath();
            } else if (n > 0 && BackpressureUtils.getAndAddRequest(this, n) == 0) {
                slowpath(n);
            }
        }

        void slowpath(long j) {
            Subscriber<? super T> subscriber = this.o;
            Iterator<? extends T> it = this.it;
            long jAddAndGet = j;
            do {
                long j2 = jAddAndGet;
                while (!subscriber.isUnsubscribed()) {
                    if (it.hasNext()) {
                        j2--;
                        if (j2 >= 0) {
                            subscriber.onNext(it.next());
                        } else {
                            jAddAndGet = addAndGet(-jAddAndGet);
                        }
                    } else {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onCompleted();
                            return;
                        }
                        return;
                    }
                }
                return;
            } while (jAddAndGet != 0);
        }

        void fastpath() {
            Subscriber<? super T> subscriber = this.o;
            Iterator<? extends T> it = this.it;
            while (!subscriber.isUnsubscribed()) {
                if (it.hasNext()) {
                    subscriber.onNext(it.next());
                } else {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onCompleted();
                        return;
                    }
                    return;
                }
            }
        }
    }
}
