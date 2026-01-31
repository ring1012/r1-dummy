package com.google.android.exoplayer2.source.dash;

import android.util.Pair;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.source.CompositeSequenceableLoader;
import com.google.android.exoplayer2.source.EmptySampleStream;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.source.SampleStream;
import com.google.android.exoplayer2.source.SequenceableLoader;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.chunk.ChunkSampleStream;
import com.google.android.exoplayer2.source.dash.DashChunkSource;
import com.google.android.exoplayer2.source.dash.manifest.AdaptationSet;
import com.google.android.exoplayer2.source.dash.manifest.DashManifest;
import com.google.android.exoplayer2.source.dash.manifest.Representation;
import com.google.android.exoplayer2.source.dash.manifest.SchemeValuePair;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.LoaderErrorThrower;
import com.google.android.exoplayer2.util.MimeTypes;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
final class DashMediaPeriod implements MediaPeriod, SequenceableLoader.Callback<ChunkSampleStream<DashChunkSource>> {
    private List<AdaptationSet> adaptationSets;
    private final Allocator allocator;
    private MediaPeriod.Callback callback;
    private final DashChunkSource.Factory chunkSourceFactory;
    private final long elapsedRealtimeOffset;
    private final EmbeddedTrackInfo[] embeddedTrackInfos;
    private final AdaptiveMediaSourceEventListener.EventDispatcher eventDispatcher;
    final int id;
    private DashManifest manifest;
    private final LoaderErrorThrower manifestLoaderErrorThrower;
    private final int minLoadableRetryCount;
    private int periodIndex;
    private ChunkSampleStream<DashChunkSource>[] sampleStreams = newSampleStreamArray(0);
    private CompositeSequenceableLoader sequenceableLoader = new CompositeSequenceableLoader(this.sampleStreams);
    private final TrackGroupArray trackGroups;

    public DashMediaPeriod(int id, DashManifest manifest, int periodIndex, DashChunkSource.Factory chunkSourceFactory, int minLoadableRetryCount, AdaptiveMediaSourceEventListener.EventDispatcher eventDispatcher, long elapsedRealtimeOffset, LoaderErrorThrower manifestLoaderErrorThrower, Allocator allocator) {
        this.id = id;
        this.manifest = manifest;
        this.periodIndex = periodIndex;
        this.chunkSourceFactory = chunkSourceFactory;
        this.minLoadableRetryCount = minLoadableRetryCount;
        this.eventDispatcher = eventDispatcher;
        this.elapsedRealtimeOffset = elapsedRealtimeOffset;
        this.manifestLoaderErrorThrower = manifestLoaderErrorThrower;
        this.allocator = allocator;
        this.adaptationSets = manifest.getPeriod(periodIndex).adaptationSets;
        Pair<TrackGroupArray, EmbeddedTrackInfo[]> result = buildTrackGroups(this.adaptationSets);
        this.trackGroups = (TrackGroupArray) result.first;
        this.embeddedTrackInfos = (EmbeddedTrackInfo[]) result.second;
    }

    public void updateManifest(DashManifest manifest, int periodIndex) {
        this.manifest = manifest;
        this.periodIndex = periodIndex;
        this.adaptationSets = manifest.getPeriod(periodIndex).adaptationSets;
        if (this.sampleStreams != null) {
            for (ChunkSampleStream<DashChunkSource> sampleStream : this.sampleStreams) {
                ((DashChunkSource) sampleStream.getChunkSource()).updateManifest(manifest, periodIndex);
            }
            this.callback.onContinueLoadingRequested(this);
        }
    }

