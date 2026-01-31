package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSONAware;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONStreamAware;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/* loaded from: classes.dex */
public class JSONSerializer {
    private List<AfterFilter> afterFilters;
    private List<BeforeFilter> beforeFilters;
    private final SerializeConfig config;
    private SerialContext context;
    private DateFormat dateFormat;
    private String dateFormatPattern;
    private String indent;
    private int indentCount;
    private List<NameFilter> nameFilters;
    private final SerializeWriter out;
    private List<PropertyFilter> propertyFilters;
    private List<PropertyPreFilter> propertyPreFilters;
    private IdentityHashMap<Object, SerialContext> references;
    private List<ValueFilter> valueFilters;

    public JSONSerializer() {
        this(new SerializeWriter(), SerializeConfig.getGlobalInstance());
    }

    public JSONSerializer(SerializeWriter out) {
        this(out, SerializeConfig.getGlobalInstance());
    }

    public JSONSerializer(SerializeConfig config) {
        this(new SerializeWriter(), config);
    }

    public JSONSerializer(SerializeWriter out, SerializeConfig config) {
        this.beforeFilters = null;
        this.afterFilters = null;
        this.propertyFilters = null;
        this.valueFilters = null;
        this.nameFilters = null;
        this.propertyPreFilters = null;
        this.indentCount = 0;
        this.indent = "\t";
        this.references = null;
        this.out = out;
        this.config = config;
    }

    public String getDateFormatPattern() {
        return this.dateFormat instanceof SimpleDateFormat ? ((SimpleDateFormat) this.dateFormat).toPattern() : this.dateFormatPattern;
    }

