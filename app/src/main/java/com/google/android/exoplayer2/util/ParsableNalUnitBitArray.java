package com.google.android.exoplayer2.util;

/* loaded from: classes.dex */
public final class ParsableNalUnitBitArray {
    private int bitOffset;
    private int byteLimit;
    private int byteOffset;
    private byte[] data;

    public ParsableNalUnitBitArray(byte[] data, int offset, int limit) {
        reset(data, offset, limit);
    }

    public void reset(byte[] data, int offset, int limit) {
        this.data = data;
        this.byteOffset = offset;
        this.byteLimit = limit;
        this.bitOffset = 0;
        assertValidOffset();
    }

    public void skipBits(int n) {
        int oldByteOffset = this.byteOffset;
        this.byteOffset += n / 8;
        this.bitOffset += n % 8;
        if (this.bitOffset > 7) {
            this.byteOffset++;
            this.bitOffset -= 8;
        }
        int i = oldByteOffset + 1;
        while (i <= this.byteOffset) {
            if (shouldSkipByte(i)) {
                this.byteOffset++;
                i += 2;
            }
            i++;
        }
        assertValidOffset();
    }

    public boolean canReadBits(int n) {
        int oldByteOffset = this.byteOffset;
        int newByteOffset = this.byteOffset + (n / 8);
        int newBitOffset = this.bitOffset + (n % 8);
        if (newBitOffset > 7) {
            newByteOffset++;
            newBitOffset -= 8;
        }
        int i = oldByteOffset + 1;
        while (i <= newByteOffset && newByteOffset < this.byteLimit) {
            if (shouldSkipByte(i)) {
                newByteOffset++;
                i += 2;
            }
            i++;
        }
        return newByteOffset < this.byteLimit || (newByteOffset == this.byteLimit && newBitOffset == 0);
    }

    public boolean readBit() {
        return readBits(1) == 1;
    }

    public int readBits(int numBits) {
        int i;
        if (numBits == 0) {
            return 0;
        }
        int returnValue = 0;
        int wholeBytes = numBits / 8;
        for (int i2 = 0; i2 < wholeBytes; i2++) {
            int nextByteOffset = shouldSkipByte(this.byteOffset + 1) ? this.byteOffset + 2 : this.byteOffset + 1;
            if (this.bitOffset != 0) {
                i = ((this.data[this.byteOffset] & 255) << this.bitOffset) | ((this.data[nextByteOffset] & 255) >>> (8 - this.bitOffset));
            } else {
                i = this.data[this.byteOffset];
            }
            numBits -= 8;
            returnValue |= (i & 255) << numBits;
            this.byteOffset = nextByteOffset;
        }
        if (numBits > 0) {
            int nextBit = this.bitOffset + numBits;
            byte writeMask = (byte) (255 >> (8 - numBits));
            int nextByteOffset2 = shouldSkipByte(this.byteOffset + 1) ? this.byteOffset + 2 : this.byteOffset + 1;
            if (nextBit > 8) {
                returnValue |= (((this.data[this.byteOffset] & 255) << (nextBit - 8)) | ((this.data[nextByteOffset2] & 255) >> (16 - nextBit))) & writeMask;
                this.byteOffset = nextByteOffset2;
            } else {
                returnValue |= ((this.data[this.byteOffset] & 255) >> (8 - nextBit)) & writeMask;
                if (nextBit == 8) {
                    this.byteOffset = nextByteOffset2;
                }
            }
            this.bitOffset = nextBit % 8;
        }
        assertValidOffset();
        return returnValue;
    }

    public boolean canReadExpGolombCodedNum() {
        int initialByteOffset = this.byteOffset;
        int initialBitOffset = this.bitOffset;
        int leadingZeros = 0;
        while (this.byteOffset < this.byteLimit && !readBit()) {
            leadingZeros++;
        }
        boolean hitLimit = this.byteOffset == this.byteLimit;
        this.byteOffset = initialByteOffset;
        this.bitOffset = initialBitOffset;
        return !hitLimit && canReadBits((leadingZeros * 2) + 1);
    }

    public int readUnsignedExpGolombCodedInt() {
        return readExpGolombCodeNum();
    }

    public int readSignedExpGolombCodedInt() {
        int codeNum = readExpGolombCodeNum();
        return (codeNum % 2 == 0 ? -1 : 1) * ((codeNum + 1) / 2);
    }

    private int readExpGolombCodeNum() {
        int leadingZeros = 0;
        while (!readBit()) {
            leadingZeros++;
        }
        return (leadingZeros > 0 ? readBits(leadingZeros) : 0) + ((1 << leadingZeros) - 1);
    }

    private boolean shouldSkipByte(int offset) {
        return 2 <= offset && offset < this.byteLimit && this.data[offset] == 3 && this.data[offset + (-2)] == 0 && this.data[offset + (-1)] == 0;
    }

    private void assertValidOffset() {
        Assertions.checkState(this.byteOffset >= 0 && this.bitOffset >= 0 && this.bitOffset < 8 && (this.byteOffset < this.byteLimit || (this.byteOffset == this.byteLimit && this.bitOffset == 0)));
    }
}
