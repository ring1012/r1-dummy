package okhttp3.internal.http2;

import android.support.v4.internal.view.SupportMenu;
import com.unisound.vui.priority.PriorityMap;
import java.io.Closeable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import okhttp3.Protocol;
import okhttp3.internal.NamedRunnable;
import okhttp3.internal.Util;
import okhttp3.internal.http2.Http2Reader;
import okhttp3.internal.platform.Platform;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.Okio;

/* loaded from: classes.dex */
public final class Http2Connection implements Closeable {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final int OKHTTP_CLIENT_WINDOW_SIZE = 16777216;
    static final ExecutorService executor;
    long bytesLeftInWriteWindow;
    final boolean client;
    final String hostname;
    int lastGoodStreamId;
    final Listener listener;
    private int nextPingId;
    int nextStreamId;
    private Map<Integer, Ping> pings;
    private final ExecutorService pushExecutor;
    final PushObserver pushObserver;
    final ReaderRunnable readerRunnable;
    boolean shutdown;
    final Socket socket;
    final Http2Writer writer;
    final Map<Integer, Http2Stream> streams = new LinkedHashMap();
    long unacknowledgedBytesRead = 0;
    Settings okHttpSettings = new Settings();
    final Settings peerSettings = new Settings();
    boolean receivedInitialPeerSettings = false;
    final Set<Integer> currentPushRequests = new LinkedHashSet();

    static {
        $assertionsDisabled = !Http2Connection.class.desiredAssertionStatus();
        executor = new ThreadPoolExecutor(0, PriorityMap.PRIORITY_MAX, 60L, TimeUnit.SECONDS, new SynchronousQueue(), Util.threadFactory("OkHttp Http2Connection", true));
    }

