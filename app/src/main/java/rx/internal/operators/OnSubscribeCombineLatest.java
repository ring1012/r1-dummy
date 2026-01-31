package rx.internal.operators;

import java.util.BitSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.exceptions.MissingBackpressureException;
import rx.functions.FuncN;
import rx.internal.util.RxRingBuffer;

/* loaded from: classes.dex */
public final class OnSubscribeCombineLatest<T, R> implements Observable.OnSubscribe<R> {
    final FuncN<? extends R> combinator;
    final List<? extends Observable<? extends T>> sources;

    public OnSubscribeCombineLatest(List<? extends Observable<? extends T>> sources, FuncN<? extends R> combinator) {
        this.sources = sources;
        this.combinator = combinator;
        if (sources.size() > RxRingBuffer.SIZE) {
            throw new IllegalArgumentException("More than RxRingBuffer.SIZE sources to combineLatest is not supported.");
        }
    }

    @Override // rx.functions.Action1
    public void call(Subscriber<? super R> child) {
        if (this.sources.isEmpty()) {
            child.onCompleted();
        } else if (this.sources.size() == 1) {
            child.setProducer(new SingleSourceProducer(child, this.sources.get(0), this.combinator));
        } else {
            child.setProducer(new MultiSourceProducer(child, this.sources, this.combinator));
        }
    }

    static final class MultiSourceProducer<T, R> implements Producer {
        private final Subscriber<? super R> child;
        private final Object[] collectedValues;
        private final FuncN<? extends R> combinator;
        private final BitSet completion;
        private volatile int completionCount;
        private final BitSet haveValues;
        private volatile int haveValuesCount;
        private final List<? extends Observable<? extends T>> sources;
        private final MultiSourceRequestableSubscriber<T, R>[] subscribers;
        private final AtomicBoolean started = new AtomicBoolean();
        private final AtomicLong requested = new AtomicLong();
        private final RxRingBuffer buffer = RxRingBuffer.getSpmcInstance();
        private final AtomicLong counter = new AtomicLong();

        public MultiSourceProducer(Subscriber<? super R> child, List<? extends Observable<? extends T>> sources, FuncN<? extends R> combinator) {
            this.sources = sources;
            this.child = child;
            this.combinator = combinator;
            int n = sources.size();
            this.subscribers = new MultiSourceRequestableSubscriber[n];
            this.collectedValues = new Object[n];
            this.haveValues = new BitSet(n);
            this.completion = new BitSet(n);
        }

        @Override // rx.Producer
        public void request(long n) {
            BackpressureUtils.getAndAddRequest(this.requested, n);
            if (!this.started.get() && this.started.compareAndSet(false, true)) {
                int sizePerSubscriber = RxRingBuffer.SIZE / this.sources.size();
                int leftOver = RxRingBuffer.SIZE % this.sources.size();
                for (int i = 0; i < this.sources.size(); i++) {
                    Observable<? extends T> o = this.sources.get(i);
                    int toRequest = sizePerSubscriber;
                    if (i == this.sources.size() - 1) {
                        toRequest += leftOver;
                    }
                    MultiSourceRequestableSubscriber<T, R> s = new MultiSourceRequestableSubscriber<>(i, toRequest, this.child, this);
                    this.subscribers[i] = s;
                    o.unsafeSubscribe(s);
                }
            }
            tick();
        }

        void tick() {
            Object o;
            AtomicLong localCounter = this.counter;
            if (localCounter.getAndIncrement() == 0) {
                int emitted = 0;
                do {
                    if (this.requested.get() > 0 && (o = this.buffer.poll()) != null) {
                        if (this.buffer.isCompleted(o)) {
                            this.child.onCompleted();
                        } else {
                            this.buffer.accept(o, this.child);
                            emitted++;
                            this.requested.decrementAndGet();
                        }
                    }
                } while (localCounter.decrementAndGet() > 0);
                if (emitted > 0) {
                    MultiSourceRequestableSubscriber<T, R>[] arr$ = this.subscribers;
                    for (MultiSourceRequestableSubscriber<T, R> s : arr$) {
                        s.requestUpTo(emitted);
                    }
                }
            }
        }

