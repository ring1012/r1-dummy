package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.Locale;

/* loaded from: classes.dex */
public final class DateTypeAdapter extends TypeAdapter<Date> {
    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() { // from class: com.google.gson.internal.bind.DateTypeAdapter.1
        @Override // com.google.gson.TypeAdapterFactory
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            if (typeToken.getRawType() == Date.class) {
                return new DateTypeAdapter();
            }
            return null;
        }
    };
    private final DateFormat enUsFormat = DateFormat.getDateTimeInstance(2, 2, Locale.US);
    private final DateFormat localFormat = DateFormat.getDateTimeInstance(2, 2);

    @Override // com.google.gson.TypeAdapter
    /* renamed from: read, reason: avoid collision after fix types in other method */
    public Date read2(JsonReader in) throws IOException {
        if (in.peek() != JsonToken.NULL) {
            return deserializeToDate(in.nextString());
        }
        in.nextNull();
        return null;
    }

    private synchronized Date deserializeToDate(String json) {
        Date date;
        try {
            date = this.localFormat.parse(json);
        } catch (ParseException e) {
            try {
                date = this.enUsFormat.parse(json);
            } catch (ParseException e2) {
                try {
                    date = ISO8601Utils.parse(json, new ParsePosition(0));
                } catch (ParseException e3) {
                    throw new JsonSyntaxException(json, e3);
                }
            }
        }
        return date;
    }

    @Override // com.google.gson.TypeAdapter
    public synchronized void write(JsonWriter out, Date value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            String dateFormatAsString = this.enUsFormat.format(value);
            out.value(dateFormatAsString);
        }
    }
}
