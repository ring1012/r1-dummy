package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

/* loaded from: classes.dex */
public class LongArraySerializer implements ObjectSerializer {
    public static LongArraySerializer instance = new LongArraySerializer();

    @Override // com.alibaba.fastjson.serializer.ObjectSerializer
    public final void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
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
        long[] array = (long[]) object;
        out.write('[');
        for (int i = 0; i < array.length; i++) {
            if (i != 0) {
                out.write(',');
            }
            out.writeLong(array[i]);
        }
        out.write(']');
    }
}
