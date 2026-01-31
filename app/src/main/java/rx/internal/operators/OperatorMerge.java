package rx.internal.operators;

import com.unisound.vui.priority.PriorityMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.CompositeException;
import rx.exceptions.Exceptions;
import rx.exceptions.MissingBackpressureException;
import rx.exceptions.OnErrorThrowable;
import rx.internal.util.RxRingBuffer;
import rx.internal.util.ScalarSynchronousObservable;
import rx.internal.util.atomic.SpscAtomicArrayQueue;
import rx.internal.util.atomic.SpscExactAtomicArrayQueue;
import rx.internal.util.atomic.SpscUnboundedAtomicArrayQueue;
import rx.internal.util.unsafe.Pow2;
import rx.internal.util.unsafe.SpscArrayQueue;
import rx.internal.util.unsafe.UnsafeAccess;
import rx.subscriptions.CompositeSubscription;

/* loaded from: classes.dex */
public final class OperatorMerge<T> implements Observable.Operator<T, Observable<? extends T>> {
    final boolean delayErrors;
    final int maxConcurrent;

    private static final class HolderNoDelay {
        static final OperatorMerge<Object> INSTANCE = new OperatorMerge<>(false, PriorityMap.PRIORITY_MAX);

        private HolderNoDelay() {
        }
    }

    private static final class HolderDelayErrors {
        static final OperatorMerge<Object> INSTANCE = new OperatorMerge<>(true, PriorityMap.PRIORITY_MAX);

        private HolderDelayErrors() {
        }
    }

    public static <T> OperatorMerge<T> instance(boolean z) {
        return z ? (OperatorMerge<T>) HolderDelayErrors.INSTANCE : (OperatorMerge<T>) HolderNoDelay.INSTANCE;
    }

    public static <T> OperatorMerge<T> instance(boolean delayErrors, int maxConcurrent) {
        return maxConcurrent == Integer.MAX_VALUE ? instance(delayErrors) : new OperatorMerge<>(delayErrors, maxConcurrent);
    }

    private OperatorMerge(boolean delayErrors, int maxConcurrent) {
        this.delayErrors = delayErrors;
        this.maxConcurrent = maxConcurrent;
    }

    @Override // rx.functions.Func1
    public Subscriber<Observable<? extends T>> call(Subscriber<? super T> child) {
        MergeSubscriber<T> subscriber = new MergeSubscriber<>(child, this.delayErrors, this.maxConcurrent);
        MergeProducer<T> producer = new MergeProducer<>(subscriber);
        subscriber.producer = producer;
        child.add(subscriber);
        child.setProducer(producer);
        return subscriber;
    }

    static final class MergeProducer<T> extends AtomicLong implements Producer {
        private static final long serialVersionUID = -1214379189873595503L;
        final MergeSubscriber<T> subscriber;

        public MergeProducer(MergeSubscriber<T> subscriber) {
            this.subscriber = subscriber;
        }

        @Override // rx.Producer
        public void request(long n) {
            if (n <= 0) {
                if (n < 0) {
                    throw new IllegalArgumentException("n >= 0 required");
                }
            } else if (get() != Long.MAX_VALUE) {
                BackpressureUtils.getAndAddRequest(this, n);
                this.subscriber.emit();
            }
        }

        public long produced(int n) {
            return addAndGet(-n);
        }
    }

    static final class MergeSubscriber<T> extends Subscriber<Observable<? extends T>> {
        static final InnerSubscriber<?>[] EMPTY = new InnerSubscriber[0];
        final Subscriber<? super T> child;
        final boolean delayErrors;
        volatile boolean done;
        boolean emitting;
        volatile ConcurrentLinkedQueue<Throwable> errors;
        long lastId;
        int lastIndex;
        final int maxConcurrent;
        boolean missed;
        MergeProducer<T> producer;
        volatile Queue<Object> queue;
        volatile CompositeSubscription subscriptions;
        long uniqueId;
        final NotificationLite<T> nl = NotificationLite.instance();
        final Object innerGuard = new Object();
        volatile InnerSubscriber<?>[] innerSubscribers = EMPTY;

