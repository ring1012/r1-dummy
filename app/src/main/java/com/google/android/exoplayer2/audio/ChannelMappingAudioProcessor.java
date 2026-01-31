package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.audio.AudioProcessor;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/* loaded from: classes.dex */
final class ChannelMappingAudioProcessor implements AudioProcessor {
    private boolean active;
    private boolean inputEnded;
    private int[] outputChannels;
    private int[] pendingOutputChannels;
    private ByteBuffer buffer = EMPTY_BUFFER;
    private ByteBuffer outputBuffer = EMPTY_BUFFER;
    private int channelCount = -1;
    private int sampleRateHz = -1;

    public void setChannelMap(int[] outputChannels) {
        this.pendingOutputChannels = outputChannels;
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public boolean configure(int sampleRateHz, int channelCount, int encoding) throws AudioProcessor.UnhandledFormatException {
        boolean outputChannelsChanged = !Arrays.equals(this.pendingOutputChannels, this.outputChannels);
        this.outputChannels = this.pendingOutputChannels;
        if (this.outputChannels == null) {
            this.active = false;
            return outputChannelsChanged;
        }
        if (encoding != 2) {
            throw new AudioProcessor.UnhandledFormatException(sampleRateHz, channelCount, encoding);
        }
        if (!outputChannelsChanged && this.sampleRateHz == sampleRateHz && this.channelCount == channelCount) {
            return false;
        }
        this.sampleRateHz = sampleRateHz;
        this.channelCount = channelCount;
        this.active = channelCount != this.outputChannels.length;
        int i = 0;
        while (i < this.outputChannels.length) {
            int channelIndex = this.outputChannels[i];
            if (channelIndex >= channelCount) {
                throw new AudioProcessor.UnhandledFormatException(sampleRateHz, channelCount, encoding);
            }
            this.active = (channelIndex != i) | this.active;
            i++;
        }
        return true;
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public boolean isActive() {
        return this.active;
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public int getOutputChannelCount() {
        return this.outputChannels == null ? this.channelCount : this.outputChannels.length;
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public int getOutputEncoding() {
        return 2;
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public void queueInput(ByteBuffer inputBuffer) {
        int position = inputBuffer.position();
        int limit = inputBuffer.limit();
        int frameCount = (limit - position) / (this.channelCount * 2);
        int outputSize = this.outputChannels.length * frameCount * 2;
        if (this.buffer.capacity() < outputSize) {
            this.buffer = ByteBuffer.allocateDirect(outputSize).order(ByteOrder.nativeOrder());
        } else {
            this.buffer.clear();
        }
        while (position < limit) {
            for (int channelIndex : this.outputChannels) {
                this.buffer.putShort(inputBuffer.getShort((channelIndex * 2) + position));
            }
            position += this.channelCount * 2;
        }
        inputBuffer.position(limit);
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
        this.channelCount = -1;
        this.sampleRateHz = -1;
        this.outputChannels = null;
        this.active = false;
    }
}
