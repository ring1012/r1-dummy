package com.google.android.exoplayer2.source;

import android.os.Handler;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;

/* loaded from: classes.dex */
public interface AdaptiveMediaSourceEventListener {
    void onDownstreamFormatChanged(int i, Format format, int i2, Object obj, long j);

    void onLoadCanceled(DataSpec dataSpec, int i, int i2, Format format, int i3, Object obj, long j, long j2, long j3, long j4, long j5);

    void onLoadCompleted(DataSpec dataSpec, int i, int i2, Format format, int i3, Object obj, long j, long j2, long j3, long j4, long j5);

    void onLoadError(DataSpec dataSpec, int i, int i2, Format format, int i3, Object obj, long j, long j2, long j3, long j4, long j5, IOException iOException, boolean z);

    void onLoadStarted(DataSpec dataSpec, int i, int i2, Format format, int i3, Object obj, long j, long j2, long j3);

    void onUpstreamDiscarded(int i, long j, long j2);

    public static final class EventDispatcher {
        private final Handler handler;
        private final AdaptiveMediaSourceEventListener listener;
        private final long mediaTimeOffsetMs;

        public EventDispatcher(Handler handler, AdaptiveMediaSourceEventListener listener) {
            this(handler, listener, 0L);
        }

        public EventDispatcher(Handler handler, AdaptiveMediaSourceEventListener listener, long mediaTimeOffsetMs) {
            this.handler = listener != null ? (Handler) Assertions.checkNotNull(handler) : null;
            this.listener = listener;
            this.mediaTimeOffsetMs = mediaTimeOffsetMs;
        }

        public EventDispatcher copyWithMediaTimeOffsetMs(long mediaTimeOffsetMs) {
            return new EventDispatcher(this.handler, this.listener, mediaTimeOffsetMs);
        }

        public void loadStarted(DataSpec dataSpec, int dataType, long elapsedRealtimeMs) {
            loadStarted(dataSpec, dataType, -1, null, 0, null, C.TIME_UNSET, C.TIME_UNSET, elapsedRealtimeMs);
        }

        public void loadStarted(final DataSpec dataSpec, final int dataType, final int trackType, final Format trackFormat, final int trackSelectionReason, final Object trackSelectionData, final long mediaStartTimeUs, final long mediaEndTimeUs, final long elapsedRealtimeMs) {
            if (this.listener != null) {
                this.handler.post(new Runnable() { // from class: com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener.EventDispatcher.1
                    @Override // java.lang.Runnable
                    public void run() {
                        EventDispatcher.this.listener.onLoadStarted(dataSpec, dataType, trackType, trackFormat, trackSelectionReason, trackSelectionData, EventDispatcher.this.adjustMediaTime(mediaStartTimeUs), EventDispatcher.this.adjustMediaTime(mediaEndTimeUs), elapsedRealtimeMs);
                    }
                });
            }
        }

        public void loadCompleted(DataSpec dataSpec, int dataType, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded) {
            loadCompleted(dataSpec, dataType, -1, null, 0, null, C.TIME_UNSET, C.TIME_UNSET, elapsedRealtimeMs, loadDurationMs, bytesLoaded);
        }

        public void loadCompleted(final DataSpec dataSpec, final int dataType, final int trackType, final Format trackFormat, final int trackSelectionReason, final Object trackSelectionData, final long mediaStartTimeUs, final long mediaEndTimeUs, final long elapsedRealtimeMs, final long loadDurationMs, final long bytesLoaded) {
            if (this.listener != null) {
                this.handler.post(new Runnable() { // from class: com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener.EventDispatcher.2
                    @Override // java.lang.Runnable
                    public void run() {
                        EventDispatcher.this.listener.onLoadCompleted(dataSpec, dataType, trackType, trackFormat, trackSelectionReason, trackSelectionData, EventDispatcher.this.adjustMediaTime(mediaStartTimeUs), EventDispatcher.this.adjustMediaTime(mediaEndTimeUs), elapsedRealtimeMs, loadDurationMs, bytesLoaded);
                    }
                });
            }
        }

