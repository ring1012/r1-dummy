package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

/* loaded from: classes.dex */
public class IntArraySerializer implements ObjectSerializer {
    public static IntArraySerializer instance = new IntArraySerializer();

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
        int[] array = (int[]) object;
        out.write('[');
        for (int i = 0; i < array.length; i++) {
            if (i != 0) {
                out.write(',');
            }
            out.writeInt(array[i]);
        }
        out.write(']');
    }
}
