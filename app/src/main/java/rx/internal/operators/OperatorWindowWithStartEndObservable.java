package rx.internal.operators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Func1;
import rx.observers.SerializedObserver;
import rx.observers.SerializedSubscriber;
import rx.subscriptions.CompositeSubscription;

/* loaded from: classes.dex */
public final class OperatorWindowWithStartEndObservable<T, U, V> implements Observable.Operator<Observable<T>, T> {
    final Func1<? super U, ? extends Observable<? extends V>> windowClosingSelector;
    final Observable<? extends U> windowOpenings;

    public OperatorWindowWithStartEndObservable(Observable<? extends U> windowOpenings, Func1<? super U, ? extends Observable<? extends V>> windowClosingSelector) {
        this.windowOpenings = windowOpenings;
        this.windowClosingSelector = windowClosingSelector;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(Subscriber<? super Observable<T>> child) {
        CompositeSubscription csub = new CompositeSubscription();
        child.add(csub);
        final OperatorWindowWithStartEndObservable<T, U, V>.SourceSubscriber sub = new SourceSubscriber(child, csub);
        Subscriber<U> open = new Subscriber<U>() { // from class: rx.internal.operators.OperatorWindowWithStartEndObservable.1
            @Override // rx.Subscriber
            public void onStart() {
                request(Long.MAX_VALUE);
            }

            @Override // rx.Observer
            public void onNext(U t) throws Throwable {
                sub.beginWindow(t);
            }

            @Override // rx.Observer
            public void onError(Throwable e) throws Throwable {
                sub.onError(e);
            }

            @Override // rx.Observer
            public void onCompleted() throws Throwable {
                sub.onCompleted();
            }
        };
        csub.add(sub);
        csub.add(open);
        this.windowOpenings.unsafeSubscribe(open);
        return sub;
    }

    static final class SerializedSubject<T> {
        final Observer<T> consumer;
        final Observable<T> producer;

        public SerializedSubject(Observer<T> consumer, Observable<T> producer) {
            this.consumer = new SerializedObserver(consumer);
            this.producer = producer;
        }
    }

    final class SourceSubscriber extends Subscriber<T> {
        final Subscriber<? super Observable<T>> child;
        final CompositeSubscription csub;
        boolean done;
        final Object guard = new Object();
        final List<SerializedSubject<T>> chunks = new LinkedList();

        public SourceSubscriber(Subscriber<? super Observable<T>> child, CompositeSubscription csub) {
            this.child = new SerializedSubscriber(child);
            this.csub = csub;
        }

        @Override // rx.Subscriber
        public void onStart() {
            request(Long.MAX_VALUE);
        }

        @Override // rx.Observer
        public void onNext(T t) {
            synchronized (this.guard) {
                if (!this.done) {
                    List<SerializedSubject<T>> list = new ArrayList<>(this.chunks);
                    for (SerializedSubject<T> cs : list) {
                        cs.consumer.onNext(t);
                    }
                }
            }
        }

        @Override // rx.Observer
        public void onError(Throwable e) throws Throwable {
            try {
                synchronized (this.guard) {
                    if (!this.done) {
                        this.done = true;
                        List<SerializedSubject<T>> list = new ArrayList<>(this.chunks);
                        this.chunks.clear();
                        for (SerializedSubject<T> cs : list) {
                            cs.consumer.onError(e);
                        }
                        this.child.onError(e);
                    }
                }
            } finally {
                this.csub.unsubscribe();
            }
        }

        @Override // rx.Observer
        public void onCompleted() throws Throwable {
            try {
                synchronized (this.guard) {
                    if (!this.done) {
                        this.done = true;
                        List<SerializedSubject<T>> list = new ArrayList<>(this.chunks);
                        this.chunks.clear();
                        for (SerializedSubject<T> cs : list) {
                            cs.consumer.onCompleted();
                        }
                        this.child.onCompleted();
                    }
                }
            } finally {
                this.csub.unsubscribe();
            }
        }

        void beginWindow(U token) throws Throwable {
            final SerializedSubject<T> s = createSerializedSubject();
            synchronized (this.guard) {
                if (!this.done) {
                    this.chunks.add(s);
                    this.child.onNext(s.producer);
                    try {
                        Observable<? extends V> end = OperatorWindowWithStartEndObservable.this.windowClosingSelector.call(token);
                        Subscriber<V> v = new Subscriber<V>() { // from class: rx.internal.operators.OperatorWindowWithStartEndObservable.SourceSubscriber.1
                            boolean once = true;

                            @Override // rx.Observer
                            public void onNext(V t) {
                                onCompleted();
                            }

                            @Override // rx.Observer
                            public void onError(Throwable e) {
                            }

                            @Override // rx.Observer
                            public void onCompleted() {
                                if (this.once) {
                                    this.once = false;
                                    SourceSubscriber.this.endWindow(s);
                                    SourceSubscriber.this.csub.remove(this);
                                }
                            }
                        };
                        this.csub.add(v);
                        end.unsafeSubscribe(v);
                    } catch (Throwable e) {
                        onError(e);
                    }
                }
            }
        }

        void endWindow(SerializedSubject<T> window) {
            boolean terminate = false;
            synchronized (this.guard) {
                if (!this.done) {
                    Iterator<SerializedSubject<T>> it = this.chunks.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        SerializedSubject<T> s = it.next();
                        if (s == window) {
                            terminate = true;
                            it.remove();
                            break;
                        }
                    }
                    if (terminate) {
                        window.consumer.onCompleted();
                    }
                }
            }
        }

        SerializedSubject<T> createSerializedSubject() {
            UnicastSubject<T> bus = UnicastSubject.create();
            return new SerializedSubject<>(bus, bus);
        }
    }
}
