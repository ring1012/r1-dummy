package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class JavaBeanSerializer implements ObjectSerializer {
    private int features;
    private final FieldSerializer[] getters;
    private final FieldSerializer[] sortedGetters;

    public FieldSerializer[] getGetters() {
        return this.getters;
    }

    public JavaBeanSerializer(Class<?> clazz) {
        this(clazz, (Map<String, String>) null);
    }

    public JavaBeanSerializer(Class<?> clazz, String... aliasList) {
        this(clazz, createAliasMap(aliasList));
    }

    static Map<String, String> createAliasMap(String... aliasList) {
        Map<String, String> aliasMap = new HashMap<>();
        for (String alias : aliasList) {
            aliasMap.put(alias, alias);
        }
        return aliasMap;
    }

    public JavaBeanSerializer(Class<?> clazz, Map<String, String> aliasMap) throws SecurityException {
        this.features = 0;
        this.features = TypeUtils.getSerializeFeatures(clazz);
        List<FieldSerializer> getterList = new ArrayList<>();
        List<FieldInfo> fieldInfoList = TypeUtils.computeGetters(clazz, aliasMap, false);
        for (FieldInfo fieldInfo : fieldInfoList) {
            getterList.add(createFieldSerializer(fieldInfo));
        }
        this.getters = (FieldSerializer[]) getterList.toArray(new FieldSerializer[getterList.size()]);
        List<FieldSerializer> getterList2 = new ArrayList<>();
        List<FieldInfo> fieldInfoList2 = TypeUtils.computeGetters(clazz, aliasMap, true);
        for (FieldInfo fieldInfo2 : fieldInfoList2) {
            getterList2.add(createFieldSerializer(fieldInfo2));
        }
        this.sortedGetters = (FieldSerializer[]) getterList2.toArray(new FieldSerializer[getterList2.size()]);
    }

    protected boolean isWriteClassName(JSONSerializer serializer, Object obj, Type fieldType, Object fieldName) {
        return serializer.isWriteClassName(fieldType, obj);
    }

    @Override // com.alibaba.fastjson.serializer.ObjectSerializer
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        FieldSerializer[] getters;
        Class<?> fieldCLass;
        Field field;
        SerializeWriter out = serializer.getWriter();
        if (object == null) {
            out.writeNull();
            return;
        }
        if (!writeReference(serializer, object)) {
            if (out.isEnabled(SerializerFeature.SortField)) {
                getters = this.sortedGetters;
            } else {
                getters = this.getters;
            }
            SerialContext parent = serializer.getContext();
            serializer.setContext(parent, object, fieldName, this.features);
            boolean writeAsArray = isWriteAsArray(serializer);
            char startSeperator = writeAsArray ? '[' : '{';
            char endSeperator = writeAsArray ? ']' : '}';
            try {
                try {
                    out.append(startSeperator);
                    if (getters.length > 0 && out.isEnabled(SerializerFeature.PrettyFormat)) {
                        serializer.incrementIndent();
                        serializer.println();
                    }
                    boolean commaFlag = false;
                    if (isWriteClassName(serializer, object, fieldType, fieldName)) {
                        Class<?> objClass = object.getClass();
                        if (objClass != fieldType) {
                            out.writeFieldName(JSON.DEFAULT_TYPE_KEY);
                            serializer.write(object.getClass());
                            commaFlag = true;
                        }
                    }
                    char seperator = commaFlag ? ',' : (char) 0;
                    char newSeperator = FilterUtils.writeBefore(serializer, object, seperator);
                    boolean commaFlag2 = newSeperator == ',';
                    for (FieldSerializer fieldSerializer : getters) {
                        if ((!serializer.isEnabled(SerializerFeature.SkipTransientField) || (field = fieldSerializer.getField()) == null || !Modifier.isTransient(field.getModifiers())) && FilterUtils.applyName(serializer, object, fieldSerializer.getName())) {
                            Object propertyValue = fieldSerializer.getPropertyValue(object);
                            if (FilterUtils.apply(serializer, object, fieldSerializer.getName(), propertyValue)) {
                                String key = FilterUtils.processKey(serializer, object, fieldSerializer.getName(), propertyValue);
                                Object propertyValue2 = FilterUtils.processValue(serializer, object, fieldSerializer.getName(), propertyValue);
                                if ((propertyValue2 != null || writeAsArray || fieldSerializer.isWriteNull() || serializer.isEnabled(SerializerFeature.WriteMapNullValue)) && (propertyValue2 == null || !serializer.isEnabled(SerializerFeature.NotWriteDefaultValue) || (((fieldCLass = fieldSerializer.fieldInfo.getFieldClass()) != Byte.TYPE || !(propertyValue2 instanceof Byte) || ((Byte) propertyValue2).byteValue() != 0) && ((fieldCLass != Short.TYPE || !(propertyValue2 instanceof Short) || ((Short) propertyValue2).shortValue() != 0) && ((fieldCLass != Integer.TYPE || !(propertyValue2 instanceof Integer) || ((Integer) propertyValue2).intValue() != 0) && ((fieldCLass != Long.TYPE || !(propertyValue2 instanceof Long) || ((Long) propertyValue2).longValue() != 0) && ((fieldCLass != Float.TYPE || !(propertyValue2 instanceof Float) || ((Float) propertyValue2).floatValue() != 0.0f) && ((fieldCLass != Double.TYPE || !(propertyValue2 instanceof Double) || ((Double) propertyValue2).doubleValue() != 0.0d) && (fieldCLass != Boolean.TYPE || !(propertyValue2 instanceof Boolean) || ((Boolean) propertyValue2).booleanValue()))))))))) {
                                    if (commaFlag2) {
                                        out.append(',');
                                        if (out.isEnabled(SerializerFeature.PrettyFormat)) {
                                            serializer.println();
                                        }
                                    }
                                    if (key != fieldSerializer.getName()) {
                                        if (!writeAsArray) {
                                            out.writeFieldName(key);
                                        }
                                        serializer.write(propertyValue2);
                                    } else if (propertyValue != propertyValue2) {
                                        if (!writeAsArray) {
                                            fieldSerializer.writePrefix(serializer);
                                        }
                                        serializer.write(propertyValue2);
                                    } else if (!writeAsArray) {
                                        fieldSerializer.writeProperty(serializer, propertyValue2);
                                    } else {
                                        fieldSerializer.writeValue(serializer, propertyValue2);
                                    }
                                    commaFlag2 = true;
                                }
                            }
                        }
                    }
                    FilterUtils.writeAfter(serializer, object, commaFlag2 ? ',' : (char) 0);
                    if (getters.length > 0 && out.isEnabled(SerializerFeature.PrettyFormat)) {
                        serializer.decrementIdent();
                        serializer.println();
                    }
                    out.append(endSeperator);
                } catch (Exception e) {
                    throw new JSONException("write javaBean error", e);
                }
            } finally {
                serializer.setContext(parent);
            }
        }
    }

    public boolean writeReference(JSONSerializer serializer, Object object) {
        SerialContext context = serializer.getContext();
        if ((context != null && context.isEnabled(SerializerFeature.DisableCircularReferenceDetect)) || !serializer.containsReference(object)) {
            return false;
        }
        serializer.writeReference(object);
        return true;
    }

    public FieldSerializer createFieldSerializer(FieldInfo fieldInfo) {
        Class<?> clazz = fieldInfo.getFieldClass();
        return clazz == Number.class ? new NumberFieldSerializer(fieldInfo) : new ObjectFieldSerializer(fieldInfo);
    }

    public boolean isWriteAsArray(JSONSerializer serializer) {
        if (SerializerFeature.isEnabled(this.features, SerializerFeature.BeanToArray) || serializer.isEnabled(SerializerFeature.BeanToArray)) {
            return true;
        }
        return false;
    }
}
