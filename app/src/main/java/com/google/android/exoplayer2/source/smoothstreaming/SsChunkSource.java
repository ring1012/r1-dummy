package com.google.android.exoplayer2.source.smoothstreaming;

import com.google.android.exoplayer2.extractor.mp4.TrackEncryptionBox;
import com.google.android.exoplayer2.source.chunk.ChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifest;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.LoaderErrorThrower;

/* loaded from: classes.dex */
public interface SsChunkSource extends ChunkSource {

    public interface Factory {
        SsChunkSource createChunkSource(LoaderErrorThrower loaderErrorThrower, SsManifest ssManifest, int i, TrackSelection trackSelection, TrackEncryptionBox[] trackEncryptionBoxArr);
    }

    void updateManifest(SsManifest ssManifest);
}
