package rx.subjects;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Actions;
import rx.internal.operators.NotificationLite;
import rx.subscriptions.Subscriptions;

/* loaded from: classes.dex */
final class SubjectSubscriptionManager<T> extends AtomicReference<State<T>> implements Observable.OnSubscribe<T> {
    boolean active;
    volatile Object latest;
    public final NotificationLite<T> nl;
    Action1<SubjectObserver<T>> onAdded;
    Action1<SubjectObserver<T>> onStart;
    Action1<SubjectObserver<T>> onTerminated;

    public SubjectSubscriptionManager() {
        super(State.EMPTY);
        this.active = true;
        this.onStart = Actions.empty();
        this.onAdded = Actions.empty();
        this.onTerminated = Actions.empty();
        this.nl = NotificationLite.instance();
    }

    @Override // rx.functions.Action1
    public void call(Subscriber<? super T> child) {
        SubjectObserver<T> bo = new SubjectObserver<>(child);
        addUnsubscriber(child, bo);
        this.onStart.call(bo);
        if (!child.isUnsubscribed() && add(bo) && child.isUnsubscribed()) {
            remove(bo);
        }
    }

    void addUnsubscriber(Subscriber<? super T> child, final SubjectObserver<T> bo) {
        child.add(Subscriptions.create(new Action0() { // from class: rx.subjects.SubjectSubscriptionManager.1
            @Override // rx.functions.Action0
            public void call() {
                SubjectSubscriptionManager.this.remove(bo);
            }
        }));
    }

    void setLatest(Object value) {
        this.latest = value;
    }

    Object getLatest() {
        return this.latest;
    }

    SubjectObserver<T>[] observers() {
        return get().observers;
    }

    boolean add(SubjectObserver<T> o) {
        State oldState;
        State newState;
        do {
            oldState = get();
            if (oldState.terminated) {
                this.onTerminated.call(o);
                return false;
            }
            newState = oldState.add(o);
        } while (!compareAndSet(oldState, newState));
        this.onAdded.call(o);
        return true;
    }

    void remove(SubjectObserver<T> o) {
        State oldState;
        State newState;
        do {
            oldState = get();
            if (oldState.terminated || (newState = oldState.remove(o)) == oldState) {
                return;
            }
        } while (!compareAndSet(oldState, newState));
    }

    SubjectObserver<T>[] next(Object n) {
        setLatest(n);
        return get().observers;
    }

    SubjectObserver<T>[] terminate(Object n) {
        setLatest(n);
        this.active = false;
        State<T> oldState = get();
        return oldState.terminated ? State.NO_OBSERVERS : getAndSet(State.TERMINATED).observers;
    }

    protected static final class State<T> {
        final SubjectObserver[] observers;
        final boolean terminated;
        static final SubjectObserver[] NO_OBSERVERS = new SubjectObserver[0];
        static final State TERMINATED = new State(true, NO_OBSERVERS);
        static final State EMPTY = new State(false, NO_OBSERVERS);

        public State(boolean terminated, SubjectObserver[] observers) {
            this.terminated = terminated;
            this.observers = observers;
        }

        public State add(SubjectObserver o) {
            SubjectObserver[] a2 = this.observers;
            int n = a2.length;
            SubjectObserver[] b = new SubjectObserver[n + 1];
            System.arraycopy(this.observers, 0, b, 0, n);
            b[n] = o;
            return new State(this.terminated, b);
        }

        public State remove(SubjectObserver o) {
            int j;
            SubjectObserver[] a2 = this.observers;
            int n = a2.length;
            if (n == 1 && a2[0] == o) {
                return EMPTY;
            }
            if (n != 0) {
                SubjectObserver[] b = new SubjectObserver[n - 1];
                int i = 0;
                int j2 = 0;
                while (i < n) {
                    SubjectObserver ai = a2[i];
                    if (ai == o) {
                        j = j2;
                    } else if (j2 != n - 1) {
                        j = j2 + 1;
                        b[j2] = ai;
                    } else {
                        return this;
                    }
                    i++;
                    j2 = j;
                }
                if (j2 == 0) {
                    return EMPTY;
                }
                if (j2 < n - 1) {
                    SubjectObserver[] c = new SubjectObserver[j2];
                    System.arraycopy(b, 0, c, 0, j2);
                    b = c;
                }
                return new State<>(this.terminated, b);
            }
            return this;
        }
    }

