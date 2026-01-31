package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.DateDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;

/* loaded from: classes.dex */
public class CalendarCodec implements ObjectSerializer, ObjectDeserializer {
    public static final CalendarCodec instance = new CalendarCodec();

    @Override // com.alibaba.fastjson.serializer.ObjectSerializer
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        Calendar calendar = (Calendar) object;
        Date date = calendar.getTime();
        serializer.write(date);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [T, java.util.Calendar] */
    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        T t = (T) DateDeserializer.instance.deserialze(defaultJSONParser, type, obj);
        if (!(t instanceof Calendar)) {
            Date date = (Date) t;
            if (date == null) {
                return null;
            }
            ?? r0 = (T) Calendar.getInstance();
            r0.setTime(date);
            return r0;
        }
        return t;
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public int getFastMatchToken() {
        return 2;
    }
}
