package org.apache.commons.io.output;

import java.io.Serializable;
import java.io.Writer;

/* loaded from: classes.dex */
public class StringBuilderWriter extends Writer implements Serializable {
    private final StringBuilder builder;

    public StringBuilderWriter() {
        this.builder = new StringBuilder();
    }

    public StringBuilderWriter(int capacity) {
        this.builder = new StringBuilder(capacity);
    }

    public StringBuilderWriter(StringBuilder builder) {
        this.builder = builder == null ? new StringBuilder() : builder;
    }

    @Override // java.io.Writer, java.lang.Appendable
    public Writer append(char value) {
        this.builder.append(value);
        return this;
    }

    @Override // java.io.Writer, java.lang.Appendable
    public Writer append(CharSequence value) {
        this.builder.append(value);
        return this;
    }

    @Override // java.io.Writer, java.lang.Appendable
    public Writer append(CharSequence value, int start, int end) {
        this.builder.append(value, start, end);
        return this;
    }

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
    }

    @Override // java.io.Writer, java.io.Flushable
    public void flush() {
    }

    @Override // java.io.Writer
    public void write(String value) {
        if (value != null) {
            this.builder.append(value);
        }
    }

    @Override // java.io.Writer
    public void write(char[] value, int offset, int length) {
        if (value != null) {
            this.builder.append(value, offset, length);
        }
    }

    public StringBuilder getBuilder() {
        return this.builder;
    }

    public String toString() {
        return this.builder.toString();
    }
}
