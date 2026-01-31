package retrofit2;

import com.android.volley.toolbox.HttpClientStack;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.ParameterHandler;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.OPTIONS;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.QueryName;
import retrofit2.http.Url;

/* loaded from: classes.dex */
final class ServiceMethod<R, T> {
    private final HttpUrl baseUrl;
    final CallAdapter<R, T> callAdapter;
    final Call.Factory callFactory;
    private final MediaType contentType;
    private final boolean hasBody;
    private final Headers headers;
    private final String httpMethod;
    private final boolean isFormEncoded;
    private final boolean isMultipart;
    private final ParameterHandler<?>[] parameterHandlers;
    private final String relativeUrl;
    private final Converter<ResponseBody, R> responseConverter;
    static final Pattern PARAM_URL_REGEX = Pattern.compile("\\{([a-zA-Z][a-zA-Z0-9_-]*)\\}");
    static final String PARAM = "[a-zA-Z][a-zA-Z0-9_-]*";
    static final Pattern PARAM_NAME_REGEX = Pattern.compile(PARAM);

    ServiceMethod(Builder<R, T> builder) {
        this.callFactory = builder.retrofit.callFactory();
        this.callAdapter = builder.callAdapter;
        this.baseUrl = builder.retrofit.baseUrl();
        this.responseConverter = builder.responseConverter;
        this.httpMethod = builder.httpMethod;
        this.relativeUrl = builder.relativeUrl;
        this.headers = builder.headers;
        this.contentType = builder.contentType;
        this.hasBody = builder.hasBody;
        this.isFormEncoded = builder.isFormEncoded;
        this.isMultipart = builder.isMultipart;
        this.parameterHandlers = builder.parameterHandlers;
    }

    Request toRequest(@Nullable Object... args) throws IOException {
        RequestBuilder requestBuilder = new RequestBuilder(this.httpMethod, this.baseUrl, this.relativeUrl, this.headers, this.contentType, this.hasBody, this.isFormEncoded, this.isMultipart);
        ParameterHandler<Object>[] handlers = this.parameterHandlers;
        int argumentCount = args != null ? args.length : 0;
        if (argumentCount != handlers.length) {
            throw new IllegalArgumentException("Argument count (" + argumentCount + ") doesn't match expected count (" + handlers.length + ")");
        }
        for (int p = 0; p < argumentCount; p++) {
            handlers[p].apply(requestBuilder, args[p]);
        }
        return requestBuilder.build();
    }

    R toResponse(ResponseBody body) throws IOException {
        return this.responseConverter.convert(body);
    }

    static final class Builder<T, R> {
        CallAdapter<T, R> callAdapter;
        MediaType contentType;
        boolean gotBody;
        boolean gotField;
        boolean gotPart;
        boolean gotPath;
        boolean gotQuery;
        boolean gotUrl;
        boolean hasBody;
        Headers headers;
        String httpMethod;
        boolean isFormEncoded;
        boolean isMultipart;
        final Method method;
        final Annotation[] methodAnnotations;
        final Annotation[][] parameterAnnotationsArray;
        ParameterHandler<?>[] parameterHandlers;
        final Type[] parameterTypes;
        String relativeUrl;
        Set<String> relativeUrlParamNames;
        Converter<ResponseBody, T> responseConverter;
        Type responseType;
        final Retrofit retrofit;

        Builder(Retrofit retrofit, Method method) {
            this.retrofit = retrofit;
            this.method = method;
            this.methodAnnotations = method.getAnnotations();
            this.parameterTypes = method.getGenericParameterTypes();
            this.parameterAnnotationsArray = method.getParameterAnnotations();
        }

