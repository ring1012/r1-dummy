package com.google.gson.internal.bind;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public final class ReflectiveTypeAdapterFactory implements TypeAdapterFactory {
    private final ConstructorConstructor constructorConstructor;
    private final Excluder excluder;
    private final FieldNamingStrategy fieldNamingPolicy;
    private final JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;

    public ReflectiveTypeAdapterFactory(ConstructorConstructor constructorConstructor, FieldNamingStrategy fieldNamingPolicy, Excluder excluder, JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory) {
        this.constructorConstructor = constructorConstructor;
        this.fieldNamingPolicy = fieldNamingPolicy;
        this.excluder = excluder;
        this.jsonAdapterFactory = jsonAdapterFactory;
    }

    public boolean excludeField(Field f, boolean serialize) {
        return excludeField(f, serialize, this.excluder);
    }

    static boolean excludeField(Field f, boolean serialize, Excluder excluder) {
        return (excluder.excludeClass(f.getType(), serialize) || excluder.excludeField(f, serialize)) ? false : true;
    }

    private List<String> getFieldNames(Field f) {
        SerializedName annotation = (SerializedName) f.getAnnotation(SerializedName.class);
        if (annotation == null) {
            String name = this.fieldNamingPolicy.translateName(f);
            return Collections.singletonList(name);
        }
        String serializedName = annotation.value();
        String[] alternates = annotation.alternate();
        if (alternates.length == 0) {
            return Collections.singletonList(serializedName);
        }
        List<String> fieldNames = new ArrayList<>(alternates.length + 1);
        fieldNames.add(serializedName);
        for (String alternate : alternates) {
            fieldNames.add(alternate);
        }
        return fieldNames;
    }

    @Override // com.google.gson.TypeAdapterFactory
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) throws NoSuchMethodException, SecurityException {
        Class<? super T> raw = type.getRawType();
        if (!Object.class.isAssignableFrom(raw)) {
            return null;
        }
        ObjectConstructor<T> constructor = this.constructorConstructor.get(type);
        return new Adapter(constructor, getBoundFields(gson, type, raw));
    }

    private BoundField createBoundField(final Gson context, final Field field, String name, final TypeToken<?> fieldType, boolean serialize, boolean deserialize) {
        final boolean isPrimitive = Primitives.isPrimitive(fieldType.getRawType());
        JsonAdapter annotation = (JsonAdapter) field.getAnnotation(JsonAdapter.class);
        TypeAdapter<?> mapped = null;
        if (annotation != null) {
            mapped = this.jsonAdapterFactory.getTypeAdapter(this.constructorConstructor, context, fieldType, annotation);
        }
        final boolean jsonAdapterPresent = mapped != null;
        if (mapped == null) {
            mapped = context.getAdapter(fieldType);
        }
        final TypeAdapter<?> typeAdapter = mapped;
        return new BoundField(name, serialize, deserialize) { // from class: com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.1
            @Override // com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.BoundField
            void write(JsonWriter writer, Object value) throws IllegalAccessException, IOException, IllegalArgumentException {
                Object fieldValue = field.get(value);
                TypeAdapter t = jsonAdapterPresent ? typeAdapter : new TypeAdapterRuntimeTypeWrapper(context, typeAdapter, fieldType.getType());
                t.write(writer, fieldValue);
            }

            @Override // com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.BoundField
            void read(JsonReader reader, Object value) throws IllegalAccessException, IOException, IllegalArgumentException {
                Object fieldValue = typeAdapter.read2(reader);
                if (fieldValue != null || !isPrimitive) {
                    field.set(value, fieldValue);
                }
            }

            @Override // com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.BoundField
            public boolean writeField(Object value) throws IllegalAccessException, IOException, IllegalArgumentException {
                if (!this.serialized) {
                    return false;
                }
                Object fieldValue = field.get(value);
                return fieldValue != value;
            }
        };
    }

    /* JADX WARN: Code restructure failed: missing block: B:28:0x00a4, code lost:
    
        r22 = com.google.gson.reflect.TypeToken.get(com.google.gson.internal.C$Gson$Types.resolve(r22.getType(), r23, r23.getGenericSuperclass()));
        r23 = r22.getRawType();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.util.Map<java.lang.String, com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.BoundField> getBoundFields(com.google.gson.Gson r21, com.google.gson.reflect.TypeToken<?> r22, java.lang.Class<?> r23) {
        /*
            r20 = this;
            java.util.LinkedHashMap r17 = new java.util.LinkedHashMap
            r17.<init>()
            boolean r2 = r23.isInterface()
            if (r2 == 0) goto Lc
        Lb:
            return r17
        Lc:
            java.lang.reflect.Type r10 = r22.getType()
        L10:
            java.lang.Class<java.lang.Object> r2 = java.lang.Object.class
            r0 = r23
            if (r0 == r2) goto Lb
            java.lang.reflect.Field[] r13 = r23.getDeclaredFields()
            int r0 = r13.length
            r19 = r0
            r2 = 0
            r18 = r2
        L20:
            r0 = r18
            r1 = r19
            if (r0 >= r1) goto La4
            r4 = r13[r18]
            r2 = 1
            r0 = r20
            boolean r7 = r0.excludeField(r4, r2)
            r2 = 0
            r0 = r20
            boolean r8 = r0.excludeField(r4, r2)
            if (r7 != 0) goto L3f
            if (r8 != 0) goto L3f
        L3a:
            int r2 = r18 + 1
            r18 = r2
            goto L20
        L3f:
            r2 = 1
            r4.setAccessible(r2)
            java.lang.reflect.Type r2 = r22.getType()
            java.lang.reflect.Type r3 = r4.getGenericType()
            r0 = r23
            java.lang.reflect.Type r12 = com.google.gson.internal.C$Gson$Types.resolve(r2, r0, r3)
            r0 = r20
            java.util.List r11 = r0.getFieldNames(r4)
            r15 = 0
            r14 = 0
        L59:
            int r2 = r11.size()
            if (r14 >= r2) goto L83
            java.lang.Object r5 = r11.get(r14)
            java.lang.String r5 = (java.lang.String) r5
            if (r14 == 0) goto L68
            r7 = 0
        L68:
            com.google.gson.reflect.TypeToken r6 = com.google.gson.reflect.TypeToken.get(r12)
            r2 = r20
            r3 = r21
            com.google.gson.internal.bind.ReflectiveTypeAdapterFactory$BoundField r9 = r2.createBoundField(r3, r4, r5, r6, r7, r8)
            r0 = r17
            java.lang.Object r16 = r0.put(r5, r9)
            com.google.gson.internal.bind.ReflectiveTypeAdapterFactory$BoundField r16 = (com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.BoundField) r16
            if (r15 != 0) goto L80
            r15 = r16
        L80:
            int r14 = r14 + 1
            goto L59
        L83:
            if (r15 == 0) goto L3a
            java.lang.IllegalArgumentException r2 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.StringBuilder r3 = r3.append(r10)
            java.lang.String r6 = " declares multiple JSON fields named "
            java.lang.StringBuilder r3 = r3.append(r6)
            java.lang.String r6 = r15.name
            java.lang.StringBuilder r3 = r3.append(r6)
            java.lang.String r3 = r3.toString()
            r2.<init>(r3)
            throw r2
        La4:
            java.lang.reflect.Type r2 = r22.getType()
            java.lang.reflect.Type r3 = r23.getGenericSuperclass()
            r0 = r23
            java.lang.reflect.Type r2 = com.google.gson.internal.C$Gson$Types.resolve(r2, r0, r3)
            com.google.gson.reflect.TypeToken r22 = com.google.gson.reflect.TypeToken.get(r2)
            java.lang.Class r23 = r22.getRawType()
            goto L10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.getBoundFields(com.google.gson.Gson, com.google.gson.reflect.TypeToken, java.lang.Class):java.util.Map");
    }

    static abstract class BoundField {
        final boolean deserialized;
        final String name;
        final boolean serialized;

        abstract void read(JsonReader jsonReader, Object obj) throws IllegalAccessException, IOException;

        abstract void write(JsonWriter jsonWriter, Object obj) throws IllegalAccessException, IOException;

        abstract boolean writeField(Object obj) throws IllegalAccessException, IOException;

        protected BoundField(String name, boolean serialized, boolean deserialized) {
            this.name = name;
            this.serialized = serialized;
            this.deserialized = deserialized;
        }
    }

    public static final class Adapter<T> extends TypeAdapter<T> {
        private final Map<String, BoundField> boundFields;
        private final ObjectConstructor<T> constructor;

        Adapter(ObjectConstructor<T> constructor, Map<String, BoundField> boundFields) {
            this.constructor = constructor;
            this.boundFields = boundFields;
        }

        @Override // com.google.gson.TypeAdapter
        /* renamed from: read */
        public T read2(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            T instance = this.constructor.construct();
            try {
                in.beginObject();
                while (in.hasNext()) {
                    String name = in.nextName();
                    BoundField field = this.boundFields.get(name);
                    if (field == null || !field.deserialized) {
                        in.skipValue();
                    } else {
                        field.read(in, instance);
                    }
                }
                in.endObject();
                return instance;
            } catch (IllegalAccessException e) {
                throw new AssertionError(e);
            } catch (IllegalStateException e2) {
                throw new JsonSyntaxException(e2);
            }
        }

        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter out, T value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }
            out.beginObject();
            try {
                for (BoundField boundField : this.boundFields.values()) {
                    if (boundField.writeField(value)) {
                        out.name(boundField.name);
                        boundField.write(out, value);
                    }
                }
                out.endObject();
            } catch (IllegalAccessException e) {
                throw new AssertionError(e);
            }
        }
    }
}
