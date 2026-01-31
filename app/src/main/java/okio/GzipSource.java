package okio;

import java.io.EOFException;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/* loaded from: classes.dex */
public final class GzipSource implements Source {
    private static final byte FCOMMENT = 4;
    private static final byte FEXTRA = 2;
    private static final byte FHCRC = 1;
    private static final byte FNAME = 3;
    private static final byte SECTION_BODY = 1;
    private static final byte SECTION_DONE = 3;
    private static final byte SECTION_HEADER = 0;
    private static final byte SECTION_TRAILER = 2;
    private final Inflater inflater;
    private final InflaterSource inflaterSource;
    private final BufferedSource source;
    private int section = 0;
    private final CRC32 crc = new CRC32();

    public GzipSource(Source source) {
        if (source == null) {
            throw new IllegalArgumentException("source == null");
        }
        this.inflater = new Inflater(true);
        this.source = Okio.buffer(source);
        this.inflaterSource = new InflaterSource(this.source, this.inflater);
    }

    @Override // okio.Source
    public long read(Buffer sink, long byteCount) throws DataFormatException, IOException {
        if (byteCount < 0) {
            throw new IllegalArgumentException("byteCount < 0: " + byteCount);
        }
        if (byteCount == 0) {
            return 0L;
        }
        if (this.section == 0) {
            consumeHeader();
            this.section = 1;
        }
        if (this.section == 1) {
            long offset = sink.size;
            long result = this.inflaterSource.read(sink, byteCount);
            if (result != -1) {
                updateCrc(sink, offset, result);
                return result;
            }
            this.section = 2;
        }
        if (this.section == 2) {
            consumeTrailer();
            this.section = 3;
            if (!this.source.exhausted()) {
                throw new IOException("gzip finished without exhausting source");
            }
        }
        return -1L;
    }

    private void consumeHeader() throws IOException {
        this.source.require(10L);
        byte flags = this.source.buffer().getByte(3L);
        boolean fhcrc = ((flags >> 1) & 1) == 1;
        if (fhcrc) {
            updateCrc(this.source.buffer(), 0L, 10L);
        }
        short id1id2 = this.source.readShort();
        checkEqual("ID1ID2", 8075, id1id2);
        this.source.skip(8L);
        if (((flags >> 2) & 1) == 1) {
            this.source.require(2L);
            if (fhcrc) {
                updateCrc(this.source.buffer(), 0L, 2L);
            }
            int xlen = this.source.buffer().readShortLe();
            this.source.require(xlen);
            if (fhcrc) {
                updateCrc(this.source.buffer(), 0L, xlen);
            }
            this.source.skip(xlen);
        }
        if (((flags >> 3) & 1) == 1) {
            long index = this.source.indexOf(SECTION_HEADER);
            if (index == -1) {
                throw new EOFException();
            }
            if (fhcrc) {
                updateCrc(this.source.buffer(), 0L, 1 + index);
            }
            this.source.skip(1 + index);
        }
        if (((flags >> 4) & 1) == 1) {
            long index2 = this.source.indexOf(SECTION_HEADER);
            if (index2 == -1) {
                throw new EOFException();
            }
            if (fhcrc) {
                updateCrc(this.source.buffer(), 0L, 1 + index2);
            }
            this.source.skip(1 + index2);
        }
        if (fhcrc) {
            checkEqual("FHCRC", this.source.readShortLe(), (short) this.crc.getValue());
            this.crc.reset();
        }
    }

    private void consumeTrailer() throws IOException {
        checkEqual("CRC", this.source.readIntLe(), (int) this.crc.getValue());
        checkEqual("ISIZE", this.source.readIntLe(), (int) this.inflater.getBytesWritten());
    }

    @Override // okio.Source
    public Timeout timeout() {
        return this.source.timeout();
    }

    @Override // okio.Source, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.inflaterSource.close();
    }

    private void updateCrc(Buffer buffer, long offset, long byteCount) {
        Segment s = buffer.head;
        while (offset >= s.limit - s.pos) {
            offset -= s.limit - s.pos;
            s = s.next;
        }
        while (byteCount > 0) {
            int pos = (int) (s.pos + offset);
            int toUpdate = (int) Math.min(s.limit - pos, byteCount);
            this.crc.update(s.data, pos, toUpdate);
            byteCount -= toUpdate;
            offset = 0;
            s = s.next;
        }
    }

    private void checkEqual(String name, int expected, int actual) throws IOException {
        if (actual != expected) {
            throw new IOException(String.format("%s: actual 0x%08x != expected 0x%08x", name, Integer.valueOf(actual), Integer.valueOf(expected)));
        }
    }
}