        public void loadCanceled(DataSpec dataSpec, int dataType, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded) {
            loadCanceled(dataSpec, dataType, -1, null, 0, null, C.TIME_UNSET, C.TIME_UNSET, elapsedRealtimeMs, loadDurationMs, bytesLoaded);
        }

        public void loadCanceled(final DataSpec dataSpec, final int dataType, final int trackType, final Format trackFormat, final int trackSelectionReason, final Object trackSelectionData, final long mediaStartTimeUs, final long mediaEndTimeUs, final long elapsedRealtimeMs, final long loadDurationMs, final long bytesLoaded) {
            if (this.listener != null) {
                this.handler.post(new Runnable() { // from class: com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener.EventDispatcher.3
                    @Override // java.lang.Runnable
                    public void run() {
                        EventDispatcher.this.listener.onLoadCanceled(dataSpec, dataType, trackType, trackFormat, trackSelectionReason, trackSelectionData, EventDispatcher.this.adjustMediaTime(mediaStartTimeUs), EventDispatcher.this.adjustMediaTime(mediaEndTimeUs), elapsedRealtimeMs, loadDurationMs, bytesLoaded);
                    }
                });
            }
        }

        public void loadError(DataSpec dataSpec, int dataType, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded, IOException error, boolean wasCanceled) {
            loadError(dataSpec, dataType, -1, null, 0, null, C.TIME_UNSET, C.TIME_UNSET, elapsedRealtimeMs, loadDurationMs, bytesLoaded, error, wasCanceled);
        }

        public void loadError(final DataSpec dataSpec, final int dataType, final int trackType, final Format trackFormat, final int trackSelectionReason, final Object trackSelectionData, final long mediaStartTimeUs, final long mediaEndTimeUs, final long elapsedRealtimeMs, final long loadDurationMs, final long bytesLoaded, final IOException error, final boolean wasCanceled) {
            if (this.listener != null) {
                this.handler.post(new Runnable() { // from class: com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener.EventDispatcher.4
                    @Override // java.lang.Runnable
                    public void run() {
                        EventDispatcher.this.listener.onLoadError(dataSpec, dataType, trackType, trackFormat, trackSelectionReason, trackSelectionData, EventDispatcher.this.adjustMediaTime(mediaStartTimeUs), EventDispatcher.this.adjustMediaTime(mediaEndTimeUs), elapsedRealtimeMs, loadDurationMs, bytesLoaded, error, wasCanceled);
                    }
                });
            }
        }

        public void upstreamDiscarded(final int trackType, final long mediaStartTimeUs, final long mediaEndTimeUs) {
            if (this.listener != null) {
                this.handler.post(new Runnable() { // from class: com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener.EventDispatcher.5
                    @Override // java.lang.Runnable
                    public void run() {
                        EventDispatcher.this.listener.onUpstreamDiscarded(trackType, EventDispatcher.this.adjustMediaTime(mediaStartTimeUs), EventDispatcher.this.adjustMediaTime(mediaEndTimeUs));
                    }
                });
            }
        }

        public void downstreamFormatChanged(final int trackType, final Format trackFormat, final int trackSelectionReason, final Object trackSelectionData, final long mediaTimeUs) {
            if (this.listener != null) {
                this.handler.post(new Runnable() { // from class: com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener.EventDispatcher.6
                    @Override // java.lang.Runnable
                    public void run() {
                        EventDispatcher.this.listener.onDownstreamFormatChanged(trackType, trackFormat, trackSelectionReason, trackSelectionData, EventDispatcher.this.adjustMediaTime(mediaTimeUs));
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public long adjustMediaTime(long mediaTimeUs) {
            long mediaTimeMs = C.usToMs(mediaTimeUs);
            return mediaTimeMs == C.TIME_UNSET ? C.TIME_UNSET : this.mediaTimeOffsetMs + mediaTimeMs;
        }
    }
}
