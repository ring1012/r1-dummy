package rx.internal.operators;

import java.util.Deque;
import java.util.concurrent.atomic.AtomicLong;
import rx.Producer;
import rx.Subscriber;

/* loaded from: classes.dex */
final class TakeLastQueueProducer<T> extends AtomicLong implements Producer {
    private final Deque<Object> deque;
    private volatile boolean emittingStarted = false;
    private final NotificationLite<T> notification;
    private final Subscriber<? super T> subscriber;

    public TakeLastQueueProducer(NotificationLite<T> n, Deque<Object> q, Subscriber<? super T> subscriber) {
        this.notification = n;
        this.deque = q;
        this.subscriber = subscriber;
    }

    void startEmitting() {
        if (!this.emittingStarted) {
            this.emittingStarted = true;
            emit(0L);
        }
    }

    @Override // rx.Producer
    public void request(long n) {
        long _c;
        if (get() != Long.MAX_VALUE) {
            if (n == Long.MAX_VALUE) {
                _c = getAndSet(Long.MAX_VALUE);
            } else {
                _c = BackpressureUtils.getAndAddRequest(this, n);
            }
            if (this.emittingStarted) {
                emit(_c);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:34:0x009d, code lost:
    
        r10 = get();
        r6 = r10 - r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00ab, code lost:
    
        if (r10 == Long.MAX_VALUE) goto L51;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00b3, code lost:
    
        if (compareAndSet(r10, r6) == false) goto L56;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00b9, code lost:
    
        if (r6 != 0) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:?, code lost:
    
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void emit(long r20) {
        /*
            r19 = this;
            long r14 = r19.get()
            r16 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            int r13 = (r14 > r16 ? 1 : (r14 == r16 ? 0 : -1))
            if (r13 != 0) goto L64
            r14 = 0
            int r13 = (r20 > r14 ? 1 : (r20 == r14 ? 0 : -1))
            if (r13 != 0) goto L36
            r0 = r19
            java.util.Deque<java.lang.Object> r13 = r0.deque     // Catch: java.lang.Throwable -> L43 java.lang.Throwable -> L5b
            java.util.Iterator r4 = r13.iterator()     // Catch: java.lang.Throwable -> L43 java.lang.Throwable -> L5b
        L1b:
            boolean r13 = r4.hasNext()     // Catch: java.lang.Throwable -> L43 java.lang.Throwable -> L5b
            if (r13 == 0) goto L53
            java.lang.Object r12 = r4.next()     // Catch: java.lang.Throwable -> L43 java.lang.Throwable -> L5b
            r0 = r19
            rx.Subscriber<? super T> r13 = r0.subscriber     // Catch: java.lang.Throwable -> L43 java.lang.Throwable -> L5b
            boolean r13 = r13.isUnsubscribed()     // Catch: java.lang.Throwable -> L43 java.lang.Throwable -> L5b
            if (r13 == 0) goto L37
            r0 = r19
            java.util.Deque<java.lang.Object> r13 = r0.deque
            r13.clear()
        L36:
            return
        L37:
            r0 = r19
            rx.internal.operators.NotificationLite<T> r13 = r0.notification     // Catch: java.lang.Throwable -> L43 java.lang.Throwable -> L5b
            r0 = r19
            rx.Subscriber<? super T> r14 = r0.subscriber     // Catch: java.lang.Throwable -> L43 java.lang.Throwable -> L5b
            r13.accept(r14, r12)     // Catch: java.lang.Throwable -> L43 java.lang.Throwable -> L5b
            goto L1b
        L43:
            r2 = move-exception
            r0 = r19
            rx.Subscriber<? super T> r13 = r0.subscriber     // Catch: java.lang.Throwable -> L5b
            rx.exceptions.Exceptions.throwOrReport(r2, r13)     // Catch: java.lang.Throwable -> L5b
            r0 = r19
            java.util.Deque<java.lang.Object> r13 = r0.deque
            r13.clear()
            goto L36
        L53:
            r0 = r19
            java.util.Deque<java.lang.Object> r13 = r0.deque
            r13.clear()
            goto L36
        L5b:
            r13 = move-exception
            r0 = r19
            java.util.Deque<java.lang.Object> r14 = r0.deque
            r14.clear()
            throw r13
        L64:
            r14 = 0
            int r13 = (r20 > r14 ? 1 : (r20 == r14 ? 0 : -1))
            if (r13 != 0) goto L36
        L6a:
            long r8 = r19.get()
            r3 = 0
        L6f:
            r14 = 1
            long r8 = r8 - r14
            r14 = 0
            int r13 = (r8 > r14 ? 1 : (r8 == r14 ? 0 : -1))
            if (r13 < 0) goto L9d
            r0 = r19
            java.util.Deque<java.lang.Object> r13 = r0.deque
            java.lang.Object r5 = r13.poll()
            if (r5 == 0) goto L9d
            r0 = r19
            rx.Subscriber<? super T> r13 = r0.subscriber
            boolean r13 = r13.isUnsubscribed()
            if (r13 != 0) goto L36
            r0 = r19
            rx.internal.operators.NotificationLite<T> r13 = r0.notification
            r0 = r19
            rx.Subscriber<? super T> r14 = r0.subscriber
            boolean r13 = r13.accept(r14, r5)
            if (r13 != 0) goto L36
            int r3 = r3 + 1
            goto L6f
        L9d:
            long r10 = r19.get()
            long r14 = (long) r3
            long r6 = r10 - r14
            r14 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            int r13 = (r10 > r14 ? 1 : (r10 == r14 ? 0 : -1))
            if (r13 == 0) goto L6a
            r0 = r19
            boolean r13 = r0.compareAndSet(r10, r6)
            if (r13 == 0) goto L9d
            r14 = 0
            int r13 = (r6 > r14 ? 1 : (r6 == r14 ? 0 : -1))
            if (r13 != 0) goto L6a
            goto L36
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.TakeLastQueueProducer.emit(long):void");
    }
}