        public ServiceMethod build() {
            this.callAdapter = createCallAdapter();
            this.responseType = this.callAdapter.responseType();
            if (this.responseType == Response.class || this.responseType == okhttp3.Response.class) {
                throw methodError("'" + Utils.getRawType(this.responseType).getName() + "' is not a valid response body type. Did you mean ResponseBody?", new Object[0]);
            }
            this.responseConverter = createResponseConverter();
            for (Annotation annotation : this.methodAnnotations) {
                parseMethodAnnotation(annotation);
            }
            if (this.httpMethod == null) {
                throw methodError("HTTP method annotation is required (e.g., @GET, @POST, etc.).", new Object[0]);
            }
            if (!this.hasBody) {
                if (this.isMultipart) {
                    throw methodError("Multipart can only be specified on HTTP methods with request body (e.g., @POST).", new Object[0]);
                }
                if (this.isFormEncoded) {
                    throw methodError("FormUrlEncoded can only be specified on HTTP methods with request body (e.g., @POST).", new Object[0]);
                }
            }
            int parameterCount = this.parameterAnnotationsArray.length;
            this.parameterHandlers = new ParameterHandler[parameterCount];
            for (int p = 0; p < parameterCount; p++) {
                Type parameterType = this.parameterTypes[p];
                if (Utils.hasUnresolvableType(parameterType)) {
                    throw parameterError(p, "Parameter type must not include a type variable or wildcard: %s", parameterType);
                }
                Annotation[] parameterAnnotations = this.parameterAnnotationsArray[p];
                if (parameterAnnotations == null) {
                    throw parameterError(p, "No Retrofit annotation found.", new Object[0]);
                }
                this.parameterHandlers[p] = parseParameter(p, parameterType, parameterAnnotations);
            }
            if (this.relativeUrl == null && !this.gotUrl) {
                throw methodError("Missing either @%s URL or @Url parameter.", this.httpMethod);
            }
            if (!this.isFormEncoded && !this.isMultipart && !this.hasBody && this.gotBody) {
                throw methodError("Non-body HTTP method cannot contain @Body.", new Object[0]);
            }
            if (this.isFormEncoded && !this.gotField) {
                throw methodError("Form-encoded method must contain at least one @Field.", new Object[0]);
            }
            if (this.isMultipart && !this.gotPart) {
                throw methodError("Multipart method must contain at least one @Part.", new Object[0]);
            }
            return new ServiceMethod(this);
        }

        private CallAdapter<T, R> createCallAdapter() {
            Type genericReturnType = this.method.getGenericReturnType();
            if (Utils.hasUnresolvableType(genericReturnType)) {
                throw methodError("Method return type must not include a type variable or wildcard: %s", genericReturnType);
            }
            if (genericReturnType == Void.TYPE) {
                throw methodError("Service methods cannot return void.", new Object[0]);
            }
            try {
                return (CallAdapter<T, R>) this.retrofit.callAdapter(genericReturnType, this.method.getAnnotations());
            } catch (RuntimeException e) {
                throw methodError(e, "Unable to create call adapter for %s", genericReturnType);
            }
        }

        private void parseMethodAnnotation(Annotation annotation) {
            if (annotation instanceof DELETE) {
                parseHttpMethodAndPath("DELETE", ((DELETE) annotation).value(), false);
                return;
            }
            if (annotation instanceof GET) {
                parseHttpMethodAndPath("GET", ((GET) annotation).value(), false);
                return;
            }
            if (annotation instanceof HEAD) {
                parseHttpMethodAndPath("HEAD", ((HEAD) annotation).value(), false);
                if (!Void.class.equals(this.responseType)) {
                    throw methodError("HEAD method must use Void as response type.", new Object[0]);
                }
                return;
            }
            if (annotation instanceof PATCH) {
                parseHttpMethodAndPath(HttpClientStack.HttpPatch.METHOD_NAME, ((PATCH) annotation).value(), true);
                return;
            }
            if (annotation instanceof POST) {
                parseHttpMethodAndPath("POST", ((POST) annotation).value(), true);
                return;
            }
            if (annotation instanceof PUT) {
                parseHttpMethodAndPath("PUT", ((PUT) annotation).value(), true);
                return;
            }
            if (annotation instanceof OPTIONS) {
                parseHttpMethodAndPath("OPTIONS", ((OPTIONS) annotation).value(), false);
                return;
            }
            if (annotation instanceof HTTP) {
                HTTP http = (HTTP) annotation;
                parseHttpMethodAndPath(http.method(), http.path(), http.hasBody());
                return;
            }
            if (annotation instanceof retrofit2.http.Headers) {
                String[] headersToParse = ((retrofit2.http.Headers) annotation).value();
                if (headersToParse.length == 0) {
                    throw methodError("@Headers annotation is empty.", new Object[0]);
                }
                this.headers = parseHeaders(headersToParse);
                return;
            }
            if (annotation instanceof Multipart) {
                if (this.isFormEncoded) {
                    throw methodError("Only one encoding annotation is allowed.", new Object[0]);
                }
                this.isMultipart = true;
            } else if (annotation instanceof FormUrlEncoded) {
                if (this.isMultipart) {
                    throw methodError("Only one encoding annotation is allowed.", new Object[0]);
                }
                this.isFormEncoded = true;
            }
        }

