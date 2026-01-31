package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

/* loaded from: classes.dex */
public class JSONSerializableSerializer implements ObjectSerializer {
    public static JSONSerializableSerializer instance = new JSONSerializableSerializer();

    @Override // com.alibaba.fastjson.serializer.ObjectSerializer
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        JSONSerializable jsonSerializable = (JSONSerializable) object;
        jsonSerializable.write(serializer, fieldName, fieldType);
    }
}
