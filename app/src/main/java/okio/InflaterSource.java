package okio;

import java.io.EOFException;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/* loaded from: classes.dex */
public final class InflaterSource implements Source {
    private int bufferBytesHeldByInflater;
    private boolean closed;
    private final Inflater inflater;
    private final BufferedSource source;

    public InflaterSource(Source source, Inflater inflater) {
        this(Okio.buffer(source), inflater);
    }

    InflaterSource(BufferedSource source, Inflater inflater) {
        if (source == null) {
            throw new IllegalArgumentException("source == null");
        }
        if (inflater == null) {
            throw new IllegalArgumentException("inflater == null");
        }
        this.source = source;
        this.inflater = inflater;
    }

    @Override // okio.Source
    public long read(Buffer sink, long byteCount) throws DataFormatException, IOException {
        boolean sourceExhausted;
        if (byteCount < 0) {
            throw new IllegalArgumentException("byteCount < 0: " + byteCount);
        }
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        if (byteCount == 0) {
            return 0L;
        }
        do {
            sourceExhausted = refill();
            try {
                Segment tail = sink.writableSegment(1);
                int bytesInflated = this.inflater.inflate(tail.data, tail.limit, 8192 - tail.limit);
                if (bytesInflated > 0) {
                    tail.limit += bytesInflated;
                    sink.size += bytesInflated;
                    return bytesInflated;
                }
                if (this.inflater.finished() || this.inflater.needsDictionary()) {
                    releaseInflatedBytes();
                    if (tail.pos == tail.limit) {
                        sink.head = tail.pop();
                        SegmentPool.recycle(tail);
                    }
                    return -1L;
                }
            } catch (DataFormatException e) {
                throw new IOException(e);
            }
        } while (!sourceExhausted);
        throw new EOFException("source exhausted prematurely");
    }

    public boolean refill() throws IOException {
        if (!this.inflater.needsInput()) {
            return false;
        }
        releaseInflatedBytes();
        if (this.inflater.getRemaining() != 0) {
            throw new IllegalStateException("?");
        }
        if (this.source.exhausted()) {
            return true;
        }
        Segment head = this.source.buffer().head;
        this.bufferBytesHeldByInflater = head.limit - head.pos;
        this.inflater.setInput(head.data, head.pos, this.bufferBytesHeldByInflater);
        return false;
    }

    private void releaseInflatedBytes() throws IOException {
        if (this.bufferBytesHeldByInflater != 0) {
            int toRelease = this.bufferBytesHeldByInflater - this.inflater.getRemaining();
            this.bufferBytesHeldByInflater -= toRelease;
            this.source.skip(toRelease);
        }
    }

    @Override // okio.Source
    public Timeout timeout() {
        return this.source.timeout();
    }

    @Override // okio.Source, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!this.closed) {
            this.inflater.end();
            this.closed = true;
            this.source.close();
        }
    }
}
