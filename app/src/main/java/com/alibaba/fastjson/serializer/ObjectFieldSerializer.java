package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.util.FieldInfo;
import com.tencent.bugly.Bugly;
import java.util.Collection;

/* loaded from: classes.dex */
public class ObjectFieldSerializer extends FieldSerializer {
    private String format;
    private RuntimeSerializerInfo runtimeInfo;
    boolean writeEnumUsingToString;
    boolean writeNullBooleanAsFalse;
    boolean writeNullListAsEmpty;
    boolean writeNullStringAsEmpty;
    boolean writeNumberAsZero;

    public ObjectFieldSerializer(FieldInfo fieldInfo) {
        super(fieldInfo);
        this.writeNumberAsZero = false;
        this.writeNullStringAsEmpty = false;
        this.writeNullBooleanAsFalse = false;
        this.writeNullListAsEmpty = false;
        this.writeEnumUsingToString = false;
        JSONField annotation = (JSONField) fieldInfo.getAnnotation(JSONField.class);
        if (annotation != null) {
            this.format = annotation.format();
            if (this.format.trim().length() == 0) {
                this.format = null;
            }
            SerializerFeature[] arr$ = annotation.serialzeFeatures();
            for (SerializerFeature feature : arr$) {
                if (feature == SerializerFeature.WriteNullNumberAsZero) {
                    this.writeNumberAsZero = true;
                } else if (feature == SerializerFeature.WriteNullStringAsEmpty) {
                    this.writeNullStringAsEmpty = true;
                } else if (feature == SerializerFeature.WriteNullBooleanAsFalse) {
                    this.writeNullBooleanAsFalse = true;
                } else if (feature == SerializerFeature.WriteNullListAsEmpty) {
                    this.writeNullListAsEmpty = true;
                } else if (feature == SerializerFeature.WriteEnumUsingToString) {
                    this.writeEnumUsingToString = true;
                }
            }
        }
    }

    @Override // com.alibaba.fastjson.serializer.FieldSerializer
    public void writeProperty(JSONSerializer serializer, Object propertyValue) throws Exception {
        writePrefix(serializer);
        writeValue(serializer, propertyValue);
    }

    @Override // com.alibaba.fastjson.serializer.FieldSerializer
    public void writeValue(JSONSerializer serializer, Object propertyValue) throws Exception {
        Class<?> runtimeFieldClass;
        if (this.format != null) {
            serializer.writeWithFormat(propertyValue, this.format);
            return;
        }
        if (this.runtimeInfo == null) {
            if (propertyValue == null) {
                runtimeFieldClass = this.fieldInfo.getFieldClass();
            } else {
                runtimeFieldClass = propertyValue.getClass();
            }
            ObjectSerializer fieldSerializer = serializer.getObjectWriter(runtimeFieldClass);
            this.runtimeInfo = new RuntimeSerializerInfo(fieldSerializer, runtimeFieldClass);
        }
        RuntimeSerializerInfo runtimeInfo = this.runtimeInfo;
        if (propertyValue == null) {
            if (this.writeNumberAsZero && Number.class.isAssignableFrom(runtimeInfo.runtimeFieldClass)) {
                serializer.getWriter().write('0');
                return;
            }
            if (this.writeNullStringAsEmpty && String.class == runtimeInfo.runtimeFieldClass) {
                serializer.getWriter().write("\"\"");
                return;
            }
            if (this.writeNullBooleanAsFalse && Boolean.class == runtimeInfo.runtimeFieldClass) {
                serializer.getWriter().write(Bugly.SDK_IS_DEV);
                return;
            } else if (this.writeNullListAsEmpty && Collection.class.isAssignableFrom(runtimeInfo.runtimeFieldClass)) {
                serializer.getWriter().write("[]");
                return;
            } else {
                runtimeInfo.fieldSerializer.write(serializer, null, this.fieldInfo.getName(), null);
                return;
            }
        }
        if (this.writeEnumUsingToString && runtimeInfo.runtimeFieldClass.isEnum()) {
            serializer.getWriter().writeString(((Enum) propertyValue).name());
            return;
        }
        Class<?> valueClass = propertyValue.getClass();
        if (valueClass == runtimeInfo.runtimeFieldClass) {
            runtimeInfo.fieldSerializer.write(serializer, propertyValue, this.fieldInfo.getName(), this.fieldInfo.getFieldType());
        } else {
            ObjectSerializer valueSerializer = serializer.getObjectWriter(valueClass);
            valueSerializer.write(serializer, propertyValue, this.fieldInfo.getName(), this.fieldInfo.getFieldType());
        }
    }

    static class RuntimeSerializerInfo {
        ObjectSerializer fieldSerializer;
        Class<?> runtimeFieldClass;

        public RuntimeSerializerInfo(ObjectSerializer fieldSerializer, Class<?> runtimeFieldClass) {
            this.fieldSerializer = fieldSerializer;
            this.runtimeFieldClass = runtimeFieldClass;
        }
    }
}
