package com.google.android.exoplayer2.metadata.scte35;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;

/* loaded from: classes.dex */
public final class TimeSignalCommand extends SpliceCommand {
    public static final Parcelable.Creator<TimeSignalCommand> CREATOR = new Parcelable.Creator<TimeSignalCommand>() { // from class: com.google.android.exoplayer2.metadata.scte35.TimeSignalCommand.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TimeSignalCommand createFromParcel(Parcel in) {
            return new TimeSignalCommand(in.readLong(), in.readLong());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TimeSignalCommand[] newArray(int size) {
            return new TimeSignalCommand[size];
        }
    };
    public final long playbackPositionUs;
    public final long ptsTime;

    private TimeSignalCommand(long ptsTime, long playbackPositionUs) {
        this.ptsTime = ptsTime;
        this.playbackPositionUs = playbackPositionUs;
    }

    static TimeSignalCommand parseFromSection(ParsableByteArray sectionData, long ptsAdjustment, TimestampAdjuster timestampAdjuster) {
        long ptsTime = parseSpliceTime(sectionData, ptsAdjustment);
        long playbackPositionUs = timestampAdjuster.adjustTsTimestamp(ptsTime);
        return new TimeSignalCommand(ptsTime, playbackPositionUs);
    }

    static long parseSpliceTime(ParsableByteArray sectionData, long ptsAdjustment) {
        long firstByte = sectionData.readUnsignedByte();
        if ((128 & firstByte) == 0) {
            return C.TIME_UNSET;
        }
        long ptsTime = ((1 & firstByte) << 32) | sectionData.readUnsignedInt();
        return (ptsTime + ptsAdjustment) & 8589934591L;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.ptsTime);
        dest.writeLong(this.playbackPositionUs);
    }
}
