package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public class BoundedInputStream extends InputStream {
    private final InputStream in;
    private long mark;
    private final long max;
    private long pos;
    private boolean propagateClose;

    public BoundedInputStream(InputStream in, long size) {
        this.pos = 0L;
        this.mark = -1L;
        this.propagateClose = true;
        this.max = size;
        this.in = in;
    }

    public BoundedInputStream(InputStream in) {
        this(in, -1L);
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.max >= 0 && this.pos >= this.max) {
            return -1;
        }
        int i = this.in.read();
        this.pos++;
        return i;
    }

    @Override // java.io.InputStream
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override // java.io.InputStream
    public int read(byte[] b, int off, int len) throws IOException {
        if (this.max >= 0 && this.pos >= this.max) {
            return -1;
        }
        long maxRead = this.max >= 0 ? Math.min(len, this.max - this.pos) : len;
        int bytesRead = this.in.read(b, off, (int) maxRead);
        if (bytesRead == -1) {
            return -1;
        }
        this.pos += bytesRead;
        return bytesRead;
    }

    @Override // java.io.InputStream
    public long skip(long n) throws IOException {
        long toSkip = this.max >= 0 ? Math.min(n, this.max - this.pos) : n;
        long skippedBytes = this.in.skip(toSkip);
        this.pos += skippedBytes;
        return skippedBytes;
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        if (this.max < 0 || this.pos < this.max) {
            return this.in.available();
        }
        return 0;
    }

    public String toString() {
        return this.in.toString();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.propagateClose) {
            this.in.close();
        }
    }

    @Override // java.io.InputStream
    public synchronized void reset() throws IOException {
        this.in.reset();
        this.pos = this.mark;
    }

    @Override // java.io.InputStream
    public synchronized void mark(int readlimit) {
        this.in.mark(readlimit);
        this.mark = this.pos;
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return this.in.markSupported();
    }

    public boolean isPropagateClose() {
        return this.propagateClose;
    }

    public void setPropagateClose(boolean propagateClose) {
        this.propagateClose = propagateClose;
    }
}
