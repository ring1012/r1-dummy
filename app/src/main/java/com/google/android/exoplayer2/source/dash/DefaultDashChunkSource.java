package com.google.android.exoplayer2.source.dash;

import android.os.SystemClock;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.ChunkIndex;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.mkv.MatroskaExtractor;
import com.google.android.exoplayer2.extractor.mp4.FragmentedMp4Extractor;
import com.google.android.exoplayer2.extractor.rawcc.RawCcExtractor;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.chunk.Chunk;
import com.google.android.exoplayer2.source.chunk.ChunkExtractorWrapper;
import com.google.android.exoplayer2.source.chunk.ChunkHolder;
import com.google.android.exoplayer2.source.chunk.ChunkedTrackBlacklistUtil;
import com.google.android.exoplayer2.source.chunk.ContainerMediaChunk;
import com.google.android.exoplayer2.source.chunk.InitializationChunk;
import com.google.android.exoplayer2.source.chunk.MediaChunk;
import com.google.android.exoplayer2.source.chunk.SingleSampleMediaChunk;
import com.google.android.exoplayer2.source.dash.DashChunkSource;
import com.google.android.exoplayer2.source.dash.manifest.AdaptationSet;
import com.google.android.exoplayer2.source.dash.manifest.DashManifest;
import com.google.android.exoplayer2.source.dash.manifest.RangedUri;
import com.google.android.exoplayer2.source.dash.manifest.Representation;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.LoaderErrorThrower;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.List;

/* loaded from: classes.dex */
public class DefaultDashChunkSource implements DashChunkSource {
    private final int adaptationSetIndex;
    private final DataSource dataSource;
    private final long elapsedRealtimeOffsetMs;
    private IOException fatalError;
    private DashManifest manifest;
    private final LoaderErrorThrower manifestLoaderErrorThrower;
    private final int maxSegmentsPerLoad;
    private boolean missingLastSegment;
    private int periodIndex;
    private final RepresentationHolder[] representationHolders;
    private final TrackSelection trackSelection;

    public static final class Factory implements DashChunkSource.Factory {
        private final DataSource.Factory dataSourceFactory;
        private final int maxSegmentsPerLoad;

        public Factory(DataSource.Factory dataSourceFactory) {
            this(dataSourceFactory, 1);
        }

        public Factory(DataSource.Factory dataSourceFactory, int maxSegmentsPerLoad) {
            this.dataSourceFactory = dataSourceFactory;
            this.maxSegmentsPerLoad = maxSegmentsPerLoad;
        }

        @Override // com.google.android.exoplayer2.source.dash.DashChunkSource.Factory
        public DashChunkSource createDashChunkSource(LoaderErrorThrower manifestLoaderErrorThrower, DashManifest manifest, int periodIndex, int adaptationSetIndex, TrackSelection trackSelection, long elapsedRealtimeOffsetMs, boolean enableEventMessageTrack, boolean enableCea608Track) {
            DataSource dataSource = this.dataSourceFactory.createDataSource();
            return new DefaultDashChunkSource(manifestLoaderErrorThrower, manifest, periodIndex, adaptationSetIndex, trackSelection, dataSource, elapsedRealtimeOffsetMs, this.maxSegmentsPerLoad, enableEventMessageTrack, enableCea608Track);
        }
    }

    public DefaultDashChunkSource(LoaderErrorThrower manifestLoaderErrorThrower, DashManifest manifest, int periodIndex, int adaptationSetIndex, TrackSelection trackSelection, DataSource dataSource, long elapsedRealtimeOffsetMs, int maxSegmentsPerLoad, boolean enableEventMessageTrack, boolean enableCea608Track) {
        this.manifestLoaderErrorThrower = manifestLoaderErrorThrower;
        this.manifest = manifest;
        this.adaptationSetIndex = adaptationSetIndex;
        this.trackSelection = trackSelection;
        this.dataSource = dataSource;
        this.periodIndex = periodIndex;
        this.elapsedRealtimeOffsetMs = elapsedRealtimeOffsetMs;
        this.maxSegmentsPerLoad = maxSegmentsPerLoad;
        long periodDurationUs = manifest.getPeriodDurationUs(periodIndex);
        AdaptationSet adaptationSet = getAdaptationSet();
        List<Representation> representations = adaptationSet.representations;
        this.representationHolders = new RepresentationHolder[trackSelection.length()];
        for (int i = 0; i < this.representationHolders.length; i++) {
            Representation representation = representations.get(trackSelection.getIndexInTrackGroup(i));
            this.representationHolders[i] = new RepresentationHolder(periodDurationUs, representation, enableEventMessageTrack, enableCea608Track, adaptationSet.type);
        }
    }