        private void parseHttpMethodAndPath(String httpMethod, String value, boolean hasBody) {
            if (this.httpMethod != null) {
                throw methodError("Only one HTTP method is allowed. Found: %s and %s.", this.httpMethod, httpMethod);
            }
            this.httpMethod = httpMethod;
            this.hasBody = hasBody;
            if (!value.isEmpty()) {
                int question = value.indexOf(63);
                if (question != -1 && question < value.length() - 1) {
                    String queryParams = value.substring(question + 1);
                    Matcher queryParamMatcher = ServiceMethod.PARAM_URL_REGEX.matcher(queryParams);
                    if (queryParamMatcher.find()) {
                        throw methodError("URL query string \"%s\" must not have replace block. For dynamic query parameters use @Query.", queryParams);
                    }
                }
                this.relativeUrl = value;
                this.relativeUrlParamNames = ServiceMethod.parsePathParameters(value);
            }
        }

        private Headers parseHeaders(String[] headers) {
            Headers.Builder builder = new Headers.Builder();
            for (String header : headers) {
                int colon = header.indexOf(58);
                if (colon == -1 || colon == 0 || colon == header.length() - 1) {
                    throw methodError("@Headers value must be in the form \"Name: Value\". Found: \"%s\"", header);
                }
                String headerName = header.substring(0, colon);
                String headerValue = header.substring(colon + 1).trim();
                if ("Content-Type".equalsIgnoreCase(headerName)) {
                    MediaType type = MediaType.parse(headerValue);
                    if (type == null) {
                        throw methodError("Malformed content type: %s", headerValue);
                    }
                    this.contentType = type;
                } else {
                    builder.add(headerName, headerValue);
                }
            }
            return builder.build();
        }

        private ParameterHandler<?> parseParameter(int p, Type parameterType, Annotation[] annotations) {
            ParameterHandler<?> result = null;
            for (Annotation annotation : annotations) {
                ParameterHandler<?> annotationAction = parseParameterAnnotation(p, parameterType, annotations, annotation);
                if (annotationAction != null) {
                    if (result != null) {
                        throw parameterError(p, "Multiple Retrofit annotations found, only one allowed.", new Object[0]);
                    }
                    result = annotationAction;
                }
            }
            if (result == null) {
                throw parameterError(p, "No Retrofit annotation found.", new Object[0]);
            }
            return result;
        }

