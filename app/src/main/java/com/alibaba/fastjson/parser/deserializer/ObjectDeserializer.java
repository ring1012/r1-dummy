package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import java.lang.reflect.Type;

/* loaded from: classes.dex */
public interface ObjectDeserializer {
    <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj);

    int getFastMatchToken();
}
