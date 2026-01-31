package okhttp3;

import com.google.android.exoplayer2.C;
import com.unisound.vui.priority.PriorityMap;
import java.io.IOException;
import java.lang.ref.Reference;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.RouteDatabase;
import okhttp3.internal.connection.StreamAllocation;
import okhttp3.internal.platform.Platform;

/* loaded from: classes.dex */
public final class ConnectionPool {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final Executor executor;
    private final Runnable cleanupRunnable;
    boolean cleanupRunning;
    private final Deque<RealConnection> connections;
    private final long keepAliveDurationNs;
    private final int maxIdleConnections;
    final RouteDatabase routeDatabase;

    static {
        $assertionsDisabled = !ConnectionPool.class.desiredAssertionStatus();
        executor = new ThreadPoolExecutor(0, PriorityMap.PRIORITY_MAX, 60L, TimeUnit.SECONDS, new SynchronousQueue(), Util.threadFactory("OkHttp ConnectionPool", true));
    }

    public ConnectionPool() {
        this(5, 5L, TimeUnit.MINUTES);
    }

    public ConnectionPool(int maxIdleConnections, long keepAliveDuration, TimeUnit timeUnit) {
        this.cleanupRunnable = new Runnable() { // from class: okhttp3.ConnectionPool.1
            @Override // java.lang.Runnable
            public void run() throws IOException {
                while (true) {
                    long waitNanos = ConnectionPool.this.cleanup(System.nanoTime());
                    if (waitNanos == -1) {
                        return;
                    }
                    if (waitNanos > 0) {
                        long waitMillis = waitNanos / C.MICROS_PER_SECOND;
                        long waitNanos2 = waitNanos - (waitMillis * C.MICROS_PER_SECOND);
                        synchronized (ConnectionPool.this) {
                            try {
                                ConnectionPool.this.wait(waitMillis, (int) waitNanos2);
                            } catch (InterruptedException e) {
                            }
                        }
                    }
                }
            }
        };
        this.connections = new ArrayDeque();
        this.routeDatabase = new RouteDatabase();
        this.maxIdleConnections = maxIdleConnections;
        this.keepAliveDurationNs = timeUnit.toNanos(keepAliveDuration);
        if (keepAliveDuration <= 0) {
            throw new IllegalArgumentException("keepAliveDuration <= 0: " + keepAliveDuration);
        }
    }

    public synchronized int idleConnectionCount() {
        int total;
        total = 0;
        for (RealConnection connection : this.connections) {
            if (connection.allocations.isEmpty()) {
                total++;
            }
        }
        return total;
    }

    public synchronized int connectionCount() {
        return this.connections.size();
    }

    @Nullable
    RealConnection get(Address address, StreamAllocation streamAllocation, Route route) {
        if (!$assertionsDisabled && !Thread.holdsLock(this)) {
            throw new AssertionError();
        }
        for (RealConnection connection : this.connections) {
            if (connection.isEligible(address, route)) {
                streamAllocation.acquire(connection);
                return connection;
            }
        }
        return null;
    }

    @Nullable
    Socket deduplicate(Address address, StreamAllocation streamAllocation) {
        if (!$assertionsDisabled && !Thread.holdsLock(this)) {
            throw new AssertionError();
        }
        for (RealConnection connection : this.connections) {
            if (connection.isEligible(address, null) && connection.isMultiplexed() && connection != streamAllocation.connection()) {
                return streamAllocation.releaseAndAcquire(connection);
            }
        }
        return null;
    }

    void put(RealConnection connection) {
        if (!$assertionsDisabled && !Thread.holdsLock(this)) {
            throw new AssertionError();
        }
        if (!this.cleanupRunning) {
            this.cleanupRunning = true;
            executor.execute(this.cleanupRunnable);
        }
        this.connections.add(connection);
    }

    boolean connectionBecameIdle(RealConnection connection) {
        if (!$assertionsDisabled && !Thread.holdsLock(this)) {
            throw new AssertionError();
        }
        if (connection.noNewStreams || this.maxIdleConnections == 0) {
            this.connections.remove(connection);
            return true;
        }
        notifyAll();
        return false;
    }

    public void evictAll() throws IOException {
        List<RealConnection> evictedConnections = new ArrayList<>();
        synchronized (this) {
            Iterator<RealConnection> i = this.connections.iterator();
            while (i.hasNext()) {
                RealConnection connection = i.next();
                if (connection.allocations.isEmpty()) {
                    connection.noNewStreams = true;
                    evictedConnections.add(connection);
                    i.remove();
                }
            }
        }
        Iterator<RealConnection> it = evictedConnections.iterator();
        while (it.hasNext()) {
            Util.closeQuietly(it.next().socket());
        }
    }

    long cleanup(long now) throws IOException {
        int inUseConnectionCount = 0;
        int idleConnectionCount = 0;
        RealConnection longestIdleConnection = null;
        long longestIdleDurationNs = Long.MIN_VALUE;
        synchronized (this) {
            for (RealConnection connection : this.connections) {
                if (pruneAndGetAllocationCount(connection, now) > 0) {
                    inUseConnectionCount++;
                } else {
                    idleConnectionCount++;
                    long idleDurationNs = now - connection.idleAtNanos;
                    if (idleDurationNs > longestIdleDurationNs) {
                        longestIdleDurationNs = idleDurationNs;
                        longestIdleConnection = connection;
                    }
                }
            }
            if (longestIdleDurationNs >= this.keepAliveDurationNs || idleConnectionCount > this.maxIdleConnections) {
                this.connections.remove(longestIdleConnection);
                Util.closeQuietly(longestIdleConnection.socket());
                return 0L;
            }
            if (idleConnectionCount > 0) {
                return this.keepAliveDurationNs - longestIdleDurationNs;
            }
            if (inUseConnectionCount > 0) {
                return this.keepAliveDurationNs;
            }
            this.cleanupRunning = false;
            return -1L;
        }
    }

    private int pruneAndGetAllocationCount(RealConnection connection, long now) {
        List<Reference<StreamAllocation>> references = connection.allocations;
        int i = 0;
        while (i < references.size()) {
            Reference<StreamAllocation> reference = references.get(i);
            if (reference.get() != null) {
                i++;
            } else {
                StreamAllocation.StreamAllocationReference streamAllocRef = (StreamAllocation.StreamAllocationReference) reference;
                String message = "A connection to " + connection.route().address().url() + " was leaked. Did you forget to close a response body?";
                Platform.get().logCloseableLeak(message, streamAllocRef.callStackTrace);
                references.remove(i);
                connection.noNewStreams = true;
                if (references.isEmpty()) {
                    connection.idleAtNanos = now - this.keepAliveDurationNs;
                    return 0;
                }
            }
        }
        return references.size();
    }
}
