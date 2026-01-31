package okhttp3.internal.http1;

import java.io.EOFException;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.concurrent.TimeUnit;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.StreamAllocation;
import okhttp3.internal.http.HttpCodec;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http.RealResponseBody;
import okhttp3.internal.http.RequestLine;
import okhttp3.internal.http.StatusLine;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ForwardingTimeout;
import okio.Okio;
import okio.Sink;
import okio.Source;
import okio.Timeout;
import org.apache.commons.io.IOUtils;

/* loaded from: classes.dex */
public final class Http1Codec implements HttpCodec {
    private static final int STATE_CLOSED = 6;
    private static final int STATE_IDLE = 0;
    private static final int STATE_OPEN_REQUEST_BODY = 1;
    private static final int STATE_OPEN_RESPONSE_BODY = 4;
    private static final int STATE_READING_RESPONSE_BODY = 5;
    private static final int STATE_READ_RESPONSE_HEADERS = 3;
    private static final int STATE_WRITING_REQUEST_BODY = 2;
    final OkHttpClient client;
    final BufferedSink sink;
    final BufferedSource source;
    int state = 0;
    final StreamAllocation streamAllocation;

    public Http1Codec(OkHttpClient client, StreamAllocation streamAllocation, BufferedSource source, BufferedSink sink) {
        this.client = client;
        this.streamAllocation = streamAllocation;
        this.source = source;
        this.sink = sink;
    }

    @Override // okhttp3.internal.http.HttpCodec
    public Sink createRequestBody(Request request, long contentLength) {
        if ("chunked".equalsIgnoreCase(request.header("Transfer-Encoding"))) {
            return newChunkedSink();
        }
        if (contentLength != -1) {
            return newFixedLengthSink(contentLength);
        }
        throw new IllegalStateException("Cannot stream a request body without chunked encoding or a known content length!");
    }

    @Override // okhttp3.internal.http.HttpCodec
    public void cancel() throws IOException {
        RealConnection connection = this.streamAllocation.connection();
        if (connection != null) {
            connection.cancel();
        }
    }

    @Override // okhttp3.internal.http.HttpCodec
    public void writeRequestHeaders(Request request) throws IOException {
        String requestLine = RequestLine.get(request, this.streamAllocation.connection().route().proxy().type());
        writeRequest(request.headers(), requestLine);
    }

    @Override // okhttp3.internal.http.HttpCodec
    public ResponseBody openResponseBody(Response response) throws IOException {
        Source source = getTransferStream(response);
        return new RealResponseBody(response.headers(), Okio.buffer(source));
    }

    private Source getTransferStream(Response response) throws IOException {
        if (!HttpHeaders.hasBody(response)) {
            return newFixedLengthSource(0L);
        }
        if ("chunked".equalsIgnoreCase(response.header("Transfer-Encoding"))) {
            return newChunkedSource(response.request().url());
        }
        long contentLength = HttpHeaders.contentLength(response);
        if (contentLength != -1) {
            return newFixedLengthSource(contentLength);
        }
        return newUnknownLengthSource();
    }

    public boolean isClosed() {
        return this.state == 6;
    }

    @Override // okhttp3.internal.http.HttpCodec
    public void flushRequest() throws IOException {
        this.sink.flush();
    }

    @Override // okhttp3.internal.http.HttpCodec
    public void finishRequest() throws IOException {
        this.sink.flush();
    }

    public void writeRequest(Headers headers, String requestLine) throws IOException {
        if (this.state != 0) {
            throw new IllegalStateException("state: " + this.state);
        }
        this.sink.writeUtf8(requestLine).writeUtf8(IOUtils.LINE_SEPARATOR_WINDOWS);
        int size = headers.size();
        for (int i = 0; i < size; i++) {
            this.sink.writeUtf8(headers.name(i)).writeUtf8(": ").writeUtf8(headers.value(i)).writeUtf8(IOUtils.LINE_SEPARATOR_WINDOWS);
        }
        this.sink.writeUtf8(IOUtils.LINE_SEPARATOR_WINDOWS);
        this.state = 1;
    }

    @Override // okhttp3.internal.http.HttpCodec
    public Response.Builder readResponseHeaders(boolean expectContinue) throws NumberFormatException, IOException {
        if (this.state != 1 && this.state != 3) {
            throw new IllegalStateException("state: " + this.state);
        }
        try {
            StatusLine statusLine = StatusLine.parse(this.source.readUtf8LineStrict());
            Response.Builder responseBuilder = new Response.Builder().protocol(statusLine.protocol).code(statusLine.code).message(statusLine.message).headers(readHeaders());
            if (expectContinue && statusLine.code == 100) {
                return null;
            }
            this.state = 4;
            return responseBuilder;
        } catch (EOFException e) {
            IOException exception = new IOException("unexpected end of stream on " + this.streamAllocation);
            exception.initCause(e);
            throw exception;
        }
    }