        private ParameterHandler<?> parseParameterAnnotation(int p, Type type, Annotation[] annotations, Annotation annotation) {
            if (annotation instanceof Url) {
                if (this.gotUrl) {
                    throw parameterError(p, "Multiple @Url method annotations found.", new Object[0]);
                }
                if (this.gotPath) {
                    throw parameterError(p, "@Path parameters may not be used with @Url.", new Object[0]);
                }
                if (this.gotQuery) {
                    throw parameterError(p, "A @Url parameter must not come after a @Query", new Object[0]);
                }
                if (this.relativeUrl != null) {
                    throw parameterError(p, "@Url cannot be used with @%s URL", this.httpMethod);
                }
                this.gotUrl = true;
                if (type == HttpUrl.class || type == String.class || type == URI.class || ((type instanceof Class) && "android.net.Uri".equals(((Class) type).getName()))) {
                    return new ParameterHandler.RelativeUrl();
                }
                throw parameterError(p, "@Url must be okhttp3.HttpUrl, String, java.net.URI, or android.net.Uri type.", new Object[0]);
            }
            if (annotation instanceof Path) {
                if (this.gotQuery) {
                    throw parameterError(p, "A @Path parameter must not come after a @Query.", new Object[0]);
                }
                if (this.gotUrl) {
                    throw parameterError(p, "@Path parameters may not be used with @Url.", new Object[0]);
                }
                if (this.relativeUrl == null) {
                    throw parameterError(p, "@Path can only be used with relative url on @%s", this.httpMethod);
                }
                this.gotPath = true;
                Path path = (Path) annotation;
                String name = path.value();
                validatePathName(p, name);
                return new ParameterHandler.Path(name, this.retrofit.stringConverter(type, annotations), path.encoded());
            }
            if (annotation instanceof Query) {
                Query query = (Query) annotation;
                String name2 = query.value();
                boolean encoded = query.encoded();
                Class<?> rawParameterType = Utils.getRawType(type);
                this.gotQuery = true;
                if (Iterable.class.isAssignableFrom(rawParameterType)) {
                    if (!(type instanceof ParameterizedType)) {
                        throw parameterError(p, rawParameterType.getSimpleName() + " must include generic type (e.g., " + rawParameterType.getSimpleName() + "<String>)", new Object[0]);
                    }
                    return new ParameterHandler.Query(name2, this.retrofit.stringConverter(Utils.getParameterUpperBound(0, (ParameterizedType) type), annotations), encoded).iterable();
                }
                if (rawParameterType.isArray()) {
                    return new ParameterHandler.Query(name2, this.retrofit.stringConverter(ServiceMethod.boxIfPrimitive(rawParameterType.getComponentType()), annotations), encoded).array();
                }
                return new ParameterHandler.Query(name2, this.retrofit.stringConverter(type, annotations), encoded);
            }
            if (annotation instanceof QueryName) {
                boolean encoded2 = ((QueryName) annotation).encoded();
                Class<?> rawParameterType2 = Utils.getRawType(type);
                this.gotQuery = true;
                if (Iterable.class.isAssignableFrom(rawParameterType2)) {
                    if (!(type instanceof ParameterizedType)) {
                        throw parameterError(p, rawParameterType2.getSimpleName() + " must include generic type (e.g., " + rawParameterType2.getSimpleName() + "<String>)", new Object[0]);
                    }
                    return new ParameterHandler.QueryName(this.retrofit.stringConverter(Utils.getParameterUpperBound(0, (ParameterizedType) type), annotations), encoded2).iterable();
                }
                if (rawParameterType2.isArray()) {
                    return new ParameterHandler.QueryName(this.retrofit.stringConverter(ServiceMethod.boxIfPrimitive(rawParameterType2.getComponentType()), annotations), encoded2).array();
                }
                return new ParameterHandler.QueryName(this.retrofit.stringConverter(type, annotations), encoded2);
            }
            if (annotation instanceof QueryMap) {
                Class<?> rawParameterType3 = Utils.getRawType(type);
                if (!Map.class.isAssignableFrom(rawParameterType3)) {
                    throw parameterError(p, "@QueryMap parameter type must be Map.", new Object[0]);
                }
                Type mapType = Utils.getSupertype(type, rawParameterType3, Map.class);
                if (!(mapType instanceof ParameterizedType)) {
                    throw parameterError(p, "Map must include generic types (e.g., Map<String, String>)", new Object[0]);
                }
                ParameterizedType parameterizedType = (ParameterizedType) mapType;
                Type keyType = Utils.getParameterUpperBound(0, parameterizedType);
                if (String.class != keyType) {
                    throw parameterError(p, "@QueryMap keys must be of type String: " + keyType, new Object[0]);
                }
                return new ParameterHandler.QueryMap(this.retrofit.stringConverter(Utils.getParameterUpperBound(1, parameterizedType), annotations), ((QueryMap) annotation).encoded());
            }
            if (annotation instanceof Header) {
                Header header = (Header) annotation;
                String name3 = header.value();
                Class<?> rawParameterType4 = Utils.getRawType(type);
                if (Iterable.class.isAssignableFrom(rawParameterType4)) {
                    if (!(type instanceof ParameterizedType)) {
                        throw parameterError(p, rawParameterType4.getSimpleName() + " must include generic type (e.g., " + rawParameterType4.getSimpleName() + "<String>)", new Object[0]);
                    }
                    return new ParameterHandler.Header(name3, this.retrofit.stringConverter(Utils.getParameterUpperBound(0, (ParameterizedType) type), annotations)).iterable();
                }
                if (rawParameterType4.isArray()) {
                    return new ParameterHandler.Header(name3, this.retrofit.stringConverter(ServiceMethod.boxIfPrimitive(rawParameterType4.getComponentType()), annotations)).array();
                }
                return new ParameterHandler.Header(name3, this.retrofit.stringConverter(type, annotations));
            }
            if (annotation instanceof HeaderMap) {
                Class<?> rawParameterType5 = Utils.getRawType(type);
                if (!Map.class.isAssignableFrom(rawParameterType5)) {
                    throw parameterError(p, "@HeaderMap parameter type must be Map.", new Object[0]);
                }
                Type mapType2 = Utils.getSupertype(type, rawParameterType5, Map.class);
                if (!(mapType2 instanceof ParameterizedType)) {
                    throw parameterError(p, "Map must include generic types (e.g., Map<String, String>)", new Object[0]);
                }
                ParameterizedType parameterizedType2 = (ParameterizedType) mapType2;
                Type keyType2 = Utils.getParameterUpperBound(0, parameterizedType2);
                if (String.class != keyType2) {
                    throw parameterError(p, "@HeaderMap keys must be of type String: " + keyType2, new Object[0]);
                }
                return new ParameterHandler.HeaderMap(this.retrofit.stringConverter(Utils.getParameterUpperBound(1, parameterizedType2), annotations));
            }
            if (annotation instanceof Field) {
                if (!this.isFormEncoded) {
                    throw parameterError(p, "@Field parameters can only be used with form encoding.", new Object[0]);
                }
                Field field = (Field) annotation;
                String name4 = field.value();
                boolean encoded3 = field.encoded();
                this.gotField = true;
                Class<?> rawParameterType6 = Utils.getRawType(type);
                if (Iterable.class.isAssignableFrom(rawParameterType6)) {
                    if (!(type instanceof ParameterizedType)) {
                        throw parameterError(p, rawParameterType6.getSimpleName() + " must include generic type (e.g., " + rawParameterType6.getSimpleName() + "<String>)", new Object[0]);
                    }
                    return new ParameterHandler.Field(name4, this.retrofit.stringConverter(Utils.getParameterUpperBound(0, (ParameterizedType) type), annotations), encoded3).iterable();
                }
                if (rawParameterType6.isArray()) {
                    return new ParameterHandler.Field(name4, this.retrofit.stringConverter(ServiceMethod.boxIfPrimitive(rawParameterType6.getComponentType()), annotations), encoded3).array();
                }
                return new ParameterHandler.Field(name4, this.retrofit.stringConverter(type, annotations), encoded3);
            }
            if (annotation instanceof FieldMap) {
                if (!this.isFormEncoded) {
                    throw parameterError(p, "@FieldMap parameters can only be used with form encoding.", new Object[0]);
                }
                Class<?> rawParameterType7 = Utils.getRawType(type);
                if (!Map.class.isAssignableFrom(rawParameterType7)) {
                    throw parameterError(p, "@FieldMap parameter type must be Map.", new Object[0]);
                }
                Type mapType3 = Utils.getSupertype(type, rawParameterType7, Map.class);
                if (!(mapType3 instanceof ParameterizedType)) {
                    throw parameterError(p, "Map must include generic types (e.g., Map<String, String>)", new Object[0]);
                }
                ParameterizedType parameterizedType3 = (ParameterizedType) mapType3;
                Type keyType3 = Utils.getParameterUpperBound(0, parameterizedType3);
                if (String.class != keyType3) {
                    throw parameterError(p, "@FieldMap keys must be of type String: " + keyType3, new Object[0]);
                }
                Converter<T, String> converterStringConverter = this.retrofit.stringConverter(Utils.getParameterUpperBound(1, parameterizedType3), annotations);
                this.gotField = true;
                return new ParameterHandler.FieldMap(converterStringConverter, ((FieldMap) annotation).encoded());
            }
            if (annotation instanceof Part) {
                if (!this.isMultipart) {
                    throw parameterError(p, "@Part parameters can only be used with multipart encoding.", new Object[0]);
                }
                Part part = (Part) annotation;
                this.gotPart = true;
                String partName = part.value();
                Class<?> rawParameterType8 = Utils.getRawType(type);
                if (partName.isEmpty()) {
                    if (Iterable.class.isAssignableFrom(rawParameterType8)) {
                        if (!(type instanceof ParameterizedType)) {
                            throw parameterError(p, rawParameterType8.getSimpleName() + " must include generic type (e.g., " + rawParameterType8.getSimpleName() + "<String>)", new Object[0]);
                        }
                        if (!MultipartBody.Part.class.isAssignableFrom(Utils.getRawType(Utils.getParameterUpperBound(0, (ParameterizedType) type)))) {
                            throw parameterError(p, "@Part annotation must supply a name or use MultipartBody.Part parameter type.", new Object[0]);
                        }
                        return ParameterHandler.RawPart.INSTANCE.iterable();
                    }
                    if (rawParameterType8.isArray()) {
                        if (!MultipartBody.Part.class.isAssignableFrom(rawParameterType8.getComponentType())) {
                            throw parameterError(p, "@Part annotation must supply a name or use MultipartBody.Part parameter type.", new Object[0]);
                        }
                        return ParameterHandler.RawPart.INSTANCE.array();
                    }
                    if (MultipartBody.Part.class.isAssignableFrom(rawParameterType8)) {
                        return ParameterHandler.RawPart.INSTANCE;
                    }
                    throw parameterError(p, "@Part annotation must supply a name or use MultipartBody.Part parameter type.", new Object[0]);
                }
                Headers headers = Headers.of("Content-Disposition", "form-data; name=\"" + partName + "\"", "Content-Transfer-Encoding", part.encoding());
                if (Iterable.class.isAssignableFrom(rawParameterType8)) {
                    if (!(type instanceof ParameterizedType)) {
                        throw parameterError(p, rawParameterType8.getSimpleName() + " must include generic type (e.g., " + rawParameterType8.getSimpleName() + "<String>)", new Object[0]);
                    }
                    Type iterableType = Utils.getParameterUpperBound(0, (ParameterizedType) type);
                    if (MultipartBody.Part.class.isAssignableFrom(Utils.getRawType(iterableType))) {
                        throw parameterError(p, "@Part parameters using the MultipartBody.Part must not include a part name in the annotation.", new Object[0]);
                    }
                    return new ParameterHandler.Part(headers, this.retrofit.requestBodyConverter(iterableType, annotations, this.methodAnnotations)).iterable();
                }
                if (rawParameterType8.isArray()) {
                    Class<?> arrayComponentType = ServiceMethod.boxIfPrimitive(rawParameterType8.getComponentType());
                    if (MultipartBody.Part.class.isAssignableFrom(arrayComponentType)) {
                        throw parameterError(p, "@Part parameters using the MultipartBody.Part must not include a part name in the annotation.", new Object[0]);
                    }
                    return new ParameterHandler.Part(headers, this.retrofit.requestBodyConverter(arrayComponentType, annotations, this.methodAnnotations)).array();
                }
                if (MultipartBody.Part.class.isAssignableFrom(rawParameterType8)) {
                    throw parameterError(p, "@Part parameters using the MultipartBody.Part must not include a part name in the annotation.", new Object[0]);
                }
                return new ParameterHandler.Part(headers, this.retrofit.requestBodyConverter(type, annotations, this.methodAnnotations));
            }
            if (annotation instanceof PartMap) {
                if (!this.isMultipart) {
                    throw parameterError(p, "@PartMap parameters can only be used with multipart encoding.", new Object[0]);
                }
                this.gotPart = true;
                Class<?> rawParameterType9 = Utils.getRawType(type);
                if (!Map.class.isAssignableFrom(rawParameterType9)) {
                    throw parameterError(p, "@PartMap parameter type must be Map.", new Object[0]);
                }
                Type mapType4 = Utils.getSupertype(type, rawParameterType9, Map.class);
                if (!(mapType4 instanceof ParameterizedType)) {
                    throw parameterError(p, "Map must include generic types (e.g., Map<String, String>)", new Object[0]);
                }
                ParameterizedType parameterizedType4 = (ParameterizedType) mapType4;
                Type keyType4 = Utils.getParameterUpperBound(0, parameterizedType4);
                if (String.class != keyType4) {
                    throw parameterError(p, "@PartMap keys must be of type String: " + keyType4, new Object[0]);
                }
                Type valueType = Utils.getParameterUpperBound(1, parameterizedType4);
                if (MultipartBody.Part.class.isAssignableFrom(Utils.getRawType(valueType))) {
                    throw parameterError(p, "@PartMap values cannot be MultipartBody.Part. Use @Part List<Part> or a different value type instead.", new Object[0]);
                }
                PartMap partMap = (PartMap) annotation;
                return new ParameterHandler.PartMap(this.retrofit.requestBodyConverter(valueType, annotations, this.methodAnnotations), partMap.encoding());
            }
            if (annotation instanceof Body) {
                if (this.isFormEncoded || this.isMultipart) {
                    throw parameterError(p, "@Body parameters cannot be used with form or multi-part encoding.", new Object[0]);
                }
                if (this.gotBody) {
                    throw parameterError(p, "Multiple @Body method annotations found.", new Object[0]);
                }
                try {
                    Converter<T, RequestBody> converterRequestBodyConverter = this.retrofit.requestBodyConverter(type, annotations, this.methodAnnotations);
                    this.gotBody = true;
                    return new ParameterHandler.Body(converterRequestBodyConverter);
                } catch (RuntimeException e) {
                    throw parameterError(e, p, "Unable to create @Body converter for %s", type);
                }
            }
            return null;
        }

