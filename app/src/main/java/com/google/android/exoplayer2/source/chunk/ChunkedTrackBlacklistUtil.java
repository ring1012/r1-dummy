package com.google.android.exoplayer2.source.chunk;

import android.util.Log;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.HttpDataSource;

/* loaded from: classes.dex */
public final class ChunkedTrackBlacklistUtil {
    public static final long DEFAULT_TRACK_BLACKLIST_MS = 60000;
    private static final String TAG = "ChunkedTrackBlacklist";

    public static boolean maybeBlacklistTrack(TrackSelection trackSelection, int trackSelectionIndex, Exception e) {
        return maybeBlacklistTrack(trackSelection, trackSelectionIndex, e, DEFAULT_TRACK_BLACKLIST_MS);
    }

    public static boolean maybeBlacklistTrack(TrackSelection trackSelection, int trackSelectionIndex, Exception e, long blacklistDurationMs) {
        if (shouldBlacklist(e)) {
            boolean blacklisted = trackSelection.blacklist(trackSelectionIndex, blacklistDurationMs);
            int responseCode = ((HttpDataSource.InvalidResponseCodeException) e).responseCode;
            if (blacklisted) {
                Log.w(TAG, "Blacklisted: duration=" + blacklistDurationMs + ", responseCode=" + responseCode + ", format=" + trackSelection.getFormat(trackSelectionIndex));
                return blacklisted;
            }
            Log.w(TAG, "Blacklisting failed (cannot blacklist last enabled track): responseCode=" + responseCode + ", format=" + trackSelection.getFormat(trackSelectionIndex));
            return blacklisted;
        }
        return false;
    }

    public static boolean shouldBlacklist(Exception e) {
        if (!(e instanceof HttpDataSource.InvalidResponseCodeException)) {
            return false;
        }
        int responseCode = ((HttpDataSource.InvalidResponseCodeException) e).responseCode;
        return responseCode == 404 || responseCode == 410;
    }

    private ChunkedTrackBlacklistUtil() {
    }
}
