package okhttp3.internal.connection;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import okhttp3.Address;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Route;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpCodec;
import okhttp3.internal.http2.ConnectionShutdownException;
import okhttp3.internal.http2.ErrorCode;
import okhttp3.internal.http2.StreamResetException;

/* loaded from: classes.dex */
public final class StreamAllocation {
    static final /* synthetic */ boolean $assertionsDisabled;
    public final Address address;
    private final Object callStackTrace;
    private boolean canceled;
    private HttpCodec codec;
    private RealConnection connection;
    private final ConnectionPool connectionPool;
    private int refusedStreamCount;
    private boolean released;
    private Route route;
    private final RouteSelector routeSelector;

    static {
        $assertionsDisabled = !StreamAllocation.class.desiredAssertionStatus();
    }

    public StreamAllocation(ConnectionPool connectionPool, Address address, Object callStackTrace) {
        this.connectionPool = connectionPool;
        this.address = address;
        this.routeSelector = new RouteSelector(address, routeDatabase());
        this.callStackTrace = callStackTrace;
    }

    public HttpCodec newStream(OkHttpClient client, boolean doExtensiveHealthChecks) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        int connectTimeout = client.connectTimeoutMillis();
        int readTimeout = client.readTimeoutMillis();
        int writeTimeout = client.writeTimeoutMillis();
        boolean connectionRetryEnabled = client.retryOnConnectionFailure();
        try {
            RealConnection resultConnection = findHealthyConnection(connectTimeout, readTimeout, writeTimeout, connectionRetryEnabled, doExtensiveHealthChecks);
            HttpCodec resultCodec = resultConnection.newCodec(client, this);
            synchronized (this.connectionPool) {
                this.codec = resultCodec;
            }
            return resultCodec;
        } catch (IOException e) {
            throw new RouteException(e);
        }
    }

    private RealConnection findHealthyConnection(int connectTimeout, int readTimeout, int writeTimeout, boolean connectionRetryEnabled, boolean doExtensiveHealthChecks) throws IllegalAccessException, IOException, IllegalArgumentException, InvocationTargetException {
        RealConnection candidate;
        while (true) {
            candidate = findConnection(connectTimeout, readTimeout, writeTimeout, connectionRetryEnabled);
            synchronized (this.connectionPool) {
                if (candidate.successCount != 0) {
                    if (candidate.isHealthy(doExtensiveHealthChecks)) {
                        break;
                    }
                    noNewStreams();
                } else {
                    break;
                }
            }
        }
        return candidate;
    }

    private RealConnection findConnection(int connectTimeout, int readTimeout, int writeTimeout, boolean connectionRetryEnabled) throws IllegalAccessException, IOException, IllegalArgumentException, InvocationTargetException {
        synchronized (this.connectionPool) {
            if (this.released) {
                throw new IllegalStateException("released");
            }
            if (this.codec != null) {
                throw new IllegalStateException("codec != null");
            }
            if (this.canceled) {
                throw new IOException("Canceled");
            }
            RealConnection allocatedConnection = this.connection;
            if (allocatedConnection == null || allocatedConnection.noNewStreams) {
                Internal.instance.get(this.connectionPool, this.address, this, null);
                if (this.connection != null) {
                    return this.connection;
                }
                Route selectedRoute = this.route;
                if (selectedRoute == null) {
                    selectedRoute = this.routeSelector.next();
                }
                synchronized (this.connectionPool) {
                    if (this.canceled) {
                        throw new IOException("Canceled");
                    }
                    Internal.instance.get(this.connectionPool, this.address, this, selectedRoute);
                    if (this.connection == null) {
                        this.route = selectedRoute;
                        this.refusedStreamCount = 0;
                        RealConnection result = new RealConnection(this.connectionPool, selectedRoute);
                        acquire(result);
                        result.connect(connectTimeout, readTimeout, writeTimeout, connectionRetryEnabled);
                        routeDatabase().connected(result.route());
                        Socket socket = null;
                        synchronized (this.connectionPool) {
                            Internal.instance.put(this.connectionPool, result);
                            if (result.isMultiplexed()) {
                                socket = Internal.instance.deduplicate(this.connectionPool, this.address, this);
                                result = this.connection;
                            }
                        }
                        Util.closeQuietly(socket);
                        return result;
                    }
                    return this.connection;
                }
            }
            return allocatedConnection;
        }
    }

    public void streamFinished(boolean noNewStreams, HttpCodec codec) {
        Socket socket;
        synchronized (this.connectionPool) {
            if (codec != null) {
                if (codec == this.codec) {
                    if (!noNewStreams) {
                        this.connection.successCount++;
                    }
                    socket = deallocate(noNewStreams, false, true);
                }
            }
            throw new IllegalStateException("expected " + this.codec + " but was " + codec);
        }
        Util.closeQuietly(socket);
    }

    public HttpCodec codec() {
        HttpCodec httpCodec;
        synchronized (this.connectionPool) {
            httpCodec = this.codec;
        }
        return httpCodec;
    }

    private RouteDatabase routeDatabase() {
        return Internal.instance.routeDatabase(this.connectionPool);
    }

    public synchronized RealConnection connection() {
        return this.connection;
    }

    public void release() throws IOException {
        Socket socket;
        synchronized (this.connectionPool) {
            socket = deallocate(false, true, false);
        }
        Util.closeQuietly(socket);
    }

    public void noNewStreams() throws IOException {
        Socket socket;
        synchronized (this.connectionPool) {
            socket = deallocate(true, false, false);
        }
        Util.closeQuietly(socket);
    }

    private Socket deallocate(boolean noNewStreams, boolean released, boolean streamFinished) {
        if (!$assertionsDisabled && !Thread.holdsLock(this.connectionPool)) {
            throw new AssertionError();
        }
        if (streamFinished) {
            this.codec = null;
        }
        if (released) {
            this.released = true;
        }
        Socket socket = null;
        if (this.connection != null) {
            if (noNewStreams) {
                this.connection.noNewStreams = true;
            }
            if (this.codec == null && (this.released || this.connection.noNewStreams)) {
                release(this.connection);
                if (this.connection.allocations.isEmpty()) {
                    this.connection.idleAtNanos = System.nanoTime();
                    if (Internal.instance.connectionBecameIdle(this.connectionPool, this.connection)) {
                        socket = this.connection.socket();
                    }
                }
                this.connection = null;
            }
        }
        return socket;
    }

    public void cancel() throws IOException {
        HttpCodec codecToCancel;
        RealConnection connectionToCancel;
        synchronized (this.connectionPool) {
            this.canceled = true;
            codecToCancel = this.codec;
            connectionToCancel = this.connection;
        }
        if (codecToCancel != null) {
            codecToCancel.cancel();
        } else if (connectionToCancel != null) {
            connectionToCancel.cancel();
        }
    }

    public void streamFailed(IOException e) throws IOException {
        Socket socket;
        boolean noNewStreams = false;
        synchronized (this.connectionPool) {
            if (e instanceof StreamResetException) {
                StreamResetException streamResetException = (StreamResetException) e;
                if (streamResetException.errorCode == ErrorCode.REFUSED_STREAM) {
                    this.refusedStreamCount++;
                }
                if (streamResetException.errorCode != ErrorCode.REFUSED_STREAM || this.refusedStreamCount > 1) {
                    noNewStreams = true;
                    this.route = null;
                }
            } else if (this.connection != null && (!this.connection.isMultiplexed() || (e instanceof ConnectionShutdownException))) {
                noNewStreams = true;
                if (this.connection.successCount == 0) {
                    if (this.route != null && e != null) {
                        this.routeSelector.connectFailed(this.route, e);
                    }
                    this.route = null;
                }
            }
            socket = deallocate(noNewStreams, false, true);
        }
        Util.closeQuietly(socket);
    }

    public void acquire(RealConnection connection) {
        if (!$assertionsDisabled && !Thread.holdsLock(this.connectionPool)) {
            throw new AssertionError();
        }
        if (this.connection != null) {
            throw new IllegalStateException();
        }
        this.connection = connection;
        connection.allocations.add(new StreamAllocationReference(this, this.callStackTrace));
    }

    private void release(RealConnection connection) {
        int size = connection.allocations.size();
        for (int i = 0; i < size; i++) {
            Reference<StreamAllocation> reference = connection.allocations.get(i);
            if (reference.get() == this) {
                connection.allocations.remove(i);
                return;
            }
        }
        throw new IllegalStateException();
    }

    public Socket releaseAndAcquire(RealConnection newConnection) {
        if (!$assertionsDisabled && !Thread.holdsLock(this.connectionPool)) {
            throw new AssertionError();
        }
        if (this.codec != null || this.connection.allocations.size() != 1) {
            throw new IllegalStateException();
        }
        Reference<StreamAllocation> onlyAllocation = this.connection.allocations.get(0);
        Socket socket = deallocate(true, false, false);
        this.connection = newConnection;
        newConnection.allocations.add(onlyAllocation);
        return socket;
    }

    public boolean hasMoreRoutes() {
        return this.route != null || this.routeSelector.hasNext();
    }

    public String toString() {
        RealConnection connection = connection();
        return connection != null ? connection.toString() : this.address.toString();
    }

    public static final class StreamAllocationReference extends WeakReference<StreamAllocation> {
        public final Object callStackTrace;

        StreamAllocationReference(StreamAllocation referent, Object callStackTrace) {
            super(referent);
            this.callStackTrace = callStackTrace;
        }
    }
}
