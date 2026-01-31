package com.android.volley.toolbox;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.HttpClientStack;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;

/* loaded from: classes.dex */
public class HurlStack implements HttpStack {
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private final SSLSocketFactory mSslSocketFactory;
    private final UrlRewriter mUrlRewriter;

    public interface UrlRewriter {
        String rewriteUrl(String str);
    }

    public HurlStack() {
        this(null);
    }

    public HurlStack(UrlRewriter urlRewriter) {
        this(urlRewriter, null);
    }

    public HurlStack(UrlRewriter urlRewriter, SSLSocketFactory sslSocketFactory) {
        this.mUrlRewriter = urlRewriter;
        this.mSslSocketFactory = sslSocketFactory;
    }

    @Override // com.android.volley.toolbox.HttpStack
    public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError {
        String url = request.getUrl();
        HashMap<String, String> map = new HashMap<>();
        map.putAll(request.getHeaders());
        map.putAll(additionalHeaders);
        if (this.mUrlRewriter != null) {
            String rewritten = this.mUrlRewriter.rewriteUrl(url);
            if (rewritten == null) {
                throw new IOException("URL blocked by rewriter: " + url);
            }
            url = rewritten;
        }
        URL parsedUrl = new URL(url);
        HttpURLConnection connection = openConnection(parsedUrl, request);
        for (String headerName : map.keySet()) {
            connection.addRequestProperty(headerName, map.get(headerName));
        }
        setConnectionParametersForRequest(connection, request);
        ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
        int responseCode = connection.getResponseCode();
        if (responseCode == -1) {
            throw new IOException("Could not retrieve response code from HttpUrlConnection.");
        }
        StatusLine responseStatus = new BasicStatusLine(protocolVersion, connection.getResponseCode(), connection.getResponseMessage());
        BasicHttpResponse response = new BasicHttpResponse(responseStatus);
        if (hasResponseBody(request.getMethod(), responseStatus.getStatusCode())) {
            response.setEntity(entityFromConnection(connection));
        }
        for (Map.Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
            if (header.getKey() != null) {
                Header h = new BasicHeader(header.getKey(), header.getValue().get(0));
                response.addHeader(h);
            }
        }
        return response;
    }

    private static boolean hasResponseBody(int requestMethod, int responseCode) {
        return (requestMethod == 4 || (100 <= responseCode && responseCode < 200) || responseCode == 204 || responseCode == 304) ? false : true;
    }

    private static HttpEntity entityFromConnection(HttpURLConnection connection) {
        InputStream inputStream;
        BasicHttpEntity entity = new BasicHttpEntity();
        try {
            inputStream = connection.getInputStream();
        } catch (IOException e) {
            inputStream = connection.getErrorStream();
        }
        entity.setContent(inputStream);
        entity.setContentLength(connection.getContentLength());
        entity.setContentEncoding(connection.getContentEncoding());
        entity.setContentType(connection.getContentType());
        return entity;
    }

    protected HttpURLConnection createConnection(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }

    private HttpURLConnection openConnection(URL url, Request<?> request) throws IOException {
        HttpURLConnection connection = createConnection(url);
        int timeoutMs = request.getTimeoutMs();
        connection.setConnectTimeout(timeoutMs);
        connection.setReadTimeout(timeoutMs);
        connection.setUseCaches(false);
        connection.setDoInput(true);
        if ("https".equals(url.getProtocol()) && this.mSslSocketFactory != null) {
            ((HttpsURLConnection) connection).setSSLSocketFactory(this.mSslSocketFactory);
        }
        return connection;
    }

    static void setConnectionParametersForRequest(HttpURLConnection connection, Request<?> request) throws IOException, AuthFailureError {
        switch (request.getMethod()) {
            case -1:
                byte[] postBody = request.getPostBody();
                if (postBody != null) {
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    connection.addRequestProperty(HEADER_CONTENT_TYPE, request.getPostBodyContentType());
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    out.write(postBody);
                    out.close();
                    return;
                }
                return;
            case 0:
                connection.setRequestMethod("GET");
                return;
            case 1:
                connection.setRequestMethod("POST");
                addBodyIfExists(connection, request);
                return;
            case 2:
                connection.setRequestMethod("PUT");
                addBodyIfExists(connection, request);
                return;
            case 3:
                connection.setRequestMethod("DELETE");
                return;
            case 4:
                connection.setRequestMethod("HEAD");
                return;
            case 5:
                connection.setRequestMethod("OPTIONS");
                return;
            case 6:
                connection.setRequestMethod("TRACE");
                return;
            case 7:
                connection.setRequestMethod(HttpClientStack.HttpPatch.METHOD_NAME);
                addBodyIfExists(connection, request);
                return;
            default:
                throw new IllegalStateException("Unknown method type.");
        }
    }

    private static void addBodyIfExists(HttpURLConnection connection, Request<?> request) throws IOException, AuthFailureError {
        byte[] body = request.getBody();
        if (body != null) {
            connection.setDoOutput(true);
            connection.addRequestProperty(HEADER_CONTENT_TYPE, request.getBodyContentType());
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(body);
            out.close();
        }
    }
}
