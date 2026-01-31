package com.google.android.exoplayer2.audio;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTimestamp;
import android.os.ConditionVariable;
import android.os.SystemClock;
import android.util.Log;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.audio.AudioProcessor;
import com.google.android.exoplayer2.upstream.cache.CacheDataSink;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.LinkedList;
import org.apache.commons.io.FileUtils;

/* loaded from: classes.dex */
public final class AudioTrack {
    private static final int BUFFER_MULTIPLICATION_FACTOR = 4;
    public static final long CURRENT_POSITION_NOT_SET = Long.MIN_VALUE;
    private static final int ERROR_BAD_VALUE = -2;
    private static final long MAX_AUDIO_TIMESTAMP_OFFSET_US = 5000000;
    private static final long MAX_BUFFER_DURATION_US = 750000;
    private static final long MAX_LATENCY_US = 5000000;
    private static final int MAX_PLAYHEAD_OFFSET_COUNT = 10;
    private static final long MIN_BUFFER_DURATION_US = 250000;
    private static final int MIN_PLAYHEAD_OFFSET_SAMPLE_INTERVAL_US = 30000;
    private static final int MIN_TIMESTAMP_SAMPLE_INTERVAL_US = 500000;
    private static final int MODE_STATIC = 0;
    private static final int MODE_STREAM = 1;
    private static final long PASSTHROUGH_BUFFER_DURATION_US = 250000;
    private static final int PLAYSTATE_PAUSED = 2;
    private static final int PLAYSTATE_PLAYING = 3;
    private static final int PLAYSTATE_STOPPED = 1;
    private static final int SONIC_MIN_BYTES_FOR_SPEEDUP = 1024;
    private static final int START_IN_SYNC = 1;
    private static final int START_NEED_SYNC = 2;
    private static final int START_NOT_SET = 0;
    private static final int STATE_INITIALIZED = 1;
    private static final String TAG = "AudioTrack";

    @SuppressLint({"InlinedApi"})
    private static final int WRITE_NON_BLOCKING = 1;
    public static boolean enablePreV21AudioSessionWorkaround = false;
    public static boolean failOnSpuriousAudioTimestamp = false;
    private final AudioCapabilities audioCapabilities;
    private AudioProcessor[] audioProcessors;
    private int audioSessionId;
    private boolean audioTimestampSet;
    private android.media.AudioTrack audioTrack;
    private final AudioTrackUtil audioTrackUtil;
    private ByteBuffer avSyncHeader;
    private final AudioProcessor[] availableAudioProcessors;
    private int bufferSize;
    private long bufferSizeUs;
    private int bytesUntilNextAvSync;
    private int channelConfig;
    private final ChannelMappingAudioProcessor channelMappingAudioProcessor;
    private int drainingAudioProcessorIndex;
    private PlaybackParameters drainingPlaybackParameters;
    private int encoding;
    private int framesPerEncodedSample;
    private Method getLatencyMethod;
    private boolean handledEndOfStream;
    private boolean hasData;
    private ByteBuffer inputBuffer;
    private android.media.AudioTrack keepSessionIdAudioTrack;
    private long lastFeedElapsedRealtimeMs;
    private long lastPlayheadSampleTimeUs;
    private long lastTimestampSampleTimeUs;
    private long latencyUs;
    private final Listener listener;
    private int nextPlayheadOffsetIndex;
    private ByteBuffer outputBuffer;
    private ByteBuffer[] outputBuffers;
    private int outputEncoding;
    private int outputPcmFrameSize;
    private boolean passthrough;
    private int pcmFrameSize;
    private PlaybackParameters playbackParameters;
    private final LinkedList<PlaybackParametersCheckpoint> playbackParametersCheckpoints;
    private long playbackParametersOffsetUs;
    private long playbackParametersPositionUs;
    private int playheadOffsetCount;
    private final long[] playheadOffsets;
    private boolean playing;
    private byte[] preV21OutputBuffer;
    private int preV21OutputBufferOffset;
    private final ConditionVariable releasingConditionVariable = new ConditionVariable(true);
    private long resumeSystemTimeUs;
    private int sampleRate;
    private long smoothedPlayheadOffsetUs;
    private final SonicAudioProcessor sonicAudioProcessor;
    private int startMediaTimeState;
    private long startMediaTimeUs;
    private int streamType;
    private long submittedEncodedFrames;
    private long submittedPcmBytes;
    private boolean tunneling;
    private float volume;
    private long writtenEncodedFrames;
    private long writtenPcmBytes;

    public interface Listener {
        void onAudioSessionId(int i);

        void onPositionDiscontinuity();

        void onUnderrun(int i, long j, long j2);
    }

    public static final class ConfigurationException extends Exception {
        public ConfigurationException(Throwable cause) {
            super(cause);
        }

        public ConfigurationException(String message) {
            super(message);
        }
    }

    public static final class InitializationException extends Exception {
        public final int audioTrackState;

        public InitializationException(int audioTrackState, int sampleRate, int channelConfig, int bufferSize) {
            super("AudioTrack init failed: " + audioTrackState + ", Config(" + sampleRate + ", " + channelConfig + ", " + bufferSize + ")");
            this.audioTrackState = audioTrackState;
        }
    }

    public static final class WriteException extends Exception {
        public final int errorCode;

        public WriteException(int errorCode) {
            super("AudioTrack write failed: " + errorCode);
            this.errorCode = errorCode;
        }
    }

    public static final class InvalidAudioTrackTimestampException extends RuntimeException {
        public InvalidAudioTrackTimestampException(String detailMessage) {
            super(detailMessage);
        }
    }

