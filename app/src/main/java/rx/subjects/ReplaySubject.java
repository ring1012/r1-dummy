package rx.subjects;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.annotations.Beta;
import rx.exceptions.Exceptions;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.internal.operators.NotificationLite;
import rx.internal.util.UtilityFunctions;
import rx.schedulers.Timestamped;
import rx.subjects.SubjectSubscriptionManager;

/* loaded from: classes.dex */
public final class ReplaySubject<T> extends Subject<T, T> {
    private static final Object[] EMPTY_ARRAY = new Object[0];
    final SubjectSubscriptionManager<T> ssm;
    final ReplayState<T, ?> state;

    interface EvictionPolicy {
        void evict(NodeList<Object> nodeList);

        void evictFinal(NodeList<Object> nodeList);

        boolean test(Object obj, long j);
    }

    interface ReplayState<T, I> {
        void complete();

        void error(Throwable th);

        boolean isEmpty();

        T latest();

        void next(T t);

        boolean replayObserver(SubjectSubscriptionManager.SubjectObserver<? super T> subjectObserver);

        I replayObserverFromIndex(I i, SubjectSubscriptionManager.SubjectObserver<? super T> subjectObserver);

        I replayObserverFromIndexTest(I i, SubjectSubscriptionManager.SubjectObserver<? super T> subjectObserver, long j);

        int size();

        boolean terminated();

        T[] toArray(T[] tArr);
    }

    public static <T> ReplaySubject<T> create() {
        return create(16);
    }

