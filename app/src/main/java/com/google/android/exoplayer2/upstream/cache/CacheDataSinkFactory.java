package com.google.android.exoplayer2.upstream.cache;

import com.google.android.exoplayer2.upstream.DataSink;

/* loaded from: classes.dex */
public final class CacheDataSinkFactory implements DataSink.Factory {
    private final int bufferSize;
    private final Cache cache;
    private final long maxCacheFileSize;

    public CacheDataSinkFactory(Cache cache, long maxCacheFileSize) {
        this(cache, maxCacheFileSize, CacheDataSink.DEFAULT_BUFFER_SIZE);
    }

    public CacheDataSinkFactory(Cache cache, long maxCacheFileSize, int bufferSize) {
        this.cache = cache;
        this.maxCacheFileSize = maxCacheFileSize;
        this.bufferSize = bufferSize;
    }

    @Override // com.google.android.exoplayer2.upstream.DataSink.Factory
    public DataSink createDataSink() {
        return new CacheDataSink(this.cache, this.maxCacheFileSize, this.bufferSize);
    }
}
