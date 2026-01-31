package com.google.android.exoplayer2.source;

import android.net.Uri;
import android.os.Handler;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/* loaded from: classes.dex */
final class SingleSampleMediaPeriod implements MediaPeriod, Loader.Callback<SourceLoadable> {
    private static final int INITIAL_SAMPLE_SIZE = 1024;
    private final DataSource.Factory dataSourceFactory;
    private final Handler eventHandler;
    private final SingleSampleMediaSource.EventListener eventListener;
    private final int eventSourceId;
    final Format format;
    boolean loadingFinished;
    private final int minLoadableRetryCount;
    byte[] sampleData;
    int sampleSize;
    private final TrackGroupArray tracks;
    private final Uri uri;
    private final ArrayList<SampleStreamImpl> sampleStreams = new ArrayList<>();
    final Loader loader = new Loader("Loader:SingleSampleMediaPeriod");

    public SingleSampleMediaPeriod(Uri uri, DataSource.Factory dataSourceFactory, Format format, int minLoadableRetryCount, Handler eventHandler, SingleSampleMediaSource.EventListener eventListener, int eventSourceId) {
        this.uri = uri;
        this.dataSourceFactory = dataSourceFactory;
        this.format = format;
        this.minLoadableRetryCount = minLoadableRetryCount;
        this.eventHandler = eventHandler;
        this.eventListener = eventListener;
        this.eventSourceId = eventSourceId;
        this.tracks = new TrackGroupArray(new TrackGroup(format));
    }

