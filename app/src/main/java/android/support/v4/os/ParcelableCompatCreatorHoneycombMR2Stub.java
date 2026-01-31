package android.support.v4.os;

import android.annotation.TargetApi;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;

/* compiled from: ParcelableCompatHoneycombMR2.java */
@RequiresApi(13)
@TargetApi(13)
/* loaded from: classes.dex */
class ParcelableCompatCreatorHoneycombMR2Stub {
    ParcelableCompatCreatorHoneycombMR2Stub() {
    }

    static <T> Parcelable.Creator<T> instantiate(ParcelableCompatCreatorCallbacks<T> callbacks) {
        return new ParcelableCompatCreatorHoneycombMR2(callbacks);
    }
}
