package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

/* loaded from: classes.dex */
public class BooleanArraySerializer implements ObjectSerializer {
    public static BooleanArraySerializer instance = new BooleanArraySerializer();

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
        boolean[] array = (boolean[]) object;
        out.write('[');
        for (int i = 0; i < array.length; i++) {
            if (i != 0) {
                out.write(',');
            }
            out.write(array[i]);
        }
        out.write(']');
    }
}
