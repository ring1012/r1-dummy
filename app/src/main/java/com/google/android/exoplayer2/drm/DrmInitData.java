package com.google.android.exoplayer2.drm;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/* loaded from: classes.dex */
public final class DrmInitData implements Comparator<SchemeData>, Parcelable {
    public static final Parcelable.Creator<DrmInitData> CREATOR = new Parcelable.Creator<DrmInitData>() { // from class: com.google.android.exoplayer2.drm.DrmInitData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DrmInitData createFromParcel(Parcel in) {
            return new DrmInitData(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DrmInitData[] newArray(int size) {
            return new DrmInitData[size];
        }
    };
    private int hashCode;
    public final int schemeDataCount;
    private final SchemeData[] schemeDatas;

    public DrmInitData(List<SchemeData> schemeDatas) {
        this(false, (SchemeData[]) schemeDatas.toArray(new SchemeData[schemeDatas.size()]));
    }

    public DrmInitData(SchemeData... schemeDatas) {
        this(true, schemeDatas);
    }

    private DrmInitData(boolean cloneSchemeDatas, SchemeData... schemeDatas) {
        schemeDatas = cloneSchemeDatas ? (SchemeData[]) schemeDatas.clone() : schemeDatas;
        Arrays.sort(schemeDatas, this);
        for (int i = 1; i < schemeDatas.length; i++) {
            if (schemeDatas[i - 1].uuid.equals(schemeDatas[i].uuid)) {
                throw new IllegalArgumentException("Duplicate data for uuid: " + schemeDatas[i].uuid);
            }
        }
        this.schemeDatas = schemeDatas;
        this.schemeDataCount = schemeDatas.length;
    }

    DrmInitData(Parcel in) {
        this.schemeDatas = (SchemeData[]) in.createTypedArray(SchemeData.CREATOR);
        this.schemeDataCount = this.schemeDatas.length;
    }

    public SchemeData get(UUID uuid) {
        for (SchemeData schemeData : this.schemeDatas) {
            if (schemeData.matches(uuid)) {
                return schemeData;
            }
        }
        return null;
    }

    public SchemeData get(int index) {
        return this.schemeDatas[index];
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = Arrays.hashCode(this.schemeDatas);
        }
        return this.hashCode;
    }

    @Override // java.util.Comparator
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return Arrays.equals(this.schemeDatas, ((DrmInitData) obj).schemeDatas);
    }

    @Override // java.util.Comparator
    public int compare(SchemeData first, SchemeData second) {
        if (C.UUID_NIL.equals(first.uuid)) {
            return C.UUID_NIL.equals(second.uuid) ? 0 : 1;
        }
        return first.uuid.compareTo(second.uuid);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(this.schemeDatas, 0);
    }

    public static final class SchemeData implements Parcelable {
        public static final Parcelable.Creator<SchemeData> CREATOR = new Parcelable.Creator<SchemeData>() { // from class: com.google.android.exoplayer2.drm.DrmInitData.SchemeData.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SchemeData createFromParcel(Parcel in) {
                return new SchemeData(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SchemeData[] newArray(int size) {
                return new SchemeData[size];
            }
        };
        public final byte[] data;
        private int hashCode;
        public final String mimeType;
        public final boolean requiresSecureDecryption;
        private final UUID uuid;

        public SchemeData(UUID uuid, String mimeType, byte[] data) {
            this(uuid, mimeType, data, false);
        }

        public SchemeData(UUID uuid, String mimeType, byte[] data, boolean requiresSecureDecryption) {
            this.uuid = (UUID) Assertions.checkNotNull(uuid);
            this.mimeType = (String) Assertions.checkNotNull(mimeType);
            this.data = (byte[]) Assertions.checkNotNull(data);
            this.requiresSecureDecryption = requiresSecureDecryption;
        }

        SchemeData(Parcel in) {
            this.uuid = new UUID(in.readLong(), in.readLong());
            this.mimeType = in.readString();
            this.data = in.createByteArray();
            this.requiresSecureDecryption = in.readByte() != 0;
        }

        public boolean matches(UUID schemeUuid) {
            return C.UUID_NIL.equals(this.uuid) || schemeUuid.equals(this.uuid);
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof SchemeData)) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            SchemeData other = (SchemeData) obj;
            return this.mimeType.equals(other.mimeType) && Util.areEqual(this.uuid, other.uuid) && Arrays.equals(this.data, other.data);
        }

        public int hashCode() {
            if (this.hashCode == 0) {
                int result = this.uuid.hashCode();
                this.hashCode = (((result * 31) + this.mimeType.hashCode()) * 31) + Arrays.hashCode(this.data);
            }
            return this.hashCode;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.uuid.getMostSignificantBits());
            dest.writeLong(this.uuid.getLeastSignificantBits());
            dest.writeString(this.mimeType);
            dest.writeByteArray(this.data);
            dest.writeByte((byte) (this.requiresSecureDecryption ? 1 : 0));
        }
    }
}
