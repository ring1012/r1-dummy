package com.google.android.exoplayer2.source.chunk;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.Arrays;

/* loaded from: classes.dex */
public abstract class DataChunk extends Chunk {
    private static final int READ_GRANULARITY = 16384;
    private byte[] data;
    private int limit;
    private volatile boolean loadCanceled;

    protected abstract void consume(byte[] bArr, int i) throws IOException;

    public DataChunk(DataSource dataSource, DataSpec dataSpec, int type, Format trackFormat, int trackSelectionReason, Object trackSelectionData, byte[] data) {
        super(dataSource, dataSpec, type, trackFormat, trackSelectionReason, trackSelectionData, C.TIME_UNSET, C.TIME_UNSET);
        this.data = data;
    }

    public byte[] getDataHolder() {
        return this.data;
    }

    @Override // com.google.android.exoplayer2.source.chunk.Chunk
    public long bytesLoaded() {
        return this.limit;
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.Loadable
    public final void cancelLoad() {
        this.loadCanceled = true;
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.Loadable
    public final boolean isLoadCanceled() {
        return this.loadCanceled;
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.Loadable
    public final void load() throws InterruptedException, IOException {
        try {
            this.dataSource.open(this.dataSpec);
            this.limit = 0;
            int bytesRead = 0;
            while (bytesRead != -1 && !this.loadCanceled) {
                maybeExpandData();
                bytesRead = this.dataSource.read(this.data, this.limit, 16384);
                if (bytesRead != -1) {
                    this.limit += bytesRead;
                }
            }
            if (!this.loadCanceled) {
                consume(this.data, this.limit);
            }
        } finally {
            Util.closeQuietly(this.dataSource);
        }
    }

    private void maybeExpandData() {
        if (this.data == null) {
            this.data = new byte[16384];
        } else if (this.data.length < this.limit + 16384) {
            this.data = Arrays.copyOf(this.data, this.data.length + 16384);
        }
    }
}