    public Headers readHeaders() throws IOException {
        Headers.Builder headers = new Headers.Builder();
        while (true) {
            String line = this.source.readUtf8LineStrict();
            if (line.length() != 0) {
                Internal.instance.addLenient(headers, line);
            } else {
                return headers.build();
            }
        }
    }

    public Sink newChunkedSink() {
        if (this.state != 1) {
            throw new IllegalStateException("state: " + this.state);
        }
        this.state = 2;
        return new ChunkedSink();
    }

    public Sink newFixedLengthSink(long contentLength) {
        if (this.state != 1) {
            throw new IllegalStateException("state: " + this.state);
        }
        this.state = 2;
        return new FixedLengthSink(contentLength);
    }

    public Source newFixedLengthSource(long length) throws IOException {
        if (this.state != 4) {
            throw new IllegalStateException("state: " + this.state);
        }
        this.state = 5;
        return new FixedLengthSource(length);
    }

    public Source newChunkedSource(HttpUrl url) throws IOException {
        if (this.state != 4) {
            throw new IllegalStateException("state: " + this.state);
        }
        this.state = 5;
        return new ChunkedSource(url);
    }

    public Source newUnknownLengthSource() throws IOException {
        if (this.state != 4) {
            throw new IllegalStateException("state: " + this.state);
        }
        if (this.streamAllocation == null) {
            throw new IllegalStateException("streamAllocation == null");
        }
        this.state = 5;
        this.streamAllocation.noNewStreams();
        return new UnknownLengthSource();
    }

    void detachTimeout(ForwardingTimeout timeout) {
        Timeout oldDelegate = timeout.delegate();
        timeout.setDelegate(Timeout.NONE);
        oldDelegate.clearDeadline();
        oldDelegate.clearTimeout();
    }

    private final class FixedLengthSink implements Sink {
        private long bytesRemaining;
        private boolean closed;
        private final ForwardingTimeout timeout;

        FixedLengthSink(long bytesRemaining) {
            this.timeout = new ForwardingTimeout(Http1Codec.this.sink.timeout());
            this.bytesRemaining = bytesRemaining;
        }

        @Override // okio.Sink
        public Timeout timeout() {
            return this.timeout;
        }

        @Override // okio.Sink
        public void write(Buffer source, long byteCount) throws IOException {
            if (this.closed) {
                throw new IllegalStateException("closed");
            }
            Util.checkOffsetAndCount(source.size(), 0L, byteCount);
            if (byteCount > this.bytesRemaining) {
                throw new ProtocolException("expected " + this.bytesRemaining + " bytes but received " + byteCount);
            }
            Http1Codec.this.sink.write(source, byteCount);
            this.bytesRemaining -= byteCount;
        }

        @Override // okio.Sink, java.io.Flushable
        public void flush() throws IOException {
            if (!this.closed) {
                Http1Codec.this.sink.flush();
            }
        }

