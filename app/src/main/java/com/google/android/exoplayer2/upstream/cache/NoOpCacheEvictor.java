package com.google.android.exoplayer2.upstream.cache;

/* loaded from: classes.dex */
public final class NoOpCacheEvictor implements CacheEvictor {
    @Override // com.google.android.exoplayer2.upstream.cache.CacheEvictor
    public void onCacheInitialized() {
    }

    @Override // com.google.android.exoplayer2.upstream.cache.CacheEvictor
    public void onStartFile(Cache cache, String key, long position, long maxLength) {
    }

    @Override // com.google.android.exoplayer2.upstream.cache.Cache.Listener
    public void onSpanAdded(Cache cache, CacheSpan span) {
    }

    @Override // com.google.android.exoplayer2.upstream.cache.Cache.Listener
    public void onSpanRemoved(Cache cache, CacheSpan span) {
    }

    @Override // com.google.android.exoplayer2.upstream.cache.Cache.Listener
    public void onSpanTouched(Cache cache, CacheSpan oldSpan, CacheSpan newSpan) {
    }
}
