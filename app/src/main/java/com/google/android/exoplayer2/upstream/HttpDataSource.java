package com.google.android.exoplayer2.upstream;

import android.text.TextUtils;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Predicate;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public interface HttpDataSource extends DataSource {
    public static final Predicate<String> REJECT_PAYWALL_TYPES = new Predicate<String>() { // from class: com.google.android.exoplayer2.upstream.HttpDataSource.1
        @Override // com.google.android.exoplayer2.util.Predicate
        public boolean evaluate(String contentType) {
            String contentType2 = Util.toLowerInvariant(contentType);
            return (TextUtils.isEmpty(contentType2) || (contentType2.contains("text") && !contentType2.contains(MimeTypes.TEXT_VTT)) || contentType2.contains("html") || contentType2.contains("xml")) ? false : true;
        }
    };

    public interface Factory extends DataSource.Factory {
        @Deprecated
        void clearAllDefaultRequestProperties();

        @Deprecated
        void clearDefaultRequestProperty(String str);

        @Override // com.google.android.exoplayer2.upstream.DataSource.Factory
        HttpDataSource createDataSource();

        RequestProperties getDefaultRequestProperties();

        @Deprecated
        void setDefaultRequestProperty(String str, String str2);
    }

    void clearAllRequestProperties();

    void clearRequestProperty(String str);

    @Override // com.google.android.exoplayer2.upstream.DataSource
    void close() throws HttpDataSourceException;

    Map<String, List<String>> getResponseHeaders();

    @Override // com.google.android.exoplayer2.upstream.DataSource
    long open(DataSpec dataSpec) throws HttpDataSourceException;

    @Override // com.google.android.exoplayer2.upstream.DataSource
    int read(byte[] bArr, int i, int i2) throws HttpDataSourceException;

    void setRequestProperty(String str, String str2);

    public static final class RequestProperties {
        private final Map<String, String> requestProperties = new HashMap();
        private Map<String, String> requestPropertiesSnapshot;

        public synchronized void set(String name, String value) {
            this.requestPropertiesSnapshot = null;
            this.requestProperties.put(name, value);
        }

        public synchronized void set(Map<String, String> properties) {
            this.requestPropertiesSnapshot = null;
            this.requestProperties.putAll(properties);
        }

        public synchronized void clearAndSet(Map<String, String> properties) {
            this.requestPropertiesSnapshot = null;
            this.requestProperties.clear();
            this.requestProperties.putAll(properties);
        }

        public synchronized void remove(String name) {
            this.requestPropertiesSnapshot = null;
            this.requestProperties.remove(name);
        }

        public synchronized void clear() {
            this.requestPropertiesSnapshot = null;
            this.requestProperties.clear();
        }

        public synchronized Map<String, String> getSnapshot() {
            if (this.requestPropertiesSnapshot == null) {
                this.requestPropertiesSnapshot = Collections.unmodifiableMap(new HashMap(this.requestProperties));
            }
            return this.requestPropertiesSnapshot;
        }
    }

    public static abstract class BaseFactory implements Factory {
        private final RequestProperties defaultRequestProperties = new RequestProperties();

        protected abstract HttpDataSource createDataSourceInternal(RequestProperties requestProperties);

        @Override // com.google.android.exoplayer2.upstream.DataSource.Factory
        public final HttpDataSource createDataSource() {
            return createDataSourceInternal(this.defaultRequestProperties);
        }

        @Override // com.google.android.exoplayer2.upstream.HttpDataSource.Factory
        public final RequestProperties getDefaultRequestProperties() {
            return this.defaultRequestProperties;
        }

        @Override // com.google.android.exoplayer2.upstream.HttpDataSource.Factory
        @Deprecated
        public final void setDefaultRequestProperty(String name, String value) {
            this.defaultRequestProperties.set(name, value);
        }

        @Override // com.google.android.exoplayer2.upstream.HttpDataSource.Factory
        @Deprecated
        public final void clearDefaultRequestProperty(String name) {
            this.defaultRequestProperties.remove(name);
        }

        @Override // com.google.android.exoplayer2.upstream.HttpDataSource.Factory
        @Deprecated
        public final void clearAllDefaultRequestProperties() {
            this.defaultRequestProperties.clear();
        }
    }

    public static class HttpDataSourceException extends IOException {
        public static final int TYPE_CLOSE = 3;
        public static final int TYPE_OPEN = 1;
        public static final int TYPE_READ = 2;
        public final DataSpec dataSpec;
        public final int type;

        @Retention(RetentionPolicy.SOURCE)
        public @interface Type {
        }

        public HttpDataSourceException(DataSpec dataSpec, int type) {
            this.dataSpec = dataSpec;
            this.type = type;
        }

        public HttpDataSourceException(String message, DataSpec dataSpec, int type) {
            super(message);
            this.dataSpec = dataSpec;
            this.type = type;
        }

        public HttpDataSourceException(IOException cause, DataSpec dataSpec, int type) {
            super(cause);
            this.dataSpec = dataSpec;
            this.type = type;
        }

        public HttpDataSourceException(String message, IOException cause, DataSpec dataSpec, int type) {
            super(message, cause);
            this.dataSpec = dataSpec;
            this.type = type;
        }
    }

    public static final class InvalidContentTypeException extends HttpDataSourceException {
        public final String contentType;

        public InvalidContentTypeException(String contentType, DataSpec dataSpec) {
            super("Invalid content type: " + contentType, dataSpec, 1);
            this.contentType = contentType;
        }
    }

    public static final class InvalidResponseCodeException extends HttpDataSourceException {
        public final Map<String, List<String>> headerFields;
        public final int responseCode;

        public InvalidResponseCodeException(int responseCode, Map<String, List<String>> headerFields, DataSpec dataSpec) {
            super("Response code: " + responseCode, dataSpec, 1);
            this.responseCode = responseCode;
            this.headerFields = headerFields;
        }
    }
}
