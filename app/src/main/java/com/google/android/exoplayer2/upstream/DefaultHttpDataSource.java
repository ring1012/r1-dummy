package com.google.android.exoplayer2.upstream;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Predicate;
import com.google.android.exoplayer2.util.Util;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.NoRouteToHostException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class DefaultHttpDataSource implements HttpDataSource {
    public static final int DEFAULT_CONNECT_TIMEOUT_MILLIS = 8000;
    public static final int DEFAULT_READ_TIMEOUT_MILLIS = 8000;
    private static final long MAX_BYTES_TO_DRAIN = 2048;
    private static final int MAX_REDIRECTS = 20;
    private static final String TAG = "DefaultHttpDataSource";
    private final boolean allowCrossProtocolRedirects;
    private long bytesRead;
    private long bytesSkipped;
    private long bytesToRead;
    private long bytesToSkip;
    private final int connectTimeoutMillis;
    private HttpURLConnection connection;
    private final Predicate<String> contentTypePredicate;
    private DataSpec dataSpec;
    private final HttpDataSource.RequestProperties defaultRequestProperties;
    private InputStream inputStream;
    private final TransferListener<? super DefaultHttpDataSource> listener;
    private boolean opened;
    private final int readTimeoutMillis;
    private final HttpDataSource.RequestProperties requestProperties;
    private final String userAgent;
    private static final Pattern CONTENT_RANGE_HEADER = Pattern.compile("^bytes (\\d+)-(\\d+)/(\\d+)$");
    private static final AtomicReference<byte[]> skipBufferReference = new AtomicReference<>();

    public DefaultHttpDataSource(String userAgent, Predicate<String> contentTypePredicate) {
        this(userAgent, contentTypePredicate, null);
    }

    public DefaultHttpDataSource(String userAgent, Predicate<String> contentTypePredicate, TransferListener<? super DefaultHttpDataSource> listener) {
        this(userAgent, contentTypePredicate, listener, 8000, 8000);
    }

    public DefaultHttpDataSource(String userAgent, Predicate<String> contentTypePredicate, TransferListener<? super DefaultHttpDataSource> listener, int connectTimeoutMillis, int readTimeoutMillis) {
        this(userAgent, contentTypePredicate, listener, connectTimeoutMillis, readTimeoutMillis, false, null);
    }

    public DefaultHttpDataSource(String userAgent, Predicate<String> contentTypePredicate, TransferListener<? super DefaultHttpDataSource> listener, int connectTimeoutMillis, int readTimeoutMillis, boolean allowCrossProtocolRedirects, HttpDataSource.RequestProperties defaultRequestProperties) {
        this.userAgent = Assertions.checkNotEmpty(userAgent);
        this.contentTypePredicate = contentTypePredicate;
        this.listener = listener;
        this.requestProperties = new HttpDataSource.RequestProperties();
        this.connectTimeoutMillis = connectTimeoutMillis;
        this.readTimeoutMillis = readTimeoutMillis;
        this.allowCrossProtocolRedirects = allowCrossProtocolRedirects;
        this.defaultRequestProperties = defaultRequestProperties;
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public Uri getUri() {
        if (this.connection == null) {
            return null;
        }
        return Uri.parse(this.connection.getURL().toString());
    }

    @Override // com.google.android.exoplayer2.upstream.HttpDataSource
    public Map<String, List<String>> getResponseHeaders() {
        if (this.connection == null) {
            return null;
        }
        return this.connection.getHeaderFields();
    }

    @Override // com.google.android.exoplayer2.upstream.HttpDataSource
    public void setRequestProperty(String name, String value) {
        Assertions.checkNotNull(name);
        Assertions.checkNotNull(value);
        this.requestProperties.set(name, value);
    }

    @Override // com.google.android.exoplayer2.upstream.HttpDataSource
    public void clearRequestProperty(String name) {
        Assertions.checkNotNull(name);
        this.requestProperties.remove(name);
    }

    @Override // com.google.android.exoplayer2.upstream.HttpDataSource
    public void clearAllRequestProperties() {
        this.requestProperties.clear();
    }

    @Override // com.google.android.exoplayer2.upstream.HttpDataSource, com.google.android.exoplayer2.upstream.DataSource
    public long open(DataSpec dataSpec) throws IOException, NumberFormatException {
        this.dataSpec = dataSpec;
        this.bytesRead = 0L;
        this.bytesSkipped = 0L;
        try {
            this.connection = makeConnection(dataSpec);
            try {
                int responseCode = this.connection.getResponseCode();
                if (responseCode < 200 || responseCode > 299) {
                    Map<String, List<String>> headers = this.connection.getHeaderFields();
                    closeConnectionQuietly();
                    HttpDataSource.InvalidResponseCodeException exception = new HttpDataSource.InvalidResponseCodeException(responseCode, headers, dataSpec);
                    if (responseCode == 416) {
                        exception.initCause(new DataSourceException(0));
                        throw exception;
                    }
                    throw exception;
                }
                String contentType = this.connection.getContentType();
                if (this.contentTypePredicate != null && !this.contentTypePredicate.evaluate(contentType)) {
                    closeConnectionQuietly();
                    throw new HttpDataSource.InvalidContentTypeException(contentType, dataSpec);
                }
                this.bytesToSkip = (responseCode != 200 || dataSpec.position == 0) ? 0L : dataSpec.position;
                if (dataSpec.isFlagSet(1) || dataSpec.length != -1) {
                    this.bytesToRead = dataSpec.length;
                } else {
                    long contentLength = getContentLength(this.connection);
                    this.bytesToRead = contentLength != -1 ? contentLength - this.bytesToSkip : -1L;
                }
                try {
                    this.inputStream = this.connection.getInputStream();
                    this.opened = true;
                    if (this.listener != null) {
                        this.listener.onTransferStart(this, dataSpec);
                    }
                    return this.bytesToRead;
                } catch (IOException e) {
                    closeConnectionQuietly();
                    throw new HttpDataSource.HttpDataSourceException(e, dataSpec, 1);
                }
            } catch (IOException e2) {
                closeConnectionQuietly();
                throw new HttpDataSource.HttpDataSourceException("Unable to connect to " + dataSpec.uri.toString(), e2, dataSpec, 1);
            }
        } catch (IOException e3) {
            throw new HttpDataSource.HttpDataSourceException("Unable to connect to " + dataSpec.uri.toString(), e3, dataSpec, 1);
        }
    }

    @Override // com.google.android.exoplayer2.upstream.HttpDataSource, com.google.android.exoplayer2.upstream.DataSource
    public int read(byte[] buffer, int offset, int readLength) throws HttpDataSource.HttpDataSourceException {
        try {
            skipInternal();
            return readInternal(buffer, offset, readLength);
        } catch (IOException e) {
            throw new HttpDataSource.HttpDataSourceException(e, this.dataSpec, 2);
        }
    }

    @Override // com.google.android.exoplayer2.upstream.HttpDataSource, com.google.android.exoplayer2.upstream.DataSource
    public void close() throws HttpDataSource.HttpDataSourceException {
        try {
            if (this.inputStream != null) {
                maybeTerminateInputStream(this.connection, bytesRemaining());
                try {
                    this.inputStream.close();
                } catch (IOException e) {
                    throw new HttpDataSource.HttpDataSourceException(e, this.dataSpec, 3);
                }
            }
        } finally {
            this.inputStream = null;
            closeConnectionQuietly();
            if (this.opened) {
                this.opened = false;
                if (this.listener != null) {
                    this.listener.onTransferEnd(this);
                }
            }
        }
    }

    protected final HttpURLConnection getConnection() {
        return this.connection;
    }

    protected final long bytesSkipped() {
        return this.bytesSkipped;
    }

    protected final long bytesRead() {
        return this.bytesRead;
    }

    protected final long bytesRemaining() {
        return this.bytesToRead == -1 ? this.bytesToRead : this.bytesToRead - this.bytesRead;
    }

    private HttpURLConnection makeConnection(DataSpec dataSpec) throws IOException {
        URL url = new URL(dataSpec.uri.toString());
        byte[] postBody = dataSpec.postBody;
        long position = dataSpec.position;
        long length = dataSpec.length;
        boolean allowGzip = dataSpec.isFlagSet(1);
        if (!this.allowCrossProtocolRedirects) {
            return makeConnection(url, postBody, position, length, allowGzip, true);
        }
        int redirectCount = 0;
        while (true) {
            int redirectCount2 = redirectCount;
            redirectCount = redirectCount2 + 1;
            if (redirectCount2 <= 20) {
                HttpURLConnection connection = makeConnection(url, postBody, position, length, allowGzip, false);
                int responseCode = connection.getResponseCode();
                if (responseCode != 300 && responseCode != 301 && responseCode != 302 && responseCode != 303) {
                    if (postBody != null) {
                        return connection;
                    }
                    if (responseCode != 307 && responseCode != 308) {
                        return connection;
                    }
                }
                postBody = null;
                String location = connection.getHeaderField("Location");
                connection.disconnect();
                url = handleRedirect(url, location);
            } else {
                throw new NoRouteToHostException("Too many redirects: " + redirectCount);
            }
        }
    }

    private HttpURLConnection makeConnection(URL url, byte[] postBody, long position, long length, boolean allowGzip, boolean followRedirects) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(this.connectTimeoutMillis);
        connection.setReadTimeout(this.readTimeoutMillis);
        if (this.defaultRequestProperties != null) {
            for (Map.Entry<String, String> property : this.defaultRequestProperties.getSnapshot().entrySet()) {
                connection.setRequestProperty(property.getKey(), property.getValue());
            }
        }
        for (Map.Entry<String, String> property2 : this.requestProperties.getSnapshot().entrySet()) {
            connection.setRequestProperty(property2.getKey(), property2.getValue());
        }
        if (position != 0 || length != -1) {
            String rangeRequest = "bytes=" + position + "-";
            if (length != -1) {
                rangeRequest = rangeRequest + ((position + length) - 1);
            }
            connection.setRequestProperty("Range", rangeRequest);
        }
        connection.setRequestProperty("User-Agent", this.userAgent);
        if (!allowGzip) {
            connection.setRequestProperty("Accept-Encoding", "identity");
        }
        connection.setInstanceFollowRedirects(followRedirects);
        connection.setDoOutput(postBody != null);
        if (postBody != null) {
            connection.setRequestMethod("POST");
            if (postBody.length == 0) {
                connection.connect();
            } else {
                connection.setFixedLengthStreamingMode(postBody.length);
                connection.connect();
                OutputStream os = connection.getOutputStream();
                os.write(postBody);
                os.close();
            }
        } else {
            connection.connect();
        }
        return connection;
    }

    private static URL handleRedirect(URL originalUrl, String location) throws IOException {
        if (location == null) {
            throw new ProtocolException("Null location redirect");
        }
        URL url = new URL(originalUrl, location);
        String protocol = url.getProtocol();
        if (!"https".equals(protocol) && !"http".equals(protocol)) {
            throw new ProtocolException("Unsupported protocol redirect: " + protocol);
        }
        return url;
    }

    private static long getContentLength(HttpURLConnection connection) throws NumberFormatException {
        long contentLength = -1;
        String contentLengthHeader = connection.getHeaderField("Content-Length");
        if (!TextUtils.isEmpty(contentLengthHeader)) {
            try {
                contentLength = Long.parseLong(contentLengthHeader);
            } catch (NumberFormatException e) {
                Log.e(TAG, "Unexpected Content-Length [" + contentLengthHeader + "]");
            }
        }
        String contentRangeHeader = connection.getHeaderField("Content-Range");
        if (!TextUtils.isEmpty(contentRangeHeader)) {
            Matcher matcher = CONTENT_RANGE_HEADER.matcher(contentRangeHeader);
            if (matcher.find()) {
                try {
                    long contentLengthFromRange = (Long.parseLong(matcher.group(2)) - Long.parseLong(matcher.group(1))) + 1;
                    if (contentLength < 0) {
                        return contentLengthFromRange;
                    }
                    if (contentLength != contentLengthFromRange) {
                        Log.w(TAG, "Inconsistent headers [" + contentLengthHeader + "] [" + contentRangeHeader + "]");
                        return Math.max(contentLength, contentLengthFromRange);
                    }
                    return contentLength;
                } catch (NumberFormatException e2) {
                    Log.e(TAG, "Unexpected Content-Range [" + contentRangeHeader + "]");
                    return contentLength;
                }
            }
            return contentLength;
        }
        return contentLength;
    }

    private void skipInternal() throws IOException {
        if (this.bytesSkipped != this.bytesToSkip) {
            byte[] skipBuffer = skipBufferReference.getAndSet(null);
            if (skipBuffer == null) {
                skipBuffer = new byte[4096];
            }
            while (this.bytesSkipped != this.bytesToSkip) {
                int readLength = (int) Math.min(this.bytesToSkip - this.bytesSkipped, skipBuffer.length);
                int read = this.inputStream.read(skipBuffer, 0, readLength);
                if (Thread.interrupted()) {
                    throw new InterruptedIOException();
                }
                if (read == -1) {
                    throw new EOFException();
                }
                this.bytesSkipped += read;
                if (this.listener != null) {
                    this.listener.onBytesTransferred(this, read);
                }
            }
            skipBufferReference.set(skipBuffer);
        }
    }

    private int readInternal(byte[] buffer, int offset, int readLength) throws IOException {
        if (readLength == 0) {
            return 0;
        }
        if (this.bytesToRead != -1) {
            long bytesRemaining = this.bytesToRead - this.bytesRead;
            if (bytesRemaining == 0) {
                return -1;
            }
            readLength = (int) Math.min(readLength, bytesRemaining);
        }
        int read = this.inputStream.read(buffer, offset, readLength);
        if (read == -1) {
            if (this.bytesToRead != -1) {
                throw new EOFException();
            }
            return -1;
        }
        this.bytesRead += read;
        if (this.listener != null) {
            this.listener.onBytesTransferred(this, read);
            return read;
        }
        return read;
    }

    private static void maybeTerminateInputStream(HttpURLConnection connection, long bytesRemaining) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        if (Util.SDK_INT == 19 || Util.SDK_INT == 20) {
            try {
                InputStream inputStream = connection.getInputStream();
                if (bytesRemaining == -1) {
                    if (inputStream.read() == -1) {
                        return;
                    }
                } else if (bytesRemaining <= MAX_BYTES_TO_DRAIN) {
                    return;
                }
                String className = inputStream.getClass().getName();
                if (className.equals("com.android.okhttp.internal.http.HttpTransport$ChunkedInputStream") || className.equals("com.android.okhttp.internal.http.HttpTransport$FixedLengthInputStream")) {
                    Class<?> superclass = inputStream.getClass().getSuperclass();
                    Method unexpectedEndOfInput = superclass.getDeclaredMethod("unexpectedEndOfInput", new Class[0]);
                    unexpectedEndOfInput.setAccessible(true);
                    unexpectedEndOfInput.invoke(inputStream, new Object[0]);
                }
            } catch (Exception e) {
            }
        }
    }

    private void closeConnectionQuietly() {
        if (this.connection != null) {
            try {
                this.connection.disconnect();
            } catch (Exception e) {
                Log.e(TAG, "Unexpected error while disconnecting", e);
            }
            this.connection = null;
        }
    }
}
