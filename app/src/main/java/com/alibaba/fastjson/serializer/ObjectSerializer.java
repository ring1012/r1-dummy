package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

/* loaded from: classes.dex */
public interface ObjectSerializer {
    void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type) throws IOException;
}
