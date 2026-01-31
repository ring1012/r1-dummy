package com.google.android.exoplayer2.upstream.cache;

import android.net.Uri;
import android.support.annotation.Nullable;
import com.google.android.exoplayer2.upstream.DataSink;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSourceException;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.TeeDataSource;
import com.google.android.exoplayer2.upstream.cache.Cache;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public final class CacheDataSource implements DataSource {
    public static final long DEFAULT_MAX_CACHE_FILE_SIZE = 2097152;
    public static final int FLAG_BLOCK_ON_CACHE = 1;
    public static final int FLAG_IGNORE_CACHE_FOR_UNSET_LENGTH_REQUESTS = 4;
    public static final int FLAG_IGNORE_CACHE_ON_ERROR = 2;
    private final boolean blockOnCache;
    private long bytesRemaining;
    private final Cache cache;
    private final DataSource cacheReadDataSource;
    private final DataSource cacheWriteDataSource;
    private DataSource currentDataSource;
    private boolean currentRequestIgnoresCache;
    private boolean currentRequestUnbounded;

    @Nullable
    private final EventListener eventListener;
    private int flags;
    private final boolean ignoreCacheForUnsetLengthRequests;
    private final boolean ignoreCacheOnError;
    private String key;
    private CacheSpan lockedSpan;
    private long readPosition;
    private boolean seenCacheError;
    private long totalCachedBytesRead;
    private final DataSource upstreamDataSource;
    private Uri uri;

    public interface EventListener {
        void onCachedBytesRead(long j, long j2);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Flags {
    }

    public CacheDataSource(Cache cache, DataSource upstream, int flags) {
        this(cache, upstream, flags, DEFAULT_MAX_CACHE_FILE_SIZE);
    }

    public CacheDataSource(Cache cache, DataSource upstream, int flags, long maxCacheFileSize) {
        this(cache, upstream, new FileDataSource(), new CacheDataSink(cache, maxCacheFileSize), flags, null);
    }

    public CacheDataSource(Cache cache, DataSource upstream, DataSource cacheReadDataSource, DataSink cacheWriteDataSink, int flags, @Nullable EventListener eventListener) {
        this.cache = cache;
        this.cacheReadDataSource = cacheReadDataSource;
        this.blockOnCache = (flags & 1) != 0;
        this.ignoreCacheOnError = (flags & 2) != 0;
        this.ignoreCacheForUnsetLengthRequests = (flags & 4) != 0;
        this.upstreamDataSource = upstream;
        if (cacheWriteDataSink != null) {
            this.cacheWriteDataSource = new TeeDataSource(upstream, cacheWriteDataSink);
        } else {
            this.cacheWriteDataSource = null;
        }
        this.eventListener = eventListener;
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public long open(DataSpec dataSpec) throws IOException {
        boolean z = true;
        try {
            this.uri = dataSpec.uri;
            this.flags = dataSpec.flags;
            this.key = CacheUtil.getKey(dataSpec);
            this.readPosition = dataSpec.position;
            if ((!this.ignoreCacheOnError || !this.seenCacheError) && (dataSpec.length != -1 || !this.ignoreCacheForUnsetLengthRequests)) {
                z = false;
            }
            this.currentRequestIgnoresCache = z;
            if (dataSpec.length != -1 || this.currentRequestIgnoresCache) {
                this.bytesRemaining = dataSpec.length;
            } else {
                this.bytesRemaining = this.cache.getContentLength(this.key);
                if (this.bytesRemaining != -1) {
                    this.bytesRemaining -= dataSpec.position;
                    if (this.bytesRemaining <= 0) {
                        throw new DataSourceException(0);
                    }
                }
            }
            openNextSource(true);
            return this.bytesRemaining;
        } catch (IOException e) {
            handleBeforeThrow(e);
            throw e;
        }
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public int read(byte[] buffer, int offset, int readLength) throws IOException {
        if (readLength == 0) {
            return 0;
        }
        if (this.bytesRemaining == 0) {
            return -1;
        }
        try {
            int bytesRead = this.currentDataSource.read(buffer, offset, readLength);
            if (bytesRead >= 0) {
                if (this.currentDataSource == this.cacheReadDataSource) {
                    this.totalCachedBytesRead += bytesRead;
                }
                this.readPosition += bytesRead;
                if (this.bytesRemaining != -1) {
                    this.bytesRemaining -= bytesRead;
                    return bytesRead;
                }
                return bytesRead;
            }
            if (this.currentRequestUnbounded) {
                setContentLength(this.readPosition);
                this.bytesRemaining = 0L;
            }
            closeCurrentSource();
            if ((this.bytesRemaining > 0 || this.bytesRemaining == -1) && openNextSource(false)) {
                return read(buffer, offset, readLength);
            }
            return bytesRead;
        } catch (IOException e) {
            handleBeforeThrow(e);
            throw e;
        }
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public Uri getUri() {
        return this.currentDataSource == this.upstreamDataSource ? this.currentDataSource.getUri() : this.uri;
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public void close() throws IOException {
        this.uri = null;
        notifyBytesRead();
        try {
            closeCurrentSource();
        } catch (IOException e) {
            handleBeforeThrow(e);
            throw e;
        }
    }

    private boolean openNextSource(boolean initial) throws IOException {
        CacheSpan span;
        long length;
        DataSpec dataSpec;
        if (this.currentRequestIgnoresCache) {
            span = null;
        } else if (this.blockOnCache) {
            try {
                span = this.cache.startReadWrite(this.key, this.readPosition);
            } catch (InterruptedException e) {
                throw new InterruptedIOException();
            }
        } else {
            span = this.cache.startReadWriteNonBlocking(this.key, this.readPosition);
        }
        if (span == null) {
            this.currentDataSource = this.upstreamDataSource;
            dataSpec = new DataSpec(this.uri, this.readPosition, this.bytesRemaining, this.key, this.flags);
        } else if (span.isCached) {
            Uri fileUri = Uri.fromFile(span.file);
            long filePosition = this.readPosition - span.position;
            long length2 = span.length - filePosition;
            if (this.bytesRemaining != -1) {
                length2 = Math.min(length2, this.bytesRemaining);
            }
            dataSpec = new DataSpec(fileUri, this.readPosition, filePosition, length2, this.key, this.flags);
            this.currentDataSource = this.cacheReadDataSource;
        } else {
            if (span.isOpenEnded()) {
                length = this.bytesRemaining;
            } else {
                length = span.length;
                if (this.bytesRemaining != -1) {
                    length = Math.min(length, this.bytesRemaining);
                }
            }
            dataSpec = new DataSpec(this.uri, this.readPosition, length, this.key, this.flags);
            if (this.cacheWriteDataSource != null) {
                this.currentDataSource = this.cacheWriteDataSource;
                this.lockedSpan = span;
            } else {
                this.currentDataSource = this.upstreamDataSource;
                this.cache.releaseHoleSpan(span);
            }
        }
        this.currentRequestUnbounded = dataSpec.length == -1;
        boolean successful = false;
        long currentBytesRemaining = 0;
        try {
            currentBytesRemaining = this.currentDataSource.open(dataSpec);
            successful = true;
        } catch (IOException e2) {
            e = e2;
            if (!initial && this.currentRequestUnbounded) {
                Throwable cause = e;
                while (true) {
                    if (cause == null) {
                        break;
                    }
                    if (cause instanceof DataSourceException) {
                        int reason = ((DataSourceException) cause).reason;
                        if (reason == 0) {
                            e = null;
                            break;
                        }
                    }
                    cause = cause.getCause();
                }
            }
            if (e != null) {
                throw e;
            }
        }
        if (this.currentRequestUnbounded && currentBytesRemaining != -1) {
            this.bytesRemaining = currentBytesRemaining;
            setContentLength(dataSpec.position + this.bytesRemaining);
        }
        return successful;
    }

    private void setContentLength(long length) throws IOException {
        if (this.currentDataSource == this.cacheWriteDataSource) {
            this.cache.setContentLength(this.key, length);
        }
    }

    /* JADX WARN: Finally extract failed */
    private void closeCurrentSource() throws IOException {
        if (this.currentDataSource != null) {
            try {
                this.currentDataSource.close();
                this.currentDataSource = null;
                this.currentRequestUnbounded = false;
                if (this.lockedSpan != null) {
                    this.cache.releaseHoleSpan(this.lockedSpan);
                    this.lockedSpan = null;
                }
            } catch (Throwable th) {
                if (this.lockedSpan != null) {
                    this.cache.releaseHoleSpan(this.lockedSpan);
                    this.lockedSpan = null;
                }
                throw th;
            }
        }
    }

    private void handleBeforeThrow(IOException exception) {
        if (this.currentDataSource == this.cacheReadDataSource || (exception instanceof Cache.CacheException)) {
            this.seenCacheError = true;
        }
    }

    private void notifyBytesRead() {
        if (this.eventListener != null && this.totalCachedBytesRead > 0) {
            this.eventListener.onCachedBytesRead(this.cache.getCacheSpace(), this.totalCachedBytesRead);
            this.totalCachedBytesRead = 0L;
        }
    }
}
