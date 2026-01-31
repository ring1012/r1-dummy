package android.support.v4.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
public final class ArraySet<E> implements Collection<E>, Set<E> {
    private static final int BASE_SIZE = 4;
    private static final int CACHE_SIZE = 10;
    private static final boolean DEBUG = false;
    private static final int[] INT = new int[0];
    private static final Object[] OBJECT = new Object[0];
    private static final String TAG = "ArraySet";
    static Object[] sBaseCache;
    static int sBaseCacheSize;
    static Object[] sTwiceBaseCache;
    static int sTwiceBaseCacheSize;
    Object[] mArray;
    MapCollections<E, E> mCollections;
    int[] mHashes;
    final boolean mIdentityHashCode;
    int mSize;

    private int indexOf(Object key, int hash) {
        int N = this.mSize;
        if (N == 0) {
            return -1;
        }
        int index = ContainerHelpers.binarySearch(this.mHashes, N, hash);
        if (index >= 0 && !key.equals(this.mArray[index])) {
            int end = index + 1;
            while (end < N && this.mHashes[end] == hash) {
                if (key.equals(this.mArray[end])) {
                    return end;
                }
                end++;
            }
            for (int i = index - 1; i >= 0 && this.mHashes[i] == hash; i--) {
                if (key.equals(this.mArray[i])) {
                    return i;
                }
            }
            return end ^ (-1);
        }
        return index;
    }

    private int indexOfNull() {
        int N = this.mSize;
        if (N == 0) {
            return -1;
        }
        int index = ContainerHelpers.binarySearch(this.mHashes, N, 0);
        if (index >= 0 && this.mArray[index] != null) {
            int end = index + 1;
            while (end < N && this.mHashes[end] == 0) {
                if (this.mArray[end] == null) {
                    return end;
                }
                end++;
            }
            for (int i = index - 1; i >= 0 && this.mHashes[i] == 0; i--) {
                if (this.mArray[i] == null) {
                    return i;
                }
            }
            return end ^ (-1);
        }
        return index;
    }

    private void allocArrays(int size) {
        if (size == 8) {
            synchronized (ArraySet.class) {
                if (sTwiceBaseCache != null) {
                    Object[] array = sTwiceBaseCache;
                    this.mArray = array;
                    sTwiceBaseCache = (Object[]) array[0];
                    this.mHashes = (int[]) array[1];
                    array[1] = null;
                    array[0] = null;
                    sTwiceBaseCacheSize--;
                    return;
                }
            }
        } else if (size == 4) {
            synchronized (ArraySet.class) {
                if (sBaseCache != null) {
                    Object[] array2 = sBaseCache;
                    this.mArray = array2;
                    sBaseCache = (Object[]) array2[0];
                    this.mHashes = (int[]) array2[1];
                    array2[1] = null;
                    array2[0] = null;
                    sBaseCacheSize--;
                    return;
                }
            }
        }
        this.mHashes = new int[size];
        this.mArray = new Object[size];
    }

    private static void freeArrays(int[] hashes, Object[] array, int size) {
        if (hashes.length == 8) {
            synchronized (ArraySet.class) {
                if (sTwiceBaseCacheSize < 10) {
                    array[0] = sTwiceBaseCache;
                    array[1] = hashes;
                    for (int i = size - 1; i >= 2; i--) {
                        array[i] = null;
                    }
                    sTwiceBaseCache = array;
                    sTwiceBaseCacheSize++;
                }
            }
            return;
        }
        if (hashes.length == 4) {
            synchronized (ArraySet.class) {
                if (sBaseCacheSize < 10) {
                    array[0] = sBaseCache;
                    array[1] = hashes;
                    for (int i2 = size - 1; i2 >= 2; i2--) {
                        array[i2] = null;
                    }
                    sBaseCache = array;
                    sBaseCacheSize++;
                }
            }
        }
    }

    public ArraySet() {
        this(0, false);
    }

    public ArraySet(int capacity) {
        this(capacity, false);
    }

