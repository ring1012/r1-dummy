package okhttp3.internal.connection;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public final class RouteException extends RuntimeException {
    private static final Method addSuppressedExceptionMethod;
    private IOException lastException;

    static {
        Method m;
        try {
            m = Throwable.class.getDeclaredMethod("addSuppressed", Throwable.class);
        } catch (Exception e) {
            m = null;
        }
        addSuppressedExceptionMethod = m;
    }

    public RouteException(IOException cause) {
        super(cause);
        this.lastException = cause;
    }

    public IOException getLastConnectException() {
        return this.lastException;
    }

    public void addConnectException(IOException e) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        addSuppressedIfPossible(e, this.lastException);
        this.lastException = e;
    }

    private void addSuppressedIfPossible(IOException e, IOException suppressed) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (addSuppressedExceptionMethod != null) {
            try {
                addSuppressedExceptionMethod.invoke(e, suppressed);
            } catch (IllegalAccessException e2) {
            } catch (InvocationTargetException e3) {
            }
        }
    }
}
