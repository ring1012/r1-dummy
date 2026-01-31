package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

/* loaded from: classes.dex */
public class CharArraySerializer implements ObjectSerializer {
    public static CharArraySerializer instance = new CharArraySerializer();

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
        char[] chars = (char[]) object;
        out.writeString(new String(chars));
    }
}
