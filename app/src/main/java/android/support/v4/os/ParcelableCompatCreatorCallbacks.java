package android.support.v4.os;

import android.os.Parcel;

/* loaded from: classes.dex */
public interface ParcelableCompatCreatorCallbacks<T> {
    T createFromParcel(Parcel parcel, ClassLoader classLoader);

    T[] newArray(int i);
}
