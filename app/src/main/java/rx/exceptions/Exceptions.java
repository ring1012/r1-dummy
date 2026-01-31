package rx.exceptions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import rx.Observer;
import rx.annotations.Experimental;

/* loaded from: classes.dex */
public final class Exceptions {
    private static final int MAX_DEPTH = 25;

    private Exceptions() {
    }

    public static RuntimeException propagate(Throwable t) throws Throwable {
        if (t instanceof RuntimeException) {
            throw ((RuntimeException) t);
        }
        if (t instanceof Error) {
            throw ((Error) t);
        }
        throw new RuntimeException(t);
    }

    public static void throwIfFatal(Throwable t) {
        if (t instanceof OnErrorNotImplementedException) {
            throw ((OnErrorNotImplementedException) t);
        }
        if (t instanceof OnErrorFailedException) {
            throw ((OnErrorFailedException) t);
        }
        if (t instanceof StackOverflowError) {
            throw ((StackOverflowError) t);
        }
        if (t instanceof VirtualMachineError) {
            throw ((VirtualMachineError) t);
        }
        if (t instanceof ThreadDeath) {
            throw ((ThreadDeath) t);
        }
        if (t instanceof LinkageError) {
            throw ((LinkageError) t);
        }
    }

    public static void addCause(Throwable e, Throwable cause) {
        Set<Throwable> seenCauses = new HashSet<>();
        int i = 0;
        while (e.getCause() != null) {
            int i2 = i + 1;
            if (i < 25) {
                e = e.getCause();
                if (seenCauses.contains(e.getCause())) {
                    break;
                }
                seenCauses.add(e.getCause());
                i = i2;
            } else {
                return;
            }
        }
        try {
            e.initCause(cause);
        } catch (Throwable th) {
        }
    }

    public static Throwable getFinalCause(Throwable e) {
        int i = 0;
        while (e.getCause() != null) {
            int i2 = i + 1;
            if (i >= 25) {
                return new RuntimeException("Stack too deep to get final cause");
            }
            e = e.getCause();
            i = i2;
        }
        return e;
    }

    public static void throwIfAny(List<? extends Throwable> exceptions) throws Throwable {
        if (exceptions != null && !exceptions.isEmpty()) {
            if (exceptions.size() == 1) {
                Throwable t = exceptions.get(0);
                if (t instanceof RuntimeException) {
                    throw ((RuntimeException) t);
                }
                if (t instanceof Error) {
                    throw ((Error) t);
                }
                throw new RuntimeException(t);
            }
            throw new CompositeException("Multiple exceptions", exceptions);
        }
    }

    @Experimental
    public static void throwOrReport(Throwable t, Observer<?> o, Object value) {
        throwIfFatal(t);
        o.onError(OnErrorThrowable.addValueAsLastCause(t, value));
    }

    @Experimental
    public static void throwOrReport(Throwable t, Observer<?> o) {
        throwIfFatal(t);
        o.onError(t);
    }
}
