package rx.internal.operators;

import com.unisound.vui.priority.PriorityMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import rx.Observable;
import rx.Producer;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.Exceptions;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.observables.ConnectableObservable;
import rx.schedulers.Timestamped;
import rx.subscriptions.Subscriptions;

/* loaded from: classes.dex */
public final class OperatorReplay<T> extends ConnectableObservable<T> {
    static final Func0 DEFAULT_UNBOUNDED_FACTORY = new Func0() { // from class: rx.internal.operators.OperatorReplay.1
        @Override // rx.functions.Func0, java.util.concurrent.Callable
        public Object call() {
            return new UnboundedReplayBuffer(16);
        }
    };
    final Func0<? extends ReplayBuffer<T>> bufferFactory;
    final AtomicReference<ReplaySubscriber<T>> current;
    final Observable<? extends T> source;

    interface ReplayBuffer<T> {
        void complete();

        void error(Throwable th);

        void next(T t);

        void replay(InnerProducer<T> innerProducer);
    }

    public static <T, U, R> Observable<R> multicastSelector(final Func0<? extends ConnectableObservable<U>> connectableFactory, final Func1<? super Observable<U>, ? extends Observable<R>> selector) {
        return Observable.create(new Observable.OnSubscribe<R>() { // from class: rx.internal.operators.OperatorReplay.2
            @Override // rx.functions.Action1
            public void call(final Subscriber<? super R> child) {
                try {
                    ConnectableObservable connectableObservable = (ConnectableObservable) connectableFactory.call();
                    Observable<R> observable = (Observable) selector.call(connectableObservable);
                    observable.subscribe(child);
                    connectableObservable.connect(new Action1<Subscription>() { // from class: rx.internal.operators.OperatorReplay.2.1
                        @Override // rx.functions.Action1
                        public void call(Subscription t) {
                            child.add(t);
                        }
                    });
                } catch (Throwable e) {
                    Exceptions.throwOrReport(e, child);
                }
            }
        });
    }

    public static <T> ConnectableObservable<T> observeOn(final ConnectableObservable<T> co, Scheduler scheduler) {
        final Observable<T> observable = co.observeOn(scheduler);
        Observable.OnSubscribe<T> onSubscribe = new Observable.OnSubscribe<T>() { // from class: rx.internal.operators.OperatorReplay.3
            @Override // rx.functions.Action1
            public void call(final Subscriber<? super T> child) {
                observable.unsafeSubscribe(new Subscriber<T>(child) { // from class: rx.internal.operators.OperatorReplay.3.1
                    @Override // rx.Observer
                    public void onNext(T t) {
                        child.onNext(t);
                    }

                    @Override // rx.Observer
                    public void onError(Throwable e) {
                        child.onError(e);
                    }

                    @Override // rx.Observer
                    public void onCompleted() {
                        child.onCompleted();
                    }
                });
            }
        };
        return new ConnectableObservable<T>(onSubscribe) { // from class: rx.internal.operators.OperatorReplay.4
            @Override // rx.observables.ConnectableObservable
            public void connect(Action1<? super Subscription> connection) {
                co.connect(connection);
            }
        };
    }

    public static <T> ConnectableObservable<T> create(Observable<? extends T> source) {
        return create(source, DEFAULT_UNBOUNDED_FACTORY);
    }

    public static <T> ConnectableObservable<T> create(Observable<? extends T> source, final int bufferSize) {
        return bufferSize == Integer.MAX_VALUE ? create(source) : create(source, new Func0<ReplayBuffer<T>>() { // from class: rx.internal.operators.OperatorReplay.5
            @Override // rx.functions.Func0, java.util.concurrent.Callable
            public ReplayBuffer<T> call() {
                return new SizeBoundReplayBuffer(bufferSize);
            }
        });
    }

    public static <T> ConnectableObservable<T> create(Observable<? extends T> source, long maxAge, TimeUnit unit, Scheduler scheduler) {
        return create(source, maxAge, unit, scheduler, PriorityMap.PRIORITY_MAX);
    }

    public static <T> ConnectableObservable<T> create(Observable<? extends T> source, long maxAge, TimeUnit unit, final Scheduler scheduler, final int bufferSize) {
        final long maxAgeInMillis = unit.toMillis(maxAge);
        return create(source, new Func0<ReplayBuffer<T>>() { // from class: rx.internal.operators.OperatorReplay.6
            @Override // rx.functions.Func0, java.util.concurrent.Callable
            public ReplayBuffer<T> call() {
                return new SizeAndTimeBoundReplayBuffer(bufferSize, maxAgeInMillis, scheduler);
            }
        });
    }