    public static <T> ReplaySubject<T> create(int capacity) {
        final UnboundedReplayState<T> state = new UnboundedReplayState<>(capacity);
        SubjectSubscriptionManager<T> ssm = new SubjectSubscriptionManager<>();
        ssm.onStart = new Action1<SubjectSubscriptionManager.SubjectObserver<T>>() { // from class: rx.subjects.ReplaySubject.1
            @Override // rx.functions.Action1
            public void call(SubjectSubscriptionManager.SubjectObserver<T> o) {
                int lastIndex = state.replayObserverFromIndex((Integer) 0, (SubjectSubscriptionManager.SubjectObserver) o).intValue();
                o.index(Integer.valueOf(lastIndex));
            }
        };
        ssm.onAdded = new Action1<SubjectSubscriptionManager.SubjectObserver<T>>() { // from class: rx.subjects.ReplaySubject.2
            /* JADX WARN: Removed duplicated region for block: B:22:0x003e  */
            /* JADX WARN: Removed duplicated region for block: B:58:? A[RETURN, SYNTHETIC] */
            @Override // rx.functions.Action1
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public void call(rx.subjects.SubjectSubscriptionManager.SubjectObserver<T> r8) {
                /*
                    r7 = this;
                    monitor-enter(r8)
                    boolean r5 = r8.first     // Catch: java.lang.Throwable -> L47
                    if (r5 == 0) goto L9
                    boolean r5 = r8.emitting     // Catch: java.lang.Throwable -> L47
                    if (r5 == 0) goto Lb
                L9:
                    monitor-exit(r8)     // Catch: java.lang.Throwable -> L47
                La:
                    return
                Lb:
                    r5 = 0
                    r8.first = r5     // Catch: java.lang.Throwable -> L47
                    r5 = 1
                    r8.emitting = r5     // Catch: java.lang.Throwable -> L47
                    monitor-exit(r8)     // Catch: java.lang.Throwable -> L47
                    r4 = 0
                    rx.subjects.ReplaySubject$UnboundedReplayState r2 = r1     // Catch: java.lang.Throwable -> L4f
                L15:
                    java.lang.Object r5 = r8.index()     // Catch: java.lang.Throwable -> L4f
                    java.lang.Integer r5 = (java.lang.Integer) r5     // Catch: java.lang.Throwable -> L4f
                    int r0 = r5.intValue()     // Catch: java.lang.Throwable -> L4f
                    int r3 = r2.get()     // Catch: java.lang.Throwable -> L4f
                    if (r0 == r3) goto L30
                    java.lang.Integer r5 = java.lang.Integer.valueOf(r0)     // Catch: java.lang.Throwable -> L4f
                    java.lang.Integer r1 = r2.replayObserverFromIndex(r5, r8)     // Catch: java.lang.Throwable -> L4f
                    r8.index(r1)     // Catch: java.lang.Throwable -> L4f
                L30:
                    monitor-enter(r8)     // Catch: java.lang.Throwable -> L4f
                    int r5 = r2.get()     // Catch: java.lang.Throwable -> L4c
                    if (r3 != r5) goto L4a
                    r5 = 0
                    r8.emitting = r5     // Catch: java.lang.Throwable -> L4c
                    r4 = 1
                    monitor-exit(r8)     // Catch: java.lang.Throwable -> L4c
                    if (r4 != 0) goto La
                    monitor-enter(r8)
                    r5 = 0
                    r8.emitting = r5     // Catch: java.lang.Throwable -> L44
                    monitor-exit(r8)     // Catch: java.lang.Throwable -> L44
                    goto La
                L44:
                    r5 = move-exception
                    monitor-exit(r8)     // Catch: java.lang.Throwable -> L44
                    throw r5
                L47:
                    r5 = move-exception
                    monitor-exit(r8)     // Catch: java.lang.Throwable -> L47
                    throw r5
                L4a:
                    monitor-exit(r8)     // Catch: java.lang.Throwable -> L4c
                    goto L15
                L4c:
                    r5 = move-exception
                    monitor-exit(r8)     // Catch: java.lang.Throwable -> L4c
                    throw r5     // Catch: java.lang.Throwable -> L4f
                L4f:
                    r5 = move-exception
                    if (r4 != 0) goto L57
                    monitor-enter(r8)
                    r6 = 0
                    r8.emitting = r6     // Catch: java.lang.Throwable -> L58
                    monitor-exit(r8)     // Catch: java.lang.Throwable -> L58
                L57:
                    throw r5
                L58:
                    r5 = move-exception
                    monitor-exit(r8)     // Catch: java.lang.Throwable -> L58
                    throw r5
                */
                throw new UnsupportedOperationException("Method not decompiled: rx.subjects.ReplaySubject.AnonymousClass2.call(rx.subjects.SubjectSubscriptionManager$SubjectObserver):void");
            }
        };
        ssm.onTerminated = new Action1<SubjectSubscriptionManager.SubjectObserver<T>>() { // from class: rx.subjects.ReplaySubject.3
            @Override // rx.functions.Action1
            public void call(SubjectSubscriptionManager.SubjectObserver<T> o) {
                int idx = (Integer) o.index();
                if (idx == null) {
                    idx = 0;
                }
                state.replayObserverFromIndex(idx, (SubjectSubscriptionManager.SubjectObserver) o);
            }
        };
        return new ReplaySubject<>(ssm, ssm, state);
    }

    static <T> ReplaySubject<T> createUnbounded() {
        BoundedState<T> state = new BoundedState<>(new EmptyEvictionPolicy(), UtilityFunctions.identity(), UtilityFunctions.identity());
        return createWithState(state, new DefaultOnAdd(state));
    }

    public static <T> ReplaySubject<T> createWithSize(int size) {
        BoundedState<T> state = new BoundedState<>(new SizeEvictionPolicy(size), UtilityFunctions.identity(), UtilityFunctions.identity());
        return createWithState(state, new DefaultOnAdd(state));
    }

    public static <T> ReplaySubject<T> createWithTime(long time, TimeUnit unit, Scheduler scheduler) {
        BoundedState<T> state = new BoundedState<>(new TimeEvictionPolicy(unit.toMillis(time), scheduler), new AddTimestamped(scheduler), new RemoveTimestamped());
        return createWithState(state, new TimedOnAdd(state, scheduler));
    }

    public static <T> ReplaySubject<T> createWithTimeAndSize(long time, TimeUnit unit, int size, Scheduler scheduler) {
        BoundedState<T> state = new BoundedState<>(new PairEvictionPolicy(new SizeEvictionPolicy(size), new TimeEvictionPolicy(unit.toMillis(time), scheduler)), new AddTimestamped(scheduler), new RemoveTimestamped());
        return createWithState(state, new TimedOnAdd(state, scheduler));
    }

