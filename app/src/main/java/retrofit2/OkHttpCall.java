package retrofit2;

import java.io.IOException;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;

/* loaded from: classes.dex */
final class OkHttpCall<T> implements Call<T> {

    @Nullable
    private final Object[] args;
    private volatile boolean canceled;

    @GuardedBy("this")
    @Nullable
    private Throwable creationFailure;

    @GuardedBy("this")
    private boolean executed;

    @GuardedBy("this")
    @Nullable
    private okhttp3.Call rawCall;
    private final ServiceMethod<T, ?> serviceMethod;

    OkHttpCall(ServiceMethod<T, ?> serviceMethod, @Nullable Object[] args) {
        this.serviceMethod = serviceMethod;
        this.args = args;
    }

    @Override // retrofit2.Call
    public OkHttpCall<T> clone() {
        return new OkHttpCall<>(this.serviceMethod, this.args);
    }

    @Override // retrofit2.Call
    public synchronized Request request() {
        Request request;
        okhttp3.Call call = this.rawCall;
        if (call != null) {
            request = call.request();
        } else {
            if (this.creationFailure != null) {
                if (this.creationFailure instanceof IOException) {
                    throw new RuntimeException("Unable to create request.", this.creationFailure);
                }
                throw ((RuntimeException) this.creationFailure);
            }
            try {
                okhttp3.Call callCreateRawCall = createRawCall();
                this.rawCall = callCreateRawCall;
                request = callCreateRawCall.request();
            } catch (IOException e) {
                this.creationFailure = e;
                throw new RuntimeException("Unable to create request.", e);
            } catch (RuntimeException e2) {
                this.creationFailure = e2;
                throw e2;
            }
        }
        return request;
    }

    @Override // retrofit2.Call
    public void enqueue(final Callback<T> callback) {
        okhttp3.Call call;
        Throwable failure;
        Utils.checkNotNull(callback, "callback == null");
        synchronized (this) {
            if (this.executed) {
                throw new IllegalStateException("Already executed.");
            }
            this.executed = true;
            call = this.rawCall;
            failure = this.creationFailure;
            if (call == null && failure == null) {
                try {
                    okhttp3.Call call2 = createRawCall();
                    this.rawCall = call2;
                    call = call2;
                } catch (Throwable t) {
                    this.creationFailure = t;
                    failure = t;
                }
            }
        }
        if (failure != null) {
            callback.onFailure(this, failure);
            return;
        }
        if (this.canceled) {
            call.cancel();
        }
        call.enqueue(new okhttp3.Callback() { // from class: retrofit2.OkHttpCall.1
            @Override // okhttp3.Callback
            public void onResponse(okhttp3.Call call3, okhttp3.Response rawResponse) throws IOException {
                try {
                    Response<T> response = OkHttpCall.this.parseResponse(rawResponse);
                    callSuccess(response);
                } catch (Throwable e) {
                    callFailure(e);
                }
            }

            @Override // okhttp3.Callback
            public void onFailure(okhttp3.Call call3, IOException e) {
                try {
                    callback.onFailure(OkHttpCall.this, e);
                } catch (Throwable t2) {
                    t2.printStackTrace();
                }
            }

            private void callFailure(Throwable e) {
                try {
                    callback.onFailure(OkHttpCall.this, e);
                } catch (Throwable t2) {
                    t2.printStackTrace();
                }
            }

            private void callSuccess(Response<T> response) {
                try {
                    callback.onResponse(OkHttpCall.this, response);
                } catch (Throwable t2) {
                    t2.printStackTrace();
                }
            }
        });
    }

    @Override // retrofit2.Call
    public synchronized boolean isExecuted() {
        return this.executed;
    }

