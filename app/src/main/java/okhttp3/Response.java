package okhttp3;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import okhttp3.Headers;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http.StatusLine;
import okio.Buffer;
import okio.BufferedSource;

/* loaded from: classes.dex */
public final class Response implements Closeable {

    @Nullable
    final ResponseBody body;
    private volatile CacheControl cacheControl;

    @Nullable
    final Response cacheResponse;
    final int code;

    @Nullable
    final Handshake handshake;
    final Headers headers;
    final String message;

    @Nullable
    final Response networkResponse;

    @Nullable
    final Response priorResponse;
    final Protocol protocol;
    final long receivedResponseAtMillis;
    final Request request;
    final long sentRequestAtMillis;

    Response(Builder builder) {
        this.request = builder.request;
        this.protocol = builder.protocol;
        this.code = builder.code;
        this.message = builder.message;
        this.handshake = builder.handshake;
        this.headers = builder.headers.build();
        this.body = builder.body;
        this.networkResponse = builder.networkResponse;
        this.cacheResponse = builder.cacheResponse;
        this.priorResponse = builder.priorResponse;
        this.sentRequestAtMillis = builder.sentRequestAtMillis;
        this.receivedResponseAtMillis = builder.receivedResponseAtMillis;
    }

    public Request request() {
        return this.request;
    }

    public Protocol protocol() {
        return this.protocol;
    }

    public int code() {
        return this.code;
    }

    public boolean isSuccessful() {
        return this.code >= 200 && this.code < 300;
    }

    public String message() {
        return this.message;
    }

    public Handshake handshake() {
        return this.handshake;
    }

    public List<String> headers(String name) {
        return this.headers.values(name);
    }

    @Nullable
    public String header(String name) {
        return header(name, null);
    }

    @Nullable
    public String header(String name, @Nullable String defaultValue) {
        String result = this.headers.get(name);
        return result != null ? result : defaultValue;
    }

    public Headers headers() {
        return this.headers;
    }

    public ResponseBody peekBody(long byteCount) throws IOException {
        Buffer result;
        BufferedSource source = this.body.source();
        source.request(byteCount);
        Buffer copy = source.buffer().clone();
        if (copy.size() > byteCount) {
            result = new Buffer();
            result.write(copy, byteCount);
            copy.clear();
        } else {
            result = copy;
        }
        return ResponseBody.create(this.body.contentType(), result.size(), result);
    }

