package rx.internal.operators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import rx.Observable;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.Exceptions;

/* loaded from: classes.dex */
public final class OperatorBufferWithSize<T> implements Observable.Operator<List<T>, T> {
    final int count;
    final int skip;

    public OperatorBufferWithSize(int count, int skip) {
        if (count <= 0) {
            throw new IllegalArgumentException("count must be greater than 0");
        }
        if (skip <= 0) {
            throw new IllegalArgumentException("skip must be greater than 0");
        }
        this.count = count;
        this.skip = skip;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(final Subscriber<? super List<T>> subscriber) {
        return this.count == this.skip ? new Subscriber<T>(subscriber) { // from class: rx.internal.operators.OperatorBufferWithSize.1
            List<T> buffer;

            @Override // rx.Subscriber
            public void setProducer(final Producer producer) {
                subscriber.setProducer(new Producer() { // from class: rx.internal.operators.OperatorBufferWithSize.1.1
                    private volatile boolean infinite = false;

                    @Override // rx.Producer
                    public void request(long n) {
                        if (this.infinite) {
                            return;
                        }
                        if (n >= Long.MAX_VALUE / OperatorBufferWithSize.this.count) {
                            this.infinite = true;
                            producer.request(Long.MAX_VALUE);
                        } else {
                            producer.request(OperatorBufferWithSize.this.count * n);
                        }
                    }
                });
            }

            @Override // rx.Observer
            public void onNext(T t) {
                if (this.buffer == null) {
                    this.buffer = new ArrayList(OperatorBufferWithSize.this.count);
                }
                this.buffer.add(t);
                if (this.buffer.size() == OperatorBufferWithSize.this.count) {
                    List<T> oldBuffer = this.buffer;
                    this.buffer = null;
                    subscriber.onNext(oldBuffer);
                }
            }

            @Override // rx.Observer
            public void onError(Throwable e) {
                this.buffer = null;
                subscriber.onError(e);
            }

            @Override // rx.Observer
            public void onCompleted() {
                List<T> oldBuffer = this.buffer;
                this.buffer = null;
                if (oldBuffer != null) {
                    try {
                        subscriber.onNext(oldBuffer);
                    } catch (Throwable t) {
                        Exceptions.throwOrReport(t, this);
                        return;
                    }
                }
                subscriber.onCompleted();
            }
        } : new Subscriber<T>(subscriber) { // from class: rx.internal.operators.OperatorBufferWithSize.2
            final List<List<T>> chunks = new LinkedList();
            int index;

            @Override // rx.Subscriber
            public void setProducer(final Producer producer) {
                subscriber.setProducer(new Producer() { // from class: rx.internal.operators.OperatorBufferWithSize.2.1
                    private volatile boolean firstRequest = true;
                    private volatile boolean infinite = false;

                    private void requestInfinite() {
                        this.infinite = true;
                        producer.request(Long.MAX_VALUE);
                    }

                    @Override // rx.Producer
                    public void request(long n) {
                        if (n == 0) {
                            return;
                        }
                        if (n < 0) {
                            throw new IllegalArgumentException("request a negative number: " + n);
                        }
                        if (this.infinite) {
                            return;
                        }
                        if (n == Long.MAX_VALUE) {
                            requestInfinite();
                            return;
                        }
                        if (!this.firstRequest) {
                            if (n >= Long.MAX_VALUE / OperatorBufferWithSize.this.skip) {
                                requestInfinite();
                                return;
                            } else {
                                producer.request(OperatorBufferWithSize.this.skip * n);
                                return;
                            }
                        }
                        this.firstRequest = false;
                        if (n - 1 >= (Long.MAX_VALUE - OperatorBufferWithSize.this.count) / OperatorBufferWithSize.this.skip) {
                            requestInfinite();
                        } else {
                            producer.request(OperatorBufferWithSize.this.count + (OperatorBufferWithSize.this.skip * (n - 1)));
                        }
                    }
                });
            }

            @Override // rx.Observer
            public void onNext(T t) {
                int i = this.index;
                this.index = i + 1;
                if (i % OperatorBufferWithSize.this.skip == 0) {
                    this.chunks.add(new ArrayList(OperatorBufferWithSize.this.count));
                }
                Iterator<List<T>> it = this.chunks.iterator();
                while (it.hasNext()) {
                    List<T> chunk = it.next();
                    chunk.add(t);
                    if (chunk.size() == OperatorBufferWithSize.this.count) {
                        it.remove();
                        subscriber.onNext(chunk);
                    }
                }
            }

            @Override // rx.Observer
            public void onError(Throwable e) {
                this.chunks.clear();
                subscriber.onError(e);
            }

            @Override // rx.Observer
            public void onCompleted() {
                try {
                    for (List<T> chunk : this.chunks) {
                        subscriber.onNext(chunk);
                    }
                    subscriber.onCompleted();
                } catch (Throwable t) {
                    Exceptions.throwOrReport(t, this);
                } finally {
                    this.chunks.clear();
                }
            }
        };
    }
}
