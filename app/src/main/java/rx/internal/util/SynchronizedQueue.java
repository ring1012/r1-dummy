package rx.internal.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/* loaded from: classes.dex */
public class SynchronizedQueue<T> implements Queue<T> {
    private final LinkedList<T> list;
    private final int size;

    public SynchronizedQueue() {
        this.list = new LinkedList<>();
        this.size = -1;
    }

    public SynchronizedQueue(int size) {
        this.list = new LinkedList<>();
        this.size = size;
    }

    @Override // java.util.Collection
    public synchronized boolean isEmpty() {
        return this.list.isEmpty();
    }

    @Override // java.util.Collection
    public synchronized boolean contains(Object o) {
        return this.list.contains(o);
    }

    @Override // java.util.Collection, java.lang.Iterable
    public synchronized Iterator<T> iterator() {
        return this.list.iterator();
    }

    @Override // java.util.Collection
    public synchronized int size() {
        return this.list.size();
    }

    @Override // java.util.Queue, java.util.Collection
    public synchronized boolean add(T e) {
        return this.list.add(e);
    }

    @Override // java.util.Collection
    public synchronized boolean remove(Object o) {
        return this.list.remove(o);
    }

    @Override // java.util.Collection
    public synchronized boolean containsAll(Collection<?> c) {
        return this.list.containsAll(c);
    }

    @Override // java.util.Collection
    public synchronized boolean addAll(Collection<? extends T> c) {
        return this.list.addAll(c);
    }

    @Override // java.util.Collection
    public synchronized boolean removeAll(Collection<?> c) {
        return this.list.removeAll(c);
    }

    @Override // java.util.Collection
    public synchronized boolean retainAll(Collection<?> c) {
        return this.list.retainAll(c);
    }

    @Override // java.util.Collection
    public synchronized void clear() {
        this.list.clear();
    }

    public synchronized String toString() {
        return this.list.toString();
    }

    @Override // java.util.Collection
    public int hashCode() {
        return this.list.hashCode();
    }

    @Override // java.util.Collection
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            SynchronizedQueue<?> other = (SynchronizedQueue) obj;
            return this.list == null ? other.list == null : this.list.equals(other.list);
        }
        return false;
    }

    @Override // java.util.Queue
    public synchronized T peek() {
        return this.list.peek();
    }

    @Override // java.util.Queue
    public synchronized T element() {
        return this.list.element();
    }

    @Override // java.util.Queue
    public synchronized T poll() {
        return this.list.poll();
    }

    @Override // java.util.Queue
    public synchronized T remove() {
        return this.list.remove();
    }

    @Override // java.util.Queue
    public synchronized boolean offer(T e) {
        return (this.size <= -1 || this.list.size() + 1 <= this.size) ? this.list.offer(e) : false;
    }

    public synchronized Object clone() {
        SynchronizedQueue<T> q;
        q = new SynchronizedQueue<>(this.size);
        q.addAll(this.list);
        return q;
    }

    @Override // java.util.Collection
    public synchronized Object[] toArray() {
        return this.list.toArray();
    }

    @Override // java.util.Collection
    public synchronized <R> R[] toArray(R[] rArr) {
        return (R[]) this.list.toArray(rArr);
    }
}
