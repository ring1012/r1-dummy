package com.google.android.exoplayer2.upstream;

import android.content.Context;
import com.google.android.exoplayer2.upstream.DataSource;

/* loaded from: classes.dex */
public final class DefaultDataSourceFactory implements DataSource.Factory {
    private final DataSource.Factory baseDataSourceFactory;
    private final Context context;
    private final TransferListener<? super DataSource> listener;

    public DefaultDataSourceFactory(Context context, String userAgent) {
        this(context, userAgent, (TransferListener<? super DataSource>) null);
    }

    public DefaultDataSourceFactory(Context context, String userAgent, TransferListener<? super DataSource> listener) {
        this(context, listener, new DefaultHttpDataSourceFactory(userAgent, listener));
    }

    public DefaultDataSourceFactory(Context context, TransferListener<? super DataSource> listener, DataSource.Factory baseDataSourceFactory) {
        this.context = context.getApplicationContext();
        this.listener = listener;
        this.baseDataSourceFactory = baseDataSourceFactory;
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource.Factory
    public DefaultDataSource createDataSource() {
        return new DefaultDataSource(this.context, this.listener, this.baseDataSourceFactory.createDataSource());
    }
}
