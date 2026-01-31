package com.unisound.vui.transport;

import com.alibaba.fastjson.TypeReference;
import com.unisound.client.SpeechConstants;
import com.unisound.vui.transport.out.OptionContent;
import com.unisound.vui.transport.out.SdkCreatorInfo;
import com.unisound.vui.transport.out.VocabContent;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class c {

    /* renamed from: a, reason: collision with root package name */
    static final Type f429a = new TypeReference<Object<SdkCreatorInfo>>() { // from class: com.unisound.vui.transport.c.1
    }.getType();
    static final Type b = new TypeReference<Object<VocabContent>>() { // from class: com.unisound.vui.transport.c.2
    }.getType();
    static final Type c = new TypeReference<Object<OptionContent>>() { // from class: com.unisound.vui.transport.c.3
    }.getType();
    static final Map<Integer, Type> d;
    private final Map<Integer, Type> e;
    private final Type f;

    static {
        HashMap map = new HashMap();
        map.put(4108, f429a);
        map.put(Integer.valueOf(SpeechConstants.VPR_BLUETOOTH_ENABLED), b);
        map.put(4000, c);
        d = Collections.unmodifiableMap(map);
    }

    public c(int i, Type type) {
        if (type == null) {
            throw new NullPointerException("defaultValue");
        }
        this.e = new HashMap(i);
        this.f = type;
    }

    public c(Type type) {
        this(2, type);
    }

    public Type a(Integer num) {
        Type type = d.get(num);
        return type == null ? this.f : type;
    }
}
