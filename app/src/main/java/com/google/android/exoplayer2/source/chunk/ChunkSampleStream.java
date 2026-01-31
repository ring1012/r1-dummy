package com.google.android.exoplayer2.source.chunk;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.extractor.DefaultTrackOutput;
import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.source.SampleStream;
import com.google.android.exoplayer2.source.SequenceableLoader;
import com.google.android.exoplayer2.source.chunk.ChunkSource;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes.dex */
public class ChunkSampleStream<T extends ChunkSource> implements SampleStream, SequenceableLoader, Loader.Callback<Chunk> {
    private final SequenceableLoader.Callback<ChunkSampleStream<T>> callback;
    private final T chunkSource;
    private final DefaultTrackOutput[] embeddedSampleQueues;
    private final int[] embeddedTrackTypes;
    private final boolean[] embeddedTracksSelected;
    private final AdaptiveMediaSourceEventListener.EventDispatcher eventDispatcher;
    long lastSeekPositionUs;
    boolean loadingFinished;
    private final BaseMediaChunkOutput mediaChunkOutput;
    private final int minLoadableRetryCount;
    private long pendingResetPositionUs;
    private Format primaryDownstreamTrackFormat;
    private final DefaultTrackOutput primarySampleQueue;
    private final int primaryTrackType;
    private final Loader loader = new Loader("Loader:ChunkSampleStream");
    private final ChunkHolder nextChunkHolder = new ChunkHolder();
    private final LinkedList<BaseMediaChunk> mediaChunks = new LinkedList<>();
    private final List<BaseMediaChunk> readOnlyMediaChunks = Collections.unmodifiableList(this.mediaChunks);

    public ChunkSampleStream(int primaryTrackType, int[] embeddedTrackTypes, T chunkSource, SequenceableLoader.Callback<ChunkSampleStream<T>> callback, Allocator allocator, long positionUs, int minLoadableRetryCount, AdaptiveMediaSourceEventListener.EventDispatcher eventDispatcher) {
        this.primaryTrackType = primaryTrackType;
        this.embeddedTrackTypes = embeddedTrackTypes;
        this.chunkSource = chunkSource;
        this.callback = callback;
        this.eventDispatcher = eventDispatcher;
        this.minLoadableRetryCount = minLoadableRetryCount;
        int embeddedTrackCount = embeddedTrackTypes == null ? 0 : embeddedTrackTypes.length;
        this.embeddedSampleQueues = new DefaultTrackOutput[embeddedTrackCount];
        this.embeddedTracksSelected = new boolean[embeddedTrackCount];
        int[] trackTypes = new int[embeddedTrackCount + 1];
        DefaultTrackOutput[] sampleQueues = new DefaultTrackOutput[embeddedTrackCount + 1];
        this.primarySampleQueue = new DefaultTrackOutput(allocator);
        trackTypes[0] = primaryTrackType;
        sampleQueues[0] = this.primarySampleQueue;
        for (int i = 0; i < embeddedTrackCount; i++) {
            DefaultTrackOutput trackOutput = new DefaultTrackOutput(allocator);
            this.embeddedSampleQueues[i] = trackOutput;
            sampleQueues[i + 1] = trackOutput;
            trackTypes[i + 1] = embeddedTrackTypes[i];
        }
        this.mediaChunkOutput = new BaseMediaChunkOutput(trackTypes, sampleQueues);
        this.pendingResetPositionUs = positionUs;
        this.lastSeekPositionUs = positionUs;
    }

    public void discardUnselectedEmbeddedTracksTo(long positionUs) {
        for (int i = 0; i < this.embeddedSampleQueues.length; i++) {
            if (!this.embeddedTracksSelected[i]) {
                this.embeddedSampleQueues[i].skipToKeyframeBefore(positionUs, true);
            }
        }
    }

    public ChunkSampleStream<T>.EmbeddedSampleStream selectEmbeddedTrack(long positionUs, int trackType) {
        for (int i = 0; i < this.embeddedSampleQueues.length; i++) {
            if (this.embeddedTrackTypes[i] == trackType) {
                Assertions.checkState(!this.embeddedTracksSelected[i]);
                this.embeddedTracksSelected[i] = true;
                this.embeddedSampleQueues[i].skipToKeyframeBefore(positionUs, true);
                return new EmbeddedSampleStream(this, this.embeddedSampleQueues[i], i);
            }
        }
        throw new IllegalStateException();
    }

    public T getChunkSource() {
        return this.chunkSource;
    }