    static final <T> ReplaySubject<T> createWithState(final BoundedState<T> state, Action1<SubjectSubscriptionManager.SubjectObserver<T>> onStart) {
        SubjectSubscriptionManager<T> ssm = new SubjectSubscriptionManager<>();
        ssm.onStart = onStart;
        ssm.onAdded = new Action1<SubjectSubscriptionManager.SubjectObserver<T>>() { // from class: rx.subjects.ReplaySubject.4
            /* JADX WARN: Removed duplicated region for block: B:21:0x003a  */
            /* JADX WARN: Removed duplicated region for block: B:57:? A[RETURN, SYNTHETIC] */
            @Override // rx.functions.Action1
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public void call(rx.subjects.SubjectSubscriptionManager.SubjectObserver<T> r7) {
                /*
                    r6 = this;
                    monitor-enter(r7)
                    boolean r4 = r7.first     // Catch: java.lang.Throwable -> L43
                    if (r4 == 0) goto L9
                    boolean r4 = r7.emitting     // Catch: java.lang.Throwable -> L43
                    if (r4 == 0) goto Lb
                L9:
                    monitor-exit(r7)     // Catch: java.lang.Throwable -> L43
                La:
                    return
                Lb:
                    r4 = 0
                    r7.first = r4     // Catch: java.lang.Throwable -> L43
                    r4 = 1
                    r7.emitting = r4     // Catch: java.lang.Throwable -> L43
                    monitor-exit(r7)     // Catch: java.lang.Throwable -> L43
                    r3 = 0
                L13:
                    java.lang.Object r0 = r7.index()     // Catch: java.lang.Throwable -> L4b
                    rx.subjects.ReplaySubject$NodeList$Node r0 = (rx.subjects.ReplaySubject.NodeList.Node) r0     // Catch: java.lang.Throwable -> L4b
                    rx.subjects.ReplaySubject$BoundedState r4 = r1     // Catch: java.lang.Throwable -> L4b
                    rx.subjects.ReplaySubject$NodeList$Node r2 = r4.tail()     // Catch: java.lang.Throwable -> L4b
                    if (r0 == r2) goto L2a
                    rx.subjects.ReplaySubject$BoundedState r4 = r1     // Catch: java.lang.Throwable -> L4b
                    rx.subjects.ReplaySubject$NodeList$Node r1 = r4.replayObserverFromIndex(r0, r7)     // Catch: java.lang.Throwable -> L4b
                    r7.index(r1)     // Catch: java.lang.Throwable -> L4b
                L2a:
                    monitor-enter(r7)     // Catch: java.lang.Throwable -> L4b
                    rx.subjects.ReplaySubject$BoundedState r4 = r1     // Catch: java.lang.Throwable -> L48
                    rx.subjects.ReplaySubject$NodeList$Node r4 = r4.tail()     // Catch: java.lang.Throwable -> L48
                    if (r2 != r4) goto L46
                    r4 = 0
                    r7.emitting = r4     // Catch: java.lang.Throwable -> L48
                    r3 = 1
                    monitor-exit(r7)     // Catch: java.lang.Throwable -> L48
                    if (r3 != 0) goto La
                    monitor-enter(r7)
                    r4 = 0
                    r7.emitting = r4     // Catch: java.lang.Throwable -> L40
                    monitor-exit(r7)     // Catch: java.lang.Throwable -> L40
                    goto La
                L40:
                    r4 = move-exception
                    monitor-exit(r7)     // Catch: java.lang.Throwable -> L40
                    throw r4
                L43:
                    r4 = move-exception
                    monitor-exit(r7)     // Catch: java.lang.Throwable -> L43
                    throw r4
                L46:
                    monitor-exit(r7)     // Catch: java.lang.Throwable -> L48
                    goto L13
                L48:
                    r4 = move-exception
                    monitor-exit(r7)     // Catch: java.lang.Throwable -> L48
                    throw r4     // Catch: java.lang.Throwable -> L4b
                L4b:
                    r4 = move-exception
                    if (r3 != 0) goto L53
                    monitor-enter(r7)
                    r5 = 0
                    r7.emitting = r5     // Catch: java.lang.Throwable -> L54
                    monitor-exit(r7)     // Catch: java.lang.Throwable -> L54
                L53:
                    throw r4
                L54:
                    r4 = move-exception
                    monitor-exit(r7)     // Catch: java.lang.Throwable -> L54
                    throw r4
                */
                throw new UnsupportedOperationException("Method not decompiled: rx.subjects.ReplaySubject.AnonymousClass4.call(rx.subjects.SubjectSubscriptionManager$SubjectObserver):void");
            }
        };
        ssm.onTerminated = new Action1<SubjectSubscriptionManager.SubjectObserver<T>>() { // from class: rx.subjects.ReplaySubject.5
            @Override // rx.functions.Action1
            public void call(SubjectSubscriptionManager.SubjectObserver<T> t1) {
                NodeList.Node<Object> l = (NodeList.Node) t1.index();
                if (l == null) {
                    l = state.head();
                }
                state.replayObserverFromIndex(l, (SubjectSubscriptionManager.SubjectObserver) t1);
            }
        };
        return new ReplaySubject<>(ssm, ssm, state);
    }

