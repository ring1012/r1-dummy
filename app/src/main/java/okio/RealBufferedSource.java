package okio;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
final class RealBufferedSource implements BufferedSource {
    public final Buffer buffer = new Buffer();
    boolean closed;
    public final Source source;

    RealBufferedSource(Source source) {
        if (source == null) {
            throw new NullPointerException("source == null");
        }
        this.source = source;
    }

    @Override // okio.BufferedSource, okio.BufferedSink
    public Buffer buffer() {
        return this.buffer;
    }

    @Override // okio.Source
    public long read(Buffer sink, long byteCount) throws IOException {
        if (sink == null) {
            throw new IllegalArgumentException("sink == null");
        }
        if (byteCount < 0) {
            throw new IllegalArgumentException("byteCount < 0: " + byteCount);
        }
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        if (this.buffer.size == 0) {
            long read = this.source.read(this.buffer, 8192L);
            if (read == -1) {
                return -1L;
            }
        }
        long toRead = Math.min(byteCount, this.buffer.size);
        return this.buffer.read(sink, toRead);
    }

    @Override // okio.BufferedSource
    public boolean exhausted() throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        return this.buffer.exhausted() && this.source.read(this.buffer, 8192L) == -1;
    }

    @Override // okio.BufferedSource
    public void require(long byteCount) throws IOException {
        if (!request(byteCount)) {
            throw new EOFException();
        }
    }

    @Override // okio.BufferedSource
    public boolean request(long byteCount) throws IOException {
        if (byteCount < 0) {
            throw new IllegalArgumentException("byteCount < 0: " + byteCount);
        }
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        while (this.buffer.size < byteCount) {
            if (this.source.read(this.buffer, 8192L) == -1) {
                return false;
            }
        }
        return true;
    }

    @Override // okio.BufferedSource
    public byte readByte() throws IOException {
        require(1L);
        return this.buffer.readByte();
    }

    @Override // okio.BufferedSource
    public ByteString readByteString() throws IOException {
        this.buffer.writeAll(this.source);
        return this.buffer.readByteString();
    }

    @Override // okio.BufferedSource
    public ByteString readByteString(long byteCount) throws IOException {
        require(byteCount);
        return this.buffer.readByteString(byteCount);
    }

    @Override // okio.BufferedSource
    public int select(Options options) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        do {
            int index = this.buffer.selectPrefix(options);
            if (index == -1) {
                return -1;
            }
            int selectedSize = options.byteStrings[index].size();
            if (selectedSize <= this.buffer.size) {
                this.buffer.skip(selectedSize);
                return index;
            }
        } while (this.source.read(this.buffer, 8192L) != -1);
        return -1;
    }

    @Override // okio.BufferedSource
    public byte[] readByteArray() throws IOException {
        this.buffer.writeAll(this.source);
        return this.buffer.readByteArray();
    }

    @Override // okio.BufferedSource
    public byte[] readByteArray(long byteCount) throws IOException {
        require(byteCount);
        return this.buffer.readByteArray(byteCount);
    }

    @Override // okio.BufferedSource
    public int read(byte[] sink) throws IOException {
        return read(sink, 0, sink.length);
    }

    @Override // okio.BufferedSource
    public void readFully(byte[] sink) throws IOException {
        try {
            require(sink.length);
            this.buffer.readFully(sink);
        } catch (EOFException e) {
            int offset = 0;
            while (this.buffer.size > 0) {
                int read = this.buffer.read(sink, offset, (int) this.buffer.size);
                if (read == -1) {
                    throw new AssertionError();
                }
                offset += read;
            }
            throw e;
        }
    }

    @Override // okio.BufferedSource
    public int read(byte[] sink, int offset, int byteCount) throws IOException {
        Util.checkOffsetAndCount(sink.length, offset, byteCount);
        if (this.buffer.size == 0) {
            long read = this.source.read(this.buffer, 8192L);
            if (read == -1) {
                return -1;
            }
        }
        int toRead = (int) Math.min(byteCount, this.buffer.size);
        return this.buffer.read(sink, offset, toRead);
    }

    @Override // okio.BufferedSource
    public void readFully(Buffer sink, long byteCount) throws IOException {
        try {
            require(byteCount);
            this.buffer.readFully(sink, byteCount);
        } catch (EOFException e) {
            sink.writeAll(this.buffer);
            throw e;
        }
    }

    @Override // okio.BufferedSource
    public long readAll(Sink sink) throws IOException {
        if (sink == null) {
            throw new IllegalArgumentException("sink == null");
        }
        long totalBytesWritten = 0;
        while (this.source.read(this.buffer, 8192L) != -1) {
            long emitByteCount = this.buffer.completeSegmentByteCount();
            if (emitByteCount > 0) {
                totalBytesWritten += emitByteCount;
                sink.write(this.buffer, emitByteCount);
            }
        }
        if (this.buffer.size() > 0) {
            long totalBytesWritten2 = totalBytesWritten + this.buffer.size();
            sink.write(this.buffer, this.buffer.size());
            return totalBytesWritten2;
        }
        return totalBytesWritten;
    }

    @Override // okio.BufferedSource
    public String readUtf8() throws IOException {
        this.buffer.writeAll(this.source);
        return this.buffer.readUtf8();
    }

    @Override // okio.BufferedSource
    public String readUtf8(long byteCount) throws IOException {
        require(byteCount);
        return this.buffer.readUtf8(byteCount);
    }

    @Override // okio.BufferedSource
    public String readString(Charset charset) throws IOException {
        if (charset == null) {
            throw new IllegalArgumentException("charset == null");
        }
        this.buffer.writeAll(this.source);
        return this.buffer.readString(charset);
    }

    @Override // okio.BufferedSource
    public String readString(long byteCount, Charset charset) throws IOException {
        require(byteCount);
        if (charset == null) {
            throw new IllegalArgumentException("charset == null");
        }
        return this.buffer.readString(byteCount, charset);
    }

    @Override // okio.BufferedSource
    @Nullable
    public String readUtf8Line() throws IOException {
        long newline = indexOf((byte) 10);
        if (newline != -1) {
            return this.buffer.readUtf8Line(newline);
        }
        if (this.buffer.size != 0) {
            return readUtf8(this.buffer.size);
        }
        return null;
    }

    @Override // okio.BufferedSource
    public String readUtf8LineStrict() throws IOException {
        return readUtf8LineStrict(Long.MAX_VALUE);
    }

    @Override // okio.BufferedSource
    public String readUtf8LineStrict(long limit) throws IOException {
        if (limit < 0) {
            throw new IllegalArgumentException("limit < 0: " + limit);
        }
        long scanLength = limit == Long.MAX_VALUE ? Long.MAX_VALUE : limit + 1;
        long newline = indexOf((byte) 10, 0L, scanLength);
        if (newline != -1) {
            return this.buffer.readUtf8Line(newline);
        }
        if (scanLength < Long.MAX_VALUE && request(scanLength) && this.buffer.getByte(scanLength - 1) == 13 && request(1 + scanLength) && this.buffer.getByte(scanLength) == 10) {
            return this.buffer.readUtf8Line(scanLength);
        }
        Buffer data = new Buffer();
        this.buffer.copyTo(data, 0L, Math.min(32L, this.buffer.size()));
        throw new EOFException("\\n not found: limit=" + Math.min(this.buffer.size(), limit) + " content=" + data.readByteString().hex() + (char) 8230);
    }

    @Override // okio.BufferedSource
    public int readUtf8CodePoint() throws IOException {
        require(1L);
        byte b0 = this.buffer.getByte(0L);
        if ((b0 & 224) == 192) {
            require(2L);
        } else if ((b0 & 240) == 224) {
            require(3L);
        } else if ((b0 & 248) == 240) {
            require(4L);
        }
        return this.buffer.readUtf8CodePoint();
    }

    @Override // okio.BufferedSource
    public short readShort() throws IOException {
        require(2L);
        return this.buffer.readShort();
    }

    @Override // okio.BufferedSource
    public short readShortLe() throws IOException {
        require(2L);
        return this.buffer.readShortLe();
    }

    @Override // okio.BufferedSource
    public int readInt() throws IOException {
        require(4L);
        return this.buffer.readInt();
    }

    @Override // okio.BufferedSource
    public int readIntLe() throws IOException {
        require(4L);
        return this.buffer.readIntLe();
    }

    @Override // okio.BufferedSource
    public long readLong() throws IOException {
        require(8L);
        return this.buffer.readLong();
    }

    @Override // okio.BufferedSource
    public long readLongLe() throws IOException {
        require(8L);
        return this.buffer.readLongLe();
    }

    @Override // okio.BufferedSource
    public long readDecimalLong() throws IOException {
        require(1L);
        for (int pos = 0; request(pos + 1); pos++) {
            byte b = this.buffer.getByte(pos);
            if ((b < 48 || b > 57) && (pos != 0 || b != 45)) {
                if (pos == 0) {
                    throw new NumberFormatException(String.format("Expected leading [0-9] or '-' character but was %#x", Byte.valueOf(b)));
                }
                return this.buffer.readDecimalLong();
            }
        }
        return this.buffer.readDecimalLong();
    }

    @Override // okio.BufferedSource
    public long readHexadecimalUnsignedLong() throws IOException {
        require(1L);
        for (int pos = 0; request(pos + 1); pos++) {
            byte b = this.buffer.getByte(pos);
            if ((b < 48 || b > 57) && ((b < 97 || b > 102) && (b < 65 || b > 70))) {
                if (pos == 0) {
                    throw new NumberFormatException(String.format("Expected leading [0-9a-fA-F] character but was %#x", Byte.valueOf(b)));
                }
                return this.buffer.readHexadecimalUnsignedLong();
            }
        }
        return this.buffer.readHexadecimalUnsignedLong();
    }

    @Override // okio.BufferedSource
    public void skip(long byteCount) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        while (byteCount > 0) {
            if (this.buffer.size == 0 && this.source.read(this.buffer, 8192L) == -1) {
                throw new EOFException();
            }
            long toSkip = Math.min(byteCount, this.buffer.size());
            this.buffer.skip(toSkip);
            byteCount -= toSkip;
        }
    }

    @Override // okio.BufferedSource
    public long indexOf(byte b) throws IOException {
        return indexOf(b, 0L, Long.MAX_VALUE);
    }

    @Override // okio.BufferedSource
    public long indexOf(byte b, long fromIndex) throws IOException {
        return indexOf(b, fromIndex, Long.MAX_VALUE);
    }

    @Override // okio.BufferedSource
    public long indexOf(byte b, long fromIndex, long toIndex) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        if (fromIndex < 0 || toIndex < fromIndex) {
            throw new IllegalArgumentException(String.format("fromIndex=%s toIndex=%s", Long.valueOf(fromIndex), Long.valueOf(toIndex)));
        }
        while (fromIndex < toIndex) {
            long result = this.buffer.indexOf(b, fromIndex, toIndex);
            if (result == -1) {
                long lastBufferSize = this.buffer.size;
                if (lastBufferSize >= toIndex || this.source.read(this.buffer, 8192L) == -1) {
                    return -1L;
                }
                fromIndex = Math.max(fromIndex, lastBufferSize);
            } else {
                return result;
            }
        }
        return -1L;
    }

    @Override // okio.BufferedSource
    public long indexOf(ByteString bytes) throws IOException {
        return indexOf(bytes, 0L);
    }

    @Override // okio.BufferedSource
    public long indexOf(ByteString bytes, long fromIndex) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        while (true) {
            long result = this.buffer.indexOf(bytes, fromIndex);
            if (result == -1) {
                long lastBufferSize = this.buffer.size;
                if (this.source.read(this.buffer, 8192L) == -1) {
                    return -1L;
                }
                fromIndex = Math.max(fromIndex, (lastBufferSize - bytes.size()) + 1);
            } else {
                return result;
            }
        }
    }

    @Override // okio.BufferedSource
    public long indexOfElement(ByteString targetBytes) throws IOException {
        return indexOfElement(targetBytes, 0L);
    }

    @Override // okio.BufferedSource
    public long indexOfElement(ByteString targetBytes, long fromIndex) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        while (true) {
            long result = this.buffer.indexOfElement(targetBytes, fromIndex);
            if (result == -1) {
                long lastBufferSize = this.buffer.size;
                if (this.source.read(this.buffer, 8192L) == -1) {
                    return -1L;
                }
                fromIndex = Math.max(fromIndex, lastBufferSize);
            } else {
                return result;
            }
        }
    }

    @Override // okio.BufferedSource
    public boolean rangeEquals(long offset, ByteString bytes) throws IOException {
        return rangeEquals(offset, bytes, 0, bytes.size());
    }

    @Override // okio.BufferedSource
    public boolean rangeEquals(long offset, ByteString bytes, int bytesOffset, int byteCount) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        if (offset < 0 || bytesOffset < 0 || byteCount < 0 || bytes.size() - bytesOffset < byteCount) {
            return false;
        }
        for (int i = 0; i < byteCount; i++) {
            long bufferOffset = offset + i;
            if (!request(1 + bufferOffset) || this.buffer.getByte(bufferOffset) != bytes.getByte(bytesOffset + i)) {
                return false;
            }
        }
        return true;
    }

    @Override // okio.BufferedSource
    public InputStream inputStream() {
        return new InputStream() { // from class: okio.RealBufferedSource.1
            @Override // java.io.InputStream
            public int read() throws IOException {
                if (RealBufferedSource.this.closed) {
                    throw new IOException("closed");
                }
                if (RealBufferedSource.this.buffer.size == 0) {
                    long count = RealBufferedSource.this.source.read(RealBufferedSource.this.buffer, 8192L);
                    if (count == -1) {
                        return -1;
                    }
                }
                return RealBufferedSource.this.buffer.readByte() & 255;
            }

            @Override // java.io.InputStream
            public int read(byte[] data, int offset, int byteCount) throws IOException {
                if (RealBufferedSource.this.closed) {
                    throw new IOException("closed");
                }
                Util.checkOffsetAndCount(data.length, offset, byteCount);
                if (RealBufferedSource.this.buffer.size == 0) {
                    long count = RealBufferedSource.this.source.read(RealBufferedSource.this.buffer, 8192L);
                    if (count == -1) {
                        return -1;
                    }
                }
                return RealBufferedSource.this.buffer.read(data, offset, byteCount);
            }

            @Override // java.io.InputStream
            public int available() throws IOException {
                if (RealBufferedSource.this.closed) {
                    throw new IOException("closed");
                }
                return (int) Math.min(RealBufferedSource.this.buffer.size, 2147483647L);
            }

            @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
            public void close() throws IOException {
                RealBufferedSource.this.close();
            }

            public String toString() {
                return RealBufferedSource.this + ".inputStream()";
            }
        };
    }

    @Override // okio.Source, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            this.source.close();
            this.buffer.clear();
        }
    }

    @Override // okio.Source
    public Timeout timeout() {
        return this.source.timeout();
    }

    public String toString() {
        return "buffer(" + this.source + ")";
    }
}
