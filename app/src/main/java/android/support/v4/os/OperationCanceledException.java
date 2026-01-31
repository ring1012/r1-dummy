package android.support.v4.os;

/* loaded from: classes.dex */
public class OperationCanceledException extends RuntimeException {
    public OperationCanceledException() {
        this(null);
    }

    public OperationCanceledException(String message) {
        super(message == null ? "The operation has been canceled." : message);
    }
}