    ReplaySubject(Observable.OnSubscribe<T> onSubscribe, SubjectSubscriptionManager<T> ssm, ReplayState<T, ?> state) {
        super(onSubscribe);
        this.ssm = ssm;
        this.state = state;
    }

    @Override // rx.Observer
    public void onNext(T t) {
        if (this.ssm.active) {
            this.state.next(t);
            SubjectSubscriptionManager.SubjectObserver[] arr$ = this.ssm.observers();
            for (SubjectSubscriptionManager.SubjectObserver subjectObserver : arr$) {
                if (caughtUp(subjectObserver)) {
                    subjectObserver.onNext(t);
                }
            }
        }
    }

    @Override // rx.Observer
    public void onError(Throwable e) throws Throwable {
        if (this.ssm.active) {
            this.state.error(e);
            List<Throwable> errors = null;
            SubjectSubscriptionManager.SubjectObserver[] arr$ = this.ssm.terminate(NotificationLite.instance().error(e));
            for (SubjectSubscriptionManager.SubjectObserver subjectObserver : arr$) {
                try {
                    if (caughtUp(subjectObserver)) {
                        subjectObserver.onError(e);
                    }
                } catch (Throwable e2) {
                    if (errors == null) {
                        errors = new ArrayList<>();
                    }
                    errors.add(e2);
                }
            }
            Exceptions.throwIfAny(errors);
        }
    }

    @Override // rx.Observer
    public void onCompleted() {
        if (this.ssm.active) {
            this.state.complete();
            SubjectSubscriptionManager.SubjectObserver[] arr$ = this.ssm.terminate(NotificationLite.instance().completed());
            for (SubjectSubscriptionManager.SubjectObserver subjectObserver : arr$) {
                if (caughtUp(subjectObserver)) {
                    subjectObserver.onCompleted();
                }
            }
        }
    }

    int subscriberCount() {
        return this.ssm.get().observers.length;
    }

    @Override // rx.subjects.Subject
    public boolean hasObservers() {
        return this.ssm.observers().length > 0;
    }

    private boolean caughtUp(SubjectSubscriptionManager.SubjectObserver<? super T> o) {
        if (o.caughtUp) {
            return true;
        }
        if (this.state.replayObserver(o)) {
            o.caughtUp = true;
            o.index(null);
        }
        return false;
    }

    static final class UnboundedReplayState<T> extends AtomicInteger implements ReplayState<T, Integer> {
        private final ArrayList<Object> list;
        private final NotificationLite<T> nl = NotificationLite.instance();
        private volatile boolean terminated;

        public UnboundedReplayState(int initialCapacity) {
            this.list = new ArrayList<>(initialCapacity);
        }

        @Override // rx.subjects.ReplaySubject.ReplayState
        public void next(T n) {
            if (!this.terminated) {
                this.list.add(this.nl.next(n));
                getAndIncrement();
            }
        }

        public void accept(Observer<? super T> o, int idx) {
            this.nl.accept(o, this.list.get(idx));
        }