    public void release() {
        this.loader.release();
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public void prepare(MediaPeriod.Callback callback) {
        callback.onPrepared(this);
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public void maybeThrowPrepareError() throws IOException {
        this.loader.maybeThrowError();
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public TrackGroupArray getTrackGroups() {
        return this.tracks;
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public long selectTracks(TrackSelection[] selections, boolean[] mayRetainStreamFlags, SampleStream[] streams, boolean[] streamResetFlags, long positionUs) {
        for (int i = 0; i < selections.length; i++) {
            if (streams[i] != null && (selections[i] == null || !mayRetainStreamFlags[i])) {
                this.sampleStreams.remove(streams[i]);
                streams[i] = null;
            }
            if (streams[i] == null && selections[i] != null) {
                SampleStreamImpl stream = new SampleStreamImpl();
                this.sampleStreams.add(stream);
                streams[i] = stream;
                streamResetFlags[i] = true;
            }
        }
        return positionUs;
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public void discardBuffer(long positionUs) {
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod, com.google.android.exoplayer2.source.SequenceableLoader
    public boolean continueLoading(long positionUs) {
        if (this.loadingFinished || this.loader.isLoading()) {
            return false;
        }
        this.loader.startLoading(new SourceLoadable(this.uri, this.dataSourceFactory.createDataSource()), this, this.minLoadableRetryCount);
        return true;
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public long readDiscontinuity() {
        return C.TIME_UNSET;
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod, com.google.android.exoplayer2.source.SequenceableLoader
    public long getNextLoadPositionUs() {
        return (this.loadingFinished || this.loader.isLoading()) ? Long.MIN_VALUE : 0L;
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public long getBufferedPositionUs() {
        return this.loadingFinished ? Long.MIN_VALUE : 0L;
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public long seekToUs(long positionUs) {
        for (int i = 0; i < this.sampleStreams.size(); i++) {
            this.sampleStreams.get(i).seekToUs(positionUs);
        }
        return positionUs;
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.Callback
    public void onLoadCompleted(SourceLoadable loadable, long elapsedRealtimeMs, long loadDurationMs) {
        this.sampleSize = loadable.sampleSize;
        this.sampleData = loadable.sampleData;
        this.loadingFinished = true;
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.Callback
    public void onLoadCanceled(SourceLoadable loadable, long elapsedRealtimeMs, long loadDurationMs, boolean released) {
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.Callback
    public int onLoadError(SourceLoadable loadable, long elapsedRealtimeMs, long loadDurationMs, IOException error) {
        notifyLoadError(error);
        return 0;
    }

    private void notifyLoadError(final IOException e) {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post(new Runnable() { // from class: com.google.android.exoplayer2.source.SingleSampleMediaPeriod.1
                @Override // java.lang.Runnable
                public void run() {
                    SingleSampleMediaPeriod.this.eventListener.onLoadError(SingleSampleMediaPeriod.this.eventSourceId, e);
                }
            });
        }
    }

    private final class SampleStreamImpl implements SampleStream {
        private static final int STREAM_STATE_END_OF_STREAM = 2;
        private static final int STREAM_STATE_SEND_FORMAT = 0;
        private static final int STREAM_STATE_SEND_SAMPLE = 1;
        private int streamState;

        private SampleStreamImpl() {
        }

        public void seekToUs(long positionUs) {
            if (this.streamState == 2) {
                this.streamState = 1;
            }
        }

        @Override // com.google.android.exoplayer2.source.SampleStream
        public boolean isReady() {
            return SingleSampleMediaPeriod.this.loadingFinished;
        }

        @Override // com.google.android.exoplayer2.source.SampleStream
        public void maybeThrowError() throws IOException {
            SingleSampleMediaPeriod.this.loader.maybeThrowError();
        }

        @Override // com.google.android.exoplayer2.source.SampleStream
        public int readData(FormatHolder formatHolder, DecoderInputBuffer buffer, boolean requireFormat) throws IllegalStateException {
            if (this.streamState == 2) {
                buffer.addFlag(4);
                return -4;
            }
            if (requireFormat || this.streamState == 0) {
                formatHolder.format = SingleSampleMediaPeriod.this.format;
                this.streamState = 1;
                return -5;
            }
            Assertions.checkState(this.streamState == 1);
            if (!SingleSampleMediaPeriod.this.loadingFinished) {
                return -3;
            }
            buffer.timeUs = 0L;
            buffer.addFlag(1);
            buffer.ensureSpaceForWrite(SingleSampleMediaPeriod.this.sampleSize);
            buffer.data.put(SingleSampleMediaPeriod.this.sampleData, 0, SingleSampleMediaPeriod.this.sampleSize);
            this.streamState = 2;
            return -4;
        }

        @Override // com.google.android.exoplayer2.source.SampleStream
        public void skipData(long positionUs) {
            if (positionUs > 0) {
                this.streamState = 2;
            }
        }
    }

    static final class SourceLoadable implements Loader.Loadable {
        private final DataSource dataSource;
        private byte[] sampleData;
        private int sampleSize;
        private final Uri uri;

        public SourceLoadable(Uri uri, DataSource dataSource) {
            this.uri = uri;
            this.dataSource = dataSource;
        }

        @Override // com.google.android.exoplayer2.upstream.Loader.Loadable
        public void cancelLoad() {
        }

        @Override // com.google.android.exoplayer2.upstream.Loader.Loadable
        public boolean isLoadCanceled() {
            return false;
        }

        @Override // com.google.android.exoplayer2.upstream.Loader.Loadable
        public void load() throws InterruptedException, IOException {
            this.sampleSize = 0;
            try {
                this.dataSource.open(new DataSpec(this.uri));
                int result = 0;
                while (result != -1) {
                    this.sampleSize += result;
                    if (this.sampleData == null) {
                        this.sampleData = new byte[1024];
                    } else if (this.sampleSize == this.sampleData.length) {
                        this.sampleData = Arrays.copyOf(this.sampleData, this.sampleData.length * 2);
                    }
                    result = this.dataSource.read(this.sampleData, this.sampleSize, this.sampleData.length - this.sampleSize);
                }
            } finally {
                Util.closeQuietly(this.dataSource);
            }
        }
    }
}
