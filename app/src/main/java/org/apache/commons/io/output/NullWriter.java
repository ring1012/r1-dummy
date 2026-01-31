package org.apache.commons.io.output;

import java.io.Writer;

/* loaded from: classes.dex */
public class NullWriter extends Writer {
    public static final NullWriter NULL_WRITER = new NullWriter();

    @Override // java.io.Writer, java.lang.Appendable
    public Writer append(char c) {
        return this;
    }

    @Override // java.io.Writer, java.lang.Appendable
    public Writer append(CharSequence csq, int start, int end) {
        return this;
    }

    @Override // java.io.Writer, java.lang.Appendable
    public Writer append(CharSequence csq) {
        return this;
    }

    @Override // java.io.Writer
    public void write(int idx) {
    }

    @Override // java.io.Writer
    public void write(char[] chr) {
    }

    @Override // java.io.Writer
    public void write(char[] chr, int st, int end) {
    }

    @Override // java.io.Writer
    public void write(String str) {
    }

    @Override // java.io.Writer
    public void write(String str, int st, int end) {
    }

    @Override // java.io.Writer, java.io.Flushable
    public void flush() {
    }

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
    }
}
