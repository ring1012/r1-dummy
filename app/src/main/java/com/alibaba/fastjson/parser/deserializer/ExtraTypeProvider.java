package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;

/* loaded from: classes.dex */
public interface ExtraTypeProvider extends ParseProcess {
    Type getExtraType(Object obj, String str);
}
