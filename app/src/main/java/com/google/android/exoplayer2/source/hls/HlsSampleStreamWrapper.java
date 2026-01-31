package com.google.android.exoplayer2.source.hls;

import android.os.Handler;
import android.text.TextUtils;
import android.util.SparseArray;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.extractor.DefaultTrackOutput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.source.SampleStream;
import com.google.android.exoplayer2.source.SequenceableLoader;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.chunk.Chunk;
import com.google.android.exoplayer2.source.hls.HlsChunkSource;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.MimeTypes;
import java.io.IOException;
import java.util.LinkedList;

/* loaded from: classes.dex */
final class HlsSampleStreamWrapper implements Loader.Callback<Chunk>, SequenceableLoader, ExtractorOutput, DefaultTrackOutput.UpstreamFormatChangedListener {
    private static final int PRIMARY_TYPE_AUDIO = 2;
    private static final int PRIMARY_TYPE_NONE = 0;
    private static final int PRIMARY_TYPE_TEXT = 1;
    private static final int PRIMARY_TYPE_VIDEO = 3;
    private final Allocator allocator;
    private final Callback callback;
    private final HlsChunkSource chunkSource;
    private Format downstreamTrackFormat;
    private int enabledTrackCount;
    private final AdaptiveMediaSourceEventListener.EventDispatcher eventDispatcher;
    private boolean[] groupEnabledStates;
    private long lastSeekPositionUs;
    private boolean loadingFinished;
    private final int minLoadableRetryCount;
    private final Format muxedAudioFormat;
    private long pendingResetPositionUs;
    private boolean prepared;
    private int primaryTrackGroupIndex;
    private boolean released;
    private boolean sampleQueuesBuilt;
    private TrackGroupArray trackGroups;
    private final int trackType;
    private int upstreamChunkUid;
    private final Loader loader = new Loader("Loader:HlsSampleStreamWrapper");
    private final HlsChunkSource.HlsChunkHolder nextChunkHolder = new HlsChunkSource.HlsChunkHolder();
    private final SparseArray<DefaultTrackOutput> sampleQueues = new SparseArray<>();
    private final LinkedList<HlsMediaChunk> mediaChunks = new LinkedList<>();
    private final Runnable maybeFinishPrepareRunnable = new Runnable() { // from class: com.google.android.exoplayer2.source.hls.HlsSampleStreamWrapper.1
        @Override // java.lang.Runnable
        public void run() {
            HlsSampleStreamWrapper.this.maybeFinishPrepare();
        }
    };
    private final Handler handler = new Handler();

    public interface Callback extends SequenceableLoader.Callback<HlsSampleStreamWrapper> {
        void onPlaylistRefreshRequired(HlsMasterPlaylist.HlsUrl hlsUrl);

        void onPrepared();
    }

    public HlsSampleStreamWrapper(int trackType, Callback callback, HlsChunkSource chunkSource, Allocator allocator, long positionUs, Format muxedAudioFormat, int minLoadableRetryCount, AdaptiveMediaSourceEventListener.EventDispatcher eventDispatcher) {
        this.trackType = trackType;
        this.callback = callback;
        this.chunkSource = chunkSource;
        this.allocator = allocator;
        this.muxedAudioFormat = muxedAudioFormat;
        this.minLoadableRetryCount = minLoadableRetryCount;
        this.eventDispatcher = eventDispatcher;
        this.lastSeekPositionUs = positionUs;
        this.pendingResetPositionUs = positionUs;
    }

    public void continuePreparing() {
        if (!this.prepared) {
            continueLoading(this.lastSeekPositionUs);
        }
    }

    public void prepareSingleTrack(Format format) {
        track(0, -1).format(format);
        this.sampleQueuesBuilt = true;
        maybeFinishPrepare();
    }

    public void maybeThrowPrepareError() throws IOException {
        maybeThrowError();
    }

    public TrackGroupArray getTrackGroups() {
        return this.trackGroups;
    }

