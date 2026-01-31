package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public class CountingInputStream extends ProxyInputStream {
    private long count;

    public CountingInputStream(InputStream in) {
        super(in);
    }

    @Override // org.apache.commons.io.input.ProxyInputStream, java.io.FilterInputStream, java.io.InputStream
    public synchronized long skip(long length) throws IOException {
        long skip;
        skip = super.skip(length);
        this.count += skip;
        return skip;
    }

    @Override // org.apache.commons.io.input.ProxyInputStream
    protected synchronized void afterRead(int n) {
        if (n != -1) {
            this.count += n;
        }
    }

    public int getCount() {
        long result = getByteCount();
        if (result > 2147483647L) {
            throw new ArithmeticException("The byte count " + result + " is too large to be converted to an int");
        }
        return (int) result;
    }

    public int resetCount() {
        long result = resetByteCount();
        if (result > 2147483647L) {
            throw new ArithmeticException("The byte count " + result + " is too large to be converted to an int");
        }
        return (int) result;
    }

    public synchronized long getByteCount() {
        return this.count;
    }

    public synchronized long resetByteCount() {
        long tmp;
        tmp = this.count;
        this.count = 0L;
        return tmp;
    }
}
