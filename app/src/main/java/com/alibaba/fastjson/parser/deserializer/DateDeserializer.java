package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONScanner;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/* loaded from: classes.dex */
public class DateDeserializer extends AbstractDateDeserializer implements ObjectDeserializer {
    public static final DateDeserializer instance = new DateDeserializer();

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [T, java.util.Calendar] */
    @Override // com.alibaba.fastjson.parser.deserializer.AbstractDateDeserializer
    protected <T> T cast(DefaultJSONParser defaultJSONParser, Type type, Object obj, Object obj2) {
        if (obj2 == 0) {
            return null;
        }
        if (!(obj2 instanceof Date)) {
            if (obj2 instanceof Number) {
                return (T) new Date(((Number) obj2).longValue());
            }
            if (obj2 instanceof String) {
                String str = (String) obj2;
                if (str.length() == 0) {
                    return null;
                }
                JSONScanner jSONScanner = new JSONScanner(str);
                try {
                    if (jSONScanner.scanISO8601DateIfMatch(false)) {
                        ?? r0 = (T) jSONScanner.getCalendar();
                        return type == Calendar.class ? r0 : (T) r0.getTime();
                    }
                    jSONScanner.close();
                    try {
                        return (T) defaultJSONParser.getDateFormat().parse(str);
                    } catch (ParseException e) {
                        return (T) new Date(Long.parseLong(str));
                    }
                } finally {
                    jSONScanner.close();
                }
            }
            throw new JSONException("parse error");
        }
        return obj2;
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public int getFastMatchToken() {
        return 2;
    }
}
