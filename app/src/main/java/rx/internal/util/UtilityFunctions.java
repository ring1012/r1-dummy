package rx.internal.util;

import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func3;
import rx.functions.Func4;
import rx.functions.Func5;
import rx.functions.Func6;
import rx.functions.Func7;
import rx.functions.Func8;
import rx.functions.Func9;
import rx.functions.FuncN;

/* loaded from: classes.dex */
public final class UtilityFunctions {
    private static final NullFunction NULL_FUNCTION = new NullFunction();

    public static <T> Func1<? super T, Boolean> alwaysTrue() {
        return AlwaysTrue.INSTANCE;
    }

    public static <T> Func1<? super T, Boolean> alwaysFalse() {
        return AlwaysFalse.INSTANCE;
    }

    public static <T> Func1<T, T> identity() {
        return new Func1<T, T>() { // from class: rx.internal.util.UtilityFunctions.1
            @Override // rx.functions.Func1
            public T call(T o) {
                return o;
            }
        };
    }

    private enum AlwaysTrue implements Func1<Object, Boolean> {
        INSTANCE;

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // rx.functions.Func1
        public Boolean call(Object o) {
            return true;
        }
    }

    private enum AlwaysFalse implements Func1<Object, Boolean> {
        INSTANCE;

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // rx.functions.Func1
        public Boolean call(Object o) {
            return false;
        }
    }

    public static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, R> NullFunction<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, R> returnNull() {
        return NULL_FUNCTION;
    }

    private static final class NullFunction<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, R> implements Func0<R>, Func1<T0, R>, Func2<T0, T1, R>, Func3<T0, T1, T2, R>, Func4<T0, T1, T2, T3, R>, Func5<T0, T1, T2, T3, T4, R>, Func6<T0, T1, T2, T3, T4, T5, R>, Func7<T0, T1, T2, T3, T4, T5, T6, R>, Func8<T0, T1, T2, T3, T4, T5, T6, T7, R>, Func9<T0, T1, T2, T3, T4, T5, T6, T7, T8, R>, FuncN<R> {
        private NullFunction() {
        }

        @Override // rx.functions.Func0, java.util.concurrent.Callable
        public R call() {
            return null;
        }

        @Override // rx.functions.Func1
        public R call(T0 t1) {
            return null;
        }

        @Override // rx.functions.Func2
        public R call(T0 t1, T1 t2) {
            return null;
        }

        @Override // rx.functions.Func3
        public R call(T0 t1, T1 t2, T2 t3) {
            return null;
        }

        @Override // rx.functions.Func4
        public R call(T0 t1, T1 t2, T2 t3, T3 t4) {
            return null;
        }

        @Override // rx.functions.Func5
        public R call(T0 t1, T1 t2, T2 t3, T3 t4, T4 t5) {
            return null;
        }

        @Override // rx.functions.Func6
        public R call(T0 t1, T1 t2, T2 t3, T3 t4, T4 t5, T5 t6) {
            return null;
        }

        @Override // rx.functions.Func7
        public R call(T0 t1, T1 t2, T2 t3, T3 t4, T4 t5, T5 t6, T6 t7) {
            return null;
        }

        @Override // rx.functions.Func8
        public R call(T0 t1, T1 t2, T2 t3, T3 t4, T4 t5, T5 t6, T6 t7, T7 t8) {
            return null;
        }

        @Override // rx.functions.Func9
        public R call(T0 t1, T1 t2, T2 t3, T3 t4, T4 t5, T5 t6, T6 t7, T7 t8, T8 t9) {
            return null;
        }

        @Override // rx.functions.FuncN
        public R call(Object... args) {
            return null;
        }
    }
}
