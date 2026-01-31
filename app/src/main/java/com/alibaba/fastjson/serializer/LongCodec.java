package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.IOException;
import java.lang.reflect.Type;

/* loaded from: classes.dex */
public class LongCodec implements ObjectSerializer, ObjectDeserializer {
    public static LongCodec instance = new LongCodec();

    @Override // com.alibaba.fastjson.serializer.ObjectSerializer
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.getWriter();
        if (object == null) {
            if (out.isEnabled(SerializerFeature.WriteNullNumberAsZero)) {
                out.write('0');
                return;
            } else {
                out.writeNull();
                return;
            }
        }
        long value = ((Long) object).longValue();
        out.writeLong(value);
        if (serializer.isEnabled(SerializerFeature.WriteClassName) && value <= 2147483647L && value >= -2147483648L && fieldType != Long.class) {
            out.write('L');
        }
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        JSONLexer lexer = defaultJSONParser.getLexer();
        if (lexer.token() == 2) {
            long jLongValue = lexer.longValue();
            lexer.nextToken(16);
            return (T) Long.valueOf(jLongValue);
        }
        Object obj2 = defaultJSONParser.parse();
        if (obj2 == null) {
            return null;
        }
        return (T) TypeUtils.castToLong(obj2);
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public int getFastMatchToken() {
        return 2;
    }
}
