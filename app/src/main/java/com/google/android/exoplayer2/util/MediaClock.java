package com.google.android.exoplayer2.util;

import com.google.android.exoplayer2.PlaybackParameters;

/* loaded from: classes.dex */
public interface MediaClock {
    PlaybackParameters getPlaybackParameters();

    long getPositionUs();

    PlaybackParameters setPlaybackParameters(PlaybackParameters playbackParameters);
}
