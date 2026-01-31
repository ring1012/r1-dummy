package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;

/* loaded from: classes.dex */
public class IntegerCodec implements ObjectSerializer, ObjectDeserializer {
    public static IntegerCodec instance = new IntegerCodec();

    @Override // com.alibaba.fastjson.serializer.ObjectSerializer
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.getWriter();
        Number value = (Number) object;
        if (value == null) {
            if (out.isEnabled(SerializerFeature.WriteNullNumberAsZero)) {
                out.write('0');
                return;
            } else {
                out.writeNull();
                return;
            }
        }
        out.writeInt(value.intValue());
        if (serializer.isEnabled(SerializerFeature.WriteClassName)) {
            Class<?> clazz = value.getClass();
            if (clazz == Byte.class) {
                out.write('B');
            } else if (clazz == Short.class) {
                out.write('S');
            }
        }
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        JSONLexer lexer = defaultJSONParser.getLexer();
        if (lexer.token() == 8) {
            lexer.nextToken(16);
            return null;
        }
        if (lexer.token() == 2) {
            int iIntValue = lexer.intValue();
            lexer.nextToken(16);
            return (T) Integer.valueOf(iIntValue);
        }
        if (lexer.token() == 3) {
            BigDecimal bigDecimalDecimalValue = lexer.decimalValue();
            lexer.nextToken(16);
            return (T) Integer.valueOf(bigDecimalDecimalValue.intValue());
        }
        return (T) TypeUtils.castToInt(defaultJSONParser.parse());
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public int getFastMatchToken() {
        return 2;
    }
}
