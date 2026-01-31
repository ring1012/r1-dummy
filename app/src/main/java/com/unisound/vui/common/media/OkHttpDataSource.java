package com.unisound.vui.common.media;

import android.net.Uri;
import com.google.android.exoplayer2.upstream.DataSourceException;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Predicate;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/* loaded from: classes.dex */
public class OkHttpDataSource implements HttpDataSource {
    private static final AtomicReference<byte[]> skipBufferReference = new AtomicReference<>();
    private long bytesRead;
    private long bytesSkipped;
    private long bytesToRead;
    private long bytesToSkip;
    private final CacheControl cacheControl;
    private final Call.Factory callFactory;
    private final Predicate<String> contentTypePredicate;
    private DataSpec dataSpec;
    private final HttpDataSource.RequestProperties defaultRequestProperties;
    private final TransferListener<? super OkHttpDataSource> listener;
    private boolean opened;
    private final HttpDataSource.RequestProperties requestProperties;
    private Response response;
    private InputStream responseByteStream;
    private final String userAgent;

    public OkHttpDataSource(Call.Factory callFactory, String userAgent, Predicate<String> contentTypePredicate) {
        this(callFactory, userAgent, contentTypePredicate, null);
    }

    public OkHttpDataSource(Call.Factory callFactory, String userAgent, Predicate<String> contentTypePredicate, TransferListener<? super OkHttpDataSource> listener) {
        this(callFactory, userAgent, contentTypePredicate, listener, null, null);
    }

    public OkHttpDataSource(Call.Factory callFactory, String userAgent, Predicate<String> contentTypePredicate, TransferListener<? super OkHttpDataSource> listener, CacheControl cacheControl, HttpDataSource.RequestProperties defaultRequestProperties) {
        this.callFactory = (Call.Factory) Assertions.checkNotNull(callFactory);
        this.userAgent = Assertions.checkNotEmpty(userAgent);
        this.contentTypePredicate = contentTypePredicate;
        this.listener = listener;
        this.cacheControl = cacheControl;
        this.defaultRequestProperties = defaultRequestProperties;
        this.requestProperties = new HttpDataSource.RequestProperties();
    }

    private void closeConnectionQuietly() {
        this.response.body().close();
        this.response = null;
        this.responseByteStream = null;
    }

    private Request makeRequest(DataSpec dataSpec) {
        long j = dataSpec.position;
        long j2 = dataSpec.length;
        boolean zIsFlagSet = dataSpec.isFlagSet(1);
        Request.Builder builderUrl = new Request.Builder().url(HttpUrl.parse(dataSpec.uri.toString()));
        if (this.cacheControl != null) {
            builderUrl.cacheControl(this.cacheControl);
        }
        if (this.defaultRequestProperties != null) {
            for (Map.Entry<String, String> entry : this.defaultRequestProperties.getSnapshot().entrySet()) {
                builderUrl.header(entry.getKey(), entry.getValue());
            }
        }
        for (Map.Entry<String, String> entry2 : this.requestProperties.getSnapshot().entrySet()) {
            builderUrl.header(entry2.getKey(), entry2.getValue());
        }
        if (j != 0 || j2 != -1) {
            String str = "bytes=" + j + "-";
            if (j2 != -1) {
                str = str + ((j + j2) - 1);
            }
            builderUrl.addHeader("Range", str);
        }
        builderUrl.addHeader("User-Agent", this.userAgent);
        if (!zIsFlagSet) {
            builderUrl.addHeader("Accept-Encoding", "identity");
        }
        if (dataSpec.postBody != null) {
            builderUrl.post(RequestBody.create((MediaType) null, dataSpec.postBody));
        }
        return builderUrl.build();
    }

    private int readInternal(byte[] buffer, int offset, int readLength) throws IOException {
        if (readLength == 0) {
            return 0;
        }
        if (this.bytesToRead != -1) {
            long j = this.bytesToRead - this.bytesRead;
            if (j == 0) {
                return -1;
            }
            readLength = (int) Math.min(readLength, j);
        }
        int i = this.responseByteStream.read(buffer, offset, readLength);
        if (i == -1) {
            if (this.bytesToRead != -1) {
                throw new EOFException();
            }
            return -1;
        }
        this.bytesRead += i;
        if (this.listener != null) {
            this.listener.onBytesTransferred(this, i);
        }
        return i;
    }

