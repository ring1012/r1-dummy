package com.google.android.exoplayer2.upstream.cache;

import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.util.Assertions;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.TreeSet;

/* loaded from: classes.dex */
final class CachedContent {
    private final TreeSet<SimpleCacheSpan> cachedSpans;
    public final int id;
    public final String key;
    private long length;

    public CachedContent(DataInputStream input) throws IOException {
        this(input.readInt(), input.readUTF(), input.readLong());
    }

    public CachedContent(int id, String key, long length) {
        this.id = id;
        this.key = key;
        this.length = length;
        this.cachedSpans = new TreeSet<>();
    }

    public void writeToStream(DataOutputStream output) throws IOException {
        output.writeInt(this.id);
        output.writeUTF(this.key);
        output.writeLong(this.length);
    }

    public long getLength() {
        return this.length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public void addSpan(SimpleCacheSpan span) {
        this.cachedSpans.add(span);
    }

    public TreeSet<SimpleCacheSpan> getSpans() {
        return this.cachedSpans;
    }

    public SimpleCacheSpan getSpan(long position) {
        SimpleCacheSpan lookupSpan = SimpleCacheSpan.createLookup(this.key, position);
        SimpleCacheSpan floorSpan = this.cachedSpans.floor(lookupSpan);
        if (floorSpan == null || floorSpan.position + floorSpan.length <= position) {
            SimpleCacheSpan ceilSpan = this.cachedSpans.ceiling(lookupSpan);
            return ceilSpan == null ? SimpleCacheSpan.createOpenHole(this.key, position) : SimpleCacheSpan.createClosedHole(this.key, position, ceilSpan.position - position);
        }
        return floorSpan;
    }

    public long getCachedBytes(long position, long length) {
        SimpleCacheSpan span = getSpan(position);
        if (span.isHoleSpan()) {
            return -Math.min(span.isOpenEnded() ? Long.MAX_VALUE : span.length, length);
        }
        long queryEndPosition = position + length;
        long currentEndPosition = span.position + span.length;
        if (currentEndPosition < queryEndPosition) {
            for (SimpleCacheSpan next : this.cachedSpans.tailSet(span, false)) {
                if (next.position > currentEndPosition) {
                    break;
                }
                currentEndPosition = Math.max(currentEndPosition, next.position + next.length);
                if (currentEndPosition >= queryEndPosition) {
                    break;
                }
            }
        }
        return Math.min(currentEndPosition - position, length);
    }

    public SimpleCacheSpan touch(SimpleCacheSpan cacheSpan) throws Cache.CacheException {
        Assertions.checkState(this.cachedSpans.remove(cacheSpan));
        SimpleCacheSpan newCacheSpan = cacheSpan.copyWithUpdatedLastAccessTime(this.id);
        if (!cacheSpan.file.renameTo(newCacheSpan.file)) {
            throw new Cache.CacheException("Renaming of " + cacheSpan.file + " to " + newCacheSpan.file + " failed.");
        }
        this.cachedSpans.add(newCacheSpan);
        return newCacheSpan;
    }

    public boolean isEmpty() {
        return this.cachedSpans.isEmpty();
    }

    public boolean removeSpan(CacheSpan span) {
        if (!this.cachedSpans.remove(span)) {
            return false;
        }
        span.file.delete();
        return true;
    }

    public int headerHashCode() {
        int result = this.id;
        return (((result * 31) + this.key.hashCode()) * 31) + ((int) (this.length ^ (this.length >>> 32)));
    }
}
