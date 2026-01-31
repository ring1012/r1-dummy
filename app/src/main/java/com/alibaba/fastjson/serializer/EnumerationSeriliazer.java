package com.alibaba.fastjson.serializer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Enumeration;

/* loaded from: classes.dex */
public class EnumerationSeriliazer implements ObjectSerializer {
    public static EnumerationSeriliazer instance = new EnumerationSeriliazer();

    @Override // com.alibaba.fastjson.serializer.ObjectSerializer
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws Throwable {
        SerializeWriter out = serializer.getWriter();
        if (object == null) {
            if (out.isEnabled(SerializerFeature.WriteNullListAsEmpty)) {
                out.write("[]");
                return;
            } else {
                out.writeNull();
                return;
            }
        }
        Type elementType = null;
        if (serializer.isEnabled(SerializerFeature.WriteClassName) && (fieldType instanceof ParameterizedType)) {
            ParameterizedType param = (ParameterizedType) fieldType;
            elementType = param.getActualTypeArguments()[0];
        }
        Enumeration<?> e = (Enumeration) object;
        SerialContext context = serializer.getContext();
        serializer.setContext(context, object, fieldName, 0);
        try {
            out.append('[');
            int i = 0;
            while (e.hasMoreElements()) {
                try {
                    Object item = e.nextElement();
                    int i2 = i + 1;
                    if (i != 0) {
                        out.append(',');
                    }
                    if (item == null) {
                        out.writeNull();
                        i = i2;
                    } else {
                        Class<?> clazz = item.getClass();
                        ObjectSerializer itemSerializer = serializer.getObjectWriter(clazz);
                        itemSerializer.write(serializer, item, Integer.valueOf(i2 - 1), elementType);
                        i = i2;
                    }
                } catch (Throwable th) {
                    th = th;
                    serializer.setContext(context);
                    throw th;
                }
            }
            out.append(']');
            serializer.setContext(context);
        } catch (Throwable th2) {
            th = th2;
        }
    }
}
