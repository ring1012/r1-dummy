package com.google.android.exoplayer2.video;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Surface;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.mediacodec.MediaCodecInfo;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.TraceUtil;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import java.nio.ByteBuffer;

@TargetApi(16)
/* loaded from: classes.dex */
public class MediaCodecVideoRenderer extends MediaCodecRenderer {
    private static final String KEY_CROP_BOTTOM = "crop-bottom";
    private static final String KEY_CROP_LEFT = "crop-left";
    private static final String KEY_CROP_RIGHT = "crop-right";
    private static final String KEY_CROP_TOP = "crop-top";
    private static final int[] STANDARD_LONG_EDGE_VIDEO_PX = {1920, 1600, 1440, 1280, 960, 854, 640, 540, 480};
    private static final String TAG = "MediaCodecVideoRenderer";
    private final long allowedJoiningTimeMs;
    private CodecMaxValues codecMaxValues;
    private int consecutiveDroppedFrameCount;
    private int currentHeight;
    private float currentPixelWidthHeightRatio;
    private int currentUnappliedRotationDegrees;
    private int currentWidth;
    private final boolean deviceNeedsAutoFrcWorkaround;
    private long droppedFrameAccumulationStartTimeMs;
    private int droppedFrames;
    private final VideoRendererEventListener.EventDispatcher eventDispatcher;
    private final VideoFrameReleaseTimeHelper frameReleaseTimeHelper;
    private long joiningDeadlineMs;
    private final int maxDroppedFramesToNotify;
    private float pendingPixelWidthHeightRatio;
    private int pendingRotationDegrees;
    private boolean renderedFirstFrame;
    private int reportedHeight;
    private float reportedPixelWidthHeightRatio;
    private int reportedUnappliedRotationDegrees;
    private int reportedWidth;
    private int scalingMode;
    private Format[] streamFormats;
    private Surface surface;
    private boolean tunneling;
    private int tunnelingAudioSessionId;
    OnFrameRenderedListenerV23 tunnelingOnFrameRenderedListener;

    public MediaCodecVideoRenderer(Context context, MediaCodecSelector mediaCodecSelector) {
        this(context, mediaCodecSelector, 0L);
    }

    public MediaCodecVideoRenderer(Context context, MediaCodecSelector mediaCodecSelector, long allowedJoiningTimeMs) {
        this(context, mediaCodecSelector, allowedJoiningTimeMs, null, null, -1);
    }

    public MediaCodecVideoRenderer(Context context, MediaCodecSelector mediaCodecSelector, long allowedJoiningTimeMs, Handler eventHandler, VideoRendererEventListener eventListener, int maxDroppedFrameCountToNotify) {
        this(context, mediaCodecSelector, allowedJoiningTimeMs, null, false, eventHandler, eventListener, maxDroppedFrameCountToNotify);
    }