        @Override // rx.subjects.ReplaySubject.ReplayState
        public void complete() {
            if (!this.terminated) {
                this.terminated = true;
                this.list.add(this.nl.completed());
                getAndIncrement();
            }
        }

        @Override // rx.subjects.ReplaySubject.ReplayState
        public void error(Throwable e) {
            if (!this.terminated) {
                this.terminated = true;
                this.list.add(this.nl.error(e));
                getAndIncrement();
            }
        }

        @Override // rx.subjects.ReplaySubject.ReplayState
        public boolean terminated() {
            return this.terminated;
        }

        @Override // rx.subjects.ReplaySubject.ReplayState
        public boolean replayObserver(SubjectSubscriptionManager.SubjectObserver<? super T> observer) {
            synchronized (observer) {
                observer.first = false;
                if (observer.emitting) {
                    return false;
                }
                Integer lastEmittedLink = (Integer) observer.index();
                if (lastEmittedLink != null) {
                    int l = replayObserverFromIndex(lastEmittedLink, (SubjectSubscriptionManager.SubjectObserver) observer).intValue();
                    observer.index(Integer.valueOf(l));
                    return true;
                }
                throw new IllegalStateException("failed to find lastEmittedLink for: " + observer);
            }
        }

        @Override // rx.subjects.ReplaySubject.ReplayState
        public Integer replayObserverFromIndex(Integer idx, SubjectSubscriptionManager.SubjectObserver<? super T> observer) {
            int i = idx.intValue();
            while (i < get()) {
                accept(observer, i);
                i++;
            }
            return Integer.valueOf(i);
        }

        @Override // rx.subjects.ReplaySubject.ReplayState
        public Integer replayObserverFromIndexTest(Integer idx, SubjectSubscriptionManager.SubjectObserver<? super T> observer, long now) {
            return replayObserverFromIndex(idx, (SubjectSubscriptionManager.SubjectObserver) observer);
        }

        @Override // rx.subjects.ReplaySubject.ReplayState
        public int size() {
            int idx = get();
            if (idx > 0) {
                Object o = this.list.get(idx - 1);
                if (this.nl.isCompleted(o) || this.nl.isError(o)) {
                    return idx - 1;
                }
                return idx;
            }
            return idx;
        }

        @Override // rx.subjects.ReplaySubject.ReplayState
        public boolean isEmpty() {
            return size() == 0;
        }

        @Override // rx.subjects.ReplaySubject.ReplayState
        public T[] toArray(T[] tArr) {
            int size = size();
            if (size > 0) {
                if (size > tArr.length) {
                    tArr = (T[]) ((Object[]) Array.newInstance(tArr.getClass().getComponentType(), size));
                }
                for (int i = 0; i < size; i++) {
                    tArr[i] = this.list.get(i);
                }
                if (tArr.length > size) {
                    tArr[size] = null;
                }
            } else if (tArr.length > 0) {
                tArr[0] = null;
            }
            return tArr;
        }

        @Override // rx.subjects.ReplaySubject.ReplayState
        public T latest() {
            int idx = get();
            if (idx <= 0) {
                return null;
            }
            Object o = this.list.get(idx - 1);
            if (this.nl.isCompleted(o) || this.nl.isError(o)) {
                if (idx > 1) {
                    return this.nl.getValue(this.list.get(idx - 2));
                }
                return null;
            }
            return this.nl.getValue(o);
        }
    }

    static final class BoundedState<T> implements ReplayState<T, NodeList.Node<Object>> {
        final Func1<Object, Object> enterTransform;
        final EvictionPolicy evictionPolicy;
        final Func1<Object, Object> leaveTransform;
        volatile boolean terminated;
        final NotificationLite<T> nl = NotificationLite.instance();
        final NodeList<Object> list = new NodeList<>();
        volatile NodeList.Node<Object> tail = this.list.tail;

        public BoundedState(EvictionPolicy evictionPolicy, Func1<Object, Object> enterTransform, Func1<Object, Object> leaveTransform) {
            this.evictionPolicy = evictionPolicy;
            this.enterTransform = enterTransform;
            this.leaveTransform = leaveTransform;
        }

