package com.google.android.exoplayer2.source.hls;

import com.google.android.exoplayer2.upstream.DataSource;

/* loaded from: classes.dex */
public final class DefaultHlsDataSourceFactory implements HlsDataSourceFactory {
    private final DataSource.Factory dataSourceFactory;

    public DefaultHlsDataSourceFactory(DataSource.Factory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }

    @Override // com.google.android.exoplayer2.source.hls.HlsDataSourceFactory
    public DataSource createDataSource(int dataType) {
        return this.dataSourceFactory.createDataSource();
    }
}