    public MediaCodecVideoRenderer(Context context, MediaCodecSelector mediaCodecSelector, long allowedJoiningTimeMs, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, boolean playClearSamplesWithoutKeys, Handler eventHandler, VideoRendererEventListener eventListener, int maxDroppedFramesToNotify) {
        super(2, mediaCodecSelector, drmSessionManager, playClearSamplesWithoutKeys);
        this.allowedJoiningTimeMs = allowedJoiningTimeMs;
        this.maxDroppedFramesToNotify = maxDroppedFramesToNotify;
        this.frameReleaseTimeHelper = new VideoFrameReleaseTimeHelper(context);
        this.eventDispatcher = new VideoRendererEventListener.EventDispatcher(eventHandler, eventListener);
        this.deviceNeedsAutoFrcWorkaround = deviceNeedsAutoFrcWorkaround();
        this.joiningDeadlineMs = C.TIME_UNSET;
        this.currentWidth = -1;
        this.currentHeight = -1;
        this.currentPixelWidthHeightRatio = -1.0f;
        this.pendingPixelWidthHeightRatio = -1.0f;
        this.scalingMode = 1;
        clearReportedVideoSize();
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected int supportsFormat(MediaCodecSelector mediaCodecSelector, Format format) throws MediaCodecUtil.DecoderQueryException {
        String mimeType = format.sampleMimeType;
        if (!MimeTypes.isVideo(mimeType)) {
            return 0;
        }
        boolean requiresSecureDecryption = false;
        DrmInitData drmInitData = format.drmInitData;
        if (drmInitData != null) {
            for (int i = 0; i < drmInitData.schemeDataCount; i++) {
                requiresSecureDecryption |= drmInitData.get(i).requiresSecureDecryption;
            }
        }
        MediaCodecInfo decoderInfo = mediaCodecSelector.getDecoderInfo(mimeType, requiresSecureDecryption);
        if (decoderInfo == null) {
            return 1;
        }
        boolean decoderCapable = decoderInfo.isCodecSupported(format.codecs);
        if (decoderCapable && format.width > 0 && format.height > 0) {
            if (Util.SDK_INT >= 21) {
                decoderCapable = decoderInfo.isVideoSizeAndRateSupportedV21(format.width, format.height, format.frameRate);
            } else {
                decoderCapable = format.width * format.height <= MediaCodecUtil.maxH264DecodableFrameSize();
                if (!decoderCapable) {
                    Log.d(TAG, "FalseCheck [legacyFrameSize, " + format.width + "x" + format.height + "] [" + Util.DEVICE_DEBUG_INFO + "]");
                }
            }
        }
        int adaptiveSupport = decoderInfo.adaptive ? 8 : 4;
        int tunnelingSupport = decoderInfo.tunneling ? 16 : 0;
        int formatSupport = decoderCapable ? 3 : 2;
        return adaptiveSupport | tunnelingSupport | formatSupport;
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.BaseRenderer
    protected void onEnabled(boolean joining) throws ExoPlaybackException {
        super.onEnabled(joining);
        this.tunnelingAudioSessionId = getConfiguration().tunnelingAudioSessionId;
        this.tunneling = this.tunnelingAudioSessionId != 0;
        this.eventDispatcher.enabled(this.decoderCounters);
        this.frameReleaseTimeHelper.enable();
    }

    @Override // com.google.android.exoplayer2.BaseRenderer
    protected void onStreamChanged(Format[] formats) throws ExoPlaybackException {
        this.streamFormats = formats;
        super.onStreamChanged(formats);
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.BaseRenderer
    protected void onPositionReset(long positionUs, boolean joining) throws ExoPlaybackException {
        super.onPositionReset(positionUs, joining);
        clearRenderedFirstFrame();
        this.consecutiveDroppedFrameCount = 0;
        if (joining) {
            setJoiningDeadlineMs();
        } else {
            this.joiningDeadlineMs = C.TIME_UNSET;
        }
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.Renderer
    public boolean isReady() {
        if ((this.renderedFirstFrame || super.shouldInitCodec()) && super.isReady()) {
            this.joiningDeadlineMs = C.TIME_UNSET;
            return true;
        }
        if (this.joiningDeadlineMs == C.TIME_UNSET) {
            return false;
        }
        if (SystemClock.elapsedRealtime() < this.joiningDeadlineMs) {
            return true;
        }
        this.joiningDeadlineMs = C.TIME_UNSET;
        return false;
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.BaseRenderer
    protected void onStarted() {
        super.onStarted();
        this.droppedFrames = 0;
        this.droppedFrameAccumulationStartTimeMs = SystemClock.elapsedRealtime();
        this.joiningDeadlineMs = C.TIME_UNSET;
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.BaseRenderer
    protected void onStopped() {
        maybeNotifyDroppedFrames();
        super.onStopped();
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer, com.google.android.exoplayer2.BaseRenderer
    protected void onDisabled() {
        this.currentWidth = -1;
        this.currentHeight = -1;
        this.currentPixelWidthHeightRatio = -1.0f;
        this.pendingPixelWidthHeightRatio = -1.0f;
        clearReportedVideoSize();
        clearRenderedFirstFrame();
        this.frameReleaseTimeHelper.disable();
        this.tunnelingOnFrameRenderedListener = null;
        try {
            super.onDisabled();
        } finally {
            this.decoderCounters.ensureUpdated();
            this.eventDispatcher.disabled(this.decoderCounters);
        }
    }

    @Override // com.google.android.exoplayer2.BaseRenderer, com.google.android.exoplayer2.ExoPlayer.ExoPlayerComponent
    public void handleMessage(int messageType, Object message) throws ExoPlaybackException {
        if (messageType == 1) {
            setSurface((Surface) message);
            return;
        }
        if (messageType == 4) {
            this.scalingMode = ((Integer) message).intValue();
            MediaCodec codec = getCodec();
            if (codec != null) {
                setVideoScalingMode(codec, this.scalingMode);
                return;
            }
            return;
        }
        super.handleMessage(messageType, message);
    }

    private void setSurface(Surface surface) throws ExoPlaybackException {
        if (this.surface != surface) {
            this.surface = surface;
            int state = getState();
            if (state == 1 || state == 2) {
                MediaCodec codec = getCodec();
                if (Util.SDK_INT >= 23 && codec != null && surface != null) {
                    setOutputSurfaceV23(codec, surface);
                } else {
                    releaseCodec();
                    maybeInitCodec();
                }
            }
            if (surface != null) {
                maybeRenotifyVideoSizeChanged();
                clearRenderedFirstFrame();
                if (state == 2) {
                    setJoiningDeadlineMs();
                    return;
                }
                return;
            }
            clearReportedVideoSize();
            clearRenderedFirstFrame();
            return;
        }
        if (surface != null) {
            maybeRenotifyVideoSizeChanged();
            maybeRenotifyRenderedFirstFrame();
        }
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected boolean shouldInitCodec() {
        return super.shouldInitCodec() && this.surface != null && this.surface.isValid();
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected void configureCodec(MediaCodecInfo codecInfo, MediaCodec codec, Format format, MediaCrypto crypto) throws MediaCodecUtil.DecoderQueryException {
        this.codecMaxValues = getCodecMaxValues(codecInfo, format, this.streamFormats);
        MediaFormat mediaFormat = getMediaFormat(format, this.codecMaxValues, this.deviceNeedsAutoFrcWorkaround, this.tunnelingAudioSessionId);
        codec.configure(mediaFormat, this.surface, crypto, 0);
        if (Util.SDK_INT >= 23 && this.tunneling) {
            this.tunnelingOnFrameRenderedListener = new OnFrameRenderedListenerV23(codec);
        }
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected void onCodecInitialized(String name, long initializedTimestampMs, long initializationDurationMs) {
        this.eventDispatcher.decoderInitialized(name, initializedTimestampMs, initializationDurationMs);
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected void onInputFormatChanged(Format newFormat) throws ExoPlaybackException {
        super.onInputFormatChanged(newFormat);
        this.eventDispatcher.inputFormatChanged(newFormat);
        this.pendingPixelWidthHeightRatio = getPixelWidthHeightRatio(newFormat);
        this.pendingRotationDegrees = getRotationDegrees(newFormat);
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected void onQueueInputBuffer(DecoderInputBuffer buffer) {
        if (Util.SDK_INT < 23 && this.tunneling) {
            maybeNotifyRenderedFirstFrame();
        }
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected void onOutputFormatChanged(MediaCodec codec, MediaFormat outputFormat) {
        int integer;
        int integer2;
        boolean hasCrop = outputFormat.containsKey(KEY_CROP_RIGHT) && outputFormat.containsKey(KEY_CROP_LEFT) && outputFormat.containsKey(KEY_CROP_BOTTOM) && outputFormat.containsKey(KEY_CROP_TOP);
        if (hasCrop) {
            integer = (outputFormat.getInteger(KEY_CROP_RIGHT) - outputFormat.getInteger(KEY_CROP_LEFT)) + 1;
        } else {
            integer = outputFormat.getInteger("width");
        }
        this.currentWidth = integer;
        if (hasCrop) {
            integer2 = (outputFormat.getInteger(KEY_CROP_BOTTOM) - outputFormat.getInteger(KEY_CROP_TOP)) + 1;
        } else {
            integer2 = outputFormat.getInteger("height");
        }
        this.currentHeight = integer2;
        this.currentPixelWidthHeightRatio = this.pendingPixelWidthHeightRatio;
        if (Util.SDK_INT >= 21) {
            if (this.pendingRotationDegrees == 90 || this.pendingRotationDegrees == 270) {
                int rotatedHeight = this.currentWidth;
                this.currentWidth = this.currentHeight;
                this.currentHeight = rotatedHeight;
                this.currentPixelWidthHeightRatio = 1.0f / this.currentPixelWidthHeightRatio;
            }
        } else {
            this.currentUnappliedRotationDegrees = this.pendingRotationDegrees;
        }
        setVideoScalingMode(codec, this.scalingMode);
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected boolean canReconfigureCodec(MediaCodec codec, boolean codecIsAdaptive, Format oldFormat, Format newFormat) {
        return areAdaptationCompatible(oldFormat, newFormat) && newFormat.width <= this.codecMaxValues.width && newFormat.height <= this.codecMaxValues.height && newFormat.maxInputSize <= this.codecMaxValues.inputSize && (codecIsAdaptive || (oldFormat.width == newFormat.width && oldFormat.height == newFormat.height));
    }

    @Override // com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
    protected boolean processOutputBuffer(long positionUs, long elapsedRealtimeUs, MediaCodec codec, ByteBuffer buffer, int bufferIndex, int bufferFlags, long bufferPresentationTimeUs, boolean shouldSkip) throws InterruptedException {
        if (shouldSkip) {
            skipOutputBuffer(codec, bufferIndex);
            return true;
        }
        if (!this.renderedFirstFrame) {
            if (Util.SDK_INT >= 21) {
                renderOutputBufferV21(codec, bufferIndex, System.nanoTime());
            } else {
                renderOutputBuffer(codec, bufferIndex);
            }
            return true;
        }
        if (getState() != 2) {
            return false;
        }
        long elapsedSinceStartOfLoopUs = (SystemClock.elapsedRealtime() * 1000) - elapsedRealtimeUs;
        long systemTimeNs = System.nanoTime();
        long unadjustedFrameReleaseTimeNs = systemTimeNs + (1000 * ((bufferPresentationTimeUs - positionUs) - elapsedSinceStartOfLoopUs));
        long adjustedReleaseTimeNs = this.frameReleaseTimeHelper.adjustReleaseTime(bufferPresentationTimeUs, unadjustedFrameReleaseTimeNs);
        long earlyUs = (adjustedReleaseTimeNs - systemTimeNs) / 1000;
        if (shouldDropOutputBuffer(earlyUs, elapsedRealtimeUs)) {
            dropOutputBuffer(codec, bufferIndex);
            return true;
        }
        if (Util.SDK_INT >= 21) {
            if (earlyUs < 50000) {
                renderOutputBufferV21(codec, bufferIndex, adjustedReleaseTimeNs);
                return true;
            }
        } else if (earlyUs < 30000) {
            if (earlyUs > 11000) {
                try {
                    Thread.sleep((earlyUs - 10000) / 1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            renderOutputBuffer(codec, bufferIndex);
            return true;
        }
        return false;
    }

    protected boolean shouldDropOutputBuffer(long earlyUs, long elapsedRealtimeUs) {
        return earlyUs < -30000;
    }

    private void skipOutputBuffer(MediaCodec codec, int bufferIndex) {
        TraceUtil.beginSection("skipVideoBuffer");
        codec.releaseOutputBuffer(bufferIndex, false);
        TraceUtil.endSection();
        this.decoderCounters.skippedOutputBufferCount++;
    }

    private void dropOutputBuffer(MediaCodec codec, int bufferIndex) {
        TraceUtil.beginSection("dropVideoBuffer");
        codec.releaseOutputBuffer(bufferIndex, false);
        TraceUtil.endSection();
        this.decoderCounters.droppedOutputBufferCount++;
        this.droppedFrames++;
        this.consecutiveDroppedFrameCount++;
        this.decoderCounters.maxConsecutiveDroppedOutputBufferCount = Math.max(this.consecutiveDroppedFrameCount, this.decoderCounters.maxConsecutiveDroppedOutputBufferCount);
        if (this.droppedFrames == this.maxDroppedFramesToNotify) {
            maybeNotifyDroppedFrames();
        }
    }

    private void renderOutputBuffer(MediaCodec codec, int bufferIndex) {
        maybeNotifyVideoSizeChanged();
        TraceUtil.beginSection("releaseOutputBuffer");
        codec.releaseOutputBuffer(bufferIndex, true);
        TraceUtil.endSection();
        this.decoderCounters.renderedOutputBufferCount++;
        this.consecutiveDroppedFrameCount = 0;
        maybeNotifyRenderedFirstFrame();
    }

    @TargetApi(21)
    private void renderOutputBufferV21(MediaCodec codec, int bufferIndex, long releaseTimeNs) {
        maybeNotifyVideoSizeChanged();
        TraceUtil.beginSection("releaseOutputBuffer");
        codec.releaseOutputBuffer(bufferIndex, releaseTimeNs);
        TraceUtil.endSection();
        this.decoderCounters.renderedOutputBufferCount++;
        this.consecutiveDroppedFrameCount = 0;
        maybeNotifyRenderedFirstFrame();
    }

    private void setJoiningDeadlineMs() {
        this.joiningDeadlineMs = this.allowedJoiningTimeMs > 0 ? SystemClock.elapsedRealtime() + this.allowedJoiningTimeMs : C.TIME_UNSET;
    }

    private void clearRenderedFirstFrame() {
        MediaCodec codec;
        this.renderedFirstFrame = false;
        if (Util.SDK_INT >= 23 && this.tunneling && (codec = getCodec()) != null) {
            this.tunnelingOnFrameRenderedListener = new OnFrameRenderedListenerV23(codec);
        }
    }

    void maybeNotifyRenderedFirstFrame() {
        if (!this.renderedFirstFrame) {
            this.renderedFirstFrame = true;
            this.eventDispatcher.renderedFirstFrame(this.surface);
        }
    }

    private void maybeRenotifyRenderedFirstFrame() {
        if (this.renderedFirstFrame) {
            this.eventDispatcher.renderedFirstFrame(this.surface);
        }
    }

    private void clearReportedVideoSize() {
        this.reportedWidth = -1;
        this.reportedHeight = -1;
        this.reportedPixelWidthHeightRatio = -1.0f;
        this.reportedUnappliedRotationDegrees = -1;
    }

    private void maybeNotifyVideoSizeChanged() {
        if (this.reportedWidth != this.currentWidth || this.reportedHeight != this.currentHeight || this.reportedUnappliedRotationDegrees != this.currentUnappliedRotationDegrees || this.reportedPixelWidthHeightRatio != this.currentPixelWidthHeightRatio) {
            this.eventDispatcher.videoSizeChanged(this.currentWidth, this.currentHeight, this.currentUnappliedRotationDegrees, this.currentPixelWidthHeightRatio);
            this.reportedWidth = this.currentWidth;
            this.reportedHeight = this.currentHeight;
            this.reportedUnappliedRotationDegrees = this.currentUnappliedRotationDegrees;
            this.reportedPixelWidthHeightRatio = this.currentPixelWidthHeightRatio;
        }
    }

    private void maybeRenotifyVideoSizeChanged() {
        if (this.reportedWidth != -1 || this.reportedHeight != -1) {
            this.eventDispatcher.videoSizeChanged(this.currentWidth, this.currentHeight, this.currentUnappliedRotationDegrees, this.currentPixelWidthHeightRatio);
        }
    }

    private void maybeNotifyDroppedFrames() {
        if (this.droppedFrames > 0) {
            long now = SystemClock.elapsedRealtime();
            long elapsedMs = now - this.droppedFrameAccumulationStartTimeMs;
            this.eventDispatcher.droppedFrames(this.droppedFrames, elapsedMs);
            this.droppedFrames = 0;
            this.droppedFrameAccumulationStartTimeMs = now;
        }
    }

    @SuppressLint({"InlinedApi"})
    private static MediaFormat getMediaFormat(Format format, CodecMaxValues codecMaxValues, boolean deviceNeedsAutoFrcWorkaround, int tunnelingAudioSessionId) {
        MediaFormat frameworkMediaFormat = format.getFrameworkMediaFormatV16();
        frameworkMediaFormat.setInteger("max-width", codecMaxValues.width);
        frameworkMediaFormat.setInteger("max-height", codecMaxValues.height);
        if (codecMaxValues.inputSize != -1) {
            frameworkMediaFormat.setInteger("max-input-size", codecMaxValues.inputSize);
        }
        if (deviceNeedsAutoFrcWorkaround) {
            frameworkMediaFormat.setInteger("auto-frc", 0);
        }
        if (tunnelingAudioSessionId != 0) {
            configureTunnelingV21(frameworkMediaFormat, tunnelingAudioSessionId);
        }
        return frameworkMediaFormat;
    }

    @TargetApi(23)
    private static void setOutputSurfaceV23(MediaCodec codec, Surface surface) {
        codec.setOutputSurface(surface);
    }

    @TargetApi(21)
    private static void configureTunnelingV21(MediaFormat mediaFormat, int tunnelingAudioSessionId) {
        mediaFormat.setFeatureEnabled("tunneled-playback", true);
        mediaFormat.setInteger("audio-session-id", tunnelingAudioSessionId);
    }

    private static CodecMaxValues getCodecMaxValues(MediaCodecInfo codecInfo, Format format, Format[] streamFormats) throws MediaCodecUtil.DecoderQueryException {
        int maxWidth = format.width;
        int maxHeight = format.height;
        int maxInputSize = getMaxInputSize(format);
        if (streamFormats.length == 1) {
            return new CodecMaxValues(maxWidth, maxHeight, maxInputSize);
        }
        boolean haveUnknownDimensions = false;
        for (Format streamFormat : streamFormats) {
            if (areAdaptationCompatible(format, streamFormat)) {
                haveUnknownDimensions |= streamFormat.width == -1 || streamFormat.height == -1;
                maxWidth = Math.max(maxWidth, streamFormat.width);
                maxHeight = Math.max(maxHeight, streamFormat.height);
                maxInputSize = Math.max(maxInputSize, getMaxInputSize(streamFormat));
            }
        }
        if (haveUnknownDimensions) {
            Log.w(TAG, "Resolutions unknown. Codec max resolution: " + maxWidth + "x" + maxHeight);
            Point codecMaxSize = getCodecMaxSize(codecInfo, format);
            if (codecMaxSize != null) {
                maxWidth = Math.max(maxWidth, codecMaxSize.x);
                maxHeight = Math.max(maxHeight, codecMaxSize.y);
                maxInputSize = Math.max(maxInputSize, getMaxInputSize(format.sampleMimeType, maxWidth, maxHeight));
                Log.w(TAG, "Codec max resolution adjusted to: " + maxWidth + "x" + maxHeight);
            }
        }
        return new CodecMaxValues(maxWidth, maxHeight, maxInputSize);
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x002f, code lost:
    
        return null;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static android.graphics.Point getCodecMaxSize(com.google.android.exoplayer2.mediacodec.MediaCodecInfo r20, com.google.android.exoplayer2.Format r21) throws com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException {
        /*
            r0 = r21
            int r12 = r0.height
            r0 = r21
            int r13 = r0.width
            if (r12 <= r13) goto L31
            r9 = 1
        Lb:
            if (r9 == 0) goto L33
            r0 = r21
            int r6 = r0.height
        L11:
            if (r9 == 0) goto L38
            r0 = r21
            int r7 = r0.width
        L17:
            float r12 = (float) r7
            float r13 = (float) r6
            float r5 = r12 / r13
            int[] r15 = com.google.android.exoplayer2.video.MediaCodecVideoRenderer.STANDARD_LONG_EDGE_VIDEO_PX
            int r0 = r15.length
            r16 = r0
            r12 = 0
            r14 = r12
        L22:
            r0 = r16
            if (r14 >= r0) goto L93
            r10 = r15[r14]
            float r12 = (float) r10
            float r12 = r12 * r5
            int r11 = (int) r12
            if (r10 <= r6) goto L2f
            if (r11 > r7) goto L3d
        L2f:
            r4 = 0
        L30:
            return r4
        L31:
            r9 = 0
            goto Lb
        L33:
            r0 = r21
            int r6 = r0.width
            goto L11
        L38:
            r0 = r21
            int r7 = r0.height
            goto L17
        L3d:
            int r12 = com.google.android.exoplayer2.util.Util.SDK_INT
            r13 = 21
            if (r12 < r13) goto L6c
            if (r9 == 0) goto L68
            r13 = r11
        L46:
            if (r9 == 0) goto L6a
            r12 = r10
        L49:
            r0 = r20
            android.graphics.Point r4 = r0.alignVideoSizeV21(r13, r12)
            r0 = r21
            float r8 = r0.frameRate
            int r12 = r4.x
            int r13 = r4.y
            double r0 = (double) r8
            r18 = r0
            r0 = r20
            r1 = r18
            boolean r12 = r0.isVideoSizeAndRateSupportedV21(r12, r13, r1)
            if (r12 != 0) goto L30
        L64:
            int r12 = r14 + 1
            r14 = r12
            goto L22
        L68:
            r13 = r10
            goto L46
        L6a:
            r12 = r11
            goto L49
        L6c:
            r12 = 16
            int r12 = com.google.android.exoplayer2.util.Util.ceilDivide(r10, r12)
            int r10 = r12 * 16
            r12 = 16
            int r12 = com.google.android.exoplayer2.util.Util.ceilDivide(r11, r12)
            int r11 = r12 * 16
            int r12 = r10 * r11
            int r13 = com.google.android.exoplayer2.mediacodec.MediaCodecUtil.maxH264DecodableFrameSize()
            if (r12 > r13) goto L64
            android.graphics.Point r4 = new android.graphics.Point
            if (r9 == 0) goto L8f
            r12 = r11
        L89:
            if (r9 == 0) goto L91
        L8b:
            r4.<init>(r12, r10)
            goto L30
        L8f:
            r12 = r10
            goto L89
        L91:
            r10 = r11
            goto L8b
        L93:
            r4 = 0
            goto L30
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.video.MediaCodecVideoRenderer.getCodecMaxSize(com.google.android.exoplayer2.mediacodec.MediaCodecInfo, com.google.android.exoplayer2.Format):android.graphics.Point");
    }

    private static int getMaxInputSize(Format format) {
        return format.maxInputSize != -1 ? format.maxInputSize : getMaxInputSize(format.sampleMimeType, format.width, format.height);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:8:0x000f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static int getMaxInputSize(java.lang.String r6, int r7, int r8) {
        /*
            r5 = 16
            r2 = -1
            if (r7 == r2) goto L7
            if (r8 != r2) goto L8
        L7:
            return r2
        L8:
            int r3 = r6.hashCode()
            switch(r3) {
                case -1664118616: goto L1d;
                case -1662541442: goto L45;
                case 1187890754: goto L27;
                case 1331836730: goto L31;
                case 1599127256: goto L3b;
                case 1599127257: goto L4f;
                default: goto Lf;
            }
        Lf:
            r3 = r2
        L10:
            switch(r3) {
                case 0: goto L14;
                case 1: goto L14;
                case 2: goto L59;
                case 3: goto L72;
                case 4: goto L76;
                case 5: goto L76;
                default: goto L13;
            }
        L13:
            goto L7
        L14:
            int r0 = r7 * r8
            r1 = 2
        L17:
            int r2 = r0 * 3
            int r3 = r1 * 2
            int r2 = r2 / r3
            goto L7
        L1d:
            java.lang.String r3 = "video/3gpp"
            boolean r3 = r6.equals(r3)
            if (r3 == 0) goto Lf
            r3 = 0
            goto L10
        L27:
            java.lang.String r3 = "video/mp4v-es"
            boolean r3 = r6.equals(r3)
            if (r3 == 0) goto Lf
            r3 = 1
            goto L10
        L31:
            java.lang.String r3 = "video/avc"
            boolean r3 = r6.equals(r3)
            if (r3 == 0) goto Lf
            r3 = 2
            goto L10
        L3b:
            java.lang.String r3 = "video/x-vnd.on2.vp8"
            boolean r3 = r6.equals(r3)
            if (r3 == 0) goto Lf
            r3 = 3
            goto L10
        L45:
            java.lang.String r3 = "video/hevc"
            boolean r3 = r6.equals(r3)
            if (r3 == 0) goto Lf
            r3 = 4
            goto L10
        L4f:
            java.lang.String r3 = "video/x-vnd.on2.vp9"
            boolean r3 = r6.equals(r3)
            if (r3 == 0) goto Lf
            r3 = 5
            goto L10
        L59:
            java.lang.String r3 = "BRAVIA 4K 2015"
            java.lang.String r4 = com.google.android.exoplayer2.util.Util.MODEL
            boolean r3 = r3.equals(r4)
            if (r3 != 0) goto L7
            int r2 = com.google.android.exoplayer2.util.Util.ceilDivide(r7, r5)
            int r3 = com.google.android.exoplayer2.util.Util.ceilDivide(r8, r5)
            int r2 = r2 * r3
            int r2 = r2 * 16
            int r0 = r2 * 16
            r1 = 2
            goto L17
        L72:
            int r0 = r7 * r8
            r1 = 2
            goto L17
        L76:
            int r0 = r7 * r8
            r1 = 4
            goto L17
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.video.MediaCodecVideoRenderer.getMaxInputSize(java.lang.String, int, int):int");
    }

    private static void setVideoScalingMode(MediaCodec codec, int scalingMode) {
        codec.setVideoScalingMode(scalingMode);
    }

    private static boolean deviceNeedsAutoFrcWorkaround() {
        return Util.SDK_INT <= 22 && "foster".equals(Util.DEVICE) && "NVIDIA".equals(Util.MANUFACTURER);
    }

    private static boolean areAdaptationCompatible(Format first, Format second) {
        return first.sampleMimeType.equals(second.sampleMimeType) && getRotationDegrees(first) == getRotationDegrees(second);
    }

    private static float getPixelWidthHeightRatio(Format format) {
        if (format.pixelWidthHeightRatio == -1.0f) {
            return 1.0f;
        }
        return format.pixelWidthHeightRatio;
    }

    private static int getRotationDegrees(Format format) {
        if (format.rotationDegrees == -1) {
            return 0;
        }
        return format.rotationDegrees;
    }

    private static final class CodecMaxValues {
        public final int height;
        public final int inputSize;
        public final int width;

        public CodecMaxValues(int width, int height, int inputSize) {
            this.width = width;
            this.height = height;
            this.inputSize = inputSize;
        }
    }

    @TargetApi(23)
    private final class OnFrameRenderedListenerV23 implements MediaCodec.OnFrameRenderedListener {
        private OnFrameRenderedListenerV23(MediaCodec codec) {
            codec.setOnFrameRenderedListener(this, new Handler());
        }

        @Override // android.media.MediaCodec.OnFrameRenderedListener
        public void onFrameRendered(@NonNull MediaCodec codec, long presentationTimeUs, long nanoTime) {
            if (this == MediaCodecVideoRenderer.this.tunnelingOnFrameRenderedListener) {
                MediaCodecVideoRenderer.this.maybeNotifyRenderedFirstFrame();
            }
        }
    }
}
