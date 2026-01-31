package okhttp3.internal.http;

import java.io.IOException;
import java.util.List;
import okhttp3.Connection;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.StreamAllocation;

/* loaded from: classes.dex */
public final class RealInterceptorChain implements Interceptor.Chain {
    private int calls;
    private final RealConnection connection;
    private final HttpCodec httpCodec;
    private final int index;
    private final List<Interceptor> interceptors;
    private final Request request;
    private final StreamAllocation streamAllocation;

    public RealInterceptorChain(List<Interceptor> interceptors, StreamAllocation streamAllocation, HttpCodec httpCodec, RealConnection connection, int index, Request request) {
        this.interceptors = interceptors;
        this.connection = connection;
        this.streamAllocation = streamAllocation;
        this.httpCodec = httpCodec;
        this.index = index;
        this.request = request;
    }

    @Override // okhttp3.Interceptor.Chain
    public Connection connection() {
        return this.connection;
    }

    public StreamAllocation streamAllocation() {
        return this.streamAllocation;
    }

    public HttpCodec httpStream() {
        return this.httpCodec;
    }

    @Override // okhttp3.Interceptor.Chain
    public Request request() {
        return this.request;
    }

    @Override // okhttp3.Interceptor.Chain
    public Response proceed(Request request) throws IOException {
        return proceed(request, this.streamAllocation, this.httpCodec, this.connection);
    }

    public Response proceed(Request request, StreamAllocation streamAllocation, HttpCodec httpCodec, RealConnection connection) throws IOException {
        if (this.index >= this.interceptors.size()) {
            throw new AssertionError();
        }
        this.calls++;
        if (this.httpCodec != null && !this.connection.supportsUrl(request.url())) {
            throw new IllegalStateException("network interceptor " + this.interceptors.get(this.index - 1) + " must retain the same host and port");
        }
        if (this.httpCodec != null && this.calls > 1) {
            throw new IllegalStateException("network interceptor " + this.interceptors.get(this.index - 1) + " must call proceed() exactly once");
        }
        RealInterceptorChain next = new RealInterceptorChain(this.interceptors, streamAllocation, httpCodec, connection, this.index + 1, request);
        Interceptor interceptor = this.interceptors.get(this.index);
        Response response = interceptor.intercept(next);
        if (httpCodec != null && this.index + 1 < this.interceptors.size() && next.calls != 1) {
            throw new IllegalStateException("network interceptor " + interceptor + " must call proceed() exactly once");
        }
        if (response == null) {
            throw new NullPointerException("interceptor " + interceptor + " returned null");
        }
        return response;
    }
}