    private void skipInternal() throws IOException {
        if (this.bytesSkipped == this.bytesToSkip) {
            return;
        }
        byte[] andSet = skipBufferReference.getAndSet(null);
        if (andSet == null) {
            andSet = new byte[4096];
        }
        while (this.bytesSkipped != this.bytesToSkip) {
            int i = this.responseByteStream.read(andSet, 0, (int) Math.min(this.bytesToSkip - this.bytesSkipped, andSet.length));
            if (Thread.interrupted()) {
                throw new InterruptedIOException();
            }
            if (i == -1) {
                throw new EOFException();
            }
            this.bytesSkipped += i;
            if (this.listener != null) {
                this.listener.onBytesTransferred(this, i);
            }
        }
        skipBufferReference.set(andSet);
    }

    protected final long bytesRead() {
        return this.bytesRead;
    }

    protected final long bytesRemaining() {
        return this.bytesToRead == -1 ? this.bytesToRead : this.bytesToRead - this.bytesRead;
    }

    protected final long bytesSkipped() {
        return this.bytesSkipped;
    }

    @Override // com.google.android.exoplayer2.upstream.HttpDataSource
    public void clearAllRequestProperties() {
        this.requestProperties.clear();
    }

    @Override // com.google.android.exoplayer2.upstream.HttpDataSource
    public void clearRequestProperty(String name) {
        Assertions.checkNotNull(name);
        this.requestProperties.remove(name);
    }

    @Override // com.google.android.exoplayer2.upstream.HttpDataSource, com.google.android.exoplayer2.upstream.DataSource
    public void close() {
        if (this.opened) {
            this.opened = false;
            if (this.listener != null) {
                this.listener.onTransferEnd(this);
            }
            closeConnectionQuietly();
        }
    }

    @Override // com.google.android.exoplayer2.upstream.HttpDataSource
    public Map<String, List<String>> getResponseHeaders() {
        if (this.response == null) {
            return null;
        }
        return this.response.headers().toMultimap();
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public Uri getUri() {
        if (this.response == null) {
            return null;
        }
        return Uri.parse(this.response.request().url().toString());
    }

    @Override // com.google.android.exoplayer2.upstream.HttpDataSource, com.google.android.exoplayer2.upstream.DataSource
    public long open(DataSpec dataSpec) throws HttpDataSource.HttpDataSourceException {
        this.dataSpec = dataSpec;
        this.bytesRead = 0L;
        this.bytesSkipped = 0L;
        Request requestMakeRequest = makeRequest(dataSpec);
        try {
            this.response = this.callFactory.newCall(requestMakeRequest).execute();
            this.responseByteStream = this.response.body().byteStream();
            int iCode = this.response.code();
            if (!this.response.isSuccessful()) {
                Map<String, List<String>> multimap = requestMakeRequest.headers().toMultimap();
                closeConnectionQuietly();
                HttpDataSource.InvalidResponseCodeException invalidResponseCodeException = new HttpDataSource.InvalidResponseCodeException(iCode, multimap, dataSpec);
                if (iCode != 416) {
                    throw invalidResponseCodeException;
                }
                invalidResponseCodeException.initCause(new DataSourceException(0));
                throw invalidResponseCodeException;
            }
            MediaType mediaTypeContentType = this.response.body().contentType();
            String string = mediaTypeContentType != null ? mediaTypeContentType.toString() : null;
            if (this.contentTypePredicate != null && !this.contentTypePredicate.evaluate(string)) {
                closeConnectionQuietly();
                throw new HttpDataSource.InvalidContentTypeException(string, dataSpec);
            }
            this.bytesToSkip = (iCode != 200 || dataSpec.position == 0) ? 0L : dataSpec.position;
            if (dataSpec.length != -1) {
                this.bytesToRead = dataSpec.length;
            } else {
                long jContentLength = this.response.body().contentLength();
                this.bytesToRead = jContentLength != -1 ? jContentLength - this.bytesToSkip : -1L;
            }
            this.opened = true;
            if (this.listener != null) {
                this.listener.onTransferStart(this, dataSpec);
            }
            return this.bytesToRead;
        } catch (IOException e) {
            throw new HttpDataSource.HttpDataSourceException("Unable to connect to " + dataSpec.uri.toString(), e, dataSpec, 1);
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

    @Override // com.google.android.exoplayer2.upstream.HttpDataSource
    public void setRequestProperty(String name, String value) {
        Assertions.checkNotNull(name);
        Assertions.checkNotNull(value);
        this.requestProperties.set(name, value);
    }
}
