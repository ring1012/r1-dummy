package rx.internal.producers;

import java.util.ArrayList;
import java.util.List;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;

/* loaded from: classes.dex */
public final class ProducerObserverArbiter<T> implements Producer, Observer<T> {
    static final Producer NULL_PRODUCER = new Producer() { // from class: rx.internal.producers.ProducerObserverArbiter.1
        @Override // rx.Producer
        public void request(long n) {
        }
    };
    final Subscriber<? super T> child;
    Producer currentProducer;
    boolean emitting;
    volatile boolean hasError;
    Producer missedProducer;
    long missedRequested;
    Object missedTerminal;
    List<T> queue;
    long requested;

    public ProducerObserverArbiter(Subscriber<? super T> child) {
        this.child = child;
    }

    @Override // rx.Observer
    public void onNext(T t) {
        synchronized (this) {
            if (this.emitting) {
                List<T> q = this.queue;
                if (q == null) {
                    q = new ArrayList<>(4);
                    this.queue = q;
                }
                q.add(t);
                return;
            }
            try {
                this.child.onNext(t);
                long r = this.requested;
                if (r != Long.MAX_VALUE) {
                    this.requested = r - 1;
                }
                emitLoop();
                if (1 == 0) {
                    synchronized (this) {
                        this.emitting = false;
                    }
                }
            } catch (Throwable th) {
                if (0 == 0) {
                    synchronized (this) {
                        this.emitting = false;
                    }
                }
                throw th;
            }
        }
    }

    @Override // rx.Observer
    public void onError(Throwable e) {
        boolean emit;
        synchronized (this) {
            if (this.emitting) {
                this.missedTerminal = e;
                emit = false;
            } else {
                this.emitting = true;
                emit = true;
            }
        }
        if (emit) {
            this.child.onError(e);
        } else {
            this.hasError = true;
        }
    }

    @Override // rx.Observer
    public void onCompleted() {
        synchronized (this) {
            if (this.emitting) {
                this.missedTerminal = true;
            } else {
                this.emitting = true;
                this.child.onCompleted();
            }
        }
    }

    @Override // rx.Producer
    public void request(long n) {
        if (n < 0) {
            throw new IllegalArgumentException("n >= 0 required");
        }
        if (n != 0) {
            synchronized (this) {
                if (this.emitting) {
                    this.missedRequested += n;
                } else {
                    this.emitting = true;
                    Producer p = this.currentProducer;
                    try {
                        long r = this.requested;
                        long u = r + n;
                        if (u < 0) {
                            u = Long.MAX_VALUE;
                        }
                        this.requested = u;
                        emitLoop();
                        if (1 == 0) {
                            synchronized (this) {
                                this.emitting = false;
                            }
                        }
                        if (p != null) {
                            p.request(n);
                        }
                    } catch (Throwable th) {
                        if (0 == 0) {
                            synchronized (this) {
                                this.emitting = false;
                            }
                        }
                        throw th;
                    }
                }
            }
        }
    }

    public void setProducer(Producer p) {
        synchronized (this) {
            if (this.emitting) {
                if (p == null) {
                    p = NULL_PRODUCER;
                }
                this.missedProducer = p;
                return;
            }
            this.emitting = true;
            this.currentProducer = p;
            long r = this.requested;
            try {
                emitLoop();
                if (1 == 0) {
                    synchronized (this) {
                        this.emitting = false;
                    }
                }
                if (p != null && r != 0) {
                    p.request(r);
                }
            } catch (Throwable th) {
                if (0 == 0) {
                    synchronized (this) {
                        this.emitting = false;
                    }
                }
                throw th;
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:97:0x0008, code lost:
    
        continue;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void emitLoop() {
        /*
            Method dump skipped, instructions count: 328
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.internal.producers.ProducerObserverArbiter.emitLoop():void");
    }
}
