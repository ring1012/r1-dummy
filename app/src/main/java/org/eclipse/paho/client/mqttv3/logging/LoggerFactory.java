package org.eclipse.paho.client.mqttv3.logging;

import java.lang.reflect.InvocationTargetException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/* loaded from: classes.dex */
public class LoggerFactory {
    public static final String MQTT_CLIENT_MSG_CAT = "org.eclipse.paho.client.mqttv3.internal.nls.logcat";

    /* renamed from: a, reason: collision with root package name */
    static Class f510a;
    static Class b;
    private static final String c;
    private static String d;
    private static String e;

    static {
        Class<?> cls = f510a;
        if (cls == null) {
            try {
                cls = Class.forName("org.eclipse.paho.client.mqttv3.logging.LoggerFactory");
                f510a = cls;
            } catch (ClassNotFoundException e2) {
                throw new NoClassDefFoundError(e2.getMessage());
            }
        }
        c = cls.getName();
        d = null;
        e = "org.eclipse.paho.client.mqttv3.logging.JSR47Logger";
    }

    private static Logger a(String str, ResourceBundle resourceBundle, String str2, String str3) throws ClassNotFoundException {
        Logger logger;
        try {
            Class<?> cls = Class.forName(str);
            if (cls != null) {
                try {
                    logger = (Logger) cls.newInstance();
                    logger.initialise(resourceBundle, str2, str3);
                } catch (ExceptionInInitializerError e2) {
                    return null;
                } catch (IllegalAccessException e3) {
                    return null;
                } catch (InstantiationException e4) {
                    return null;
                } catch (SecurityException e5) {
                    return null;
                }
            } else {
                logger = null;
            }
            return logger;
        } catch (ClassNotFoundException e6) {
            return null;
        } catch (NoClassDefFoundError e7) {
            return null;
        }
    }

    public static Logger getLogger(String str, String str2) throws ClassNotFoundException {
        String str3 = d;
        if (str3 == null) {
            str3 = e;
        }
        Logger loggerA = a(str3, ResourceBundle.getBundle(str), str2, null);
        if (loggerA == null) {
            throw new MissingResourceException("Error locating the logging class", c, str2);
        }
        return loggerA;
    }

    public static String getLoggingProperty(String str) throws IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
        try {
            Class<?> cls = Class.forName("java.util.logging.LogManager");
            Object objInvoke = cls.getMethod("getLogManager", new Class[0]).invoke(null, null);
            Class<?>[] clsArr = new Class[1];
            Class<?> cls2 = b;
            if (cls2 == null) {
                try {
                    cls2 = Class.forName("java.lang.String");
                    b = cls2;
                } catch (ClassNotFoundException e2) {
                    throw new NoClassDefFoundError(e2.getMessage());
                }
            }
            clsArr[0] = cls2;
            return (String) cls.getMethod("getProperty", clsArr).invoke(objInvoke, str);
        } catch (Exception e3) {
            return null;
        }
    }

    public static void setLogger(String str) {
        d = str;
    }
}
