package com.alibaba.fastjson.serializer;

import java.lang.reflect.Type;

/* loaded from: classes.dex */
public class ExceptionSerializer extends JavaBeanSerializer {
    public ExceptionSerializer(Class<?> clazz) {
        super(clazz);
    }

    @Override // com.alibaba.fastjson.serializer.JavaBeanSerializer
    protected boolean isWriteClassName(JSONSerializer serializer, Object obj, Type fieldType, Object fieldName) {
        return true;
    }
}