    public boolean selectTracks(TrackSelection[] selections, boolean[] mayRetainStreamFlags, SampleStream[] streams, boolean[] streamResetFlags, boolean isFirstTrackSelection) {
        Assertions.checkState(this.prepared);
        for (int i = 0; i < selections.length; i++) {
            if (streams[i] != null && (selections[i] == null || !mayRetainStreamFlags[i])) {
                int group = ((HlsSampleStream) streams[i]).group;
                setTrackGroupEnabledState(group, false);
                this.sampleQueues.valueAt(group).disable();
                streams[i] = null;
            }
        }
        TrackSelection primaryTrackSelection = null;
        boolean selectedNewTracks = false;
        for (int i2 = 0; i2 < selections.length; i2++) {
            if (streams[i2] == null && selections[i2] != null) {
                TrackSelection selection = selections[i2];
                int group2 = this.trackGroups.indexOf(selection.getTrackGroup());
                setTrackGroupEnabledState(group2, true);
                if (group2 == this.primaryTrackGroupIndex) {
                    primaryTrackSelection = selection;
                    this.chunkSource.selectTracks(selection);
                }
                streams[i2] = new HlsSampleStream(this, group2);
                streamResetFlags[i2] = true;
                selectedNewTracks = true;
            }
        }
        if (isFirstTrackSelection) {
            int sampleQueueCount = this.sampleQueues.size();
            for (int i3 = 0; i3 < sampleQueueCount; i3++) {
                if (!this.groupEnabledStates[i3]) {
                    this.sampleQueues.valueAt(i3).disable();
                }
            }
            if (primaryTrackSelection != null && !this.mediaChunks.isEmpty()) {
                primaryTrackSelection.updateSelectedTrack(0L);
                int chunkIndex = this.chunkSource.getTrackGroup().indexOf(this.mediaChunks.getLast().trackFormat);
                if (primaryTrackSelection.getSelectedIndexInTrackGroup() != chunkIndex) {
                    seekTo(this.lastSeekPositionUs);
                }
            }
        }
        if (this.enabledTrackCount == 0) {
            this.chunkSource.reset();
            this.downstreamTrackFormat = null;
            this.mediaChunks.clear();
            if (this.loader.isLoading()) {
                this.loader.cancelLoading();
            }
        }
        return selectedNewTracks;
    }

    public void seekTo(long positionUs) {
        this.lastSeekPositionUs = positionUs;
        this.pendingResetPositionUs = positionUs;
        this.loadingFinished = false;
        this.mediaChunks.clear();
        if (this.loader.isLoading()) {
            this.loader.cancelLoading();
            return;
        }
        int sampleQueueCount = this.sampleQueues.size();
        for (int i = 0; i < sampleQueueCount; i++) {
            this.sampleQueues.valueAt(i).reset(this.groupEnabledStates[i]);
        }
    }

    public long getBufferedPositionUs() {
        HlsMediaChunk lastCompletedMediaChunk;
        if (this.loadingFinished) {
            return Long.MIN_VALUE;
        }
        if (isPendingReset()) {
            return this.pendingResetPositionUs;
        }
        long bufferedPositionUs = this.lastSeekPositionUs;
        HlsMediaChunk lastMediaChunk = this.mediaChunks.getLast();
        if (lastMediaChunk.isLoadCompleted()) {
            lastCompletedMediaChunk = lastMediaChunk;
        } else {
            lastCompletedMediaChunk = this.mediaChunks.size() > 1 ? this.mediaChunks.get(this.mediaChunks.size() - 2) : null;
        }
        if (lastCompletedMediaChunk != null) {
            bufferedPositionUs = Math.max(bufferedPositionUs, lastCompletedMediaChunk.endTimeUs);
        }
        int sampleQueueCount = this.sampleQueues.size();
        for (int i = 0; i < sampleQueueCount; i++) {
            bufferedPositionUs = Math.max(bufferedPositionUs, this.sampleQueues.valueAt(i).getLargestQueuedTimestampUs());
        }
        return bufferedPositionUs;
    }

    public void release() {
        int sampleQueueCount = this.sampleQueues.size();
        for (int i = 0; i < sampleQueueCount; i++) {
            this.sampleQueues.valueAt(i).disable();
        }
        this.loader.release();
        this.handler.removeCallbacksAndMessages(null);
        this.released = true;
    }

    public void setIsTimestampMaster(boolean isTimestampMaster) {
        this.chunkSource.setIsTimestampMaster(isTimestampMaster);
    }

    public void onPlaylistBlacklisted(HlsMasterPlaylist.HlsUrl url, long blacklistMs) {
        this.chunkSource.onPlaylistBlacklisted(url, blacklistMs);
    }

    boolean isReady(int group) {
        return this.loadingFinished || !(isPendingReset() || this.sampleQueues.valueAt(group).isEmpty());
    }

