package com.google.android.exoplayer2.upstream.cache;

import com.google.android.exoplayer2.upstream.DataSink;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.FileDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;

/* loaded from: classes.dex */
public final class CacheDataSourceFactory implements DataSource.Factory {
    private final Cache cache;
    private final DataSource.Factory cacheReadDataSourceFactory;
    private final DataSink.Factory cacheWriteDataSinkFactory;
    private final CacheDataSource.EventListener eventListener;
    private final int flags;
    private final DataSource.Factory upstreamFactory;

    public CacheDataSourceFactory(Cache cache, DataSource.Factory upstreamFactory, int flags) {
        this(cache, upstreamFactory, flags, CacheDataSource.DEFAULT_MAX_CACHE_FILE_SIZE);
    }

    public CacheDataSourceFactory(Cache cache, DataSource.Factory upstreamFactory, int flags, long maxCacheFileSize) {
        this(cache, upstreamFactory, new FileDataSourceFactory(), new CacheDataSinkFactory(cache, maxCacheFileSize), flags, null);
    }

    public CacheDataSourceFactory(Cache cache, DataSource.Factory upstreamFactory, DataSource.Factory cacheReadDataSourceFactory, DataSink.Factory cacheWriteDataSinkFactory, int flags, CacheDataSource.EventListener eventListener) {
        this.cache = cache;
        this.upstreamFactory = upstreamFactory;
        this.cacheReadDataSourceFactory = cacheReadDataSourceFactory;
        this.cacheWriteDataSinkFactory = cacheWriteDataSinkFactory;
        this.flags = flags;
        this.eventListener = eventListener;
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource.Factory
    public CacheDataSource createDataSource() {
        return new CacheDataSource(this.cache, this.upstreamFactory.createDataSource(), this.cacheReadDataSourceFactory.createDataSource(), this.cacheWriteDataSinkFactory != null ? this.cacheWriteDataSinkFactory.createDataSink() : null, this.flags, this.eventListener);
    }
}
