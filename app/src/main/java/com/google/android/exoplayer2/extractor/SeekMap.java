package com.google.android.exoplayer2.extractor;

/* loaded from: classes.dex */
public interface SeekMap {
    long getDurationUs();

    long getPosition(long j);

    boolean isSeekable();

    public static final class Unseekable implements SeekMap {
        private final long durationUs;

        public Unseekable(long durationUs) {
            this.durationUs = durationUs;
        }

        @Override // com.google.android.exoplayer2.extractor.SeekMap
        public boolean isSeekable() {
            return false;
        }

        @Override // com.google.android.exoplayer2.extractor.SeekMap
        public long getDurationUs() {
            return this.durationUs;
        }

        @Override // com.google.android.exoplayer2.extractor.SeekMap
        public long getPosition(long timeUs) {
            return 0L;
        }
    }
}
