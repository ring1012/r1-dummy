package com.google.android.exoplayer2.mediacodec;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import com.google.android.exoplayer2.BaseRenderer;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.TraceUtil;
import com.google.android.exoplayer2.util.Util;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

@TargetApi(16)
/* loaded from: classes.dex */
public abstract class MediaCodecRenderer extends BaseRenderer {
    private static final byte[] ADAPTATION_WORKAROUND_BUFFER = Util.getBytesFromHexString("0000016742C00BDA259000000168CE0F13200000016588840DCE7118A0002FBF1C31C3275D78");
    private static final int ADAPTATION_WORKAROUND_SLICE_WIDTH_HEIGHT = 32;
    private static final long MAX_CODEC_HOTSWAP_TIME_MS = 1000;
    private static final int RECONFIGURATION_STATE_NONE = 0;
    private static final int RECONFIGURATION_STATE_QUEUE_PENDING = 2;
    private static final int RECONFIGURATION_STATE_WRITE_PENDING = 1;
    private static final int REINITIALIZATION_STATE_NONE = 0;
    private static final int REINITIALIZATION_STATE_SIGNAL_END_OF_STREAM = 1;
    private static final int REINITIALIZATION_STATE_WAIT_END_OF_STREAM = 2;
    private static final String TAG = "MediaCodecRenderer";
    private final DecoderInputBuffer buffer;
    private MediaCodec codec;
    private long codecHotswapDeadlineMs;
    private boolean codecIsAdaptive;
    private boolean codecNeedsAdaptationWorkaround;
    private boolean codecNeedsAdaptationWorkaroundBuffer;
    private boolean codecNeedsDiscardToSpsWorkaround;
    private boolean codecNeedsEosFlushWorkaround;
    private boolean codecNeedsEosOutputExceptionWorkaround;
    private boolean codecNeedsEosPropagationWorkaround;
    private boolean codecNeedsFlushWorkaround;
    private boolean codecNeedsMonoChannelCountWorkaround;
    private boolean codecReceivedBuffers;
    private boolean codecReceivedEos;
    private int codecReconfigurationState;
    private boolean codecReconfigured;
    private int codecReinitializationState;
    private final List<Long> decodeOnlyPresentationTimestamps;
    protected DecoderCounters decoderCounters;
    private DrmSession<FrameworkMediaCrypto> drmSession;
    private final DrmSessionManager<FrameworkMediaCrypto> drmSessionManager;
    private final DecoderInputBuffer flagsOnlyBuffer;
    private Format format;
    private final FormatHolder formatHolder;
    private ByteBuffer[] inputBuffers;
    private int inputIndex;
    private boolean inputStreamEnded;
    private final MediaCodecSelector mediaCodecSelector;
    private final MediaCodec.BufferInfo outputBufferInfo;
    private ByteBuffer[] outputBuffers;
    private int outputIndex;
    private boolean outputStreamEnded;
    private DrmSession<FrameworkMediaCrypto> pendingDrmSession;
    private final boolean playClearSamplesWithoutKeys;
    private boolean shouldSkipAdaptationWorkaroundOutputBuffer;
    private boolean shouldSkipOutputBuffer;
    private boolean waitingForFirstSyncFrame;
    private boolean waitingForKeys;

    protected abstract void configureCodec(MediaCodecInfo mediaCodecInfo, MediaCodec mediaCodec, Format format, MediaCrypto mediaCrypto) throws MediaCodecUtil.DecoderQueryException;

    protected abstract boolean processOutputBuffer(long j, long j2, MediaCodec mediaCodec, ByteBuffer byteBuffer, int i, int i2, long j3, boolean z) throws ExoPlaybackException;

    protected abstract int supportsFormat(MediaCodecSelector mediaCodecSelector, Format format) throws MediaCodecUtil.DecoderQueryException;

    public static class DecoderInitializationException extends Exception {
        private static final int CUSTOM_ERROR_CODE_BASE = -50000;
        private static final int DECODER_QUERY_ERROR = -49998;
        private static final int NO_SUITABLE_DECODER_ERROR = -49999;
        public final String decoderName;
        public final String diagnosticInfo;
        public final String mimeType;
        public final boolean secureDecoderRequired;

