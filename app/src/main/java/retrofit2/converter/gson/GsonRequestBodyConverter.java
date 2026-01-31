package retrofit2.converter.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Converter;

/* loaded from: classes.dex */
final class GsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private final TypeAdapter<T> adapter;
    private final Gson gson;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // retrofit2.Converter
    public /* bridge */ /* synthetic */ RequestBody convert(Object obj) throws IOException {
        return convert((GsonRequestBodyConverter<T>) obj);
    }

    GsonRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override // retrofit2.Converter
    public RequestBody convert(T value) throws IOException {
        Buffer buffer = new Buffer();
        Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
        JsonWriter jsonWriter = this.gson.newJsonWriter(writer);
        this.adapter.write(jsonWriter, value);
        jsonWriter.close();
        return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
    }
}
