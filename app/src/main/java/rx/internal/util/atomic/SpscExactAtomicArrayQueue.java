package rx.internal.util.atomic;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceArray;
import rx.internal.util.unsafe.Pow2;

/* loaded from: classes.dex */
public final class SpscExactAtomicArrayQueue<T> extends AtomicReferenceArray<T> implements Queue<T> {
    private static final long serialVersionUID = 6210984603741293445L;
    final int capacitySkip;
    volatile long consumerIndex;
    final int mask;
    volatile long producerIndex;
    static final AtomicLongFieldUpdater<SpscExactAtomicArrayQueue> PRODUCER_INDEX = AtomicLongFieldUpdater.newUpdater(SpscExactAtomicArrayQueue.class, "producerIndex");
    static final AtomicLongFieldUpdater<SpscExactAtomicArrayQueue> CONSUMER_INDEX = AtomicLongFieldUpdater.newUpdater(SpscExactAtomicArrayQueue.class, "consumerIndex");

    public SpscExactAtomicArrayQueue(int capacity) {
        super(Pow2.roundToPowerOfTwo(capacity));
        int len = length();
        this.mask = len - 1;
        this.capacitySkip = len - capacity;
    }

    @Override // java.util.Queue
    public boolean offer(T value) {
        if (value == null) {
            throw new NullPointerException();
        }
        long pi = this.producerIndex;
        int m = this.mask;
        int fullCheck = ((int) (this.capacitySkip + pi)) & m;
        if (get(fullCheck) != null) {
            return false;
        }
        int offset = ((int) pi) & m;
        PRODUCER_INDEX.lazySet(this, 1 + pi);
        lazySet(offset, value);
        return true;
    }

    @Override // java.util.Queue
    public T poll() {
        long ci = this.consumerIndex;
        int offset = ((int) ci) & this.mask;
        T value = get(offset);
        if (value == null) {
            return null;
        }
        CONSUMER_INDEX.lazySet(this, 1 + ci);
        lazySet(offset, null);
        return value;
    }

    @Override // java.util.Queue
    public T peek() {
        return get(((int) this.consumerIndex) & this.mask);
    }

    @Override // java.util.Collection
    public void clear() {
        while (true) {
            if (poll() == null && isEmpty()) {
                return;
            }
        }
    }

    @Override // java.util.Collection
    public boolean isEmpty() {
        return this.producerIndex == this.consumerIndex;
    }

    @Override // java.util.Collection
    public int size() {
        long ci = this.consumerIndex;
        while (true) {
            long pi = this.producerIndex;
            long ci2 = this.consumerIndex;
            if (ci == ci2) {
                return (int) (pi - ci2);
            }
            ci = ci2;
        }
    }

    @Override // java.util.Collection
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Collection, java.lang.Iterable
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Collection
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Collection
    public <E> E[] toArray(E[] a2) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Collection
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Collection
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Collection
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Collection
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Collection
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Queue, java.util.Collection
    public boolean add(T e) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Queue
    public T remove() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Queue
    public T element() {
        throw new UnsupportedOperationException();
    }
}
