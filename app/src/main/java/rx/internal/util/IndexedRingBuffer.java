package rx.internal.util;

import a.a.a.a;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import rx.Subscription;
import rx.functions.Func1;

/* loaded from: classes.dex */
public final class IndexedRingBuffer<E> implements Subscription {
    private static final ObjectPool<IndexedRingBuffer<?>> POOL = new ObjectPool<IndexedRingBuffer<?>>() { // from class: rx.internal.util.IndexedRingBuffer.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // rx.internal.util.ObjectPool
        public IndexedRingBuffer<?> createObject() {
            return new IndexedRingBuffer<>();
        }
    };
    static final int SIZE;
    static int _size;
    private final ElementSection<E> elements;
    final AtomicInteger index;
    private final IndexSection removed;
    final AtomicInteger removedIndex;

    static {
        _size = 256;
        if (PlatformDependent.isAndroid()) {
            _size = 8;
        }
        String sizeFromProperty = System.getProperty("rx.indexed-ring-buffer.size");
        if (sizeFromProperty != null) {
            try {
                _size = Integer.parseInt(sizeFromProperty);
            } catch (Exception e) {
                System.err.println("Failed to set 'rx.indexed-ring-buffer.size' with value " + sizeFromProperty + " => " + e.getMessage());
            }
        }
        SIZE = _size;
    }

    public static final <T> IndexedRingBuffer<T> getInstance() {
        return (IndexedRingBuffer) POOL.borrowObject();
    }

    public void releaseToPool() {
        int maxIndex = this.index.get();
        int realIndex = 0;
        loop0: for (ElementSection<E> section = this.elements; section != null; section = (ElementSection) ((ElementSection) section).next.get()) {
            int i = 0;
            while (i < SIZE) {
                if (realIndex >= maxIndex) {
                    break loop0;
                }
                ((ElementSection) section).array.set(i, null);
                i++;
                realIndex++;
            }
        }
        this.index.set(0);
        this.removedIndex.set(0);
        POOL.returnObject(this);
    }

    @Override // rx.Subscription
    public void unsubscribe() {
        releaseToPool();
    }

    private IndexedRingBuffer() {
        this.elements = new ElementSection<>();
        this.removed = new IndexSection();
        this.index = new AtomicInteger();
        this.removedIndex = new AtomicInteger();
    }

    public int add(E e) {
        int i = getIndexForAdd();
        if (i >= SIZE) {
            int sectionIndex = i % SIZE;
            ((ElementSection) getElementSection(i)).array.set(sectionIndex, e);
        } else {
            ((ElementSection) this.elements).array.set(i, e);
        }
        return i;
    }

    public E remove(int i) {
        E e;
        if (i >= SIZE) {
            e = (E) ((ElementSection) getElementSection(i)).array.getAndSet(i % SIZE, null);
        } else {
            e = (E) ((ElementSection) this.elements).array.getAndSet(i, null);
        }
        pushRemovedIndex(i);
        return e;
    }

    private IndexSection getIndexSection(int index) {
        if (index < SIZE) {
            return this.removed;
        }
        int numSections = index / SIZE;
        IndexSection a2 = this.removed;
        for (int i = 0; i < numSections; i++) {
            a2 = a2.getNext();
        }
        return a2;
    }

    private ElementSection<E> getElementSection(int index) {
        if (index < SIZE) {
            return this.elements;
        }
        int numSections = index / SIZE;
        ElementSection<E> a2 = this.elements;
        for (int i = 0; i < numSections; i++) {
            a2 = a2.getNext();
        }
        return a2;
    }

    private synchronized int getIndexForAdd() {
        int i;
        int ri = getIndexFromPreviouslyRemoved();
        if (ri >= 0) {
            if (ri < SIZE) {
                i = this.removed.getAndSet(ri, -1);
            } else {
                int sectionIndex = ri % SIZE;
                i = getIndexSection(ri).getAndSet(sectionIndex, -1);
            }
            if (i == this.index.get()) {
                this.index.getAndIncrement();
            }
        } else {
            i = this.index.getAndIncrement();
        }
        return i;
    }

    private synchronized int getIndexFromPreviouslyRemoved() {
        int i;
        while (true) {
            int currentRi = this.removedIndex.get();
            if (currentRi <= 0) {
                i = -1;
                break;
            }
            if (this.removedIndex.compareAndSet(currentRi, currentRi - 1)) {
                i = currentRi - 1;
                break;
            }
        }
        return i;
    }

    private synchronized void pushRemovedIndex(int elementIndex) {
        int i = this.removedIndex.getAndIncrement();
        if (i < SIZE) {
            this.removed.set(i, elementIndex);
        } else {
            int sectionIndex = i % SIZE;
            getIndexSection(i).set(sectionIndex, elementIndex);
        }
    }

    @Override // rx.Subscription
    public boolean isUnsubscribed() {
        return false;
    }

    public int forEach(Func1<? super E, Boolean> action) {
        return forEach(action, 0);
    }

    public int forEach(Func1<? super E, Boolean> action, int startIndex) {
        int endedAt = forEach(action, startIndex, this.index.get());
        if (startIndex > 0 && endedAt == this.index.get()) {
            return forEach(action, 0, startIndex);
        }
        if (endedAt == this.index.get()) {
            return 0;
        }
        return endedAt;
    }

    private int forEach(Func1<? super E, Boolean> func1, int i, int i2) {
        int i3 = this.index.get();
        int i4 = i;
        ElementSection<E> elementSection = this.elements;
        if (i >= SIZE) {
            elementSection = getElementSection(i);
            i %= SIZE;
        }
        loop0: while (elementSection != null) {
            int i5 = i;
            while (i5 < SIZE) {
                if (i4 >= i3 || i4 >= i2) {
                    break loop0;
                }
                a aVar = (Object) ((ElementSection) elementSection).array.get(i5);
                if (aVar != null) {
                    int i6 = i4;
                    if (!func1.call(aVar).booleanValue()) {
                        return i6;
                    }
                }
                i5++;
                i4++;
            }
            elementSection = (ElementSection) ((ElementSection) elementSection).next.get();
            i = 0;
        }
        return i4;
    }

    private static class ElementSection<E> {
        private final AtomicReferenceArray<E> array;
        private final AtomicReference<ElementSection<E>> next;

        private ElementSection() {
            this.array = new AtomicReferenceArray<>(IndexedRingBuffer.SIZE);
            this.next = new AtomicReference<>();
        }

        ElementSection<E> getNext() {
            if (this.next.get() != null) {
                return this.next.get();
            }
            ElementSection<E> newSection = new ElementSection<>();
            return this.next.compareAndSet(null, newSection) ? newSection : this.next.get();
        }
    }

    private static class IndexSection {
        private final AtomicReference<IndexSection> _next;
        private final AtomicIntegerArray unsafeArray;

        private IndexSection() {
            this.unsafeArray = new AtomicIntegerArray(IndexedRingBuffer.SIZE);
            this._next = new AtomicReference<>();
        }

        public int getAndSet(int expected, int newValue) {
            return this.unsafeArray.getAndSet(expected, newValue);
        }

        public void set(int i, int elementIndex) {
            this.unsafeArray.set(i, elementIndex);
        }

        IndexSection getNext() {
            if (this._next.get() != null) {
                return this._next.get();
            }
            IndexSection newSection = new IndexSection();
            return this._next.compareAndSet(null, newSection) ? newSection : this._next.get();
        }
    }
}
