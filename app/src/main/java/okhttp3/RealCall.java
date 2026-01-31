package okhttp3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.EventListener;
import okhttp3.Interceptor;
import okhttp3.internal.NamedRunnable;
import okhttp3.internal.cache.CacheInterceptor;
import okhttp3.internal.connection.ConnectInterceptor;
import okhttp3.internal.connection.StreamAllocation;
import okhttp3.internal.http.BridgeInterceptor;
import okhttp3.internal.http.CallServerInterceptor;
import okhttp3.internal.http.RealInterceptorChain;
import okhttp3.internal.http.RetryAndFollowUpInterceptor;
import okhttp3.internal.platform.Platform;

/* loaded from: classes.dex */
final class RealCall implements Call {
    final OkHttpClient client;
    final EventListener eventListener;
    private boolean executed;
    final boolean forWebSocket;
    final Request originalRequest;
    final RetryAndFollowUpInterceptor retryAndFollowUpInterceptor;

    RealCall(OkHttpClient client, Request originalRequest, boolean forWebSocket) {
        EventListener.Factory eventListenerFactory = client.eventListenerFactory();
        this.client = client;
        this.originalRequest = originalRequest;
        this.forWebSocket = forWebSocket;
        this.retryAndFollowUpInterceptor = new RetryAndFollowUpInterceptor(client, forWebSocket);
        this.eventListener = eventListenerFactory.create(this);
    }

    @Override // okhttp3.Call
    public Request request() {
        return this.originalRequest;
    }

    @Override // okhttp3.Call
    public Response execute() throws IOException {
        synchronized (this) {
            if (this.executed) {
                throw new IllegalStateException("Already Executed");
            }
            this.executed = true;
        }
        captureCallStackTrace();
        try {
            this.client.dispatcher().executed(this);
            Response result = getResponseWithInterceptorChain();
            if (result == null) {
                throw new IOException("Canceled");
            }
            return result;
        } finally {
            this.client.dispatcher().finished(this);
        }
    }

    private void captureCallStackTrace() {
        Object callStackTrace = Platform.get().getStackTraceForCloseable("response.body().close()");
        this.retryAndFollowUpInterceptor.setCallStackTrace(callStackTrace);
    }

    @Override // okhttp3.Call
    public void enqueue(Callback responseCallback) {
        synchronized (this) {
            if (this.executed) {
                throw new IllegalStateException("Already Executed");
            }
            this.executed = true;
        }
        captureCallStackTrace();
        this.client.dispatcher().enqueue(new AsyncCall(responseCallback));
    }

    @Override // okhttp3.Call
    public void cancel() throws IOException {
        this.retryAndFollowUpInterceptor.cancel();
    }

    @Override // okhttp3.Call
    public synchronized boolean isExecuted() {
        return this.executed;
    }

    @Override // okhttp3.Call
    public boolean isCanceled() {
        return this.retryAndFollowUpInterceptor.isCanceled();
    }

    @Override // okhttp3.Call
    public RealCall clone() {
        return new RealCall(this.client, this.originalRequest, this.forWebSocket);
    }

    StreamAllocation streamAllocation() {
        return this.retryAndFollowUpInterceptor.streamAllocation();
    }

    final class AsyncCall extends NamedRunnable {
        private final Callback responseCallback;

        AsyncCall(Callback responseCallback) {
            super("OkHttp %s", RealCall.this.redactedUrl());
            this.responseCallback = responseCallback;
        }

        String host() {
            return RealCall.this.originalRequest.url().host();
        }

        Request request() {
            return RealCall.this.originalRequest;
        }

        RealCall get() {
            return RealCall.this;
        }

        @Override // okhttp3.internal.NamedRunnable
        protected void execute() {
            try {
                Response response = RealCall.this.getResponseWithInterceptorChain();
                if (RealCall.this.retryAndFollowUpInterceptor.isCanceled()) {
                    this.responseCallback.onFailure(RealCall.this, new IOException("Canceled"));
                } else {
                    this.responseCallback.onResponse(RealCall.this, response);
                }
            } catch (IOException e) {
                if (0 != 0) {
                    Platform.get().log(4, "Callback failure for " + RealCall.this.toLoggableString(), e);
                } else {
                    this.responseCallback.onFailure(RealCall.this, e);
                }
            } finally {
                RealCall.this.client.dispatcher().finished(this);
            }
        }
    }

    String toLoggableString() {
        return (isCanceled() ? "canceled " : "") + (this.forWebSocket ? "web socket" : "call") + " to " + redactedUrl();
    }

    String redactedUrl() {
        return this.originalRequest.url().redact();
    }

    Response getResponseWithInterceptorChain() throws IOException {
        List<Interceptor> interceptors = new ArrayList<>();
        interceptors.addAll(this.client.interceptors());
        interceptors.add(this.retryAndFollowUpInterceptor);
        interceptors.add(new BridgeInterceptor(this.client.cookieJar()));
        interceptors.add(new CacheInterceptor(this.client.internalCache()));
        interceptors.add(new ConnectInterceptor(this.client));
        if (!this.forWebSocket) {
            interceptors.addAll(this.client.networkInterceptors());
        }
        interceptors.add(new CallServerInterceptor(this.forWebSocket));
        Interceptor.Chain chain = new RealInterceptorChain(interceptors, null, null, null, 0, this.originalRequest);
        return chain.proceed(this.originalRequest);
    }
}