    @Override // com.google.android.exoplayer2.source.dash.DashChunkSource
    public void updateManifest(DashManifest newManifest, int newPeriodIndex) {
        try {
            this.manifest = newManifest;
            this.periodIndex = newPeriodIndex;
            long periodDurationUs = this.manifest.getPeriodDurationUs(this.periodIndex);
            List<Representation> representations = getAdaptationSet().representations;
            for (int i = 0; i < this.representationHolders.length; i++) {
                Representation representation = representations.get(this.trackSelection.getIndexInTrackGroup(i));
                this.representationHolders[i].updateRepresentation(periodDurationUs, representation);
            }
        } catch (BehindLiveWindowException e) {
            this.fatalError = e;
        }
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
        int lastAvailableSegmentNum;
        int segmentNum;
        if (this.fatalError == null) {
            long bufferedDurationUs = previous != null ? previous.endTimeUs - playbackPositionUs : 0L;
            this.trackSelection.updateSelectedTrack(bufferedDurationUs);
            RepresentationHolder representationHolder = this.representationHolders[this.trackSelection.getSelectedIndex()];
            if (representationHolder.extractorWrapper != null) {
                Representation selectedRepresentation = representationHolder.representation;
                RangedUri pendingInitializationUri = null;
                RangedUri pendingIndexUri = null;
                if (representationHolder.extractorWrapper.getSampleFormats() == null) {
                    pendingInitializationUri = selectedRepresentation.getInitializationUri();
                }
                if (representationHolder.segmentIndex == null) {
                    pendingIndexUri = selectedRepresentation.getIndexUri();
                }
                if (pendingInitializationUri != null || pendingIndexUri != null) {
                    out.chunk = newInitializationChunk(representationHolder, this.dataSource, this.trackSelection.getSelectedFormat(), this.trackSelection.getSelectionReason(), this.trackSelection.getSelectionData(), pendingInitializationUri, pendingIndexUri);
                    return;
                }
            }
            long nowUs = getNowUnixTimeUs();
            int availableSegmentCount = representationHolder.getSegmentCount();
            if (availableSegmentCount == 0) {
                out.endOfStream = !this.manifest.dynamic || this.periodIndex < this.manifest.getPeriodCount() + (-1);
                return;
            }
            int firstAvailableSegmentNum = representationHolder.getFirstSegmentNum();
            if (availableSegmentCount == -1) {
                long liveEdgeTimeUs = nowUs - (this.manifest.availabilityStartTime * 1000);
                long periodStartUs = this.manifest.getPeriod(this.periodIndex).startMs * 1000;
                long liveEdgeTimeInPeriodUs = liveEdgeTimeUs - periodStartUs;
                if (this.manifest.timeShiftBufferDepth != C.TIME_UNSET) {
                    long bufferDepthUs = this.manifest.timeShiftBufferDepth * 1000;
                    firstAvailableSegmentNum = Math.max(firstAvailableSegmentNum, representationHolder.getSegmentNum(liveEdgeTimeInPeriodUs - bufferDepthUs));
                }
                lastAvailableSegmentNum = representationHolder.getSegmentNum(liveEdgeTimeInPeriodUs) - 1;
            } else {
                lastAvailableSegmentNum = (firstAvailableSegmentNum + availableSegmentCount) - 1;
            }
            if (previous == null) {
                segmentNum = Util.constrainValue(representationHolder.getSegmentNum(playbackPositionUs), firstAvailableSegmentNum, lastAvailableSegmentNum);
            } else {
                segmentNum = previous.getNextChunkIndex();
                if (segmentNum < firstAvailableSegmentNum) {
                    this.fatalError = new BehindLiveWindowException();
                    return;
                }
            }
            if (segmentNum > lastAvailableSegmentNum || (this.missingLastSegment && segmentNum >= lastAvailableSegmentNum)) {
                out.endOfStream = !this.manifest.dynamic || this.periodIndex < this.manifest.getPeriodCount() + (-1);
            } else {
                int maxSegmentCount = Math.min(this.maxSegmentsPerLoad, (lastAvailableSegmentNum - segmentNum) + 1);
                out.chunk = newMediaChunk(representationHolder, this.dataSource, this.trackSelection.getSelectedFormat(), this.trackSelection.getSelectionReason(), this.trackSelection.getSelectionData(), segmentNum, maxSegmentCount);
            }
        }
    }

