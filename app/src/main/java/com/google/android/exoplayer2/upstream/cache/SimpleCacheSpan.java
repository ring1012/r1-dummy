package com.google.android.exoplayer2.upstream.cache;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
final class SimpleCacheSpan extends CacheSpan {
    private static final Pattern CACHE_FILE_PATTERN_V1 = Pattern.compile("^(.+)\\.(\\d+)\\.(\\d+)\\.v1\\.exo$", 32);
    private static final Pattern CACHE_FILE_PATTERN_V2 = Pattern.compile("^(.+)\\.(\\d+)\\.(\\d+)\\.v2\\.exo$", 32);
    private static final Pattern CACHE_FILE_PATTERN_V3 = Pattern.compile("^(\\d+)\\.(\\d+)\\.(\\d+)\\.v3\\.exo$", 32);
    private static final String SUFFIX = ".v3.exo";

    public static File getCacheFile(File cacheDir, int id, long position, long lastAccessTimestamp) {
        return new File(cacheDir, id + "." + position + "." + lastAccessTimestamp + SUFFIX);
    }

    public static SimpleCacheSpan createLookup(String key, long position) {
        return new SimpleCacheSpan(key, position, -1L, C.TIME_UNSET, null);
    }

    public static SimpleCacheSpan createOpenHole(String key, long position) {
        return new SimpleCacheSpan(key, position, -1L, C.TIME_UNSET, null);
    }

    public static SimpleCacheSpan createClosedHole(String key, long position, long length) {
        return new SimpleCacheSpan(key, position, length, C.TIME_UNSET, null);
    }

    public static SimpleCacheSpan createCacheEntry(File file, CachedContentIndex index) throws NumberFormatException {
        String name = file.getName();
        if (!name.endsWith(SUFFIX)) {
            file = upgradeFile(file, index);
            if (file == null) {
                return null;
            }
            name = file.getName();
        }
        Matcher matcher = CACHE_FILE_PATTERN_V3.matcher(name);
        if (!matcher.matches()) {
            return null;
        }
        long length = file.length();
        int id = Integer.parseInt(matcher.group(1));
        String key = index.getKeyForId(id);
        if (key != null) {
            return new SimpleCacheSpan(key, Long.parseLong(matcher.group(2)), length, Long.parseLong(matcher.group(3)), file);
        }
        return null;
    }

    private static File upgradeFile(File file, CachedContentIndex index) {
        String key;
        String filename = file.getName();
        Matcher matcher = CACHE_FILE_PATTERN_V2.matcher(filename);
        if (matcher.matches()) {
            key = Util.unescapeFileName(matcher.group(1));
            if (key == null) {
                return null;
            }
        } else {
            matcher = CACHE_FILE_PATTERN_V1.matcher(filename);
            if (!matcher.matches()) {
                return null;
            }
            key = matcher.group(1);
        }
        File newCacheFile = getCacheFile(file.getParentFile(), index.assignIdForKey(key), Long.parseLong(matcher.group(2)), Long.parseLong(matcher.group(3)));
        if (file.renameTo(newCacheFile)) {
            return newCacheFile;
        }
        return null;
    }

    private SimpleCacheSpan(String key, long position, long length, long lastAccessTimestamp, File file) {
        super(key, position, length, lastAccessTimestamp, file);
    }

    public SimpleCacheSpan copyWithUpdatedLastAccessTime(int id) {
        Assertions.checkState(this.isCached);
        long now = System.currentTimeMillis();
        File newCacheFile = getCacheFile(this.file.getParentFile(), id, this.position, now);
        return new SimpleCacheSpan(this.key, this.position, this.length, now, newCacheFile);
    }
}
