package rx.exceptions;

/* loaded from: classes.dex */
public final class OnCompletedFailedException extends RuntimeException {
    private static final long serialVersionUID = 8622579378868820554L;

    public OnCompletedFailedException(Throwable throwable) {
        super(throwable == null ? new NullPointerException() : throwable);
    }

    public OnCompletedFailedException(String message, Throwable throwable) {
        super(message, throwable == null ? new NullPointerException() : throwable);
    }
}
