package com.google.android.exoplayer2.audio;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Handler;
import android.view.Surface;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.audio.AudioTrack;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.mediacodec.MediaCodecInfo;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.util.MediaClock;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import java.nio.ByteBuffer;

@TargetApi(16)
/* loaded from: classes.dex */
public class MediaCodecAudioRenderer extends MediaCodecRenderer implements MediaClock {
    private boolean allowPositionDiscontinuity;
    private final AudioTrack audioTrack;
    private int channelCount;
    private boolean codecNeedsDiscardChannelsWorkaround;
    private long currentPositionUs;
    private final AudioRendererEventListener.EventDispatcher eventDispatcher;
    private boolean passthroughEnabled;
    private MediaFormat passthroughMediaFormat;
    private int pcmEncoding;

    public MediaCodecAudioRenderer(MediaCodecSelector mediaCodecSelector) {
        this(mediaCodecSelector, (DrmSessionManager<FrameworkMediaCrypto>) null, true);
    }

    public MediaCodecAudioRenderer(MediaCodecSelector mediaCodecSelector, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, boolean playClearSamplesWithoutKeys) {
        this(mediaCodecSelector, drmSessionManager, playClearSamplesWithoutKeys, null, null);
    }

    public MediaCodecAudioRenderer(MediaCodecSelector mediaCodecSelector, Handler eventHandler, AudioRendererEventListener eventListener) {
        this(mediaCodecSelector, null, true, eventHandler, eventListener);
    }

    public MediaCodecAudioRenderer(MediaCodecSelector mediaCodecSelector, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, boolean playClearSamplesWithoutKeys, Handler eventHandler, AudioRendererEventListener eventListener) {
        this(mediaCodecSelector, drmSessionManager, playClearSamplesWithoutKeys, eventHandler, eventListener, null, new AudioProcessor[0]);
    }

