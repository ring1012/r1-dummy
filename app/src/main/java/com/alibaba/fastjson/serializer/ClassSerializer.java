package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

/* loaded from: classes.dex */
public class ClassSerializer implements ObjectSerializer {
    public static final ClassSerializer instance = new ClassSerializer();

    @Override // com.alibaba.fastjson.serializer.ObjectSerializer
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.getWriter();
        Class clazz = (Class) object;
        out.writeString(clazz.getName());
    }
}