    public AudioTrack(AudioCapabilities audioCapabilities, AudioProcessor[] audioProcessors, Listener listener) {
        this.audioCapabilities = audioCapabilities;
        this.listener = listener;
        if (Util.SDK_INT >= 18) {
            try {
                this.getLatencyMethod = android.media.AudioTrack.class.getMethod("getLatency", (Class[]) null);
            } catch (NoSuchMethodException e) {
            }
        }
        if (Util.SDK_INT >= 19) {
            this.audioTrackUtil = new AudioTrackUtilV19();
        } else {
            this.audioTrackUtil = new AudioTrackUtil();
        }
        this.channelMappingAudioProcessor = new ChannelMappingAudioProcessor();
        this.sonicAudioProcessor = new SonicAudioProcessor();
        this.availableAudioProcessors = new AudioProcessor[audioProcessors.length + 3];
        this.availableAudioProcessors[0] = new ResamplingAudioProcessor();
        this.availableAudioProcessors[1] = this.channelMappingAudioProcessor;
        System.arraycopy(audioProcessors, 0, this.availableAudioProcessors, 2, audioProcessors.length);
        this.availableAudioProcessors[audioProcessors.length + 2] = this.sonicAudioProcessor;
        this.playheadOffsets = new long[10];
        this.volume = 1.0f;
        this.startMediaTimeState = 0;
        this.streamType = 3;
        this.audioSessionId = 0;
        this.playbackParameters = PlaybackParameters.DEFAULT;
        this.drainingAudioProcessorIndex = -1;
        this.audioProcessors = new AudioProcessor[0];
        this.outputBuffers = new ByteBuffer[0];
        this.playbackParametersCheckpoints = new LinkedList<>();
    }

    public boolean isPassthroughSupported(String mimeType) {
        return this.audioCapabilities != null && this.audioCapabilities.supportsEncoding(getEncodingForMimeType(mimeType));
    }

    public long getCurrentPositionUs(boolean sourceEnded) {
        long positionUs;
        if (!hasCurrentPositionUs()) {
            return Long.MIN_VALUE;
        }
        if (this.audioTrack.getPlayState() == 3) {
            maybeSampleSyncParams();
        }
        long systemClockUs = System.nanoTime() / 1000;
        if (this.audioTimestampSet) {
            long elapsedSinceTimestampUs = systemClockUs - (this.audioTrackUtil.getTimestampNanoTime() / 1000);
            long elapsedSinceTimestampFrames = durationUsToFrames(elapsedSinceTimestampUs);
            long elapsedFrames = this.audioTrackUtil.getTimestampFramePosition() + elapsedSinceTimestampFrames;
            positionUs = framesToDurationUs(elapsedFrames);
        } else {
            if (this.playheadOffsetCount == 0) {
                positionUs = this.audioTrackUtil.getPositionUs();
            } else {
                positionUs = systemClockUs + this.smoothedPlayheadOffsetUs;
            }
            if (!sourceEnded) {
                positionUs -= this.latencyUs;
            }
        }
        return this.startMediaTimeUs + applySpeedup(positionUs);
    }

    public void configure(String mimeType, int channelCount, int sampleRate, int pcmEncoding, int specifiedBufferSize) throws IllegalStateException, ConfigurationException {
        configure(mimeType, channelCount, sampleRate, pcmEncoding, specifiedBufferSize, null);
    }

    public void configure(String mimeType, int channelCount, int sampleRate, int pcmEncoding, int specifiedBufferSize, int[] outputChannels) throws IllegalStateException, ConfigurationException {
        int channelConfig;
        boolean passthrough = !MimeTypes.AUDIO_RAW.equals(mimeType);
        int encoding = passthrough ? getEncodingForMimeType(mimeType) : pcmEncoding;
        boolean flush = false;
        if (!passthrough) {
            this.pcmFrameSize = Util.getPcmFrameSize(pcmEncoding, channelCount);
            this.channelMappingAudioProcessor.setChannelMap(outputChannels);
            for (AudioProcessor audioProcessor : this.availableAudioProcessors) {
                try {
                    flush |= audioProcessor.configure(sampleRate, channelCount, encoding);
                    if (audioProcessor.isActive()) {
                        channelCount = audioProcessor.getOutputChannelCount();
                        encoding = audioProcessor.getOutputEncoding();
                    }
                } catch (AudioProcessor.UnhandledFormatException e) {
                    throw new ConfigurationException(e);
                }
            }
            if (flush) {
                resetAudioProcessors();
            }
        }
        switch (channelCount) {
            case 1:
                channelConfig = 4;
                break;
            case 2:
                channelConfig = 12;
                break;
            case 3:
                channelConfig = 28;
                break;
            case 4:
                channelConfig = 204;
                break;
            case 5:
                channelConfig = 220;
                break;
            case 6:
                channelConfig = 252;
                break;
            case 7:
                channelConfig = 1276;
                break;
            case 8:
                channelConfig = C.CHANNEL_OUT_7POINT1_SURROUND;
                break;
            default:
                throw new ConfigurationException("Unsupported channel count: " + channelCount);
        }
        if (Util.SDK_INT <= 23 && "foster".equals(Util.DEVICE) && "NVIDIA".equals(Util.MANUFACTURER)) {
            switch (channelCount) {
                case 3:
                case 5:
                    channelConfig = 252;
                    break;
                case 7:
                    channelConfig = C.CHANNEL_OUT_7POINT1_SURROUND;
                    break;
            }
        }
        if (Util.SDK_INT <= 25 && "fugu".equals(Util.DEVICE) && passthrough && channelCount == 1) {
            channelConfig = 12;
        }
        if (flush || !isInitialized() || this.encoding != encoding || this.sampleRate != sampleRate || this.channelConfig != channelConfig) {
            reset();
            this.encoding = encoding;
            this.passthrough = passthrough;
            this.sampleRate = sampleRate;
            this.channelConfig = channelConfig;
            if (!passthrough) {
                encoding = 2;
            }
            this.outputEncoding = encoding;
            this.outputPcmFrameSize = Util.getPcmFrameSize(2, channelCount);
            if (specifiedBufferSize != 0) {
                this.bufferSize = specifiedBufferSize;
            } else if (passthrough) {
                if (this.outputEncoding == 5 || this.outputEncoding == 6) {
                    this.bufferSize = CacheDataSink.DEFAULT_BUFFER_SIZE;
                } else {
                    this.bufferSize = 49152;
                }
            } else {
                int minBufferSize = android.media.AudioTrack.getMinBufferSize(sampleRate, channelConfig, this.outputEncoding);
                Assertions.checkState(minBufferSize != -2);
                int multipliedBufferSize = minBufferSize * 4;
                int minAppBufferSize = ((int) durationUsToFrames(250000L)) * this.outputPcmFrameSize;
                int maxAppBufferSize = (int) Math.max(minBufferSize, durationUsToFrames(MAX_BUFFER_DURATION_US) * this.outputPcmFrameSize);
                if (multipliedBufferSize >= minAppBufferSize) {
                    minAppBufferSize = multipliedBufferSize > maxAppBufferSize ? maxAppBufferSize : multipliedBufferSize;
                }
                this.bufferSize = minAppBufferSize;
            }
            this.bufferSizeUs = passthrough ? C.TIME_UNSET : framesToDurationUs(this.bufferSize / this.outputPcmFrameSize);
            setPlaybackParameters(this.playbackParameters);
        }
    }

