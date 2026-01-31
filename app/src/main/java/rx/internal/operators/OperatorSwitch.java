package rx.internal.operators;

import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.Producer;
import rx.Subscriber;
import rx.internal.producers.ProducerArbiter;
import rx.observers.SerializedSubscriber;
import rx.subscriptions.SerialSubscription;

/* loaded from: classes.dex */
public final class OperatorSwitch<T> implements Observable.Operator<T, Observable<? extends T>> {

    private static final class Holder {
        static final OperatorSwitch<Object> INSTANCE = new OperatorSwitch<>();

        private Holder() {
        }
    }

    public static <T> OperatorSwitch<T> instance() {
        return (OperatorSwitch<T>) Holder.INSTANCE;
    }

    private OperatorSwitch() {
    }

    @Override // rx.functions.Func1
    public Subscriber<? super Observable<? extends T>> call(Subscriber<? super T> child) {
        SwitchSubscriber<T> sws = new SwitchSubscriber<>(child);
        child.add(sws);
        return sws;
    }

    private static final class SwitchSubscriber<T> extends Subscriber<Observable<? extends T>> {
        boolean active;
        InnerSubscriber<T> currentSubscriber;
        boolean emitting;
        int index;
        boolean mainDone;
        List<Object> queue;
        final SerializedSubscriber<T> serializedChild;
        final Object guard = new Object();
        final NotificationLite<?> nl = NotificationLite.instance();
        final ProducerArbiter arbiter = new ProducerArbiter();
        final SerialSubscription ssub = new SerialSubscription();

        SwitchSubscriber(Subscriber<? super T> child) {
            this.serializedChild = new SerializedSubscriber<>(child);
            child.add(this.ssub);
            child.setProducer(new Producer() { // from class: rx.internal.operators.OperatorSwitch.SwitchSubscriber.1
                @Override // rx.Producer
                public void request(long n) {
                    if (n > 0) {
                        SwitchSubscriber.this.arbiter.request(n);
                    }
                }
            });
        }

        @Override // rx.Observer
        public void onNext(Observable<? extends T> t) {
            synchronized (this.guard) {
                int id = this.index + 1;
                this.index = id;
                this.active = true;
                this.currentSubscriber = new InnerSubscriber<>(id, this.arbiter, this);
            }
            this.ssub.set(this.currentSubscriber);
            t.unsafeSubscribe(this.currentSubscriber);
        }

        @Override // rx.Observer
        public void onError(Throwable e) {
            this.serializedChild.onError(e);
            unsubscribe();
        }

