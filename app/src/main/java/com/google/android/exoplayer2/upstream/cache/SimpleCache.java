package com.google.android.exoplayer2.upstream.cache;

import android.os.ConditionVariable;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.util.Assertions;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

/* loaded from: classes.dex */
public final class SimpleCache implements Cache {
    private final File cacheDir;
    private final CacheEvictor evictor;
    private final CachedContentIndex index;
    private Cache.CacheException initializationException;
    private final HashMap<String, ArrayList<Cache.Listener>> listeners;
    private final HashMap<String, CacheSpan> lockedSpans;
    private long totalSpace;

    public SimpleCache(File cacheDir, CacheEvictor evictor) {
        this(cacheDir, evictor, null);
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [com.google.android.exoplayer2.upstream.cache.SimpleCache$1] */
    public SimpleCache(File cacheDir, CacheEvictor evictor, byte[] secretKey) {
        this.totalSpace = 0L;
        this.cacheDir = cacheDir;
        this.evictor = evictor;
        this.lockedSpans = new HashMap<>();
        this.index = new CachedContentIndex(cacheDir, secretKey);
        this.listeners = new HashMap<>();
        final ConditionVariable conditionVariable = new ConditionVariable();
        new Thread("SimpleCache.initialize()") { // from class: com.google.android.exoplayer2.upstream.cache.SimpleCache.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                synchronized (SimpleCache.this) {
                    conditionVariable.open();
                    try {
                        SimpleCache.this.initialize();
                    } catch (Cache.CacheException e) {
                        SimpleCache.this.initializationException = e;
                    }
                    SimpleCache.this.evictor.onCacheInitialized();
                }
            }
        }.start();
        conditionVariable.block();
    }

    @Override // com.google.android.exoplayer2.upstream.cache.Cache
    public synchronized NavigableSet<CacheSpan> addListener(String key, Cache.Listener listener) {
        ArrayList<Cache.Listener> listenersForKey = this.listeners.get(key);
        if (listenersForKey == null) {
            listenersForKey = new ArrayList<>();
            this.listeners.put(key, listenersForKey);
        }
        listenersForKey.add(listener);
        return getCachedSpans(key);
    }

    @Override // com.google.android.exoplayer2.upstream.cache.Cache
    public synchronized void removeListener(String key, Cache.Listener listener) {
        ArrayList<Cache.Listener> listenersForKey = this.listeners.get(key);
        if (listenersForKey != null) {
            listenersForKey.remove(listener);
            if (listenersForKey.isEmpty()) {
                this.listeners.remove(key);
            }
        }
    }

    @Override // com.google.android.exoplayer2.upstream.cache.Cache
    public synchronized NavigableSet<CacheSpan> getCachedSpans(String key) {
        CachedContent cachedContent;
        cachedContent = this.index.get(key);
        return cachedContent == null ? null : new TreeSet((Collection) cachedContent.getSpans());
    }

    @Override // com.google.android.exoplayer2.upstream.cache.Cache
    public synchronized Set<String> getKeys() {
        return new HashSet(this.index.getKeys());
    }

    @Override // com.google.android.exoplayer2.upstream.cache.Cache
    public synchronized long getCacheSpace() {
        return this.totalSpace;
    }

    @Override // com.google.android.exoplayer2.upstream.cache.Cache
    public synchronized SimpleCacheSpan startReadWrite(String key, long position) throws InterruptedException, Cache.CacheException {
        SimpleCacheSpan span;
        while (true) {
            span = startReadWriteNonBlocking(key, position);
            if (span == null) {
                wait();
            }
        }
        return span;
    }

    @Override // com.google.android.exoplayer2.upstream.cache.Cache
    public synchronized SimpleCacheSpan startReadWriteNonBlocking(String key, long position) throws Cache.CacheException {
        SimpleCacheSpan newCacheSpan;
        if (this.initializationException != null) {
            throw this.initializationException;
        }
        SimpleCacheSpan cacheSpan = getSpan(key, position);
        if (cacheSpan.isCached) {
            newCacheSpan = this.index.get(key).touch(cacheSpan);
            notifySpanTouched(cacheSpan, newCacheSpan);
        } else if (!this.lockedSpans.containsKey(key)) {
            this.lockedSpans.put(key, cacheSpan);
            newCacheSpan = cacheSpan;
        } else {
            newCacheSpan = null;
        }
        return newCacheSpan;
    }

    @Override // com.google.android.exoplayer2.upstream.cache.Cache
    public synchronized File startFile(String key, long position, long maxLength) throws Cache.CacheException {
        Assertions.checkState(this.lockedSpans.containsKey(key));
        if (!this.cacheDir.exists()) {
            removeStaleSpansAndCachedContents();
            this.cacheDir.mkdirs();
        }
        this.evictor.onStartFile(this, key, position, maxLength);
        return SimpleCacheSpan.getCacheFile(this.cacheDir, this.index.assignIdForKey(key), position, System.currentTimeMillis());
    }

    @Override // com.google.android.exoplayer2.upstream.cache.Cache
    public synchronized void commitFile(File file) throws Cache.CacheException {
        synchronized (this) {
            SimpleCacheSpan span = SimpleCacheSpan.createCacheEntry(file, this.index);
            Assertions.checkState(span != null);
            Assertions.checkState(this.lockedSpans.containsKey(span.key));
            if (file.exists()) {
                if (file.length() == 0) {
                    file.delete();
                } else {
                    Long length = Long.valueOf(getContentLength(span.key));
                    if (length.longValue() != -1) {
                        Assertions.checkState(span.position + span.length <= length.longValue());
                    }
                    addSpan(span);
                    this.index.store();
                    notifyAll();
                }
            }
        }
    }

    @Override // com.google.android.exoplayer2.upstream.cache.Cache
    public synchronized void releaseHoleSpan(CacheSpan holeSpan) {
        Assertions.checkState(holeSpan == this.lockedSpans.remove(holeSpan.key));
        notifyAll();
    }

    private SimpleCacheSpan getSpan(String key, long position) throws Throwable {
        CachedContent cachedContent = this.index.get(key);
        if (cachedContent == null) {
            return SimpleCacheSpan.createOpenHole(key, position);
        }
        while (true) {
            SimpleCacheSpan span = cachedContent.getSpan(position);
            if (span.isCached && !span.file.exists()) {
                removeStaleSpansAndCachedContents();
            } else {
                return span;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initialize() throws Throwable {
        if (!this.cacheDir.exists()) {
            this.cacheDir.mkdirs();
            return;
        }
        this.index.load();
        File[] files = this.cacheDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (!file.getName().equals(CachedContentIndex.FILE_NAME)) {
                    SimpleCacheSpan span = file.length() > 0 ? SimpleCacheSpan.createCacheEntry(file, this.index) : null;
                    if (span != null) {
                        addSpan(span);
                    } else {
                        file.delete();
                    }
                }
            }
            this.index.removeEmpty();
            this.index.store();
        }
    }

    private void addSpan(SimpleCacheSpan span) {
        this.index.add(span.key).addSpan(span);
        this.totalSpace += span.length;
        notifySpanAdded(span);
    }

    private void removeSpan(CacheSpan span, boolean removeEmptyCachedContent) throws Throwable {
        CachedContent cachedContent = this.index.get(span.key);
        Assertions.checkState(cachedContent.removeSpan(span));
        this.totalSpace -= span.length;
        if (removeEmptyCachedContent && cachedContent.isEmpty()) {
            this.index.removeEmpty(cachedContent.key);
            this.index.store();
        }
        notifySpanRemoved(span);
    }

    @Override // com.google.android.exoplayer2.upstream.cache.Cache
    public synchronized void removeSpan(CacheSpan span) throws Cache.CacheException {
        removeSpan(span, true);
    }

    private void removeStaleSpansAndCachedContents() throws Throwable {
        LinkedList<CacheSpan> spansToBeRemoved = new LinkedList<>();
        for (CachedContent cachedContent : this.index.getAll()) {
            Iterator<SimpleCacheSpan> it = cachedContent.getSpans().iterator();
            while (it.hasNext()) {
                CacheSpan span = it.next();
                if (!span.file.exists()) {
                    spansToBeRemoved.add(span);
                }
            }
        }
        Iterator<CacheSpan> it2 = spansToBeRemoved.iterator();
        while (it2.hasNext()) {
            removeSpan(it2.next(), false);
        }
        this.index.removeEmpty();
        this.index.store();
    }

    private void notifySpanRemoved(CacheSpan span) {
        ArrayList<Cache.Listener> keyListeners = this.listeners.get(span.key);
        if (keyListeners != null) {
            for (int i = keyListeners.size() - 1; i >= 0; i--) {
                keyListeners.get(i).onSpanRemoved(this, span);
            }
        }
        this.evictor.onSpanRemoved(this, span);
    }

    private void notifySpanAdded(SimpleCacheSpan span) {
        ArrayList<Cache.Listener> keyListeners = this.listeners.get(span.key);
        if (keyListeners != null) {
            for (int i = keyListeners.size() - 1; i >= 0; i--) {
                keyListeners.get(i).onSpanAdded(this, span);
            }
        }
        this.evictor.onSpanAdded(this, span);
    }

    private void notifySpanTouched(SimpleCacheSpan oldSpan, CacheSpan newSpan) {
        ArrayList<Cache.Listener> keyListeners = this.listeners.get(oldSpan.key);
        if (keyListeners != null) {
            for (int i = keyListeners.size() - 1; i >= 0; i--) {
                keyListeners.get(i).onSpanTouched(this, oldSpan, newSpan);
            }
        }
        this.evictor.onSpanTouched(this, oldSpan, newSpan);
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0014  */
    @Override // com.google.android.exoplayer2.upstream.cache.Cache
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized boolean isCached(java.lang.String r5, long r6, long r8) {
        /*
            r4 = this;
            monitor-enter(r4)
            com.google.android.exoplayer2.upstream.cache.CachedContentIndex r1 = r4.index     // Catch: java.lang.Throwable -> L16
            com.google.android.exoplayer2.upstream.cache.CachedContent r0 = r1.get(r5)     // Catch: java.lang.Throwable -> L16
            if (r0 == 0) goto L14
            long r2 = r0.getCachedBytes(r6, r8)     // Catch: java.lang.Throwable -> L16
            int r1 = (r2 > r8 ? 1 : (r2 == r8 ? 0 : -1))
            if (r1 < 0) goto L14
            r1 = 1
        L12:
            monitor-exit(r4)
            return r1
        L14:
            r1 = 0
            goto L12
        L16:
            r1 = move-exception
            monitor-exit(r4)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.upstream.cache.SimpleCache.isCached(java.lang.String, long, long):boolean");
    }

    @Override // com.google.android.exoplayer2.upstream.cache.Cache
    public synchronized long getCachedBytes(String key, long position, long length) {
        CachedContent cachedContent;
        cachedContent = this.index.get(key);
        return cachedContent != null ? cachedContent.getCachedBytes(position, length) : -length;
    }

    @Override // com.google.android.exoplayer2.upstream.cache.Cache
    public synchronized void setContentLength(String key, long length) throws Cache.CacheException {
        this.index.setContentLength(key, length);
        this.index.store();
    }

    @Override // com.google.android.exoplayer2.upstream.cache.Cache
    public synchronized long getContentLength(String key) {
        return this.index.getContentLength(key);
    }
}
