package com.google.android.exoplayer2.source.smoothstreaming;

import android.net.Uri;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.mp4.FragmentedMp4Extractor;
import com.google.android.exoplayer2.extractor.mp4.Track;
import com.google.android.exoplayer2.extractor.mp4.TrackEncryptionBox;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.chunk.Chunk;
import com.google.android.exoplayer2.source.chunk.ChunkExtractorWrapper;
import com.google.android.exoplayer2.source.chunk.ChunkHolder;
import com.google.android.exoplayer2.source.chunk.ChunkedTrackBlacklistUtil;
import com.google.android.exoplayer2.source.chunk.ContainerMediaChunk;
import com.google.android.exoplayer2.source.chunk.MediaChunk;
import com.google.android.exoplayer2.source.smoothstreaming.SsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifest;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.LoaderErrorThrower;
import java.io.IOException;
import java.util.List;

/* loaded from: classes.dex */
public class DefaultSsChunkSource implements SsChunkSource {
    private int currentManifestChunkOffset;
    private final DataSource dataSource;
    private final int elementIndex;
    private final ChunkExtractorWrapper[] extractorWrappers;
    private IOException fatalError;
    private SsManifest manifest;
    private final LoaderErrorThrower manifestLoaderErrorThrower;
    private final TrackSelection trackSelection;

    public static final class Factory implements SsChunkSource.Factory {
        private final DataSource.Factory dataSourceFactory;

        public Factory(DataSource.Factory dataSourceFactory) {
            this.dataSourceFactory = dataSourceFactory;
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.SsChunkSource.Factory
        public SsChunkSource createChunkSource(LoaderErrorThrower manifestLoaderErrorThrower, SsManifest manifest, int elementIndex, TrackSelection trackSelection, TrackEncryptionBox[] trackEncryptionBoxes) {
            DataSource dataSource = this.dataSourceFactory.createDataSource();
            return new DefaultSsChunkSource(manifestLoaderErrorThrower, manifest, elementIndex, trackSelection, dataSource, trackEncryptionBoxes);
        }
    }

    public DefaultSsChunkSource(LoaderErrorThrower manifestLoaderErrorThrower, SsManifest manifest, int elementIndex, TrackSelection trackSelection, DataSource dataSource, TrackEncryptionBox[] trackEncryptionBoxes) {
        this.manifestLoaderErrorThrower = manifestLoaderErrorThrower;
        this.manifest = manifest;
        this.elementIndex = elementIndex;
        this.trackSelection = trackSelection;
        this.dataSource = dataSource;
        SsManifest.StreamElement streamElement = manifest.streamElements[elementIndex];
        this.extractorWrappers = new ChunkExtractorWrapper[trackSelection.length()];
        for (int i = 0; i < this.extractorWrappers.length; i++) {
            int manifestTrackIndex = trackSelection.getIndexInTrackGroup(i);
            Format format = streamElement.formats[manifestTrackIndex];
            int nalUnitLengthFieldLength = streamElement.type == 2 ? 4 : 0;
            Track track = new Track(manifestTrackIndex, streamElement.type, streamElement.timescale, C.TIME_UNSET, manifest.durationUs, format, 0, trackEncryptionBoxes, nalUnitLengthFieldLength, null, null);
            FragmentedMp4Extractor extractor = new FragmentedMp4Extractor(3, null, track);
            this.extractorWrappers[i] = new ChunkExtractorWrapper(extractor, format);
        }
    }

    @Override // com.google.android.exoplayer2.source.smoothstreaming.SsChunkSource
    public void updateManifest(SsManifest newManifest) {
        SsManifest.StreamElement currentElement = this.manifest.streamElements[this.elementIndex];
        int currentElementChunkCount = currentElement.chunkCount;
        SsManifest.StreamElement newElement = newManifest.streamElements[this.elementIndex];
        if (currentElementChunkCount == 0 || newElement.chunkCount == 0) {
            this.currentManifestChunkOffset += currentElementChunkCount;
        } else {
            long currentElementEndTimeUs = currentElement.getStartTimeUs(currentElementChunkCount - 1) + currentElement.getChunkDurationUs(currentElementChunkCount - 1);
            long newElementStartTimeUs = newElement.getStartTimeUs(0);
            if (currentElementEndTimeUs <= newElementStartTimeUs) {
                this.currentManifestChunkOffset += currentElementChunkCount;
            } else {
                this.currentManifestChunkOffset += currentElement.getChunkIndex(newElementStartTimeUs);
            }
        }
        this.manifest = newManifest;
    }

