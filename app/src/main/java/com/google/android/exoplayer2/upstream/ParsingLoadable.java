package com.google.android.exoplayer2.upstream;

import android.net.Uri;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public final class ParsingLoadable<T> implements Loader.Loadable {
    private volatile long bytesLoaded;
    private final DataSource dataSource;
    public final DataSpec dataSpec;
    private volatile boolean isCanceled;
    private final Parser<? extends T> parser;
    private volatile T result;
    public final int type;

    public interface Parser<T> {
        T parse(Uri uri, InputStream inputStream) throws IOException;
    }

    public ParsingLoadable(DataSource dataSource, Uri uri, int type, Parser<? extends T> parser) {
        this.dataSource = dataSource;
        this.dataSpec = new DataSpec(uri, 1);
        this.type = type;
        this.parser = parser;
    }

    public final T getResult() {
        return this.result;
    }

    public long bytesLoaded() {
        return this.bytesLoaded;
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.Loadable
    public final void cancelLoad() {
        this.isCanceled = true;
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.Loadable
    public final boolean isLoadCanceled() {
        return this.isCanceled;
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.Loadable
    public final void load() throws InterruptedException, IOException {
        DataSourceInputStream inputStream = new DataSourceInputStream(this.dataSource, this.dataSpec);
        try {
            inputStream.open();
            this.result = this.parser.parse(this.dataSource.getUri(), inputStream);
        } finally {
            this.bytesLoaded = inputStream.bytesRead();
            Util.closeQuietly(inputStream);
        }
    }
}
