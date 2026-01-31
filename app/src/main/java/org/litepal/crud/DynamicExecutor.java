package org.litepal.crud;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.litepal.exceptions.DataSupportException;

/* loaded from: classes.dex */
class DynamicExecutor {
    private DynamicExecutor() {
    }

    static Object send(Object object, String methodName, Object[] parameters, Class<?> objectClass, Class<?>[] clsArr) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        if (parameters == null) {
            try {
                parameters = new Object[0];
            } catch (NoSuchMethodException e) {
                throw new DataSupportException(DataSupportException.noSuchMethodException(objectClass.getSimpleName(), methodName));
            }
        }
        if (clsArr == null) {
            clsArr = new Class[0];
        }
        Method method = objectClass.getDeclaredMethod(methodName, clsArr);
        method.setAccessible(true);
        return method.invoke(object, parameters);
    }

    static void setField(Object object, String fieldName, Object value, Class<?> objectClass) throws IllegalAccessException, NoSuchFieldException, SecurityException, IllegalArgumentException {
        try {
            Field objectField = objectClass.getDeclaredField(fieldName);
            objectField.setAccessible(true);
            objectField.set(object, value);
        } catch (NoSuchFieldException e) {
            throw new DataSupportException(DataSupportException.noSuchFieldExceptioin(objectClass.getSimpleName(), fieldName));
        }
    }

    static Object getField(Object object, String fieldName, Class<?> objectClass) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
        try {
            Field objectField = objectClass.getDeclaredField(fieldName);
            objectField.setAccessible(true);
            return objectField.get(object);
        } catch (NoSuchFieldException e) {
            throw new DataSupportException(DataSupportException.noSuchFieldExceptioin(objectClass.getSimpleName(), fieldName));
        }
    }
}