    Http2Connection(Builder builder) {
        this.pushObserver = builder.pushObserver;
        this.client = builder.client;
        this.listener = builder.listener;
        this.nextStreamId = builder.client ? 1 : 2;
        if (builder.client) {
            this.nextStreamId += 2;
        }
        this.nextPingId = builder.client ? 1 : 2;
        if (builder.client) {
            this.okHttpSettings.set(7, 16777216);
        }
        this.hostname = builder.hostname;
        this.pushExecutor = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue(), Util.threadFactory(Util.format("OkHttp %s Push Observer", this.hostname), true));
        this.peerSettings.set(7, SupportMenu.USER_MASK);
        this.peerSettings.set(5, 16384);
        this.bytesLeftInWriteWindow = this.peerSettings.getInitialWindowSize();
        this.socket = builder.socket;
        this.writer = new Http2Writer(builder.sink, this.client);
        this.readerRunnable = new ReaderRunnable(new Http2Reader(builder.source, this.client));
    }

    public Protocol getProtocol() {
        return Protocol.HTTP_2;
    }

    public synchronized int openStreamCount() {
        return this.streams.size();
    }

    synchronized Http2Stream getStream(int id) {
        return this.streams.get(Integer.valueOf(id));
    }

    synchronized Http2Stream removeStream(int streamId) {
        Http2Stream stream;
        stream = this.streams.remove(Integer.valueOf(streamId));
        notifyAll();
        return stream;
    }

    public synchronized int maxConcurrentStreams() {
        return this.peerSettings.getMaxConcurrentStreams(PriorityMap.PRIORITY_MAX);
    }

    public Http2Stream pushStream(int associatedStreamId, List<Header> requestHeaders, boolean out) throws IOException {
        if (this.client) {
            throw new IllegalStateException("Client cannot push requests.");
        }
        return newStream(associatedStreamId, requestHeaders, out);
    }

    public Http2Stream newStream(List<Header> requestHeaders, boolean out) throws IOException {
        return newStream(0, requestHeaders, out);
    }

    private Http2Stream newStream(int associatedStreamId, List<Header> requestHeaders, boolean out) throws IOException {
        int streamId;
        Http2Stream stream;
        boolean flushHeaders;
        boolean outFinished = !out;
        synchronized (this.writer) {
            synchronized (this) {
                if (this.shutdown) {
                    throw new ConnectionShutdownException();
                }
                streamId = this.nextStreamId;
                this.nextStreamId += 2;
                stream = new Http2Stream(streamId, this, outFinished, false, requestHeaders);
                flushHeaders = !out || this.bytesLeftInWriteWindow == 0 || stream.bytesLeftInWriteWindow == 0;
                if (stream.isOpen()) {
                    this.streams.put(Integer.valueOf(streamId), stream);
                }
            }
            if (associatedStreamId == 0) {
                this.writer.synStream(outFinished, streamId, associatedStreamId, requestHeaders);
            } else {
                if (this.client) {
                    throw new IllegalArgumentException("client streams shouldn't have associated stream IDs");
                }
                this.writer.pushPromise(associatedStreamId, streamId, requestHeaders);
            }
        }
        if (flushHeaders) {
            this.writer.flush();
        }
        return stream;
    }

    void writeSynReply(int streamId, boolean outFinished, List<Header> alternating) throws IOException {
        this.writer.synReply(outFinished, streamId, alternating);
    }

    public void writeData(int streamId, boolean outFinished, Buffer buffer, long byteCount) throws IOException {
        int toWrite;
        if (byteCount == 0) {
            this.writer.data(outFinished, streamId, buffer, 0);
            return;
        }
        while (byteCount > 0) {
            synchronized (this) {
                while (this.bytesLeftInWriteWindow <= 0) {
                    try {
                        if (!this.streams.containsKey(Integer.valueOf(streamId))) {
                            throw new IOException("stream closed");
                        }
                        wait();
                    } catch (InterruptedException e) {
                        throw new InterruptedIOException();
                    }
                }
                toWrite = Math.min((int) Math.min(byteCount, this.bytesLeftInWriteWindow), this.writer.maxDataLength());
                this.bytesLeftInWriteWindow -= toWrite;
            }
            byteCount -= toWrite;
            this.writer.data(outFinished && byteCount == 0, streamId, buffer, toWrite);
        }
    }

    void addBytesToWriteWindow(long delta) {
        this.bytesLeftInWriteWindow += delta;
        if (delta > 0) {
            notifyAll();
        }
    }

    void writeSynResetLater(final int streamId, final ErrorCode errorCode) {
        executor.execute(new NamedRunnable("OkHttp %s stream %d", new Object[]{this.hostname, Integer.valueOf(streamId)}) { // from class: okhttp3.internal.http2.Http2Connection.1
            @Override // okhttp3.internal.NamedRunnable
            public void execute() {
                try {
                    Http2Connection.this.writeSynReset(streamId, errorCode);
                } catch (IOException e) {
                }
            }
        });
    }

    void writeSynReset(int streamId, ErrorCode statusCode) throws IOException {
        this.writer.rstStream(streamId, statusCode);
    }

    void writeWindowUpdateLater(final int streamId, final long unacknowledgedBytesRead) {
        executor.execute(new NamedRunnable("OkHttp Window Update %s stream %d", new Object[]{this.hostname, Integer.valueOf(streamId)}) { // from class: okhttp3.internal.http2.Http2Connection.2
            @Override // okhttp3.internal.NamedRunnable
            public void execute() {
                try {
                    Http2Connection.this.writer.windowUpdate(streamId, unacknowledgedBytesRead);
                } catch (IOException e) {
                }
            }
        });
    }

    public Ping ping() throws IOException {
        int pingId;
        Ping ping = new Ping();
        synchronized (this) {
            if (this.shutdown) {
                throw new ConnectionShutdownException();
            }
            pingId = this.nextPingId;
            this.nextPingId += 2;
            if (this.pings == null) {
                this.pings = new LinkedHashMap();
            }
            this.pings.put(Integer.valueOf(pingId), ping);
        }
        writePing(false, pingId, 1330343787, ping);
        return ping;
    }

    void writePingLater(final boolean reply, final int payload1, final int payload2, final Ping ping) {
        executor.execute(new NamedRunnable("OkHttp %s ping %08x%08x", new Object[]{this.hostname, Integer.valueOf(payload1), Integer.valueOf(payload2)}) { // from class: okhttp3.internal.http2.Http2Connection.3
            @Override // okhttp3.internal.NamedRunnable
            public void execute() {
                try {
                    Http2Connection.this.writePing(reply, payload1, payload2, ping);
                } catch (IOException e) {
                }
            }
        });
    }

    void writePing(boolean reply, int payload1, int payload2, Ping ping) throws IOException {
        synchronized (this.writer) {
            if (ping != null) {
                ping.send();
                this.writer.ping(reply, payload1, payload2);
            } else {
                this.writer.ping(reply, payload1, payload2);
            }
        }
    }

    synchronized Ping removePing(int id) {
        return this.pings != null ? this.pings.remove(Integer.valueOf(id)) : null;
    }

    public void flush() throws IOException {
        this.writer.flush();
    }

    public void shutdown(ErrorCode statusCode) throws IOException {
        synchronized (this.writer) {
            synchronized (this) {
                if (!this.shutdown) {
                    this.shutdown = true;
                    int lastGoodStreamId = this.lastGoodStreamId;
                    this.writer.goAway(lastGoodStreamId, statusCode, Util.EMPTY_BYTE_ARRAY);
                }
            }
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        close(ErrorCode.NO_ERROR, ErrorCode.CANCEL);
    }

    void close(ErrorCode connectionCode, ErrorCode streamCode) throws IOException {
        if (!$assertionsDisabled && Thread.holdsLock(this)) {
            throw new AssertionError();
        }
        IOException thrown = null;
        try {
            shutdown(connectionCode);
        } catch (IOException e) {
            thrown = e;
        }
        Http2Stream[] streamsToClose = null;
        Ping[] pingsToCancel = null;
        synchronized (this) {
            if (!this.streams.isEmpty()) {
                streamsToClose = (Http2Stream[]) this.streams.values().toArray(new Http2Stream[this.streams.size()]);
                this.streams.clear();
            }
            if (this.pings != null) {
                pingsToCancel = (Ping[]) this.pings.values().toArray(new Ping[this.pings.size()]);
                this.pings = null;
            }
        }
        if (streamsToClose != null) {
            for (Http2Stream stream : streamsToClose) {
                try {
                    stream.close(streamCode);
                } catch (IOException e2) {
                    if (thrown != null) {
                        thrown = e2;
                    }
                }
            }
        }
        if (pingsToCancel != null) {
            for (Ping ping : pingsToCancel) {
                ping.cancel();
            }
        }
        try {
            this.writer.close();
        } catch (IOException e3) {
            if (thrown == null) {
                thrown = e3;
            }
        }
        try {
            this.socket.close();
        } catch (IOException e4) {
            thrown = e4;
        }
        if (thrown != null) {
            throw thrown;
        }
    }

    public void start() throws IOException {
        start(true);
    }

    void start(boolean sendConnectionPreface) throws IOException {
        if (sendConnectionPreface) {
            this.writer.connectionPreface();
            this.writer.settings(this.okHttpSettings);
            int windowSize = this.okHttpSettings.getInitialWindowSize();
            if (windowSize != 65535) {
                this.writer.windowUpdate(0, windowSize - SupportMenu.USER_MASK);
            }
        }
        new Thread(this.readerRunnable).start();
    }

    public void setSettings(Settings settings) throws IOException {
        synchronized (this.writer) {
            synchronized (this) {
                if (this.shutdown) {
                    throw new ConnectionShutdownException();
                }
                this.okHttpSettings.merge(settings);
                this.writer.settings(settings);
            }
        }
    }

    public synchronized boolean isShutdown() {
        return this.shutdown;
    }

    public static class Builder {
        boolean client;
        String hostname;
        Listener listener = Listener.REFUSE_INCOMING_STREAMS;
        PushObserver pushObserver = PushObserver.CANCEL;
        BufferedSink sink;
        Socket socket;
        BufferedSource source;

        public Builder(boolean client) {
            this.client = client;
        }

        public Builder socket(Socket socket) throws IOException {
            return socket(socket, ((InetSocketAddress) socket.getRemoteSocketAddress()).getHostName(), Okio.buffer(Okio.source(socket)), Okio.buffer(Okio.sink(socket)));
        }

        public Builder socket(Socket socket, String hostname, BufferedSource source, BufferedSink sink) {
            this.socket = socket;
            this.hostname = hostname;
            this.source = source;
            this.sink = sink;
            return this;
        }

        public Builder listener(Listener listener) {
            this.listener = listener;
            return this;
        }

        public Builder pushObserver(PushObserver pushObserver) {
            this.pushObserver = pushObserver;
            return this;
        }

        public Http2Connection build() throws IOException {
            return new Http2Connection(this);
        }
    }

    class ReaderRunnable extends NamedRunnable implements Http2Reader.Handler {
        final Http2Reader reader;

        ReaderRunnable(Http2Reader reader) {
            super("OkHttp %s", Http2Connection.this.hostname);
            this.reader = reader;
        }

        @Override // okhttp3.internal.NamedRunnable
        protected void execute() {
            ErrorCode connectionErrorCode = ErrorCode.INTERNAL_ERROR;
            ErrorCode streamErrorCode = ErrorCode.INTERNAL_ERROR;
            try {
                try {
                    this.reader.readConnectionPreface(this);
                    while (this.reader.nextFrame(false, this)) {
                    }
                    connectionErrorCode = ErrorCode.NO_ERROR;
                    streamErrorCode = ErrorCode.CANCEL;
                } catch (IOException e) {
                    connectionErrorCode = ErrorCode.PROTOCOL_ERROR;
                    streamErrorCode = ErrorCode.PROTOCOL_ERROR;
                    try {
                        Http2Connection.this.close(connectionErrorCode, streamErrorCode);
                    } catch (IOException e2) {
                    }
                    Util.closeQuietly(this.reader);
                }
            } finally {
                try {
                    Http2Connection.this.close(connectionErrorCode, streamErrorCode);
                } catch (IOException e3) {
                }
                Util.closeQuietly(this.reader);
            }
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void data(boolean inFinished, int streamId, BufferedSource source, int length) throws IOException {
            if (Http2Connection.this.pushedStream(streamId)) {
                Http2Connection.this.pushDataLater(streamId, source, length, inFinished);
                return;
            }
            Http2Stream dataStream = Http2Connection.this.getStream(streamId);
            if (dataStream == null) {
                Http2Connection.this.writeSynResetLater(streamId, ErrorCode.PROTOCOL_ERROR);
                source.skip(length);
            } else {
                dataStream.receiveData(source, length);
                if (inFinished) {
                    dataStream.receiveFin();
                }
            }
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void headers(boolean inFinished, int streamId, int associatedStreamId, List<Header> headerBlock) {
            if (Http2Connection.this.pushedStream(streamId)) {
                Http2Connection.this.pushHeadersLater(streamId, headerBlock, inFinished);
                return;
            }
            synchronized (Http2Connection.this) {
                if (!Http2Connection.this.shutdown) {
                    Http2Stream stream = Http2Connection.this.getStream(streamId);
                    if (stream == null) {
                        if (streamId > Http2Connection.this.lastGoodStreamId) {
                            if (streamId % 2 != Http2Connection.this.nextStreamId % 2) {
                                final Http2Stream newStream = new Http2Stream(streamId, Http2Connection.this, false, inFinished, headerBlock);
                                Http2Connection.this.lastGoodStreamId = streamId;
                                Http2Connection.this.streams.put(Integer.valueOf(streamId), newStream);
                                Http2Connection.executor.execute(new NamedRunnable("OkHttp %s stream %d", new Object[]{Http2Connection.this.hostname, Integer.valueOf(streamId)}) { // from class: okhttp3.internal.http2.Http2Connection.ReaderRunnable.1
                                    @Override // okhttp3.internal.NamedRunnable
                                    public void execute() {
                                        try {
                                            Http2Connection.this.listener.onStream(newStream);
                                        } catch (IOException e) {
                                            Platform.get().log(4, "Http2Connection.Listener failure for " + Http2Connection.this.hostname, e);
                                            try {
                                                newStream.close(ErrorCode.PROTOCOL_ERROR);
                                            } catch (IOException e2) {
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    } else {
                        stream.receiveHeaders(headerBlock);
                        if (inFinished) {
                            stream.receiveFin();
                        }
                    }
                }
            }
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void rstStream(int streamId, ErrorCode errorCode) {
            if (Http2Connection.this.pushedStream(streamId)) {
                Http2Connection.this.pushResetLater(streamId, errorCode);
                return;
            }
            Http2Stream rstStream = Http2Connection.this.removeStream(streamId);
            if (rstStream != null) {
                rstStream.receiveRstStream(errorCode);
            }
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void settings(boolean clearPrevious, Settings newSettings) {
            long delta = 0;
            Http2Stream[] streamsToNotify = null;
            synchronized (Http2Connection.this) {
                int priorWriteWindowSize = Http2Connection.this.peerSettings.getInitialWindowSize();
                if (clearPrevious) {
                    Http2Connection.this.peerSettings.clear();
                }
                Http2Connection.this.peerSettings.merge(newSettings);
                applyAndAckSettings(newSettings);
                int peerInitialWindowSize = Http2Connection.this.peerSettings.getInitialWindowSize();
                if (peerInitialWindowSize != -1 && peerInitialWindowSize != priorWriteWindowSize) {
                    delta = peerInitialWindowSize - priorWriteWindowSize;
                    if (!Http2Connection.this.receivedInitialPeerSettings) {
                        Http2Connection.this.addBytesToWriteWindow(delta);
                        Http2Connection.this.receivedInitialPeerSettings = true;
                    }
                    if (!Http2Connection.this.streams.isEmpty()) {
                        streamsToNotify = (Http2Stream[]) Http2Connection.this.streams.values().toArray(new Http2Stream[Http2Connection.this.streams.size()]);
                    }
                }
                Http2Connection.executor.execute(new NamedRunnable("OkHttp %s settings", Http2Connection.this.hostname) { // from class: okhttp3.internal.http2.Http2Connection.ReaderRunnable.2
                    @Override // okhttp3.internal.NamedRunnable
                    public void execute() {
                        Http2Connection.this.listener.onSettings(Http2Connection.this);
                    }
                });
            }
            if (streamsToNotify != null && delta != 0) {
                for (Http2Stream stream : streamsToNotify) {
                    synchronized (stream) {
                        stream.addBytesToWriteWindow(delta);
                    }
                }
            }
        }

        private void applyAndAckSettings(final Settings peerSettings) {
            Http2Connection.executor.execute(new NamedRunnable("OkHttp %s ACK Settings", new Object[]{Http2Connection.this.hostname}) { // from class: okhttp3.internal.http2.Http2Connection.ReaderRunnable.3
                @Override // okhttp3.internal.NamedRunnable
                public void execute() {
                    try {
                        Http2Connection.this.writer.applyAndAckSettings(peerSettings);
                    } catch (IOException e) {
                    }
                }
            });
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void ackSettings() {
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void ping(boolean reply, int payload1, int payload2) {
            if (reply) {
                Ping ping = Http2Connection.this.removePing(payload1);
                if (ping != null) {
                    ping.receive();
                    return;
                }
                return;
            }
            Http2Connection.this.writePingLater(true, payload1, payload2, null);
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void goAway(int lastGoodStreamId, ErrorCode errorCode, ByteString debugData) {
            Http2Stream[] streamsCopy;
            if (debugData.size() > 0) {
            }
            synchronized (Http2Connection.this) {
                streamsCopy = (Http2Stream[]) Http2Connection.this.streams.values().toArray(new Http2Stream[Http2Connection.this.streams.size()]);
                Http2Connection.this.shutdown = true;
            }
            for (Http2Stream http2Stream : streamsCopy) {
                if (http2Stream.getId() > lastGoodStreamId && http2Stream.isLocallyInitiated()) {
                    http2Stream.receiveRstStream(ErrorCode.REFUSED_STREAM);
                    Http2Connection.this.removeStream(http2Stream.getId());
                }
            }
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void windowUpdate(int streamId, long windowSizeIncrement) {
            if (streamId == 0) {
                synchronized (Http2Connection.this) {
                    Http2Connection.this.bytesLeftInWriteWindow += windowSizeIncrement;
                    Http2Connection.this.notifyAll();
                }
                return;
            }
            Http2Stream stream = Http2Connection.this.getStream(streamId);
            if (stream != null) {
                synchronized (stream) {
                    stream.addBytesToWriteWindow(windowSizeIncrement);
                }
            }
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void priority(int streamId, int streamDependency, int weight, boolean exclusive) {
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void pushPromise(int streamId, int promisedStreamId, List<Header> requestHeaders) {
            Http2Connection.this.pushRequestLater(promisedStreamId, requestHeaders);
        }

        @Override // okhttp3.internal.http2.Http2Reader.Handler
        public void alternateService(int streamId, String origin, ByteString protocol, String host, int port, long maxAge) {
        }
    }

    boolean pushedStream(int streamId) {
        return streamId != 0 && (streamId & 1) == 0;
    }

    void pushRequestLater(final int streamId, final List<Header> requestHeaders) {
        synchronized (this) {
            if (this.currentPushRequests.contains(Integer.valueOf(streamId))) {
                writeSynResetLater(streamId, ErrorCode.PROTOCOL_ERROR);
            } else {
                this.currentPushRequests.add(Integer.valueOf(streamId));
                this.pushExecutor.execute(new NamedRunnable("OkHttp %s Push Request[%s]", new Object[]{this.hostname, Integer.valueOf(streamId)}) { // from class: okhttp3.internal.http2.Http2Connection.4
                    @Override // okhttp3.internal.NamedRunnable
                    public void execute() {
                        boolean cancel = Http2Connection.this.pushObserver.onRequest(streamId, requestHeaders);
                        if (cancel) {
                            try {
                                Http2Connection.this.writer.rstStream(streamId, ErrorCode.CANCEL);
                                synchronized (Http2Connection.this) {
                                    Http2Connection.this.currentPushRequests.remove(Integer.valueOf(streamId));
                                }
                            } catch (IOException e) {
                            }
                        }
                    }
                });
            }
        }
    }

    void pushHeadersLater(final int streamId, final List<Header> requestHeaders, final boolean inFinished) {
        this.pushExecutor.execute(new NamedRunnable("OkHttp %s Push Headers[%s]", new Object[]{this.hostname, Integer.valueOf(streamId)}) { // from class: okhttp3.internal.http2.Http2Connection.5
            @Override // okhttp3.internal.NamedRunnable
            public void execute() {
                boolean cancel = Http2Connection.this.pushObserver.onHeaders(streamId, requestHeaders, inFinished);
                if (cancel) {
                    try {
                        Http2Connection.this.writer.rstStream(streamId, ErrorCode.CANCEL);
                    } catch (IOException e) {
                        return;
                    }
                }
                if (cancel || inFinished) {
                    synchronized (Http2Connection.this) {
                        Http2Connection.this.currentPushRequests.remove(Integer.valueOf(streamId));
                    }
                }
            }
        });
    }

    void pushDataLater(final int streamId, BufferedSource source, final int byteCount, final boolean inFinished) throws IOException {
        final Buffer buffer = new Buffer();
        source.require(byteCount);
        source.read(buffer, byteCount);
        if (buffer.size() != byteCount) {
            throw new IOException(buffer.size() + " != " + byteCount);
        }
        this.pushExecutor.execute(new NamedRunnable("OkHttp %s Push Data[%s]", new Object[]{this.hostname, Integer.valueOf(streamId)}) { // from class: okhttp3.internal.http2.Http2Connection.6
            @Override // okhttp3.internal.NamedRunnable
            public void execute() {
                try {
                    boolean cancel = Http2Connection.this.pushObserver.onData(streamId, buffer, byteCount, inFinished);
                    if (cancel) {
                        Http2Connection.this.writer.rstStream(streamId, ErrorCode.CANCEL);
                    }
                    if (cancel || inFinished) {
                        synchronized (Http2Connection.this) {
                            Http2Connection.this.currentPushRequests.remove(Integer.valueOf(streamId));
                        }
                    }
                } catch (IOException e) {
                }
            }
        });
    }

    void pushResetLater(final int streamId, final ErrorCode errorCode) {
        this.pushExecutor.execute(new NamedRunnable("OkHttp %s Push Reset[%s]", new Object[]{this.hostname, Integer.valueOf(streamId)}) { // from class: okhttp3.internal.http2.Http2Connection.7
            @Override // okhttp3.internal.NamedRunnable
            public void execute() {
                Http2Connection.this.pushObserver.onReset(streamId, errorCode);
                synchronized (Http2Connection.this) {
                    Http2Connection.this.currentPushRequests.remove(Integer.valueOf(streamId));
                }
            }
        });
    }

    public static abstract class Listener {
        public static final Listener REFUSE_INCOMING_STREAMS = new Listener() { // from class: okhttp3.internal.http2.Http2Connection.Listener.1
            @Override // okhttp3.internal.http2.Http2Connection.Listener
            public void onStream(Http2Stream stream) throws IOException {
                stream.close(ErrorCode.REFUSED_STREAM);
            }
        };

        public abstract void onStream(Http2Stream http2Stream) throws IOException;

        public void onSettings(Http2Connection connection) {
        }
    }
}
