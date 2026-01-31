package com.google.android.exoplayer2.upstream.cache;

import com.google.android.exoplayer2.upstream.cache.Cache;
import java.util.Comparator;
import java.util.TreeSet;

/* loaded from: classes.dex */
public final class LeastRecentlyUsedCacheEvictor implements CacheEvictor, Comparator<CacheSpan> {
    private long currentSize;
    private final TreeSet<CacheSpan> leastRecentlyUsed = new TreeSet<>(this);
    private final long maxBytes;

    public LeastRecentlyUsedCacheEvictor(long maxBytes) {
        this.maxBytes = maxBytes;
    }

    @Override // com.google.android.exoplayer2.upstream.cache.CacheEvictor
    public void onCacheInitialized() {
    }

    @Override // com.google.android.exoplayer2.upstream.cache.CacheEvictor
    public void onStartFile(Cache cache, String key, long position, long maxLength) {
        evictCache(cache, maxLength);
    }

    @Override // com.google.android.exoplayer2.upstream.cache.Cache.Listener
    public void onSpanAdded(Cache cache, CacheSpan span) {
        this.leastRecentlyUsed.add(span);
        this.currentSize += span.length;
        evictCache(cache, 0L);
    }

    @Override // com.google.android.exoplayer2.upstream.cache.Cache.Listener
    public void onSpanRemoved(Cache cache, CacheSpan span) {
        this.leastRecentlyUsed.remove(span);
        this.currentSize -= span.length;
    }

    @Override // com.google.android.exoplayer2.upstream.cache.Cache.Listener
    public void onSpanTouched(Cache cache, CacheSpan oldSpan, CacheSpan newSpan) {
        onSpanRemoved(cache, oldSpan);
        onSpanAdded(cache, newSpan);
    }

    @Override // java.util.Comparator
    public int compare(CacheSpan lhs, CacheSpan rhs) {
        long lastAccessTimestampDelta = lhs.lastAccessTimestamp - rhs.lastAccessTimestamp;
        if (lastAccessTimestampDelta == 0) {
            return lhs.compareTo(rhs);
        }
        return lhs.lastAccessTimestamp < rhs.lastAccessTimestamp ? -1 : 1;
    }

    private void evictCache(Cache cache, long requiredSpace) {
        while (this.currentSize + requiredSpace > this.maxBytes) {
            try {
                cache.removeSpan(this.leastRecentlyUsed.first());
            } catch (Cache.CacheException e) {
            }
        }
    }
}
