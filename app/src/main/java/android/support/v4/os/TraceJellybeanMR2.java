package android.support.v4.os;

import android.annotation.TargetApi;
import android.os.Trace;
import android.support.annotation.RequiresApi;

@RequiresApi(18)
@TargetApi(18)
/* loaded from: classes.dex */
class TraceJellybeanMR2 {
    TraceJellybeanMR2() {
    }

    public static void beginSection(String section) {
        Trace.beginSection(section);
    }

    public static void endSection() {
        Trace.endSection();
    }
}
