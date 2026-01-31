package org.greenrobot.greendao.internal;

import com.unisound.vui.priority.PriorityMap;
import java.util.Arrays;
import org.greenrobot.greendao.DaoLog;

/* loaded from: classes.dex */
public final class LongHashMap<T> {
    private int capacity;
    private int size;
    private Entry<T>[] table;
    private int threshold;

    static final class Entry<T> {
        final long key;
        Entry<T> next;
        T value;

        Entry(long key, T value, Entry<T> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public LongHashMap() {
        this(16);
    }

    public LongHashMap(int capacity) {
        this.capacity = capacity;
        this.threshold = (capacity * 4) / 3;
        this.table = new Entry[capacity];
    }

    public boolean containsKey(long key) {
        int index = ((((int) (key >>> 32)) ^ ((int) key)) & PriorityMap.PRIORITY_MAX) % this.capacity;
        for (Entry<T> entry = this.table[index]; entry != null; entry = entry.next) {
            if (entry.key == key) {
                return true;
            }
        }
        return false;
    }

    public T get(long key) {
        int index = ((((int) (key >>> 32)) ^ ((int) key)) & PriorityMap.PRIORITY_MAX) % this.capacity;
        for (Entry<T> entry = this.table[index]; entry != null; entry = entry.next) {
            if (entry.key == key) {
                return entry.value;
            }
        }
        return null;
    }

    public T put(long key, T value) {
        int index = ((((int) (key >>> 32)) ^ ((int) key)) & PriorityMap.PRIORITY_MAX) % this.capacity;
        Entry<T> entryOriginal = this.table[index];
        for (Entry<T> entry = entryOriginal; entry != null; entry = entry.next) {
            if (entry.key == key) {
                T oldValue = entry.value;
                entry.value = value;
                return oldValue;
            }
        }
        this.table[index] = new Entry<>(key, value, entryOriginal);
        this.size++;
        if (this.size > this.threshold) {
            setCapacity(this.capacity * 2);
        }
        return null;
    }

    public T remove(long key) {
        int index = ((((int) (key >>> 32)) ^ ((int) key)) & PriorityMap.PRIORITY_MAX) % this.capacity;
        Entry<T> previous = null;
        Entry<T> entry = this.table[index];
        while (entry != null) {
            Entry<T> next = entry.next;
            if (entry.key == key) {
                if (previous == null) {
                    this.table[index] = next;
                } else {
                    previous.next = next;
                }
                this.size--;
                return entry.value;
            }
            previous = entry;
            entry = next;
        }
        return null;
    }

    public void clear() {
        this.size = 0;
        Arrays.fill(this.table, (Object) null);
    }

    public int size() {
        return this.size;
    }

    public void setCapacity(int newCapacity) {
        Entry<T>[] newTable = new Entry[newCapacity];
        int length = this.table.length;
        for (int i = 0; i < length; i++) {
            Entry<T> entry = this.table[i];
            while (entry != null) {
                long key = entry.key;
                int index = ((((int) (key >>> 32)) ^ ((int) key)) & PriorityMap.PRIORITY_MAX) % newCapacity;
                Entry<T> originalNext = entry.next;
                entry.next = newTable[index];
                newTable[index] = entry;
                entry = originalNext;
            }
        }
        this.table = newTable;
        this.capacity = newCapacity;
        this.threshold = (newCapacity * 4) / 3;
    }

    public void reserveRoom(int entryCount) {
        setCapacity((entryCount * 5) / 3);
    }

    public void logStats() {
        int collisions = 0;
        for (Entry<T> entry : this.table) {
            for (; entry != null && entry.next != null; entry = entry.next) {
                collisions++;
            }
        }
        DaoLog.d("load: " + (this.size / this.capacity) + ", size: " + this.size + ", capa: " + this.capacity + ", collisions: " + collisions + ", collision ratio: " + (collisions / this.size));
    }
}