    public ArraySet(int capacity, boolean identityHashCode) {
        this.mIdentityHashCode = identityHashCode;
        if (capacity == 0) {
            this.mHashes = INT;
            this.mArray = OBJECT;
        } else {
            allocArrays(capacity);
        }
        this.mSize = 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ArraySet(ArraySet<E> arraySet) {
        this();
        if (arraySet != 0) {
            addAll((ArraySet) arraySet);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ArraySet(Collection<E> collection) {
        this();
        if (collection != 0) {
            addAll(collection);
        }
    }

    @Override // java.util.Collection, java.util.Set
    public void clear() {
        if (this.mSize != 0) {
            freeArrays(this.mHashes, this.mArray, this.mSize);
            this.mHashes = INT;
            this.mArray = OBJECT;
            this.mSize = 0;
        }
    }

    public void ensureCapacity(int minimumCapacity) {
        if (this.mHashes.length < minimumCapacity) {
            int[] ohashes = this.mHashes;
            Object[] oarray = this.mArray;
            allocArrays(minimumCapacity);
            if (this.mSize > 0) {
                System.arraycopy(ohashes, 0, this.mHashes, 0, this.mSize);
                System.arraycopy(oarray, 0, this.mArray, 0, this.mSize);
            }
            freeArrays(ohashes, oarray, this.mSize);
        }
    }

    @Override // java.util.Collection, java.util.Set
    public boolean contains(Object key) {
        return indexOf(key) >= 0;
    }

    public int indexOf(Object key) {
        if (key == null) {
            return indexOfNull();
        }
        return indexOf(key, this.mIdentityHashCode ? System.identityHashCode(key) : key.hashCode());
    }

    public E valueAt(int i) {
        return (E) this.mArray[i];
    }

    @Override // java.util.Collection, java.util.Set
    public boolean isEmpty() {
        return this.mSize <= 0;
    }

    @Override // java.util.Collection, java.util.Set
    public boolean add(E value) {
        int hash;
        int index;
        int n = 8;
        if (value == null) {
            hash = 0;
            index = indexOfNull();
        } else {
            hash = this.mIdentityHashCode ? System.identityHashCode(value) : value.hashCode();
            index = indexOf(value, hash);
        }
        if (index >= 0) {
            return false;
        }
        int index2 = index ^ (-1);
        if (this.mSize >= this.mHashes.length) {
            if (this.mSize >= 8) {
                n = this.mSize + (this.mSize >> 1);
            } else if (this.mSize < 4) {
                n = 4;
            }
            int[] ohashes = this.mHashes;
            Object[] oarray = this.mArray;
            allocArrays(n);
            if (this.mHashes.length > 0) {
                System.arraycopy(ohashes, 0, this.mHashes, 0, ohashes.length);
                System.arraycopy(oarray, 0, this.mArray, 0, oarray.length);
            }
            freeArrays(ohashes, oarray, this.mSize);
        }
        if (index2 < this.mSize) {
            System.arraycopy(this.mHashes, index2, this.mHashes, index2 + 1, this.mSize - index2);
            System.arraycopy(this.mArray, index2, this.mArray, index2 + 1, this.mSize - index2);
        }
        this.mHashes[index2] = hash;
        this.mArray[index2] = value;
        this.mSize++;
        return true;
    }

    public void append(E value) {
        int hash;
        int index = this.mSize;
        if (value == null) {
            hash = 0;
        } else {
            hash = this.mIdentityHashCode ? System.identityHashCode(value) : value.hashCode();
        }
        if (index >= this.mHashes.length) {
            throw new IllegalStateException("Array is full");
        }
        if (index > 0 && this.mHashes[index - 1] > hash) {
            add(value);
            return;
        }
        this.mSize = index + 1;
        this.mHashes[index] = hash;
        this.mArray[index] = value;
    }

    public void addAll(ArraySet<? extends E> array) {
        int N = array.mSize;
        ensureCapacity(this.mSize + N);
        if (this.mSize == 0) {
            if (N > 0) {
                System.arraycopy(array.mHashes, 0, this.mHashes, 0, N);
                System.arraycopy(array.mArray, 0, this.mArray, 0, N);
                this.mSize = N;
                return;
            }
            return;
        }
        for (int i = 0; i < N; i++) {
            add(array.valueAt(i));
        }
    }

    @Override // java.util.Collection, java.util.Set
    public boolean remove(Object object) {
        int index = indexOf(object);
        if (index < 0) {
            return false;
        }
        removeAt(index);
        return true;
    }

    public E removeAt(int i) {
        E e = (E) this.mArray[i];
        if (this.mSize <= 1) {
            freeArrays(this.mHashes, this.mArray, this.mSize);
            this.mHashes = INT;
            this.mArray = OBJECT;
            this.mSize = 0;
        } else if (this.mHashes.length > 8 && this.mSize < this.mHashes.length / 3) {
            int i2 = this.mSize > 8 ? this.mSize + (this.mSize >> 1) : 8;
            int[] iArr = this.mHashes;
            Object[] objArr = this.mArray;
            allocArrays(i2);
            this.mSize--;
            if (i > 0) {
                System.arraycopy(iArr, 0, this.mHashes, 0, i);
                System.arraycopy(objArr, 0, this.mArray, 0, i);
            }
            if (i < this.mSize) {
                System.arraycopy(iArr, i + 1, this.mHashes, i, this.mSize - i);
                System.arraycopy(objArr, i + 1, this.mArray, i, this.mSize - i);
            }
        } else {
            this.mSize--;
            if (i < this.mSize) {
                System.arraycopy(this.mHashes, i + 1, this.mHashes, i, this.mSize - i);
                System.arraycopy(this.mArray, i + 1, this.mArray, i, this.mSize - i);
            }
            this.mArray[this.mSize] = null;
        }
        return e;
    }

    public boolean removeAll(ArraySet<? extends E> array) {
        int N = array.mSize;
        int originalSize = this.mSize;
        for (int i = 0; i < N; i++) {
            remove(array.valueAt(i));
        }
        return originalSize != this.mSize;
    }

    @Override // java.util.Collection, java.util.Set
    public int size() {
        return this.mSize;
    }

    @Override // java.util.Collection, java.util.Set
    public Object[] toArray() {
        Object[] result = new Object[this.mSize];
        System.arraycopy(this.mArray, 0, result, 0, this.mSize);
        return result;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object[]] */
    @Override // java.util.Collection, java.util.Set
    public <T> T[] toArray(T[] array) {
        if (array.length < this.mSize) {
            array = (Object[]) Array.newInstance(array.getClass().getComponentType(), this.mSize);
        }
        System.arraycopy(this.mArray, 0, array, 0, this.mSize);
        if (array.length > this.mSize) {
            array[this.mSize] = null;
        }
        return array;
    }

    @Override // java.util.Collection, java.util.Set
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Set)) {
            return false;
        }
        Set<?> set = (Set) object;
        if (size() != set.size()) {
            return false;
        }
        for (int i = 0; i < this.mSize; i++) {
            try {
                E mine = valueAt(i);
                if (!set.contains(mine)) {
                    return false;
                }
            } catch (ClassCastException e) {
                return false;
            } catch (NullPointerException e2) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.Collection, java.util.Set
    public int hashCode() {
        int[] hashes = this.mHashes;
        int result = 0;
        int s = this.mSize;
        for (int i = 0; i < s; i++) {
            result += hashes[i];
        }
        return result;
    }

    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder buffer = new StringBuilder(this.mSize * 14);
        buffer.append('{');
        for (int i = 0; i < this.mSize; i++) {
            if (i > 0) {
                buffer.append(", ");
            }
            Object value = valueAt(i);
            if (value != this) {
                buffer.append(value);
            } else {
                buffer.append("(this Set)");
            }
        }
        buffer.append('}');
        return buffer.toString();
    }

    private MapCollections<E, E> getCollection() {
        if (this.mCollections == null) {
            this.mCollections = new MapCollections<E, E>() { // from class: android.support.v4.util.ArraySet.1
                @Override // android.support.v4.util.MapCollections
                protected int colGetSize() {
                    return ArraySet.this.mSize;
                }

                @Override // android.support.v4.util.MapCollections
                protected Object colGetEntry(int index, int offset) {
                    return ArraySet.this.mArray[index];
                }

                @Override // android.support.v4.util.MapCollections
                protected int colIndexOfKey(Object key) {
                    return ArraySet.this.indexOf(key);
                }

                @Override // android.support.v4.util.MapCollections
                protected int colIndexOfValue(Object value) {
                    return ArraySet.this.indexOf(value);
                }

                @Override // android.support.v4.util.MapCollections
                protected Map<E, E> colGetMap() {
                    throw new UnsupportedOperationException("not a map");
                }

                @Override // android.support.v4.util.MapCollections
                protected void colPut(E key, E value) {
                    ArraySet.this.add(key);
                }

                @Override // android.support.v4.util.MapCollections
                protected E colSetValue(int index, E value) {
                    throw new UnsupportedOperationException("not a map");
                }

                @Override // android.support.v4.util.MapCollections
                protected void colRemoveAt(int index) {
                    ArraySet.this.removeAt(index);
                }

                @Override // android.support.v4.util.MapCollections
                protected void colClear() {
                    ArraySet.this.clear();
                }
            };
        }
        return this.mCollections;
    }

    @Override // java.util.Collection, java.lang.Iterable, java.util.Set
    public Iterator<E> iterator() {
        return getCollection().getKeySet().iterator();
    }

    @Override // java.util.Collection, java.util.Set
    public boolean containsAll(Collection<?> collection) {
        Iterator<?> it = collection.iterator();
        while (it.hasNext()) {
            if (!contains(it.next())) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.Collection, java.util.Set
    public boolean addAll(Collection<? extends E> collection) {
        ensureCapacity(this.mSize + collection.size());
        boolean added = false;
        for (E value : collection) {
            added |= add(value);
        }
        return added;
    }

    @Override // java.util.Collection, java.util.Set
    public boolean removeAll(Collection<?> collection) {
        boolean removed = false;
        for (Object value : collection) {
            removed |= remove(value);
        }
        return removed;
    }

    @Override // java.util.Collection, java.util.Set
    public boolean retainAll(Collection<?> collection) {
        boolean removed = false;
        for (int i = this.mSize - 1; i >= 0; i--) {
            if (!collection.contains(this.mArray[i])) {
                removeAt(i);
                removed = true;
            }
        }
        return removed;
    }
}
