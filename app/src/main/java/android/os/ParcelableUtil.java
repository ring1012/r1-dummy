package android.os;

import android.os.Parcelable;
import android.util.Log;
import java.io.Serializable;

/* loaded from: classes.dex */
public class ParcelableUtil implements Parcelable {
    public static final Parcelable.Creator<ParcelableUtil> CREATOR = new Parcelable.Creator<ParcelableUtil>() { // from class: android.os.ParcelableUtil.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ParcelableUtil createFromParcel(Parcel source) {
            return new ParcelableUtil(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ParcelableUtil[] newArray(int size) {
            return new ParcelableUtil[size];
        }
    };
    private final String TAG = "ParcelableUtil";
    private Object val;

    private ParcelableUtil(Object s) {
        this.val = s;
        if (this.val instanceof String) {
            Log.v("ParcelableUtil", "w ParcelableUtil val is String");
        } else if (this.val instanceof Serializable) {
            Log.v("ParcelableUtil", "w ParcelableUtil val is Serializable");
        } else {
            Log.v("ParcelableUtil", "w ParcelableUtil val is ddd");
        }
    }

    private ParcelableUtil(Parcel in, int a2) {
        this.val = in.readValue((ClassLoader) null);
        if (this.val instanceof String) {
            Log.v("ParcelableUtil", "r ParcelableUtil val is String");
        } else if (this.val instanceof Serializable) {
            Log.v("ParcelableUtil", "r ParcelableUtil val is Serializable");
        } else {
            Log.v("ParcelableUtil", "r ParcelableUtil val is ddd");
        }
    }

    public static ParcelableUtil obtain(String v) {
        return new ParcelableUtil(v);
    }

    public static ParcelableUtil obtain(int v) {
        return new ParcelableUtil(new Integer(v));
    }

    public static ParcelableUtil obtain(short v) {
        return new ParcelableUtil(new Short(v));
    }

    public static ParcelableUtil obtain(long v) {
        return new ParcelableUtil(new Long(v));
    }

    public static ParcelableUtil obtain(float v) {
        return new ParcelableUtil(new Float(v));
    }

    public static ParcelableUtil obtain(double v) {
        return new ParcelableUtil(new Double(v));
    }

    public static ParcelableUtil obtain(boolean v) {
        return new ParcelableUtil(new Boolean(v));
    }

    public static ParcelableUtil obtain(Serializable v) {
        return new ParcelableUtil(v);
    }

    public Object getValue() {
        return this.val;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.val);
    }

    protected ParcelableUtil(Parcel in) {
        this.val = in.readParcelable(Object.class.getClassLoader());
    }
}
