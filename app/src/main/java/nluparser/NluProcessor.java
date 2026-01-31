package nluparser;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import nluparser.a;
import nluparser.a.e;
import nluparser.b;
import nluparser.d;
import nluparser.scheme.Intent;
import nluparser.scheme.NLU;
import nluparser.scheme.Result;

/* loaded from: classes.dex */
public class NluProcessor {
    private static final String ARGUMENT_ERROR = "Only parameter interface Converter<String, NLU> definitions are supported.";
    private final b converter;
    private final Map<String, Type> serviceTypes;

    public static class Builder {
        private a.InterfaceC0015a classifier;
        private b.a converter;
        private final Map<String, Type> mappers = new HashMap();
        private d typeMapping;

        private void ensureSaneDefaults() {
            if (this.classifier == null) {
                this.classifier = e.a();
            }
            if (this.converter == null) {
                this.converter = nluparser.b.a.a();
            }
            this.typeMapping = new d() { // from class: nluparser.NluProcessor.Builder.1
                @Override // nluparser.d
                public Map<String, Type> a() {
                    Map<String, Type> mapA = new d.a().a();
                    mapA.putAll(Builder.this.mappers);
                    return Collections.unmodifiableMap(mapA);
                }
            };
        }

        public NluProcessor build() {
            ensureSaneDefaults();
            return new NluProcessor(this.typeMapping, this.converter, this.classifier);
        }

        public Builder registerTypeMapper(String service, Type type) {
            if (this.mappers.get(service) == null) {
                this.mappers.put(service, type);
            }
            return this;
        }

        public Builder registerTypeMapper(Map<String, Type> mappers) {
            if (mappers != null && mappers.size() > 0) {
                this.mappers.putAll(mappers);
            }
            return this;
        }

        public Builder setClassifier(a.InterfaceC0015a classifier) {
            this.classifier = classifier;
            return this;
        }

        public Builder setConverter(b.a converter) {
            this.converter = converter;
            return this;
        }
    }

    NluProcessor(d typeMapping, b.a converterFactory, a.InterfaceC0015a classifierFactory) {
        Map<String, Type> mapA = typeMapping.a();
        b<String, ?> bVarA = converterFactory.a(classifierFactory.a(mapA));
        checkConverterArguments(bVarA);
        this.converter = bVarA;
        this.serviceTypes = mapA;
    }

    static void checkConverterArguments(b converter) {
        Type type = converter.getClass().getGenericInterfaces()[0];
        if (!(type instanceof ParameterizedType)) {
            throw new IllegalArgumentException(ARGUMENT_ERROR);
        }
        Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
        if (actualTypeArguments[0] != String.class || actualTypeArguments[1] != NLU.class) {
            throw new IllegalArgumentException(ARGUMENT_ERROR);
        }
    }

    public <T extends NLU<I, R>, I extends Intent, R extends Result> T from(String json) {
        if (json == null) {
            return null;
        }
        return (T) this.converter.a(json);
    }

    public Map<String, Type> getSupportedType() {
        return this.serviceTypes;
    }
}
