package com.google.android.exoplayer2.upstream;

import android.net.Uri;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;

/* loaded from: classes.dex */
public final class TeeDataSource implements DataSource {
    private final DataSink dataSink;
    private final DataSource upstream;

    public TeeDataSource(DataSource upstream, DataSink dataSink) {
        this.upstream = (DataSource) Assertions.checkNotNull(upstream);
        this.dataSink = (DataSink) Assertions.checkNotNull(dataSink);
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public long open(DataSpec dataSpec) throws IOException {
        long dataLength = this.upstream.open(dataSpec);
        if (dataSpec.length == -1 && dataLength != -1) {
            dataSpec = new DataSpec(dataSpec.uri, dataSpec.absoluteStreamPosition, dataSpec.position, dataLength, dataSpec.key, dataSpec.flags);
        }
        this.dataSink.open(dataSpec);
        return dataLength;
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public int read(byte[] buffer, int offset, int max) throws IOException {
        int num = this.upstream.read(buffer, offset, max);
        if (num > 0) {
            this.dataSink.write(buffer, offset, num);
        }
        return num;
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public Uri getUri() {
        return this.upstream.getUri();
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public void close() throws IOException {
        try {
            this.upstream.close();
        } finally {
            this.dataSink.close();
        }
    }
}