    private void resetAudioProcessors() {
        ArrayList<AudioProcessor> newAudioProcessors = new ArrayList<>();
        for (AudioProcessor audioProcessor : this.availableAudioProcessors) {
            if (audioProcessor.isActive()) {
                newAudioProcessors.add(audioProcessor);
            } else {
                audioProcessor.flush();
            }
        }
        int count = newAudioProcessors.size();
        this.audioProcessors = (AudioProcessor[]) newAudioProcessors.toArray(new AudioProcessor[count]);
        this.outputBuffers = new ByteBuffer[count];
        for (int i = 0; i < count; i++) {
            AudioProcessor audioProcessor2 = this.audioProcessors[i];
            audioProcessor2.flush();
            this.outputBuffers[i] = audioProcessor2.getOutput();
        }
    }

    private void initialize() throws InitializationException {
        this.releasingConditionVariable.block();
        if (this.tunneling) {
            this.audioTrack = createHwAvSyncAudioTrackV21(this.sampleRate, this.channelConfig, this.outputEncoding, this.bufferSize, this.audioSessionId);
        } else if (this.audioSessionId == 0) {
            this.audioTrack = new android.media.AudioTrack(this.streamType, this.sampleRate, this.channelConfig, this.outputEncoding, this.bufferSize, 1);
        } else {
            this.audioTrack = new android.media.AudioTrack(this.streamType, this.sampleRate, this.channelConfig, this.outputEncoding, this.bufferSize, 1, this.audioSessionId);
        }
        checkAudioTrackInitialized();
        int audioSessionId = this.audioTrack.getAudioSessionId();
        if (enablePreV21AudioSessionWorkaround && Util.SDK_INT < 21) {
            if (this.keepSessionIdAudioTrack != null && audioSessionId != this.keepSessionIdAudioTrack.getAudioSessionId()) {
                releaseKeepSessionIdAudioTrack();
            }
            if (this.keepSessionIdAudioTrack == null) {
                this.keepSessionIdAudioTrack = new android.media.AudioTrack(this.streamType, 4000, 4, 2, 2, 0, audioSessionId);
            }
        }
        if (this.audioSessionId != audioSessionId) {
            this.audioSessionId = audioSessionId;
            this.listener.onAudioSessionId(audioSessionId);
        }
        this.audioTrackUtil.reconfigure(this.audioTrack, needsPassthroughWorkarounds());
        setVolumeInternal();
        this.hasData = false;
    }

    public void play() throws IllegalStateException {
        this.playing = true;
        if (isInitialized()) {
            this.resumeSystemTimeUs = System.nanoTime() / 1000;
            this.audioTrack.play();
        }
    }

    public void handleDiscontinuity() {
        if (this.startMediaTimeState == 1) {
            this.startMediaTimeState = 2;
        }
    }

