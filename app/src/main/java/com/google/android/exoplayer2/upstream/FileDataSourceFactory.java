package com.google.android.exoplayer2.upstream;

import com.google.android.exoplayer2.upstream.DataSource;

/* loaded from: classes.dex */
public final class FileDataSourceFactory implements DataSource.Factory {
    private final TransferListener<? super FileDataSource> listener;

    public FileDataSourceFactory() {
        this(null);
    }

    public FileDataSourceFactory(TransferListener<? super FileDataSource> listener) {
        this.listener = listener;
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource.Factory
    public DataSource createDataSource() {
        return new FileDataSource(this.listener);
    }
}
