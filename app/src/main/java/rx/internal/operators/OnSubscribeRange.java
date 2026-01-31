package rx.internal.operators;

import java.util.concurrent.atomic.AtomicLong;
import rx.Observable;
import rx.Producer;
import rx.Subscriber;

/* loaded from: classes.dex */
public final class OnSubscribeRange implements Observable.OnSubscribe<Integer> {
    private final int end;
    private final int start;

    public OnSubscribeRange(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override // rx.functions.Action1
    public void call(Subscriber<? super Integer> o) {
        o.setProducer(new RangeProducer(o, this.start, this.end));
    }

    private static final class RangeProducer extends AtomicLong implements Producer {
        private static final long serialVersionUID = 4114392207069098388L;
        private final int end;
        private long index;
        private final Subscriber<? super Integer> o;

        private RangeProducer(Subscriber<? super Integer> o, int start, int end) {
            this.o = o;
            this.index = start;
            this.end = end;
        }

        @Override // rx.Producer
        public void request(long n) {
            if (get() == Long.MAX_VALUE) {
                return;
            }
            if (n == Long.MAX_VALUE && compareAndSet(0L, Long.MAX_VALUE)) {
                fastpath();
            } else if (n > 0) {
                long c = BackpressureUtils.getAndAddRequest(this, n);
                if (c == 0) {
                    slowpath(n);
                }
            }
        }

        void slowpath(long r) {
            long idx = this.index;
            do {
                long fs = (this.end - idx) + 1;
                long e = Math.min(fs, r);
                boolean complete = fs <= r;
                long fs2 = e + idx;
                Subscriber<? super Integer> o = this.o;
                for (long i = idx; i != fs2; i++) {
                    if (!o.isUnsubscribed()) {
                        o.onNext(Integer.valueOf((int) i));
                    } else {
                        return;
                    }
                }
                if (complete) {
                    if (!o.isUnsubscribed()) {
                        o.onCompleted();
                        return;
                    }
                    return;
                } else {
                    idx = fs2;
                    this.index = fs2;
                    r = addAndGet(-e);
                }
            } while (r != 0);
        }

        void fastpath() {
            long end = this.end + 1;
            Subscriber<? super Integer> o = this.o;
            for (long i = this.index; i != end; i++) {
                if (!o.isUnsubscribed()) {
                    o.onNext(Integer.valueOf((int) i));
                } else {
                    return;
                }
            }
            if (!o.isUnsubscribed()) {
                o.onCompleted();
            }
        }
    }
}
