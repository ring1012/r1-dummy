package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.upstream.Allocator;
import java.io.IOException;

/* loaded from: classes.dex */
public interface MediaSource {

    public interface Listener {
        void onSourceInfoRefreshed(Timeline timeline, Object obj);
    }

    MediaPeriod createPeriod(int i, Allocator allocator, long j);

    void maybeThrowSourceInfoRefreshError() throws IOException;

    void prepareSource(ExoPlayer exoPlayer, boolean z, Listener listener);

    void releasePeriod(MediaPeriod mediaPeriod);

    void releaseSource();
}