    public boolean handleBuffer(ByteBuffer buffer, long presentationTimeUs) throws IllegalStateException, InitializationException, WriteException {
        Assertions.checkArgument(this.inputBuffer == null || buffer == this.inputBuffer);
        if (!isInitialized()) {
            initialize();
            if (this.playing) {
                play();
            }
        }
        if (needsPassthroughWorkarounds()) {
            if (this.audioTrack.getPlayState() == 2) {
                this.hasData = false;
                return false;
            }
            if (this.audioTrack.getPlayState() == 1 && this.audioTrackUtil.getPlaybackHeadPosition() != 0) {
                return false;
            }
        }
        boolean hadData = this.hasData;
        this.hasData = hasPendingData();
        if (hadData && !this.hasData && this.audioTrack.getPlayState() != 1) {
            long elapsedSinceLastFeedMs = SystemClock.elapsedRealtime() - this.lastFeedElapsedRealtimeMs;
            this.listener.onUnderrun(this.bufferSize, C.usToMs(this.bufferSizeUs), elapsedSinceLastFeedMs);
        }
        if (this.inputBuffer == null) {
            if (!buffer.hasRemaining()) {
                return true;
            }
            if (this.passthrough && this.framesPerEncodedSample == 0) {
                this.framesPerEncodedSample = getFramesPerEncodedSample(this.outputEncoding, buffer);
            }
            if (this.drainingPlaybackParameters != null) {
                if (!drainAudioProcessorsToEndOfStream()) {
                    return false;
                }
                this.playbackParametersCheckpoints.add(new PlaybackParametersCheckpoint(this.drainingPlaybackParameters, Math.max(0L, presentationTimeUs), framesToDurationUs(getWrittenFrames())));
                this.drainingPlaybackParameters = null;
                resetAudioProcessors();
            }
            if (this.startMediaTimeState == 0) {
                this.startMediaTimeUs = Math.max(0L, presentationTimeUs);
                this.startMediaTimeState = 1;
            } else {
                long expectedPresentationTimeUs = this.startMediaTimeUs + framesToDurationUs(getSubmittedFrames());
                if (this.startMediaTimeState == 1 && Math.abs(expectedPresentationTimeUs - presentationTimeUs) > 200000) {
                    Log.e(TAG, "Discontinuity detected [expected " + expectedPresentationTimeUs + ", got " + presentationTimeUs + "]");
                    this.startMediaTimeState = 2;
                }
                if (this.startMediaTimeState == 2) {
                    this.startMediaTimeUs += presentationTimeUs - expectedPresentationTimeUs;
                    this.startMediaTimeState = 1;
                    this.listener.onPositionDiscontinuity();
                }
            }
            if (this.passthrough) {
                this.submittedEncodedFrames += this.framesPerEncodedSample;
            } else {
                this.submittedPcmBytes += buffer.remaining();
            }
            this.inputBuffer = buffer;
        }
        if (this.passthrough) {
            writeBuffer(this.inputBuffer, presentationTimeUs);
        } else {
            processBuffers(presentationTimeUs);
        }
        if (!this.inputBuffer.hasRemaining()) {
            this.inputBuffer = null;
            return true;
        }
        return false;
    }

    private void processBuffers(long avSyncPresentationTimeUs) throws WriteException {
        ByteBuffer input;
        int count = this.audioProcessors.length;
        int index = count;
        while (index >= 0) {
            if (index > 0) {
                input = this.outputBuffers[index - 1];
            } else {
                input = this.inputBuffer != null ? this.inputBuffer : AudioProcessor.EMPTY_BUFFER;
            }
            if (index == count) {
                writeBuffer(input, avSyncPresentationTimeUs);
            } else {
                AudioProcessor audioProcessor = this.audioProcessors[index];
                audioProcessor.queueInput(input);
                ByteBuffer output = audioProcessor.getOutput();
                this.outputBuffers[index] = output;
                if (output.hasRemaining()) {
                    index++;
                }
            }
            if (!input.hasRemaining()) {
                index--;
            } else {
                return;
            }
        }
    }

    private boolean writeBuffer(ByteBuffer buffer, long avSyncPresentationTimeUs) throws WriteException {
        if (!buffer.hasRemaining()) {
            return true;
        }
        if (this.outputBuffer != null) {
            Assertions.checkArgument(this.outputBuffer == buffer);
        } else {
            this.outputBuffer = buffer;
            if (Util.SDK_INT < 21) {
                int bytesRemaining = buffer.remaining();
                if (this.preV21OutputBuffer == null || this.preV21OutputBuffer.length < bytesRemaining) {
                    this.preV21OutputBuffer = new byte[bytesRemaining];
                }
                int originalPosition = buffer.position();
                buffer.get(this.preV21OutputBuffer, 0, bytesRemaining);
                buffer.position(originalPosition);
                this.preV21OutputBufferOffset = 0;
            }
        }
        int bytesRemaining2 = buffer.remaining();
        int bytesWritten = 0;
        if (Util.SDK_INT < 21) {
            int bytesPending = (int) (this.writtenPcmBytes - (this.audioTrackUtil.getPlaybackHeadPosition() * this.outputPcmFrameSize));
            int bytesToWrite = this.bufferSize - bytesPending;
            if (bytesToWrite > 0) {
                bytesWritten = this.audioTrack.write(this.preV21OutputBuffer, this.preV21OutputBufferOffset, Math.min(bytesRemaining2, bytesToWrite));
                if (bytesWritten > 0) {
                    this.preV21OutputBufferOffset += bytesWritten;
                    buffer.position(buffer.position() + bytesWritten);
                }
            }
        } else if (this.tunneling) {
            Assertions.checkState(avSyncPresentationTimeUs != C.TIME_UNSET);
            bytesWritten = writeNonBlockingWithAvSyncV21(this.audioTrack, buffer, bytesRemaining2, avSyncPresentationTimeUs);
        } else {
            bytesWritten = writeNonBlockingV21(this.audioTrack, buffer, bytesRemaining2);
        }
        this.lastFeedElapsedRealtimeMs = SystemClock.elapsedRealtime();
        if (bytesWritten < 0) {
            throw new WriteException(bytesWritten);
        }
        if (!this.passthrough) {
            this.writtenPcmBytes += bytesWritten;
        }
        if (bytesWritten == bytesRemaining2) {
            if (this.passthrough) {
                this.writtenEncodedFrames += this.framesPerEncodedSample;
            }
            this.outputBuffer = null;
            return true;
        }
        return false;
    }

    public void playToEndOfStream() throws IllegalStateException, WriteException {
        if (!this.handledEndOfStream && isInitialized() && drainAudioProcessorsToEndOfStream()) {
            this.audioTrackUtil.handleEndOfStream(getWrittenFrames());
            this.bytesUntilNextAvSync = 0;
            this.handledEndOfStream = true;
        }
    }

