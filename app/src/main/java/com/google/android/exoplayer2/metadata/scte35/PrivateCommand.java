package com.google.android.exoplayer2.metadata.scte35;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.exoplayer2.util.ParsableByteArray;

/* loaded from: classes.dex */
public final class PrivateCommand extends SpliceCommand {
    public static final Parcelable.Creator<PrivateCommand> CREATOR = new Parcelable.Creator<PrivateCommand>() { // from class: com.google.android.exoplayer2.metadata.scte35.PrivateCommand.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PrivateCommand createFromParcel(Parcel in) {
            return new PrivateCommand(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PrivateCommand[] newArray(int size) {
            return new PrivateCommand[size];
        }
    };
    public final byte[] commandBytes;
    public final long identifier;
    public final long ptsAdjustment;

    private PrivateCommand(long identifier, byte[] commandBytes, long ptsAdjustment) {
        this.ptsAdjustment = ptsAdjustment;
        this.identifier = identifier;
        this.commandBytes = commandBytes;
    }

    private PrivateCommand(Parcel in) {
        this.ptsAdjustment = in.readLong();
        this.identifier = in.readLong();
        this.commandBytes = new byte[in.readInt()];
        in.readByteArray(this.commandBytes);
    }

    static PrivateCommand parseFromSection(ParsableByteArray sectionData, int commandLength, long ptsAdjustment) {
        long identifier = sectionData.readUnsignedInt();
        byte[] privateBytes = new byte[commandLength - 4];
        sectionData.readBytes(privateBytes, 0, privateBytes.length);
        return new PrivateCommand(identifier, privateBytes, ptsAdjustment);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.ptsAdjustment);
        dest.writeLong(this.identifier);
        dest.writeInt(this.commandBytes.length);
        dest.writeByteArray(this.commandBytes);
    }
}
