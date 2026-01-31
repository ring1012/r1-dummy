package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.util.FieldInfo;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public abstract class FieldSerializer {
    private final String double_quoted_fieldPrefix;
    protected final FieldInfo fieldInfo;
    private final String single_quoted_fieldPrefix;
    private final String un_quoted_fieldPrefix;
    private boolean writeNull;

    public abstract void writeProperty(JSONSerializer jSONSerializer, Object obj) throws Exception;

    public abstract void writeValue(JSONSerializer jSONSerializer, Object obj) throws Exception;

    public FieldSerializer(FieldInfo fieldInfo) throws SecurityException {
        this.writeNull = false;
        this.fieldInfo = fieldInfo;
        fieldInfo.setAccessible(true);
        this.double_quoted_fieldPrefix = '\"' + fieldInfo.getName() + "\":";
        this.single_quoted_fieldPrefix = '\'' + fieldInfo.getName() + "':";
        this.un_quoted_fieldPrefix = fieldInfo.getName() + ":";
        JSONField annotation = (JSONField) fieldInfo.getAnnotation(JSONField.class);
        if (annotation != null) {
            SerializerFeature[] arr$ = annotation.serialzeFeatures();
            for (SerializerFeature feature : arr$) {
                if (feature == SerializerFeature.WriteMapNullValue) {
                    this.writeNull = true;
                }
            }
        }
    }

    public boolean isWriteNull() {
        return this.writeNull;
    }

    public Field getField() {
        return this.fieldInfo.getField();
    }

    public String getName() {
        return this.fieldInfo.getName();
    }

    public Method getMethod() {
        return this.fieldInfo.getMethod();
    }

    public void writePrefix(JSONSerializer serializer) throws IOException {
        SerializeWriter out = serializer.getWriter();
        if (serializer.isEnabled(SerializerFeature.QuoteFieldNames)) {
            if (serializer.isEnabled(SerializerFeature.UseSingleQuotes)) {
                out.write(this.single_quoted_fieldPrefix);
                return;
            } else {
                out.write(this.double_quoted_fieldPrefix);
                return;
            }
        }
        out.write(this.un_quoted_fieldPrefix);
    }

    public Object getPropertyValue(Object object) throws Exception {
        try {
            return this.fieldInfo.get(object);
        } catch (Exception ex) {
            throw new JSONException("get property error。 " + this.fieldInfo.gerQualifiedName(), ex);
        }
    }
}