    private boolean drainAudioProcessorsToEndOfStream() throws WriteException {
        boolean audioProcessorNeedsEndOfStream = false;
        if (this.drainingAudioProcessorIndex == -1) {
            this.drainingAudioProcessorIndex = this.passthrough ? this.audioProcessors.length : 0;
            audioProcessorNeedsEndOfStream = true;
        }
        while (this.drainingAudioProcessorIndex < this.audioProcessors.length) {
            AudioProcessor audioProcessor = this.audioProcessors[this.drainingAudioProcessorIndex];
            if (audioProcessorNeedsEndOfStream) {
                audioProcessor.queueEndOfStream();
            }
            processBuffers(C.TIME_UNSET);
            if (!audioProcessor.isEnded()) {
                return false;
            }
            audioProcessorNeedsEndOfStream = true;
            this.drainingAudioProcessorIndex++;
        }
        if (this.outputBuffer != null) {
            writeBuffer(this.outputBuffer, C.TIME_UNSET);
            if (this.outputBuffer != null) {
                return false;
            }
        }
        this.drainingAudioProcessorIndex = -1;
        return true;
    }

    public boolean isEnded() {
        return !isInitialized() || (this.handledEndOfStream && !hasPendingData());
    }

    public boolean hasPendingData() {
        return isInitialized() && (getWrittenFrames() > this.audioTrackUtil.getPlaybackHeadPosition() || overrideHasPendingData());
    }

    public PlaybackParameters setPlaybackParameters(PlaybackParameters playbackParameters) {
        PlaybackParameters lastSetPlaybackParameters;
        if (this.passthrough) {
            this.playbackParameters = PlaybackParameters.DEFAULT;
            return this.playbackParameters;
        }
        PlaybackParameters playbackParameters2 = new PlaybackParameters(this.sonicAudioProcessor.setSpeed(playbackParameters.speed), this.sonicAudioProcessor.setPitch(playbackParameters.pitch));
        if (this.drainingPlaybackParameters != null) {
            lastSetPlaybackParameters = this.drainingPlaybackParameters;
        } else {
            lastSetPlaybackParameters = !this.playbackParametersCheckpoints.isEmpty() ? this.playbackParametersCheckpoints.getLast().playbackParameters : this.playbackParameters;
        }
        if (!playbackParameters2.equals(lastSetPlaybackParameters)) {
            if (isInitialized()) {
                this.drainingPlaybackParameters = playbackParameters2;
            } else {
                this.playbackParameters = playbackParameters2;
            }
        }
        return this.playbackParameters;
    }

    public PlaybackParameters getPlaybackParameters() {
        return this.playbackParameters;
    }

    public void setStreamType(int streamType) throws IllegalStateException {
        if (this.streamType != streamType) {
            this.streamType = streamType;
            if (!this.tunneling) {
                reset();
                this.audioSessionId = 0;
            }
        }
    }

    public void setAudioSessionId(int audioSessionId) throws IllegalStateException {
        if (this.audioSessionId != audioSessionId) {
            this.audioSessionId = audioSessionId;
            reset();
        }
    }

    public void enableTunnelingV21(int tunnelingAudioSessionId) throws IllegalStateException {
        Assertions.checkState(Util.SDK_INT >= 21);
        if (!this.tunneling || this.audioSessionId != tunnelingAudioSessionId) {
            this.tunneling = true;
            this.audioSessionId = tunnelingAudioSessionId;
            reset();
        }
    }

    public void disableTunneling() throws IllegalStateException {
        if (this.tunneling) {
            this.tunneling = false;
            this.audioSessionId = 0;
            reset();
        }
    }

    public void setVolume(float volume) {
        if (this.volume != volume) {
            this.volume = volume;
            setVolumeInternal();
        }
    }

    private void setVolumeInternal() {
        if (isInitialized()) {
            if (Util.SDK_INT >= 21) {
                setVolumeInternalV21(this.audioTrack, this.volume);
            } else {
                setVolumeInternalV3(this.audioTrack, this.volume);
            }
        }
    }

    public void pause() throws IllegalStateException {
        this.playing = false;
        if (isInitialized()) {
            resetSyncParams();
            this.audioTrackUtil.pause();
        }
    }

