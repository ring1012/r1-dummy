package com.alibaba.fastjson.serializer;

/* loaded from: classes.dex */
public enum SerializerFeature {
    QuoteFieldNames,
    UseSingleQuotes,
    WriteMapNullValue,
    WriteEnumUsingToString,
    UseISO8601DateFormat,
    WriteNullListAsEmpty,
    WriteNullStringAsEmpty,
    WriteNullNumberAsZero,
    WriteNullBooleanAsFalse,
    SkipTransientField,
    SortField,
    WriteTabAsSpecial,
    PrettyFormat,
    WriteClassName,
    DisableCircularReferenceDetect,
    WriteSlashAsSpecial,
    BrowserCompatible,
    WriteDateUseDateFormat,
    NotWriteRootClassName,
    DisableCheckSpecialChar,
    BeanToArray,
    WriteNonStringKeyAsString,
    NotWriteDefaultValue;

    private final int mask = 1 << ordinal();

    SerializerFeature() {
    }

    public final int getMask() {
        return this.mask;
    }

    public static boolean isEnabled(int features, SerializerFeature feature) {
        return (feature.getMask() & features) != 0;
    }

    public static int config(int features, SerializerFeature feature, boolean state) {
        if (state) {
            return features | feature.getMask();
        }
        return features & (feature.getMask() ^ (-1));
    }

    public static int of(SerializerFeature[] features) {
        if (features == null) {
            return 0;
        }
        int value = 0;
        for (SerializerFeature feature : features) {
            value |= feature.getMask();
        }
        return value;
    }
}