    static <T> ConnectableObservable<T> create(Observable<? extends T> source, final Func0<? extends ReplayBuffer<T>> bufferFactory) {
        final AtomicReference<ReplaySubscriber<T>> curr = new AtomicReference<>();
        Observable.OnSubscribe<T> onSubscribe = new Observable.OnSubscribe<T>() { // from class: rx.internal.operators.OperatorReplay.7
            @Override // rx.functions.Action1
            public void call(Subscriber<? super T> child) {
                ReplaySubscriber<T> r;
                while (true) {
                    r = (ReplaySubscriber) curr.get();
                    if (r != null) {
                        break;
                    }
                    ReplaySubscriber<T> u = new ReplaySubscriber<>(curr, (ReplayBuffer) bufferFactory.call());
                    u.init();
                    if (curr.compareAndSet(r, u)) {
                        r = u;
                        break;
                    }
                }
                InnerProducer<T> inner = new InnerProducer<>(r, child);
                r.add((InnerProducer) inner);
                child.add(inner);
                child.setProducer(inner);
            }
        };
        return new OperatorReplay(onSubscribe, source, curr, bufferFactory);
    }

    private OperatorReplay(Observable.OnSubscribe<T> onSubscribe, Observable<? extends T> source, AtomicReference<ReplaySubscriber<T>> current, Func0<? extends ReplayBuffer<T>> bufferFactory) {
        super(onSubscribe);
        this.source = source;
        this.current = current;
        this.bufferFactory = bufferFactory;
    }

    @Override // rx.observables.ConnectableObservable
    public void connect(Action1<? super Subscription> connection) {
        ReplaySubscriber<T> ps;
        while (true) {
            ps = this.current.get();
            if (ps != null && !ps.isUnsubscribed()) {
                break;
            }
            ReplaySubscriber<T> u = new ReplaySubscriber<>(this.current, this.bufferFactory.call());
            u.init();
            if (this.current.compareAndSet(ps, u)) {
                ps = u;
                break;
            }
        }
        boolean doConnect = !ps.shouldConnect.get() && ps.shouldConnect.compareAndSet(false, true);
        connection.call(ps);
        if (doConnect) {
            this.source.unsafeSubscribe(ps);
        }
    }

    static final class ReplaySubscriber<T> extends Subscriber<T> implements Subscription {
        static final InnerProducer[] EMPTY = new InnerProducer[0];
        static final InnerProducer[] TERMINATED = new InnerProducer[0];
        final ReplayBuffer<T> buffer;
        boolean done;
        boolean emitting;
        long maxChildRequested;
        long maxUpstreamRequested;
        boolean missed;
        volatile Producer producer;
        final NotificationLite<T> nl = NotificationLite.instance();
        final AtomicReference<InnerProducer[]> producers = new AtomicReference<>(EMPTY);
        final AtomicBoolean shouldConnect = new AtomicBoolean();

        public ReplaySubscriber(AtomicReference<ReplaySubscriber<T>> current, ReplayBuffer<T> buffer) {
            this.buffer = buffer;
            request(0L);
        }

        void init() {
            add(Subscriptions.create(new Action0() { // from class: rx.internal.operators.OperatorReplay.ReplaySubscriber.1
                @Override // rx.functions.Action0
                public void call() {
                    ReplaySubscriber.this.producers.getAndSet(ReplaySubscriber.TERMINATED);
                }
            }));
        }

        boolean add(InnerProducer<T> producer) {
            InnerProducer[] c;
            InnerProducer[] u;
            if (producer == null) {
                throw new NullPointerException();
            }
            do {
                c = this.producers.get();
                if (c == TERMINATED) {
                    return false;
                }
                int len = c.length;
                u = new InnerProducer[len + 1];
                System.arraycopy(c, 0, u, 0, len);
                u[len] = producer;
            } while (!this.producers.compareAndSet(c, u));
            return true;
        }

