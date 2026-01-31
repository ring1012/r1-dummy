package dagger.internal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

/* loaded from: classes.dex */
public class ArrayQueue<E> extends AbstractCollection<E> implements Queue<E>, Cloneable, Serializable {
    private static final int MIN_INITIAL_CAPACITY = 8;
    private static final long serialVersionUID = 2340985798034038923L;
    private transient Object[] elements;
    private transient int head;
    private transient int tail;

    private void allocateElements(int numElements) {
        int initialCapacity = 8;
        if (numElements >= 8) {
            int initialCapacity2 = numElements | (numElements >>> 1);
            int initialCapacity3 = initialCapacity2 | (initialCapacity2 >>> 2);
            int initialCapacity4 = initialCapacity3 | (initialCapacity3 >>> 4);
            int initialCapacity5 = initialCapacity4 | (initialCapacity4 >>> 8);
            initialCapacity = (initialCapacity5 | (initialCapacity5 >>> 16)) + 1;
            if (initialCapacity < 0) {
                initialCapacity >>>= 1;
            }
        }
        this.elements = new Object[initialCapacity];
    }

    private void doubleCapacity() {
        int p = this.head;
        int n = this.elements.length;
        int r = n - p;
        int newCapacity = n << 1;
        if (newCapacity < 0) {
            throw new IllegalStateException("Sorry, queue too big");
        }
        Object[] a2 = new Object[newCapacity];
        System.arraycopy(this.elements, p, a2, 0, r);
        System.arraycopy(this.elements, 0, a2, r, p);
        this.elements = a2;
        this.head = 0;
        this.tail = n;
    }

    public ArrayQueue() {
        this.elements = new Object[16];
    }

    public ArrayQueue(int numElements) {
        allocateElements(numElements);
    }

    public ArrayQueue(Collection<? extends E> c) {
        allocateElements(c.size());
        addAll(c);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Queue
    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException("e == null");
        }
        this.elements[this.tail] = e;
        int length = (this.tail + 1) & (this.elements.length - 1);
        this.tail = length;
        if (length == this.head) {
            doubleCapacity();
            return true;
        }
        return true;
    }

    @Override // java.util.Queue
    public boolean offer(E e) {
        return add(e);
    }

    @Override // java.util.Queue
    public E remove() {
        E x = poll();
        if (x == null) {
            throw new NoSuchElementException();
        }
        return x;
    }

    @Override // java.util.Queue
    public E poll() {
        int i = this.head;
        E e = (E) this.elements[i];
        if (e == null) {
            return null;
        }
        this.elements[i] = null;
        this.head = (i + 1) & (this.elements.length - 1);
        return e;
    }

    @Override // java.util.Queue
    public E element() {
        E e = (E) this.elements[this.head];
        if (e == null) {
            throw new NoSuchElementException();
        }
        return e;
    }

    @Override // java.util.Queue
    public E peek() {
        return (E) this.elements[this.head];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean delete(int i) {
        Object[] elements = this.elements;
        int mask = elements.length - 1;
        int h = this.head;
        int t = this.tail;
        int front = (i - h) & mask;
        int back = (t - i) & mask;
        if (front >= ((t - h) & mask)) {
            throw new ConcurrentModificationException();
        }
        if (front < back) {
            if (h <= i) {
                System.arraycopy(elements, h, elements, h + 1, front);
            } else {
                System.arraycopy(elements, 0, elements, 1, i);
                elements[0] = elements[mask];
                System.arraycopy(elements, h, elements, h + 1, mask - h);
            }
            elements[h] = null;
            this.head = (h + 1) & mask;
            return false;
        }
        if (i < t) {
            System.arraycopy(elements, i + 1, elements, i, back);
            this.tail = t - 1;
        } else {
            System.arraycopy(elements, i + 1, elements, i, mask - i);
            elements[mask] = elements[0];
            System.arraycopy(elements, 1, elements, 0, t);
            this.tail = (t - 1) & mask;
        }
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public int size() {
        return (this.tail - this.head) & (this.elements.length - 1);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean isEmpty() {
        return this.head == this.tail;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    public Iterator<E> iterator() {
        return new QueueIterator();
    }

    private class QueueIterator implements Iterator<E> {
        private int cursor;
        private int fence;
        private int lastRet;

        private QueueIterator() {
            this.cursor = ArrayQueue.this.head;
            this.fence = ArrayQueue.this.tail;
            this.lastRet = -1;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.cursor != this.fence;
        }

        @Override // java.util.Iterator
        public E next() {
            if (this.cursor != this.fence) {
                E e = (E) ArrayQueue.this.elements[this.cursor];
                if (ArrayQueue.this.tail != this.fence || e == null) {
                    throw new ConcurrentModificationException();
                }
                this.lastRet = this.cursor;
                this.cursor = (this.cursor + 1) & (ArrayQueue.this.elements.length - 1);
                return e;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            if (this.lastRet >= 0) {
                if (ArrayQueue.this.delete(this.lastRet)) {
                    this.cursor = (this.cursor - 1) & (ArrayQueue.this.elements.length - 1);
                    this.fence = ArrayQueue.this.tail;
                }
                this.lastRet = -1;
                return;
            }
            throw new IllegalStateException();
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        int mask = this.elements.length - 1;
        int i = this.head;
        while (true) {
            Object x = this.elements[i];
            if (x == null) {
                return false;
            }
            if (o.equals(x)) {
                return true;
            }
            i = (i + 1) & mask;
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }
        int mask = this.elements.length - 1;
        int i = this.head;
        while (true) {
            Object x = this.elements[i];
            if (x == null) {
                return false;
            }
            if (o.equals(x)) {
                delete(i);
                return true;
            }
            i = (i + 1) & mask;
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public void clear() {
        int h = this.head;
        int t = this.tail;
        if (h != t) {
            this.tail = 0;
            this.head = 0;
            int i = h;
            int mask = this.elements.length - 1;
            do {
                this.elements[i] = null;
                i = (i + 1) & mask;
            } while (i != t);
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public Object[] toArray() {
        return toArray(new Object[size()]);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        int size = size();
        if (tArr.length < size) {
            tArr = (T[]) ((Object[]) Array.newInstance(tArr.getClass().getComponentType(), size));
        }
        if (this.head < this.tail) {
            System.arraycopy(this.elements, this.head, tArr, 0, size());
        } else if (this.head > this.tail) {
            int length = this.elements.length - this.head;
            System.arraycopy(this.elements, this.head, tArr, 0, length);
            System.arraycopy(this.elements, 0, tArr, length, this.tail);
        }
        if (tArr.length > size) {
            tArr[size] = null;
        }
        return tArr;
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public ArrayQueue<E> m7clone() {
        try {
            ArrayQueue<E> result = (ArrayQueue) super.clone();
            Object[] objArr = (Object[]) Array.newInstance(this.elements.getClass().getComponentType(), this.elements.length);
            System.arraycopy(this.elements, 0, objArr, 0, this.elements.length);
            result.elements = objArr;
            return result;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(size());
        int mask = this.elements.length - 1;
        for (int i = this.head; i != this.tail; i = (i + 1) & mask) {
            s.writeObject(this.elements[i]);
        }
    }

    private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
        s.defaultReadObject();
        int size = s.readInt();
        allocateElements(size);
        this.head = 0;
        this.tail = size;
        for (int i = 0; i < size; i++) {
            this.elements[i] = s.readObject();
        }
    }
}
