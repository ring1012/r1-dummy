package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;

/* loaded from: classes.dex */
public final class ClippingMediaPeriod implements MediaPeriod, MediaPeriod.Callback {
    private MediaPeriod.Callback callback;
    public final MediaPeriod mediaPeriod;
    private boolean pendingInitialDiscontinuity;
    private long startUs = C.TIME_UNSET;
    private long endUs = C.TIME_UNSET;
    private ClippingSampleStream[] sampleStreams = new ClippingSampleStream[0];

    public ClippingMediaPeriod(MediaPeriod mediaPeriod) {
        this.mediaPeriod = mediaPeriod;
    }

    public void setClipping(long startUs, long endUs) {
        this.startUs = startUs;
        this.endUs = endUs;
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public void prepare(MediaPeriod.Callback callback) {
        this.callback = callback;
        this.mediaPeriod.prepare(this);
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public void maybeThrowPrepareError() throws IOException {
        this.mediaPeriod.maybeThrowPrepareError();
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public TrackGroupArray getTrackGroups() {
        return this.mediaPeriod.getTrackGroups();
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public long selectTracks(TrackSelection[] selections, boolean[] mayRetainStreamFlags, SampleStream[] streams, boolean[] streamResetFlags, long positionUs) {
        SampleStream sampleStream;
        this.sampleStreams = new ClippingSampleStream[streams.length];
        SampleStream[] internalStreams = new SampleStream[streams.length];
        for (int i = 0; i < streams.length; i++) {
            this.sampleStreams[i] = (ClippingSampleStream) streams[i];
            if (this.sampleStreams[i] == null) {
                sampleStream = null;
            } else {
                sampleStream = this.sampleStreams[i].stream;
            }
            internalStreams[i] = sampleStream;
        }
        long enablePositionUs = this.mediaPeriod.selectTracks(selections, mayRetainStreamFlags, internalStreams, streamResetFlags, positionUs + this.startUs);
        Assertions.checkState(enablePositionUs == this.startUs + positionUs || (enablePositionUs >= this.startUs && (this.endUs == Long.MIN_VALUE || enablePositionUs <= this.endUs)));
        for (int i2 = 0; i2 < streams.length; i2++) {
            if (internalStreams[i2] == null) {
                this.sampleStreams[i2] = null;
            } else if (streams[i2] == null || this.sampleStreams[i2].stream != internalStreams[i2]) {
                this.sampleStreams[i2] = new ClippingSampleStream(this, internalStreams[i2], this.startUs, this.endUs, this.pendingInitialDiscontinuity);
            }
            streams[i2] = this.sampleStreams[i2];
        }
        return enablePositionUs - this.startUs;
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public void discardBuffer(long positionUs) {
        this.mediaPeriod.discardBuffer(this.startUs + positionUs);
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public long readDiscontinuity() {
        if (this.pendingInitialDiscontinuity) {
            for (ClippingSampleStream sampleStream : this.sampleStreams) {
                if (sampleStream != null) {
                    sampleStream.clearPendingDiscontinuity();
                }
            }
            this.pendingInitialDiscontinuity = false;
            long discontinuityUs = readDiscontinuity();
            if (discontinuityUs != C.TIME_UNSET) {
                return discontinuityUs;
            }
            return 0L;
        }
        long discontinuityUs2 = this.mediaPeriod.readDiscontinuity();
        if (discontinuityUs2 == C.TIME_UNSET) {
            return C.TIME_UNSET;
        }
        Assertions.checkState(discontinuityUs2 >= this.startUs);
        Assertions.checkState(this.endUs == Long.MIN_VALUE || discontinuityUs2 <= this.endUs);
        return discontinuityUs2 - this.startUs;
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public long getBufferedPositionUs() {
        long bufferedPositionUs = this.mediaPeriod.getBufferedPositionUs();
        if (bufferedPositionUs == Long.MIN_VALUE) {
            return Long.MIN_VALUE;
        }
        if (this.endUs == Long.MIN_VALUE || bufferedPositionUs < this.endUs) {
            return Math.max(0L, bufferedPositionUs - this.startUs);
        }
        return Long.MIN_VALUE;
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public long seekToUs(long positionUs) {
        boolean z = false;
        for (ClippingSampleStream sampleStream : this.sampleStreams) {
            if (sampleStream != null) {
                sampleStream.clearSentEos();
            }
        }
        long seekUs = this.mediaPeriod.seekToUs(this.startUs + positionUs);
        if (seekUs == this.startUs + positionUs || (seekUs >= this.startUs && (this.endUs == Long.MIN_VALUE || seekUs <= this.endUs))) {
            z = true;
        }
        Assertions.checkState(z);
        return seekUs - this.startUs;
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod, com.google.android.exoplayer2.source.SequenceableLoader
    public long getNextLoadPositionUs() {
        long nextLoadPositionUs = this.mediaPeriod.getNextLoadPositionUs();
        if (nextLoadPositionUs == Long.MIN_VALUE) {
            return Long.MIN_VALUE;
        }
        if (this.endUs == Long.MIN_VALUE || nextLoadPositionUs < this.endUs) {
            return nextLoadPositionUs - this.startUs;
        }
        return Long.MIN_VALUE;
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod, com.google.android.exoplayer2.source.SequenceableLoader
    public boolean continueLoading(long positionUs) {
        return this.mediaPeriod.continueLoading(this.startUs + positionUs);
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod.Callback
    public void onPrepared(MediaPeriod mediaPeriod) {
        Assertions.checkState((this.startUs == C.TIME_UNSET || this.endUs == C.TIME_UNSET) ? false : true);
        this.pendingInitialDiscontinuity = this.startUs != 0;
        this.callback.onPrepared(this);
    }

    @Override // com.google.android.exoplayer2.source.SequenceableLoader.Callback
    public void onContinueLoadingRequested(MediaPeriod source) {
        this.callback.onContinueLoadingRequested(this);
    }

    private static final class ClippingSampleStream implements SampleStream {
        private final long endUs;
        private final MediaPeriod mediaPeriod;
        private boolean pendingDiscontinuity;
        private boolean sentEos;
        private final long startUs;
        private final SampleStream stream;

        public ClippingSampleStream(MediaPeriod mediaPeriod, SampleStream stream, long startUs, long endUs, boolean pendingDiscontinuity) {
            this.mediaPeriod = mediaPeriod;
            this.stream = stream;
            this.startUs = startUs;
            this.endUs = endUs;
            this.pendingDiscontinuity = pendingDiscontinuity;
        }

        public void clearPendingDiscontinuity() {
            this.pendingDiscontinuity = false;
        }

        public void clearSentEos() {
            this.sentEos = false;
        }

        @Override // com.google.android.exoplayer2.source.SampleStream
        public boolean isReady() {
            return this.stream.isReady();
        }

        @Override // com.google.android.exoplayer2.source.SampleStream
        public void maybeThrowError() throws IOException {
            this.stream.maybeThrowError();
        }

        @Override // com.google.android.exoplayer2.source.SampleStream
        public int readData(FormatHolder formatHolder, DecoderInputBuffer buffer, boolean requireFormat) {
            if (this.pendingDiscontinuity) {
                return -3;
            }
            if (this.sentEos) {
                buffer.setFlags(4);
                return -4;
            }
            int result = this.stream.readData(formatHolder, buffer, requireFormat);
            if (this.endUs != Long.MIN_VALUE && ((result == -4 && buffer.timeUs >= this.endUs) || (result == -3 && this.mediaPeriod.getBufferedPositionUs() == Long.MIN_VALUE))) {
                buffer.clear();
                buffer.setFlags(4);
                this.sentEos = true;
                return -4;
            }
            if (result == -4 && !buffer.isEndOfStream()) {
                buffer.timeUs -= this.startUs;
                return result;
            }
            return result;
        }

        @Override // com.google.android.exoplayer2.source.SampleStream
        public void skipData(long positionUs) {
            this.stream.skipData(this.startUs + positionUs);
        }
    }
}
