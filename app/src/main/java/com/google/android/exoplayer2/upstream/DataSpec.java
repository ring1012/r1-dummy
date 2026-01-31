package com.google.android.exoplayer2.upstream;

import android.net.Uri;
import com.google.android.exoplayer2.util.Assertions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

/* loaded from: classes.dex */
public final class DataSpec {
    public static final int FLAG_ALLOW_CACHING_UNKNOWN_LENGTH = 2;
    public static final int FLAG_ALLOW_GZIP = 1;
    public final long absoluteStreamPosition;
    public final int flags;
    public final String key;
    public final long length;
    public final long position;
    public final byte[] postBody;
    public final Uri uri;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Flags {
    }

    public DataSpec(Uri uri) {
        this(uri, 0);
    }

    public DataSpec(Uri uri, int flags) {
        this(uri, 0L, -1L, null, flags);
    }

    public DataSpec(Uri uri, long absoluteStreamPosition, long length, String key) {
        this(uri, absoluteStreamPosition, absoluteStreamPosition, length, key, 0);
    }

    public DataSpec(Uri uri, long absoluteStreamPosition, long length, String key, int flags) {
        this(uri, absoluteStreamPosition, absoluteStreamPosition, length, key, flags);
    }

    public DataSpec(Uri uri, long absoluteStreamPosition, long position, long length, String key, int flags) {
        this(uri, null, absoluteStreamPosition, position, length, key, flags);
    }

    public DataSpec(Uri uri, byte[] postBody, long absoluteStreamPosition, long position, long length, String key, int flags) {
        Assertions.checkArgument(absoluteStreamPosition >= 0);
        Assertions.checkArgument(position >= 0);
        Assertions.checkArgument(length > 0 || length == -1);
        this.uri = uri;
        this.postBody = postBody;
        this.absoluteStreamPosition = absoluteStreamPosition;
        this.position = position;
        this.length = length;
        this.key = key;
        this.flags = flags;
    }

    public boolean isFlagSet(int flag) {
        return (this.flags & flag) == flag;
    }

    public String toString() {
        return "DataSpec[" + this.uri + ", " + Arrays.toString(this.postBody) + ", " + this.absoluteStreamPosition + ", " + this.position + ", " + this.length + ", " + this.key + ", " + this.flags + "]";
    }
}
