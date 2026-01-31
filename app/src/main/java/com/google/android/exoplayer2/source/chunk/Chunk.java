package com.google.android.exoplayer2.source.chunk;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.util.Assertions;

/* loaded from: classes.dex */
public abstract class Chunk implements Loader.Loadable {
    protected final DataSource dataSource;
    public final DataSpec dataSpec;
    public final long endTimeUs;
    public final long startTimeUs;
    public final Format trackFormat;
    public final Object trackSelectionData;
    public final int trackSelectionReason;
    public final int type;

    public abstract long bytesLoaded();

    public Chunk(DataSource dataSource, DataSpec dataSpec, int type, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long startTimeUs, long endTimeUs) {
        this.dataSource = (DataSource) Assertions.checkNotNull(dataSource);
        this.dataSpec = (DataSpec) Assertions.checkNotNull(dataSpec);
        this.type = type;
        this.trackFormat = trackFormat;
        this.trackSelectionReason = trackSelectionReason;
        this.trackSelectionData = trackSelectionData;
        this.startTimeUs = startTimeUs;
        this.endTimeUs = endTimeUs;
    }

    public final long getDurationUs() {
        return this.endTimeUs - this.startTimeUs;
    }
}