    /* JADX WARN: Type inference failed for: r4v16, types: [com.google.android.exoplayer2.audio.AudioTrack$1] */
    public void reset() throws IllegalStateException {
        if (isInitialized()) {
            this.submittedPcmBytes = 0L;
            this.submittedEncodedFrames = 0L;
            this.writtenPcmBytes = 0L;
            this.writtenEncodedFrames = 0L;
            this.framesPerEncodedSample = 0;
            if (this.drainingPlaybackParameters != null) {
                this.playbackParameters = this.drainingPlaybackParameters;
                this.drainingPlaybackParameters = null;
            } else if (!this.playbackParametersCheckpoints.isEmpty()) {
                this.playbackParameters = this.playbackParametersCheckpoints.getLast().playbackParameters;
            }
            this.playbackParametersCheckpoints.clear();
            this.playbackParametersOffsetUs = 0L;
            this.playbackParametersPositionUs = 0L;
            this.inputBuffer = null;
            this.outputBuffer = null;
            for (int i = 0; i < this.audioProcessors.length; i++) {
                AudioProcessor audioProcessor = this.audioProcessors[i];
                audioProcessor.flush();
                this.outputBuffers[i] = audioProcessor.getOutput();
            }
            this.handledEndOfStream = false;
            this.drainingAudioProcessorIndex = -1;
            this.avSyncHeader = null;
            this.bytesUntilNextAvSync = 0;
            this.startMediaTimeState = 0;
            this.latencyUs = 0L;
            resetSyncParams();
            int playState = this.audioTrack.getPlayState();
            if (playState == 3) {
                this.audioTrack.pause();
            }
            final android.media.AudioTrack toRelease = this.audioTrack;
            this.audioTrack = null;
            this.audioTrackUtil.reconfigure(null, false);
            this.releasingConditionVariable.close();
            new Thread() { // from class: com.google.android.exoplayer2.audio.AudioTrack.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    try {
                        toRelease.flush();
                        toRelease.release();
                    } finally {
                        AudioTrack.this.releasingConditionVariable.open();
                    }
                }
            }.start();
        }
    }

    public void release() throws IllegalStateException {
        reset();
        releaseKeepSessionIdAudioTrack();
        for (AudioProcessor audioProcessor : this.availableAudioProcessors) {
            audioProcessor.reset();
        }
        this.audioSessionId = 0;
        this.playing = false;
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [com.google.android.exoplayer2.audio.AudioTrack$2] */
    private void releaseKeepSessionIdAudioTrack() {
        if (this.keepSessionIdAudioTrack != null) {
            final android.media.AudioTrack toRelease = this.keepSessionIdAudioTrack;
            this.keepSessionIdAudioTrack = null;
            new Thread() { // from class: com.google.android.exoplayer2.audio.AudioTrack.2
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    toRelease.release();
                }
            }.start();
        }
    }

    private boolean hasCurrentPositionUs() {
        return isInitialized() && this.startMediaTimeState != 0;
    }

    private long applySpeedup(long positionUs) {
        while (!this.playbackParametersCheckpoints.isEmpty() && positionUs >= this.playbackParametersCheckpoints.getFirst().positionUs) {
            PlaybackParametersCheckpoint checkpoint = this.playbackParametersCheckpoints.remove();
            this.playbackParameters = checkpoint.playbackParameters;
            this.playbackParametersPositionUs = checkpoint.positionUs;
            this.playbackParametersOffsetUs = checkpoint.mediaTimeUs - this.startMediaTimeUs;
        }
        if (this.playbackParameters.speed == 1.0f) {
            return (this.playbackParametersOffsetUs + positionUs) - this.playbackParametersPositionUs;
        }
        if (this.playbackParametersCheckpoints.isEmpty() && this.sonicAudioProcessor.getOutputByteCount() >= FileUtils.ONE_KB) {
            return Util.scaleLargeTimestamp(positionUs - this.playbackParametersPositionUs, this.sonicAudioProcessor.getInputByteCount(), this.sonicAudioProcessor.getOutputByteCount()) + this.playbackParametersOffsetUs;
        }
        return this.playbackParametersOffsetUs + ((long) (this.playbackParameters.speed * (positionUs - this.playbackParametersPositionUs)));
    }

    private void maybeSampleSyncParams() {
        long playbackPositionUs = this.audioTrackUtil.getPositionUs();
        if (playbackPositionUs != 0) {
            long systemClockUs = System.nanoTime() / 1000;
            if (systemClockUs - this.lastPlayheadSampleTimeUs >= 30000) {
                this.playheadOffsets[this.nextPlayheadOffsetIndex] = playbackPositionUs - systemClockUs;
                this.nextPlayheadOffsetIndex = (this.nextPlayheadOffsetIndex + 1) % 10;
                if (this.playheadOffsetCount < 10) {
                    this.playheadOffsetCount++;
                }
                this.lastPlayheadSampleTimeUs = systemClockUs;
                this.smoothedPlayheadOffsetUs = 0L;
                for (int i = 0; i < this.playheadOffsetCount; i++) {
                    this.smoothedPlayheadOffsetUs += this.playheadOffsets[i] / this.playheadOffsetCount;
                }
            }
            if (!needsPassthroughWorkarounds() && systemClockUs - this.lastTimestampSampleTimeUs >= 500000) {
                this.audioTimestampSet = this.audioTrackUtil.updateTimestamp();
                if (this.audioTimestampSet) {
                    long audioTimestampUs = this.audioTrackUtil.getTimestampNanoTime() / 1000;
                    long audioTimestampFramePosition = this.audioTrackUtil.getTimestampFramePosition();
                    if (audioTimestampUs < this.resumeSystemTimeUs) {
                        this.audioTimestampSet = false;
                    } else if (Math.abs(audioTimestampUs - systemClockUs) > 5000000) {
                        String message = "Spurious audio timestamp (system clock mismatch): " + audioTimestampFramePosition + ", " + audioTimestampUs + ", " + systemClockUs + ", " + playbackPositionUs;
                        if (failOnSpuriousAudioTimestamp) {
                            throw new InvalidAudioTrackTimestampException(message);
                        }
                        Log.w(TAG, message);
                        this.audioTimestampSet = false;
                    } else if (Math.abs(framesToDurationUs(audioTimestampFramePosition) - playbackPositionUs) > 5000000) {
                        String message2 = "Spurious audio timestamp (frame position mismatch): " + audioTimestampFramePosition + ", " + audioTimestampUs + ", " + systemClockUs + ", " + playbackPositionUs;
                        if (failOnSpuriousAudioTimestamp) {
                            throw new InvalidAudioTrackTimestampException(message2);
                        }
                        Log.w(TAG, message2);
                        this.audioTimestampSet = false;
                    }
                }
                if (this.getLatencyMethod != null && !this.passthrough) {
                    try {
                        this.latencyUs = (((Integer) this.getLatencyMethod.invoke(this.audioTrack, (Object[]) null)).intValue() * 1000) - this.bufferSizeUs;
                        this.latencyUs = Math.max(this.latencyUs, 0L);
                        if (this.latencyUs > 5000000) {
                            Log.w(TAG, "Ignoring impossibly large audio latency: " + this.latencyUs);
                            this.latencyUs = 0L;
                        }
                    } catch (Exception e) {
                        this.getLatencyMethod = null;
                    }
                }
                this.lastTimestampSampleTimeUs = systemClockUs;
            }
        }
    }

    private void checkAudioTrackInitialized() throws InitializationException {
        int state = this.audioTrack.getState();
        if (state == 1) {
            return;
        }
        try {
            this.audioTrack.release();
        } catch (Exception e) {
        } finally {
            this.audioTrack = null;
        }
        throw new InitializationException(state, this.sampleRate, this.channelConfig, this.bufferSize);
    }

    private boolean isInitialized() {
        return this.audioTrack != null;
    }

    private long framesToDurationUs(long frameCount) {
        return (C.MICROS_PER_SECOND * frameCount) / this.sampleRate;
    }

    private long durationUsToFrames(long durationUs) {
        return (this.sampleRate * durationUs) / C.MICROS_PER_SECOND;
    }

    private long getSubmittedFrames() {
        return this.passthrough ? this.submittedEncodedFrames : this.submittedPcmBytes / this.pcmFrameSize;
    }

    private long getWrittenFrames() {
        return this.passthrough ? this.writtenEncodedFrames : this.writtenPcmBytes / this.outputPcmFrameSize;
    }

    private void resetSyncParams() {
        this.smoothedPlayheadOffsetUs = 0L;
        this.playheadOffsetCount = 0;
        this.nextPlayheadOffsetIndex = 0;
        this.lastPlayheadSampleTimeUs = 0L;
        this.audioTimestampSet = false;
        this.lastTimestampSampleTimeUs = 0L;
    }

    private boolean needsPassthroughWorkarounds() {
        return Util.SDK_INT < 23 && (this.outputEncoding == 5 || this.outputEncoding == 6);
    }

    private boolean overrideHasPendingData() {
        return needsPassthroughWorkarounds() && this.audioTrack.getPlayState() == 2 && this.audioTrack.getPlaybackHeadPosition() == 0;
    }

    @TargetApi(21)
    private static android.media.AudioTrack createHwAvSyncAudioTrackV21(int sampleRate, int channelConfig, int encoding, int bufferSize, int sessionId) {
        AudioAttributes attributesBuilder = new AudioAttributes.Builder().setUsage(1).setContentType(3).setFlags(16).build();
        AudioFormat format = new AudioFormat.Builder().setChannelMask(channelConfig).setEncoding(encoding).setSampleRate(sampleRate).build();
        return new android.media.AudioTrack(attributesBuilder, format, bufferSize, 1, sessionId);
    }

    private static int getEncodingForMimeType(String mimeType) {
        switch (mimeType) {
            case "audio/ac3":
                return 5;
            case "audio/eac3":
                return 6;
            case "audio/vnd.dts":
                return 7;
            case "audio/vnd.dts.hd":
                return 8;
            default:
                return 0;
        }
    }

    private static int getFramesPerEncodedSample(int encoding, ByteBuffer buffer) {
        if (encoding == 7 || encoding == 8) {
            return DtsUtil.parseDtsAudioSampleCount(buffer);
        }
        if (encoding == 5) {
            return Ac3Util.getAc3SyncframeAudioSampleCount();
        }
        if (encoding == 6) {
            return Ac3Util.parseEAc3SyncframeAudioSampleCount(buffer);
        }
        throw new IllegalStateException("Unexpected audio encoding: " + encoding);
    }

    @TargetApi(21)
    private static int writeNonBlockingV21(android.media.AudioTrack audioTrack, ByteBuffer buffer, int size) {
        return audioTrack.write(buffer, size, 1);
    }

    @TargetApi(21)
    private int writeNonBlockingWithAvSyncV21(android.media.AudioTrack audioTrack, ByteBuffer buffer, int size, long presentationTimeUs) {
        if (this.avSyncHeader == null) {
            this.avSyncHeader = ByteBuffer.allocate(16);
            this.avSyncHeader.order(ByteOrder.BIG_ENDIAN);
            this.avSyncHeader.putInt(1431633921);
        }
        if (this.bytesUntilNextAvSync == 0) {
            this.avSyncHeader.putInt(4, size);
            this.avSyncHeader.putLong(8, 1000 * presentationTimeUs);
            this.avSyncHeader.position(0);
            this.bytesUntilNextAvSync = size;
        }
        int avSyncHeaderBytesRemaining = this.avSyncHeader.remaining();
        if (avSyncHeaderBytesRemaining > 0) {
            int result = audioTrack.write(this.avSyncHeader, avSyncHeaderBytesRemaining, 1);
            if (result < 0) {
                this.bytesUntilNextAvSync = 0;
                return result;
            }
            if (result < avSyncHeaderBytesRemaining) {
                return 0;
            }
        }
        int result2 = writeNonBlockingV21(audioTrack, buffer, size);
        if (result2 < 0) {
            this.bytesUntilNextAvSync = 0;
            return result2;
        }
        this.bytesUntilNextAvSync -= result2;
        return result2;
    }

    @TargetApi(21)
    private static void setVolumeInternalV21(android.media.AudioTrack audioTrack, float volume) {
        audioTrack.setVolume(volume);
    }

    private static void setVolumeInternalV3(android.media.AudioTrack audioTrack, float volume) {
        audioTrack.setStereoVolume(volume, volume);
    }

    private static class AudioTrackUtil {
        protected android.media.AudioTrack audioTrack;
        private long endPlaybackHeadPosition;
        private long lastRawPlaybackHeadPosition;
        private boolean needsPassthroughWorkaround;
        private long passthroughWorkaroundPauseOffset;
        private long rawPlaybackHeadWrapCount;
        private int sampleRate;
        private long stopPlaybackHeadPosition;
        private long stopTimestampUs;

        private AudioTrackUtil() {
        }

        public void reconfigure(android.media.AudioTrack audioTrack, boolean needsPassthroughWorkaround) {
            this.audioTrack = audioTrack;
            this.needsPassthroughWorkaround = needsPassthroughWorkaround;
            this.stopTimestampUs = C.TIME_UNSET;
            this.lastRawPlaybackHeadPosition = 0L;
            this.rawPlaybackHeadWrapCount = 0L;
            this.passthroughWorkaroundPauseOffset = 0L;
            if (audioTrack != null) {
                this.sampleRate = audioTrack.getSampleRate();
            }
        }

        public void handleEndOfStream(long writtenFrames) throws IllegalStateException {
            this.stopPlaybackHeadPosition = getPlaybackHeadPosition();
            this.stopTimestampUs = SystemClock.elapsedRealtime() * 1000;
            this.endPlaybackHeadPosition = writtenFrames;
            this.audioTrack.stop();
        }

        public void pause() throws IllegalStateException {
            if (this.stopTimestampUs == C.TIME_UNSET) {
                this.audioTrack.pause();
            }
        }

        public long getPlaybackHeadPosition() {
            if (this.stopTimestampUs != C.TIME_UNSET) {
                long elapsedTimeSinceStopUs = (SystemClock.elapsedRealtime() * 1000) - this.stopTimestampUs;
                long framesSinceStop = (this.sampleRate * elapsedTimeSinceStopUs) / C.MICROS_PER_SECOND;
                return Math.min(this.endPlaybackHeadPosition, this.stopPlaybackHeadPosition + framesSinceStop);
            }
            int state = this.audioTrack.getPlayState();
            if (state == 1) {
                return 0L;
            }
            long rawPlaybackHeadPosition = 4294967295L & this.audioTrack.getPlaybackHeadPosition();
            if (this.needsPassthroughWorkaround) {
                if (state == 2 && rawPlaybackHeadPosition == 0) {
                    this.passthroughWorkaroundPauseOffset = this.lastRawPlaybackHeadPosition;
                }
                rawPlaybackHeadPosition += this.passthroughWorkaroundPauseOffset;
            }
            if (this.lastRawPlaybackHeadPosition > rawPlaybackHeadPosition) {
                this.rawPlaybackHeadWrapCount++;
            }
            this.lastRawPlaybackHeadPosition = rawPlaybackHeadPosition;
            return (this.rawPlaybackHeadWrapCount << 32) + rawPlaybackHeadPosition;
        }

        public long getPositionUs() {
            return (getPlaybackHeadPosition() * C.MICROS_PER_SECOND) / this.sampleRate;
        }

        public boolean updateTimestamp() {
            return false;
        }

        public long getTimestampNanoTime() {
            throw new UnsupportedOperationException();
        }

        public long getTimestampFramePosition() {
            throw new UnsupportedOperationException();
        }
    }

    @TargetApi(19)
    private static class AudioTrackUtilV19 extends AudioTrackUtil {
        private final AudioTimestamp audioTimestamp;
        private long lastRawTimestampFramePosition;
        private long lastTimestampFramePosition;
        private long rawTimestampFramePositionWrapCount;

        public AudioTrackUtilV19() {
            super();
            this.audioTimestamp = new AudioTimestamp();
        }

        @Override // com.google.android.exoplayer2.audio.AudioTrack.AudioTrackUtil
        public void reconfigure(android.media.AudioTrack audioTrack, boolean needsPassthroughWorkaround) {
            super.reconfigure(audioTrack, needsPassthroughWorkaround);
            this.rawTimestampFramePositionWrapCount = 0L;
            this.lastRawTimestampFramePosition = 0L;
            this.lastTimestampFramePosition = 0L;
        }

        @Override // com.google.android.exoplayer2.audio.AudioTrack.AudioTrackUtil
        public boolean updateTimestamp() {
            boolean updated = this.audioTrack.getTimestamp(this.audioTimestamp);
            if (updated) {
                long rawFramePosition = this.audioTimestamp.framePosition;
                if (this.lastRawTimestampFramePosition > rawFramePosition) {
                    this.rawTimestampFramePositionWrapCount++;
                }
                this.lastRawTimestampFramePosition = rawFramePosition;
                this.lastTimestampFramePosition = (this.rawTimestampFramePositionWrapCount << 32) + rawFramePosition;
            }
            return updated;
        }

        @Override // com.google.android.exoplayer2.audio.AudioTrack.AudioTrackUtil
        public long getTimestampNanoTime() {
            return this.audioTimestamp.nanoTime;
        }

        @Override // com.google.android.exoplayer2.audio.AudioTrack.AudioTrackUtil
        public long getTimestampFramePosition() {
            return this.lastTimestampFramePosition;
        }
    }

    private static final class PlaybackParametersCheckpoint {
        private final long mediaTimeUs;
        private final PlaybackParameters playbackParameters;
        private final long positionUs;

        private PlaybackParametersCheckpoint(PlaybackParameters playbackParameters, long mediaTimeUs, long positionUs) {
            this.playbackParameters = playbackParameters;
            this.mediaTimeUs = mediaTimeUs;
            this.positionUs = positionUs;
        }
    }
}
