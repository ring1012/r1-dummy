package com.alibaba.fastjson.parser;

/* loaded from: classes.dex */
public enum Feature {
    AutoCloseSource,
    AllowComment,
    AllowUnQuotedFieldNames,
    AllowSingleQuotes,
    InternFieldNames,
    AllowISO8601DateFormat,
    AllowArbitraryCommas,
    UseBigDecimal,
    IgnoreNotMatch,
    SortFeidFastMatch,
    DisableASM,
    DisableCircularReferenceDetect,
    InitStringFieldAsEmpty,
    SupportArrayToBean;

    private final int mask = 1 << ordinal();

    Feature() {
    }

    public final int getMask() {
        return this.mask;
    }

    public static boolean isEnabled(int features, Feature feature) {
        return (feature.getMask() & features) != 0;
    }

    public static int config(int features, Feature feature, boolean state) {
        if (state) {
            return features | feature.getMask();
        }
        return features & (feature.getMask() ^ (-1));
    }

    public static int of(Feature[] features) {
        if (features == null) {
            return 0;
        }
        int value = 0;
        for (Feature feature : features) {
            value |= feature.getMask();
        }
        return value;
    }
}
