package okio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

/* loaded from: classes.dex */
public final class Okio {
    static final Logger logger = Logger.getLogger(Okio.class.getName());

    private Okio() {
    }

    public static BufferedSource buffer(Source source) {
        return new RealBufferedSource(source);
    }

    public static BufferedSink buffer(Sink sink) {
        return new RealBufferedSink(sink);
    }

    public static Sink sink(OutputStream out) {
        return sink(out, new Timeout());
    }

    private static Sink sink(final OutputStream out, final Timeout timeout) {
        if (out == null) {
            throw new IllegalArgumentException("out == null");
        }
        if (timeout == null) {
            throw new IllegalArgumentException("timeout == null");
        }
        return new Sink() { // from class: okio.Okio.1
            @Override // okio.Sink
            public void write(Buffer source, long byteCount) throws IOException {
                Util.checkOffsetAndCount(source.size, 0L, byteCount);
                while (byteCount > 0) {
                    timeout.throwIfReached();
                    Segment head = source.head;
                    int toCopy = (int) Math.min(byteCount, head.limit - head.pos);
                    out.write(head.data, head.pos, toCopy);
                    head.pos += toCopy;
                    byteCount -= toCopy;
                    source.size -= toCopy;
                    if (head.pos == head.limit) {
                        source.head = head.pop();
                        SegmentPool.recycle(head);
                    }
                }
            }

            @Override // okio.Sink, java.io.Flushable
            public void flush() throws IOException {
                out.flush();
            }

            @Override // okio.Sink, java.io.Closeable, java.lang.AutoCloseable
            public void close() throws IOException {
                out.close();
            }

            @Override // okio.Sink
            public Timeout timeout() {
                return timeout;
            }

            public String toString() {
                return "sink(" + out + ")";
            }
        };
    }

    public static Sink sink(Socket socket) throws IOException {
        if (socket == null) {
            throw new IllegalArgumentException("socket == null");
        }
        AsyncTimeout timeout = timeout(socket);
        Sink sink = sink(socket.getOutputStream(), timeout);
        return timeout.sink(sink);
    }

    public static Source source(InputStream in) {
        return source(in, new Timeout());
    }

    private static Source source(final InputStream in, final Timeout timeout) {
        if (in == null) {
            throw new IllegalArgumentException("in == null");
        }
        if (timeout == null) {
            throw new IllegalArgumentException("timeout == null");
        }
        return new Source() { // from class: okio.Okio.2
            @Override // okio.Source
            public long read(Buffer sink, long byteCount) throws IOException {
                if (byteCount < 0) {
                    throw new IllegalArgumentException("byteCount < 0: " + byteCount);
                }
                if (byteCount == 0) {
                    return 0L;
                }
                try {
                    timeout.throwIfReached();
                    Segment tail = sink.writableSegment(1);
                    int maxToCopy = (int) Math.min(byteCount, 8192 - tail.limit);
                    int bytesRead = in.read(tail.data, tail.limit, maxToCopy);
                    if (bytesRead == -1) {
                        return -1L;
                    }
                    tail.limit += bytesRead;
                    sink.size += bytesRead;
                    return bytesRead;
                } catch (AssertionError e) {
                    if (Okio.isAndroidGetsocknameError(e)) {
                        throw new IOException(e);
                    }
                    throw e;
                }
            }

            @Override // okio.Source, java.io.Closeable, java.lang.AutoCloseable
            public void close() throws IOException {
                in.close();
            }

            @Override // okio.Source
            public Timeout timeout() {
                return timeout;
            }

            public String toString() {
                return "source(" + in + ")";
            }
        };
    }

    public static Source source(File file) throws FileNotFoundException {
        if (file == null) {
            throw new IllegalArgumentException("file == null");
        }
        return source(new FileInputStream(file));
    }

    @IgnoreJRERequirement
    public static Source source(Path path, OpenOption... options) throws IOException {
        if (path == null) {
            throw new IllegalArgumentException("path == null");
        }
        return source(Files.newInputStream(path, options));
    }

    public static Sink sink(File file) throws FileNotFoundException {
        if (file == null) {
            throw new IllegalArgumentException("file == null");
        }
        return sink(new FileOutputStream(file));
    }

    public static Sink appendingSink(File file) throws FileNotFoundException {
        if (file == null) {
            throw new IllegalArgumentException("file == null");
        }
        return sink(new FileOutputStream(file, true));
    }

    @IgnoreJRERequirement
    public static Sink sink(Path path, OpenOption... options) throws IOException {
        if (path == null) {
            throw new IllegalArgumentException("path == null");
        }
        return sink(Files.newOutputStream(path, options));
    }

    public static Sink blackhole() {
        return new Sink() { // from class: okio.Okio.3
            @Override // okio.Sink
            public void write(Buffer source, long byteCount) throws IOException {
                source.skip(byteCount);
            }

            @Override // okio.Sink, java.io.Flushable
            public void flush() throws IOException {
            }

            @Override // okio.Sink
            public Timeout timeout() {
                return Timeout.NONE;
            }

            @Override // okio.Sink, java.io.Closeable, java.lang.AutoCloseable
            public void close() throws IOException {
            }
        };
    }

    public static Source source(Socket socket) throws IOException {
        if (socket == null) {
            throw new IllegalArgumentException("socket == null");
        }
        AsyncTimeout timeout = timeout(socket);
        Source source = source(socket.getInputStream(), timeout);
        return timeout.source(source);
    }

    private static AsyncTimeout timeout(final Socket socket) {
        return new AsyncTimeout() { // from class: okio.Okio.4
            @Override // okio.AsyncTimeout
            protected IOException newTimeoutException(@Nullable IOException cause) {
                InterruptedIOException ioe = new SocketTimeoutException("timeout");
                if (cause != null) {
                    ioe.initCause(cause);
                }
                return ioe;
            }

            @Override // okio.AsyncTimeout
            protected void timedOut() throws IOException {
                try {
                    socket.close();
                } catch (AssertionError e) {
                    if (Okio.isAndroidGetsocknameError(e)) {
                        Okio.logger.log(Level.WARNING, "Failed to close timed out socket " + socket, (Throwable) e);
                        return;
                    }
                    throw e;
                } catch (Exception e2) {
                    Okio.logger.log(Level.WARNING, "Failed to close timed out socket " + socket, (Throwable) e2);
                }
            }
        };
    }

    static boolean isAndroidGetsocknameError(AssertionError e) {
        return (e.getCause() == null || e.getMessage() == null || !e.getMessage().contains("getsockname failed")) ? false : true;
    }
}
