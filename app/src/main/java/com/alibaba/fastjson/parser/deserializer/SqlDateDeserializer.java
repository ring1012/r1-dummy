package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONScanner;
import java.lang.reflect.Type;
import java.sql.Date;
import java.text.ParseException;

/* loaded from: classes.dex */
public class SqlDateDeserializer extends AbstractDateDeserializer implements ObjectDeserializer {
    public static final SqlDateDeserializer instance = new SqlDateDeserializer();

    @Override // com.alibaba.fastjson.parser.deserializer.AbstractDateDeserializer
    protected <T> T cast(DefaultJSONParser defaultJSONParser, Type type, Object obj, Object obj2) {
        long timeInMillis;
        Date date;
        if (obj2 == null) {
            return null;
        }
        if (obj2 instanceof java.util.Date) {
            date = new Date(((java.util.Date) obj2).getTime());
        } else if (obj2 instanceof Number) {
            date = new Date(((Number) obj2).longValue());
        } else {
            if (obj2 instanceof String) {
                String str = (String) obj2;
                if (str.length() == 0) {
                    return null;
                }
                JSONScanner jSONScanner = new JSONScanner(str);
                try {
                    if (jSONScanner.scanISO8601DateIfMatch()) {
                        timeInMillis = jSONScanner.getCalendar().getTimeInMillis();
                    } else {
                        try {
                            return (T) new Date(defaultJSONParser.getDateFormat().parse(str).getTime());
                        } catch (ParseException e) {
                            timeInMillis = Long.parseLong(str);
                        }
                    }
                    jSONScanner.close();
                    return (T) new Date(timeInMillis);
                } finally {
                    jSONScanner.close();
                }
            }
            throw new JSONException("parse error : " + obj2);
        }
        return (T) date;
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public int getFastMatchToken() {
        return 2;
    }
}
