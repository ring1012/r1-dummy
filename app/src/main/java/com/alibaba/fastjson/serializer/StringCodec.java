package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import java.io.IOException;
import java.lang.reflect.Type;

/* loaded from: classes.dex */
public class StringCodec implements ObjectSerializer, ObjectDeserializer {
    public static StringCodec instance = new StringCodec();

    @Override // com.alibaba.fastjson.serializer.ObjectSerializer
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        write(serializer, (String) object);
    }

    public void write(JSONSerializer serializer, String value) {
        SerializeWriter out = serializer.getWriter();
        if (value == null) {
            if (out.isEnabled(SerializerFeature.WriteNullStringAsEmpty)) {
                out.writeString("");
                return;
            } else {
                out.writeNull();
                return;
            }
        }
        out.writeString(value);
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        return (T) deserialze(defaultJSONParser);
    }

    public static <T> T deserialze(DefaultJSONParser defaultJSONParser) {
        JSONLexer lexer = defaultJSONParser.getLexer();
        if (lexer.token() == 4) {
            T t = (T) lexer.stringVal();
            lexer.nextToken(16);
            return t;
        }
        if (lexer.token() == 2) {
            T t2 = (T) lexer.numberString();
            lexer.nextToken(16);
            return t2;
        }
        Object obj = defaultJSONParser.parse();
        if (obj == null) {
            return null;
        }
        return (T) obj.toString();
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public int getFastMatchToken() {
        return 4;
    }
}
