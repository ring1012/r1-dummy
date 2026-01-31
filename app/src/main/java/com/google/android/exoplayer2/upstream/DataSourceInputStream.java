package com.google.android.exoplayer2.upstream;

import android.support.annotation.NonNull;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public final class DataSourceInputStream extends InputStream {
    private final DataSource dataSource;
    private final DataSpec dataSpec;
    private long totalBytesRead;
    private boolean opened = false;
    private boolean closed = false;
    private final byte[] singleByteArray = new byte[1];

    public DataSourceInputStream(DataSource dataSource, DataSpec dataSpec) {
        this.dataSource = dataSource;
        this.dataSpec = dataSpec;
    }

    public long bytesRead() {
        return this.totalBytesRead;
    }

    public void open() throws IOException {
        checkOpened();
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        int length = read(this.singleByteArray);
        if (length == -1) {
            return -1;
        }
        return this.singleByteArray[0] & 255;
    }

    @Override // java.io.InputStream
    public int read(@NonNull byte[] buffer) throws IOException {
        return read(buffer, 0, buffer.length);
    }

    @Override // java.io.InputStream
    public int read(@NonNull byte[] buffer, int offset, int length) throws IOException {
        Assertions.checkState(!this.closed);
        checkOpened();
        int bytesRead = this.dataSource.read(buffer, offset, length);
        if (bytesRead == -1) {
            return -1;
        }
        this.totalBytesRead += bytesRead;
        return bytesRead;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!this.closed) {
            this.dataSource.close();
            this.closed = true;
        }
    }

    private void checkOpened() throws IOException {
        if (!this.opened) {
            this.dataSource.open(this.dataSpec);
            this.opened = true;
        }
    }
}
