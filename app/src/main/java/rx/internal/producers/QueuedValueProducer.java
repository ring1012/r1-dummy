package rx.internal.producers;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.internal.operators.BackpressureUtils;
import rx.internal.util.atomic.SpscLinkedAtomicQueue;
import rx.internal.util.unsafe.SpscLinkedQueue;
import rx.internal.util.unsafe.UnsafeAccess;

/* loaded from: classes.dex */
public final class QueuedValueProducer<T> extends AtomicLong implements Producer {
    static final Object NULL_SENTINEL = new Object();
    private static final long serialVersionUID = 7277121710709137047L;
    final Subscriber<? super T> child;
    final Queue<Object> queue;
    final AtomicInteger wip;

    public QueuedValueProducer(Subscriber<? super T> child) {
        this(child, UnsafeAccess.isUnsafeAvailable() ? new SpscLinkedQueue() : new SpscLinkedAtomicQueue());
    }

    public QueuedValueProducer(Subscriber<? super T> child, Queue<Object> queue) {
        this.child = child;
        this.queue = queue;
        this.wip = new AtomicInteger();
    }

    @Override // rx.Producer
    public void request(long n) {
        if (n < 0) {
            throw new IllegalArgumentException("n >= 0 required");
        }
        if (n > 0) {
            BackpressureUtils.getAndAddRequest(this, n);
            drain();
        }
    }

    public boolean offer(T value) {
        if (value == null) {
            if (!this.queue.offer(NULL_SENTINEL)) {
                return false;
            }
        } else if (!this.queue.offer(value)) {
            return false;
        }
        drain();
        return true;
    }

    private void drain() {
        Object v;
        if (this.wip.getAndIncrement() == 0) {
            Subscriber<? super T> c = this.child;
            Queue<Object> q = this.queue;
            while (!c.isUnsubscribed()) {
                this.wip.lazySet(1);
                long r = get();
                long e = 0;
                while (r != 0 && (v = q.poll()) != null) {
                    try {
                        if (v == NULL_SENTINEL) {
                            c.onNext(null);
                        } else {
                            c.onNext(v);
                        }
                        if (!c.isUnsubscribed()) {
                            r--;
                            e++;
                        } else {
                            return;
                        }
                    } catch (Throwable ex) {
                        if (v == NULL_SENTINEL) {
                            v = null;
                        }
                        Exceptions.throwOrReport(ex, c, v);
                        return;
                    }
                }
                if (e != 0 && get() != Long.MAX_VALUE) {
                    addAndGet(-e);
                }
                if (this.wip.decrementAndGet() == 0) {
                    return;
                }
            }
        }
    }
}