    protected static final class SubjectObserver<T> implements Observer<T> {
        final Observer<? super T> actual;
        protected volatile boolean caughtUp;
        boolean emitting;
        boolean fastPath;
        boolean first = true;
        private volatile Object index;
        List<Object> queue;

        public SubjectObserver(Observer<? super T> actual) {
            this.actual = actual;
        }

        @Override // rx.Observer
        public void onNext(T t) {
            this.actual.onNext(t);
        }

        @Override // rx.Observer
        public void onError(Throwable e) {
            this.actual.onError(e);
        }

        @Override // rx.Observer
        public void onCompleted() {
            this.actual.onCompleted();
        }

        protected void emitNext(Object n, NotificationLite<T> nl) {
            if (!this.fastPath) {
                synchronized (this) {
                    this.first = false;
                    if (this.emitting) {
                        if (this.queue == null) {
                            this.queue = new ArrayList();
                        }
                        this.queue.add(n);
                        return;
                    }
                    this.fastPath = true;
                }
            }
            nl.accept(this.actual, n);
        }

        protected void emitFirst(Object n, NotificationLite<T> nl) {
            synchronized (this) {
                if (this.first && !this.emitting) {
                    this.first = false;
                    this.emitting = n != null;
                    if (n != null) {
                        emitLoop(null, n, nl);
                    }
                }
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:25:0x0034  */
        /* JADX WARN: Removed duplicated region for block: B:50:? A[RETURN, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        protected void emitLoop(java.util.List<java.lang.Object> r7, java.lang.Object r8, rx.internal.operators.NotificationLite<T> r9) {
            /*
                r6 = this;
                r2 = 1
                r3 = 0
            L2:
                if (r7 == 0) goto L1f
                java.util.Iterator r0 = r7.iterator()     // Catch: java.lang.Throwable -> L16
            L8:
                boolean r4 = r0.hasNext()     // Catch: java.lang.Throwable -> L16
                if (r4 == 0) goto L1f
                java.lang.Object r1 = r0.next()     // Catch: java.lang.Throwable -> L16
                r6.accept(r1, r9)     // Catch: java.lang.Throwable -> L16
                goto L8
            L16:
                r4 = move-exception
                if (r3 != 0) goto L1e
                monitor-enter(r6)
                r5 = 0
                r6.emitting = r5     // Catch: java.lang.Throwable -> L42
                monitor-exit(r6)     // Catch: java.lang.Throwable -> L42
            L1e:
                throw r4
            L1f:
                if (r2 == 0) goto L25
                r2 = 0
                r6.accept(r8, r9)     // Catch: java.lang.Throwable -> L16
            L25:
                monitor-enter(r6)     // Catch: java.lang.Throwable -> L16
                java.util.List<java.lang.Object> r7 = r6.queue     // Catch: java.lang.Throwable -> L3c
                r4 = 0
                r6.queue = r4     // Catch: java.lang.Throwable -> L3c
                if (r7 != 0) goto L3a
                r4 = 0
                r6.emitting = r4     // Catch: java.lang.Throwable -> L3c
                r3 = 1
                monitor-exit(r6)     // Catch: java.lang.Throwable -> L3c
                if (r3 != 0) goto L39
                monitor-enter(r6)
                r4 = 0
                r6.emitting = r4     // Catch: java.lang.Throwable -> L3f
                monitor-exit(r6)     // Catch: java.lang.Throwable -> L3f
            L39:
                return
            L3a:
                monitor-exit(r6)     // Catch: java.lang.Throwable -> L3c
                goto L2
            L3c:
                r4 = move-exception
                monitor-exit(r6)     // Catch: java.lang.Throwable -> L3c
                throw r4     // Catch: java.lang.Throwable -> L16
            L3f:
                r4 = move-exception
                monitor-exit(r6)     // Catch: java.lang.Throwable -> L3f
                throw r4
            L42:
                r4 = move-exception
                monitor-exit(r6)     // Catch: java.lang.Throwable -> L42
                throw r4
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.subjects.SubjectSubscriptionManager.SubjectObserver.emitLoop(java.util.List, java.lang.Object, rx.internal.operators.NotificationLite):void");
        }

        protected void accept(Object n, NotificationLite<T> nl) {
            if (n != null) {
                nl.accept(this.actual, n);
            }
        }

        protected Observer<? super T> getActual() {
            return this.actual;
        }

        public <I> I index() {
            return (I) this.index;
        }

        public void index(Object newIndex) {
            this.index = newIndex;
        }
    }
}
