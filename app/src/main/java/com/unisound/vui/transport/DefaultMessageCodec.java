package com.unisound.vui.transport;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.unisound.vui.transport.a;
import java.lang.reflect.Type;

/* loaded from: classes.dex */
public final class DefaultMessageCodec implements MessageCodec {
    private static final Type DEFAULT_TYPE = new TypeReference<Object>() { // from class: com.unisound.vui.transport.DefaultMessageCodec.1
    }.getType();
    private final a classifier = new a.C0012a(new c(DEFAULT_TYPE));

    @Override // com.unisound.vui.transport.MessageCodec
    public b decode(String message) {
        if (message == null) {
            return null;
        }
        return (b) JSON.parseObject(message, this.classifier.a(message), new Feature[0]);
    }

    @Override // com.unisound.vui.transport.MessageCodec
    public String encode(b message) {
        return JSON.toJSONString(message);
    }
}
