package android.support.v4.os;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public final class ParcelableCompat {
    public static <T> Parcelable.Creator<T> newCreator(ParcelableCompatCreatorCallbacks<T> callbacks) {
        return Build.VERSION.SDK_INT >= 13 ? ParcelableCompatCreatorHoneycombMR2Stub.instantiate(callbacks) : new CompatCreator(callbacks);
    }

    static class CompatCreator<T> implements Parcelable.Creator<T> {
        final ParcelableCompatCreatorCallbacks<T> mCallbacks;

        public CompatCreator(ParcelableCompatCreatorCallbacks<T> callbacks) {
            this.mCallbacks = callbacks;
        }

        @Override // android.os.Parcelable.Creator
        public T createFromParcel(Parcel source) {
            return this.mCallbacks.createFromParcel(source, null);
        }

        @Override // android.os.Parcelable.Creator
        public T[] newArray(int size) {
            return this.mCallbacks.newArray(size);
        }
    }

    private ParcelableCompat() {
    }
}
