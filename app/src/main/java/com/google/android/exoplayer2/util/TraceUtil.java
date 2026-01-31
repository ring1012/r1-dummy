package com.google.android.exoplayer2.util;

import android.annotation.TargetApi;
import android.os.Trace;

/* loaded from: classes.dex */
public final class TraceUtil {
    private TraceUtil() {
    }

    public static void beginSection(String sectionName) {
        if (Util.SDK_INT >= 18) {
            beginSectionV18(sectionName);
        }
    }

    public static void endSection() {
        if (Util.SDK_INT >= 18) {
            endSectionV18();
        }
    }

    @TargetApi(18)
    private static void beginSectionV18(String sectionName) {
        Trace.beginSection(sectionName);
    }

    @TargetApi(18)
    private static void endSectionV18() {
        Trace.endSection();
    }
}
