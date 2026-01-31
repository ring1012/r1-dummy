package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Proxy;

/* loaded from: classes.dex */
public class ClassLoaderObjectInputStream extends ObjectInputStream {
    private final ClassLoader classLoader;

    public ClassLoaderObjectInputStream(ClassLoader classLoader, InputStream inputStream) throws IOException {
        super(inputStream);
        this.classLoader = classLoader;
    }

    @Override // java.io.ObjectInputStream
    protected Class<?> resolveClass(ObjectStreamClass objectStreamClass) throws ClassNotFoundException, IOException {
        Class<?> clazz = Class.forName(objectStreamClass.getName(), false, this.classLoader);
        return clazz != null ? clazz : super.resolveClass(objectStreamClass);
    }

    @Override // java.io.ObjectInputStream
    protected Class<?> resolveProxyClass(String[] interfaces) throws IOException, ClassNotFoundException {
        Class<?>[] interfaceClasses = new Class[interfaces.length];
        for (int i = 0; i < interfaces.length; i++) {
            interfaceClasses[i] = Class.forName(interfaces[i], false, this.classLoader);
        }
        try {
            return Proxy.getProxyClass(this.classLoader, interfaceClasses);
        } catch (IllegalArgumentException e) {
            return super.resolveProxyClass(interfaces);
        }
    }
}
