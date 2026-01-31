package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.audio.AudioProcessor;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* loaded from: classes.dex */
final class ResamplingAudioProcessor implements AudioProcessor {
    private boolean inputEnded;
    private int sampleRateHz = -1;
    private int channelCount = -1;
    private int encoding = 0;
    private ByteBuffer buffer = EMPTY_BUFFER;
    private ByteBuffer outputBuffer = EMPTY_BUFFER;

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public boolean configure(int sampleRateHz, int channelCount, int encoding) throws AudioProcessor.UnhandledFormatException {
        if (encoding != 3 && encoding != 2 && encoding != Integer.MIN_VALUE && encoding != 1073741824) {
            throw new AudioProcessor.UnhandledFormatException(sampleRateHz, channelCount, encoding);
        }
        if (this.sampleRateHz == sampleRateHz && this.channelCount == channelCount && this.encoding == encoding) {
            return false;
        }
        this.sampleRateHz = sampleRateHz;
        this.channelCount = channelCount;
        this.encoding = encoding;
        if (encoding == 2) {
            this.buffer = EMPTY_BUFFER;
        }
        return true;
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public boolean isActive() {
        return (this.encoding == 0 || this.encoding == 2) ? false : true;
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
        int resampledSize;
        int position = inputBuffer.position();
        int limit = inputBuffer.limit();
        int size = limit - position;
        switch (this.encoding) {
            case Integer.MIN_VALUE:
                resampledSize = (size / 3) * 2;
                break;
            case 3:
                resampledSize = size * 2;
                break;
            case 1073741824:
                resampledSize = size / 2;
                break;
            default:
                throw new IllegalStateException();
        }
        if (this.buffer.capacity() < resampledSize) {
            this.buffer = ByteBuffer.allocateDirect(resampledSize).order(ByteOrder.nativeOrder());
        } else {
            this.buffer.clear();
        }
        switch (this.encoding) {
            case Integer.MIN_VALUE:
                for (int i = position; i < limit; i += 3) {
                    this.buffer.put(inputBuffer.get(i + 1));
                    this.buffer.put(inputBuffer.get(i + 2));
                }
                break;
            case 3:
                for (int i2 = position; i2 < limit; i2++) {
                    this.buffer.put((byte) 0);
                    this.buffer.put((byte) ((inputBuffer.get(i2) & 255) - 128));
                }
                break;
            case 1073741824:
                for (int i3 = position; i3 < limit; i3 += 4) {
                    this.buffer.put(inputBuffer.get(i3 + 2));
                    this.buffer.put(inputBuffer.get(i3 + 3));
                }
                break;
            default:
                throw new IllegalStateException();
        }
        inputBuffer.position(inputBuffer.limit());
        this.buffer.flip();
        this.outputBuffer = this.buffer;
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public void queueEndOfStream() {
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
        return this.inputEnded && this.outputBuffer == EMPTY_BUFFER;
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public void flush() {
        this.outputBuffer = EMPTY_BUFFER;
        this.inputEnded = false;
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public void reset() {
        flush();
        this.buffer = EMPTY_BUFFER;
        this.sampleRateHz = -1;
        this.channelCount = -1;
        this.encoding = 0;
    }
}