        @Override // okio.Sink, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (!this.closed) {
                this.closed = true;
                if (this.bytesRemaining > 0) {
                    throw new ProtocolException("unexpected end of stream");
                }
                Http1Codec.this.detachTimeout(this.timeout);
                Http1Codec.this.state = 3;
            }
        }
    }

    private final class ChunkedSink implements Sink {
        private boolean closed;
        private final ForwardingTimeout timeout;

        ChunkedSink() {
            this.timeout = new ForwardingTimeout(Http1Codec.this.sink.timeout());
        }

        @Override // okio.Sink
        public Timeout timeout() {
            return this.timeout;
        }

        @Override // okio.Sink
        public void write(Buffer source, long byteCount) throws IOException {
            if (this.closed) {
                throw new IllegalStateException("closed");
            }
            if (byteCount != 0) {
                Http1Codec.this.sink.writeHexadecimalUnsignedLong(byteCount);
                Http1Codec.this.sink.writeUtf8(IOUtils.LINE_SEPARATOR_WINDOWS);
                Http1Codec.this.sink.write(source, byteCount);
                Http1Codec.this.sink.writeUtf8(IOUtils.LINE_SEPARATOR_WINDOWS);
            }
        }

        @Override // okio.Sink, java.io.Flushable
        public synchronized void flush() throws IOException {
            if (!this.closed) {
                Http1Codec.this.sink.flush();
            }
        }

        @Override // okio.Sink, java.io.Closeable, java.lang.AutoCloseable
        public synchronized void close() throws IOException {
            if (!this.closed) {
                this.closed = true;
                Http1Codec.this.sink.writeUtf8("0\r\n\r\n");
                Http1Codec.this.detachTimeout(this.timeout);
                Http1Codec.this.state = 3;
            }
        }
    }

    private abstract class AbstractSource implements Source {
        protected boolean closed;
        protected final ForwardingTimeout timeout;

        private AbstractSource() {
            this.timeout = new ForwardingTimeout(Http1Codec.this.source.timeout());
        }

        @Override // okio.Source
        public Timeout timeout() {
            return this.timeout;
        }

        protected final void endOfInput(boolean reuseConnection) throws IOException {
            if (Http1Codec.this.state != 6) {
                if (Http1Codec.this.state != 5) {
                    throw new IllegalStateException("state: " + Http1Codec.this.state);
                }
                Http1Codec.this.detachTimeout(this.timeout);
                Http1Codec.this.state = 6;
                if (Http1Codec.this.streamAllocation != null) {
                    Http1Codec.this.streamAllocation.streamFinished(!reuseConnection, Http1Codec.this);
                }
            }
        }
    }

    private class FixedLengthSource extends AbstractSource {
        private long bytesRemaining;

        FixedLengthSource(long length) throws IOException {
            super();
            this.bytesRemaining = length;
            if (this.bytesRemaining == 0) {
                endOfInput(true);
            }
        }

        @Override // okio.Source
        public long read(Buffer sink, long byteCount) throws IOException {
            if (byteCount < 0) {
                throw new IllegalArgumentException("byteCount < 0: " + byteCount);
            }
            if (this.closed) {
                throw new IllegalStateException("closed");
            }
            if (this.bytesRemaining == 0) {
                return -1L;
            }
            long read = Http1Codec.this.source.read(sink, Math.min(this.bytesRemaining, byteCount));
            if (read == -1) {
                endOfInput(false);
                throw new ProtocolException("unexpected end of stream");
            }
            this.bytesRemaining -= read;
            if (this.bytesRemaining == 0) {
                endOfInput(true);
                return read;
            }
            return read;
        }

        @Override // okio.Source, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (!this.closed) {
                if (this.bytesRemaining != 0 && !Util.discard(this, 100, TimeUnit.MILLISECONDS)) {
                    endOfInput(false);
                }
                this.closed = true;
            }
        }
    }

    private class ChunkedSource extends AbstractSource {
        private static final long NO_CHUNK_YET = -1;
        private long bytesRemainingInChunk;
        private boolean hasMoreChunks;
        private final HttpUrl url;

        ChunkedSource(HttpUrl url) {
            super();
            this.bytesRemainingInChunk = -1L;
            this.hasMoreChunks = true;
            this.url = url;
        }

        @Override // okio.Source
        public long read(Buffer sink, long byteCount) throws IOException {
            if (byteCount < 0) {
                throw new IllegalArgumentException("byteCount < 0: " + byteCount);
            }
            if (this.closed) {
                throw new IllegalStateException("closed");
            }
            if (!this.hasMoreChunks) {
                return -1L;
            }
            if (this.bytesRemainingInChunk == 0 || this.bytesRemainingInChunk == -1) {
                readChunkSize();
                if (!this.hasMoreChunks) {
                    return -1L;
                }
            }
            long read = Http1Codec.this.source.read(sink, Math.min(byteCount, this.bytesRemainingInChunk));
            if (read == -1) {
                endOfInput(false);
                throw new ProtocolException("unexpected end of stream");
            }
            this.bytesRemainingInChunk -= read;
            return read;
        }

        private void readChunkSize() throws IOException {
            if (this.bytesRemainingInChunk != -1) {
                Http1Codec.this.source.readUtf8LineStrict();
            }
            try {
                this.bytesRemainingInChunk = Http1Codec.this.source.readHexadecimalUnsignedLong();
                String extensions = Http1Codec.this.source.readUtf8LineStrict().trim();
                if (this.bytesRemainingInChunk < 0 || !(extensions.isEmpty() || extensions.startsWith(";"))) {
                    throw new ProtocolException("expected chunk size and optional extensions but was \"" + this.bytesRemainingInChunk + extensions + "\"");
                }
                if (this.bytesRemainingInChunk == 0) {
                    this.hasMoreChunks = false;
                    HttpHeaders.receiveHeaders(Http1Codec.this.client.cookieJar(), this.url, Http1Codec.this.readHeaders());
                    endOfInput(true);
                }
            } catch (NumberFormatException e) {
                throw new ProtocolException(e.getMessage());
            }
        }

        @Override // okio.Source, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (!this.closed) {
                if (this.hasMoreChunks && !Util.discard(this, 100, TimeUnit.MILLISECONDS)) {
                    endOfInput(false);
                }
                this.closed = true;
            }
        }
    }

    private class UnknownLengthSource extends AbstractSource {
        private boolean inputExhausted;

        UnknownLengthSource() {
            super();
        }

        @Override // okio.Source
        public long read(Buffer sink, long byteCount) throws IOException {
            if (byteCount < 0) {
                throw new IllegalArgumentException("byteCount < 0: " + byteCount);
            }
            if (this.closed) {
                throw new IllegalStateException("closed");
            }
            if (this.inputExhausted) {
                return -1L;
            }
            long read = Http1Codec.this.source.read(sink, byteCount);
            if (read == -1) {
                this.inputExhausted = true;
                endOfInput(true);
                return -1L;
            }
            return read;
        }

        @Override // okio.Source, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (!this.closed) {
                if (!this.inputExhausted) {
                    endOfInput(false);
                }
                this.closed = true;
            }
        }
    }
}
