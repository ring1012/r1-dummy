package com.google.android.exoplayer2.decoder;

import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public class SimpleOutputBuffer extends OutputBuffer {
    public ByteBuffer data;
    private final SimpleDecoder<?, SimpleOutputBuffer, ?> owner;

    public SimpleOutputBuffer(SimpleDecoder<?, SimpleOutputBuffer, ?> owner) {
        this.owner = owner;
    }

    public ByteBuffer init(long timeUs, int size) {
        this.timeUs = timeUs;
        if (this.data == null || this.data.capacity() < size) {
            this.data = ByteBuffer.allocateDirect(size);
        }
        this.data.position(0);
        this.data.limit(size);
        return this.data;
    }

    @Override // com.google.android.exoplayer2.decoder.Buffer
    public void clear() {
        super.clear();
        if (this.data != null) {
            this.data.clear();
        }
    }

    @Override // com.google.android.exoplayer2.decoder.OutputBuffer
    public void release() {
        this.owner.releaseOutputBuffer(this);
    }
}
