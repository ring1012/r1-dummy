package com.alibaba.fastjson.serializer;

import cn.yunzhisheng.asr.JniUscClient;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/* loaded from: classes.dex */
public final class ListSerializer implements ObjectSerializer {
    public static final ListSerializer instance = new ListSerializer();

    @Override // com.alibaba.fastjson.serializer.ObjectSerializer
    public final void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        boolean writeClassName = serializer.isEnabled(SerializerFeature.WriteClassName);
        SerializeWriter out = serializer.getWriter();
        Type elementType = null;
        if (writeClassName && (fieldType instanceof ParameterizedType)) {
            ParameterizedType param = (ParameterizedType) fieldType;
            elementType = param.getActualTypeArguments()[0];
        }
        if (object == null) {
            if (out.isEnabled(SerializerFeature.WriteNullListAsEmpty)) {
                out.write("[]");
                return;
            } else {
                out.writeNull();
                return;
            }
        }
        List<?> list = (List) object;
        if (list.size() == 0) {
            out.append("[]");
            return;
        }
        SerialContext context = serializer.getContext();
        serializer.setContext(context, object, fieldName, 0);
        try {
            if (out.isEnabled(SerializerFeature.PrettyFormat)) {
                out.append('[');
                serializer.incrementIndent();
                int i = 0;
                for (Object item : list) {
                    if (i != 0) {
                        out.append(',');
                    }
                    serializer.println();
                    if (item != null) {
                        if (serializer.containsReference(item)) {
                            serializer.writeReference(item);
                        } else {
                            ObjectSerializer itemSerializer = serializer.getObjectWriter(item.getClass());
                            SerialContext itemContext = new SerialContext(context, object, fieldName, 0);
                            serializer.setContext(itemContext);
                            itemSerializer.write(serializer, item, Integer.valueOf(i), elementType);
                        }
                    } else {
                        serializer.getWriter().writeNull();
                    }
                    i++;
                }
                serializer.decrementIdent();
                serializer.println();
                out.append(']');
                return;
            }
            out.append('[');
            int i2 = 0;
            for (Object item2 : list) {
                if (i2 != 0) {
                    out.append(',');
                }
                if (item2 == null) {
                    out.append(JniUscClient.az);
                } else {
                    Class<?> clazz = item2.getClass();
                    if (clazz == Integer.class) {
                        out.writeInt(((Integer) item2).intValue());
                    } else if (clazz == Long.class) {
                        long val = ((Long) item2).longValue();
                        if (writeClassName) {
                            out.writeLongAndChar(val, 'L');
                        } else {
                            out.writeLong(val);
                        }
                    } else {
                        SerialContext itemContext2 = new SerialContext(context, object, fieldName, 0);
                        serializer.setContext(itemContext2);
                        if (serializer.containsReference(item2)) {
                            serializer.writeReference(item2);
                        } else {
                            ObjectSerializer itemSerializer2 = serializer.getObjectWriter(item2.getClass());
                            itemSerializer2.write(serializer, item2, Integer.valueOf(i2), elementType);
                        }
                    }
                }
                i2++;
            }
            out.append(']');
        } finally {
            serializer.setContext(context);
        }
    }
}
