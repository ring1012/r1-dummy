package com.google.android.exoplayer2.upstream;

/* loaded from: classes.dex */
public final class Allocation {
    public final byte[] data;
    private final int offset;

    public Allocation(byte[] data, int offset) {
        this.data = data;
        this.offset = offset;
    }

    public int translateOffset(int offset) {
        return this.offset + offset;
    }
}
