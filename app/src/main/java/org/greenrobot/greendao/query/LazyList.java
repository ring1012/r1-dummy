package org.greenrobot.greendao.query;

import android.database.Cursor;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.ReentrantLock;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.InternalQueryDaoAccess;

/* loaded from: classes.dex */
public class LazyList<E> implements List<E>, Closeable {
    private final Cursor cursor;
    private final InternalQueryDaoAccess<E> daoAccess;
    private final List<E> entities;
    private volatile int loadedCount;
    private final ReentrantLock lock;
    private final int size;

    protected class LazyIterator implements CloseableListIterator<E> {
        private final boolean closeWhenDone;
        private int index;

        public LazyIterator(int startLocation, boolean closeWhenDone) {
            this.index = startLocation;
            this.closeWhenDone = closeWhenDone;
        }

        @Override // java.util.ListIterator
        public void add(E object) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.ListIterator
        public boolean hasPrevious() {
            return this.index > 0;
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return this.index;
        }

        @Override // java.util.ListIterator
        public E previous() {
            if (this.index <= 0) {
                throw new NoSuchElementException();
            }
            this.index--;
            return (E) LazyList.this.get(this.index);
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return this.index - 1;
        }

        @Override // java.util.ListIterator
        public void set(E object) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public boolean hasNext() {
            return this.index < LazyList.this.size;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public E next() {
            if (this.index >= LazyList.this.size) {
                throw new NoSuchElementException();
            }
            E e = (E) LazyList.this.get(this.index);
            this.index++;
            if (this.index == LazyList.this.size && this.closeWhenDone) {
                close();
            }
            return e;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            LazyList.this.close();
        }
    }

    LazyList(InternalQueryDaoAccess<E> daoAccess, Cursor cursor, boolean cacheEntities) {
        this.cursor = cursor;
        this.daoAccess = daoAccess;
        this.size = cursor.getCount();
        if (cacheEntities) {
            this.entities = new ArrayList(this.size);
            for (int i = 0; i < this.size; i++) {
                this.entities.add(null);
            }
        } else {
            this.entities = null;
        }
        if (this.size == 0) {
            cursor.close();
        }
        this.lock = new ReentrantLock();
    }

    public void loadRemaining() {
        checkCached();
        int size = this.entities.size();
        for (int i = 0; i < size; i++) {
            get(i);
        }
    }

    protected void checkCached() {
        if (this.entities == null) {
            throw new DaoException("This operation only works with cached lazy lists");
        }
    }

    public E peak(int location) {
        if (this.entities != null) {
            return this.entities.get(location);
        }
        return null;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.cursor.close();
    }

    public boolean isClosed() {
        return this.cursor.isClosed();
    }

    public int getLoadedCount() {
        return this.loadedCount;
    }

    public boolean isLoadedCompletely() {
        return this.loadedCount == this.size;
    }

    @Override // java.util.List, java.util.Collection
    public boolean add(E object) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public void add(int location, E object) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public boolean addAll(Collection<? extends E> arg0) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public boolean addAll(int arg0, Collection<? extends E> arg1) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public boolean contains(Object object) {
        loadRemaining();
        return this.entities.contains(object);
    }

    @Override // java.util.List, java.util.Collection
    public boolean containsAll(Collection<?> collection) {
        loadRemaining();
        return this.entities.containsAll(collection);
    }

    @Override // java.util.List
    public E get(int location) {
        E entity;
        if (this.entities != null) {
            entity = this.entities.get(location);
            if (entity == null) {
                this.lock.lock();
                try {
                    entity = this.entities.get(location);
                    if (entity == null) {
                        entity = loadEntity(location);
                        this.entities.set(location, entity);
                        this.loadedCount++;
                        if (this.loadedCount == this.size) {
                            this.cursor.close();
                        }
                    }
                } finally {
                }
            }
        } else {
            this.lock.lock();
            try {
                entity = loadEntity(location);
            } finally {
            }
        }
        return entity;
    }

    protected E loadEntity(int location) {
        boolean ok = this.cursor.moveToPosition(location);
        if (!ok) {
            throw new DaoException("Could not move to cursor location " + location);
        }
        E entity = this.daoAccess.loadCurrent(this.cursor, 0, true);
        if (entity == null) {
            throw new DaoException("Loading of entity failed (null) at position " + location);
        }
        return entity;
    }

    @Override // java.util.List
    public int indexOf(Object object) {
        loadRemaining();
        return this.entities.indexOf(object);
    }

    @Override // java.util.List, java.util.Collection
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override // java.util.List, java.util.Collection, java.lang.Iterable
    public Iterator<E> iterator() {
        return new LazyIterator(0, false);
    }

    @Override // java.util.List
    public int lastIndexOf(Object object) {
        loadRemaining();
        return this.entities.lastIndexOf(object);
    }

    @Override // java.util.List
    public CloseableListIterator<E> listIterator() {
        return new LazyIterator(0, false);
    }

    public CloseableListIterator<E> listIteratorAutoClose() {
        return new LazyIterator(0, true);
    }

    @Override // java.util.List
    public ListIterator<E> listIterator(int location) {
        return new LazyIterator(location, false);
    }

    @Override // java.util.List
    public E remove(int location) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public boolean remove(Object object) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public boolean removeAll(Collection<?> arg0) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public boolean retainAll(Collection<?> arg0) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public E set(int location, E object) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public int size() {
        return this.size;
    }

    @Override // java.util.List
    public List<E> subList(int start, int end) {
        checkCached();
        for (int i = start; i < end; i++) {
            get(i);
        }
        return this.entities.subList(start, end);
    }

    @Override // java.util.List, java.util.Collection
    public Object[] toArray() {
        loadRemaining();
        return this.entities.toArray();
    }

    @Override // java.util.List, java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        loadRemaining();
        return (T[]) this.entities.toArray(tArr);
    }
}
