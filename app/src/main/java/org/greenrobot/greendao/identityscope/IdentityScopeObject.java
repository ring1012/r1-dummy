package org.greenrobot.greendao.identityscope;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: classes.dex */
public class IdentityScopeObject<K, T> implements IdentityScope<K, T> {
    private final HashMap<K, Reference<T>> map = new HashMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    @Override // org.greenrobot.greendao.identityscope.IdentityScope
    public T get(K key) {
        this.lock.lock();
        try {
            Reference<T> ref = this.map.get(key);
            if (ref != null) {
                return ref.get();
            }
            return null;
        } finally {
            this.lock.unlock();
        }
    }

    @Override // org.greenrobot.greendao.identityscope.IdentityScope
    public T getNoLock(K key) {
        Reference<T> ref = this.map.get(key);
        if (ref != null) {
            return ref.get();
        }
        return null;
    }

    @Override // org.greenrobot.greendao.identityscope.IdentityScope
    public void put(K key, T entity) {
        this.lock.lock();
        try {
            this.map.put(key, new WeakReference(entity));
        } finally {
            this.lock.unlock();
        }
    }

    @Override // org.greenrobot.greendao.identityscope.IdentityScope
    public void putNoLock(K key, T entity) {
        this.map.put(key, new WeakReference(entity));
    }

    @Override // org.greenrobot.greendao.identityscope.IdentityScope
    public boolean detach(K key, T entity) {
        ReentrantLock reentrantLock;
        this.lock.lock();
        try {
            if (get(key) == entity && entity != null) {
                remove((IdentityScopeObject<K, T>) key);
                return true;
            }
            return false;
        } finally {
            this.lock.unlock();
        }
    }

    @Override // org.greenrobot.greendao.identityscope.IdentityScope
    public void remove(K key) {
        this.lock.lock();
        try {
            this.map.remove(key);
        } finally {
            this.lock.unlock();
        }
    }

    @Override // org.greenrobot.greendao.identityscope.IdentityScope
    public void remove(Iterable<K> keys) {
        this.lock.lock();
        try {
            for (K key : keys) {
                this.map.remove(key);
            }
        } finally {
            this.lock.unlock();
        }
    }

    @Override // org.greenrobot.greendao.identityscope.IdentityScope
    public void clear() {
        this.lock.lock();
        try {
            this.map.clear();
        } finally {
            this.lock.unlock();
        }
    }

    @Override // org.greenrobot.greendao.identityscope.IdentityScope
    public void lock() {
        this.lock.lock();
    }

    @Override // org.greenrobot.greendao.identityscope.IdentityScope
    public void unlock() {
        this.lock.unlock();
    }

    @Override // org.greenrobot.greendao.identityscope.IdentityScope
    public void reserveRoom(int count) {
    }
}