    void maybeThrowError() throws IOException {
        this.loader.maybeThrowError();
        this.chunkSource.maybeThrowError();
    }

    int readData(int group, FormatHolder formatHolder, DecoderInputBuffer buffer, boolean requireFormat) {
        if (isPendingReset()) {
            return -3;
        }
        while (this.mediaChunks.size() > 1 && finishedReadingChunk(this.mediaChunks.getFirst())) {
            this.mediaChunks.removeFirst();
        }
        HlsMediaChunk currentChunk = this.mediaChunks.getFirst();
        Format trackFormat = currentChunk.trackFormat;
        if (!trackFormat.equals(this.downstreamTrackFormat)) {
            this.eventDispatcher.downstreamFormatChanged(this.trackType, trackFormat, currentChunk.trackSelectionReason, currentChunk.trackSelectionData, currentChunk.startTimeUs);
        }
        this.downstreamTrackFormat = trackFormat;
        return this.sampleQueues.valueAt(group).readData(formatHolder, buffer, requireFormat, this.loadingFinished, this.lastSeekPositionUs);
    }

    void skipData(int group, long positionUs) {
        DefaultTrackOutput sampleQueue = this.sampleQueues.valueAt(group);
        if (this.loadingFinished && positionUs > sampleQueue.getLargestQueuedTimestampUs()) {
            sampleQueue.skipAll();
        } else {
            sampleQueue.skipToKeyframeBefore(positionUs, true);
        }
    }

    private boolean finishedReadingChunk(HlsMediaChunk chunk) {
        int chunkUid = chunk.uid;
        for (int i = 0; i < this.sampleQueues.size(); i++) {
            if (this.groupEnabledStates[i] && this.sampleQueues.valueAt(i).peekSourceId() == chunkUid) {
                return false;
            }
        }
        return true;
    }

    @Override // com.google.android.exoplayer2.source.SequenceableLoader
    public boolean continueLoading(long positionUs) {
        if (this.loadingFinished || this.loader.isLoading()) {
            return false;
        }
        HlsChunkSource hlsChunkSource = this.chunkSource;
        HlsMediaChunk last = this.mediaChunks.isEmpty() ? null : this.mediaChunks.getLast();
        if (this.pendingResetPositionUs != C.TIME_UNSET) {
            positionUs = this.pendingResetPositionUs;
        }
        hlsChunkSource.getNextChunk(last, positionUs, this.nextChunkHolder);
        boolean endOfStream = this.nextChunkHolder.endOfStream;
        Chunk loadable = this.nextChunkHolder.chunk;
        HlsMasterPlaylist.HlsUrl playlistToLoad = this.nextChunkHolder.playlist;
        this.nextChunkHolder.clear();
        if (endOfStream) {
            this.loadingFinished = true;
            return true;
        }
        if (loadable == null) {
            if (playlistToLoad != null) {
                this.callback.onPlaylistRefreshRequired(playlistToLoad);
            }
            return false;
        }
        if (isMediaChunk(loadable)) {
            this.pendingResetPositionUs = C.TIME_UNSET;
            HlsMediaChunk mediaChunk = (HlsMediaChunk) loadable;
            mediaChunk.init(this);
            this.mediaChunks.add(mediaChunk);
        }
        long elapsedRealtimeMs = this.loader.startLoading(loadable, this, this.minLoadableRetryCount);
        this.eventDispatcher.loadStarted(loadable.dataSpec, loadable.type, this.trackType, loadable.trackFormat, loadable.trackSelectionReason, loadable.trackSelectionData, loadable.startTimeUs, loadable.endTimeUs, elapsedRealtimeMs);
        return true;
    }

