package okhttp3.internal.http;

import cn.yunzhisheng.asr.JniUscClient;
import java.io.IOException;
import java.net.ProtocolException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Util;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.StreamAllocation;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;

/* loaded from: classes.dex */
public final class CallServerInterceptor implements Interceptor {
    private final boolean forWebSocket;

    public CallServerInterceptor(boolean forWebSocket) {
        this.forWebSocket = forWebSocket;
    }

    @Override // okhttp3.Interceptor
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Response response;
        RealInterceptorChain realChain = (RealInterceptorChain) chain;
        HttpCodec httpCodec = realChain.httpStream();
        StreamAllocation streamAllocation = realChain.streamAllocation();
        RealConnection connection = (RealConnection) realChain.connection();
        Request request = realChain.request();
        long sentRequestMillis = System.currentTimeMillis();
        httpCodec.writeRequestHeaders(request);
        Response.Builder responseBuilder = null;
        if (HttpMethod.permitsRequestBody(request.method()) && request.body() != null) {
            if ("100-continue".equalsIgnoreCase(request.header("Expect"))) {
                httpCodec.flushRequest();
                responseBuilder = httpCodec.readResponseHeaders(true);
            }
            if (responseBuilder == null) {
                Sink requestBodyOut = httpCodec.createRequestBody(request, request.body().contentLength());
                BufferedSink bufferedRequestBody = Okio.buffer(requestBodyOut);
                request.body().writeTo(bufferedRequestBody);
                bufferedRequestBody.close();
            } else if (!connection.isMultiplexed()) {
                streamAllocation.noNewStreams();
            }
        }
        httpCodec.finishRequest();
        if (responseBuilder == null) {
            responseBuilder = httpCodec.readResponseHeaders(false);
        }
        Response response2 = responseBuilder.request(request).handshake(streamAllocation.connection().handshake()).sentRequestAtMillis(sentRequestMillis).receivedResponseAtMillis(System.currentTimeMillis()).build();
        int code = response2.code();
        if (this.forWebSocket && code == 101) {
            response = response2.newBuilder().body(Util.EMPTY_RESPONSE).build();
        } else {
            response = response2.newBuilder().body(httpCodec.openResponseBody(response2)).build();
        }
        if (JniUscClient.s.equalsIgnoreCase(response.request().header("Connection")) || JniUscClient.s.equalsIgnoreCase(response.header("Connection"))) {
            streamAllocation.noNewStreams();
        }
        if ((code == 204 || code == 205) && response.body().contentLength() > 0) {
            throw new ProtocolException("HTTP " + code + " had non-zero Content-Length: " + response.body().contentLength());
        }
        return response;
    }
}