        @Override // rx.subjects.ReplaySubject.ReplayState
        public void next(T value) {
            if (!this.terminated) {
                this.list.addLast(this.enterTransform.call(this.nl.next(value)));
                this.evictionPolicy.evict(this.list);
                this.tail = this.list.tail;
            }
        }

        @Override // rx.subjects.ReplaySubject.ReplayState
        public void complete() {
            if (!this.terminated) {
                this.terminated = true;
                this.list.addLast(this.enterTransform.call(this.nl.completed()));
                this.evictionPolicy.evictFinal(this.list);
                this.tail = this.list.tail;
            }
        }

        @Override // rx.subjects.ReplaySubject.ReplayState
        public void error(Throwable e) {
            if (!this.terminated) {
                this.terminated = true;
                this.list.addLast(this.enterTransform.call(this.nl.error(e)));
                this.evictionPolicy.evictFinal(this.list);
                this.tail = this.list.tail;
            }
        }

        public void accept(Observer<? super T> o, NodeList.Node<Object> node) {
            this.nl.accept(o, this.leaveTransform.call(node.value));
        }

        public void acceptTest(Observer<? super T> o, NodeList.Node<Object> node, long now) {
            Object v = node.value;
            if (!this.evictionPolicy.test(v, now)) {
                this.nl.accept(o, this.leaveTransform.call(v));
            }
        }

        public NodeList.Node<Object> head() {
            return this.list.head;
        }

        public NodeList.Node<Object> tail() {
            return this.tail;
        }

        @Override // rx.subjects.ReplaySubject.ReplayState
        public boolean replayObserver(SubjectSubscriptionManager.SubjectObserver<? super T> observer) {
            synchronized (observer) {
                observer.first = false;
                if (observer.emitting) {
                    return false;
                }
                NodeList.Node<Object> lastEmittedLink = (NodeList.Node) observer.index();
                NodeList.Node<Object> l = replayObserverFromIndex(lastEmittedLink, (SubjectSubscriptionManager.SubjectObserver) observer);
                observer.index(l);
                return true;
            }
        }

        @Override // rx.subjects.ReplaySubject.ReplayState
        public NodeList.Node<Object> replayObserverFromIndex(NodeList.Node<Object> l, SubjectSubscriptionManager.SubjectObserver<? super T> observer) {
            while (l != tail()) {
                accept(observer, l.next);
                l = l.next;
            }
            return l;
        }

        @Override // rx.subjects.ReplaySubject.ReplayState
        public NodeList.Node<Object> replayObserverFromIndexTest(NodeList.Node<Object> l, SubjectSubscriptionManager.SubjectObserver<? super T> observer, long now) {
            while (l != tail()) {
                acceptTest(observer, l.next, now);
                l = l.next;
            }
            return l;
        }

        @Override // rx.subjects.ReplaySubject.ReplayState
        public boolean terminated() {
            return this.terminated;
        }

        @Override // rx.subjects.ReplaySubject.ReplayState
        public int size() {
            Object value;
            int size = 0;
            NodeList.Node<Object> l = head();
            for (NodeList.Node<Object> next = l.next; next != null; next = next.next) {
                size++;
                l = next;
            }
            if (l.value == null || (value = this.leaveTransform.call(l.value)) == null) {
                return size;
            }
            if (this.nl.isError(value) || this.nl.isCompleted(value)) {
                return size - 1;
            }
            return size;
        }

        @Override // rx.subjects.ReplaySubject.ReplayState
        public boolean isEmpty() {
            NodeList.Node<Object> l = head();
            NodeList.Node<Object> next = l.next;
            if (next == null) {
                return true;
            }
            Object value = this.leaveTransform.call(next.value);
            return this.nl.isError(value) || this.nl.isCompleted(value);
        }

        @Override // rx.subjects.ReplaySubject.ReplayState
        public T[] toArray(T[] tArr) {
            ArrayList arrayList = new ArrayList();
            for (NodeList.Node node = head().next; node != null; node = node.next) {
                Object objCall = this.leaveTransform.call(node.value);
                if (node.next == null && (this.nl.isError(objCall) || this.nl.isCompleted(objCall))) {
                    break;
                }
                arrayList.add(objCall);
            }
            return (T[]) arrayList.toArray(tArr);
        }

