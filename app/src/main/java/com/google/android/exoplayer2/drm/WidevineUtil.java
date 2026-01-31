package com.google.android.exoplayer2.drm;

import android.util.Pair;
import com.google.android.exoplayer2.C;
import java.util.Map;

/* loaded from: classes.dex */
public final class WidevineUtil {
    public static final String PROPERTY_LICENSE_DURATION_REMAINING = "LicenseDurationRemaining";
    public static final String PROPERTY_PLAYBACK_DURATION_REMAINING = "PlaybackDurationRemaining";

    private WidevineUtil() {
    }

    public static Pair<Long, Long> getLicenseDurationRemainingSec(DrmSession<?> drmSession) {
        Map<String, String> keyStatus = drmSession.queryKeyStatus();
        return new Pair<>(Long.valueOf(getDurationRemainingSec(keyStatus, PROPERTY_LICENSE_DURATION_REMAINING)), Long.valueOf(getDurationRemainingSec(keyStatus, PROPERTY_PLAYBACK_DURATION_REMAINING)));
    }

    private static long getDurationRemainingSec(Map<String, String> keyStatus, String property) {
        if (keyStatus != null) {
            try {
                String value = keyStatus.get(property);
                if (value != null) {
                    return Long.parseLong(value);
                }
            } catch (NumberFormatException e) {
            }
        }
        return C.TIME_UNSET;
    }
}
