package com.google.android.exoplayer2.source;

import android.net.Uri;
import android.os.Handler;
import android.util.SparseArray;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.extractor.DefaultExtractorInput;
import com.google.android.exoplayer2.extractor.DefaultTrackOutput;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.ConditionVariable;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import java.io.EOFException;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

/* loaded from: classes.dex */
final class ExtractorMediaPeriod implements MediaPeriod, ExtractorOutput, Loader.Callback<ExtractingLoadable>, DefaultTrackOutput.UpstreamFormatChangedListener {
    private static final long DEFAULT_LAST_SAMPLE_DURATION_US = 10000;
    private final Allocator allocator;
    private MediaPeriod.Callback callback;
    private final String customCacheKey;
    private final DataSource dataSource;
    private long durationUs;
    private int enabledTrackCount;
    private final Handler eventHandler;
    private final ExtractorMediaSource.EventListener eventListener;
    private int extractedSamplesCountAtStartOfLoad;
    private final ExtractorHolder extractorHolder;
    private boolean haveAudioVideoTracks;
    private long lastSeekPositionUs;
    private boolean loadingFinished;
    private final int minLoadableRetryCount;
    private boolean notifyReset;
    private boolean prepared;
    private boolean released;
    private SeekMap seekMap;
    private boolean seenFirstTrackSelection;
    private final MediaSource.Listener sourceListener;
    private boolean[] trackEnabledStates;
    private boolean[] trackIsAudioVideoFlags;
    private TrackGroupArray tracks;
    private boolean tracksBuilt;
    private final Uri uri;
    private final Loader loader = new Loader("Loader:ExtractorMediaPeriod");
    private final ConditionVariable loadCondition = new ConditionVariable();
    private final Runnable maybeFinishPrepareRunnable = new Runnable() { // from class: com.google.android.exoplayer2.source.ExtractorMediaPeriod.1
        @Override // java.lang.Runnable
        public void run() {
            ExtractorMediaPeriod.this.maybeFinishPrepare();
        }
    };
    private final Runnable onContinueLoadingRequestedRunnable = new Runnable() { // from class: com.google.android.exoplayer2.source.ExtractorMediaPeriod.2
        @Override // java.lang.Runnable
        public void run() {
            if (!ExtractorMediaPeriod.this.released) {
                ExtractorMediaPeriod.this.callback.onContinueLoadingRequested(ExtractorMediaPeriod.this);
            }
        }
    };
    private final Handler handler = new Handler();
    private long pendingResetPositionUs = C.TIME_UNSET;
    private final SparseArray<DefaultTrackOutput> sampleQueues = new SparseArray<>();
    private long length = -1;

    public ExtractorMediaPeriod(Uri uri, DataSource dataSource, Extractor[] extractors, int minLoadableRetryCount, Handler eventHandler, ExtractorMediaSource.EventListener eventListener, MediaSource.Listener sourceListener, Allocator allocator, String customCacheKey) {
        this.uri = uri;
        this.dataSource = dataSource;
        this.minLoadableRetryCount = minLoadableRetryCount;
        this.eventHandler = eventHandler;
        this.eventListener = eventListener;
        this.sourceListener = sourceListener;
        this.allocator = allocator;
        this.customCacheKey = customCacheKey;
        this.extractorHolder = new ExtractorHolder(extractors, this);
    }

