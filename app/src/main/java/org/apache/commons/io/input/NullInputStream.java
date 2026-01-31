package org.apache.commons.io.input;

import com.unisound.vui.priority.PriorityMap;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public class NullInputStream extends InputStream {
    private boolean eof;
    private long mark;
    private final boolean markSupported;
    private long position;
    private long readlimit;
    private final long size;
    private final boolean throwEofException;

    public NullInputStream(long size) {
        this(size, true, false);
    }

    public NullInputStream(long size, boolean markSupported, boolean throwEofException) {
        this.mark = -1L;
        this.size = size;
        this.markSupported = markSupported;
        this.throwEofException = throwEofException;
    }

    public long getPosition() {
        return this.position;
    }

    public long getSize() {
        return this.size;
    }

    @Override // java.io.InputStream
    public int available() {
        long avail = this.size - this.position;
        if (avail <= 0) {
            return 0;
        }
        if (avail > 2147483647L) {
            return PriorityMap.PRIORITY_MAX;
        }
        return (int) avail;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.eof = false;
        this.position = 0L;
        this.mark = -1L;
    }

    @Override // java.io.InputStream
    public synchronized void mark(int readlimit) {
        if (!this.markSupported) {
            throw new UnsupportedOperationException("Mark not supported");
        }
        this.mark = this.position;
        this.readlimit = readlimit;
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return this.markSupported;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.eof) {
            throw new IOException("Read after end of file");
        }
        if (this.position == this.size) {
            return doEndOfFile();
        }
        this.position++;
        return processByte();
    }

    @Override // java.io.InputStream
    public int read(byte[] bytes) throws IOException {
        return read(bytes, 0, bytes.length);
    }

    @Override // java.io.InputStream
    public int read(byte[] bytes, int offset, int length) throws IOException {
        if (this.eof) {
            throw new IOException("Read after end of file");
        }
        if (this.position == this.size) {
            return doEndOfFile();
        }
        this.position += length;
        int returnLength = length;
        if (this.position > this.size) {
            returnLength = length - ((int) (this.position - this.size));
            this.position = this.size;
        }
        processBytes(bytes, offset, returnLength);
        return returnLength;
    }

    @Override // java.io.InputStream
    public synchronized void reset() throws IOException {
        if (!this.markSupported) {
            throw new UnsupportedOperationException("Mark not supported");
        }
        if (this.mark < 0) {
            throw new IOException("No position has been marked");
        }
        if (this.position > this.mark + this.readlimit) {
            throw new IOException("Marked position [" + this.mark + "] is no longer valid - passed the read limit [" + this.readlimit + "]");
        }
        this.position = this.mark;
        this.eof = false;
    }

    @Override // java.io.InputStream
    public long skip(long numberOfBytes) throws IOException {
        if (this.eof) {
            throw new IOException("Skip after end of file");
        }
        if (this.position == this.size) {
            return doEndOfFile();
        }
        this.position += numberOfBytes;
        if (this.position <= this.size) {
            return numberOfBytes;
        }
        long returnLength = numberOfBytes - (this.position - this.size);
        this.position = this.size;
        return returnLength;
    }

    protected int processByte() {
        return 0;
    }

    protected void processBytes(byte[] bytes, int offset, int length) {
    }

    private int doEndOfFile() throws EOFException {
        this.eof = true;
        if (this.throwEofException) {
            throw new EOFException();
        }
        return -1;
    }
}
