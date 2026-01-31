package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: classes.dex */
public class TeeOutputStream extends ProxyOutputStream {
    protected OutputStream branch;

    public TeeOutputStream(OutputStream out, OutputStream branch) {
        super(out);
        this.branch = branch;
    }

    @Override // org.apache.commons.io.output.ProxyOutputStream, java.io.FilterOutputStream, java.io.OutputStream
    public synchronized void write(byte[] b) throws IOException {
        super.write(b);
        this.branch.write(b);
    }

    @Override // org.apache.commons.io.output.ProxyOutputStream, java.io.FilterOutputStream, java.io.OutputStream
    public synchronized void write(byte[] b, int off, int len) throws IOException {
        super.write(b, off, len);
        this.branch.write(b, off, len);
    }

    @Override // org.apache.commons.io.output.ProxyOutputStream, java.io.FilterOutputStream, java.io.OutputStream
    public synchronized void write(int b) throws IOException {
        super.write(b);
        this.branch.write(b);
    }

    @Override // org.apache.commons.io.output.ProxyOutputStream, java.io.FilterOutputStream, java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        super.flush();
        this.branch.flush();
    }

    @Override // org.apache.commons.io.output.ProxyOutputStream, java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        try {
            super.close();
        } finally {
            this.branch.close();
        }
    }
}
