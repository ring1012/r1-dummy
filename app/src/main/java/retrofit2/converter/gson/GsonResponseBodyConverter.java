package retrofit2.converter.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/* loaded from: classes.dex */
final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final TypeAdapter<T> adapter;
    private final Gson gson;

    GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override // retrofit2.Converter
    public T convert(ResponseBody value) throws IOException {
        JsonReader jsonReader = this.gson.newJsonReader(value.charStream());
        try {
            return this.adapter.read2(jsonReader);
        } finally {
            value.close();
        }
    }
}