        public DecoderInitializationException(Format format, Throwable cause, boolean secureDecoderRequired, int errorCode) {
            super("Decoder init failed: [" + errorCode + "], " + format, cause);
            this.mimeType = format.sampleMimeType;
            this.secureDecoderRequired = secureDecoderRequired;
            this.decoderName = null;
            this.diagnosticInfo = buildCustomDiagnosticInfo(errorCode);
        }

        public DecoderInitializationException(Format format, Throwable cause, boolean secureDecoderRequired, String decoderName) {
            super("Decoder init failed: " + decoderName + ", " + format, cause);
            this.mimeType = format.sampleMimeType;
            this.secureDecoderRequired = secureDecoderRequired;
            this.decoderName = decoderName;
            this.diagnosticInfo = Util.SDK_INT >= 21 ? getDiagnosticInfoV21(cause) : null;
        }

        @TargetApi(21)
        private static String getDiagnosticInfoV21(Throwable cause) {
            if (cause instanceof MediaCodec.CodecException) {
                return ((MediaCodec.CodecException) cause).getDiagnosticInfo();
            }
            return null;
        }

        private static String buildCustomDiagnosticInfo(int errorCode) {
            String sign = errorCode < 0 ? "neg_" : "";
            return "com.google.android.exoplayer.MediaCodecTrackRenderer_" + sign + Math.abs(errorCode);
        }
    }

    public MediaCodecRenderer(int trackType, MediaCodecSelector mediaCodecSelector, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, boolean playClearSamplesWithoutKeys) {
        super(trackType);
        Assertions.checkState(Util.SDK_INT >= 16);
        this.mediaCodecSelector = (MediaCodecSelector) Assertions.checkNotNull(mediaCodecSelector);
        this.drmSessionManager = drmSessionManager;
        this.playClearSamplesWithoutKeys = playClearSamplesWithoutKeys;
        this.buffer = new DecoderInputBuffer(0);
        this.flagsOnlyBuffer = DecoderInputBuffer.newFlagsOnlyInstance();
        this.formatHolder = new FormatHolder();
        this.decodeOnlyPresentationTimestamps = new ArrayList();
        this.outputBufferInfo = new MediaCodec.BufferInfo();
        this.codecReconfigurationState = 0;
        this.codecReinitializationState = 0;
    }

    @Override // com.google.android.exoplayer2.BaseRenderer, com.google.android.exoplayer2.RendererCapabilities
    public final int supportsMixedMimeTypeAdaptation() throws ExoPlaybackException {
        return 4;
    }

    @Override // com.google.android.exoplayer2.RendererCapabilities
    public final int supportsFormat(Format format) throws ExoPlaybackException {
        try {
            return supportsFormat(this.mediaCodecSelector, format);
        } catch (MediaCodecUtil.DecoderQueryException e) {
            throw ExoPlaybackException.createForRenderer(e, getIndex());
        }
    }

    protected MediaCodecInfo getDecoderInfo(MediaCodecSelector mediaCodecSelector, Format format, boolean requiresSecureDecoder) throws MediaCodecUtil.DecoderQueryException {
        return mediaCodecSelector.getDecoderInfo(format.sampleMimeType, requiresSecureDecoder);
    }

