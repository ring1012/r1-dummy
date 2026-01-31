package com.google.android.exoplayer2.text;

import com.google.android.exoplayer2.decoder.SimpleDecoder;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public abstract class SimpleSubtitleDecoder extends SimpleDecoder<SubtitleInputBuffer, SubtitleOutputBuffer, SubtitleDecoderException> implements SubtitleDecoder {
    private final String name;

    protected abstract Subtitle decode(byte[] bArr, int i, boolean z) throws SubtitleDecoderException;

    protected SimpleSubtitleDecoder(String name) {
        super(new SubtitleInputBuffer[2], new SubtitleOutputBuffer[2]);
        this.name = name;
        setInitialInputBufferSize(1024);
    }

    @Override // com.google.android.exoplayer2.decoder.Decoder
    public final String getName() {
        return this.name;
    }

    @Override // com.google.android.exoplayer2.text.SubtitleDecoder
    public void setPositionUs(long timeUs) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.decoder.SimpleDecoder
    public final SubtitleInputBuffer createInputBuffer() {
        return new SubtitleInputBuffer();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.decoder.SimpleDecoder
    public final SubtitleOutputBuffer createOutputBuffer() {
        return new SimpleSubtitleOutputBuffer(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.decoder.SimpleDecoder
    public final void releaseOutputBuffer(SubtitleOutputBuffer buffer) {
        super.releaseOutputBuffer((SimpleSubtitleDecoder) buffer);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.decoder.SimpleDecoder
    public final SubtitleDecoderException decode(SubtitleInputBuffer inputBuffer, SubtitleOutputBuffer outputBuffer, boolean reset) {
        try {
            ByteBuffer inputData = inputBuffer.data;
            Subtitle subtitle = decode(inputData.array(), inputData.limit(), reset);
            outputBuffer.setContent(inputBuffer.timeUs, subtitle, inputBuffer.subsampleOffsetUs);
            outputBuffer.clearFlag(Integer.MIN_VALUE);
            return null;
        } catch (SubtitleDecoderException e) {
            return e;
        }
    }
}
