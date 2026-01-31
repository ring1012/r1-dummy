package rx.functions;

import java.util.concurrent.Callable;

/* loaded from: classes.dex */
public interface Func0<R> extends Function, Callable<R> {
    R call();
}
