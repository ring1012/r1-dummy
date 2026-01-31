package rx.exceptions;

/* loaded from: classes.dex */
public class OnErrorFailedException extends RuntimeException {
    private static final long serialVersionUID = -419289748403337611L;

    public OnErrorFailedException(String message, Throwable e) {
        super(message, e == null ? new NullPointerException() : e);
    }

    public OnErrorFailedException(Throwable e) {
        super(e != null ? e.getMessage() : null, e == null ? new NullPointerException() : e);
    }
}
