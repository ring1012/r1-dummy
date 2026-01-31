package android.support.v4.os;

import android.annotation.TargetApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;

/* compiled from: ParcelableCompatHoneycombMR2.java */
@RequiresApi(13)
@TargetApi(13)
/* loaded from: classes.dex */
class ParcelableCompatCreatorHoneycombMR2<T> implements Parcelable.ClassLoaderCreator<T> {
    private final ParcelableCompatCreatorCallbacks<T> mCallbacks;

    public ParcelableCompatCreatorHoneycombMR2(ParcelableCompatCreatorCallbacks<T> callbacks) {
        this.mCallbacks = callbacks;
    }

    @Override // android.os.Parcelable.Creator
    public T createFromParcel(Parcel in) {
        return this.mCallbacks.createFromParcel(in, null);
    }

    @Override // android.os.Parcelable.ClassLoaderCreator
    public T createFromParcel(Parcel in, ClassLoader loader) {
        return this.mCallbacks.createFromParcel(in, loader);
    }

    @Override // android.os.Parcelable.Creator
    public T[] newArray(int size) {
        return this.mCallbacks.newArray(size);
    }
}
