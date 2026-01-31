package okio;

import java.io.IOException;

/* loaded from: classes.dex */
public abstract class ForwardingSink implements Sink {
    private final Sink delegate;

    public ForwardingSink(Sink delegate) {
        if (delegate == null) {
            throw new IllegalArgumentException("delegate == null");
        }
        this.delegate = delegate;
    }

    public final Sink delegate() {
        return this.delegate;
    }

    @Override // okio.Sink
    public void write(Buffer source, long byteCount) throws IOException {
        this.delegate.write(source, byteCount);
    }

    @Override // okio.Sink, java.io.Flushable
    public void flush() throws IOException {
        this.delegate.flush();
    }

    @Override // okio.Sink
    public Timeout timeout() {
        return this.delegate.timeout();
    }

    @Override // okio.Sink, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.delegate.close();
    }

    public String toString() {
        return getClass().getSimpleName() + "(" + this.delegate.toString() + ")";
    }
}
