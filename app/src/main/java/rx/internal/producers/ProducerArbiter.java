package rx.internal.producers;

import rx.Producer;

/* loaded from: classes.dex */
public final class ProducerArbiter implements Producer {
    static final Producer NULL_PRODUCER = new Producer() { // from class: rx.internal.producers.ProducerArbiter.1
        @Override // rx.Producer
        public void request(long n) {
        }
    };
    Producer currentProducer;
    boolean emitting;
    long missedProduced;
    Producer missedProducer;
    long missedRequested;
    long requested;

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
                    try {
                        long r = this.requested;
                        long u = r + n;
                        if (u < 0) {
                            u = Long.MAX_VALUE;
                        }
                        this.requested = u;
                        Producer p = this.currentProducer;
                        if (p != null) {
                            p.request(n);
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
        }
    }

    public void produced(long n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n > 0 required");
        }
        synchronized (this) {
            if (this.emitting) {
                this.missedProduced += n;
                return;
            }
            this.emitting = true;
            try {
                long r = this.requested;
                if (r != Long.MAX_VALUE) {
                    long u = r - n;
                    if (u < 0) {
                        throw new IllegalStateException("more items arrived than were requested");
                    }
                    this.requested = u;
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

    public void setProducer(Producer newProducer) {
        synchronized (this) {
            if (this.emitting) {
                if (newProducer == null) {
                    newProducer = NULL_PRODUCER;
                }
                this.missedProducer = newProducer;
                return;
            }
            this.emitting = true;
            try {
                this.currentProducer = newProducer;
                if (newProducer != null) {
                    newProducer.request(this.requested);
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

    public void emitLoop() {
        while (true) {
            synchronized (this) {
                long localRequested = this.missedRequested;
                long localProduced = this.missedProduced;
                Producer localProducer = this.missedProducer;
                if (localRequested == 0 && localProduced == 0 && localProducer == null) {
                    this.emitting = false;
                    return;
                }
                this.missedRequested = 0L;
                this.missedProduced = 0L;
                this.missedProducer = null;
                long r = this.requested;
                if (r != Long.MAX_VALUE) {
                    long u = r + localRequested;
                    if (u < 0 || u == Long.MAX_VALUE) {
                        r = Long.MAX_VALUE;
                        this.requested = Long.MAX_VALUE;
                    } else {
                        long v = u - localProduced;
                        if (v < 0) {
                            throw new IllegalStateException("more produced than requested");
                        }
                        r = v;
                        this.requested = v;
                    }
                }
                if (localProducer != null) {
                    if (localProducer == NULL_PRODUCER) {
                        this.currentProducer = null;
                    } else {
                        this.currentProducer = localProducer;
                        localProducer.request(r);
                    }
                } else {
                    Producer p = this.currentProducer;
                    if (p != null && localRequested != 0) {
                        p.request(localRequested);
                    }
                }
            }
        }
    }
}
