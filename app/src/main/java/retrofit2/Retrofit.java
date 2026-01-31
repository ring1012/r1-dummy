package retrofit2;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.BuiltInConverters;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.ServiceMethod;

/* loaded from: classes.dex */
public final class Retrofit {
    final List<CallAdapter.Factory> adapterFactories;
    final HttpUrl baseUrl;
    final Call.Factory callFactory;

    @Nullable
    final Executor callbackExecutor;
    final List<Converter.Factory> converterFactories;
    private final Map<Method, ServiceMethod<?, ?>> serviceMethodCache = new ConcurrentHashMap();
    final boolean validateEagerly;

    Retrofit(Call.Factory callFactory, HttpUrl baseUrl, List<Converter.Factory> converterFactories, List<CallAdapter.Factory> adapterFactories, @Nullable Executor callbackExecutor, boolean validateEagerly) {
        this.callFactory = callFactory;
        this.baseUrl = baseUrl;
        this.converterFactories = Collections.unmodifiableList(converterFactories);
        this.adapterFactories = Collections.unmodifiableList(adapterFactories);
        this.callbackExecutor = callbackExecutor;
        this.validateEagerly = validateEagerly;
    }

    public <T> T create(final Class<T> cls) throws SecurityException {
        Utils.validateServiceInterface(cls);
        if (this.validateEagerly) {
            eagerlyValidateMethods(cls);
        }
        return (T) Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, new InvocationHandler() { // from class: retrofit2.Retrofit.1
            private final Platform platform = Platform.get();

            @Override // java.lang.reflect.InvocationHandler
            public Object invoke(Object proxy, Method method, @Nullable Object[] args) throws Throwable {
                if (method.getDeclaringClass() == Object.class) {
                    return method.invoke(this, args);
                }
                if (this.platform.isDefaultMethod(method)) {
                    return this.platform.invokeDefaultMethod(method, cls, proxy, args);
                }
                ServiceMethod<Object, Object> serviceMethod = Retrofit.this.loadServiceMethod(method);
                OkHttpCall<Object> okHttpCall = new OkHttpCall<>(serviceMethod, args);
                return serviceMethod.callAdapter.adapt(okHttpCall);
            }
        });
    }

    private void eagerlyValidateMethods(Class<?> service) throws SecurityException {
        Platform platform = Platform.get();
        for (Method method : service.getDeclaredMethods()) {
            if (!platform.isDefaultMethod(method)) {
                loadServiceMethod(method);
            }
        }
    }

    ServiceMethod<?, ?> loadServiceMethod(Method method) {
        ServiceMethod result;
        ServiceMethod<?, ?> result2 = this.serviceMethodCache.get(method);
        if (result2 != null) {
            return result2;
        }
        synchronized (this.serviceMethodCache) {
            result = this.serviceMethodCache.get(method);
            if (result == null) {
                result = new ServiceMethod.Builder(this, method).build();
                this.serviceMethodCache.put(method, result);
            }
        }
        return result;
    }

    public Call.Factory callFactory() {
        return this.callFactory;
    }

    public HttpUrl baseUrl() {
        return this.baseUrl;
    }

    public List<CallAdapter.Factory> callAdapterFactories() {
        return this.adapterFactories;
    }

    public CallAdapter<?, ?> callAdapter(Type returnType, Annotation[] annotations) {
        return nextCallAdapter(null, returnType, annotations);
    }

    public CallAdapter<?, ?> nextCallAdapter(@Nullable CallAdapter.Factory skipPast, Type returnType, Annotation[] annotations) {
        Utils.checkNotNull(returnType, "returnType == null");
        Utils.checkNotNull(annotations, "annotations == null");
        int start = this.adapterFactories.indexOf(skipPast) + 1;
        int count = this.adapterFactories.size();
        for (int i = start; i < count; i++) {
            CallAdapter<?, ?> adapter = this.adapterFactories.get(i).get(returnType, annotations, this);
            if (adapter != null) {
                return adapter;
            }
        }
        StringBuilder builder = new StringBuilder("Could not locate call adapter for ").append(returnType).append(".\n");
        if (skipPast != null) {
            builder.append("  Skipped:");
            for (int i2 = 0; i2 < start; i2++) {
                builder.append("\n   * ").append(this.adapterFactories.get(i2).getClass().getName());
            }
            builder.append('\n');
        }
        builder.append("  Tried:");
        int count2 = this.adapterFactories.size();
        for (int i3 = start; i3 < count2; i3++) {
            builder.append("\n   * ").append(this.adapterFactories.get(i3).getClass().getName());
        }
        throw new IllegalArgumentException(builder.toString());
    }

    public List<Converter.Factory> converterFactories() {
        return this.converterFactories;
    }

    public <T> Converter<T, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations) {
        return nextRequestBodyConverter(null, type, parameterAnnotations, methodAnnotations);
    }

    public <T> Converter<T, RequestBody> nextRequestBodyConverter(@Nullable Converter.Factory factory, Type type, Annotation[] annotationArr, Annotation[] annotationArr2) {
        Utils.checkNotNull(type, "type == null");
        Utils.checkNotNull(annotationArr, "parameterAnnotations == null");
        Utils.checkNotNull(annotationArr2, "methodAnnotations == null");
        int iIndexOf = this.converterFactories.indexOf(factory) + 1;
        int size = this.converterFactories.size();
        for (int i = iIndexOf; i < size; i++) {
            Converter<T, RequestBody> converter = (Converter<T, RequestBody>) this.converterFactories.get(i).requestBodyConverter(type, annotationArr, annotationArr2, this);
            if (converter != null) {
                return converter;
            }
        }
        StringBuilder sbAppend = new StringBuilder("Could not locate RequestBody converter for ").append(type).append(".\n");
        if (factory != null) {
            sbAppend.append("  Skipped:");
            for (int i2 = 0; i2 < iIndexOf; i2++) {
                sbAppend.append("\n   * ").append(this.converterFactories.get(i2).getClass().getName());
            }
            sbAppend.append('\n');
        }
        sbAppend.append("  Tried:");
        int size2 = this.converterFactories.size();
        for (int i3 = iIndexOf; i3 < size2; i3++) {
            sbAppend.append("\n   * ").append(this.converterFactories.get(i3).getClass().getName());
        }
        throw new IllegalArgumentException(sbAppend.toString());
    }

    public <T> Converter<ResponseBody, T> responseBodyConverter(Type type, Annotation[] annotations) {
        return nextResponseBodyConverter(null, type, annotations);
    }

    public <T> Converter<ResponseBody, T> nextResponseBodyConverter(@Nullable Converter.Factory factory, Type type, Annotation[] annotationArr) {
        Utils.checkNotNull(type, "type == null");
        Utils.checkNotNull(annotationArr, "annotations == null");
        int iIndexOf = this.converterFactories.indexOf(factory) + 1;
        int size = this.converterFactories.size();
        for (int i = iIndexOf; i < size; i++) {
            Converter<ResponseBody, T> converter = (Converter<ResponseBody, T>) this.converterFactories.get(i).responseBodyConverter(type, annotationArr, this);
            if (converter != null) {
                return converter;
            }
        }
        StringBuilder sbAppend = new StringBuilder("Could not locate ResponseBody converter for ").append(type).append(".\n");
        if (factory != null) {
            sbAppend.append("  Skipped:");
            for (int i2 = 0; i2 < iIndexOf; i2++) {
                sbAppend.append("\n   * ").append(this.converterFactories.get(i2).getClass().getName());
            }
            sbAppend.append('\n');
        }
        sbAppend.append("  Tried:");
        int size2 = this.converterFactories.size();
        for (int i3 = iIndexOf; i3 < size2; i3++) {
            sbAppend.append("\n   * ").append(this.converterFactories.get(i3).getClass().getName());
        }
        throw new IllegalArgumentException(sbAppend.toString());
    }

    public <T> Converter<T, String> stringConverter(Type type, Annotation[] annotationArr) {
        Utils.checkNotNull(type, "type == null");
        Utils.checkNotNull(annotationArr, "annotations == null");
        int size = this.converterFactories.size();
        for (int i = 0; i < size; i++) {
            Converter<T, String> converter = (Converter<T, String>) this.converterFactories.get(i).stringConverter(type, annotationArr, this);
            if (converter != null) {
                return converter;
            }
        }
        return BuiltInConverters.ToStringConverter.INSTANCE;
    }

    @Nullable
    public Executor callbackExecutor() {
        return this.callbackExecutor;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    public static final class Builder {
        private final List<CallAdapter.Factory> adapterFactories;
        private HttpUrl baseUrl;

        @Nullable
        private Call.Factory callFactory;

        @Nullable
        private Executor callbackExecutor;
        private final List<Converter.Factory> converterFactories;
        private final Platform platform;
        private boolean validateEagerly;

        Builder(Platform platform) {
            this.converterFactories = new ArrayList();
            this.adapterFactories = new ArrayList();
            this.platform = platform;
            this.converterFactories.add(new BuiltInConverters());
        }

        public Builder() {
            this(Platform.get());
        }

        Builder(Retrofit retrofit) {
            this.converterFactories = new ArrayList();
            this.adapterFactories = new ArrayList();
            this.platform = Platform.get();
            this.callFactory = retrofit.callFactory;
            this.baseUrl = retrofit.baseUrl;
            this.converterFactories.addAll(retrofit.converterFactories);
            this.adapterFactories.addAll(retrofit.adapterFactories);
            this.adapterFactories.remove(this.adapterFactories.size() - 1);
            this.callbackExecutor = retrofit.callbackExecutor;
            this.validateEagerly = retrofit.validateEagerly;
        }

        public Builder client(OkHttpClient client) {
            return callFactory((Call.Factory) Utils.checkNotNull(client, "client == null"));
        }

        public Builder callFactory(Call.Factory factory) {
            this.callFactory = (Call.Factory) Utils.checkNotNull(factory, "factory == null");
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            Utils.checkNotNull(baseUrl, "baseUrl == null");
            HttpUrl httpUrl = HttpUrl.parse(baseUrl);
            if (httpUrl == null) {
                throw new IllegalArgumentException("Illegal URL: " + baseUrl);
            }
            return baseUrl(httpUrl);
        }

        public Builder baseUrl(HttpUrl baseUrl) {
            Utils.checkNotNull(baseUrl, "baseUrl == null");
            List<String> pathSegments = baseUrl.pathSegments();
            if (!"".equals(pathSegments.get(pathSegments.size() - 1))) {
                throw new IllegalArgumentException("baseUrl must end in /: " + baseUrl);
            }
            this.baseUrl = baseUrl;
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public Builder addConverterFactory(Converter.Factory factory) {
            this.converterFactories.add(Utils.checkNotNull(factory, "factory == null"));
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public Builder addCallAdapterFactory(CallAdapter.Factory factory) {
            this.adapterFactories.add(Utils.checkNotNull(factory, "factory == null"));
            return this;
        }

        public Builder callbackExecutor(Executor executor) {
            this.callbackExecutor = (Executor) Utils.checkNotNull(executor, "executor == null");
            return this;
        }

        public Builder validateEagerly(boolean validateEagerly) {
            this.validateEagerly = validateEagerly;
            return this;
        }

        public Retrofit build() {
            if (this.baseUrl == null) {
                throw new IllegalStateException("Base URL required.");
            }
            Call.Factory callFactory = this.callFactory;
            if (callFactory == null) {
                callFactory = new OkHttpClient();
            }
            Executor callbackExecutor = this.callbackExecutor;
            if (callbackExecutor == null) {
                callbackExecutor = this.platform.defaultCallbackExecutor();
            }
            List<CallAdapter.Factory> adapterFactories = new ArrayList<>(this.adapterFactories);
            adapterFactories.add(this.platform.defaultCallAdapterFactory(callbackExecutor));
            List<Converter.Factory> converterFactories = new ArrayList<>(this.converterFactories);
            return new Retrofit(callFactory, this.baseUrl, converterFactories, adapterFactories, callbackExecutor, this.validateEagerly);
        }
    }
}