        private void validatePathName(int p, String name) {
            if (!ServiceMethod.PARAM_NAME_REGEX.matcher(name).matches()) {
                throw parameterError(p, "@Path parameter name must match %s. Found: %s", ServiceMethod.PARAM_URL_REGEX.pattern(), name);
            }
            if (!this.relativeUrlParamNames.contains(name)) {
                throw parameterError(p, "URL \"%s\" does not contain \"{%s}\".", this.relativeUrl, name);
            }
        }

        private Converter<ResponseBody, T> createResponseConverter() {
            Annotation[] annotations = this.method.getAnnotations();
            try {
                return this.retrofit.responseBodyConverter(this.responseType, annotations);
            } catch (RuntimeException e) {
                throw methodError(e, "Unable to create converter for %s", this.responseType);
            }
        }

        private RuntimeException methodError(String message, Object... args) {
            return methodError(null, message, args);
        }

        private RuntimeException methodError(Throwable cause, String message, Object... args) {
            return new IllegalArgumentException(String.format(message, args) + "\n    for method " + this.method.getDeclaringClass().getSimpleName() + "." + this.method.getName(), cause);
        }

        private RuntimeException parameterError(Throwable cause, int p, String message, Object... args) {
            return methodError(cause, message + " (parameter #" + (p + 1) + ")", args);
        }

        private RuntimeException parameterError(int p, String message, Object... args) {
            return methodError(message + " (parameter #" + (p + 1) + ")", args);
        }
    }

    static Set<String> parsePathParameters(String path) {
        Matcher m = PARAM_URL_REGEX.matcher(path);
        Set<String> patterns = new LinkedHashSet<>();
        while (m.find()) {
            patterns.add(m.group(1));
        }
        return patterns;
    }

    static Class<?> boxIfPrimitive(Class<?> type) {
        return Boolean.TYPE == type ? Boolean.class : Byte.TYPE == type ? Byte.class : Character.TYPE == type ? Character.class : Double.TYPE == type ? Double.class : Float.TYPE == type ? Float.class : Integer.TYPE == type ? Integer.class : Long.TYPE == type ? Long.class : Short.TYPE == type ? Short.class : type;
    }
}
