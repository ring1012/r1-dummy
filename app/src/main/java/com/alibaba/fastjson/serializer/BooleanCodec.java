package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.TypeUtils;
import com.tencent.bugly.Bugly;
import java.io.IOException;
import java.lang.reflect.Type;

/* loaded from: classes.dex */
public class BooleanCodec implements ObjectSerializer, ObjectDeserializer {
    public static final BooleanCodec instance = new BooleanCodec();

    @Override // com.alibaba.fastjson.serializer.ObjectSerializer
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.getWriter();
        Boolean value = (Boolean) object;
        if (value == null) {
            if (out.isEnabled(SerializerFeature.WriteNullBooleanAsFalse)) {
                out.write(Bugly.SDK_IS_DEV);
                return;
            } else {
                out.writeNull();
                return;
            }
        }
        if (value.booleanValue()) {
            out.write("true");
        } else {
            out.write(Bugly.SDK_IS_DEV);
        }
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        JSONLexer lexer = defaultJSONParser.getLexer();
        if (lexer.token() == 6) {
            lexer.nextToken(16);
            return (T) Boolean.TRUE;
        }
        if (lexer.token() == 7) {
            lexer.nextToken(16);
            return (T) Boolean.FALSE;
        }
        if (lexer.token() == 2) {
            int iIntValue = lexer.intValue();
            lexer.nextToken(16);
            if (iIntValue == 1) {
                return (T) Boolean.TRUE;
            }
            return (T) Boolean.FALSE;
        }
        Object obj2 = defaultJSONParser.parse();
        if (obj2 == null) {
            return null;
        }
        return (T) TypeUtils.castToBoolean(obj2);
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public int getFastMatchToken() {
        return 6;
    }
}
