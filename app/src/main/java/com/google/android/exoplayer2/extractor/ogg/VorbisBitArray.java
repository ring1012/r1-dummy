package com.google.android.exoplayer2.extractor.ogg;

import com.google.android.exoplayer2.util.Assertions;

/* loaded from: classes.dex */
final class VorbisBitArray {
    private int bitOffset;
    private int byteOffset;
    public final byte[] data;
    private final int limit;

    public VorbisBitArray(byte[] data) {
        this(data, data.length);
    }

    public VorbisBitArray(byte[] data, int limit) {
        this.data = data;
        this.limit = limit * 8;
    }

    public void reset() {
        this.byteOffset = 0;
        this.bitOffset = 0;
    }

    public boolean readBit() {
        return readBits(1) == 1;
    }

    public int readBits(int numBits) {
        Assertions.checkState(getPosition() + numBits <= this.limit);
        if (numBits == 0) {
            return 0;
        }
        int result = 0;
        int bitCount = 0;
        if (this.bitOffset != 0) {
            bitCount = Math.min(numBits, 8 - this.bitOffset);
            int mask = 255 >>> (8 - bitCount);
            result = (this.data[this.byteOffset] >>> this.bitOffset) & mask;
            this.bitOffset += bitCount;
            if (this.bitOffset == 8) {
                this.byteOffset++;
                this.bitOffset = 0;
            }
        }
        if (numBits - bitCount > 7) {
            int numBytes = (numBits - bitCount) / 8;
            for (int i = 0; i < numBytes; i++) {
                byte[] bArr = this.data;
                this.byteOffset = this.byteOffset + 1;
                result = (int) (result | ((bArr[r9] & 255) << bitCount));
                bitCount += 8;
            }
        }
        if (numBits > bitCount) {
            int bitsOnNextByte = numBits - bitCount;
            int mask2 = 255 >>> (8 - bitsOnNextByte);
            result |= (this.data[this.byteOffset] & mask2) << bitCount;
            this.bitOffset += bitsOnNextByte;
        }
        return result;
    }

    public void skipBits(int numberOfBits) {
        Assertions.checkState(getPosition() + numberOfBits <= this.limit);
        this.byteOffset += numberOfBits / 8;
        this.bitOffset += numberOfBits % 8;
        if (this.bitOffset > 7) {
            this.byteOffset++;
            this.bitOffset -= 8;
        }
    }

    public int getPosition() {
        return (this.byteOffset * 8) + this.bitOffset;
    }

    public void setPosition(int position) {
        Assertions.checkArgument(position < this.limit && position >= 0);
        this.byteOffset = position / 8;
        this.bitOffset = position - (this.byteOffset * 8);
    }

    public int bitsLeft() {
        return this.limit - getPosition();
    }

    public int limit() {
        return this.limit;
    }
}