        @Override // rx.subjects.ReplaySubject.ReplayState
        public T latest() {
            NodeList.Node<Object> h = head().next;
            if (h == null) {
                return null;
            }
            NodeList.Node<Object> p = null;
            while (h != tail()) {
                p = h;
                h = h.next;
            }
            Object value = this.leaveTransform.call(h.value);
            if (this.nl.isError(value) || this.nl.isCompleted(value)) {
                if (p == null) {
                    return null;
                }
                return this.nl.getValue(this.leaveTransform.call(p.value));
            }
            return this.nl.getValue(value);
        }
    }

    static final class SizeEvictionPolicy implements EvictionPolicy {
        final int maxSize;

        public SizeEvictionPolicy(int maxSize) {
            this.maxSize = maxSize;
        }

        @Override // rx.subjects.ReplaySubject.EvictionPolicy
        public void evict(NodeList<Object> t1) {
            while (t1.size() > this.maxSize) {
                t1.removeFirst();
            }
        }

        @Override // rx.subjects.ReplaySubject.EvictionPolicy
        public boolean test(Object value, long now) {
            return false;
        }

        @Override // rx.subjects.ReplaySubject.EvictionPolicy
        public void evictFinal(NodeList<Object> t1) {
            while (t1.size() > this.maxSize + 1) {
                t1.removeFirst();
            }
        }
    }

    static final class TimeEvictionPolicy implements EvictionPolicy {
        final long maxAgeMillis;
        final Scheduler scheduler;

        public TimeEvictionPolicy(long maxAgeMillis, Scheduler scheduler) {
            this.maxAgeMillis = maxAgeMillis;
            this.scheduler = scheduler;
        }

        @Override // rx.subjects.ReplaySubject.EvictionPolicy
        public void evict(NodeList<Object> t1) {
            long now = this.scheduler.now();
            while (!t1.isEmpty()) {
                NodeList.Node<Object> n = t1.head.next;
                if (test(n.value, now)) {
                    t1.removeFirst();
                } else {
                    return;
                }
            }
        }

        @Override // rx.subjects.ReplaySubject.EvictionPolicy
        public void evictFinal(NodeList<Object> t1) {
            long now = this.scheduler.now();
            while (t1.size > 1) {
                NodeList.Node<Object> n = t1.head.next;
                if (test(n.value, now)) {
                    t1.removeFirst();
                } else {
                    return;
                }
            }
        }

        @Override // rx.subjects.ReplaySubject.EvictionPolicy
        public boolean test(Object value, long now) {
            Timestamped<?> ts = (Timestamped) value;
            return ts.getTimestampMillis() <= now - this.maxAgeMillis;
        }
    }

    static final class PairEvictionPolicy implements EvictionPolicy {
        final EvictionPolicy first;
        final EvictionPolicy second;

        public PairEvictionPolicy(EvictionPolicy first, EvictionPolicy second) {
            this.first = first;
            this.second = second;
        }

        @Override // rx.subjects.ReplaySubject.EvictionPolicy
        public void evict(NodeList<Object> t1) {
            this.first.evict(t1);
            this.second.evict(t1);
        }

        @Override // rx.subjects.ReplaySubject.EvictionPolicy
        public void evictFinal(NodeList<Object> t1) {
            this.first.evictFinal(t1);
            this.second.evictFinal(t1);
        }

        @Override // rx.subjects.ReplaySubject.EvictionPolicy
        public boolean test(Object value, long now) {
            return this.first.test(value, now) || this.second.test(value, now);
        }
    }

    static final class AddTimestamped implements Func1<Object, Object> {
        final Scheduler scheduler;

        public AddTimestamped(Scheduler scheduler) {
            this.scheduler = scheduler;
        }

        @Override // rx.functions.Func1
        public Object call(Object t1) {
            return new Timestamped(this.scheduler.now(), t1);
        }
    }

    static final class RemoveTimestamped implements Func1<Object, Object> {
        RemoveTimestamped() {
        }

        @Override // rx.functions.Func1
        public Object call(Object t1) {
            return ((Timestamped) t1).getValue();
        }
    }

