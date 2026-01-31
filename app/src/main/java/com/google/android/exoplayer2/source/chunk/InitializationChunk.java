package com.google.android.exoplayer2.source.chunk;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.DefaultExtractorInput;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;

/* loaded from: classes.dex */
public final class InitializationChunk extends Chunk {
    private volatile int bytesLoaded;
    private final ChunkExtractorWrapper extractorWrapper;
    private volatile boolean loadCanceled;

    public InitializationChunk(DataSource dataSource, DataSpec dataSpec, Format trackFormat, int trackSelectionReason, Object trackSelectionData, ChunkExtractorWrapper extractorWrapper) {
        super(dataSource, dataSpec, 2, trackFormat, trackSelectionReason, trackSelectionData, C.TIME_UNSET, C.TIME_UNSET);
        this.extractorWrapper = extractorWrapper;
    }

    @Override // com.google.android.exoplayer2.source.chunk.Chunk
    public long bytesLoaded() {
        return this.bytesLoaded;
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.Loadable
    public void cancelLoad() {
        this.loadCanceled = true;
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.Loadable
    public boolean isLoadCanceled() {
        return this.loadCanceled;
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.Loadable
    public void load() throws InterruptedException, IOException {
        DataSpec loadDataSpec = Util.getRemainderDataSpec(this.dataSpec, this.bytesLoaded);
        try {
            ExtractorInput input = new DefaultExtractorInput(this.dataSource, loadDataSpec.absoluteStreamPosition, this.dataSource.open(loadDataSpec));
            if (this.bytesLoaded == 0) {
                this.extractorWrapper.init(null);
            }
            try {
                Extractor extractor = this.extractorWrapper.extractor;
                int result = 0;
                while (result == 0 && !this.loadCanceled) {
                    result = extractor.read(input, null);
                }
                Assertions.checkState(result != 1);
            } finally {
                this.bytesLoaded = (int) (input.getPosition() - this.dataSpec.absoluteStreamPosition);
            }
        } finally {
            Util.closeQuietly(this.dataSource);
        }
    }
}