    public MediaCodecAudioRenderer(MediaCodecSelector mediaCodecSelector, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, boolean playClearSamplesWithoutKeys, Handler eventHandler, AudioRendererEventListener eventListener, AudioCapabilities audioCapabilities, AudioProcessor... audioProcessors) {
        super(1, mediaCodecSelector, drmSessionManager, playClearSamplesWithoutKeys);
        this.audioTrack = new AudioTrack(audioCapabilities, audioProcessors, new AudioTrackListener());
        this.eventDispatcher = new AudioRendererEventListener.EventDispatcher(eventHandler, eventListener);
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected int supportsFormat(MediaCodecSelector mediaCodecSelector, Format format) throws MediaCodecUtil.DecoderQueryException {
        String mimeType = format.sampleMimeType;
        if (!MimeTypes.isAudio(mimeType)) {
            return 0;
        }
        int tunnelingSupport = Util.SDK_INT >= 21 ? 16 : 0;
        if (allowPassthrough(mimeType) && mediaCodecSelector.getPassthroughDecoderInfo() != null) {
            return tunnelingSupport | 4 | 3;
        }
        MediaCodecInfo decoderInfo = mediaCodecSelector.getDecoderInfo(mimeType, false);
        if (decoderInfo == null) {
            return 1;
        }
        boolean decoderCapable = Util.SDK_INT < 21 || ((format.sampleRate == -1 || decoderInfo.isAudioSampleRateSupportedV21(format.sampleRate)) && (format.channelCount == -1 || decoderInfo.isAudioChannelCountSupportedV21(format.channelCount)));
        int formatSupport = decoderCapable ? 3 : 2;
        return tunnelingSupport | 4 | formatSupport;
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected MediaCodecInfo getDecoderInfo(MediaCodecSelector mediaCodecSelector, Format format, boolean requiresSecureDecoder) throws MediaCodecUtil.DecoderQueryException {
        MediaCodecInfo passthroughDecoderInfo;
        if (allowPassthrough(format.sampleMimeType) && (passthroughDecoderInfo = mediaCodecSelector.getPassthroughDecoderInfo()) != null) {
            this.passthroughEnabled = true;
            return passthroughDecoderInfo;
        }
        this.passthroughEnabled = false;
        return super.getDecoderInfo(mediaCodecSelector, format, requiresSecureDecoder);
    }

    protected boolean allowPassthrough(String mimeType) {
        return this.audioTrack.isPassthroughSupported(mimeType);
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected void configureCodec(MediaCodecInfo codecInfo, MediaCodec codec, Format format, MediaCrypto crypto) {
        this.codecNeedsDiscardChannelsWorkaround = codecNeedsDiscardChannelsWorkaround(codecInfo.name);
        if (this.passthroughEnabled) {
            this.passthroughMediaFormat = format.getFrameworkMediaFormatV16();
            this.passthroughMediaFormat.setString("mime", MimeTypes.AUDIO_RAW);
            codec.configure(this.passthroughMediaFormat, (Surface) null, crypto, 0);
            this.passthroughMediaFormat.setString("mime", format.sampleMimeType);
            return;
        }
        codec.configure(format.getFrameworkMediaFormatV16(), (Surface) null, crypto, 0);
        this.passthroughMediaFormat = null;
    }

    @Override // com.google.android.exoplayer2.BaseRenderer, com.google.android.exoplayer2.Renderer
    public MediaClock getMediaClock() {
        return this;
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected void onCodecInitialized(String name, long initializedTimestampMs, long initializationDurationMs) {
        this.eventDispatcher.decoderInitialized(name, initializedTimestampMs, initializationDurationMs);
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected void onInputFormatChanged(Format newFormat) throws ExoPlaybackException {
        super.onInputFormatChanged(newFormat);
        this.eventDispatcher.inputFormatChanged(newFormat);
        this.pcmEncoding = MimeTypes.AUDIO_RAW.equals(newFormat.sampleMimeType) ? newFormat.pcmEncoding : 2;
        this.channelCount = newFormat.channelCount;
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected void onOutputFormatChanged(MediaCodec codec, MediaFormat outputFormat) throws IllegalStateException, ExoPlaybackException {
        int[] channelMap;
        boolean passthrough = this.passthroughMediaFormat != null;
        String mimeType = passthrough ? this.passthroughMediaFormat.getString("mime") : MimeTypes.AUDIO_RAW;
        MediaFormat format = passthrough ? this.passthroughMediaFormat : outputFormat;
        int channelCount = format.getInteger("channel-count");
        int sampleRate = format.getInteger("sample-rate");
        if (this.codecNeedsDiscardChannelsWorkaround && channelCount == 6 && this.channelCount < 6) {
            channelMap = new int[this.channelCount];
            for (int i = 0; i < this.channelCount; i++) {
                channelMap[i] = i;
            }
        } else {
            channelMap = null;
        }
        try {
            this.audioTrack.configure(mimeType, channelCount, sampleRate, this.pcmEncoding, 0, channelMap);
        } catch (AudioTrack.ConfigurationException e) {
            throw ExoPlaybackException.createForRenderer(e, getIndex());
        }
    }

    protected void onAudioSessionId(int audioSessionId) {
    }

    protected void onAudioTrackPositionDiscontinuity() {
    }

    protected void onAudioTrackUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.BaseRenderer
    protected void onEnabled(boolean joining) throws ExoPlaybackException, IllegalStateException {
        super.onEnabled(joining);
        this.eventDispatcher.enabled(this.decoderCounters);
        int tunnelingAudioSessionId = getConfiguration().tunnelingAudioSessionId;
        if (tunnelingAudioSessionId != 0) {
            this.audioTrack.enableTunnelingV21(tunnelingAudioSessionId);
        } else {
            this.audioTrack.disableTunneling();
        }
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.BaseRenderer
    protected void onPositionReset(long positionUs, boolean joining) throws ExoPlaybackException, IllegalStateException {
        super.onPositionReset(positionUs, joining);
        this.audioTrack.reset();
        this.currentPositionUs = positionUs;
        this.allowPositionDiscontinuity = true;
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.BaseRenderer
    protected void onStarted() throws IllegalStateException {
        super.onStarted();
        this.audioTrack.play();
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.BaseRenderer
    protected void onStopped() throws IllegalStateException {
        this.audioTrack.pause();
        super.onStopped();
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.BaseRenderer
    protected void onDisabled() {
        try {
            this.audioTrack.release();
            try {
                super.onDisabled();
            } finally {
            }
        } catch (Throwable th) {
            try {
                super.onDisabled();
                throw th;
            } finally {
            }
        }
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.Renderer
    public boolean isEnded() {
        return super.isEnded() && this.audioTrack.isEnded();
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.Renderer
    public boolean isReady() {
        return this.audioTrack.hasPendingData() || super.isReady();
    }

    @Override // com.google.android.exoplayer2.util.MediaClock
    public long getPositionUs() {
        long newCurrentPositionUs = this.audioTrack.getCurrentPositionUs(isEnded());
        if (newCurrentPositionUs != Long.MIN_VALUE) {
            if (!this.allowPositionDiscontinuity) {
                newCurrentPositionUs = Math.max(this.currentPositionUs, newCurrentPositionUs);
            }
            this.currentPositionUs = newCurrentPositionUs;
            this.allowPositionDiscontinuity = false;
        }
        return this.currentPositionUs;
    }

    @Override // com.google.android.exoplayer2.util.MediaClock
    public PlaybackParameters setPlaybackParameters(PlaybackParameters playbackParameters) {
        return this.audioTrack.setPlaybackParameters(playbackParameters);
    }

    @Override // com.google.android.exoplayer2.util.MediaClock
    public PlaybackParameters getPlaybackParameters() {
        return this.audioTrack.getPlaybackParameters();
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected boolean processOutputBuffer(long positionUs, long elapsedRealtimeUs, MediaCodec codec, ByteBuffer buffer, int bufferIndex, int bufferFlags, long bufferPresentationTimeUs, boolean shouldSkip) throws ExoPlaybackException {
        if (this.passthroughEnabled && (bufferFlags & 2) != 0) {
            codec.releaseOutputBuffer(bufferIndex, false);
            return true;
        }
        if (shouldSkip) {
            codec.releaseOutputBuffer(bufferIndex, false);
            this.decoderCounters.skippedOutputBufferCount++;
            this.audioTrack.handleDiscontinuity();
            return true;
        }
        try {
            if (!this.audioTrack.handleBuffer(buffer, bufferPresentationTimeUs)) {
                return false;
            }
            codec.releaseOutputBuffer(bufferIndex, false);
            this.decoderCounters.renderedOutputBufferCount++;
            return true;
        } catch (AudioTrack.InitializationException | AudioTrack.WriteException e) {
            throw ExoPlaybackException.createForRenderer(e, getIndex());
        }
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected void renderToEndOfStream() throws IllegalStateException, ExoPlaybackException {
        try {
            this.audioTrack.playToEndOfStream();
        } catch (AudioTrack.WriteException e) {
            throw ExoPlaybackException.createForRenderer(e, getIndex());
        }
    }

    @Override // com.google.android.exoplayer2.BaseRenderer, com.google.android.exoplayer2.ExoPlayer.ExoPlayerComponent
    public void handleMessage(int messageType, Object message) throws IllegalStateException, ExoPlaybackException {
        switch (messageType) {
            case 2:
                this.audioTrack.setVolume(((Float) message).floatValue());
                break;
            case 3:
                int streamType = ((Integer) message).intValue();
                this.audioTrack.setStreamType(streamType);
                break;
            default:
                super.handleMessage(messageType, message);
                break;
        }
    }

    private static boolean codecNeedsDiscardChannelsWorkaround(String codecName) {
        return Util.SDK_INT < 24 && "OMX.SEC.aac.dec".equals(codecName) && "samsung".equals(Util.MANUFACTURER) && (Util.DEVICE.startsWith("zeroflte") || Util.DEVICE.startsWith("herolte") || Util.DEVICE.startsWith("heroqlte"));
    }

    private final class AudioTrackListener implements AudioTrack.Listener {
        private AudioTrackListener() {
        }

        @Override // com.google.android.exoplayer2.audio.AudioTrack.Listener
        public void onAudioSessionId(int audioSessionId) {
            MediaCodecAudioRenderer.this.eventDispatcher.audioSessionId(audioSessionId);
            MediaCodecAudioRenderer.this.onAudioSessionId(audioSessionId);
        }

        @Override // com.google.android.exoplayer2.audio.AudioTrack.Listener
        public void onPositionDiscontinuity() {
            MediaCodecAudioRenderer.this.onAudioTrackPositionDiscontinuity();
            MediaCodecAudioRenderer.this.allowPositionDiscontinuity = true;
        }

        @Override // com.google.android.exoplayer2.audio.AudioTrack.Listener
        public void onUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
            MediaCodecAudioRenderer.this.eventDispatcher.audioTrackUnderrun(bufferSize, bufferSizeMs, elapsedSinceLastFeedMs);
            MediaCodecAudioRenderer.this.onAudioTrackUnderrun(bufferSize, bufferSizeMs, elapsedSinceLastFeedMs);
        }
    }
}
