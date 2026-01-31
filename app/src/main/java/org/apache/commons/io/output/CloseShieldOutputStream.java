package org.apache.commons.io.output;

import java.io.OutputStream;

/* loaded from: classes.dex */
public class CloseShieldOutputStream extends ProxyOutputStream {
    public CloseShieldOutputStream(OutputStream out) {
        super(out);
    }

    @Override // org.apache.commons.io.output.ProxyOutputStream, java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.out = new ClosedOutputStream();
    }
}