    @Override // com.google.android.exoplayer2.source.chunk.ChunkSource
    public void onChunkLoadCompleted(Chunk chunk) {
        SeekMap seekMap;
        if (chunk instanceof InitializationChunk) {
            InitializationChunk initializationChunk = (InitializationChunk) chunk;
            RepresentationHolder representationHolder = this.representationHolders[this.trackSelection.indexOf(initializationChunk.trackFormat)];
            if (representationHolder.segmentIndex == null && (seekMap = representationHolder.extractorWrapper.getSeekMap()) != null) {
                representationHolder.segmentIndex = new DashWrappingSegmentIndex((ChunkIndex) seekMap);
            }
        }
    }

    @Override // com.google.android.exoplayer2.source.chunk.ChunkSource
    public boolean onChunkLoadError(Chunk chunk, boolean cancelable, Exception e) {
        RepresentationHolder representationHolder;
        int segmentCount;
        if (!cancelable) {
            return false;
        }
        if (!this.manifest.dynamic && (chunk instanceof MediaChunk) && (e instanceof HttpDataSource.InvalidResponseCodeException) && ((HttpDataSource.InvalidResponseCodeException) e).responseCode == 404 && (segmentCount = (representationHolder = this.representationHolders[this.trackSelection.indexOf(chunk.trackFormat)]).getSegmentCount()) != -1 && segmentCount != 0) {
            int lastAvailableSegmentNum = (representationHolder.getFirstSegmentNum() + segmentCount) - 1;
            if (((MediaChunk) chunk).getNextChunkIndex() > lastAvailableSegmentNum) {
                this.missingLastSegment = true;
                return true;
            }
        }
        return ChunkedTrackBlacklistUtil.maybeBlacklistTrack(this.trackSelection, this.trackSelection.indexOf(chunk.trackFormat), e);
    }

    private AdaptationSet getAdaptationSet() {
        return this.manifest.getPeriod(this.periodIndex).adaptationSets.get(this.adaptationSetIndex);
    }

    private long getNowUnixTimeUs() {
        return this.elapsedRealtimeOffsetMs != 0 ? (SystemClock.elapsedRealtime() + this.elapsedRealtimeOffsetMs) * 1000 : System.currentTimeMillis() * 1000;
    }

    private static Chunk newInitializationChunk(RepresentationHolder representationHolder, DataSource dataSource, Format trackFormat, int trackSelectionReason, Object trackSelectionData, RangedUri initializationUri, RangedUri indexUri) {
        RangedUri requestUri;
        String baseUrl = representationHolder.representation.baseUrl;
        if (initializationUri != null) {
            requestUri = initializationUri.attemptMerge(indexUri, baseUrl);
            if (requestUri == null) {
                requestUri = initializationUri;
            }
        } else {
            requestUri = indexUri;
        }
        DataSpec dataSpec = new DataSpec(requestUri.resolveUri(baseUrl), requestUri.start, requestUri.length, representationHolder.representation.getCacheKey());
        return new InitializationChunk(dataSource, dataSpec, trackFormat, trackSelectionReason, trackSelectionData, representationHolder.extractorWrapper);
    }

    private static Chunk newMediaChunk(RepresentationHolder representationHolder, DataSource dataSource, Format trackFormat, int trackSelectionReason, Object trackSelectionData, int firstSegmentNum, int maxSegmentCount) {
        Representation representation = representationHolder.representation;
        long startTimeUs = representationHolder.getSegmentStartTimeUs(firstSegmentNum);
        RangedUri segmentUri = representationHolder.getSegmentUrl(firstSegmentNum);
        String baseUrl = representation.baseUrl;
        if (representationHolder.extractorWrapper == null) {
            long endTimeUs = representationHolder.getSegmentEndTimeUs(firstSegmentNum);
            DataSpec dataSpec = new DataSpec(segmentUri.resolveUri(baseUrl), segmentUri.start, segmentUri.length, representation.getCacheKey());
            return new SingleSampleMediaChunk(dataSource, dataSpec, trackFormat, trackSelectionReason, trackSelectionData, startTimeUs, endTimeUs, firstSegmentNum, representationHolder.trackType, trackFormat);
        }
        int segmentCount = 1;
        for (int i = 1; i < maxSegmentCount; i++) {
            RangedUri nextSegmentUri = representationHolder.getSegmentUrl(firstSegmentNum + i);
            RangedUri mergedSegmentUri = segmentUri.attemptMerge(nextSegmentUri, baseUrl);
            if (mergedSegmentUri == null) {
                break;
            }
            segmentUri = mergedSegmentUri;
            segmentCount++;
        }
        long endTimeUs2 = representationHolder.getSegmentEndTimeUs((firstSegmentNum + segmentCount) - 1);
        DataSpec dataSpec2 = new DataSpec(segmentUri.resolveUri(baseUrl), segmentUri.start, segmentUri.length, representation.getCacheKey());
        long sampleOffsetUs = -representation.presentationTimeOffsetUs;
        return new ContainerMediaChunk(dataSource, dataSpec2, trackFormat, trackSelectionReason, trackSelectionData, startTimeUs, endTimeUs2, firstSegmentNum, segmentCount, sampleOffsetUs, representationHolder.extractorWrapper);
    }

