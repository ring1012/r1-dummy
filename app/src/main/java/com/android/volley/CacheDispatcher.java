package com.android.volley;

import android.os.Process;
import com.android.volley.Cache;
import java.util.concurrent.BlockingQueue;

/* loaded from: classes.dex */
public class CacheDispatcher extends Thread {
    private static final boolean DEBUG = VolleyLog.DEBUG;
    private final Cache mCache;
    private final BlockingQueue<Request<?>> mCacheQueue;
    private final ResponseDelivery mDelivery;
    private final BlockingQueue<Request<?>> mNetworkQueue;
    private volatile boolean mQuit = false;

    public CacheDispatcher(BlockingQueue<Request<?>> cacheQueue, BlockingQueue<Request<?>> networkQueue, Cache cache, ResponseDelivery delivery) {
        this.mCacheQueue = cacheQueue;
        this.mNetworkQueue = networkQueue;
        this.mCache = cache;
        this.mDelivery = delivery;
    }

    public void quit() {
        this.mQuit = true;
        interrupt();
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() throws InterruptedException, SecurityException, IllegalArgumentException {
        if (DEBUG) {
            VolleyLog.v("start new dispatcher", new Object[0]);
        }
        Process.setThreadPriority(10);
        this.mCache.initialize();
        while (true) {
            try {
                final Request<?> request = this.mCacheQueue.take();
                try {
                    request.addMarker("cache-queue-take");
                    if (request.isCanceled()) {
                        request.finish("cache-discard-canceled");
                    } else {
                        Cache.Entry entry = this.mCache.get(request.getCacheKey());
                        if (entry == null) {
                            request.addMarker("cache-miss");
                            this.mNetworkQueue.put(request);
                        } else if (entry.isExpired()) {
                            request.addMarker("cache-hit-expired");
                            request.setCacheEntry(entry);
                            this.mNetworkQueue.put(request);
                        } else {
                            request.addMarker("cache-hit");
                            Response<?> response = request.parseNetworkResponse(new NetworkResponse(entry.data, entry.responseHeaders));
                            request.addMarker("cache-hit-parsed");
                            if (!entry.refreshNeeded()) {
                                this.mDelivery.postResponse(request, response);
                            } else {
                                request.addMarker("cache-hit-refresh-needed");
                                request.setCacheEntry(entry);
                                response.intermediate = true;
                                this.mDelivery.postResponse(request, response, new Runnable() { // from class: com.android.volley.CacheDispatcher.1
                                    @Override // java.lang.Runnable
                                    public void run() throws InterruptedException {
                                        try {
                                            CacheDispatcher.this.mNetworkQueue.put(request);
                                        } catch (InterruptedException e) {
                                        }
                                    }
                                });
                            }
                        }
                    }
                } catch (Exception e) {
                    VolleyLog.e(e, "Unhandled exception %s", e.toString());
                }
            } catch (InterruptedException e2) {
                if (this.mQuit) {
                    return;
                }
            }
        }
    }
}
