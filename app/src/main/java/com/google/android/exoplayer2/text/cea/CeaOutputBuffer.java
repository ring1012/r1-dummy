package com.google.android.exoplayer2.text.cea;

import com.google.android.exoplayer2.text.SubtitleOutputBuffer;

/* loaded from: classes.dex */
public final class CeaOutputBuffer extends SubtitleOutputBuffer {
    private final CeaDecoder owner;

    public CeaOutputBuffer(CeaDecoder owner) {
        this.owner = owner;
    }

    @Override // com.google.android.exoplayer2.text.SubtitleOutputBuffer, com.google.android.exoplayer2.decoder.OutputBuffer
    public final void release() {
        this.owner.releaseOutputBuffer(this);
    }
}