        public void onCompleted(int index, boolean hadValue) {
            if (!hadValue) {
                this.child.onCompleted();
                return;
            }
            boolean done = false;
            synchronized (this) {
                if (!this.completion.get(index)) {
                    this.completion.set(index);
                    this.completionCount++;
                    done = this.completionCount == this.collectedValues.length;
                }
            }
            if (done) {
                this.buffer.onCompleted();
                tick();
            }
        }

        public boolean onNext(int index, T t) {
            synchronized (this) {
                if (!this.haveValues.get(index)) {
                    this.haveValues.set(index);
                    this.haveValuesCount++;
                }
                this.collectedValues[index] = t;
                if (this.haveValuesCount != this.collectedValues.length) {
                    return false;
                }
                try {
                    this.buffer.onNext(this.combinator.call(this.collectedValues));
                } catch (MissingBackpressureException e) {
                    onError(e);
                } catch (Throwable e2) {
                    Exceptions.throwOrReport(e2, this.child);
                }
                tick();
                return true;
            }
        }

        public void onError(Throwable e) {
            this.child.onError(e);
        }
    }

    static final class MultiSourceRequestableSubscriber<T, R> extends Subscriber<T> {
        final AtomicLong emitted;
        boolean hasValue;
        final int index;
        final MultiSourceProducer<T, R> producer;

        public MultiSourceRequestableSubscriber(int index, int initial, Subscriber<? super R> child, MultiSourceProducer<T, R> producer) {
            super(child);
            this.emitted = new AtomicLong();
            this.hasValue = false;
            this.index = index;
            this.producer = producer;
            request(initial);
        }

        public void requestUpTo(long n) {
            long r;
            long u;
            do {
                r = this.emitted.get();
                u = Math.min(r, n);
            } while (!this.emitted.compareAndSet(r, r - u));
            request(u);
        }

        @Override // rx.Observer
        public void onCompleted() {
            this.producer.onCompleted(this.index, this.hasValue);
        }

        @Override // rx.Observer
        public void onError(Throwable e) {
            this.producer.onError(e);
        }

        @Override // rx.Observer
        public void onNext(T t) {
            this.hasValue = true;
            this.emitted.incrementAndGet();
            boolean emitted = this.producer.onNext(this.index, t);
            if (!emitted) {
                request(1L);
            }
        }
    }

    static final class SingleSourceProducer<T, R> implements Producer {
        final Subscriber<? super R> child;
        final FuncN<? extends R> combinator;
        final Observable<? extends T> source;
        final AtomicBoolean started = new AtomicBoolean();
        final SingleSourceRequestableSubscriber<T, R> subscriber;

        public SingleSourceProducer(Subscriber<? super R> child, Observable<? extends T> source, FuncN<? extends R> combinator) {
            this.source = source;
            this.child = child;
            this.combinator = combinator;
            this.subscriber = new SingleSourceRequestableSubscriber<>(child, combinator);
        }

        @Override // rx.Producer
        public void request(long n) {
            this.subscriber.requestMore(n);
            if (this.started.compareAndSet(false, true)) {
                this.source.unsafeSubscribe(this.subscriber);
            }
        }
    }

    static final class SingleSourceRequestableSubscriber<T, R> extends Subscriber<T> {
        private final Subscriber<? super R> child;
        private final FuncN<? extends R> combinator;

        SingleSourceRequestableSubscriber(Subscriber<? super R> child, FuncN<? extends R> combinator) {
            super(child);
            this.child = child;
            this.combinator = combinator;
        }

        public void requestMore(long n) {
            request(n);
        }

        @Override // rx.Observer
        public void onNext(T t) {
            this.child.onNext(this.combinator.call(t));
        }

        @Override // rx.Observer
        public void onError(Throwable e) {
            this.child.onError(e);
        }

        @Override // rx.Observer
        public void onCompleted() {
            this.child.onCompleted();
        }
    }
}
