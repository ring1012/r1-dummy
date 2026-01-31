package com.alibaba.fastjson.serializer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

/* loaded from: classes.dex */
public class CollectionSerializer implements ObjectSerializer {
    public static final CollectionSerializer instance = new CollectionSerializer();

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
        Collection<?> collection = (Collection) object;
        SerialContext context = serializer.getContext();
        serializer.setContext(context, object, fieldName, 0);
        if (serializer.isEnabled(SerializerFeature.WriteClassName)) {
            if (HashSet.class == collection.getClass()) {
                out.append("Set");
            } else if (TreeSet.class == collection.getClass()) {
                out.append("TreeSet");
            }
        }
        try {
            out.append('[');
            int i = 0;
            for (Object item : collection) {
                try {
                    int i2 = i + 1;
                    if (i != 0) {
                        out.append(',');
                    }
                    if (item == null) {
                        out.writeNull();
                        i = i2;
                    } else {
                        Class<?> clazz = item.getClass();
                        if (clazz == Integer.class) {
                            out.writeInt(((Integer) item).intValue());
                            i = i2;
                        } else if (clazz == Long.class) {
                            out.writeLong(((Long) item).longValue());
                            if (out.isEnabled(SerializerFeature.WriteClassName)) {
                                out.write('L');
                                i = i2;
                            } else {
                                i = i2;
                            }
                        } else {
                            ObjectSerializer itemSerializer = serializer.getObjectWriter(clazz);
                            itemSerializer.write(serializer, item, Integer.valueOf(i2 - 1), elementType);
                            i = i2;
                        }
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