    public DateFormat getDateFormat() {
        if (this.dateFormat == null && this.dateFormatPattern != null) {
            this.dateFormat = new SimpleDateFormat(this.dateFormatPattern);
        }
        return this.dateFormat;
    }

    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
        if (this.dateFormatPattern != null) {
            this.dateFormatPattern = null;
        }
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormatPattern = dateFormat;
        if (this.dateFormat != null) {
            this.dateFormat = null;
        }
    }

    public SerialContext getContext() {
        return this.context;
    }

    public void setContext(SerialContext context) {
        this.context = context;
    }

    public void setContext(SerialContext parent, Object object, Object fieldName, int features) {
        if (!isEnabled(SerializerFeature.DisableCircularReferenceDetect)) {
            this.context = new SerialContext(parent, object, fieldName, features);
            if (this.references == null) {
                this.references = new IdentityHashMap<>();
            }
            this.references.put(object, this.context);
        }
    }

    public final boolean isWriteClassName(Type fieldType, Object obj) {
        boolean result = this.out.isEnabled(SerializerFeature.WriteClassName);
        if (!result) {
            return false;
        }
        if (fieldType == null && isEnabled(SerializerFeature.NotWriteRootClassName)) {
            boolean isRoot = this.context.getParent() == null;
            if (isRoot) {
                return false;
            }
        }
        return true;
    }

    public SerialContext getSerialContext(Object object) {
        if (this.references == null) {
            return null;
        }
        return this.references.get(object);
    }

    public boolean containsReference(Object value) {
        if (this.references == null) {
            return false;
        }
        return this.references.containsKey(value);
    }

    public void writeReference(Object object) {
        SerialContext context = getContext();
        Object current = context.getObject();
        if (object == current) {
            this.out.write("{\"$ref\":\"@\"}");
            return;
        }
        SerialContext parentContext = context.getParent();
        if (parentContext != null && object == parentContext.getObject()) {
            this.out.write("{\"$ref\":\"..\"}");
            return;
        }
        SerialContext rootContext = context;
        while (rootContext.getParent() != null) {
            rootContext = rootContext.getParent();
        }
        if (object == rootContext.getObject()) {
            this.out.write("{\"$ref\":\"$\"}");
            return;
        }
        SerialContext refContext = getSerialContext(object);
        String path = refContext.getPath();
        this.out.write("{\"$ref\":\"");
        this.out.write(path);
        this.out.write("\"}");
    }

    public List<ValueFilter> getValueFilters() {
        if (this.valueFilters == null) {
            this.valueFilters = new ArrayList();
        }
        return this.valueFilters;
    }

    public List<ValueFilter> getValueFiltersDirect() {
        return this.valueFilters;
    }

    public int getIndentCount() {
        return this.indentCount;
    }

    public void incrementIndent() {
        this.indentCount++;
    }

    public void decrementIdent() {
        this.indentCount--;
    }

    public void println() {
        this.out.write('\n');
        for (int i = 0; i < this.indentCount; i++) {
            this.out.write(this.indent);
        }
    }

    public List<BeforeFilter> getBeforeFilters() {
        if (this.beforeFilters == null) {
            this.beforeFilters = new ArrayList();
        }
        return this.beforeFilters;
    }

    public List<BeforeFilter> getBeforeFiltersDirect() {
        return this.beforeFilters;
    }

    public List<AfterFilter> getAfterFilters() {
        if (this.afterFilters == null) {
            this.afterFilters = new ArrayList();
        }
        return this.afterFilters;
    }

    public List<AfterFilter> getAfterFiltersDirect() {
        return this.afterFilters;
    }

    public List<NameFilter> getNameFilters() {
        if (this.nameFilters == null) {
            this.nameFilters = new ArrayList();
        }
        return this.nameFilters;
    }

    public List<NameFilter> getNameFiltersDirect() {
        return this.nameFilters;
    }

    public List<PropertyPreFilter> getPropertyPreFilters() {
        if (this.propertyPreFilters == null) {
            this.propertyPreFilters = new ArrayList();
        }
        return this.propertyPreFilters;
    }

    public List<PropertyPreFilter> getPropertyPreFiltersDirect() {
        return this.propertyPreFilters;
    }

    public List<PropertyFilter> getPropertyFilters() {
        if (this.propertyFilters == null) {
            this.propertyFilters = new ArrayList();
        }
        return this.propertyFilters;
    }

    public List<PropertyFilter> getPropertyFiltersDirect() {
        return this.propertyFilters;
    }

    public SerializeWriter getWriter() {
        return this.out;
    }

    public String toString() {
        return this.out.toString();
    }

    public void config(SerializerFeature feature, boolean state) {
        this.out.config(feature, state);
    }

    public boolean isEnabled(SerializerFeature feature) {
        return this.out.isEnabled(feature);
    }

    public void writeNull() {
        this.out.writeNull();
    }

    public SerializeConfig getMapping() {
        return this.config;
    }

    public static final void write(Writer out, Object object) {
        SerializeWriter writer = new SerializeWriter();
        try {
            try {
                JSONSerializer serializer = new JSONSerializer(writer);
                serializer.write(object);
                writer.writeTo(out);
            } catch (IOException ex) {
                throw new JSONException(ex.getMessage(), ex);
            }
        } finally {
            writer.close();
        }
    }

    public static final void write(SerializeWriter out, Object object) {
        JSONSerializer serializer = new JSONSerializer(out);
        serializer.write(object);
    }

    public final void write(Object object) {
        if (object == null) {
            this.out.writeNull();
            return;
        }
        Class<?> clazz = object.getClass();
        ObjectSerializer writer = getObjectWriter(clazz);
        try {
            writer.write(this, object, null, null);
        } catch (IOException e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    public final void writeWithFieldName(Object object, Object fieldName) {
        writeWithFieldName(object, fieldName, null, 0);
    }

    protected final void writeKeyValue(char seperator, String key, Object value) {
        if (seperator != 0) {
            this.out.write(seperator);
        }
        this.out.writeFieldName(key);
        write(value);
    }

    public final void writeWithFieldName(Object object, Object fieldName, Type fieldType, int features) {
        try {
            if (object == null) {
                this.out.writeNull();
            } else {
                Class<?> clazz = object.getClass();
                ObjectSerializer writer = getObjectWriter(clazz);
                writer.write(this, object, fieldName, fieldType);
            }
        } catch (IOException e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    public final void writeWithFormat(Object object, String format) {
        if (object instanceof Date) {
            DateFormat dateFormat = getDateFormat();
            if (dateFormat == null) {
                dateFormat = new SimpleDateFormat(format);
            }
            String text = dateFormat.format((Date) object);
            this.out.writeString(text);
            return;
        }
        write(object);
    }

    public final void write(String text) {
        StringCodec.instance.write(this, text);
    }

    public ObjectSerializer getObjectWriter(Class<?> clazz) {
        ObjectSerializer writer = this.config.get(clazz);
        if (writer == null) {
            if (Map.class.isAssignableFrom(clazz)) {
                this.config.put(clazz, MapSerializer.instance);
            } else if (List.class.isAssignableFrom(clazz)) {
                this.config.put(clazz, ListSerializer.instance);
            } else if (Collection.class.isAssignableFrom(clazz)) {
                this.config.put(clazz, CollectionSerializer.instance);
            } else if (Date.class.isAssignableFrom(clazz)) {
                this.config.put(clazz, DateSerializer.instance);
            } else if (JSONAware.class.isAssignableFrom(clazz)) {
                this.config.put(clazz, JSONAwareSerializer.instance);
            } else if (JSONSerializable.class.isAssignableFrom(clazz)) {
                this.config.put(clazz, JSONSerializableSerializer.instance);
            } else if (JSONStreamAware.class.isAssignableFrom(clazz)) {
                this.config.put(clazz, JSONStreamAwareSerializer.instance);
            } else if (clazz.isEnum() || (clazz.getSuperclass() != null && clazz.getSuperclass().isEnum())) {
                this.config.put(clazz, EnumSerializer.instance);
            } else if (clazz.isArray()) {
                Class<?> componentType = clazz.getComponentType();
                ObjectSerializer compObjectSerializer = getObjectWriter(componentType);
                this.config.put(clazz, new ArraySerializer(componentType, compObjectSerializer));
            } else if (Throwable.class.isAssignableFrom(clazz)) {
                this.config.put(clazz, new ExceptionSerializer(clazz));
            } else if (TimeZone.class.isAssignableFrom(clazz)) {
                this.config.put(clazz, TimeZoneCodec.instance);
            } else if (Charset.class.isAssignableFrom(clazz)) {
                this.config.put(clazz, CharsetCodec.instance);
            } else if (Enumeration.class.isAssignableFrom(clazz)) {
                this.config.put(clazz, EnumerationSeriliazer.instance);
            } else if (Calendar.class.isAssignableFrom(clazz)) {
                this.config.put(clazz, CalendarCodec.instance);
            } else {
                boolean isCglibProxy = false;
                boolean isJavassistProxy = false;
                Class<?>[] arr$ = clazz.getInterfaces();
                int len$ = arr$.length;
                int i$ = 0;
                while (true) {
                    if (i$ >= len$) {
                        break;
                    }
                    Class<?> item = arr$[i$];
                    if (item.getName().equals("net.sf.cglib.proxy.Factory") || item.getName().equals("org.springframework.cglib.proxy.Factory")) {
                        break;
                    }
                    if (!item.getName().equals("javassist.util.proxy.ProxyObject")) {
                        i$++;
                    } else {
                        isJavassistProxy = true;
                        break;
                    }
                }
                isCglibProxy = true;
                if (isCglibProxy || isJavassistProxy) {
                    Class<?> superClazz = clazz.getSuperclass();
                    ObjectSerializer superWriter = getObjectWriter(superClazz);
                    this.config.put(clazz, superWriter);
                    return superWriter;
                }
                if (Proxy.isProxyClass(clazz)) {
                    this.config.put(clazz, this.config.createJavaBeanSerializer(clazz));
                } else {
                    this.config.put(clazz, this.config.createJavaBeanSerializer(clazz));
                }
            }
            writer = this.config.get(clazz);
        }
        return writer;
    }

    public void close() {
        this.out.close();
    }
}
