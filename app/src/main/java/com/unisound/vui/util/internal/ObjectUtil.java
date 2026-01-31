package com.unisound.vui.util.internal;

/* loaded from: classes.dex */
public final class ObjectUtil {
    private ObjectUtil() {
    }

    public static <T> T checkNotNull(T arg, String text) {
        if (arg == null) {
            throw new NullPointerException(text);
        }
        return arg;
    }
}