    @Nullable
    public ResponseBody body() {
        return this.body;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    public boolean isRedirect() {
        switch (this.code) {
            case 300:
            case 301:
            case 302:
            case 303:
            case StatusLine.HTTP_TEMP_REDIRECT /* 307 */:
            case StatusLine.HTTP_PERM_REDIRECT /* 308 */:
                return true;
            case 304:
            case 305:
            case 306:
            default:
                return false;
        }
    }

    @Nullable
    public Response networkResponse() {
        return this.networkResponse;
    }

    @Nullable
    public Response cacheResponse() {
        return this.cacheResponse;
    }

    @Nullable
    public Response priorResponse() {
        return this.priorResponse;
    }

    public List<Challenge> challenges() {
        String responseField;
        if (this.code == 401) {
            responseField = "WWW-Authenticate";
        } else if (this.code == 407) {
            responseField = "Proxy-Authenticate";
        } else {
            return Collections.emptyList();
        }
        return HttpHeaders.parseChallenges(headers(), responseField);
    }

    public CacheControl cacheControl() {
        CacheControl result = this.cacheControl;
        if (result != null) {
            return result;
        }
        CacheControl result2 = CacheControl.parse(this.headers);
        this.cacheControl = result2;
        return result2;
    }

    public long sentRequestAtMillis() {
        return this.sentRequestAtMillis;
    }

    public long receivedResponseAtMillis() {
        return this.receivedResponseAtMillis;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.body.close();
    }

    public String toString() {
        return "Response{protocol=" + this.protocol + ", code=" + this.code + ", message=" + this.message + ", url=" + this.request.url() + '}';
    }

    public static class Builder {
        ResponseBody body;
        Response cacheResponse;
        int code;

        @Nullable
        Handshake handshake;
        Headers.Builder headers;
        String message;
        Response networkResponse;
        Response priorResponse;
        Protocol protocol;
        long receivedResponseAtMillis;
        Request request;
        long sentRequestAtMillis;

        public Builder() {
            this.code = -1;
            this.headers = new Headers.Builder();
        }

        Builder(Response response) {
            this.code = -1;
            this.request = response.request;
            this.protocol = response.protocol;
            this.code = response.code;
            this.message = response.message;
            this.handshake = response.handshake;
            this.headers = response.headers.newBuilder();
            this.body = response.body;
            this.networkResponse = response.networkResponse;
            this.cacheResponse = response.cacheResponse;
            this.priorResponse = response.priorResponse;
            this.sentRequestAtMillis = response.sentRequestAtMillis;
            this.receivedResponseAtMillis = response.receivedResponseAtMillis;
        }

        public Builder request(Request request) {
            this.request = request;
            return this;
        }

        public Builder protocol(Protocol protocol) {
            this.protocol = protocol;
            return this;
        }

        public Builder code(int code) {
            this.code = code;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder handshake(@Nullable Handshake handshake) {
            this.handshake = handshake;
            return this;
        }

        public Builder header(String name, String value) {
            this.headers.set(name, value);
            return this;
        }

        public Builder addHeader(String name, String value) {
            this.headers.add(name, value);
            return this;
        }

        public Builder removeHeader(String name) {
            this.headers.removeAll(name);
            return this;
        }

        public Builder headers(Headers headers) {
            this.headers = headers.newBuilder();
            return this;
        }

        public Builder body(@Nullable ResponseBody body) {
            this.body = body;
            return this;
        }

        public Builder networkResponse(@Nullable Response networkResponse) {
            if (networkResponse != null) {
                checkSupportResponse("networkResponse", networkResponse);
            }
            this.networkResponse = networkResponse;
            return this;
        }

        public Builder cacheResponse(@Nullable Response cacheResponse) {
            if (cacheResponse != null) {
                checkSupportResponse("cacheResponse", cacheResponse);
            }
            this.cacheResponse = cacheResponse;
            return this;
        }

        private void checkSupportResponse(String name, Response response) {
            if (response.body != null) {
                throw new IllegalArgumentException(name + ".body != null");
            }
            if (response.networkResponse != null) {
                throw new IllegalArgumentException(name + ".networkResponse != null");
            }
            if (response.cacheResponse != null) {
                throw new IllegalArgumentException(name + ".cacheResponse != null");
            }
            if (response.priorResponse != null) {
                throw new IllegalArgumentException(name + ".priorResponse != null");
            }
        }

        public Builder priorResponse(@Nullable Response priorResponse) {
            if (priorResponse != null) {
                checkPriorResponse(priorResponse);
            }
            this.priorResponse = priorResponse;
            return this;
        }

        private void checkPriorResponse(Response response) {
            if (response.body != null) {
                throw new IllegalArgumentException("priorResponse.body != null");
            }
        }

        public Builder sentRequestAtMillis(long sentRequestAtMillis) {
            this.sentRequestAtMillis = sentRequestAtMillis;
            return this;
        }

        public Builder receivedResponseAtMillis(long receivedResponseAtMillis) {
            this.receivedResponseAtMillis = receivedResponseAtMillis;
            return this;
        }

        public Response build() {
            if (this.request == null) {
                throw new IllegalStateException("request == null");
            }
            if (this.protocol == null) {
                throw new IllegalStateException("protocol == null");
            }
            if (this.code < 0) {
                throw new IllegalStateException("code < 0: " + this.code);
            }
            if (this.message == null) {
                throw new IllegalStateException("message == null");
            }
            return new Response(this);
        }
    }
}