        void remove(InnerProducer<T> producer) {
            InnerProducer[] c;
            InnerProducer[] u;
            do {
                c = this.producers.get();
                if (c != EMPTY && c != TERMINATED) {
                    int j = -1;
                    int len = c.length;
                    int i = 0;
                    while (true) {
                        if (i >= len) {
                            break;
                        }
                        if (!c[i].equals(producer)) {
                            i++;
                        } else {
                            j = i;
                            break;
                        }
                    }
                    if (j >= 0) {
                        if (len == 1) {
                            u = EMPTY;
                        } else {
                            u = new InnerProducer[len - 1];
                            System.arraycopy(c, 0, u, 0, j);
                            System.arraycopy(c, j + 1, u, j, (len - j) - 1);
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } while (!this.producers.compareAndSet(c, u));
        }

        @Override // rx.Subscriber
        public void setProducer(Producer p) {
            Producer p0 = this.producer;
            if (p0 != null) {
                throw new IllegalStateException("Only a single producer can be set on a Subscriber.");
            }
            this.producer = p;
            manageRequests();
            replay();
        }

        @Override // rx.Observer
        public void onNext(T t) {
            if (!this.done) {
                this.buffer.next(t);
                replay();
            }
        }

        @Override // rx.Observer
        public void onError(Throwable e) {
            if (!this.done) {
                this.done = true;
                try {
                    this.buffer.error(e);
                    replay();
                } finally {
                    unsubscribe();
                }
            }
        }

        @Override // rx.Observer
        public void onCompleted() {
            if (!this.done) {
                this.done = true;
                try {
                    this.buffer.complete();
                    replay();
                } finally {
                    unsubscribe();
                }
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:32:0x008b, code lost:
        
            r22.emitting = false;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        void manageRequests() {
            /*
                Method dump skipped, instructions count: 210
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorReplay.ReplaySubscriber.manageRequests():void");
        }

        void replay() {
            InnerProducer<T>[] a2 = this.producers.get();
            for (InnerProducer<T> rp : a2) {
                this.buffer.replay(rp);
            }
        }
    }

    static final class InnerProducer<T> extends AtomicLong implements Producer, Subscription {
        static final long UNSUBSCRIBED = Long.MIN_VALUE;
        private static final long serialVersionUID = -4453897557930727610L;
        final Subscriber<? super T> child;
        boolean emitting;
        Object index;
        boolean missed;
        final ReplaySubscriber<T> parent;
        final AtomicLong totalRequested = new AtomicLong();

        public InnerProducer(ReplaySubscriber<T> parent, Subscriber<? super T> child) {
            this.parent = parent;
            this.child = child;
        }

        @Override // rx.Producer
        public void request(long n) {
            long r;
            long u;
            if (n >= 0) {
                do {
                    r = get();
                    if (r == Long.MIN_VALUE) {
                        return;
                    }
                    if (r < 0 || n != 0) {
                        u = r + n;
                        if (u < 0) {
                            u = Long.MAX_VALUE;
                        }
                    } else {
                        return;
                    }
                } while (!compareAndSet(r, u));
                addTotalRequested(n);
                this.parent.manageRequests();
                this.parent.buffer.replay(this);
            }
        }

        void addTotalRequested(long n) {
            long r;
            long u;
            do {
                r = this.totalRequested.get();
                u = r + n;
                if (u < 0) {
                    u = Long.MAX_VALUE;
                }
            } while (!this.totalRequested.compareAndSet(r, u));
        }

        public long produced(long n) {
            long r;
            long u;
            if (n <= 0) {
                throw new IllegalArgumentException("Cant produce zero or less");
            }
            do {
                r = get();
                if (r == Long.MIN_VALUE) {
                    return Long.MIN_VALUE;
                }
                u = r - n;
                if (u < 0) {
                    throw new IllegalStateException("More produced (" + n + ") than requested (" + r + ")");
                }
            } while (!compareAndSet(r, u));
            return u;
        }

        @Override // rx.Subscription
        public boolean isUnsubscribed() {
            return get() == Long.MIN_VALUE;
        }

        @Override // rx.Subscription
        public void unsubscribe() {
            long r = get();
            if (r != Long.MIN_VALUE) {
                long r2 = getAndSet(Long.MIN_VALUE);
                if (r2 != Long.MIN_VALUE) {
                    this.parent.remove(this);
                    this.parent.manageRequests();
                }
            }
        }

        <U> U index() {
            return (U) this.index;
        }
    }

    static final class UnboundedReplayBuffer<T> extends ArrayList<Object> implements ReplayBuffer<T> {
        private static final long serialVersionUID = 7063189396499112664L;
        final NotificationLite<T> nl;
        volatile int size;

        public UnboundedReplayBuffer(int capacityHint) {
            super(capacityHint);
            this.nl = NotificationLite.instance();
        }

        @Override // rx.internal.operators.OperatorReplay.ReplayBuffer
        public void next(T value) {
            add(this.nl.next(value));
            this.size++;
        }

        @Override // rx.internal.operators.OperatorReplay.ReplayBuffer
        public void error(Throwable e) {
            add(this.nl.error(e));
            this.size++;
        }

        @Override // rx.internal.operators.OperatorReplay.ReplayBuffer
        public void complete() {
            add(this.nl.completed());
            this.size++;
        }

        @Override // rx.internal.operators.OperatorReplay.ReplayBuffer
        public void replay(InnerProducer<T> output) {
            synchronized (output) {
                if (output.emitting) {
                    output.missed = true;
                    return;
                }
                output.emitting = true;
                while (!output.isUnsubscribed()) {
                    int sourceIndex = this.size;
                    Integer destIndexObject = (Integer) output.index();
                    int destIndex = destIndexObject != null ? destIndexObject.intValue() : 0;
                    long r = output.get();
                    long e = 0;
                    while (r != 0 && destIndex < sourceIndex) {
                        Object o = get(destIndex);
                        try {
                            if (!this.nl.accept(output.child, o) && !output.isUnsubscribed()) {
                                destIndex++;
                                r--;
                                e++;
                            } else {
                                return;
                            }
                        } catch (Throwable err) {
                            Exceptions.throwIfFatal(err);
                            output.unsubscribe();
                            if (!this.nl.isError(o) && !this.nl.isCompleted(o)) {
                                output.child.onError(OnErrorThrowable.addValueAsLastCause(err, this.nl.getValue(o)));
                                return;
                            }
                            return;
                        }
                    }
                    if (e != 0) {
                        output.index = Integer.valueOf(destIndex);
                        if (r != Long.MAX_VALUE) {
                            output.produced(e);
                        }
                    }
                    synchronized (output) {
                        if (!output.missed) {
                            output.emitting = false;
                            return;
                        }
                        output.missed = false;
                    }
                }
            }
        }
    }

    static final class Node extends AtomicReference<Node> {
        private static final long serialVersionUID = 245354315435971818L;
        final Object value;

        public Node(Object value) {
            this.value = value;
        }
    }

    static class BoundedReplayBuffer<T> extends AtomicReference<Node> implements ReplayBuffer<T> {
        private static final long serialVersionUID = 2346567790059478686L;
        final NotificationLite<T> nl = NotificationLite.instance();
        int size;
        Node tail;

        public BoundedReplayBuffer() {
            Node n = new Node(null);
            this.tail = n;
            set(n);
        }

        final void addLast(Node n) {
            this.tail.set(n);
            this.tail = n;
            this.size++;
        }

        final void removeFirst() {
            Node head = get();
            Node next = head.get();
            if (next == null) {
                throw new IllegalStateException("Empty list!");
            }
            this.size--;
            setFirst(next);
        }

        final void removeSome(int n) {
            Node head = get();
            while (n > 0) {
                head = head.get();
                n--;
                this.size--;
            }
            setFirst(head);
        }

        final void setFirst(Node n) {
            set(n);
        }

        @Override // rx.internal.operators.OperatorReplay.ReplayBuffer
        public final void next(T value) {
            Object o = enterTransform(this.nl.next(value));
            Node n = new Node(o);
            addLast(n);
            truncate();
        }

        @Override // rx.internal.operators.OperatorReplay.ReplayBuffer
        public final void error(Throwable e) {
            Object o = enterTransform(this.nl.error(e));
            Node n = new Node(o);
            addLast(n);
            truncateFinal();
        }

        @Override // rx.internal.operators.OperatorReplay.ReplayBuffer
        public final void complete() {
            Object o = enterTransform(this.nl.completed());
            Node n = new Node(o);
            addLast(n);
            truncateFinal();
        }

        @Override // rx.internal.operators.OperatorReplay.ReplayBuffer
        public final void replay(InnerProducer<T> output) {
            Node v;
            synchronized (output) {
                if (output.emitting) {
                    output.missed = true;
                    return;
                }
                output.emitting = true;
                while (!output.isUnsubscribed()) {
                    long r = output.get();
                    long e = 0;
                    Node node = (Node) output.index();
                    if (node == null) {
                        node = get();
                        output.index = node;
                    }
                    while (r != 0 && (v = node.get()) != null) {
                        Object o = leaveTransform(v.value);
                        try {
                            if (this.nl.accept(output.child, o)) {
                                output.index = null;
                                return;
                            }
                            e++;
                            node = v;
                            if (output.isUnsubscribed()) {
                                return;
                            }
                        } catch (Throwable err) {
                            output.index = null;
                            Exceptions.throwIfFatal(err);
                            output.unsubscribe();
                            if (!this.nl.isError(o) && !this.nl.isCompleted(o)) {
                                output.child.onError(OnErrorThrowable.addValueAsLastCause(err, this.nl.getValue(o)));
                                return;
                            }
                            return;
                        }
                    }
                    if (e != 0) {
                        output.index = node;
                        if (r != Long.MAX_VALUE) {
                            output.produced(e);
                        }
                    }
                    synchronized (output) {
                        if (!output.missed) {
                            output.emitting = false;
                            return;
                        }
                        output.missed = false;
                    }
                }
            }
        }

        Object enterTransform(Object value) {
            return value;
        }

        Object leaveTransform(Object value) {
            return value;
        }

        void truncate() {
        }

        void truncateFinal() {
        }

        final void collect(Collection<? super T> collection) {
            Node node = get();
            while (true) {
                Node node2 = node.get();
                if (node2 != null) {
                    Object objLeaveTransform = leaveTransform(node2.value);
                    if (!this.nl.isCompleted(objLeaveTransform) && !this.nl.isError(objLeaveTransform)) {
                        collection.add(this.nl.getValue(objLeaveTransform));
                        node = node2;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
        }

        boolean hasError() {
            return this.tail.value != null && this.nl.isError(leaveTransform(this.tail.value));
        }

        boolean hasCompleted() {
            return this.tail.value != null && this.nl.isCompleted(leaveTransform(this.tail.value));
        }
    }

    static final class SizeBoundReplayBuffer<T> extends BoundedReplayBuffer<T> {
        private static final long serialVersionUID = -5898283885385201806L;
        final int limit;

        public SizeBoundReplayBuffer(int limit) {
            this.limit = limit;
        }

        @Override // rx.internal.operators.OperatorReplay.BoundedReplayBuffer
        void truncate() {
            if (this.size > this.limit) {
                removeFirst();
            }
        }
    }

    static final class SizeAndTimeBoundReplayBuffer<T> extends BoundedReplayBuffer<T> {
        private static final long serialVersionUID = 3457957419649567404L;
        final int limit;
        final long maxAgeInMillis;
        final Scheduler scheduler;

        public SizeAndTimeBoundReplayBuffer(int limit, long maxAgeInMillis, Scheduler scheduler) {
            this.scheduler = scheduler;
            this.limit = limit;
            this.maxAgeInMillis = maxAgeInMillis;
        }

        @Override // rx.internal.operators.OperatorReplay.BoundedReplayBuffer
        Object enterTransform(Object value) {
            return new Timestamped(this.scheduler.now(), value);
        }

        @Override // rx.internal.operators.OperatorReplay.BoundedReplayBuffer
        Object leaveTransform(Object value) {
            return ((Timestamped) value).getValue();
        }

        @Override // rx.internal.operators.OperatorReplay.BoundedReplayBuffer
        void truncate() {
            long timeLimit = this.scheduler.now() - this.maxAgeInMillis;
            Node prev = (Node) get();
            Node next = prev.get();
            int e = 0;
            while (next != null) {
                if (this.size > this.limit) {
                    e++;
                    this.size--;
                    prev = next;
                    next = next.get();
                } else {
                    Timestamped<?> v = (Timestamped) next.value;
                    if (v.getTimestampMillis() > timeLimit) {
                        break;
                    }
                    e++;
                    this.size--;
                    prev = next;
                    next = next.get();
                }
            }
            if (e != 0) {
                setFirst(prev);
            }
        }

        @Override // rx.internal.operators.OperatorReplay.BoundedReplayBuffer
        void truncateFinal() {
            long timeLimit = this.scheduler.now() - this.maxAgeInMillis;
            Node prev = (Node) get();
            int e = 0;
            for (Node next = prev.get(); next != null && this.size > 1; next = next.get()) {
                Timestamped<?> v = (Timestamped) next.value;
                if (v.getTimestampMillis() > timeLimit) {
                    break;
                }
                e++;
                this.size--;
                prev = next;
            }
            if (e != 0) {
                setFirst(prev);
            }
        }
    }
}
