package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

/* loaded from: classes.dex */
public class ByteArraySerializer implements ObjectSerializer {
    public static ByteArraySerializer instance = new ByteArraySerializer();

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
        byte[] array = (byte[]) object;
        out.writeByteArray(array);
    }
}
