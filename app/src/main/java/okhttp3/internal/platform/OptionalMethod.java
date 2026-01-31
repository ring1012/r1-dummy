package okhttp3.internal.platform;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
class OptionalMethod<T> {
    private final String methodName;
    private final Class[] methodParams;
    private final Class<?> returnType;

    OptionalMethod(Class<?> returnType, String methodName, Class... methodParams) {
        this.returnType = returnType;
        this.methodName = methodName;
        this.methodParams = methodParams;
    }

    public boolean isSupported(T target) {
        return getMethod(target.getClass()) != null;
    }

    public Object invokeOptional(T target, Object... args) throws NoSuchMethodException, SecurityException, InvocationTargetException {
        Method m = getMethod(target.getClass());
        if (m == null) {
            return null;
        }
        try {
            return m.invoke(target, args);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public Object invokeOptionalWithoutCheckedException(T target, Object... args) throws Throwable {
        try {
            return invokeOptional(target, args);
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            if (targetException instanceof RuntimeException) {
                throw ((RuntimeException) targetException);
            }
            AssertionError error = new AssertionError("Unexpected exception");
            error.initCause(targetException);
            throw error;
        }
    }

    public Object invoke(T target, Object... args) throws NoSuchMethodException, SecurityException, InvocationTargetException {
        Method m = getMethod(target.getClass());
        if (m == null) {
            throw new AssertionError("Method " + this.methodName + " not supported for object " + target);
        }
        try {
            return m.invoke(target, args);
        } catch (IllegalAccessException e) {
            AssertionError error = new AssertionError("Unexpectedly could not call: " + m);
            error.initCause(e);
            throw error;
        }
    }

    public Object invokeWithoutCheckedException(T target, Object... args) throws Throwable {
        try {
            return invoke(target, args);
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            if (targetException instanceof RuntimeException) {
                throw ((RuntimeException) targetException);
            }
            AssertionError error = new AssertionError("Unexpected exception");
            error.initCause(targetException);
            throw error;
        }
    }

    private Method getMethod(Class<?> clazz) throws NoSuchMethodException, SecurityException {
        if (this.methodName == null) {
            return null;
        }
        Method method = getPublicMethod(clazz, this.methodName, this.methodParams);
        if (method != null && this.returnType != null && !this.returnType.isAssignableFrom(method.getReturnType())) {
            return null;
        }
        return method;
    }

    private static Method getPublicMethod(Class<?> clazz, String methodName, Class[] parameterTypes) throws NoSuchMethodException, SecurityException {
        Method method = null;
        try {
            method = clazz.getMethod(methodName, parameterTypes);
            if ((method.getModifiers() & 1) == 0) {
                return null;
            }
            return method;
        } catch (NoSuchMethodException e) {
            return method;
        }
    }
}
