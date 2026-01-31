package okhttp3.internal.cache;

import java.io.IOException;
import okio.Buffer;
import okio.ForwardingSink;
import okio.Sink;

/* loaded from: classes.dex */
class FaultHidingSink extends ForwardingSink {
    private boolean hasErrors;

    FaultHidingSink(Sink delegate) {
        super(delegate);
    }

    @Override // okio.ForwardingSink, okio.Sink
    public void write(Buffer source, long byteCount) throws IOException {
        if (this.hasErrors) {
            source.skip(byteCount);
            return;
        }
        try {
            super.write(source, byteCount);
        } catch (IOException e) {
            this.hasErrors = true;
            onException(e);
        }
    }

    @Override // okio.ForwardingSink, okio.Sink, java.io.Flushable
    public void flush() throws IOException {
        if (!this.hasErrors) {
            try {
                super.flush();
            } catch (IOException e) {
                this.hasErrors = true;
                onException(e);
            }
        }
    }

    @Override // okio.ForwardingSink, okio.Sink, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!this.hasErrors) {
            try {
                super.close();
            } catch (IOException e) {
                this.hasErrors = true;
                onException(e);
            }
        }
    }

    protected void onException(IOException e) {
    }
}