        @Override // rx.Observer
        public void onCompleted() {
            synchronized (this.guard) {
                this.mainDone = true;
                if (!this.active) {
                    if (this.emitting) {
                        if (this.queue == null) {
                            this.queue = new ArrayList();
                        }
                        this.queue.add(this.nl.completed());
                    } else {
                        List<Object> localQueue = this.queue;
                        this.queue = null;
                        this.emitting = true;
                        drain(localQueue);
                        this.serializedChild.onCompleted();
                        unsubscribe();
                    }
                }
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:29:0x0049, code lost:
        
            r6.emitting = false;
            r2 = true;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        void emit(T r7, int r8, rx.internal.operators.OperatorSwitch.InnerSubscriber<T> r9) {
            /*
                r6 = this;
                java.lang.Object r4 = r6.guard
                monitor-enter(r4)
                int r3 = r6.index     // Catch: java.lang.Throwable -> L1f
                if (r8 == r3) goto L9
                monitor-exit(r4)     // Catch: java.lang.Throwable -> L1f
            L8:
                return
            L9:
                boolean r3 = r6.emitting     // Catch: java.lang.Throwable -> L1f
                if (r3 == 0) goto L22
                java.util.List<java.lang.Object> r3 = r6.queue     // Catch: java.lang.Throwable -> L1f
                if (r3 != 0) goto L18
                java.util.ArrayList r3 = new java.util.ArrayList     // Catch: java.lang.Throwable -> L1f
                r3.<init>()     // Catch: java.lang.Throwable -> L1f
                r6.queue = r3     // Catch: java.lang.Throwable -> L1f
            L18:
                java.util.List<java.lang.Object> r3 = r6.queue     // Catch: java.lang.Throwable -> L1f
                r3.add(r7)     // Catch: java.lang.Throwable -> L1f
                monitor-exit(r4)     // Catch: java.lang.Throwable -> L1f
                goto L8
            L1f:
                r3 = move-exception
                monitor-exit(r4)     // Catch: java.lang.Throwable -> L1f
                throw r3
            L22:
                java.util.List<java.lang.Object> r0 = r6.queue     // Catch: java.lang.Throwable -> L1f
                r3 = 0
                r6.queue = r3     // Catch: java.lang.Throwable -> L1f
                r3 = 1
                r6.emitting = r3     // Catch: java.lang.Throwable -> L1f
                monitor-exit(r4)     // Catch: java.lang.Throwable -> L1f
                r1 = 1
                r2 = 0
            L2d:
                r6.drain(r0)     // Catch: java.lang.Throwable -> L68
                if (r1 == 0) goto L3f
                r1 = 0
                rx.observers.SerializedSubscriber<T> r3 = r6.serializedChild     // Catch: java.lang.Throwable -> L68
                r3.onNext(r7)     // Catch: java.lang.Throwable -> L68
                rx.internal.producers.ProducerArbiter r3 = r6.arbiter     // Catch: java.lang.Throwable -> L68
                r4 = 1
                r3.produced(r4)     // Catch: java.lang.Throwable -> L68
            L3f:
                java.lang.Object r4 = r6.guard     // Catch: java.lang.Throwable -> L68
                monitor-enter(r4)     // Catch: java.lang.Throwable -> L68
                java.util.List<java.lang.Object> r0 = r6.queue     // Catch: java.lang.Throwable -> L65
                r3 = 0
                r6.queue = r3     // Catch: java.lang.Throwable -> L65
                if (r0 != 0) goto L5b
                r3 = 0
                r6.emitting = r3     // Catch: java.lang.Throwable -> L65
                r2 = 1
                monitor-exit(r4)     // Catch: java.lang.Throwable -> L65
            L4e:
                if (r2 != 0) goto L8
                java.lang.Object r4 = r6.guard
                monitor-enter(r4)
                r3 = 0
                r6.emitting = r3     // Catch: java.lang.Throwable -> L58
                monitor-exit(r4)     // Catch: java.lang.Throwable -> L58
                goto L8
            L58:
                r3 = move-exception
                monitor-exit(r4)     // Catch: java.lang.Throwable -> L58
                throw r3
            L5b:
                monitor-exit(r4)     // Catch: java.lang.Throwable -> L65
                rx.observers.SerializedSubscriber<T> r3 = r6.serializedChild     // Catch: java.lang.Throwable -> L68
                boolean r3 = r3.isUnsubscribed()     // Catch: java.lang.Throwable -> L68
                if (r3 == 0) goto L2d
                goto L4e
            L65:
                r3 = move-exception
                monitor-exit(r4)     // Catch: java.lang.Throwable -> L65
                throw r3     // Catch: java.lang.Throwable -> L68
            L68:
                r3 = move-exception
                if (r2 != 0) goto L72
                java.lang.Object r4 = r6.guard
                monitor-enter(r4)
                r5 = 0
                r6.emitting = r5     // Catch: java.lang.Throwable -> L73
                monitor-exit(r4)     // Catch: java.lang.Throwable -> L73
            L72:
                throw r3
            L73:
                r3 = move-exception
                monitor-exit(r4)     // Catch: java.lang.Throwable -> L73
                throw r3
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorSwitch.SwitchSubscriber.emit(java.lang.Object, int, rx.internal.operators.OperatorSwitch$InnerSubscriber):void");
        }

        /* JADX WARN: Multi-variable type inference failed */
        void drain(List<Object> list) {
            if (list != null) {
                for (Object obj : list) {
                    if (this.nl.isCompleted(obj)) {
                        this.serializedChild.onCompleted();
                        return;
                    } else if (this.nl.isError(obj)) {
                        this.serializedChild.onError(this.nl.getError(obj));
                        return;
                    } else {
                        this.serializedChild.onNext(obj);
                        this.arbiter.produced(1L);
                    }
                }
            }
        }

        void error(Throwable e, int id) {
            synchronized (this.guard) {
                if (id == this.index) {
                    if (this.emitting) {
                        if (this.queue == null) {
                            this.queue = new ArrayList();
                        }
                        this.queue.add(this.nl.error(e));
                    } else {
                        List<Object> localQueue = this.queue;
                        this.queue = null;
                        this.emitting = true;
                        drain(localQueue);
                        this.serializedChild.onError(e);
                        unsubscribe();
                    }
                }
            }
        }

        void complete(int id) {
            synchronized (this.guard) {
                if (id == this.index) {
                    this.active = false;
                    if (this.mainDone) {
                        if (this.emitting) {
                            if (this.queue == null) {
                                this.queue = new ArrayList();
                            }
                            this.queue.add(this.nl.completed());
                        } else {
                            List<Object> localQueue = this.queue;
                            this.queue = null;
                            this.emitting = true;
                            drain(localQueue);
                            this.serializedChild.onCompleted();
                            unsubscribe();
                        }
                    }
                }
            }
        }
    }

    private static final class InnerSubscriber<T> extends Subscriber<T> {
        private final ProducerArbiter arbiter;
        private final int id;
        private final SwitchSubscriber<T> parent;

        InnerSubscriber(int id, ProducerArbiter arbiter, SwitchSubscriber<T> parent) {
            this.id = id;
            this.arbiter = arbiter;
            this.parent = parent;
        }

        @Override // rx.Subscriber
        public void setProducer(Producer p) {
            this.arbiter.setProducer(p);
        }

        @Override // rx.Observer
        public void onNext(T t) {
            this.parent.emit(t, this.id, this);
        }

        @Override // rx.Observer
        public void onError(Throwable e) {
            this.parent.error(e, this.id);
        }

        @Override // rx.Observer
        public void onCompleted() {
            this.parent.complete(this.id);
        }
    }
}