    protected final void maybeInitCodec() throws ExoPlaybackException {
        if (shouldInitCodec()) {
            this.drmSession = this.pendingDrmSession;
            String mimeType = this.format.sampleMimeType;
            MediaCrypto mediaCrypto = null;
            boolean drmSessionRequiresSecureDecoder = false;
            if (this.drmSession != null) {
                int drmSessionState = this.drmSession.getState();
                if (drmSessionState == 0) {
                    throw ExoPlaybackException.createForRenderer(this.drmSession.getError(), getIndex());
                }
                if (drmSessionState == 3 || drmSessionState == 4) {
                    mediaCrypto = ((FrameworkMediaCrypto) this.drmSession.getMediaCrypto()).getWrappedMediaCrypto();
                    drmSessionRequiresSecureDecoder = this.drmSession.requiresSecureDecoderComponent(mimeType);
                } else {
                    return;
                }
            }
            MediaCodecInfo decoderInfo = null;
            try {
                decoderInfo = getDecoderInfo(this.mediaCodecSelector, this.format, drmSessionRequiresSecureDecoder);
                if (decoderInfo == null && drmSessionRequiresSecureDecoder && (decoderInfo = getDecoderInfo(this.mediaCodecSelector, this.format, false)) != null) {
                    Log.w(TAG, "Drm session requires secure decoder for " + mimeType + ", but no secure decoder available. Trying to proceed with " + decoderInfo.name + ".");
                }
            } catch (MediaCodecUtil.DecoderQueryException e) {
                throwDecoderInitError(new DecoderInitializationException(this.format, e, drmSessionRequiresSecureDecoder, -49998));
            }
            if (decoderInfo == null) {
                throwDecoderInitError(new DecoderInitializationException(this.format, (Throwable) null, drmSessionRequiresSecureDecoder, -49999));
            }
            String codecName = decoderInfo.name;
            this.codecIsAdaptive = decoderInfo.adaptive && !codecNeedsDisableAdaptationWorkaround(codecName);
            this.codecNeedsDiscardToSpsWorkaround = codecNeedsDiscardToSpsWorkaround(codecName, this.format);
            this.codecNeedsFlushWorkaround = codecNeedsFlushWorkaround(codecName);
            this.codecNeedsAdaptationWorkaround = codecNeedsAdaptationWorkaround(codecName);
            this.codecNeedsEosPropagationWorkaround = codecNeedsEosPropagationWorkaround(codecName);
            this.codecNeedsEosFlushWorkaround = codecNeedsEosFlushWorkaround(codecName);
            this.codecNeedsEosOutputExceptionWorkaround = codecNeedsEosOutputExceptionWorkaround(codecName);
            this.codecNeedsMonoChannelCountWorkaround = codecNeedsMonoChannelCountWorkaround(codecName, this.format);
            try {
                long codecInitializingTimestamp = SystemClock.elapsedRealtime();
                TraceUtil.beginSection("createCodec:" + codecName);
                this.codec = MediaCodec.createByCodecName(codecName);
                TraceUtil.endSection();
                TraceUtil.beginSection("configureCodec");
                configureCodec(decoderInfo, this.codec, this.format, mediaCrypto);
                TraceUtil.endSection();
                TraceUtil.beginSection("startCodec");
                this.codec.start();
                TraceUtil.endSection();
                long codecInitializedTimestamp = SystemClock.elapsedRealtime();
                onCodecInitialized(codecName, codecInitializedTimestamp, codecInitializedTimestamp - codecInitializingTimestamp);
                this.inputBuffers = this.codec.getInputBuffers();
                this.outputBuffers = this.codec.getOutputBuffers();
            } catch (Exception e2) {
                throwDecoderInitError(new DecoderInitializationException(this.format, e2, drmSessionRequiresSecureDecoder, codecName));
            }
            this.codecHotswapDeadlineMs = getState() == 2 ? SystemClock.elapsedRealtime() + MAX_CODEC_HOTSWAP_TIME_MS : C.TIME_UNSET;
            this.inputIndex = -1;
            this.outputIndex = -1;
            this.waitingForFirstSyncFrame = true;
            this.decoderCounters.decoderInitCount++;
        }
    }

    private void throwDecoderInitError(DecoderInitializationException e) throws ExoPlaybackException {
        throw ExoPlaybackException.createForRenderer(e, getIndex());
    }

    protected boolean shouldInitCodec() {
        return this.codec == null && this.format != null;
    }

    protected final MediaCodec getCodec() {
        return this.codec;
    }

    @Override // com.google.android.exoplayer2.BaseRenderer
    protected void onEnabled(boolean joining) throws ExoPlaybackException {
        this.decoderCounters = new DecoderCounters();
    }

    @Override // com.google.android.exoplayer2.BaseRenderer
    protected void onPositionReset(long positionUs, boolean joining) throws ExoPlaybackException {
        this.inputStreamEnded = false;
        this.outputStreamEnded = false;
        if (this.codec != null) {
            flushCodec();
        }
    }

