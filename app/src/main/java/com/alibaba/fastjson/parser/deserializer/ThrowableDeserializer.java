package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.ParserConfig;
import java.lang.reflect.Constructor;

/* loaded from: classes.dex */
public class ThrowableDeserializer extends JavaBeanDeserializer {
    public ThrowableDeserializer(ParserConfig mapping, Class<?> clazz) {
        super(mapping, clazz);
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x0054, code lost:
    
        if (r6 != null) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0056, code lost:
    
        r4 = (T) new java.lang.Exception(r10, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x005b, code lost:
    
        if (r12 == null) goto L82;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x005d, code lost:
    
        ((java.lang.Throwable) r4).setStackTrace(r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x010f, code lost:
    
        r4 = (T) createException(r10, r1, r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0113, code lost:
    
        if (r4 != null) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x011a, code lost:
    
        r4 = (T) new java.lang.Exception(r10, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x011d, code lost:
    
        r3 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x0125, code lost:
    
        throw new com.alibaba.fastjson.JSONException("create instance error", r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:?, code lost:
    
        return (T) r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:?, code lost:
    
        return (T) r4;
     */
    @Override // com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer, com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public <T> T deserialze(com.alibaba.fastjson.parser.DefaultJSONParser r16, java.lang.reflect.Type r17, java.lang.Object r18) {
        /*
            Method dump skipped, instructions count: 294
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.ThrowableDeserializer.deserialze(com.alibaba.fastjson.parser.DefaultJSONParser, java.lang.reflect.Type, java.lang.Object):java.lang.Object");
    }

    private Throwable createException(String message, Throwable cause, Class<?> exClass) throws Exception {
        Constructor<?> defaultConstructor = null;
        Constructor<?> messageConstructor = null;
        Constructor<?> causeConstructor = null;
        Constructor<?>[] arr$ = exClass.getConstructors();
        for (Constructor<?> constructor : arr$) {
            if (constructor.getParameterTypes().length == 0) {
                defaultConstructor = constructor;
            } else if (constructor.getParameterTypes().length == 1 && constructor.getParameterTypes()[0] == String.class) {
                messageConstructor = constructor;
            } else if (constructor.getParameterTypes().length == 2 && constructor.getParameterTypes()[0] == String.class && constructor.getParameterTypes()[1] == Throwable.class) {
                causeConstructor = constructor;
            }
        }
        if (causeConstructor != null) {
            return (Throwable) causeConstructor.newInstance(message, cause);
        }
        if (messageConstructor != null) {
            return (Throwable) messageConstructor.newInstance(message);
        }
        if (defaultConstructor != null) {
            return (Throwable) defaultConstructor.newInstance(new Object[0]);
        }
        return null;
    }

    @Override // com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer, com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public int getFastMatchToken() {
        return 12;
    }
}
