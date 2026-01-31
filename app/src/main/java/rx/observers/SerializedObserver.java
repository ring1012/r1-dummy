package rx.observers;

import rx.Observer;
import rx.exceptions.Exceptions;
import rx.internal.operators.NotificationLite;

/* loaded from: classes.dex */
public class SerializedObserver<T> implements Observer<T> {
    private static final int MAX_DRAIN_ITERATION = 1024;
    private final Observer<? super T> actual;
    private boolean emitting;
    private final NotificationLite<T> nl = NotificationLite.instance();
    private FastList queue;
    private volatile boolean terminated;

    static final class FastList {
        Object[] array;
        int size;

        FastList() {
        }

        public void add(Object o) {
            int s = this.size;
            Object[] a2 = this.array;
            if (a2 == null) {
                a2 = new Object[16];
                this.array = a2;
            } else if (s == a2.length) {
                Object[] array2 = new Object[(s >> 2) + s];
                System.arraycopy(a2, 0, array2, 0, s);
                a2 = array2;
                this.array = a2;
            }
            a2[s] = o;
            this.size = s + 1;
        }
    }

    public SerializedObserver(Observer<? super T> s) {
        this.actual = s;
    }

    /* JADX WARN: Code restructure failed: missing block: B:64:0x0063, code lost:
    
        continue;
     */
    @Override // rx.Observer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void onNext(T r11) {
        /*
            r10 = this;
            r9 = 1
            boolean r7 = r10.terminated
            if (r7 == 0) goto L6
        L5:
            return
        L6:
            monitor-enter(r10)
            boolean r7 = r10.terminated     // Catch: java.lang.Throwable -> Ld
            if (r7 == 0) goto L10
            monitor-exit(r10)     // Catch: java.lang.Throwable -> Ld
            goto L5
        Ld:
            r7 = move-exception
            monitor-exit(r10)     // Catch: java.lang.Throwable -> Ld
            throw r7
        L10:
            boolean r7 = r10.emitting     // Catch: java.lang.Throwable -> Ld
            if (r7 == 0) goto L2a
            rx.observers.SerializedObserver$FastList r5 = r10.queue     // Catch: java.lang.Throwable -> Ld
            if (r5 != 0) goto L1f
            rx.observers.SerializedObserver$FastList r5 = new rx.observers.SerializedObserver$FastList     // Catch: java.lang.Throwable -> Ld
            r5.<init>()     // Catch: java.lang.Throwable -> Ld
            r10.queue = r5     // Catch: java.lang.Throwable -> Ld
        L1f:
            rx.internal.operators.NotificationLite<T> r7 = r10.nl     // Catch: java.lang.Throwable -> Ld
            java.lang.Object r7 = r7.next(r11)     // Catch: java.lang.Throwable -> Ld
            r5.add(r7)     // Catch: java.lang.Throwable -> Ld
            monitor-exit(r10)     // Catch: java.lang.Throwable -> Ld
            goto L5
        L2a:
            r7 = 1
            r10.emitting = r7     // Catch: java.lang.Throwable -> Ld
            monitor-exit(r10)     // Catch: java.lang.Throwable -> Ld
            rx.Observer<? super T> r7 = r10.actual     // Catch: java.lang.Throwable -> L45
            r7.onNext(r11)     // Catch: java.lang.Throwable -> L45
        L33:
            r2 = 0
        L34:
            r7 = 1024(0x400, float:1.435E-42)
            if (r2 >= r7) goto L33
            monitor-enter(r10)
            rx.observers.SerializedObserver$FastList r5 = r10.queue     // Catch: java.lang.Throwable -> L42
            if (r5 != 0) goto L55
            r7 = 0
            r10.emitting = r7     // Catch: java.lang.Throwable -> L42
            monitor-exit(r10)     // Catch: java.lang.Throwable -> L42
            goto L5
        L42:
            r7 = move-exception
            monitor-exit(r10)     // Catch: java.lang.Throwable -> L42
            throw r7
        L45:
            r1 = move-exception
            r10.terminated = r9
            rx.exceptions.Exceptions.throwIfFatal(r1)
            rx.Observer<? super T> r7 = r10.actual
            java.lang.Throwable r8 = rx.exceptions.OnErrorThrowable.addValueAsLastCause(r1, r11)
            r7.onError(r8)
            goto L5
        L55:
            r7 = 0
            r10.queue = r7     // Catch: java.lang.Throwable -> L42
            monitor-exit(r10)     // Catch: java.lang.Throwable -> L42
            java.lang.Object[] r0 = r5.array
            int r4 = r0.length
            r3 = 0
        L5d:
            if (r3 >= r4) goto L63
            r6 = r0[r3]
            if (r6 != 0) goto L66
        L63:
            int r2 = r2 + 1
            goto L34
        L66:
            rx.internal.operators.NotificationLite<T> r7 = r10.nl     // Catch: java.lang.Throwable -> L74
            rx.Observer<? super T> r8 = r10.actual     // Catch: java.lang.Throwable -> L74
            boolean r7 = r7.accept(r8, r6)     // Catch: java.lang.Throwable -> L74
            if (r7 == 0) goto L84
            r7 = 1
            r10.terminated = r7     // Catch: java.lang.Throwable -> L74
            goto L5
        L74:
            r1 = move-exception
            r10.terminated = r9
            rx.exceptions.Exceptions.throwIfFatal(r1)
            rx.Observer<? super T> r7 = r10.actual
            java.lang.Throwable r8 = rx.exceptions.OnErrorThrowable.addValueAsLastCause(r1, r11)
            r7.onError(r8)
            goto L5
        L84:
            int r3 = r3 + 1
            goto L5d
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.observers.SerializedObserver.onNext(java.lang.Object):void");
    }

    @Override // rx.Observer
    public void onError(Throwable e) {
        Exceptions.throwIfFatal(e);
        if (!this.terminated) {
            synchronized (this) {
                if (!this.terminated) {
                    this.terminated = true;
                    if (this.emitting) {
                        FastList list = this.queue;
                        if (list == null) {
                            list = new FastList();
                            this.queue = list;
                        }
                        list.add(this.nl.error(e));
                        return;
                    }
                    this.emitting = true;
                    this.actual.onError(e);
                }
            }
        }
    }

    @Override // rx.Observer
    public void onCompleted() {
        if (!this.terminated) {
            synchronized (this) {
                if (!this.terminated) {
                    this.terminated = true;
                    if (this.emitting) {
                        FastList list = this.queue;
                        if (list == null) {
                            list = new FastList();
                            this.queue = list;
                        }
                        list.add(this.nl.completed());
                        return;
                    }
                    this.emitting = true;
                    this.actual.onCompleted();
                }
            }
        }
    }
}
