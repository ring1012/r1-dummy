package com.google.android.exoplayer2.source.hls;

import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist;

/* loaded from: classes.dex */
public final class HlsManifest {
    public final HlsMasterPlaylist masterPlaylist;
    public final HlsMediaPlaylist mediaPlaylist;

    HlsManifest(HlsMasterPlaylist masterPlaylist, HlsMediaPlaylist mediaPlaylist) {
        this.masterPlaylist = masterPlaylist;
        this.mediaPlaylist = mediaPlaylist;
    }
}