    @Override // com.google.android.exoplayer2.BaseRenderer
    protected void onDisabled() {
        this.format = null;
        try {
            releaseCodec();
            try {
                if (this.drmSession != null) {
                    this.drmSessionManager.releaseSession(this.drmSession);
                }
                try {
                    if (this.pendingDrmSession != null && this.pendingDrmSession != this.drmSession) {
                        this.drmSessionManager.releaseSession(this.pendingDrmSession);
                    }
                } finally {
                }
            } catch (Throwable th) {
                try {
                    if (this.pendingDrmSession != null && this.pendingDrmSession != this.drmSession) {
                        this.drmSessionManager.releaseSession(this.pendingDrmSession);
                    }
                    throw th;
                } finally {
                }
            }
        } catch (Throwable th2) {
            try {
                if (this.drmSession != null) {
                    this.drmSessionManager.releaseSession(this.drmSession);
                }
                try {
                    if (this.pendingDrmSession != null && this.pendingDrmSession != this.drmSession) {
                        this.drmSessionManager.releaseSession(this.pendingDrmSession);
                    }
                    throw th2;
                } finally {
                }
            } catch (Throwable th3) {
                try {
                    if (this.pendingDrmSession != null && this.pendingDrmSession != this.drmSession) {
                        this.drmSessionManager.releaseSession(this.pendingDrmSession);
                    }
                    throw th3;
                } finally {
                }
            }
        }
    }

