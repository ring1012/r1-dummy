package rx.internal.util.unsafe;

import java.lang.reflect.Field;
import sun.misc.Unsafe;

/* loaded from: classes.dex */
public final class UnsafeAccess {
    public static final Unsafe UNSAFE;

    private UnsafeAccess() {
        throw new IllegalStateException("No instances!");
    }

    static {
        Unsafe u = null;
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            u = (Unsafe) field.get(null);
        } catch (Throwable th) {
        }
        UNSAFE = u;
    }

    public static final boolean isUnsafeAvailable() {
        return UNSAFE != null;
    }

    public static int getAndIncrementInt(Object obj, long offset) {
        int current;
        int next;
        do {
            current = UNSAFE.getIntVolatile(obj, offset);
            next = current + 1;
        } while (!UNSAFE.compareAndSwapInt(obj, offset, current, next));
        return current;
    }

    public static int getAndAddInt(Object obj, long offset, int n) {
        int current;
        int next;
        do {
            current = UNSAFE.getIntVolatile(obj, offset);
            next = current + n;
        } while (!UNSAFE.compareAndSwapInt(obj, offset, current, next));
        return current;
    }

    public static int getAndSetInt(Object obj, long offset, int newValue) {
        int current;
        do {
            current = UNSAFE.getIntVolatile(obj, offset);
        } while (!UNSAFE.compareAndSwapInt(obj, offset, current, newValue));
        return current;
    }

    public static boolean compareAndSwapInt(Object obj, long offset, int expected, int newValue) {
        return UNSAFE.compareAndSwapInt(obj, offset, expected, newValue);
    }

    public static long addressOf(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        try {
            Field f = clazz.getDeclaredField(fieldName);
            return UNSAFE.objectFieldOffset(f);
        } catch (NoSuchFieldException ex) {
            InternalError ie = new InternalError();
            ie.initCause(ex);
            throw ie;
        }
    }
}