    @Override // com.google.android.exoplayer2.source.SequenceableLoader
    public long getNextLoadPositionUs() {
        if (isPendingReset()) {
            return this.pendingResetPositionUs;
        }
        if (this.loadingFinished) {
            return Long.MIN_VALUE;
        }
        return this.mediaChunks.getLast().endTimeUs;
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.Callback
    public void onLoadCompleted(Chunk loadable, long elapsedRealtimeMs, long loadDurationMs) {
        this.chunkSource.onChunkLoadCompleted(loadable);
        this.eventDispatcher.loadCompleted(loadable.dataSpec, loadable.type, this.trackType, loadable.trackFormat, loadable.trackSelectionReason, loadable.trackSelectionData, loadable.startTimeUs, loadable.endTimeUs, elapsedRealtimeMs, loadDurationMs, loadable.bytesLoaded());
        if (!this.prepared) {
            continueLoading(this.lastSeekPositionUs);
        } else {
            this.callback.onContinueLoadingRequested(this);
        }
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.Callback
    public void onLoadCanceled(Chunk loadable, long elapsedRealtimeMs, long loadDurationMs, boolean released) {
        this.eventDispatcher.loadCanceled(loadable.dataSpec, loadable.type, this.trackType, loadable.trackFormat, loadable.trackSelectionReason, loadable.trackSelectionData, loadable.startTimeUs, loadable.endTimeUs, elapsedRealtimeMs, loadDurationMs, loadable.bytesLoaded());
        if (!released) {
            int sampleQueueCount = this.sampleQueues.size();
            for (int i = 0; i < sampleQueueCount; i++) {
                this.sampleQueues.valueAt(i).reset(this.groupEnabledStates[i]);
            }
            this.callback.onContinueLoadingRequested(this);
        }
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.Callback
    public int onLoadError(Chunk loadable, long elapsedRealtimeMs, long loadDurationMs, IOException error) {
        long bytesLoaded = loadable.bytesLoaded();
        boolean isMediaChunk = isMediaChunk(loadable);
        boolean cancelable = !isMediaChunk || bytesLoaded == 0;
        boolean canceled = false;
        if (this.chunkSource.onChunkLoadError(loadable, cancelable, error)) {
            if (isMediaChunk) {
                HlsMediaChunk removed = this.mediaChunks.removeLast();
                Assertions.checkState(removed == loadable);
                if (this.mediaChunks.isEmpty()) {
                    this.pendingResetPositionUs = this.lastSeekPositionUs;
                }
            }
            canceled = true;
        }
        this.eventDispatcher.loadError(loadable.dataSpec, loadable.type, this.trackType, loadable.trackFormat, loadable.trackSelectionReason, loadable.trackSelectionData, loadable.startTimeUs, loadable.endTimeUs, elapsedRealtimeMs, loadDurationMs, loadable.bytesLoaded(), error, canceled);
        if (canceled) {
            if (!this.prepared) {
                continueLoading(this.lastSeekPositionUs);
            } else {
                this.callback.onContinueLoadingRequested(this);
            }
            return 2;
        }
        return 0;
    }

    public void init(int chunkUid, boolean shouldSpliceIn) {
        this.upstreamChunkUid = chunkUid;
        for (int i = 0; i < this.sampleQueues.size(); i++) {
            this.sampleQueues.valueAt(i).sourceId(chunkUid);
        }
        if (shouldSpliceIn) {
            for (int i2 = 0; i2 < this.sampleQueues.size(); i2++) {
                this.sampleQueues.valueAt(i2).splice();
            }
        }
    }

    @Override // com.google.android.exoplayer2.extractor.ExtractorOutput
    public DefaultTrackOutput track(int id, int type) {
        if (this.sampleQueues.indexOfKey(id) >= 0) {
            return this.sampleQueues.get(id);
        }
        DefaultTrackOutput trackOutput = new DefaultTrackOutput(this.allocator);
        trackOutput.setUpstreamFormatChangeListener(this);
        trackOutput.sourceId(this.upstreamChunkUid);
        this.sampleQueues.put(id, trackOutput);
        return trackOutput;
    }

    @Override // com.google.android.exoplayer2.extractor.ExtractorOutput
    public void endTracks() {
        this.sampleQueuesBuilt = true;
        this.handler.post(this.maybeFinishPrepareRunnable);
    }

    @Override // com.google.android.exoplayer2.extractor.ExtractorOutput
    public void seekMap(SeekMap seekMap) {
    }

    @Override // com.google.android.exoplayer2.extractor.DefaultTrackOutput.UpstreamFormatChangedListener
    public void onUpstreamFormatChanged(Format format) {
        this.handler.post(this.maybeFinishPrepareRunnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybeFinishPrepare() {
        if (!this.released && !this.prepared && this.sampleQueuesBuilt) {
            int sampleQueueCount = this.sampleQueues.size();
            for (int i = 0; i < sampleQueueCount; i++) {
                if (this.sampleQueues.valueAt(i).getUpstreamFormat() == null) {
                    return;
                }
            }
            buildTracks();
            this.prepared = true;
            this.callback.onPrepared();
        }
    }

    private void buildTracks() {
        int trackType;
        int primaryExtractorTrackType = 0;
        int primaryExtractorTrackIndex = -1;
        int extractorTrackCount = this.sampleQueues.size();
        for (int i = 0; i < extractorTrackCount; i++) {
            String sampleMimeType = this.sampleQueues.valueAt(i).getUpstreamFormat().sampleMimeType;
            if (MimeTypes.isVideo(sampleMimeType)) {
                trackType = 3;
            } else if (MimeTypes.isAudio(sampleMimeType)) {
                trackType = 2;
            } else if (MimeTypes.isText(sampleMimeType)) {
                trackType = 1;
            } else {
                trackType = 0;
            }
            if (trackType > primaryExtractorTrackType) {
                primaryExtractorTrackType = trackType;
                primaryExtractorTrackIndex = i;
            } else if (trackType == primaryExtractorTrackType && primaryExtractorTrackIndex != -1) {
                primaryExtractorTrackIndex = -1;
            }
        }
        TrackGroup chunkSourceTrackGroup = this.chunkSource.getTrackGroup();
        int chunkSourceTrackCount = chunkSourceTrackGroup.length;
        this.primaryTrackGroupIndex = -1;
        this.groupEnabledStates = new boolean[extractorTrackCount];
        TrackGroup[] trackGroups = new TrackGroup[extractorTrackCount];
        for (int i2 = 0; i2 < extractorTrackCount; i2++) {
            Format sampleFormat = this.sampleQueues.valueAt(i2).getUpstreamFormat();
            if (i2 == primaryExtractorTrackIndex) {
                Format[] formats = new Format[chunkSourceTrackCount];
                for (int j = 0; j < chunkSourceTrackCount; j++) {
                    formats[j] = deriveFormat(chunkSourceTrackGroup.getFormat(j), sampleFormat);
                }
                trackGroups[i2] = new TrackGroup(formats);
                this.primaryTrackGroupIndex = i2;
            } else {
                Format trackFormat = (primaryExtractorTrackType == 3 && MimeTypes.isAudio(sampleFormat.sampleMimeType)) ? this.muxedAudioFormat : null;
                trackGroups[i2] = new TrackGroup(deriveFormat(trackFormat, sampleFormat));
            }
        }
        this.trackGroups = new TrackGroupArray(trackGroups);
    }

    private void setTrackGroupEnabledState(int group, boolean enabledState) {
        Assertions.checkState(this.groupEnabledStates[group] != enabledState);
        this.groupEnabledStates[group] = enabledState;
        this.enabledTrackCount += enabledState ? 1 : -1;
    }

    private static Format deriveFormat(Format containerFormat, Format sampleFormat) {
        if (containerFormat != null) {
            String codecs = null;
            int sampleTrackType = MimeTypes.getTrackType(sampleFormat.sampleMimeType);
            if (sampleTrackType == 1) {
                codecs = getAudioCodecs(containerFormat.codecs);
            } else if (sampleTrackType == 2) {
                codecs = getVideoCodecs(containerFormat.codecs);
            }
            return sampleFormat.copyWithContainerInfo(containerFormat.id, codecs, containerFormat.bitrate, containerFormat.width, containerFormat.height, containerFormat.selectionFlags, containerFormat.language);
        }
        return sampleFormat;
    }

    private boolean isMediaChunk(Chunk chunk) {
        return chunk instanceof HlsMediaChunk;
    }

    private boolean isPendingReset() {
        return this.pendingResetPositionUs != C.TIME_UNSET;
    }

    private static String getAudioCodecs(String codecs) {
        return getCodecsOfType(codecs, 1);
    }

    private static String getVideoCodecs(String codecs) {
        return getCodecsOfType(codecs, 2);
    }

    private static String getCodecsOfType(String codecs, int trackType) {
        if (TextUtils.isEmpty(codecs)) {
            return null;
        }
        String[] codecArray = codecs.split("(\\s*,\\s*)|(\\s*$)");
        StringBuilder builder = new StringBuilder();
        for (String codec : codecArray) {
            if (trackType == MimeTypes.getTrackTypeOfCodec(codec)) {
                if (builder.length() > 0) {
                    builder.append(",");
                }
                builder.append(codec);
            }
        }
        if (builder.length() > 0) {
            return builder.toString();
        }
        return null;
    }
}