    static final class DefaultOnAdd<T> implements Action1<SubjectSubscriptionManager.SubjectObserver<T>> {
        final BoundedState<T> state;

        public DefaultOnAdd(BoundedState<T> state) {
            this.state = state;
        }

        @Override // rx.functions.Action1
        public void call(SubjectSubscriptionManager.SubjectObserver<T> t1) {
            NodeList.Node<Object> l = this.state.replayObserverFromIndex(this.state.head(), (SubjectSubscriptionManager.SubjectObserver) t1);
            t1.index(l);
        }
    }

    static final class TimedOnAdd<T> implements Action1<SubjectSubscriptionManager.SubjectObserver<T>> {
        final Scheduler scheduler;
        final BoundedState<T> state;

        public TimedOnAdd(BoundedState<T> state, Scheduler scheduler) {
            this.state = state;
            this.scheduler = scheduler;
        }

        @Override // rx.functions.Action1
        public void call(SubjectSubscriptionManager.SubjectObserver<T> t1) {
            NodeList.Node<Object> l;
            if (!this.state.terminated) {
                l = this.state.replayObserverFromIndexTest(this.state.head(), (SubjectSubscriptionManager.SubjectObserver) t1, this.scheduler.now());
            } else {
                l = this.state.replayObserverFromIndex(this.state.head(), (SubjectSubscriptionManager.SubjectObserver) t1);
            }
            t1.index(l);
        }
    }

    static final class NodeList<T> {
        int size;
        final Node<T> head = new Node<>(null);
        Node<T> tail = this.head;

        NodeList() {
        }

        static final class Node<T> {
            volatile Node<T> next;
            final T value;

            Node(T value) {
                this.value = value;
            }
        }

        public void addLast(T value) {
            Node<T> t = this.tail;
            Node<T> t2 = new Node<>(value);
            t.next = t2;
            this.tail = t2;
            this.size++;
        }

        public T removeFirst() {
            if (this.head.next == null) {
                throw new IllegalStateException("Empty!");
            }
            Node<T> t = this.head.next;
            this.head.next = t.next;
            if (this.head.next == null) {
                this.tail = this.head;
            }
            this.size--;
            return t.value;
        }

        public boolean isEmpty() {
            return this.size == 0;
        }

        public int size() {
            return this.size;
        }

        public void clear() {
            this.tail = this.head;
            this.size = 0;
        }
    }

    static final class EmptyEvictionPolicy implements EvictionPolicy {
        EmptyEvictionPolicy() {
        }

        @Override // rx.subjects.ReplaySubject.EvictionPolicy
        public boolean test(Object value, long now) {
            return true;
        }

        @Override // rx.subjects.ReplaySubject.EvictionPolicy
        public void evict(NodeList<Object> list) {
        }

        @Override // rx.subjects.ReplaySubject.EvictionPolicy
        public void evictFinal(NodeList<Object> list) {
        }
    }

    @Beta
    public boolean hasThrowable() {
        NotificationLite<T> nl = this.ssm.nl;
        Object o = this.ssm.getLatest();
        return nl.isError(o);
    }

    @Beta
    public boolean hasCompleted() {
        NotificationLite<T> nl = this.ssm.nl;
        Object o = this.ssm.getLatest();
        return (o == null || nl.isError(o)) ? false : true;
    }

    @Beta
    public Throwable getThrowable() {
        NotificationLite<T> nl = this.ssm.nl;
        Object o = this.ssm.getLatest();
        if (nl.isError(o)) {
            return nl.getError(o);
        }
        return null;
    }

    @Beta
    public int size() {
        return this.state.size();
    }

    @Beta
    public boolean hasAnyValue() {
        return !this.state.isEmpty();
    }

    @Beta
    public boolean hasValue() {
        return hasAnyValue();
    }

    @Beta
    public T[] getValues(T[] a2) {
        return this.state.toArray(a2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Beta
    public Object[] getValues() {
        Object[] values = getValues(EMPTY_ARRAY);
        if (values == EMPTY_ARRAY) {
            return new Object[0];
        }
        return values;
    }

    @Beta
    public T getValue() {
        return this.state.latest();
    }
}
