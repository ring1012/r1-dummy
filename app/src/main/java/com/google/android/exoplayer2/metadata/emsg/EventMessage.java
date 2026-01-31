package com.google.android.exoplayer2.metadata.emsg;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.util.Util;
import java.util.Arrays;

/* loaded from: classes.dex */
public final class EventMessage implements Metadata.Entry {
    public static final Parcelable.Creator<EventMessage> CREATOR = new Parcelable.Creator<EventMessage>() { // from class: com.google.android.exoplayer2.metadata.emsg.EventMessage.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public EventMessage createFromParcel(Parcel in) {
            return new EventMessage(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public EventMessage[] newArray(int size) {
            return new EventMessage[size];
        }
    };
    public final long durationMs;
    private int hashCode;
    public final long id;
    public final byte[] messageData;
    public final String schemeIdUri;
    public final String value;

    public EventMessage(String schemeIdUri, String value, long durationMs, long id, byte[] messageData) {
        this.schemeIdUri = schemeIdUri;
        this.value = value;
        this.durationMs = durationMs;
        this.id = id;
        this.messageData = messageData;
    }

    EventMessage(Parcel in) {
        this.schemeIdUri = in.readString();
        this.value = in.readString();
        this.durationMs = in.readLong();
        this.id = in.readLong();
        this.messageData = in.createByteArray();
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            int result = (this.schemeIdUri != null ? this.schemeIdUri.hashCode() : 0) + 527;
            this.hashCode = (((((((result * 31) + (this.value != null ? this.value.hashCode() : 0)) * 31) + ((int) (this.durationMs ^ (this.durationMs >>> 32)))) * 31) + ((int) (this.id ^ (this.id >>> 32)))) * 31) + Arrays.hashCode(this.messageData);
        }
        return this.hashCode;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        EventMessage other = (EventMessage) obj;
        return this.durationMs == other.durationMs && this.id == other.id && Util.areEqual(this.schemeIdUri, other.schemeIdUri) && Util.areEqual(this.value, other.value) && Arrays.equals(this.messageData, other.messageData);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.schemeIdUri);
        dest.writeString(this.value);
        dest.writeLong(this.durationMs);
        dest.writeLong(this.id);
        dest.writeByteArray(this.messageData);
    }
}
