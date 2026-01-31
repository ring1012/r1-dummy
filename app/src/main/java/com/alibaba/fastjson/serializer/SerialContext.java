package com.alibaba.fastjson.serializer;

/* loaded from: classes.dex */
public class SerialContext {
    private int features;
    private final Object fieldName;
    private final Object object;
    private final SerialContext parent;

    public SerialContext(SerialContext parent, Object object, Object fieldName, int features) {
        this.parent = parent;
        this.object = object;
        this.fieldName = fieldName;
        this.features = features;
    }

    public SerialContext getParent() {
        return this.parent;
    }

    public Object getObject() {
        return this.object;
    }

    public Object getFieldName() {
        return this.fieldName;
    }

    public String getPath() {
        if (this.parent == null) {
            return "$";
        }
        if (this.fieldName instanceof Integer) {
            return this.parent.getPath() + "[" + this.fieldName + "]";
        }
        return this.parent.getPath() + "." + this.fieldName;
    }

    public String toString() {
        return getPath();
    }

    public boolean isEnabled(SerializerFeature feature) {
        return SerializerFeature.isEnabled(this.features, feature);
    }
}
