package com.alibaba.fastjson.serializer;

/* loaded from: classes.dex */
public interface PropertyPreFilter extends SerializeFilter {
    boolean apply(JSONSerializer jSONSerializer, Object obj, String str);
}
