package rx.internal.util.atomic;

import java.util.concurrent.atomic.AtomicReference;

/* loaded from: classes.dex */
public final class LinkedQueueNode<E> extends AtomicReference<LinkedQueueNode<E>> {
    private static final long serialVersionUID = 2404266111789071508L;
    private E value;

    public LinkedQueueNode() {
    }

    public LinkedQueueNode(E val) {
        spValue(val);
    }

    public E getAndNullValue() {
        E temp = lpValue();
        spValue(null);
        return temp;
    }

    public E lpValue() {
        return this.value;
    }

    public void spValue(E newValue) {
        this.value = newValue;
    }

    public void soNext(LinkedQueueNode<E> n) {
        lazySet(n);
    }

    public LinkedQueueNode<E> lvNext() {
        return get();
    }
}
