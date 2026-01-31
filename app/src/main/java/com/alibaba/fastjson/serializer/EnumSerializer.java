package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

/* loaded from: classes.dex */
public class EnumSerializer implements ObjectSerializer {
    public static final EnumSerializer instance = new EnumSerializer();

    @Override // com.alibaba.fastjson.serializer.ObjectSerializer
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.getWriter();
        if (object == null) {
            serializer.getWriter().writeNull();
        } else if (serializer.isEnabled(SerializerFeature.WriteEnumUsingToString)) {
            Enum<?> e = (Enum) object;
            serializer.write(e.toString());
        } else {
            Enum<?> e2 = (Enum) object;
            out.writeInt(e2.ordinal());
        }
    }
}
