package android.support.v4.os;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;

@RequiresApi(16)
@TargetApi(16)
/* loaded from: classes.dex */
class CancellationSignalCompatJellybean {
    CancellationSignalCompatJellybean() {
    }

    public static Object create() {
        return new android.os.CancellationSignal();
    }

    public static void cancel(Object cancellationSignalObj) {
        ((android.os.CancellationSignal) cancellationSignalObj).cancel();
    }
}