        public MergeSubscriber(Subscriber<? super T> child, boolean delayErrors, int maxConcurrent) {
            this.child = child;
            this.delayErrors = delayErrors;
            this.maxConcurrent = maxConcurrent;
            request(maxConcurrent == Integer.MAX_VALUE ? Long.MAX_VALUE : maxConcurrent);
        }

        Queue<Throwable> getOrCreateErrorQueue() throws Throwable {
            ConcurrentLinkedQueue<Throwable> q = this.errors;
            if (q == null) {
                synchronized (this) {
                    try {
                        q = this.errors;
                        if (q == null) {
                            ConcurrentLinkedQueue<Throwable> q2 = new ConcurrentLinkedQueue<>();
                            try {
                                this.errors = q2;
                                q = q2;
                            } catch (Throwable th) {
                                th = th;
                                throw th;
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                    }
                }
            }
            return q;
        }

        CompositeSubscription getOrCreateComposite() throws Throwable {
            CompositeSubscription c = this.subscriptions;
            if (c == null) {
                boolean shouldAdd = false;
                synchronized (this) {
                    try {
                        c = this.subscriptions;
                        if (c == null) {
                            CompositeSubscription c2 = new CompositeSubscription();
                            try {
                                this.subscriptions = c2;
                                shouldAdd = true;
                                c = c2;
                            } catch (Throwable th) {
                                th = th;
                                throw th;
                            }
                        }
                        if (shouldAdd) {
                            add(c);
                        }
                    } catch (Throwable th2) {
                        th = th2;
                    }
                }
            }
            return c;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // rx.Observer
        public void onNext(Observable<? extends T> t) {
            if (t != null) {
                if (t instanceof ScalarSynchronousObservable) {
                    tryEmit(((ScalarSynchronousObservable) t).get());
                    return;
                }
                long j = this.uniqueId;
                this.uniqueId = 1 + j;
                InnerSubscriber<T> inner = new InnerSubscriber<>(this, j);
                addInner(inner);
                t.unsafeSubscribe(inner);
                emit();
            }
        }

        private void reportError() {
            List<Throwable> list = new ArrayList<>(this.errors);
            if (list.size() == 1) {
                this.child.onError(list.get(0));
            } else {
                this.child.onError(new CompositeException(list));
            }
        }

        @Override // rx.Observer
        public void onError(Throwable e) {
            getOrCreateErrorQueue().offer(e);
            this.done = true;
            emit();
        }

        @Override // rx.Observer
        public void onCompleted() {
            this.done = true;
            emit();
        }

        /* JADX WARN: Multi-variable type inference failed */
        void addInner(InnerSubscriber<T> inner) {
            getOrCreateComposite().add(inner);
            synchronized (this.innerGuard) {
                InnerSubscriber<?>[] a2 = this.innerSubscribers;
                int n = a2.length;
                InnerSubscriber<?>[] innerSubscriberArr = new InnerSubscriber[n + 1];
                System.arraycopy(a2, 0, innerSubscriberArr, 0, n);
                innerSubscriberArr[n] = inner;
                this.innerSubscribers = innerSubscriberArr;
            }
        }

        void removeInner(InnerSubscriber<T> inner) {
            RxRingBuffer q = inner.queue;
            if (q != null) {
                q.release();
            }
            this.subscriptions.remove(inner);
            synchronized (this.innerGuard) {
                InnerSubscriber<?>[] a2 = this.innerSubscribers;
                int n = a2.length;
                int j = -1;
                int i = 0;
                while (true) {
                    if (i >= n) {
                        break;
                    }
                    if (!inner.equals(a2[i])) {
                        i++;
                    } else {
                        j = i;
                        break;
                    }
                }
                if (j >= 0) {
                    if (n == 1) {
                        this.innerSubscribers = EMPTY;
                        return;
                    }
                    InnerSubscriber<?>[] b = new InnerSubscriber[n - 1];
                    System.arraycopy(a2, 0, b, 0, j);
                    System.arraycopy(a2, j + 1, b, j, (n - j) - 1);
                    this.innerSubscribers = b;
                }
            }
        }

        void tryEmit(InnerSubscriber<T> subscriber, T value) {
            boolean success = false;
            long r = this.producer.get();
            if (r != 0) {
                synchronized (this) {
                    r = this.producer.get();
                    if (!this.emitting && r != 0) {
                        this.emitting = true;
                        success = true;
                    }
                }
            }
            if (success) {
                emitScalar(subscriber, value, r);
            } else {
                queueScalar(subscriber, value);
            }
        }

        protected void queueScalar(InnerSubscriber<T> subscriber, T value) {
            RxRingBuffer q = subscriber.queue;
            if (q == null) {
                q = RxRingBuffer.getSpscInstance();
                subscriber.add(q);
                subscriber.queue = q;
            }
            try {
                q.onNext(this.nl.next(value));
                emit();
            } catch (IllegalStateException ex) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.unsubscribe();
                    subscriber.onError(ex);
                }
            } catch (MissingBackpressureException ex2) {
                subscriber.unsubscribe();
                subscriber.onError(ex2);
            }
        }

        protected void emitScalar(InnerSubscriber<T> subscriber, T value, long r) {
            boolean skipFinal = false;
            try {
                try {
                    this.child.onNext(value);
                } catch (Throwable t) {
                    if (!this.delayErrors) {
                        Exceptions.throwIfFatal(t);
                        subscriber.unsubscribe();
                        subscriber.onError(t);
                        if (1 == 0) {
                            synchronized (this) {
                                this.emitting = false;
                                return;
                            }
                        }
                        return;
                    }
                    getOrCreateErrorQueue().offer(t);
                }
                if (r != Long.MAX_VALUE) {
                    this.producer.produced(1);
                }
                subscriber.requestMore(1L);
                synchronized (this) {
                    skipFinal = true;
                    if (this.missed) {
                        this.missed = false;
                        if (1 == 0) {
                            synchronized (this) {
                                this.emitting = false;
                            }
                        }
                        emitLoop();
                    } else {
                        this.emitting = false;
                        if (1 == 0) {
                            synchronized (this) {
                                this.emitting = false;
                            }
                        }
                    }
                }
            } catch (Throwable th) {
                if (!skipFinal) {
                    synchronized (this) {
                        this.emitting = false;
                    }
                }
                throw th;
            }
        }

        public void requestMore(long n) {
            request(n);
        }

        void tryEmit(T value) {
            boolean success = false;
            long r = this.producer.get();
            if (r != 0) {
                synchronized (this) {
                    r = this.producer.get();
                    if (!this.emitting && r != 0) {
                        this.emitting = true;
                        success = true;
                    }
                }
            }
            if (success) {
                emitScalar(value, r);
            } else {
                queueScalar(value);
            }
        }

        protected void queueScalar(T value) {
            Queue<Object> q = this.queue;
            if (q == null) {
                int mc = this.maxConcurrent;
                if (mc == Integer.MAX_VALUE) {
                    q = new SpscUnboundedAtomicArrayQueue<>(RxRingBuffer.SIZE);
                } else if (Pow2.isPowerOfTwo(mc)) {
                    if (UnsafeAccess.isUnsafeAvailable()) {
                        q = new SpscArrayQueue<>(mc);
                    } else {
                        q = new SpscAtomicArrayQueue<>(mc);
                    }
                } else {
                    q = new SpscExactAtomicArrayQueue<>(mc);
                }
                this.queue = q;
            }
            if (!q.offer(value)) {
                unsubscribe();
                onError(OnErrorThrowable.addValueAsLastCause(new MissingBackpressureException(), value));
            } else {
                emit();
            }
        }

        protected void emitScalar(T value, long r) {
            boolean skipFinal = false;
            try {
                try {
                    this.child.onNext(value);
                } catch (Throwable t) {
                    if (!this.delayErrors) {
                        Exceptions.throwIfFatal(t);
                        unsubscribe();
                        onError(t);
                        if (1 == 0) {
                            synchronized (this) {
                                this.emitting = false;
                                return;
                            }
                        }
                        return;
                    }
                    getOrCreateErrorQueue().offer(t);
                }
                if (r != Long.MAX_VALUE) {
                    this.producer.produced(1);
                }
                requestMore(1L);
                synchronized (this) {
                    skipFinal = true;
                    if (this.missed) {
                        this.missed = false;
                        if (1 == 0) {
                            synchronized (this) {
                                this.emitting = false;
                            }
                        }
                        emitLoop();
                    } else {
                        this.emitting = false;
                        if (1 == 0) {
                            synchronized (this) {
                                this.emitting = false;
                            }
                        }
                    }
                }
            } catch (Throwable th) {
                if (!skipFinal) {
                    synchronized (this) {
                        this.emitting = false;
                    }
                }
                throw th;
            }
        }

        void emit() {
            synchronized (this) {
                if (this.emitting) {
                    this.missed = true;
                } else {
                    this.emitting = true;
                    emitLoop();
                }
            }
        }

        /* JADX WARN: Finally extract failed */
        void emitLoop() {
            Object objPoll;
            boolean z = false;
            try {
                Subscriber<? super T> subscriber = this.child;
                while (!checkTerminate()) {
                    Queue<Object> queue = this.queue;
                    long jProduced = this.producer.get();
                    boolean z2 = jProduced == Long.MAX_VALUE;
                    int i = 0;
                    if (queue != null) {
                        do {
                            int i2 = 0;
                            objPoll = null;
                            while (jProduced > 0) {
                                objPoll = queue.poll();
                                if (checkTerminate()) {
                                    if (1 == 0) {
                                        synchronized (this) {
                                            this.emitting = false;
                                        }
                                        return;
                                    }
                                    return;
                                }
                                if (objPoll == null) {
                                    break;
                                }
                                try {
                                    subscriber.onNext(this.nl.getValue(objPoll));
                                } catch (Throwable th) {
                                    if (!this.delayErrors) {
                                        Exceptions.throwIfFatal(th);
                                        unsubscribe();
                                        subscriber.onError(th);
                                        if (1 == 0) {
                                            synchronized (this) {
                                                this.emitting = false;
                                                return;
                                            }
                                        }
                                        return;
                                    }
                                    getOrCreateErrorQueue().offer(th);
                                }
                                i++;
                                i2++;
                                jProduced--;
                            }
                            if (i2 > 0) {
                                jProduced = z2 ? Long.MAX_VALUE : this.producer.produced(i2);
                            }
                            if (jProduced == 0) {
                                break;
                            }
                        } while (objPoll != null);
                    }
                    boolean z3 = this.done;
                    Queue<Object> queue2 = this.queue;
                    InnerSubscriber[] innerSubscriberArr = this.innerSubscribers;
                    int length = innerSubscriberArr.length;
                    if (z3 && ((queue2 == null || queue2.isEmpty()) && length == 0)) {
                        ConcurrentLinkedQueue<Throwable> concurrentLinkedQueue = this.errors;
                        if (concurrentLinkedQueue == null || concurrentLinkedQueue.isEmpty()) {
                            subscriber.onCompleted();
                        } else {
                            reportError();
                        }
                        if (1 == 0) {
                            synchronized (this) {
                                this.emitting = false;
                            }
                            return;
                        }
                        return;
                    }
                    boolean z4 = false;
                    if (length > 0) {
                        long j = this.lastId;
                        int i3 = this.lastIndex;
                        if (length <= i3 || innerSubscriberArr[i3].id != j) {
                            if (length <= i3) {
                                i3 = 0;
                            }
                            int i4 = i3;
                            for (int i5 = 0; i5 < length && innerSubscriberArr[i4].id != j; i5++) {
                                i4++;
                                if (i4 == length) {
                                    i4 = 0;
                                }
                            }
                            i3 = i4;
                            this.lastIndex = i4;
                            this.lastId = innerSubscriberArr[i4].id;
                        }
                        int i6 = i3;
                        for (int i7 = 0; i7 < length; i7++) {
                            if (checkTerminate()) {
                                if (1 == 0) {
                                    synchronized (this) {
                                        this.emitting = false;
                                    }
                                    return;
                                }
                                return;
                            }
                            InnerSubscriber innerSubscriber = innerSubscriberArr[i6];
                            Object objPoll2 = null;
                            do {
                                int i8 = 0;
                                while (jProduced > 0) {
                                    if (checkTerminate()) {
                                        if (1 == 0) {
                                            synchronized (this) {
                                                this.emitting = false;
                                            }
                                            return;
                                        }
                                        return;
                                    }
                                    RxRingBuffer rxRingBuffer = innerSubscriber.queue;
                                    if (rxRingBuffer == null || (objPoll2 = rxRingBuffer.poll()) == null) {
                                        break;
                                    }
                                    try {
                                        subscriber.onNext(this.nl.getValue(objPoll2));
                                        jProduced--;
                                        i8++;
                                    } catch (Throwable th2) {
                                        z = true;
                                        Exceptions.throwIfFatal(th2);
                                        try {
                                            subscriber.onError(th2);
                                            unsubscribe();
                                            if (1 == 0) {
                                                synchronized (this) {
                                                    this.emitting = false;
                                                    return;
                                                }
                                            }
                                            return;
                                        } catch (Throwable th3) {
                                            unsubscribe();
                                            throw th3;
                                        }
                                    }
                                }
                                if (i8 > 0) {
                                    jProduced = !z2 ? this.producer.produced(i8) : Long.MAX_VALUE;
                                    innerSubscriber.requestMore(i8);
                                }
                                if (jProduced == 0) {
                                    break;
                                }
                            } while (objPoll2 != null);
                            boolean z5 = innerSubscriber.done;
                            RxRingBuffer rxRingBuffer2 = innerSubscriber.queue;
                            if (z5 && (rxRingBuffer2 == null || rxRingBuffer2.isEmpty())) {
                                removeInner(innerSubscriber);
                                if (checkTerminate()) {
                                    if (1 == 0) {
                                        synchronized (this) {
                                            this.emitting = false;
                                        }
                                        return;
                                    }
                                    return;
                                }
                                i++;
                                z4 = true;
                            }
                            if (jProduced == 0) {
                                break;
                            }
                            i6++;
                            if (i6 == length) {
                                i6 = 0;
                            }
                        }
                        this.lastIndex = i6;
                        this.lastId = innerSubscriberArr[i6].id;
                    }
                    if (i > 0) {
                        request(i);
                    }
                    if (!z4) {
                        synchronized (this) {
                            if (this.missed) {
                                this.missed = false;
                            } else {
                                z = true;
                                this.emitting = false;
                            }
                        }
                        if (1 == 0) {
                            synchronized (this) {
                                this.emitting = false;
                            }
                            return;
                        }
                        return;
                    }
                }
                if (1 == 0) {
                    synchronized (this) {
                        this.emitting = false;
                    }
                }
            } catch (Throwable th4) {
                if (!z) {
                    synchronized (this) {
                        this.emitting = false;
                    }
                }
                throw th4;
            }
        }

        boolean checkTerminate() {
            if (this.child.isUnsubscribed()) {
                return true;
            }
            Queue<Throwable> e = this.errors;
            if (!this.delayErrors && e != null && !e.isEmpty()) {
                try {
                    reportError();
                    return true;
                } finally {
                    unsubscribe();
                }
            }
            return false;
        }
    }

