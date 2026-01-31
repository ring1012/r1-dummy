package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;

/* loaded from: classes.dex */
public class URLCodec implements ObjectSerializer, ObjectDeserializer {
    public static final URLCodec instance = new URLCodec();

    @Override // com.alibaba.fastjson.serializer.ObjectSerializer
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        if (object == null) {
            serializer.writeNull();
        } else {
            serializer.write(object.toString());
        }
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        String str = (String) defaultJSONParser.parse();
        if (str == null) {
            return null;
        }
        try {
            return (T) new URL(str);
        } catch (MalformedURLException e) {
            throw new JSONException("create url error", e);
        }
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public int getFastMatchToken() {
        return 4;
    }
}