    public long getBufferedPositionUs() {
        BaseMediaChunk lastCompletedMediaChunk;
        if (this.loadingFinished) {
            return Long.MIN_VALUE;
        }
        if (isPendingReset()) {
            return this.pendingResetPositionUs;
        }
        long bufferedPositionUs = this.lastSeekPositionUs;
        BaseMediaChunk lastMediaChunk = this.mediaChunks.getLast();
        if (lastMediaChunk.isLoadCompleted()) {
            lastCompletedMediaChunk = lastMediaChunk;
        } else {
            lastCompletedMediaChunk = this.mediaChunks.size() > 1 ? this.mediaChunks.get(this.mediaChunks.size() - 2) : null;
        }
        if (lastCompletedMediaChunk != null) {
            bufferedPositionUs = Math.max(bufferedPositionUs, lastCompletedMediaChunk.endTimeUs);
        }
        return Math.max(bufferedPositionUs, this.primarySampleQueue.getLargestQueuedTimestampUs());
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0042  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void seekToUs(long r10) {
        /*
            r9 = this;
            r4 = 0
            r3 = 1
            r9.lastSeekPositionUs = r10
            boolean r2 = r9.isPendingReset()
            if (r2 != 0) goto L42
            com.google.android.exoplayer2.extractor.DefaultTrackOutput r5 = r9.primarySampleQueue
            long r6 = r9.getNextLoadPositionUs()
            int r2 = (r10 > r6 ? 1 : (r10 == r6 ? 0 : -1))
            if (r2 >= 0) goto L40
            r2 = r3
        L15:
            boolean r2 = r5.skipToKeyframeBefore(r10, r2)
            if (r2 == 0) goto L42
            r1 = r3
        L1c:
            if (r1 == 0) goto L51
        L1e:
            java.util.LinkedList<com.google.android.exoplayer2.source.chunk.BaseMediaChunk> r2 = r9.mediaChunks
            int r2 = r2.size()
            if (r2 <= r3) goto L44
            java.util.LinkedList<com.google.android.exoplayer2.source.chunk.BaseMediaChunk> r2 = r9.mediaChunks
            java.lang.Object r2 = r2.get(r3)
            com.google.android.exoplayer2.source.chunk.BaseMediaChunk r2 = (com.google.android.exoplayer2.source.chunk.BaseMediaChunk) r2
            int r2 = r2.getFirstSampleIndex(r4)
            com.google.android.exoplayer2.extractor.DefaultTrackOutput r5 = r9.primarySampleQueue
            int r5 = r5.getReadIndex()
            if (r2 > r5) goto L44
            java.util.LinkedList<com.google.android.exoplayer2.source.chunk.BaseMediaChunk> r2 = r9.mediaChunks
            r2.removeFirst()
            goto L1e
        L40:
            r2 = r4
            goto L15
        L42:
            r1 = r4
            goto L1c
        L44:
            com.google.android.exoplayer2.extractor.DefaultTrackOutput[] r2 = r9.embeddedSampleQueues
            int r5 = r2.length
        L47:
            if (r4 >= r5) goto L67
            r0 = r2[r4]
            r0.skipToKeyframeBefore(r10, r3)
            int r4 = r4 + 1
            goto L47
        L51:
            r9.pendingResetPositionUs = r10
            r9.loadingFinished = r4
            java.util.LinkedList<com.google.android.exoplayer2.source.chunk.BaseMediaChunk> r2 = r9.mediaChunks
            r2.clear()
            com.google.android.exoplayer2.upstream.Loader r2 = r9.loader
            boolean r2 = r2.isLoading()
            if (r2 == 0) goto L68
            com.google.android.exoplayer2.upstream.Loader r2 = r9.loader
            r2.cancelLoading()
        L67:
            return
        L68:
            com.google.android.exoplayer2.extractor.DefaultTrackOutput r2 = r9.primarySampleQueue
            r2.reset(r3)
            com.google.android.exoplayer2.extractor.DefaultTrackOutput[] r2 = r9.embeddedSampleQueues
            int r5 = r2.length
        L70:
            if (r4 >= r5) goto L67
            r0 = r2[r4]
            r0.reset(r3)
            int r4 = r4 + 1
            goto L70
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.chunk.ChunkSampleStream.seekToUs(long):void");
    }

    public void release() {
        this.primarySampleQueue.disable();
        for (DefaultTrackOutput embeddedSampleQueue : this.embeddedSampleQueues) {
            embeddedSampleQueue.disable();
        }
        this.loader.release();
    }

    @Override // com.google.android.exoplayer2.source.SampleStream
    public boolean isReady() {
        return this.loadingFinished || !(isPendingReset() || this.primarySampleQueue.isEmpty());
    }

    @Override // com.google.android.exoplayer2.source.SampleStream
    public void maybeThrowError() throws IOException {
        this.loader.maybeThrowError();
        if (!this.loader.isLoading()) {
            this.chunkSource.maybeThrowError();
        }
    }

    @Override // com.google.android.exoplayer2.source.SampleStream
    public int readData(FormatHolder formatHolder, DecoderInputBuffer buffer, boolean formatRequired) {
        if (isPendingReset()) {
            return -3;
        }
        discardDownstreamMediaChunks(this.primarySampleQueue.getReadIndex());
        return this.primarySampleQueue.readData(formatHolder, buffer, formatRequired, this.loadingFinished, this.lastSeekPositionUs);
    }

    @Override // com.google.android.exoplayer2.source.SampleStream
    public void skipData(long positionUs) {
        if (this.loadingFinished && positionUs > this.primarySampleQueue.getLargestQueuedTimestampUs()) {
            this.primarySampleQueue.skipAll();
        } else {
            this.primarySampleQueue.skipToKeyframeBefore(positionUs, true);
        }
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.Callback
    public void onLoadCompleted(Chunk loadable, long elapsedRealtimeMs, long loadDurationMs) {
        this.chunkSource.onChunkLoadCompleted(loadable);
        this.eventDispatcher.loadCompleted(loadable.dataSpec, loadable.type, this.primaryTrackType, loadable.trackFormat, loadable.trackSelectionReason, loadable.trackSelectionData, loadable.startTimeUs, loadable.endTimeUs, elapsedRealtimeMs, loadDurationMs, loadable.bytesLoaded());
        this.callback.onContinueLoadingRequested(this);
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.Callback
    public void onLoadCanceled(Chunk loadable, long elapsedRealtimeMs, long loadDurationMs, boolean released) {
        this.eventDispatcher.loadCanceled(loadable.dataSpec, loadable.type, this.primaryTrackType, loadable.trackFormat, loadable.trackSelectionReason, loadable.trackSelectionData, loadable.startTimeUs, loadable.endTimeUs, elapsedRealtimeMs, loadDurationMs, loadable.bytesLoaded());
        if (!released) {
            this.primarySampleQueue.reset(true);
            for (DefaultTrackOutput embeddedSampleQueue : this.embeddedSampleQueues) {
                embeddedSampleQueue.reset(true);
            }
            this.callback.onContinueLoadingRequested(this);
        }
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.Callback
    public int onLoadError(Chunk loadable, long elapsedRealtimeMs, long loadDurationMs, IOException error) {
        long bytesLoaded = loadable.bytesLoaded();
        boolean isMediaChunk = isMediaChunk(loadable);
        boolean cancelable = !isMediaChunk || bytesLoaded == 0 || this.mediaChunks.size() > 1;
        boolean canceled = false;
        if (this.chunkSource.onChunkLoadError(loadable, cancelable, error)) {
            canceled = true;
            if (isMediaChunk) {
                BaseMediaChunk removed = this.mediaChunks.removeLast();
                Assertions.checkState(removed == loadable);
                this.primarySampleQueue.discardUpstreamSamples(removed.getFirstSampleIndex(0));
                for (int i = 0; i < this.embeddedSampleQueues.length; i++) {
                    this.embeddedSampleQueues[i].discardUpstreamSamples(removed.getFirstSampleIndex(i + 1));
                }
                if (this.mediaChunks.isEmpty()) {
                    this.pendingResetPositionUs = this.lastSeekPositionUs;
                }
            }
        }
        this.eventDispatcher.loadError(loadable.dataSpec, loadable.type, this.primaryTrackType, loadable.trackFormat, loadable.trackSelectionReason, loadable.trackSelectionData, loadable.startTimeUs, loadable.endTimeUs, elapsedRealtimeMs, loadDurationMs, bytesLoaded, error, canceled);
        if (!canceled) {
            return 0;
        }
        this.callback.onContinueLoadingRequested(this);
        return 2;
    }

    @Override // com.google.android.exoplayer2.source.SequenceableLoader
    public boolean continueLoading(long positionUs) {
        if (this.loadingFinished || this.loader.isLoading()) {
            return false;
        }
        T t = this.chunkSource;
        BaseMediaChunk last = this.mediaChunks.isEmpty() ? null : this.mediaChunks.getLast();
        if (this.pendingResetPositionUs != C.TIME_UNSET) {
            positionUs = this.pendingResetPositionUs;
        }
        t.getNextChunk(last, positionUs, this.nextChunkHolder);
        boolean endOfStream = this.nextChunkHolder.endOfStream;
        Chunk loadable = this.nextChunkHolder.chunk;
        this.nextChunkHolder.clear();
        if (endOfStream) {
            this.loadingFinished = true;
            return true;
        }
        if (loadable == null) {
            return false;
        }
        if (isMediaChunk(loadable)) {
            this.pendingResetPositionUs = C.TIME_UNSET;
            BaseMediaChunk mediaChunk = (BaseMediaChunk) loadable;
            mediaChunk.init(this.mediaChunkOutput);
            this.mediaChunks.add(mediaChunk);
        }
        long elapsedRealtimeMs = this.loader.startLoading(loadable, this, this.minLoadableRetryCount);
        this.eventDispatcher.loadStarted(loadable.dataSpec, loadable.type, this.primaryTrackType, loadable.trackFormat, loadable.trackSelectionReason, loadable.trackSelectionData, loadable.startTimeUs, loadable.endTimeUs, elapsedRealtimeMs);
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

    private void maybeDiscardUpstream(long positionUs) {
        int queueSize = this.chunkSource.getPreferredQueueSize(positionUs, this.readOnlyMediaChunks);
        discardUpstreamMediaChunks(Math.max(1, queueSize));
    }

    private boolean isMediaChunk(Chunk chunk) {
        return chunk instanceof BaseMediaChunk;
    }

    boolean isPendingReset() {
        return this.pendingResetPositionUs != C.TIME_UNSET;
    }

    private void discardDownstreamMediaChunks(int primaryStreamReadIndex) {
        while (this.mediaChunks.size() > 1 && this.mediaChunks.get(1).getFirstSampleIndex(0) <= primaryStreamReadIndex) {
            this.mediaChunks.removeFirst();
        }
        BaseMediaChunk currentChunk = this.mediaChunks.getFirst();
        Format trackFormat = currentChunk.trackFormat;
        if (!trackFormat.equals(this.primaryDownstreamTrackFormat)) {
            this.eventDispatcher.downstreamFormatChanged(this.primaryTrackType, trackFormat, currentChunk.trackSelectionReason, currentChunk.trackSelectionData, currentChunk.startTimeUs);
        }
        this.primaryDownstreamTrackFormat = trackFormat;
    }

    private boolean discardUpstreamMediaChunks(int queueLength) {
        if (this.mediaChunks.size() <= queueLength) {
            return false;
        }
        long startTimeUs = 0;
        long endTimeUs = this.mediaChunks.getLast().endTimeUs;
        BaseMediaChunk removed = null;
        while (this.mediaChunks.size() > queueLength) {
            BaseMediaChunk removed2 = this.mediaChunks.removeLast();
            removed = removed2;
            startTimeUs = removed.startTimeUs;
            this.loadingFinished = false;
        }
        this.primarySampleQueue.discardUpstreamSamples(removed.getFirstSampleIndex(0));
        for (int i = 0; i < this.embeddedSampleQueues.length; i++) {
            this.embeddedSampleQueues[i].discardUpstreamSamples(removed.getFirstSampleIndex(i + 1));
        }
        this.eventDispatcher.upstreamDiscarded(this.primaryTrackType, startTimeUs, endTimeUs);
        return true;
    }

    public final class EmbeddedSampleStream implements SampleStream {
        private final int index;
        public final ChunkSampleStream<T> parent;
        private final DefaultTrackOutput sampleQueue;

        public EmbeddedSampleStream(ChunkSampleStream<T> parent, DefaultTrackOutput sampleQueue, int index) {
            this.parent = parent;
            this.sampleQueue = sampleQueue;
            this.index = index;
        }

        @Override // com.google.android.exoplayer2.source.SampleStream
        public boolean isReady() {
            return ChunkSampleStream.this.loadingFinished || !(ChunkSampleStream.this.isPendingReset() || this.sampleQueue.isEmpty());
        }

        @Override // com.google.android.exoplayer2.source.SampleStream
        public void skipData(long positionUs) {
            if (ChunkSampleStream.this.loadingFinished && positionUs > this.sampleQueue.getLargestQueuedTimestampUs()) {
                this.sampleQueue.skipAll();
            } else {
                this.sampleQueue.skipToKeyframeBefore(positionUs, true);
            }
        }

        @Override // com.google.android.exoplayer2.source.SampleStream
        public void maybeThrowError() throws IOException {
        }

        @Override // com.google.android.exoplayer2.source.SampleStream
        public int readData(FormatHolder formatHolder, DecoderInputBuffer buffer, boolean formatRequired) {
            if (ChunkSampleStream.this.isPendingReset()) {
                return -3;
            }
            return this.sampleQueue.readData(formatHolder, buffer, formatRequired, ChunkSampleStream.this.loadingFinished, ChunkSampleStream.this.lastSeekPositionUs);
        }

        public void release() {
            Assertions.checkState(ChunkSampleStream.this.embeddedTracksSelected[this.index]);
            ChunkSampleStream.this.embeddedTracksSelected[this.index] = false;
        }
    }
}
