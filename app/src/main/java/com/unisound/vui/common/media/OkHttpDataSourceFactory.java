package com.unisound.vui.common.media;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.TransferListener;
import okhttp3.CacheControl;
import okhttp3.Call;

/* loaded from: classes.dex */
public final class OkHttpDataSourceFactory extends HttpDataSource.BaseFactory {
    private final CacheControl cacheControl;
    private final Call.Factory callFactory;
    private final TransferListener<? super DataSource> listener;
    private final String userAgent;

    public OkHttpDataSourceFactory(Call.Factory callFactory, String userAgent, TransferListener<? super DataSource> listener) {
        this(callFactory, userAgent, listener, null);
    }

    public OkHttpDataSourceFactory(Call.Factory callFactory, String userAgent, TransferListener<? super DataSource> listener, CacheControl cacheControl) {
        this.callFactory = callFactory;
        this.userAgent = userAgent;
        this.listener = listener;
        this.cacheControl = cacheControl;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.upstream.HttpDataSource.BaseFactory
    public OkHttpDataSource createDataSourceInternal(HttpDataSource.RequestProperties defaultRequestProperties) {
        return new OkHttpDataSource(this.callFactory, this.userAgent, null, this.listener, this.cacheControl, defaultRequestProperties);
    }
}
