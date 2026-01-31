package rx.internal.operators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import rx.Observable;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

/* loaded from: classes.dex */
public final class OperatorWindowWithSize<T> implements Observable.Operator<Observable<T>, T> {
    final int size;
    final int skip;

    public OperatorWindowWithSize(int size, int skip) {
        this.size = size;
        this.skip = skip;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(Subscriber<? super Observable<T>> child) {
        if (this.skip == this.size) {
            OperatorWindowWithSize<T>.ExactSubscriber e = new ExactSubscriber(child);
            e.init();
            return e;
        }
        OperatorWindowWithSize<T>.InexactSubscriber ie = new InexactSubscriber(child);
        ie.init();
        return ie;
    }

    final class ExactSubscriber extends Subscriber<T> {
        final Subscriber<? super Observable<T>> child;
        int count;
        volatile boolean noWindow = true;
        UnicastSubject<T> window;

        public ExactSubscriber(Subscriber<? super Observable<T>> child) {
            this.child = child;
        }

        void init() {
            this.child.add(Subscriptions.create(new Action0() { // from class: rx.internal.operators.OperatorWindowWithSize.ExactSubscriber.1
                @Override // rx.functions.Action0
                public void call() {
                    if (ExactSubscriber.this.noWindow) {
                        ExactSubscriber.this.unsubscribe();
                    }
                }
            }));
            this.child.setProducer(new Producer() { // from class: rx.internal.operators.OperatorWindowWithSize.ExactSubscriber.2
                @Override // rx.Producer
                public void request(long n) {
                    if (n > 0) {
                        long u = n * OperatorWindowWithSize.this.size;
                        if ((u >>> 31) != 0 && u / n != OperatorWindowWithSize.this.size) {
                            u = Long.MAX_VALUE;
                        }
                        ExactSubscriber.this.requestMore(u);
                    }
                }
            });
        }

        void requestMore(long n) {
            request(n);
        }

        @Override // rx.Observer
        public void onNext(T t) {
            if (this.window == null) {
                this.noWindow = false;
                this.window = UnicastSubject.create();
                this.child.onNext(this.window);
            }
            this.window.onNext(t);
            int i = this.count + 1;
            this.count = i;
            if (i % OperatorWindowWithSize.this.size == 0) {
                this.window.onCompleted();
                this.window = null;
                this.noWindow = true;
                if (this.child.isUnsubscribed()) {
                    unsubscribe();
                }
            }
        }

        @Override // rx.Observer
        public void onError(Throwable e) {
            if (this.window != null) {
                this.window.onError(e);
            }
            this.child.onError(e);
        }

        @Override // rx.Observer
        public void onCompleted() {
            if (this.window != null) {
                this.window.onCompleted();
            }
            this.child.onCompleted();
        }
    }

    final class InexactSubscriber extends Subscriber<T> {
        final Subscriber<? super Observable<T>> child;
        int count;
        final List<CountedSubject<T>> chunks = new LinkedList();
        volatile boolean noWindow = true;

        public InexactSubscriber(Subscriber<? super Observable<T>> child) {
            this.child = child;
        }

        void init() {
            this.child.add(Subscriptions.create(new Action0() { // from class: rx.internal.operators.OperatorWindowWithSize.InexactSubscriber.1
                @Override // rx.functions.Action0
                public void call() {
                    if (InexactSubscriber.this.noWindow) {
                        InexactSubscriber.this.unsubscribe();
                    }
                }
            }));
            this.child.setProducer(new Producer() { // from class: rx.internal.operators.OperatorWindowWithSize.InexactSubscriber.2
                @Override // rx.Producer
                public void request(long n) {
                    if (n > 0) {
                        long u = n * OperatorWindowWithSize.this.size;
                        if ((u >>> 31) != 0 && u / n != OperatorWindowWithSize.this.size) {
                            u = Long.MAX_VALUE;
                        }
                        InexactSubscriber.this.requestMore(u);
                    }
                }
            });
        }

        void requestMore(long n) {
            request(n);
        }

        @Override // rx.Observer
        public void onNext(T t) {
            int i = this.count;
            this.count = i + 1;
            if (i % OperatorWindowWithSize.this.skip == 0 && !this.child.isUnsubscribed()) {
                if (this.chunks.isEmpty()) {
                    this.noWindow = false;
                }
                CountedSubject<T> cs = createCountedSubject();
                this.chunks.add(cs);
                this.child.onNext(cs.producer);
            }
            Iterator<CountedSubject<T>> it = this.chunks.iterator();
            while (it.hasNext()) {
                CountedSubject<T> cs2 = it.next();
                cs2.consumer.onNext(t);
                int i2 = cs2.count + 1;
                cs2.count = i2;
                if (i2 == OperatorWindowWithSize.this.size) {
                    it.remove();
                    cs2.consumer.onCompleted();
                }
            }
            if (this.chunks.isEmpty()) {
                this.noWindow = true;
                if (this.child.isUnsubscribed()) {
                    unsubscribe();
                }
            }
        }

        @Override // rx.Observer
        public void onError(Throwable e) {
            List<CountedSubject<T>> list = new ArrayList<>(this.chunks);
            this.chunks.clear();
            this.noWindow = true;
            for (CountedSubject<T> cs : list) {
                cs.consumer.onError(e);
            }
            this.child.onError(e);
        }

        @Override // rx.Observer
        public void onCompleted() {
            List<CountedSubject<T>> list = new ArrayList<>(this.chunks);
            this.chunks.clear();
            this.noWindow = true;
            for (CountedSubject<T> cs : list) {
                cs.consumer.onCompleted();
            }
            this.child.onCompleted();
        }

        CountedSubject<T> createCountedSubject() {
            UnicastSubject<T> bus = UnicastSubject.create();
            return new CountedSubject<>(bus, bus);
        }
    }

    static final class CountedSubject<T> {
        final Observer<T> consumer;
        int count;
        final Observable<T> producer;

        public CountedSubject(Observer<T> consumer, Observable<T> producer) {
            this.consumer = consumer;
            this.producer = producer;
        }
    }
}
