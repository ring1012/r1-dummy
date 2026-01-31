package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: classes.dex */
public class TeeInputStream extends ProxyInputStream {
    private final OutputStream branch;
    private final boolean closeBranch;

    public TeeInputStream(InputStream input, OutputStream branch) {
        this(input, branch, false);
    }

    public TeeInputStream(InputStream input, OutputStream branch, boolean closeBranch) {
        super(input);
        this.branch = branch;
        this.closeBranch = closeBranch;
    }

    @Override // org.apache.commons.io.input.ProxyInputStream, java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        try {
            super.close();
        } finally {
            if (this.closeBranch) {
                this.branch.close();
            }
        }
    }

    @Override // org.apache.commons.io.input.ProxyInputStream, java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        int ch = super.read();
        if (ch != -1) {
            this.branch.write(ch);
        }
        return ch;
    }

    @Override // org.apache.commons.io.input.ProxyInputStream, java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bts, int st, int end) throws IOException {
        int n = super.read(bts, st, end);
        if (n != -1) {
            this.branch.write(bts, st, n);
        }
        return n;
    }

    @Override // org.apache.commons.io.input.ProxyInputStream, java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bts) throws IOException {
        int n = super.read(bts);
        if (n != -1) {
            this.branch.write(bts, 0, n);
        }
        return n;
    }
}
