package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.audio.AudioProcessor;
import com.google.android.exoplayer2.util.Util;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

/* loaded from: classes.dex */
public final class SonicAudioProcessor implements AudioProcessor {
    private static final float CLOSE_THRESHOLD = 0.01f;
    public static final float MAXIMUM_PITCH = 8.0f;
    public static final float MAXIMUM_SPEED = 8.0f;
    public static final float MINIMUM_PITCH = 0.1f;
    public static final float MINIMUM_SPEED = 0.1f;
    private long inputBytes;
    private boolean inputEnded;
    private long outputBytes;
    private Sonic sonic;
    private float speed = 1.0f;
    private float pitch = 1.0f;
    private int channelCount = -1;
    private int sampleRateHz = -1;
    private ByteBuffer buffer = EMPTY_BUFFER;
    private ShortBuffer shortBuffer = this.buffer.asShortBuffer();
    private ByteBuffer outputBuffer = EMPTY_BUFFER;

    public float setSpeed(float speed) {
        this.speed = Util.constrainValue(speed, 0.1f, 8.0f);
        return this.speed;
    }

    public float setPitch(float pitch) {
        this.pitch = Util.constrainValue(pitch, 0.1f, 8.0f);
        return pitch;
    }

    public long getInputByteCount() {
        return this.inputBytes;
    }

    public long getOutputByteCount() {
        return this.outputBytes;
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public boolean configure(int sampleRateHz, int channelCount, int encoding) throws AudioProcessor.UnhandledFormatException {
        if (encoding != 2) {
            throw new AudioProcessor.UnhandledFormatException(sampleRateHz, channelCount, encoding);
        }
        if (this.sampleRateHz == sampleRateHz && this.channelCount == channelCount) {
            return false;
        }
        this.sampleRateHz = sampleRateHz;
        this.channelCount = channelCount;
        return true;
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public boolean isActive() {
        return Math.abs(this.speed - 1.0f) >= CLOSE_THRESHOLD || Math.abs(this.pitch - 1.0f) >= CLOSE_THRESHOLD;
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public int getOutputChannelCount() {
        return this.channelCount;
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public int getOutputEncoding() {
        return 2;
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public void queueInput(ByteBuffer inputBuffer) {
        if (inputBuffer.hasRemaining()) {
            ShortBuffer shortBuffer = inputBuffer.asShortBuffer();
            int inputSize = inputBuffer.remaining();
            this.inputBytes += inputSize;
            this.sonic.queueInput(shortBuffer);
            inputBuffer.position(inputBuffer.position() + inputSize);
        }
        int outputSize = this.sonic.getSamplesAvailable() * this.channelCount * 2;
        if (outputSize > 0) {
            if (this.buffer.capacity() < outputSize) {
                this.buffer = ByteBuffer.allocateDirect(outputSize).order(ByteOrder.nativeOrder());
                this.shortBuffer = this.buffer.asShortBuffer();
            } else {
                this.buffer.clear();
                this.shortBuffer.clear();
            }
            this.sonic.getOutput(this.shortBuffer);
            this.outputBytes += outputSize;
            this.buffer.limit(outputSize);
            this.outputBuffer = this.buffer;
        }
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public void queueEndOfStream() {
        this.sonic.queueEndOfStream();
        this.inputEnded = true;
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public ByteBuffer getOutput() {
        ByteBuffer outputBuffer = this.outputBuffer;
        this.outputBuffer = EMPTY_BUFFER;
        return outputBuffer;
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public boolean isEnded() {
        return this.inputEnded && (this.sonic == null || this.sonic.getSamplesAvailable() == 0);
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public void flush() {
        this.sonic = new Sonic(this.sampleRateHz, this.channelCount);
        this.sonic.setSpeed(this.speed);
        this.sonic.setPitch(this.pitch);
        this.outputBuffer = EMPTY_BUFFER;
        this.inputBytes = 0L;
        this.outputBytes = 0L;
        this.inputEnded = false;
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public void reset() {
        this.sonic = null;
        this.buffer = EMPTY_BUFFER;
        this.shortBuffer = this.buffer.asShortBuffer();
        this.outputBuffer = EMPTY_BUFFER;
        this.channelCount = -1;
        this.sampleRateHz = -1;
        this.inputBytes = 0L;
        this.outputBytes = 0L;
        this.inputEnded = false;
    }
}