    public void release() {
        for (ChunkSampleStream<DashChunkSource> sampleStream : this.sampleStreams) {
            sampleStream.release();
        }
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public void prepare(MediaPeriod.Callback callback) {
        this.callback = callback;
        callback.onPrepared(this);
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public void maybeThrowPrepareError() throws IOException {
        this.manifestLoaderErrorThrower.maybeThrowError();
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public TrackGroupArray getTrackGroups() {
        return this.trackGroups;
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public long selectTracks(TrackSelection[] selections, boolean[] mayRetainStreamFlags, SampleStream[] streams, boolean[] streamResetFlags, long positionUs) {
        int trackGroupIndex;
        boolean mayRetainStream;
        int trackGroupIndex2;
        int adaptationSetCount = this.adaptationSets.size();
        HashMap<Integer, ChunkSampleStream<DashChunkSource>> primarySampleStreams = new HashMap<>();
        for (int i = 0; i < selections.length; i++) {
            if (streams[i] instanceof ChunkSampleStream) {
                ChunkSampleStream<DashChunkSource> stream = (ChunkSampleStream) streams[i];
                if (selections[i] == null || !mayRetainStreamFlags[i]) {
                    stream.release();
                    streams[i] = null;
                } else {
                    int adaptationSetIndex = this.trackGroups.indexOf(selections[i].getTrackGroup());
                    primarySampleStreams.put(Integer.valueOf(adaptationSetIndex), stream);
                }
            }
            if (streams[i] == null && selections[i] != null && (trackGroupIndex2 = this.trackGroups.indexOf(selections[i].getTrackGroup())) < adaptationSetCount) {
                ChunkSampleStream<DashChunkSource> stream2 = buildSampleStream(trackGroupIndex2, selections[i], positionUs);
                primarySampleStreams.put(Integer.valueOf(trackGroupIndex2), stream2);
                streams[i] = stream2;
                streamResetFlags[i] = true;
            }
        }
        for (int i2 = 0; i2 < selections.length; i2++) {
            if (((streams[i2] instanceof ChunkSampleStream.EmbeddedSampleStream) || (streams[i2] instanceof EmptySampleStream)) && (selections[i2] == null || !mayRetainStreamFlags[i2])) {
                releaseIfEmbeddedSampleStream(streams[i2]);
                streams[i2] = null;
            }
            if (selections[i2] != null && (trackGroupIndex = this.trackGroups.indexOf(selections[i2].getTrackGroup())) >= adaptationSetCount) {
                int embeddedTrackIndex = trackGroupIndex - adaptationSetCount;
                EmbeddedTrackInfo embeddedTrackInfo = this.embeddedTrackInfos[embeddedTrackIndex];
                int adaptationSetIndex2 = embeddedTrackInfo.adaptationSetIndex;
                ChunkSampleStream<?> primaryStream = primarySampleStreams.get(Integer.valueOf(adaptationSetIndex2));
                SampleStream stream3 = streams[i2];
                if (primaryStream == null) {
                    mayRetainStream = stream3 instanceof EmptySampleStream;
                } else {
                    mayRetainStream = (stream3 instanceof ChunkSampleStream.EmbeddedSampleStream) && ((ChunkSampleStream.EmbeddedSampleStream) stream3).parent == primaryStream;
                }
                if (!mayRetainStream) {
                    releaseIfEmbeddedSampleStream(stream3);
                    streams[i2] = primaryStream == null ? new EmptySampleStream() : primaryStream.selectEmbeddedTrack(positionUs, embeddedTrackInfo.trackType);
                    streamResetFlags[i2] = true;
                }
            }
        }
        this.sampleStreams = newSampleStreamArray(primarySampleStreams.size());
        primarySampleStreams.values().toArray(this.sampleStreams);
        this.sequenceableLoader = new CompositeSequenceableLoader(this.sampleStreams);
        return positionUs;
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public void discardBuffer(long positionUs) {
        for (ChunkSampleStream<DashChunkSource> sampleStream : this.sampleStreams) {
            sampleStream.discardUnselectedEmbeddedTracksTo(positionUs);
        }
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod, com.google.android.exoplayer2.source.SequenceableLoader
    public boolean continueLoading(long positionUs) {
        return this.sequenceableLoader.continueLoading(positionUs);
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod, com.google.android.exoplayer2.source.SequenceableLoader
    public long getNextLoadPositionUs() {
        return this.sequenceableLoader.getNextLoadPositionUs();
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public long readDiscontinuity() {
        return C.TIME_UNSET;
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public long getBufferedPositionUs() {
        long bufferedPositionUs = Long.MAX_VALUE;
        for (ChunkSampleStream<DashChunkSource> sampleStream : this.sampleStreams) {
            long rendererBufferedPositionUs = sampleStream.getBufferedPositionUs();
            if (rendererBufferedPositionUs != Long.MIN_VALUE) {
                bufferedPositionUs = Math.min(bufferedPositionUs, rendererBufferedPositionUs);
            }
        }
        if (bufferedPositionUs == Long.MAX_VALUE) {
            return Long.MIN_VALUE;
        }
        return bufferedPositionUs;
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public long seekToUs(long positionUs) {
        for (ChunkSampleStream<DashChunkSource> sampleStream : this.sampleStreams) {
            sampleStream.seekToUs(positionUs);
        }
        return positionUs;
    }

    @Override // com.google.android.exoplayer2.source.SequenceableLoader.Callback
    public void onContinueLoadingRequested(ChunkSampleStream<DashChunkSource> sampleStream) {
        this.callback.onContinueLoadingRequested(this);
    }

    private static Pair<TrackGroupArray, EmbeddedTrackInfo[]> buildTrackGroups(List<AdaptationSet> adaptationSets) {
        int adaptationSetCount = adaptationSets.size();
        int embeddedTrackCount = getEmbeddedTrackCount(adaptationSets);
        TrackGroup[] trackGroupArray = new TrackGroup[adaptationSetCount + embeddedTrackCount];
        EmbeddedTrackInfo[] embeddedTrackInfos = new EmbeddedTrackInfo[embeddedTrackCount];
        int embeddedTrackIndex = 0;
        int i = 0;
        while (true) {
            int embeddedTrackIndex2 = embeddedTrackIndex;
            if (i < adaptationSetCount) {
                AdaptationSet adaptationSet = adaptationSets.get(i);
                List<Representation> representations = adaptationSet.representations;
                Format[] formats = new Format[representations.size()];
                for (int j = 0; j < formats.length; j++) {
                    formats[j] = representations.get(j).format;
                }
                trackGroupArray[i] = new TrackGroup(formats);
                if (hasEventMessageTrack(adaptationSet)) {
                    Format format = Format.createSampleFormat(adaptationSet.id + ":emsg", MimeTypes.APPLICATION_EMSG, null, -1, null);
                    trackGroupArray[adaptationSetCount + embeddedTrackIndex2] = new TrackGroup(format);
                    embeddedTrackIndex = embeddedTrackIndex2 + 1;
                    embeddedTrackInfos[embeddedTrackIndex2] = new EmbeddedTrackInfo(i, 4);
                } else {
                    embeddedTrackIndex = embeddedTrackIndex2;
                }
                if (hasCea608Track(adaptationSet)) {
                    Format format2 = Format.createTextSampleFormat(adaptationSet.id + ":cea608", MimeTypes.APPLICATION_CEA608, null, -1, 0, null, null);
                    trackGroupArray[adaptationSetCount + embeddedTrackIndex] = new TrackGroup(format2);
                    embeddedTrackInfos[embeddedTrackIndex] = new EmbeddedTrackInfo(i, 3);
                    embeddedTrackIndex++;
                }
                i++;
            } else {
                return Pair.create(new TrackGroupArray(trackGroupArray), embeddedTrackInfos);
            }
        }
    }

    private ChunkSampleStream<DashChunkSource> buildSampleStream(int adaptationSetIndex, TrackSelection selection, long positionUs) {
        AdaptationSet adaptationSet = this.adaptationSets.get(adaptationSetIndex);
        int embeddedTrackCount = 0;
        int[] embeddedTrackTypes = new int[2];
        boolean enableEventMessageTrack = hasEventMessageTrack(adaptationSet);
        if (enableEventMessageTrack) {
            int embeddedTrackCount2 = 0 + 1;
            embeddedTrackTypes[0] = 4;
            embeddedTrackCount = embeddedTrackCount2;
        }
        boolean enableCea608Track = hasCea608Track(adaptationSet);
        if (enableCea608Track) {
            embeddedTrackTypes[embeddedTrackCount] = 3;
            embeddedTrackCount++;
        }
        if (embeddedTrackCount < embeddedTrackTypes.length) {
            embeddedTrackTypes = Arrays.copyOf(embeddedTrackTypes, embeddedTrackCount);
        }
        DashChunkSource chunkSource = this.chunkSourceFactory.createDashChunkSource(this.manifestLoaderErrorThrower, this.manifest, this.periodIndex, adaptationSetIndex, selection, this.elapsedRealtimeOffset, enableEventMessageTrack, enableCea608Track);
        ChunkSampleStream<DashChunkSource> stream = new ChunkSampleStream<>(adaptationSet.type, embeddedTrackTypes, chunkSource, this, this.allocator, positionUs, this.minLoadableRetryCount, this.eventDispatcher);
        return stream;
    }

    private static int getEmbeddedTrackCount(List<AdaptationSet> adaptationSets) {
        int embeddedTrackCount = 0;
        for (int i = 0; i < adaptationSets.size(); i++) {
            AdaptationSet adaptationSet = adaptationSets.get(i);
            if (hasEventMessageTrack(adaptationSet)) {
                embeddedTrackCount++;
            }
            if (hasCea608Track(adaptationSet)) {
                embeddedTrackCount++;
            }
        }
        return embeddedTrackCount;
    }

    private static boolean hasEventMessageTrack(AdaptationSet adaptationSet) {
        List<Representation> representations = adaptationSet.representations;
        for (int i = 0; i < representations.size(); i++) {
            Representation representation = representations.get(i);
            if (!representation.inbandEventStreams.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasCea608Track(AdaptationSet adaptationSet) {
        List<SchemeValuePair> descriptors = adaptationSet.accessibilityDescriptors;
        for (int i = 0; i < descriptors.size(); i++) {
            SchemeValuePair descriptor = descriptors.get(i);
            if ("urn:scte:dash:cc:cea-608:2015".equals(descriptor.schemeIdUri)) {
                return true;
            }
        }
        return false;
    }

    private static ChunkSampleStream<DashChunkSource>[] newSampleStreamArray(int length) {
        return new ChunkSampleStream[length];
    }

    private static void releaseIfEmbeddedSampleStream(SampleStream sampleStream) {
        if (sampleStream instanceof ChunkSampleStream.EmbeddedSampleStream) {
            ((ChunkSampleStream.EmbeddedSampleStream) sampleStream).release();
        }
    }

    private static final class EmbeddedTrackInfo {
        public final int adaptationSetIndex;
        public final int trackType;

        public EmbeddedTrackInfo(int adaptationSetIndex, int trackType) {
            this.adaptationSetIndex = adaptationSetIndex;
            this.trackType = trackType;
        }
    }
}
