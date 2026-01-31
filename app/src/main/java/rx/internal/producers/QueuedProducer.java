package rx.internal.producers;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.exceptions.MissingBackpressureException;
import rx.internal.operators.BackpressureUtils;
import rx.internal.util.atomic.SpscLinkedAtomicQueue;
import rx.internal.util.unsafe.SpscLinkedQueue;
import rx.internal.util.unsafe.UnsafeAccess;

/* loaded from: classes.dex */
public final class QueuedProducer<T> extends AtomicLong implements Producer, Observer<T> {
    static final Object NULL_SENTINEL = new Object();
    private static final long serialVersionUID = 7277121710709137047L;
    final Subscriber<? super T> child;
    volatile boolean done;
    Throwable error;
    final Queue<Object> queue;
    final AtomicInteger wip;

    public QueuedProducer(Subscriber<? super T> child) {
        this(child, UnsafeAccess.isUnsafeAvailable() ? new SpscLinkedQueue() : new SpscLinkedAtomicQueue());
    }

    public QueuedProducer(Subscriber<? super T> child, Queue<Object> queue) {
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

    @Override // rx.Observer
    public void onNext(T value) {
        if (!offer(value)) {
            onError(new MissingBackpressureException());
        }
    }

    @Override // rx.Observer
    public void onError(Throwable e) {
        this.error = e;
        this.done = true;
        drain();
    }

    @Override // rx.Observer
    public void onCompleted() {
        this.done = true;
        drain();
    }

    private boolean checkTerminated(boolean isDone, boolean isEmpty) {
        if (this.child.isUnsubscribed()) {
            return true;
        }
        if (isDone) {
            Throwable e = this.error;
            if (e != null) {
                this.queue.clear();
                this.child.onError(e);
                return true;
            }
            if (isEmpty) {
                this.child.onCompleted();
                return true;
            }
        }
        return false;
    }

    private void drain() {
        if (this.wip.getAndIncrement() == 0) {
            Subscriber<? super T> c = this.child;
            Queue<Object> q = this.queue;
            while (!checkTerminated(this.done, q.isEmpty())) {
                this.wip.lazySet(1);
                long r = get();
                long e = 0;
                while (r != 0) {
                    boolean d = this.done;
                    Object v = q.poll();
                    if (!checkTerminated(d, v == null)) {
                        if (v == null) {
                            break;
                        }
                        try {
                            if (v == NULL_SENTINEL) {
                                c.onNext(null);
                            } else {
                                c.onNext(v);
                            }
                            r--;
                            e++;
                        } catch (Throwable ex) {
                            if (v == NULL_SENTINEL) {
                                v = null;
                            }
                            Exceptions.throwOrReport(ex, c, v);
                            return;
                        }
                    } else {
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
