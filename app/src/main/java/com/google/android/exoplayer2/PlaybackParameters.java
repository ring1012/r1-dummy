package com.google.android.exoplayer2;

/* loaded from: classes.dex */
public final class PlaybackParameters {
    public static final PlaybackParameters DEFAULT = new PlaybackParameters(1.0f, 1.0f);
    public final float pitch;
    private final int scaledUsPerMs;
    public final float speed;

    public PlaybackParameters(float speed, float pitch) {
        this.speed = speed;
        this.pitch = pitch;
        this.scaledUsPerMs = Math.round(1000.0f * speed);
    }

    public long getSpeedAdjustedDurationUs(long timeMs) {
        return this.scaledUsPerMs * timeMs;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PlaybackParameters other = (PlaybackParameters) obj;
        return this.speed == other.speed && this.pitch == other.pitch;
    }

    public int hashCode() {
        int result = Float.floatToRawIntBits(this.speed) + 527;
        return (result * 31) + Float.floatToRawIntBits(this.pitch);
    }
}
