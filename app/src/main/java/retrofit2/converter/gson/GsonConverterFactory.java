package retrofit2.converter.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/* loaded from: classes.dex */
public final class GsonConverterFactory extends Converter.Factory {
    private final Gson gson;

    public static GsonConverterFactory create() {
        return create(new Gson());
    }

    public static GsonConverterFactory create(Gson gson) {
        if (gson == null) {
            throw new NullPointerException("gson == null");
        }
        return new GsonConverterFactory(gson);
    }

    private GsonConverterFactory(Gson gson) {
        this.gson = gson;
    }

    @Override // retrofit2.Converter.Factory
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = this.gson.getAdapter(TypeToken.get(type));
        return new GsonResponseBodyConverter(this.gson, adapter);
    }

    @Override // retrofit2.Converter.Factory
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = this.gson.getAdapter(TypeToken.get(type));
        return new GsonRequestBodyConverter(this.gson, adapter);
    }
}
