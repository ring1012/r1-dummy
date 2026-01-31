package com.google.android.exoplayer2.drm;

import android.annotation.TargetApi;
import android.net.Uri;
import android.text.TextUtils;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.drm.ExoMediaDrm;
import com.google.android.exoplayer2.upstream.DataSourceInputStream;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@TargetApi(18)
/* loaded from: classes.dex */
public final class HttpMediaDrmCallback implements MediaDrmCallback {
    private static final Map<String, String> PLAYREADY_KEY_REQUEST_PROPERTIES = new HashMap();
    private final HttpDataSource.Factory dataSourceFactory;
    private final String defaultUrl;
    private final Map<String, String> keyRequestProperties;

    static {
        PLAYREADY_KEY_REQUEST_PROPERTIES.put("Content-Type", "text/xml");
        PLAYREADY_KEY_REQUEST_PROPERTIES.put("SOAPAction", "http://schemas.microsoft.com/DRM/2007/03/protocols/AcquireLicense");
    }

    public HttpMediaDrmCallback(String defaultUrl, HttpDataSource.Factory dataSourceFactory) {
        this(defaultUrl, dataSourceFactory, null);
    }

    @Deprecated
    public HttpMediaDrmCallback(String defaultUrl, HttpDataSource.Factory dataSourceFactory, Map<String, String> keyRequestProperties) {
        this.dataSourceFactory = dataSourceFactory;
        this.defaultUrl = defaultUrl;
        this.keyRequestProperties = new HashMap();
        if (keyRequestProperties != null) {
            this.keyRequestProperties.putAll(keyRequestProperties);
        }
    }

    public void setKeyRequestProperty(String name, String value) {
        Assertions.checkNotNull(name);
        Assertions.checkNotNull(value);
        synchronized (this.keyRequestProperties) {
            this.keyRequestProperties.put(name, value);
        }
    }

    public void clearKeyRequestProperty(String name) {
        Assertions.checkNotNull(name);
        synchronized (this.keyRequestProperties) {
            this.keyRequestProperties.remove(name);
        }
    }

    public void clearAllKeyRequestProperties() {
        synchronized (this.keyRequestProperties) {
            this.keyRequestProperties.clear();
        }
    }

    @Override // com.google.android.exoplayer2.drm.MediaDrmCallback
    public byte[] executeProvisionRequest(UUID uuid, ExoMediaDrm.ProvisionRequest request) throws IOException {
        String url = request.getDefaultUrl() + "&signedRequest=" + new String(request.getData());
        return executePost(this.dataSourceFactory, url, new byte[0], null);
    }

    @Override // com.google.android.exoplayer2.drm.MediaDrmCallback
    public byte[] executeKeyRequest(UUID uuid, ExoMediaDrm.KeyRequest request) throws Exception {
        String url = request.getDefaultUrl();
        if (TextUtils.isEmpty(url)) {
            url = this.defaultUrl;
        }
        Map<String, String> requestProperties = new HashMap<>();
        requestProperties.put("Content-Type", "application/octet-stream");
        if (C.PLAYREADY_UUID.equals(uuid)) {
            requestProperties.putAll(PLAYREADY_KEY_REQUEST_PROPERTIES);
        }
        synchronized (this.keyRequestProperties) {
            requestProperties.putAll(this.keyRequestProperties);
        }
        return executePost(this.dataSourceFactory, url, request.getData(), requestProperties);
    }

    private static byte[] executePost(HttpDataSource.Factory dataSourceFactory, String url, byte[] data, Map<String, String> requestProperties) throws IOException {
        HttpDataSource dataSource = dataSourceFactory.createDataSource();
        if (requestProperties != null) {
            for (Map.Entry<String, String> requestProperty : requestProperties.entrySet()) {
                dataSource.setRequestProperty(requestProperty.getKey(), requestProperty.getValue());
            }
        }
        DataSpec dataSpec = new DataSpec(Uri.parse(url), data, 0L, 0L, -1L, null, 1);
        DataSourceInputStream inputStream = new DataSourceInputStream(dataSource, dataSpec);
        try {
            return Util.toByteArray(inputStream);
        } finally {
            Util.closeQuietly(inputStream);
        }
    }
}
