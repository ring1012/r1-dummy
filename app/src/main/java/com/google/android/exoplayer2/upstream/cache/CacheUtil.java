package com.google.android.exoplayer2.upstream.cache;

import android.net.Uri;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.PriorityTaskManager;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.NavigableSet;

/* loaded from: classes.dex */
public final class CacheUtil {

    public static class CachingCounters {
        public long alreadyCachedBytes;
        public long downloadedBytes;
    }

    public static String generateKey(Uri uri) {
        return uri.toString();
    }

    public static String getKey(DataSpec dataSpec) {
        return dataSpec.key != null ? dataSpec.key : generateKey(dataSpec.uri);
    }

    public static CachingCounters getCached(DataSpec dataSpec, Cache cache, CachingCounters counters) {
        try {
            return internalCache(dataSpec, cache, null, null, null, 0, counters);
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    public static CachingCounters cache(DataSpec dataSpec, Cache cache, CacheDataSource dataSource, byte[] buffer, PriorityTaskManager priorityTaskManager, int priority, CachingCounters counters) throws InterruptedException, IOException {
        Assertions.checkNotNull(dataSource);
        Assertions.checkNotNull(buffer);
        return internalCache(dataSpec, cache, dataSource, buffer, priorityTaskManager, priority, counters);
    }

    private static CachingCounters internalCache(DataSpec dataSpec, Cache cache, CacheDataSource dataSource, byte[] buffer, PriorityTaskManager priorityTaskManager, int priority, CachingCounters counters) throws InterruptedException, IOException {
        long start = dataSpec.position;
        long left = dataSpec.length;
        String key = getKey(dataSpec);
        if (left == -1) {
            left = cache.getContentLength(key);
            if (left == -1) {
                left = Long.MAX_VALUE;
            }
        }
        if (counters == null) {
            counters = new CachingCounters();
        } else {
            counters.alreadyCachedBytes = 0L;
            counters.downloadedBytes = 0L;
        }
        while (true) {
            if (left <= 0) {
                break;
            }
            long blockLength = cache.getCachedBytes(key, start, left);
            if (blockLength > 0) {
                counters.alreadyCachedBytes += blockLength;
            } else {
                blockLength = -blockLength;
                if (dataSource != null && buffer != null) {
                    DataSpec subDataSpec = new DataSpec(dataSpec.uri, start, blockLength == Long.MAX_VALUE ? -1L : blockLength, key);
                    long read = readAndDiscard(subDataSpec, dataSource, buffer, priorityTaskManager, priority);
                    counters.downloadedBytes += read;
                    if (read < blockLength) {
                        break;
                    }
                } else {
                    if (blockLength == Long.MAX_VALUE) {
                        counters.downloadedBytes = -1L;
                        break;
                    }
                    counters.downloadedBytes += blockLength;
                }
            }
            start += blockLength;
            if (left != Long.MAX_VALUE) {
                left -= blockLength;
            }
        }
        return counters;
    }

    private static long readAndDiscard(DataSpec dataSpec, DataSource dataSource, byte[] buffer, PriorityTaskManager priorityTaskManager, int priority) throws InterruptedException, IOException {
        while (true) {
            if (priorityTaskManager != null) {
                priorityTaskManager.proceed(priority);
            }
            try {
                dataSource.open(dataSpec);
                long totalRead = 0;
                while (!Thread.interrupted()) {
                    int read = dataSource.read(buffer, 0, buffer.length);
                    if (read == -1) {
                        return totalRead;
                    }
                    totalRead += read;
                }
                throw new InterruptedException();
            } catch (PriorityTaskManager.PriorityTooLowException e) {
            } finally {
                Util.closeQuietly(dataSource);
            }
        }
    }

    public static void remove(Cache cache, String key) {
        NavigableSet<CacheSpan> cachedSpans = cache.getCachedSpans(key);
        if (cachedSpans != null) {
            for (CacheSpan cachedSpan : cachedSpans) {
                try {
                    cache.removeSpan(cachedSpan);
                } catch (Cache.CacheException e) {
                }
            }
        }
    }

    private CacheUtil() {
    }
}
