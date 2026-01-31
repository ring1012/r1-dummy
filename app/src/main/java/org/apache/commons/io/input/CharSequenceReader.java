package org.apache.commons.io.input;

import java.io.Reader;
import java.io.Serializable;

/* loaded from: classes.dex */
public class CharSequenceReader extends Reader implements Serializable {
    private final CharSequence charSequence;
    private int idx;
    private int mark;

    public CharSequenceReader(String charSequence) {
        this.charSequence = charSequence == null ? "" : charSequence;
    }

    @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.idx = 0;
        this.mark = 0;
    }

    @Override // java.io.Reader
    public void mark(int readAheadLimit) {
        this.mark = this.idx;
    }

    @Override // java.io.Reader
    public boolean markSupported() {
        return true;
    }

    @Override // java.io.Reader
    public int read() {
        if (this.idx >= this.charSequence.length()) {
            return -1;
        }
        CharSequence charSequence = this.charSequence;
        int i = this.idx;
        this.idx = i + 1;
        return charSequence.charAt(i);
    }

    @Override // java.io.Reader
    public int read(char[] array, int offset, int length) {
        if (this.idx >= this.charSequence.length()) {
            return -1;
        }
        if (array == null) {
            throw new NullPointerException("Character array is missing");
        }
        if (length < 0 || offset < 0 || offset + length > array.length) {
            throw new IndexOutOfBoundsException("Array Size=" + array.length + ", offset=" + offset + ", length=" + length);
        }
        int count = 0;
        for (int i = 0; i < length; i++) {
            int c = read();
            if (c != -1) {
                array[offset + i] = (char) c;
                count++;
            } else {
                return count;
            }
        }
        return count;
    }

    @Override // java.io.Reader
    public void reset() {
        this.idx = this.mark;
    }

    @Override // java.io.Reader
    public long skip(long n) {
        if (n < 0) {
            throw new IllegalArgumentException("Number of characters to skip is less than zero: " + n);
        }
        if (this.idx >= this.charSequence.length()) {
            return -1L;
        }
        int dest = (int) Math.min(this.charSequence.length(), this.idx + n);
        int count = dest - this.idx;
        this.idx = dest;
        return count;
    }

    public String toString() {
        return this.charSequence.toString();
    }
}