    protected void releaseCodec() {
        if (this.codec != null) {
            this.codecHotswapDeadlineMs = C.TIME_UNSET;
            this.inputIndex = -1;
            this.outputIndex = -1;
            this.waitingForKeys = false;
            this.shouldSkipOutputBuffer = false;
            this.decodeOnlyPresentationTimestamps.clear();
            this.inputBuffers = null;
            this.outputBuffers = null;
            this.codecReconfigured = false;
            this.codecReceivedBuffers = false;
            this.codecIsAdaptive = false;
            this.codecNeedsDiscardToSpsWorkaround = false;
            this.codecNeedsFlushWorkaround = false;
            this.codecNeedsAdaptationWorkaround = false;
            this.codecNeedsEosPropagationWorkaround = false;
            this.codecNeedsEosFlushWorkaround = false;
            this.codecNeedsMonoChannelCountWorkaround = false;
            this.codecNeedsAdaptationWorkaroundBuffer = false;
            this.shouldSkipAdaptationWorkaroundOutputBuffer = false;
            this.codecReceivedEos = false;
            this.codecReconfigurationState = 0;
            this.codecReinitializationState = 0;
            this.decoderCounters.decoderReleaseCount++;
            this.buffer.data = null;
            try {
                this.codec.stop();
                try {
                    this.codec.release();
                    this.codec = null;
                    if (this.drmSession == null || this.pendingDrmSession == this.drmSession) {
                        return;
                    }
                    try {
                        this.drmSessionManager.releaseSession(this.drmSession);
                    } finally {
                    }
                } catch (Throwable th) {
                    this.codec = null;
                    if (this.drmSession != null && this.pendingDrmSession != this.drmSession) {
                        try {
                            this.drmSessionManager.releaseSession(this.drmSession);
                        } finally {
                        }
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                try {
                    this.codec.release();
                    this.codec = null;
                    if (this.drmSession != null && this.pendingDrmSession != this.drmSession) {
                        try {
                            this.drmSessionManager.releaseSession(this.drmSession);
                        } finally {
                        }
                    }
                    throw th2;
                } catch (Throwable th3) {
                    this.codec = null;
                    if (this.drmSession != null && this.pendingDrmSession != this.drmSession) {
                        try {
                            this.drmSessionManager.releaseSession(this.drmSession);
                        } finally {
                        }
                    }
                    throw th3;
                }
            }
        }
    }

    @Override // com.google.android.exoplayer2.BaseRenderer
    protected void onStarted() {
    }

    @Override // com.google.android.exoplayer2.BaseRenderer
    protected void onStopped() {
    }

    @Override // com.google.android.exoplayer2.Renderer
    public void render(long positionUs, long elapsedRealtimeUs) throws ExoPlaybackException {
        if (this.outputStreamEnded) {
            renderToEndOfStream();
            return;
        }
        if (this.format == null) {
            this.buffer.clear();
            int result = readSource(this.formatHolder, this.flagsOnlyBuffer, true);
            if (result == -5) {
                onInputFormatChanged(this.formatHolder.format);
            } else {
                if (result == -4) {
                    Assertions.checkState(this.flagsOnlyBuffer.isEndOfStream());
                    this.inputStreamEnded = true;
                    processEndOfStream();
                    return;
                }
                return;
            }
        }
        maybeInitCodec();
        if (this.codec != null) {
            TraceUtil.beginSection("drainAndFeed");
            while (drainOutputBuffer(positionUs, elapsedRealtimeUs)) {
            }
            while (feedInputBuffer()) {
            }
            TraceUtil.endSection();
        } else {
            skipSource(positionUs);
            this.flagsOnlyBuffer.clear();
            int result2 = readSource(this.formatHolder, this.flagsOnlyBuffer, false);
            if (result2 == -5) {
                onInputFormatChanged(this.formatHolder.format);
            } else if (result2 == -4) {
                Assertions.checkState(this.flagsOnlyBuffer.isEndOfStream());
                this.inputStreamEnded = true;
                processEndOfStream();
            }
        }
        this.decoderCounters.ensureUpdated();
    }

    protected void flushCodec() throws ExoPlaybackException {
        this.codecHotswapDeadlineMs = C.TIME_UNSET;
        this.inputIndex = -1;
        this.outputIndex = -1;
        this.waitingForFirstSyncFrame = true;
        this.waitingForKeys = false;
        this.shouldSkipOutputBuffer = false;
        this.decodeOnlyPresentationTimestamps.clear();
        this.codecNeedsAdaptationWorkaroundBuffer = false;
        this.shouldSkipAdaptationWorkaroundOutputBuffer = false;
        if (this.codecNeedsFlushWorkaround || ((this.codecNeedsEosFlushWorkaround && this.codecReceivedEos) || this.codecReinitializationState != 0)) {
            releaseCodec();
            maybeInitCodec();
        } else {
            this.codec.flush();
            this.codecReceivedBuffers = false;
        }
        if (this.codecReconfigured && this.format != null) {
            this.codecReconfigurationState = 1;
        }
    }

    private boolean feedInputBuffer() throws ExoPlaybackException, MediaCodec.CryptoException {
        int result;
        if (this.codec == null || this.codecReinitializationState == 2 || this.inputStreamEnded) {
            return false;
        }
        if (this.inputIndex < 0) {
            this.inputIndex = this.codec.dequeueInputBuffer(0L);
            if (this.inputIndex < 0) {
                return false;
            }
            this.buffer.data = this.inputBuffers[this.inputIndex];
            this.buffer.clear();
        }
        if (this.codecReinitializationState == 1) {
            if (!this.codecNeedsEosPropagationWorkaround) {
                this.codecReceivedEos = true;
                this.codec.queueInputBuffer(this.inputIndex, 0, 0, 0L, 4);
                this.inputIndex = -1;
            }
            this.codecReinitializationState = 2;
            return false;
        }
        if (this.codecNeedsAdaptationWorkaroundBuffer) {
            this.codecNeedsAdaptationWorkaroundBuffer = false;
            this.buffer.data.put(ADAPTATION_WORKAROUND_BUFFER);
            this.codec.queueInputBuffer(this.inputIndex, 0, ADAPTATION_WORKAROUND_BUFFER.length, 0L, 0);
            this.inputIndex = -1;
            this.codecReceivedBuffers = true;
            return true;
        }
        int adaptiveReconfigurationBytes = 0;
        if (this.waitingForKeys) {
            result = -4;
        } else {
            if (this.codecReconfigurationState == 1) {
                for (int i = 0; i < this.format.initializationData.size(); i++) {
                    byte[] data = this.format.initializationData.get(i);
                    this.buffer.data.put(data);
                }
                this.codecReconfigurationState = 2;
            }
            adaptiveReconfigurationBytes = this.buffer.data.position();
            result = readSource(this.formatHolder, this.buffer, false);
        }
        if (result == -3) {
            return false;
        }
        if (result == -5) {
            if (this.codecReconfigurationState == 2) {
                this.buffer.clear();
                this.codecReconfigurationState = 1;
            }
            onInputFormatChanged(this.formatHolder.format);
            return true;
        }
        if (this.buffer.isEndOfStream()) {
            if (this.codecReconfigurationState == 2) {
                this.buffer.clear();
                this.codecReconfigurationState = 1;
            }
            this.inputStreamEnded = true;
            if (!this.codecReceivedBuffers) {
                processEndOfStream();
                return false;
            }
            try {
                if (!this.codecNeedsEosPropagationWorkaround) {
                    this.codecReceivedEos = true;
                    this.codec.queueInputBuffer(this.inputIndex, 0, 0, 0L, 4);
                    this.inputIndex = -1;
                }
                return false;
            } catch (MediaCodec.CryptoException e) {
                throw ExoPlaybackException.createForRenderer(e, getIndex());
            }
        }
        if (this.waitingForFirstSyncFrame && !this.buffer.isKeyFrame()) {
            this.buffer.clear();
            if (this.codecReconfigurationState == 2) {
                this.codecReconfigurationState = 1;
            }
            return true;
        }
        this.waitingForFirstSyncFrame = false;
        boolean bufferEncrypted = this.buffer.isEncrypted();
        this.waitingForKeys = shouldWaitForKeys(bufferEncrypted);
        if (this.waitingForKeys) {
            return false;
        }
        if (this.codecNeedsDiscardToSpsWorkaround && !bufferEncrypted) {
            NalUnitUtil.discardToSps(this.buffer.data);
            if (this.buffer.data.position() == 0) {
                return true;
            }
            this.codecNeedsDiscardToSpsWorkaround = false;
        }
        try {
            long presentationTimeUs = this.buffer.timeUs;
            if (this.buffer.isDecodeOnly()) {
                this.decodeOnlyPresentationTimestamps.add(Long.valueOf(presentationTimeUs));
            }
            this.buffer.flip();
            onQueueInputBuffer(this.buffer);
            if (bufferEncrypted) {
                MediaCodec.CryptoInfo cryptoInfo = getFrameworkCryptoInfo(this.buffer, adaptiveReconfigurationBytes);
                this.codec.queueSecureInputBuffer(this.inputIndex, 0, cryptoInfo, presentationTimeUs, 0);
            } else {
                this.codec.queueInputBuffer(this.inputIndex, 0, this.buffer.data.limit(), presentationTimeUs, 0);
            }
            this.inputIndex = -1;
            this.codecReceivedBuffers = true;
            this.codecReconfigurationState = 0;
            this.decoderCounters.inputBufferCount++;
            return true;
        } catch (MediaCodec.CryptoException e2) {
            throw ExoPlaybackException.createForRenderer(e2, getIndex());
        }
    }

    private static MediaCodec.CryptoInfo getFrameworkCryptoInfo(DecoderInputBuffer buffer, int adaptiveReconfigurationBytes) {
        MediaCodec.CryptoInfo cryptoInfo = buffer.cryptoInfo.getFrameworkCryptoInfoV16();
        if (adaptiveReconfigurationBytes != 0) {
            if (cryptoInfo.numBytesOfClearData == null) {
                cryptoInfo.numBytesOfClearData = new int[1];
            }
            int[] iArr = cryptoInfo.numBytesOfClearData;
            iArr[0] = iArr[0] + adaptiveReconfigurationBytes;
        }
        return cryptoInfo;
    }

    private boolean shouldWaitForKeys(boolean bufferEncrypted) throws ExoPlaybackException {
        if (this.drmSession == null) {
            return false;
        }
        int drmSessionState = this.drmSession.getState();
        if (drmSessionState == 0) {
            throw ExoPlaybackException.createForRenderer(this.drmSession.getError(), getIndex());
        }
        if (drmSessionState != 4) {
            return bufferEncrypted || !this.playClearSamplesWithoutKeys;
        }
        return false;
    }

    protected void onCodecInitialized(String name, long initializedTimestampMs, long initializationDurationMs) {
    }

    protected void onInputFormatChanged(Format newFormat) throws ExoPlaybackException {
        Format oldFormat = this.format;
        this.format = newFormat;
        boolean drmInitDataChanged = !Util.areEqual(this.format.drmInitData, oldFormat == null ? null : oldFormat.drmInitData);
        if (drmInitDataChanged) {
            if (this.format.drmInitData != null) {
                if (this.drmSessionManager == null) {
                    throw ExoPlaybackException.createForRenderer(new IllegalStateException("Media requires a DrmSessionManager"), getIndex());
                }
                this.pendingDrmSession = this.drmSessionManager.acquireSession(Looper.myLooper(), this.format.drmInitData);
                if (this.pendingDrmSession == this.drmSession) {
                    this.drmSessionManager.releaseSession(this.pendingDrmSession);
                }
            } else {
                this.pendingDrmSession = null;
            }
        }
        if (this.pendingDrmSession == this.drmSession && this.codec != null && canReconfigureCodec(this.codec, this.codecIsAdaptive, oldFormat, this.format)) {
            this.codecReconfigured = true;
            this.codecReconfigurationState = 1;
            this.codecNeedsAdaptationWorkaroundBuffer = this.codecNeedsAdaptationWorkaround && this.format.width == oldFormat.width && this.format.height == oldFormat.height;
        } else if (this.codecReceivedBuffers) {
            this.codecReinitializationState = 1;
        } else {
            releaseCodec();
            maybeInitCodec();
        }
    }

    protected void onOutputFormatChanged(MediaCodec codec, MediaFormat outputFormat) throws ExoPlaybackException {
    }

    protected void onQueueInputBuffer(DecoderInputBuffer buffer) {
    }

    protected void onProcessedOutputBuffer(long presentationTimeUs) {
    }

    protected boolean canReconfigureCodec(MediaCodec codec, boolean codecIsAdaptive, Format oldFormat, Format newFormat) {
        return false;
    }

    @Override // com.google.android.exoplayer2.Renderer
    public boolean isEnded() {
        return this.outputStreamEnded;
    }

    @Override // com.google.android.exoplayer2.Renderer
    public boolean isReady() {
        return (this.format == null || this.waitingForKeys || (!isSourceReady() && this.outputIndex < 0 && (this.codecHotswapDeadlineMs == C.TIME_UNSET || SystemClock.elapsedRealtime() >= this.codecHotswapDeadlineMs))) ? false : true;
    }

    protected long getDequeueOutputBufferTimeoutUs() {
        return 0L;
    }

    private boolean drainOutputBuffer(long positionUs, long elapsedRealtimeUs) throws ExoPlaybackException {
        boolean processedOutputBuffer;
        if (this.outputIndex < 0) {
            if (this.codecNeedsEosOutputExceptionWorkaround && this.codecReceivedEos) {
                try {
                    this.outputIndex = this.codec.dequeueOutputBuffer(this.outputBufferInfo, getDequeueOutputBufferTimeoutUs());
                } catch (IllegalStateException e) {
                    processEndOfStream();
                    if (this.outputStreamEnded) {
                        releaseCodec();
                    }
                    return false;
                }
            } else {
                this.outputIndex = this.codec.dequeueOutputBuffer(this.outputBufferInfo, getDequeueOutputBufferTimeoutUs());
            }
            if (this.outputIndex >= 0) {
                if (this.shouldSkipAdaptationWorkaroundOutputBuffer) {
                    this.shouldSkipAdaptationWorkaroundOutputBuffer = false;
                    this.codec.releaseOutputBuffer(this.outputIndex, false);
                    this.outputIndex = -1;
                    return true;
                }
                if ((this.outputBufferInfo.flags & 4) != 0) {
                    processEndOfStream();
                    this.outputIndex = -1;
                    return false;
                }
                ByteBuffer outputBuffer = this.outputBuffers[this.outputIndex];
                if (outputBuffer != null) {
                    outputBuffer.position(this.outputBufferInfo.offset);
                    outputBuffer.limit(this.outputBufferInfo.offset + this.outputBufferInfo.size);
                }
                this.shouldSkipOutputBuffer = shouldSkipOutputBuffer(this.outputBufferInfo.presentationTimeUs);
            } else {
                if (this.outputIndex == -2) {
                    processOutputFormat();
                    return true;
                }
                if (this.outputIndex == -3) {
                    processOutputBuffersChanged();
                    return true;
                }
                if (this.codecNeedsEosPropagationWorkaround && (this.inputStreamEnded || this.codecReinitializationState == 2)) {
                    processEndOfStream();
                }
                return false;
            }
        }
        if (this.codecNeedsEosOutputExceptionWorkaround && this.codecReceivedEos) {
            try {
                processedOutputBuffer = processOutputBuffer(positionUs, elapsedRealtimeUs, this.codec, this.outputBuffers[this.outputIndex], this.outputIndex, this.outputBufferInfo.flags, this.outputBufferInfo.presentationTimeUs, this.shouldSkipOutputBuffer);
            } catch (IllegalStateException e2) {
                processEndOfStream();
                if (this.outputStreamEnded) {
                    releaseCodec();
                }
                return false;
            }
        } else {
            processedOutputBuffer = processOutputBuffer(positionUs, elapsedRealtimeUs, this.codec, this.outputBuffers[this.outputIndex], this.outputIndex, this.outputBufferInfo.flags, this.outputBufferInfo.presentationTimeUs, this.shouldSkipOutputBuffer);
        }
        if (processedOutputBuffer) {
            onProcessedOutputBuffer(this.outputBufferInfo.presentationTimeUs);
            this.outputIndex = -1;
            return true;
        }
        return false;
    }

    private void processOutputFormat() throws ExoPlaybackException {
        MediaFormat format = this.codec.getOutputFormat();
        if (this.codecNeedsAdaptationWorkaround && format.getInteger("width") == 32 && format.getInteger("height") == 32) {
            this.shouldSkipAdaptationWorkaroundOutputBuffer = true;
            return;
        }
        if (this.codecNeedsMonoChannelCountWorkaround) {
            format.setInteger("channel-count", 1);
        }
        onOutputFormatChanged(this.codec, format);
    }

    private void processOutputBuffersChanged() {
        this.outputBuffers = this.codec.getOutputBuffers();
    }

    protected void renderToEndOfStream() throws ExoPlaybackException {
    }

    private void processEndOfStream() throws ExoPlaybackException {
        if (this.codecReinitializationState == 2) {
            releaseCodec();
            maybeInitCodec();
        } else {
            this.outputStreamEnded = true;
            renderToEndOfStream();
        }
    }

    private boolean shouldSkipOutputBuffer(long presentationTimeUs) {
        int size = this.decodeOnlyPresentationTimestamps.size();
        for (int i = 0; i < size; i++) {
            if (this.decodeOnlyPresentationTimestamps.get(i).longValue() == presentationTimeUs) {
                this.decodeOnlyPresentationTimestamps.remove(i);
                return true;
            }
        }
        return false;
    }

    private static boolean codecNeedsFlushWorkaround(String name) {
        return Util.SDK_INT < 18 || (Util.SDK_INT == 18 && ("OMX.SEC.avc.dec".equals(name) || "OMX.SEC.avc.dec.secure".equals(name))) || (Util.SDK_INT == 19 && Util.MODEL.startsWith("SM-G800") && ("OMX.Exynos.avc.dec".equals(name) || "OMX.Exynos.avc.dec.secure".equals(name)));
    }

    private static boolean codecNeedsAdaptationWorkaround(String name) {
        return Util.SDK_INT < 24 && ("OMX.Nvidia.h264.decode".equals(name) || "OMX.Nvidia.h264.decode.secure".equals(name)) && ("flounder".equals(Util.DEVICE) || "flounder_lte".equals(Util.DEVICE) || "grouper".equals(Util.DEVICE) || "tilapia".equals(Util.DEVICE));
    }

    private static boolean codecNeedsDiscardToSpsWorkaround(String name, Format format) {
        return Util.SDK_INT < 21 && format.initializationData.isEmpty() && "OMX.MTK.VIDEO.DECODER.AVC".equals(name);
    }

    private static boolean codecNeedsEosPropagationWorkaround(String name) {
        return Util.SDK_INT <= 17 && ("OMX.rk.video_decoder.avc".equals(name) || "OMX.allwinner.video.decoder.avc".equals(name));
    }

    private static boolean codecNeedsEosFlushWorkaround(String name) {
        return (Util.SDK_INT <= 23 && "OMX.google.vorbis.decoder".equals(name)) || (Util.SDK_INT <= 19 && "hb2000".equals(Util.DEVICE) && ("OMX.amlogic.avc.decoder.awesome".equals(name) || "OMX.amlogic.avc.decoder.awesome.secure".equals(name)));
    }

    private static boolean codecNeedsEosOutputExceptionWorkaround(String name) {
        return Util.SDK_INT == 21 && "OMX.google.aac.decoder".equals(name);
    }

    private static boolean codecNeedsMonoChannelCountWorkaround(String name, Format format) {
        return Util.SDK_INT <= 18 && format.channelCount == 1 && "OMX.MTK.AUDIO.DECODER.MP3".equals(name);
    }

    private static boolean codecNeedsDisableAdaptationWorkaround(String name) {
        return Util.SDK_INT <= 19 && Util.MODEL.equals("ODROID-XU3") && ("OMX.Exynos.AVC.Decoder".equals(name) || "OMX.Exynos.AVC.Decoder.secure".equals(name));
    }
}
