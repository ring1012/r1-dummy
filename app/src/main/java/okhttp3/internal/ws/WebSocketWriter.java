package okhttp3.internal.ws;

import java.io.IOException;
import java.util.Random;
import okio.Buffer;
import okio.BufferedSink;
import okio.ByteString;
import okio.Sink;
import okio.Timeout;

/* loaded from: classes.dex */
final class WebSocketWriter {
    static final /* synthetic */ boolean $assertionsDisabled;
    boolean activeWriter;
    final Buffer buffer = new Buffer();
    final FrameSink frameSink = new FrameSink();
    final boolean isClient;
    final byte[] maskBuffer;
    final byte[] maskKey;
    final Random random;
    final BufferedSink sink;
    boolean writerClosed;

    static {
        $assertionsDisabled = !WebSocketWriter.class.desiredAssertionStatus();
    }

    WebSocketWriter(boolean isClient, BufferedSink sink, Random random) {
        if (sink == null) {
            throw new NullPointerException("sink == null");
        }
        if (random == null) {
            throw new NullPointerException("random == null");
        }
        this.isClient = isClient;
        this.sink = sink;
        this.random = random;
        this.maskKey = isClient ? new byte[4] : null;
        this.maskBuffer = isClient ? new byte[8192] : null;
    }

    void writePing(ByteString payload) throws IOException {
        synchronized (this) {
            writeControlFrameSynchronized(9, payload);
        }
    }

    void writePong(ByteString payload) throws IOException {
        synchronized (this) {
            writeControlFrameSynchronized(10, payload);
        }
    }

    void writeClose(int code, ByteString reason) throws IOException {
        ByteString payload = ByteString.EMPTY;
        if (code != 0 || reason != null) {
            if (code != 0) {
                WebSocketProtocol.validateCloseCode(code);
            }
            Buffer buffer = new Buffer();
            buffer.writeShort(code);
            if (reason != null) {
                buffer.write(reason);
            }
            payload = buffer.readByteString();
        }
        synchronized (this) {
            try {
                writeControlFrameSynchronized(8, payload);
                this.writerClosed = true;
            } catch (Throwable th) {
                this.writerClosed = true;
                throw th;
            }
        }
    }

    private void writeControlFrameSynchronized(int opcode, ByteString payload) throws IOException {
        if (!$assertionsDisabled && !Thread.holdsLock(this)) {
            throw new AssertionError();
        }
        if (this.writerClosed) {
            throw new IOException("closed");
        }
        int length = payload.size();
        if (length > 125) {
            throw new IllegalArgumentException("Payload size must be less than or equal to 125");
        }
        int b0 = opcode | 128;
        this.sink.writeByte(b0);
        if (!this.isClient) {
            this.sink.writeByte(length);
            this.sink.write(payload);
        } else {
            int b1 = length | 128;
            this.sink.writeByte(b1);
            this.random.nextBytes(this.maskKey);
            this.sink.write(this.maskKey);
            byte[] bytes = payload.toByteArray();
            WebSocketProtocol.toggleMask(bytes, bytes.length, this.maskKey, 0L);
            this.sink.write(bytes);
        }
        this.sink.flush();
    }

    Sink newMessageSink(int formatOpcode, long contentLength) {
        if (this.activeWriter) {
            throw new IllegalStateException("Another message writer is active. Did you call close()?");
        }
        this.activeWriter = true;
        this.frameSink.formatOpcode = formatOpcode;
        this.frameSink.contentLength = contentLength;
        this.frameSink.isFirstFrame = true;
        this.frameSink.closed = false;
        return this.frameSink;
    }

    void writeMessageFrameSynchronized(int formatOpcode, long byteCount, boolean isFirstFrame, boolean isFinal) throws IOException {
        if (!$assertionsDisabled && !Thread.holdsLock(this)) {
            throw new AssertionError();
        }
        if (this.writerClosed) {
            throw new IOException("closed");
        }
        int b0 = isFirstFrame ? formatOpcode : 0;
        if (isFinal) {
            b0 |= 128;
        }
        this.sink.writeByte(b0);
        int b1 = 0;
        if (this.isClient) {
            b1 = 0 | 128;
        }
        if (byteCount <= 125) {
            this.sink.writeByte(b1 | ((int) byteCount));
        } else if (byteCount <= 65535) {
            this.sink.writeByte(b1 | 126);
            this.sink.writeShort((int) byteCount);
        } else {
            this.sink.writeByte(b1 | 127);
            this.sink.writeLong(byteCount);
        }
        if (this.isClient) {
            this.random.nextBytes(this.maskKey);
            this.sink.write(this.maskKey);
            long written = 0;
            while (written < byteCount) {
                int toRead = (int) Math.min(byteCount, this.maskBuffer.length);
                int read = this.buffer.read(this.maskBuffer, 0, toRead);
                if (read == -1) {
                    throw new AssertionError();
                }
                WebSocketProtocol.toggleMask(this.maskBuffer, read, this.maskKey, written);
                this.sink.write(this.maskBuffer, 0, read);
                written += read;
            }
        } else {
            this.sink.write(this.buffer, byteCount);
        }
        this.sink.emit();
    }

    final class FrameSink implements Sink {
        boolean closed;
        long contentLength;
        int formatOpcode;
        boolean isFirstFrame;

        FrameSink() {
        }

        @Override // okio.Sink
        public void write(Buffer source, long byteCount) throws IOException {
            if (this.closed) {
                throw new IOException("closed");
            }
            WebSocketWriter.this.buffer.write(source, byteCount);
            boolean deferWrite = this.isFirstFrame && this.contentLength != -1 && WebSocketWriter.this.buffer.size() > this.contentLength - 8192;
            long emitCount = WebSocketWriter.this.buffer.completeSegmentByteCount();
            if (emitCount > 0 && !deferWrite) {
                synchronized (WebSocketWriter.this) {
                    WebSocketWriter.this.writeMessageFrameSynchronized(this.formatOpcode, emitCount, this.isFirstFrame, false);
                }
                this.isFirstFrame = false;
            }
        }

        @Override // okio.Sink, java.io.Flushable
        public void flush() throws IOException {
            if (this.closed) {
                throw new IOException("closed");
            }
            synchronized (WebSocketWriter.this) {
                WebSocketWriter.this.writeMessageFrameSynchronized(this.formatOpcode, WebSocketWriter.this.buffer.size(), this.isFirstFrame, false);
            }
            this.isFirstFrame = false;
        }

        @Override // okio.Sink
        public Timeout timeout() {
            return WebSocketWriter.this.sink.timeout();
        }

        @Override // okio.Sink, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (this.closed) {
                throw new IOException("closed");
            }
            synchronized (WebSocketWriter.this) {
                WebSocketWriter.this.writeMessageFrameSynchronized(this.formatOpcode, WebSocketWriter.this.buffer.size(), this.isFirstFrame, true);
            }
            this.closed = true;
            WebSocketWriter.this.activeWriter = false;
        }
    }
}
