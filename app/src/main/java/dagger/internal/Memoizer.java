package dagger.internal;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* loaded from: classes.dex */
abstract class Memoizer<K, V> {
    private final Map<K, V> map = new LinkedHashMap();
    private final Lock readLock;
    private final Lock writeLock;

    protected abstract V create(K k);

    public Memoizer() {
        ReadWriteLock lock = new ReentrantReadWriteLock();
        this.readLock = lock.readLock();
        this.writeLock = lock.writeLock();
    }

    public final V get(K key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        this.readLock.lock();
        try {
            V value = this.map.get(key);
            if (value == null) {
                this.readLock.unlock();
                V newValue = create(key);
                if (newValue == null) {
                    throw new NullPointerException("create returned null");
                }
                this.writeLock.lock();
                try {
                    this.map.put(key, newValue);
                    return newValue;
                } finally {
                    this.writeLock.unlock();
                }
            }
            return value;
        } finally {
            this.readLock.unlock();
        }
    }

    public final String toString() {
        this.readLock.lock();
        try {
            return this.map.toString();
        } finally {
            this.readLock.unlock();
        }
    }
}
