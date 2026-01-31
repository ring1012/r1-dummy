package rx.internal.operators;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import org.apache.commons.io.FileUtils;
import rx.Observable;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.observables.GroupedObservable;
import rx.subjects.Subject;
import rx.subscriptions.Subscriptions;

/* loaded from: classes.dex */
public class OperatorGroupBy<T, K, R> implements Observable.Operator<GroupedObservable<K, R>, T> {
    private static final Func1<Object, Object> IDENTITY = new Func1<Object, Object>() { // from class: rx.internal.operators.OperatorGroupBy.1
        @Override // rx.functions.Func1
        public Object call(Object t) {
            return t;
        }
    };
    private static final Object NULL_KEY = new Object();
    final Func1<? super T, ? extends K> keySelector;
    final Func1<? super T, ? extends R> valueSelector;

    public OperatorGroupBy(Func1<? super T, ? extends K> keySelector) {
        this(keySelector, IDENTITY);
    }

    public OperatorGroupBy(Func1<? super T, ? extends K> keySelector, Func1<? super T, ? extends R> valueSelector) {
        this.keySelector = keySelector;
        this.valueSelector = valueSelector;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(Subscriber<? super GroupedObservable<K, R>> child) {
        return new GroupBySubscriber(this.keySelector, this.valueSelector, child);
    }

    static final class GroupBySubscriber<K, T, R> extends Subscriber<T> {
        private static final int MAX_QUEUE_SIZE = 1024;
        private static final int TERMINATED_WITH_COMPLETED = 1;
        private static final int TERMINATED_WITH_ERROR = 2;
        private static final int UNTERMINATED = 0;
        volatile long bufferedCount;
        final Subscriber<? super GroupedObservable<K, R>> child;
        volatile int completionEmitted;
        final Func1<? super T, ? extends R> elementSelector;
        final Func1<? super T, ? extends K> keySelector;
        volatile long requested;
        static final AtomicIntegerFieldUpdater<GroupBySubscriber> WIP_FOR_UNSUBSCRIBE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(GroupBySubscriber.class, "wipForUnsubscribe");
        private static final NotificationLite<Object> nl = NotificationLite.instance();
        static final AtomicIntegerFieldUpdater<GroupBySubscriber> COMPLETION_EMITTED_UPDATER = AtomicIntegerFieldUpdater.newUpdater(GroupBySubscriber.class, "completionEmitted");
        static final AtomicIntegerFieldUpdater<GroupBySubscriber> TERMINATED_UPDATER = AtomicIntegerFieldUpdater.newUpdater(GroupBySubscriber.class, "terminated");
        static final AtomicLongFieldUpdater<GroupBySubscriber> REQUESTED = AtomicLongFieldUpdater.newUpdater(GroupBySubscriber.class, "requested");
        static final AtomicLongFieldUpdater<GroupBySubscriber> BUFFERED_COUNT = AtomicLongFieldUpdater.newUpdater(GroupBySubscriber.class, "bufferedCount");
        final GroupBySubscriber<K, T, R> self = this;
        volatile int wipForUnsubscribe = 1;
        private final ConcurrentHashMap<Object, GroupState<K, T>> groups = new ConcurrentHashMap<>();
        volatile int terminated = 0;

        public GroupBySubscriber(Func1<? super T, ? extends K> keySelector, Func1<? super T, ? extends R> elementSelector, Subscriber<? super GroupedObservable<K, R>> child) {
            this.keySelector = keySelector;
            this.elementSelector = elementSelector;
            this.child = child;
            child.add(Subscriptions.create(new Action0() { // from class: rx.internal.operators.OperatorGroupBy.GroupBySubscriber.1
                @Override // rx.functions.Action0
                public void call() {
                    if (GroupBySubscriber.WIP_FOR_UNSUBSCRIBE_UPDATER.decrementAndGet(GroupBySubscriber.this.self) == 0) {
                        GroupBySubscriber.this.self.unsubscribe();
                    }
                }
            }));
        }

        private static class GroupState<K, T> {
            private final Queue<Object> buffer;
            private final AtomicLong count;
            private final AtomicLong requested;
            private final Subject<T, T> s;

            private GroupState() {
                this.s = BufferUntilSubscriber.create();
                this.requested = new AtomicLong();
                this.count = new AtomicLong();
                this.buffer = new ConcurrentLinkedQueue();
            }

            public Observable<T> getObservable() {
                return this.s;
            }

            public Observer<T> getObserver() {
                return this.s;
            }
        }

        @Override // rx.Subscriber
        public void onStart() {
            REQUESTED.set(this, FileUtils.ONE_KB);
            request(FileUtils.ONE_KB);
        }

        @Override // rx.Observer
        public void onCompleted() {
            if (TERMINATED_UPDATER.compareAndSet(this, 0, 1)) {
                for (GroupState<K, T> group : this.groups.values()) {
                    emitItem(group, nl.completed());
                }
                if (this.groups.isEmpty() && COMPLETION_EMITTED_UPDATER.compareAndSet(this, 0, 1)) {
                    this.child.onCompleted();
                }
            }
        }

        @Override // rx.Observer
        public void onError(Throwable e) {
            if (TERMINATED_UPDATER.compareAndSet(this, 0, 2)) {
                for (GroupState<K, T> group : this.groups.values()) {
                    emitItem(group, nl.error(e));
                }
                try {
                    this.child.onError(e);
                } finally {
                    unsubscribe();
                }
            }
        }

        void requestFromGroupedObservable(long n, GroupState<K, T> group) {
            BackpressureUtils.getAndAddRequest(((GroupState) group).requested, n);
            if (((GroupState) group).count.getAndIncrement() == 0) {
                pollQueue(group);
            }
        }

        private Object groupedKey(K key) {
            return key == null ? OperatorGroupBy.NULL_KEY : key;
        }

        /* JADX WARN: Multi-variable type inference failed */
        private K getKey(Object obj) {
            if (obj == OperatorGroupBy.NULL_KEY) {
                return null;
            }
            return obj;
        }

        @Override // rx.Observer
        public void onNext(T t) {
            try {
                Object key = groupedKey(this.keySelector.call(t));
                GroupState<K, T> group = this.groups.get(key);
                if (group == null) {
                    if (!this.child.isUnsubscribed()) {
                        group = createNewGroup(key);
                    } else {
                        return;
                    }
                }
                if (group != null) {
                    emitItem(group, nl.next(t));
                }
            } catch (Throwable e) {
                Exceptions.throwOrReport(e, this, t);
            }
        }

        private GroupState<K, T> createNewGroup(final Object key) {
            int wip;
            final GroupState<K, T> groupState = new GroupState<>();
            GroupedObservable<K, R> go = GroupedObservable.create(getKey(key), new Observable.OnSubscribe<R>() { // from class: rx.internal.operators.OperatorGroupBy.GroupBySubscriber.2
                @Override // rx.functions.Action1
                public void call(final Subscriber<? super R> subscriber) {
                    subscriber.setProducer(new Producer() { // from class: rx.internal.operators.OperatorGroupBy.GroupBySubscriber.2.1
                        @Override // rx.Producer
                        public void request(long n) {
                            GroupBySubscriber.this.requestFromGroupedObservable(n, groupState);
                        }
                    });
                    final AtomicBoolean atomicBoolean = new AtomicBoolean();
                    groupState.getObservable().doOnUnsubscribe(new Action0() { // from class: rx.internal.operators.OperatorGroupBy.GroupBySubscriber.2.3
                        @Override // rx.functions.Action0
                        public void call() {
                            if (atomicBoolean.compareAndSet(false, true)) {
                                GroupBySubscriber.this.cleanupGroup(key);
                            }
                        }
                    }).unsafeSubscribe(new Subscriber<T>(subscriber) { // from class: rx.internal.operators.OperatorGroupBy.GroupBySubscriber.2.2
                        @Override // rx.Observer
                        public void onCompleted() {
                            subscriber.onCompleted();
                            if (atomicBoolean.compareAndSet(false, true)) {
                                GroupBySubscriber.this.cleanupGroup(key);
                            }
                        }

                        @Override // rx.Observer
                        public void onError(Throwable e) {
                            subscriber.onError(e);
                            if (atomicBoolean.compareAndSet(false, true)) {
                                GroupBySubscriber.this.cleanupGroup(key);
                            }
                        }

                        @Override // rx.Observer
                        public void onNext(T t) {
                            try {
                                subscriber.onNext(GroupBySubscriber.this.elementSelector.call(t));
                            } catch (Throwable e) {
                                Exceptions.throwOrReport(e, this, t);
                            }
                        }
                    });
                }
            });
            do {
                wip = this.wipForUnsubscribe;
                if (wip <= 0) {
                    return null;
                }
            } while (!WIP_FOR_UNSUBSCRIBE_UPDATER.compareAndSet(this, wip, wip + 1));
            GroupState<K, T> putIfAbsent = this.groups.putIfAbsent(key, groupState);
            if (putIfAbsent != null) {
                throw new IllegalStateException("Group already existed while creating a new one");
            }
            this.child.onNext(go);
            return groupState;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void cleanupGroup(Object key) {
            GroupState<K, T> removed = this.groups.remove(key);
            if (removed != null) {
                if (!((GroupState) removed).buffer.isEmpty()) {
                    BUFFERED_COUNT.addAndGet(this.self, -((GroupState) removed).buffer.size());
                }
                completeInner();
                requestMoreIfNecessary();
            }
        }

        private void emitItem(GroupState<K, T> groupState, Object item) {
            Queue<Object> q = ((GroupState) groupState).buffer;
            AtomicLong keyRequested = ((GroupState) groupState).requested;
            REQUESTED.decrementAndGet(this);
            if (keyRequested != null && keyRequested.get() > 0 && (q == null || q.isEmpty())) {
                Observer<Object> obs = groupState.getObserver();
                nl.accept(obs, item);
                if (keyRequested.get() != Long.MAX_VALUE) {
                    keyRequested.decrementAndGet();
                }
            } else {
                q.add(item);
                BUFFERED_COUNT.incrementAndGet(this);
                if (((GroupState) groupState).count.getAndIncrement() == 0) {
                    pollQueue(groupState);
                }
            }
            requestMoreIfNecessary();
        }

        private void pollQueue(GroupState<K, T> groupState) {
            do {
                drainIfPossible(groupState);
                long c = ((GroupState) groupState).count.decrementAndGet();
                if (c > 1) {
                    ((GroupState) groupState).count.set(1L);
                }
            } while (((GroupState) groupState).count.get() > 0);
        }

        private void requestMoreIfNecessary() {
            if (REQUESTED.get(this) == 0 && this.terminated == 0) {
                long toRequest = FileUtils.ONE_KB - BUFFERED_COUNT.get(this);
                if (toRequest > 0 && REQUESTED.compareAndSet(this, 0L, toRequest)) {
                    request(toRequest);
                }
            }
        }

        private void drainIfPossible(GroupState<K, T> groupState) {
            Object t;
            while (((GroupState) groupState).requested.get() > 0 && (t = ((GroupState) groupState).buffer.poll()) != null) {
                Observer<Object> obs = groupState.getObserver();
                nl.accept(obs, t);
                if (((GroupState) groupState).requested.get() != Long.MAX_VALUE) {
                    ((GroupState) groupState).requested.decrementAndGet();
                }
                BUFFERED_COUNT.decrementAndGet(this);
                requestMoreIfNecessary();
            }
        }

        private void completeInner() {
            if (WIP_FOR_UNSUBSCRIBE_UPDATER.decrementAndGet(this) == 0) {
                unsubscribe();
            } else if (this.groups.isEmpty() && this.terminated == 1 && COMPLETION_EMITTED_UPDATER.compareAndSet(this, 0, 1)) {
                this.child.onCompleted();
            }
        }
    }
}
