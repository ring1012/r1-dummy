package com.google.android.exoplayer2.audio;

import android.os.Handler;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.util.Assertions;

/* loaded from: classes.dex */
public interface AudioRendererEventListener {
    void onAudioDecoderInitialized(String str, long j, long j2);

    void onAudioDisabled(DecoderCounters decoderCounters);

    void onAudioEnabled(DecoderCounters decoderCounters);

    void onAudioInputFormatChanged(Format format);

    void onAudioSessionId(int i);

    void onAudioTrackUnderrun(int i, long j, long j2);

    public static final class EventDispatcher {
        private final Handler handler;
        private final AudioRendererEventListener listener;

        public EventDispatcher(Handler handler, AudioRendererEventListener listener) {
            this.handler = listener != null ? (Handler) Assertions.checkNotNull(handler) : null;
            this.listener = listener;
        }

        public void enabled(final DecoderCounters decoderCounters) {
            if (this.listener != null) {
                this.handler.post(new Runnable() { // from class: com.google.android.exoplayer2.audio.AudioRendererEventListener.EventDispatcher.1
                    @Override // java.lang.Runnable
                    public void run() {
                        EventDispatcher.this.listener.onAudioEnabled(decoderCounters);
                    }
                });
            }
        }

        public void decoderInitialized(final String decoderName, final long initializedTimestampMs, final long initializationDurationMs) {
            if (this.listener != null) {
                this.handler.post(new Runnable() { // from class: com.google.android.exoplayer2.audio.AudioRendererEventListener.EventDispatcher.2
                    @Override // java.lang.Runnable
                    public void run() {
                        EventDispatcher.this.listener.onAudioDecoderInitialized(decoderName, initializedTimestampMs, initializationDurationMs);
                    }
                });
            }
        }

        public void inputFormatChanged(final Format format) {
            if (this.listener != null) {
                this.handler.post(new Runnable() { // from class: com.google.android.exoplayer2.audio.AudioRendererEventListener.EventDispatcher.3
                    @Override // java.lang.Runnable
                    public void run() {
                        EventDispatcher.this.listener.onAudioInputFormatChanged(format);
                    }
                });
            }
        }

        public void audioTrackUnderrun(final int bufferSize, final long bufferSizeMs, final long elapsedSinceLastFeedMs) {
            if (this.listener != null) {
                this.handler.post(new Runnable() { // from class: com.google.android.exoplayer2.audio.AudioRendererEventListener.EventDispatcher.4
                    @Override // java.lang.Runnable
                    public void run() {
                        EventDispatcher.this.listener.onAudioTrackUnderrun(bufferSize, bufferSizeMs, elapsedSinceLastFeedMs);
                    }
                });
            }
        }

        public void disabled(final DecoderCounters counters) {
            if (this.listener != null) {
                this.handler.post(new Runnable() { // from class: com.google.android.exoplayer2.audio.AudioRendererEventListener.EventDispatcher.5
                    @Override // java.lang.Runnable
                    public void run() {
                        counters.ensureUpdated();
                        EventDispatcher.this.listener.onAudioDisabled(counters);
                    }
                });
            }
        }

        public void audioSessionId(final int audioSessionId) {
            if (this.listener != null) {
                this.handler.post(new Runnable() { // from class: com.google.android.exoplayer2.audio.AudioRendererEventListener.EventDispatcher.6
                    @Override // java.lang.Runnable
                    public void run() {
                        EventDispatcher.this.listener.onAudioSessionId(audioSessionId);
                    }
                });
            }
        }
    }
}
