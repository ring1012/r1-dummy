package org.apache.commons.io.input;

import java.io.DataInput;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.EndianUtils;

/* loaded from: classes.dex */
public class SwappedDataInputStream extends ProxyInputStream implements DataInput {
    public SwappedDataInputStream(InputStream input) {
        super(input);
    }

    @Override // java.io.DataInput
    public boolean readBoolean() throws IOException {
        return readByte() != 0;
    }

    @Override // java.io.DataInput
    public byte readByte() throws IOException {
        return (byte) this.in.read();
    }

    @Override // java.io.DataInput
    public char readChar() throws IOException {
        return (char) readShort();
    }

    @Override // java.io.DataInput
    public double readDouble() throws IOException {
        return EndianUtils.readSwappedDouble(this.in);
    }

    @Override // java.io.DataInput
    public float readFloat() throws IOException {
        return EndianUtils.readSwappedFloat(this.in);
    }

    @Override // java.io.DataInput
    public void readFully(byte[] data) throws IOException {
        readFully(data, 0, data.length);
    }

    @Override // java.io.DataInput
    public void readFully(byte[] data, int offset, int length) throws IOException {
        int remaining = length;
        while (remaining > 0) {
            int location = (offset + length) - remaining;
            int count = read(data, location, remaining);
            if (-1 == count) {
                throw new EOFException();
            }
            remaining -= count;
        }
    }

    @Override // java.io.DataInput
    public int readInt() throws IOException {
        return EndianUtils.readSwappedInteger(this.in);
    }

    @Override // java.io.DataInput
    public String readLine() throws IOException {
        throw new UnsupportedOperationException("Operation not supported: readLine()");
    }

    @Override // java.io.DataInput
    public long readLong() throws IOException {
        return EndianUtils.readSwappedLong(this.in);
    }

    @Override // java.io.DataInput
    public short readShort() throws IOException {
        return EndianUtils.readSwappedShort(this.in);
    }

    @Override // java.io.DataInput
    public int readUnsignedByte() throws IOException {
        return this.in.read();
    }

    @Override // java.io.DataInput
    public int readUnsignedShort() throws IOException {
        return EndianUtils.readSwappedUnsignedShort(this.in);
    }

    @Override // java.io.DataInput
    public String readUTF() throws IOException {
        throw new UnsupportedOperationException("Operation not supported: readUTF()");
    }

    @Override // java.io.DataInput
    public int skipBytes(int count) throws IOException {
        return (int) this.in.skip(count);
    }
}