    static final class InnerSubscriber<T> extends Subscriber<T> {
        static final int limit = RxRingBuffer.SIZE / 4;
        volatile boolean done;
        final long id;
        int outstanding;
        final MergeSubscriber<T> parent;
        volatile RxRingBuffer queue;

        public InnerSubscriber(MergeSubscriber<T> parent, long id) {
            this.parent = parent;
            this.id = id;
        }

        @Override // rx.Subscriber
        public void onStart() {
            this.outstanding = RxRingBuffer.SIZE;
            request(RxRingBuffer.SIZE);
        }

        @Override // rx.Observer
        public void onNext(T t) {
            this.parent.tryEmit(this, t);
        }

        @Override // rx.Observer
        public void onError(Throwable e) {
            this.done = true;
            this.parent.getOrCreateErrorQueue().offer(e);
            this.parent.emit();
        }

        @Override // rx.Observer
        public void onCompleted() {
            this.done = true;
            this.parent.emit();
        }

        public void requestMore(long n) {
            int r = this.outstanding - ((int) n);
            if (r > limit) {
                this.outstanding = r;
                return;
            }
            this.outstanding = RxRingBuffer.SIZE;
            int k = RxRingBuffer.SIZE - r;
            if (k > 0) {
                request(k);
            }
        }
    }
}
