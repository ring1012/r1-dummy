package rx.internal.operators;

import java.util.ArrayDeque;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;

/* loaded from: classes.dex */
public final class OperatorTakeLastTimed<T> implements Observable.Operator<T, T> {
    private final long ageMillis;
    private final int count;
    private final Scheduler scheduler;

    public OperatorTakeLastTimed(long time, TimeUnit unit, Scheduler scheduler) {
        this.ageMillis = unit.toMillis(time);
        this.scheduler = scheduler;
        this.count = -1;
    }

    public OperatorTakeLastTimed(int count, long time, TimeUnit unit, Scheduler scheduler) {
        if (count < 0) {
            throw new IndexOutOfBoundsException("count could not be negative");
        }
        this.ageMillis = unit.toMillis(time);
        this.scheduler = scheduler;
        this.count = count;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        final ArrayDeque arrayDeque = new ArrayDeque();
        final ArrayDeque arrayDeque2 = new ArrayDeque();
        final NotificationLite notificationLiteInstance = NotificationLite.instance();
        final TakeLastQueueProducer takeLastQueueProducer = new TakeLastQueueProducer(notificationLiteInstance, arrayDeque, subscriber);
        subscriber.setProducer(takeLastQueueProducer);
        return new Subscriber<T>(subscriber) { // from class: rx.internal.operators.OperatorTakeLastTimed.1
            protected void runEvictionPolicy(long now) {
                while (OperatorTakeLastTimed.this.count >= 0 && arrayDeque.size() > OperatorTakeLastTimed.this.count) {
                    arrayDeque2.pollFirst();
                    arrayDeque.pollFirst();
                }
                while (!arrayDeque.isEmpty()) {
                    long v = ((Long) arrayDeque2.peekFirst()).longValue();
                    if (v < now - OperatorTakeLastTimed.this.ageMillis) {
                        arrayDeque2.pollFirst();
                        arrayDeque.pollFirst();
                    } else {
                        return;
                    }
                }
            }

            @Override // rx.Subscriber
            public void onStart() {
                request(Long.MAX_VALUE);
            }

            @Override // rx.Observer
            public void onNext(T args) {
                long t = OperatorTakeLastTimed.this.scheduler.now();
                arrayDeque2.add(Long.valueOf(t));
                arrayDeque.add(notificationLiteInstance.next(args));
                runEvictionPolicy(t);
            }

            @Override // rx.Observer
            public void onError(Throwable e) {
                arrayDeque2.clear();
                arrayDeque.clear();
                subscriber.onError(e);
            }

            @Override // rx.Observer
            public void onCompleted() {
                runEvictionPolicy(OperatorTakeLastTimed.this.scheduler.now());
                arrayDeque2.clear();
                arrayDeque.offer(notificationLiteInstance.completed());
                takeLastQueueProducer.startEmitting();
            }
        };
    }
}