    @Override // com.google.android.exoplayer2.source.chunk.ChunkSource
    public void maybeThrowError() throws IOException {
        if (this.fatalError != null) {
            throw this.fatalError;
        }
        this.manifestLoaderErrorThrower.maybeThrowError();
    }

    @Override // com.google.android.exoplayer2.source.chunk.ChunkSource
    public int getPreferredQueueSize(long playbackPositionUs, List<? extends MediaChunk> queue) {
        return (this.fatalError != null || this.trackSelection.length() < 2) ? queue.size() : this.trackSelection.evaluateQueueSize(playbackPositionUs, queue);
    }

    @Override // com.google.android.exoplayer2.source.chunk.ChunkSource
    public final void getNextChunk(MediaChunk previous, long playbackPositionUs, ChunkHolder out) {
        int chunkIndex;
        if (this.fatalError == null) {
            long bufferedDurationUs = previous != null ? previous.endTimeUs - playbackPositionUs : 0L;
            this.trackSelection.updateSelectedTrack(bufferedDurationUs);
            SsManifest.StreamElement streamElement = this.manifest.streamElements[this.elementIndex];
            if (streamElement.chunkCount == 0) {
                out.endOfStream = !this.manifest.isLive;
                return;
            }
            if (previous == null) {
                chunkIndex = streamElement.getChunkIndex(playbackPositionUs);
            } else {
                chunkIndex = previous.getNextChunkIndex() - this.currentManifestChunkOffset;
                if (chunkIndex < 0) {
                    this.fatalError = new BehindLiveWindowException();
                    return;
                }
            }
            if (chunkIndex >= streamElement.chunkCount) {
                out.endOfStream = !this.manifest.isLive;
                return;
            }
            long chunkStartTimeUs = streamElement.getStartTimeUs(chunkIndex);
            long chunkEndTimeUs = chunkStartTimeUs + streamElement.getChunkDurationUs(chunkIndex);
            int currentAbsoluteChunkIndex = chunkIndex + this.currentManifestChunkOffset;
            int trackSelectionIndex = this.trackSelection.getSelectedIndex();
            ChunkExtractorWrapper extractorWrapper = this.extractorWrappers[trackSelectionIndex];
            int manifestTrackIndex = this.trackSelection.getIndexInTrackGroup(trackSelectionIndex);
            Uri uri = streamElement.buildRequestUri(manifestTrackIndex, chunkIndex);
            out.chunk = newMediaChunk(this.trackSelection.getSelectedFormat(), this.dataSource, uri, null, currentAbsoluteChunkIndex, chunkStartTimeUs, chunkEndTimeUs, this.trackSelection.getSelectionReason(), this.trackSelection.getSelectionData(), extractorWrapper);
        }
    }

    @Override // com.google.android.exoplayer2.source.chunk.ChunkSource
    public void onChunkLoadCompleted(Chunk chunk) {
    }

    @Override // com.google.android.exoplayer2.source.chunk.ChunkSource
    public boolean onChunkLoadError(Chunk chunk, boolean cancelable, Exception e) {
        return cancelable && ChunkedTrackBlacklistUtil.maybeBlacklistTrack(this.trackSelection, this.trackSelection.indexOf(chunk.trackFormat), e);
    }

    private static MediaChunk newMediaChunk(Format format, DataSource dataSource, Uri uri, String cacheKey, int chunkIndex, long chunkStartTimeUs, long chunkEndTimeUs, int trackSelectionReason, Object trackSelectionData, ChunkExtractorWrapper extractorWrapper) {
        DataSpec dataSpec = new DataSpec(uri, 0L, -1L, cacheKey);
        return new ContainerMediaChunk(dataSource, dataSpec, format, trackSelectionReason, trackSelectionData, chunkStartTimeUs, chunkEndTimeUs, chunkIndex, 1, chunkStartTimeUs, extractorWrapper);
    }
}
