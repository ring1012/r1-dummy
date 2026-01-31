package okio;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public class ForwardingTimeout extends Timeout {
    private Timeout delegate;

    public ForwardingTimeout(Timeout delegate) {
        if (delegate == null) {
            throw new IllegalArgumentException("delegate == null");
        }
        this.delegate = delegate;
    }

    public final Timeout delegate() {
        return this.delegate;
    }

    public final ForwardingTimeout setDelegate(Timeout delegate) {
        if (delegate == null) {
            throw new IllegalArgumentException("delegate == null");
        }
        this.delegate = delegate;
        return this;
    }

    @Override // okio.Timeout
    public Timeout timeout(long timeout, TimeUnit unit) {
        return this.delegate.timeout(timeout, unit);
    }

    @Override // okio.Timeout
    public long timeoutNanos() {
        return this.delegate.timeoutNanos();
    }

    @Override // okio.Timeout
    public boolean hasDeadline() {
        return this.delegate.hasDeadline();
    }

    @Override // okio.Timeout
    public long deadlineNanoTime() {
        return this.delegate.deadlineNanoTime();
    }

    @Override // okio.Timeout
    public Timeout deadlineNanoTime(long deadlineNanoTime) {
        return this.delegate.deadlineNanoTime(deadlineNanoTime);
    }

    @Override // okio.Timeout
    public Timeout clearTimeout() {
        return this.delegate.clearTimeout();
    }

    @Override // okio.Timeout
    public Timeout clearDeadline() {
        return this.delegate.clearDeadline();
    }

    @Override // okio.Timeout
    public void throwIfReached() throws IOException {
        this.delegate.throwIfReached();
    }
}
