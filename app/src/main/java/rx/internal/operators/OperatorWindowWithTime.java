package rx.internal.operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Action0;
import rx.observers.SerializedObserver;
import rx.observers.SerializedSubscriber;
import rx.subscriptions.Subscriptions;

/* loaded from: classes.dex */
public final class OperatorWindowWithTime<T> implements Observable.Operator<Observable<T>, T> {
    static final Object NEXT_SUBJECT = new Object();
    static final NotificationLite<Object> nl = NotificationLite.instance();
    final Scheduler scheduler;
    final int size;
    final long timeshift;
    final long timespan;
    final TimeUnit unit;

    public OperatorWindowWithTime(long timespan, long timeshift, TimeUnit unit, int size, Scheduler scheduler) {
        this.timespan = timespan;
        this.timeshift = timeshift;
        this.unit = unit;
        this.size = size;
        this.scheduler = scheduler;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(Subscriber<? super Observable<T>> child) {
        Scheduler.Worker worker = this.scheduler.createWorker();
        if (this.timespan == this.timeshift) {
            OperatorWindowWithTime<T>.ExactSubscriber s = new ExactSubscriber(child, worker);
            s.add(worker);
            s.scheduleExact();
            return s;
        }
        OperatorWindowWithTime<T>.InexactSubscriber s2 = new InexactSubscriber(child, worker);
        s2.add(worker);
        s2.startNewChunk();
        s2.scheduleChunk();
        return s2;
    }

    static final class State<T> {
        static final State<Object> EMPTY = new State<>(null, null, 0);
        final Observer<T> consumer;
        final int count;
        final Observable<T> producer;

        public State(Observer<T> consumer, Observable<T> producer, int count) {
            this.consumer = consumer;
            this.producer = producer;
            this.count = count;
        }

        public State<T> next() {
            return new State<>(this.consumer, this.producer, this.count + 1);
        }

        public State<T> create(Observer<T> consumer, Observable<T> producer) {
            return new State<>(consumer, producer, 0);
        }

        public State<T> clear() {
            return empty();
        }

        public static <T> State<T> empty() {
            return (State<T>) EMPTY;
        }
    }

    final class ExactSubscriber extends Subscriber<T> {
        final Subscriber<? super Observable<T>> child;
        boolean emitting;
        List<Object> queue;
        final Scheduler.Worker worker;
        final Object guard = new Object();
        volatile State<T> state = State.empty();

        public ExactSubscriber(Subscriber<? super Observable<T>> child, Scheduler.Worker worker) {
            this.child = new SerializedSubscriber(child);
            this.worker = worker;
            child.add(Subscriptions.create(new Action0() { // from class: rx.internal.operators.OperatorWindowWithTime.ExactSubscriber.1
                @Override // rx.functions.Action0
                public void call() {
                    if (ExactSubscriber.this.state.consumer == null) {
                        ExactSubscriber.this.unsubscribe();
                    }
                }
            }));
        }

        @Override // rx.Subscriber
        public void onStart() {
            request(Long.MAX_VALUE);
        }

        @Override // rx.Observer
        public void onNext(T t) {
            List<Object> localQueue;
            synchronized (this.guard) {
                if (this.emitting) {
                    if (this.queue == null) {
                        this.queue = new ArrayList();
                    }
                    this.queue.add(t);
                    return;
                }
                this.emitting = true;
                boolean skipFinal = false;
                try {
                    if (!emitValue(t)) {
                        if (0 == 0) {
                            synchronized (this.guard) {
                                this.emitting = false;
                            }
                            return;
                        }
                        return;
                    }
                    do {
                        synchronized (this.guard) {
                            localQueue = this.queue;
                            if (localQueue == null) {
                                this.emitting = false;
                                skipFinal = true;
                                if (1 == 0) {
                                    synchronized (this.guard) {
                                        this.emitting = false;
                                    }
                                    return;
                                }
                                return;
                            }
                            this.queue = null;
                        }
                    } while (drain(localQueue));
                    if (0 == 0) {
                        synchronized (this.guard) {
                            this.emitting = false;
                        }
                    }
                } catch (Throwable th) {
                    if (!skipFinal) {
                        synchronized (this.guard) {
                            this.emitting = false;
                        }
                    }
                    throw th;
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        boolean drain(List<Object> queue) {
            if (queue == null) {
                return true;
            }
            for (Object o : queue) {
                if (o == OperatorWindowWithTime.NEXT_SUBJECT) {
                    if (!replaceSubject()) {
                        return false;
                    }
                } else {
                    if (OperatorWindowWithTime.nl.isError(o)) {
                        error(OperatorWindowWithTime.nl.getError(o));
                        return true;
                    }
                    if (OperatorWindowWithTime.nl.isCompleted(o)) {
                        complete();
                        return true;
                    }
                    if (!emitValue(o)) {
                        return false;
                    }
                }
            }
            return true;
        }

        boolean replaceSubject() {
            Observer<T> s = this.state.consumer;
            if (s != null) {
                s.onCompleted();
            }
            if (this.child.isUnsubscribed()) {
                this.state = this.state.clear();
                unsubscribe();
                return false;
            }
            UnicastSubject<T> bus = UnicastSubject.create();
            this.state = this.state.create(bus, bus);
            this.child.onNext(bus);
            return true;
        }

        boolean emitValue(T t) {
            State<T> s;
            State<T> s2 = this.state;
            if (s2.consumer == null) {
                if (!replaceSubject()) {
                    return false;
                }
                s2 = this.state;
            }
            s2.consumer.onNext(t);
            if (s2.count == OperatorWindowWithTime.this.size - 1) {
                s2.consumer.onCompleted();
                s = s2.clear();
            } else {
                s = s2.next();
            }
            this.state = s;
            return true;
        }

        @Override // rx.Observer
        public void onError(Throwable e) {
            synchronized (this.guard) {
                if (this.emitting) {
                    this.queue = Collections.singletonList(OperatorWindowWithTime.nl.error(e));
                    return;
                }
                this.queue = null;
                this.emitting = true;
                error(e);
            }
        }

        void error(Throwable e) {
            Observer<T> s = this.state.consumer;
            this.state = this.state.clear();
            if (s != null) {
                s.onError(e);
            }
            this.child.onError(e);
            unsubscribe();
        }

        void complete() {
            Observer<T> s = this.state.consumer;
            this.state = this.state.clear();
            if (s != null) {
                s.onCompleted();
            }
            this.child.onCompleted();
            unsubscribe();
        }

        @Override // rx.Observer
        public void onCompleted() {
            synchronized (this.guard) {
                if (this.emitting) {
                    if (this.queue == null) {
                        this.queue = new ArrayList();
                    }
                    this.queue.add(OperatorWindowWithTime.nl.completed());
                    return;
                }
                List<Object> localQueue = this.queue;
                this.queue = null;
                this.emitting = true;
                try {
                    drain(localQueue);
                    complete();
                } catch (Throwable e) {
                    error(e);
                }
            }
        }

        void scheduleExact() {
            this.worker.schedulePeriodically(new Action0() { // from class: rx.internal.operators.OperatorWindowWithTime.ExactSubscriber.2
                @Override // rx.functions.Action0
                public void call() {
                    ExactSubscriber.this.nextWindow();
                }
            }, 0L, OperatorWindowWithTime.this.timespan, OperatorWindowWithTime.this.unit);
        }

        void nextWindow() {
            List<Object> localQueue;
            synchronized (this.guard) {
                if (this.emitting) {
                    if (this.queue == null) {
                        this.queue = new ArrayList();
                    }
                    this.queue.add(OperatorWindowWithTime.NEXT_SUBJECT);
                    return;
                }
                this.emitting = true;
                boolean skipFinal = false;
                try {
                    if (!replaceSubject()) {
                        if (0 == 0) {
                            synchronized (this.guard) {
                                this.emitting = false;
                            }
                            return;
                        }
                        return;
                    }
                    do {
                        synchronized (this.guard) {
                            localQueue = this.queue;
                            if (localQueue == null) {
                                this.emitting = false;
                                skipFinal = true;
                                if (1 == 0) {
                                    synchronized (this.guard) {
                                        this.emitting = false;
                                    }
                                    return;
                                }
                                return;
                            }
                            this.queue = null;
                        }
                    } while (drain(localQueue));
                    if (0 == 0) {
                        synchronized (this.guard) {
                            this.emitting = false;
                        }
                    }
                } catch (Throwable th) {
                    if (!skipFinal) {
                        synchronized (this.guard) {
                            this.emitting = false;
                        }
                    }
                    throw th;
                }
            }
        }
    }

    static final class CountedSerializedSubject<T> {
        final Observer<T> consumer;
        int count;
        final Observable<T> producer;

        public CountedSerializedSubject(Observer<T> consumer, Observable<T> producer) {
            this.consumer = new SerializedObserver(consumer);
            this.producer = producer;
        }
    }

    final class InexactSubscriber extends Subscriber<T> {
        final Subscriber<? super Observable<T>> child;
        final List<CountedSerializedSubject<T>> chunks;
        boolean done;
        final Object guard;
        final Scheduler.Worker worker;

        public InexactSubscriber(Subscriber<? super Observable<T>> child, Scheduler.Worker worker) {
            super(child);
            this.child = child;
            this.worker = worker;
            this.guard = new Object();
            this.chunks = new LinkedList();
        }

        @Override // rx.Subscriber
        public void onStart() {
            request(Long.MAX_VALUE);
        }

        @Override // rx.Observer
        public void onNext(T t) {
            synchronized (this.guard) {
                if (!this.done) {
                    List<CountedSerializedSubject<T>> list = new ArrayList<>(this.chunks);
                    Iterator<CountedSerializedSubject<T>> it = this.chunks.iterator();
                    while (it.hasNext()) {
                        CountedSerializedSubject<T> cs = it.next();
                        int i = cs.count + 1;
                        cs.count = i;
                        if (i == OperatorWindowWithTime.this.size) {
                            it.remove();
                        }
                    }
                    for (CountedSerializedSubject<T> cs2 : list) {
                        cs2.consumer.onNext(t);
                        if (cs2.count == OperatorWindowWithTime.this.size) {
                            cs2.consumer.onCompleted();
                        }
                    }
                }
            }
        }

        @Override // rx.Observer
        public void onError(Throwable e) {
            synchronized (this.guard) {
                if (!this.done) {
                    this.done = true;
                    List<CountedSerializedSubject<T>> list = new ArrayList<>(this.chunks);
                    this.chunks.clear();
                    for (CountedSerializedSubject<T> cs : list) {
                        cs.consumer.onError(e);
                    }
                    this.child.onError(e);
                }
            }
        }

        @Override // rx.Observer
        public void onCompleted() {
            synchronized (this.guard) {
                if (!this.done) {
                    this.done = true;
                    List<CountedSerializedSubject<T>> list = new ArrayList<>(this.chunks);
                    this.chunks.clear();
                    for (CountedSerializedSubject<T> cs : list) {
                        cs.consumer.onCompleted();
                    }
                    this.child.onCompleted();
                }
            }
        }

        void scheduleChunk() {
            this.worker.schedulePeriodically(new Action0() { // from class: rx.internal.operators.OperatorWindowWithTime.InexactSubscriber.1
                @Override // rx.functions.Action0
                public void call() {
                    InexactSubscriber.this.startNewChunk();
                }
            }, OperatorWindowWithTime.this.timeshift, OperatorWindowWithTime.this.timeshift, OperatorWindowWithTime.this.unit);
        }

        void startNewChunk() {
            final CountedSerializedSubject<T> chunk = createCountedSerializedSubject();
            synchronized (this.guard) {
                if (!this.done) {
                    this.chunks.add(chunk);
                    try {
                        this.child.onNext(chunk.producer);
                        this.worker.schedule(new Action0() { // from class: rx.internal.operators.OperatorWindowWithTime.InexactSubscriber.2
                            @Override // rx.functions.Action0
                            public void call() {
                                InexactSubscriber.this.terminateChunk(chunk);
                            }
                        }, OperatorWindowWithTime.this.timespan, OperatorWindowWithTime.this.unit);
                    } catch (Throwable e) {
                        onError(e);
                    }
                }
            }
        }

        void terminateChunk(CountedSerializedSubject<T> chunk) {
            boolean terminate = false;
            synchronized (this.guard) {
                if (!this.done) {
                    Iterator<CountedSerializedSubject<T>> it = this.chunks.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        CountedSerializedSubject<T> cs = it.next();
                        if (cs == chunk) {
                            terminate = true;
                            it.remove();
                            break;
                        }
                    }
                    if (terminate) {
                        chunk.consumer.onCompleted();
                    }
                }
            }
        }

        CountedSerializedSubject<T> createCountedSerializedSubject() {
            UnicastSubject<T> bus = UnicastSubject.create();
            return new CountedSerializedSubject<>(bus, bus);
        }
    }
}