    public void release() {
        final ExtractorHolder extractorHolder = this.extractorHolder;
        this.loader.release(new Runnable() { // from class: com.google.android.exoplayer2.source.ExtractorMediaPeriod.3
            @Override // java.lang.Runnable
            public void run() {
                extractorHolder.release();
                int trackCount = ExtractorMediaPeriod.this.sampleQueues.size();
                for (int i = 0; i < trackCount; i++) {
                    ((DefaultTrackOutput) ExtractorMediaPeriod.this.sampleQueues.valueAt(i)).disable();
                }
            }
        });
        this.handler.removeCallbacksAndMessages(null);
        this.released = true;
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public void prepare(MediaPeriod.Callback callback) {
        this.callback = callback;
        this.loadCondition.open();
        startLoading();
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public void maybeThrowPrepareError() throws IOException {
        maybeThrowError();
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public TrackGroupArray getTrackGroups() {
        return this.tracks;
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public long selectTracks(TrackSelection[] selections, boolean[] mayRetainStreamFlags, SampleStream[] streams, boolean[] streamResetFlags, long positionUs) {
        Assertions.checkState(this.prepared);
        for (int i = 0; i < selections.length; i++) {
            if (streams[i] != null && (selections[i] == null || !mayRetainStreamFlags[i])) {
                int track = ((SampleStreamImpl) streams[i]).track;
                Assertions.checkState(this.trackEnabledStates[track]);
                this.enabledTrackCount--;
                this.trackEnabledStates[track] = false;
                this.sampleQueues.valueAt(track).disable();
                streams[i] = null;
            }
        }
        boolean selectedNewTracks = false;
        for (int i2 = 0; i2 < selections.length; i2++) {
            if (streams[i2] == null && selections[i2] != null) {
                TrackSelection selection = selections[i2];
                Assertions.checkState(selection.length() == 1);
                Assertions.checkState(selection.getIndexInTrackGroup(0) == 0);
                int track2 = this.tracks.indexOf(selection.getTrackGroup());
                Assertions.checkState(!this.trackEnabledStates[track2]);
                this.enabledTrackCount++;
                this.trackEnabledStates[track2] = true;
                streams[i2] = new SampleStreamImpl(track2);
                streamResetFlags[i2] = true;
                selectedNewTracks = true;
            }
        }
        if (!this.seenFirstTrackSelection) {
            int trackCount = this.sampleQueues.size();
            for (int i3 = 0; i3 < trackCount; i3++) {
                if (!this.trackEnabledStates[i3]) {
                    this.sampleQueues.valueAt(i3).disable();
                }
            }
        }
        if (this.enabledTrackCount == 0) {
            this.notifyReset = false;
            if (this.loader.isLoading()) {
                this.loader.cancelLoading();
            }
        } else if (!this.seenFirstTrackSelection ? positionUs != 0 : selectedNewTracks) {
            positionUs = seekToUs(positionUs);
            for (int i4 = 0; i4 < streams.length; i4++) {
                if (streams[i4] != null) {
                    streamResetFlags[i4] = true;
                }
            }
        }
        this.seenFirstTrackSelection = true;
        return positionUs;
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public void discardBuffer(long positionUs) {
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod, com.google.android.exoplayer2.source.SequenceableLoader
    public boolean continueLoading(long playbackPositionUs) {
        if (this.loadingFinished || (this.prepared && this.enabledTrackCount == 0)) {
            return false;
        }
        boolean zOpen = this.loadCondition.open();
        if (!this.loader.isLoading()) {
            startLoading();
            return true;
        }
        return zOpen;
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod, com.google.android.exoplayer2.source.SequenceableLoader
    public long getNextLoadPositionUs() {
        if (this.enabledTrackCount == 0) {
            return Long.MIN_VALUE;
        }
        return getBufferedPositionUs();
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public long readDiscontinuity() {
        if (!this.notifyReset) {
            return C.TIME_UNSET;
        }
        this.notifyReset = false;
        return this.lastSeekPositionUs;
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public long getBufferedPositionUs() {
        long largestQueuedTimestampUs;
        if (this.loadingFinished) {
            return Long.MIN_VALUE;
        }
        if (isPendingReset()) {
            return this.pendingResetPositionUs;
        }
        if (this.haveAudioVideoTracks) {
            largestQueuedTimestampUs = Long.MAX_VALUE;
            int trackCount = this.sampleQueues.size();
            for (int i = 0; i < trackCount; i++) {
                if (this.trackIsAudioVideoFlags[i]) {
                    largestQueuedTimestampUs = Math.min(largestQueuedTimestampUs, this.sampleQueues.valueAt(i).getLargestQueuedTimestampUs());
                }
            }
        } else {
            largestQueuedTimestampUs = getLargestQueuedTimestampUs();
        }
        if (largestQueuedTimestampUs == Long.MIN_VALUE) {
            long largestQueuedTimestampUs2 = this.lastSeekPositionUs;
            return largestQueuedTimestampUs2;
        }
        return largestQueuedTimestampUs;
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public long seekToUs(long positionUs) {
        if (!this.seekMap.isSeekable()) {
            positionUs = 0;
        }
        this.lastSeekPositionUs = positionUs;
        int trackCount = this.sampleQueues.size();
        boolean seekInsideBuffer = !isPendingReset();
        for (int i = 0; seekInsideBuffer && i < trackCount; i++) {
            if (this.trackEnabledStates[i]) {
                seekInsideBuffer = this.sampleQueues.valueAt(i).skipToKeyframeBefore(positionUs, false);
            }
        }
        if (!seekInsideBuffer) {
            this.pendingResetPositionUs = positionUs;
            this.loadingFinished = false;
            if (this.loader.isLoading()) {
                this.loader.cancelLoading();
            } else {
                for (int i2 = 0; i2 < trackCount; i2++) {
                    this.sampleQueues.valueAt(i2).reset(this.trackEnabledStates[i2]);
                }
            }
        }
        this.notifyReset = false;
        return positionUs;
    }

    boolean isReady(int track) {
        return this.loadingFinished || !(isPendingReset() || this.sampleQueues.valueAt(track).isEmpty());
    }

    void maybeThrowError() throws IOException {
        this.loader.maybeThrowError();
    }

    int readData(int track, FormatHolder formatHolder, DecoderInputBuffer buffer, boolean formatRequired) {
        if (this.notifyReset || isPendingReset()) {
            return -3;
        }
        return this.sampleQueues.valueAt(track).readData(formatHolder, buffer, formatRequired, this.loadingFinished, this.lastSeekPositionUs);
    }

    void skipData(int track, long positionUs) {
        DefaultTrackOutput sampleQueue = this.sampleQueues.valueAt(track);
        if (this.loadingFinished && positionUs > sampleQueue.getLargestQueuedTimestampUs()) {
            sampleQueue.skipAll();
        } else {
            sampleQueue.skipToKeyframeBefore(positionUs, true);
        }
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.Callback
    public void onLoadCompleted(ExtractingLoadable loadable, long elapsedRealtimeMs, long loadDurationMs) {
        copyLengthFromLoader(loadable);
        this.loadingFinished = true;
        if (this.durationUs == C.TIME_UNSET) {
            long largestQueuedTimestampUs = getLargestQueuedTimestampUs();
            this.durationUs = largestQueuedTimestampUs == Long.MIN_VALUE ? 0L : DEFAULT_LAST_SAMPLE_DURATION_US + largestQueuedTimestampUs;
            this.sourceListener.onSourceInfoRefreshed(new SinglePeriodTimeline(this.durationUs, this.seekMap.isSeekable()), null);
        }
        this.callback.onContinueLoadingRequested(this);
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.Callback
    public void onLoadCanceled(ExtractingLoadable loadable, long elapsedRealtimeMs, long loadDurationMs, boolean released) {
        copyLengthFromLoader(loadable);
        if (!released && this.enabledTrackCount > 0) {
            int trackCount = this.sampleQueues.size();
            for (int i = 0; i < trackCount; i++) {
                this.sampleQueues.valueAt(i).reset(this.trackEnabledStates[i]);
            }
            this.callback.onContinueLoadingRequested(this);
        }
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.Callback
    public int onLoadError(ExtractingLoadable loadable, long elapsedRealtimeMs, long loadDurationMs, IOException error) {
        copyLengthFromLoader(loadable);
        notifyLoadError(error);
        if (isLoadableExceptionFatal(error)) {
            return 3;
        }
        int extractedSamplesCount = getExtractedSamplesCount();
        boolean madeProgress = extractedSamplesCount > this.extractedSamplesCountAtStartOfLoad;
        configureRetry(loadable);
        this.extractedSamplesCountAtStartOfLoad = getExtractedSamplesCount();
        return !madeProgress ? 0 : 1;
    }

    @Override // com.google.android.exoplayer2.extractor.ExtractorOutput
    public TrackOutput track(int id, int type) {
        DefaultTrackOutput trackOutput = this.sampleQueues.get(id);
        if (trackOutput == null) {
            DefaultTrackOutput trackOutput2 = new DefaultTrackOutput(this.allocator);
            trackOutput2.setUpstreamFormatChangeListener(this);
            this.sampleQueues.put(id, trackOutput2);
            return trackOutput2;
        }
        return trackOutput;
    }

    @Override // com.google.android.exoplayer2.extractor.ExtractorOutput
    public void endTracks() {
        this.tracksBuilt = true;
        this.handler.post(this.maybeFinishPrepareRunnable);
    }

    @Override // com.google.android.exoplayer2.extractor.ExtractorOutput
    public void seekMap(SeekMap seekMap) {
        this.seekMap = seekMap;
        this.handler.post(this.maybeFinishPrepareRunnable);
    }

    @Override // com.google.android.exoplayer2.extractor.DefaultTrackOutput.UpstreamFormatChangedListener
    public void onUpstreamFormatChanged(Format format) {
        this.handler.post(this.maybeFinishPrepareRunnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybeFinishPrepare() {
        if (!this.released && !this.prepared && this.seekMap != null && this.tracksBuilt) {
            int trackCount = this.sampleQueues.size();
            for (int i = 0; i < trackCount; i++) {
                if (this.sampleQueues.valueAt(i).getUpstreamFormat() == null) {
                    return;
                }
            }
            this.loadCondition.close();
            TrackGroup[] trackArray = new TrackGroup[trackCount];
            this.trackIsAudioVideoFlags = new boolean[trackCount];
            this.trackEnabledStates = new boolean[trackCount];
            this.durationUs = this.seekMap.getDurationUs();
            for (int i2 = 0; i2 < trackCount; i2++) {
                Format trackFormat = this.sampleQueues.valueAt(i2).getUpstreamFormat();
                trackArray[i2] = new TrackGroup(trackFormat);
                String mimeType = trackFormat.sampleMimeType;
                boolean isAudioVideo = MimeTypes.isVideo(mimeType) || MimeTypes.isAudio(mimeType);
                this.trackIsAudioVideoFlags[i2] = isAudioVideo;
                this.haveAudioVideoTracks |= isAudioVideo;
            }
            this.tracks = new TrackGroupArray(trackArray);
            this.prepared = true;
            this.sourceListener.onSourceInfoRefreshed(new SinglePeriodTimeline(this.durationUs, this.seekMap.isSeekable()), null);
            this.callback.onPrepared(this);
        }
    }

    private void copyLengthFromLoader(ExtractingLoadable loadable) {
        if (this.length != -1) {
            return;
        }
        this.length = loadable.length;
    }

    private void startLoading() {
        ExtractingLoadable loadable = new ExtractingLoadable(this.uri, this.dataSource, this.extractorHolder, this.loadCondition);
        if (this.prepared) {
            Assertions.checkState(isPendingReset());
            if (this.durationUs != C.TIME_UNSET && this.pendingResetPositionUs >= this.durationUs) {
                this.loadingFinished = true;
                this.pendingResetPositionUs = C.TIME_UNSET;
                return;
            } else {
                loadable.setLoadPosition(this.seekMap.getPosition(this.pendingResetPositionUs), this.pendingResetPositionUs);
                this.pendingResetPositionUs = C.TIME_UNSET;
            }
        }
        this.extractedSamplesCountAtStartOfLoad = getExtractedSamplesCount();
        int minRetryCount = this.minLoadableRetryCount;
        if (minRetryCount == -1) {
            minRetryCount = (this.prepared && this.length == -1 && (this.seekMap == null || this.seekMap.getDurationUs() == C.TIME_UNSET)) ? 6 : 3;
        }
        this.loader.startLoading(loadable, this, minRetryCount);
    }

    private void configureRetry(ExtractingLoadable loadable) {
        if (this.length == -1) {
            if (this.seekMap == null || this.seekMap.getDurationUs() == C.TIME_UNSET) {
                this.lastSeekPositionUs = 0L;
                this.notifyReset = this.prepared;
                int trackCount = this.sampleQueues.size();
                for (int i = 0; i < trackCount; i++) {
                    this.sampleQueues.valueAt(i).reset(!this.prepared || this.trackEnabledStates[i]);
                }
                loadable.setLoadPosition(0L, 0L);
            }
        }
    }

    private int getExtractedSamplesCount() {
        int extractedSamplesCount = 0;
        int trackCount = this.sampleQueues.size();
        for (int i = 0; i < trackCount; i++) {
            extractedSamplesCount += this.sampleQueues.valueAt(i).getWriteIndex();
        }
        return extractedSamplesCount;
    }

    private long getLargestQueuedTimestampUs() {
        long largestQueuedTimestampUs = Long.MIN_VALUE;
        int trackCount = this.sampleQueues.size();
        for (int i = 0; i < trackCount; i++) {
            largestQueuedTimestampUs = Math.max(largestQueuedTimestampUs, this.sampleQueues.valueAt(i).getLargestQueuedTimestampUs());
        }
        return largestQueuedTimestampUs;
    }

    private boolean isPendingReset() {
        return this.pendingResetPositionUs != C.TIME_UNSET;
    }

    private boolean isLoadableExceptionFatal(IOException e) {
        return e instanceof UnrecognizedInputFormatException;
    }

    private void notifyLoadError(final IOException error) {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post(new Runnable() { // from class: com.google.android.exoplayer2.source.ExtractorMediaPeriod.4
                @Override // java.lang.Runnable
                public void run() {
                    ExtractorMediaPeriod.this.eventListener.onLoadError(error);
                }
            });
        }
    }

    private final class SampleStreamImpl implements SampleStream {
        private final int track;

        public SampleStreamImpl(int track) {
            this.track = track;
        }

        @Override // com.google.android.exoplayer2.source.SampleStream
        public boolean isReady() {
            return ExtractorMediaPeriod.this.isReady(this.track);
        }

        @Override // com.google.android.exoplayer2.source.SampleStream
        public void maybeThrowError() throws IOException {
            ExtractorMediaPeriod.this.maybeThrowError();
        }

        @Override // com.google.android.exoplayer2.source.SampleStream
        public int readData(FormatHolder formatHolder, DecoderInputBuffer buffer, boolean formatRequired) {
            return ExtractorMediaPeriod.this.readData(this.track, formatHolder, buffer, formatRequired);
        }

        @Override // com.google.android.exoplayer2.source.SampleStream
        public void skipData(long positionUs) {
            ExtractorMediaPeriod.this.skipData(this.track, positionUs);
        }
    }

    final class ExtractingLoadable implements Loader.Loadable {
        private static final int CONTINUE_LOADING_CHECK_INTERVAL_BYTES = 1048576;
        private final DataSource dataSource;
        private final ExtractorHolder extractorHolder;
        private volatile boolean loadCanceled;
        private final ConditionVariable loadCondition;
        private long seekTimeUs;
        private final Uri uri;
        private final PositionHolder positionHolder = new PositionHolder();
        private boolean pendingExtractorSeek = true;
        private long length = -1;

        public ExtractingLoadable(Uri uri, DataSource dataSource, ExtractorHolder extractorHolder, ConditionVariable loadCondition) {
            this.uri = (Uri) Assertions.checkNotNull(uri);
            this.dataSource = (DataSource) Assertions.checkNotNull(dataSource);
            this.extractorHolder = (ExtractorHolder) Assertions.checkNotNull(extractorHolder);
            this.loadCondition = loadCondition;
        }

        public void setLoadPosition(long position, long timeUs) {
            this.positionHolder.position = position;
            this.seekTimeUs = timeUs;
            this.pendingExtractorSeek = true;
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
        public void load() throws Throwable {
            ExtractorInput input;
            int result = 0;
            while (result == 0 && !this.loadCanceled) {
                try {
                    long position = this.positionHolder.position;
                    this.length = this.dataSource.open(new DataSpec(this.uri, position, -1L, ExtractorMediaPeriod.this.customCacheKey));
                    if (this.length != -1) {
                        this.length += position;
                    }
                    input = new DefaultExtractorInput(this.dataSource, position, this.length);
                    try {
                        Extractor extractor = this.extractorHolder.selectExtractor(input, this.dataSource.getUri());
                        if (this.pendingExtractorSeek) {
                            extractor.seek(position, this.seekTimeUs);
                            this.pendingExtractorSeek = false;
                        }
                        while (result == 0 && !this.loadCanceled) {
                            this.loadCondition.block();
                            result = extractor.read(input, this.positionHolder);
                            if (input.getPosition() > FileUtils.ONE_MB + position) {
                                position = input.getPosition();
                                this.loadCondition.close();
                                ExtractorMediaPeriod.this.handler.post(ExtractorMediaPeriod.this.onContinueLoadingRequestedRunnable);
                            }
                        }
                        if (result == 1) {
                            result = 0;
                        } else if (input != null) {
                            this.positionHolder.position = input.getPosition();
                        }
                        Util.closeQuietly(this.dataSource);
                    } catch (Throwable th) {
                        th = th;
                        if (result != 1 && input != null) {
                            this.positionHolder.position = input.getPosition();
                        }
                        Util.closeQuietly(this.dataSource);
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    input = null;
                }
            }
        }
    }

    private static final class ExtractorHolder {
        private Extractor extractor;
        private final ExtractorOutput extractorOutput;
        private final Extractor[] extractors;

        public ExtractorHolder(Extractor[] extractors, ExtractorOutput extractorOutput) {
            this.extractors = extractors;
            this.extractorOutput = extractorOutput;
        }

        public Extractor selectExtractor(ExtractorInput input, Uri uri) throws InterruptedException, IOException {
            if (this.extractor != null) {
                return this.extractor;
            }
            Extractor[] extractorArr = this.extractors;
            int length = extractorArr.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                Extractor extractor = extractorArr[i];
                try {
                } catch (EOFException e) {
                } finally {
                    input.resetPeekPosition();
                }
                if (!extractor.sniff(input)) {
                    i++;
                } else {
                    this.extractor = extractor;
                    break;
                }
            }
            if (this.extractor == null) {
                throw new UnrecognizedInputFormatException("None of the available extractors (" + Util.getCommaDelimitedSimpleClassNames(this.extractors) + ") could read the stream.", uri);
            }
            this.extractor.init(this.extractorOutput);
            return this.extractor;
        }

        public void release() {
            if (this.extractor != null) {
                this.extractor.release();
                this.extractor = null;
            }
        }
    }
}