    protected static final class RepresentationHolder {
        public final ChunkExtractorWrapper extractorWrapper;
        private long periodDurationUs;
        public Representation representation;
        public DashSegmentIndex segmentIndex;
        private int segmentNumShift;
        public final int trackType;

        public RepresentationHolder(long periodDurationUs, Representation representation, boolean enableEventMessageTrack, boolean enableCea608Track, int trackType) {
            Extractor extractor;
            this.periodDurationUs = periodDurationUs;
            this.representation = representation;
            this.trackType = trackType;
            String containerMimeType = representation.format.containerMimeType;
            if (mimeTypeIsRawText(containerMimeType)) {
                this.extractorWrapper = null;
            } else {
                if (MimeTypes.APPLICATION_RAWCC.equals(containerMimeType)) {
                    extractor = new RawCcExtractor(representation.format);
                } else if (mimeTypeIsWebm(containerMimeType)) {
                    extractor = new MatroskaExtractor(1);
                } else {
                    int flags = enableEventMessageTrack ? 0 | 4 : 0;
                    extractor = new FragmentedMp4Extractor(enableCea608Track ? flags | 8 : flags);
                }
                this.extractorWrapper = new ChunkExtractorWrapper(extractor, representation.format);
            }
            this.segmentIndex = representation.getIndex();
        }

        public void updateRepresentation(long newPeriodDurationUs, Representation newRepresentation) throws BehindLiveWindowException {
            int oldIndexSegmentCount;
            DashSegmentIndex oldIndex = this.representation.getIndex();
            DashSegmentIndex newIndex = newRepresentation.getIndex();
            this.periodDurationUs = newPeriodDurationUs;
            this.representation = newRepresentation;
            if (oldIndex != null) {
                this.segmentIndex = newIndex;
                if (oldIndex.isExplicit() && (oldIndexSegmentCount = oldIndex.getSegmentCount(this.periodDurationUs)) != 0) {
                    int oldIndexLastSegmentNum = (oldIndex.getFirstSegmentNum() + oldIndexSegmentCount) - 1;
                    long oldIndexEndTimeUs = oldIndex.getTimeUs(oldIndexLastSegmentNum) + oldIndex.getDurationUs(oldIndexLastSegmentNum, this.periodDurationUs);
                    int newIndexFirstSegmentNum = newIndex.getFirstSegmentNum();
                    long newIndexStartTimeUs = newIndex.getTimeUs(newIndexFirstSegmentNum);
                    if (oldIndexEndTimeUs == newIndexStartTimeUs) {
                        this.segmentNumShift += (oldIndexLastSegmentNum + 1) - newIndexFirstSegmentNum;
                    } else {
                        if (oldIndexEndTimeUs < newIndexStartTimeUs) {
                            throw new BehindLiveWindowException();
                        }
                        this.segmentNumShift += oldIndex.getSegmentNum(newIndexStartTimeUs, this.periodDurationUs) - newIndexFirstSegmentNum;
                    }
                }
            }
        }

        public int getFirstSegmentNum() {
            return this.segmentIndex.getFirstSegmentNum() + this.segmentNumShift;
        }

        public int getSegmentCount() {
            return this.segmentIndex.getSegmentCount(this.periodDurationUs);
        }

        public long getSegmentStartTimeUs(int segmentNum) {
            return this.segmentIndex.getTimeUs(segmentNum - this.segmentNumShift);
        }

        public long getSegmentEndTimeUs(int segmentNum) {
            return getSegmentStartTimeUs(segmentNum) + this.segmentIndex.getDurationUs(segmentNum - this.segmentNumShift, this.periodDurationUs);
        }

        public int getSegmentNum(long positionUs) {
            return this.segmentIndex.getSegmentNum(positionUs, this.periodDurationUs) + this.segmentNumShift;
        }

        public RangedUri getSegmentUrl(int segmentNum) {
            return this.segmentIndex.getSegmentUrl(segmentNum - this.segmentNumShift);
        }

        private static boolean mimeTypeIsWebm(String mimeType) {
            return mimeType.startsWith(MimeTypes.VIDEO_WEBM) || mimeType.startsWith(MimeTypes.AUDIO_WEBM) || mimeType.startsWith(MimeTypes.APPLICATION_WEBM);
        }

        private static boolean mimeTypeIsRawText(String mimeType) {
            return MimeTypes.isText(mimeType) || MimeTypes.APPLICATION_TTML.equals(mimeType);
        }
    }
}
