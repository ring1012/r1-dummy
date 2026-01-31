package retrofit2;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import javax.annotation.Nullable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/* loaded from: classes.dex */
public interface Converter<F, T> {
    T convert(F f) throws IOException;

    public static abstract class Factory {
        @Nullable
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            return null;
        }

        @Nullable
        public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
            return null;
        }

        @Nullable
        public Converter<?, String> stringConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            return null;
        }

        protected static Type getParameterUpperBound(int index, ParameterizedType type) {
            return Utils.getParameterUpperBound(index, type);
        }

        protected static Class<?> getRawType(Type type) {
            return Utils.getRawType(type);
        }
    }
}
