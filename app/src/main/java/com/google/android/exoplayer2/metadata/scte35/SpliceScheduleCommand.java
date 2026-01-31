package com.google.android.exoplayer2.metadata.scte35;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public final class SpliceScheduleCommand extends SpliceCommand {
    public static final Parcelable.Creator<SpliceScheduleCommand> CREATOR = new Parcelable.Creator<SpliceScheduleCommand>() { // from class: com.google.android.exoplayer2.metadata.scte35.SpliceScheduleCommand.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SpliceScheduleCommand createFromParcel(Parcel in) {
            return new SpliceScheduleCommand(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SpliceScheduleCommand[] newArray(int size) {
            return new SpliceScheduleCommand[size];
        }
    };
    public final List<Event> events;

    public static final class Event {
        public final boolean autoReturn;
        public final int availNum;
        public final int availsExpected;
        public final long breakDuration;
        public final List<ComponentSplice> componentSpliceList;
        public final boolean outOfNetworkIndicator;
        public final boolean programSpliceFlag;
        public final boolean spliceEventCancelIndicator;
        public final long spliceEventId;
        public final int uniqueProgramId;
        public final long utcSpliceTime;

        private Event(long spliceEventId, boolean spliceEventCancelIndicator, boolean outOfNetworkIndicator, boolean programSpliceFlag, List<ComponentSplice> componentSpliceList, long utcSpliceTime, boolean autoReturn, long breakDuration, int uniqueProgramId, int availNum, int availsExpected) {
            this.spliceEventId = spliceEventId;
            this.spliceEventCancelIndicator = spliceEventCancelIndicator;
            this.outOfNetworkIndicator = outOfNetworkIndicator;
            this.programSpliceFlag = programSpliceFlag;
            this.componentSpliceList = Collections.unmodifiableList(componentSpliceList);
            this.utcSpliceTime = utcSpliceTime;
            this.autoReturn = autoReturn;
            this.breakDuration = breakDuration;
            this.uniqueProgramId = uniqueProgramId;
            this.availNum = availNum;
            this.availsExpected = availsExpected;
        }

        private Event(Parcel in) {
            this.spliceEventId = in.readLong();
            this.spliceEventCancelIndicator = in.readByte() == 1;
            this.outOfNetworkIndicator = in.readByte() == 1;
            this.programSpliceFlag = in.readByte() == 1;
            int componentSpliceListLength = in.readInt();
            ArrayList<ComponentSplice> componentSpliceList = new ArrayList<>(componentSpliceListLength);
            for (int i = 0; i < componentSpliceListLength; i++) {
                componentSpliceList.add(ComponentSplice.createFromParcel(in));
            }
            this.componentSpliceList = Collections.unmodifiableList(componentSpliceList);
            this.utcSpliceTime = in.readLong();
            this.autoReturn = in.readByte() == 1;
            this.breakDuration = in.readLong();
            this.uniqueProgramId = in.readInt();
            this.availNum = in.readInt();
            this.availsExpected = in.readInt();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static Event parseFromSection(ParsableByteArray sectionData) {
            long spliceEventId = sectionData.readUnsignedInt();
            boolean spliceEventCancelIndicator = (sectionData.readUnsignedByte() & 128) != 0;
            boolean outOfNetworkIndicator = false;
            boolean programSpliceFlag = false;
            long utcSpliceTime = C.TIME_UNSET;
            ArrayList<ComponentSplice> componentSplices = new ArrayList<>();
            int uniqueProgramId = 0;
            int availNum = 0;
            int availsExpected = 0;
            boolean autoReturn = false;
            long duration = C.TIME_UNSET;
            if (!spliceEventCancelIndicator) {
                int headerByte = sectionData.readUnsignedByte();
                outOfNetworkIndicator = (headerByte & 128) != 0;
                programSpliceFlag = (headerByte & 64) != 0;
                boolean durationFlag = (headerByte & 32) != 0;
                if (programSpliceFlag) {
                    utcSpliceTime = sectionData.readUnsignedInt();
                }
                if (!programSpliceFlag) {
                    int componentCount = sectionData.readUnsignedByte();
                    componentSplices = new ArrayList<>(componentCount);
                    for (int i = 0; i < componentCount; i++) {
                        int componentTag = sectionData.readUnsignedByte();
                        long componentUtcSpliceTime = sectionData.readUnsignedInt();
                        componentSplices.add(new ComponentSplice(componentTag, componentUtcSpliceTime));
                    }
                }
                if (durationFlag) {
                    long firstByte = sectionData.readUnsignedByte();
                    autoReturn = (128 & firstByte) != 0;
                    duration = ((1 & firstByte) << 32) | sectionData.readUnsignedInt();
                }
                uniqueProgramId = sectionData.readUnsignedShort();
                availNum = sectionData.readUnsignedByte();
                availsExpected = sectionData.readUnsignedByte();
            }
            return new Event(spliceEventId, spliceEventCancelIndicator, outOfNetworkIndicator, programSpliceFlag, componentSplices, utcSpliceTime, autoReturn, duration, uniqueProgramId, availNum, availsExpected);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void writeToParcel(Parcel dest) {
            dest.writeLong(this.spliceEventId);
            dest.writeByte((byte) (this.spliceEventCancelIndicator ? 1 : 0));
            dest.writeByte((byte) (this.outOfNetworkIndicator ? 1 : 0));
            dest.writeByte((byte) (this.programSpliceFlag ? 1 : 0));
            int componentSpliceListSize = this.componentSpliceList.size();
            dest.writeInt(componentSpliceListSize);
            for (int i = 0; i < componentSpliceListSize; i++) {
                this.componentSpliceList.get(i).writeToParcel(dest);
            }
            dest.writeLong(this.utcSpliceTime);
            dest.writeByte((byte) (this.autoReturn ? 1 : 0));
            dest.writeLong(this.breakDuration);
            dest.writeInt(this.uniqueProgramId);
            dest.writeInt(this.availNum);
            dest.writeInt(this.availsExpected);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static Event createFromParcel(Parcel in) {
            return new Event(in);
        }
    }

    public static final class ComponentSplice {
        public final int componentTag;
        public final long utcSpliceTime;

        private ComponentSplice(int componentTag, long utcSpliceTime) {
            this.componentTag = componentTag;
            this.utcSpliceTime = utcSpliceTime;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static ComponentSplice createFromParcel(Parcel in) {
            return new ComponentSplice(in.readInt(), in.readLong());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void writeToParcel(Parcel dest) {
            dest.writeInt(this.componentTag);
            dest.writeLong(this.utcSpliceTime);
        }
    }

    private SpliceScheduleCommand(List<Event> events) {
        this.events = Collections.unmodifiableList(events);
    }

    private SpliceScheduleCommand(Parcel in) {
        int eventsSize = in.readInt();
        ArrayList<Event> events = new ArrayList<>(eventsSize);
        for (int i = 0; i < eventsSize; i++) {
            events.add(Event.createFromParcel(in));
        }
        this.events = Collections.unmodifiableList(events);
    }

    static SpliceScheduleCommand parseFromSection(ParsableByteArray sectionData) {
        int spliceCount = sectionData.readUnsignedByte();
        ArrayList<Event> events = new ArrayList<>(spliceCount);
        for (int i = 0; i < spliceCount; i++) {
            events.add(Event.parseFromSection(sectionData));
        }
        return new SpliceScheduleCommand(events);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        int eventsSize = this.events.size();
        dest.writeInt(eventsSize);
        for (int i = 0; i < eventsSize; i++) {
            this.events.get(i).writeToParcel(dest);
        }
    }
}
