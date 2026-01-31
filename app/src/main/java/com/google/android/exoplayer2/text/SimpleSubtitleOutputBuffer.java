package com.google.android.exoplayer2.text;

/* loaded from: classes.dex */
final class SimpleSubtitleOutputBuffer extends SubtitleOutputBuffer {
    private final SimpleSubtitleDecoder owner;

    public SimpleSubtitleOutputBuffer(SimpleSubtitleDecoder owner) {
        this.owner = owner;
    }

    @Override // com.google.android.exoplayer2.text.SubtitleOutputBuffer, com.google.android.exoplayer2.decoder.OutputBuffer
    public final void release() {
        this.owner.releaseOutputBuffer((SubtitleOutputBuffer) this);
    }
}