    @Override // retrofit2.Call
    public Response<T> execute() throws IOException {
        okhttp3.Call call;
        Exception e;
        synchronized (this) {
            if (this.executed) {
                throw new IllegalStateException("Already executed.");
            }
            this.executed = true;
            if (this.creationFailure != null) {
                if (this.creationFailure instanceof IOException) {
                    throw ((IOException) this.creationFailure);
                }
                throw ((RuntimeException) this.creationFailure);
            }
            call = this.rawCall;
            if (call == null) {
                try {
                    call = createRawCall();
                    this.rawCall = call;
                } catch (IOException e2) {
                    e = e2;
                    this.creationFailure = e;
                    throw e;
                } catch (RuntimeException e3) {
                    e = e3;
                    this.creationFailure = e;
                    throw e;
                }
            }
        }
        if (this.canceled) {
            call.cancel();
        }
        return parseResponse(call.execute());
    }

    private okhttp3.Call createRawCall() throws IOException {
        Request request = this.serviceMethod.toRequest(this.args);
        okhttp3.Call call = this.serviceMethod.callFactory.newCall(request);
        if (call == null) {
            throw new NullPointerException("Call.Factory returned null.");
        }
        return call;
    }

    Response<T> parseResponse(okhttp3.Response rawResponse) throws IOException {
        ResponseBody rawBody = rawResponse.body();
        okhttp3.Response rawResponse2 = rawResponse.newBuilder().body(new NoContentResponseBody(rawBody.contentType(), rawBody.contentLength())).build();
        int code = rawResponse2.code();
        if (code < 200 || code >= 300) {
            try {
                ResponseBody bufferedBody = Utils.buffer(rawBody);
                return Response.error(bufferedBody, rawResponse2);
            } finally {
                rawBody.close();
            }
        }
        if (code == 204 || code == 205) {
            rawBody.close();
            return Response.success((Object) null, rawResponse2);
        }
        ExceptionCatchingRequestBody catchingBody = new ExceptionCatchingRequestBody(rawBody);
        try {
            T body = this.serviceMethod.toResponse(catchingBody);
            return Response.success(body, rawResponse2);
        } catch (RuntimeException e) {
            catchingBody.throwIfCaught();
            throw e;
        }
    }

    @Override // retrofit2.Call
    public void cancel() {
        okhttp3.Call call;
        this.canceled = true;
        synchronized (this) {
            call = this.rawCall;
        }
        if (call != null) {
            call.cancel();
        }
    }

    @Override // retrofit2.Call
    public boolean isCanceled() {
        if (!this.canceled) {
            synchronized (this) {
                z = this.rawCall != null && this.rawCall.isCanceled();
            }
        }
        return z;
    }

    static final class NoContentResponseBody extends ResponseBody {
        private final long contentLength;
        private final MediaType contentType;

        NoContentResponseBody(MediaType contentType, long contentLength) {
            this.contentType = contentType;
            this.contentLength = contentLength;
        }

        @Override // okhttp3.ResponseBody
        public MediaType contentType() {
            return this.contentType;
        }

        @Override // okhttp3.ResponseBody
        public long contentLength() {
            return this.contentLength;
        }

        @Override // okhttp3.ResponseBody
        public BufferedSource source() {
            throw new IllegalStateException("Cannot read raw response body of a converted body.");
        }
    }

    static final class ExceptionCatchingRequestBody extends ResponseBody {
        private final ResponseBody delegate;
        IOException thrownException;

        ExceptionCatchingRequestBody(ResponseBody delegate) {
            this.delegate = delegate;
        }

        @Override // okhttp3.ResponseBody
        public MediaType contentType() {
            return this.delegate.contentType();
        }

        @Override // okhttp3.ResponseBody
        public long contentLength() {
            return this.delegate.contentLength();
        }

        @Override // okhttp3.ResponseBody
        public BufferedSource source() {
            return Okio.buffer(new ForwardingSource(this.delegate.source()) { // from class: retrofit2.OkHttpCall.ExceptionCatchingRequestBody.1
                @Override // okio.ForwardingSource, okio.Source
                public long read(Buffer sink, long byteCount) throws IOException {
                    try {
                        return super.read(sink, byteCount);
                    } catch (IOException e) {
                        ExceptionCatchingRequestBody.this.thrownException = e;
                        throw e;
                    }
                }
            });
        }

        @Override // okhttp3.ResponseBody, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            this.delegate.close();
        }

        void throwIfCaught() throws IOException {
            if (this.thrownException != null) {
                throw this.thrownException;
            }
        }
    }
}
