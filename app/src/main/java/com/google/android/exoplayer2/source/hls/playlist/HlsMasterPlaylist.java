package com.google.android.exoplayer2.source.hls.playlist;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.util.MimeTypes;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public final class HlsMasterPlaylist extends HlsPlaylist {
    public final List<HlsUrl> audios;
    public final Format muxedAudioFormat;
    public final List<Format> muxedCaptionFormats;
    public final List<HlsUrl> subtitles;
    public final List<HlsUrl> variants;

    public static final class HlsUrl {
        public final Format format;
        public final String url;

        public static HlsUrl createMediaPlaylistHlsUrl(String baseUri) {
            Format format = Format.createContainerFormat("0", MimeTypes.APPLICATION_M3U8, null, null, -1, 0, null);
            return new HlsUrl(baseUri, format);
        }

        public HlsUrl(String url, Format format) {
            this.url = url;
            this.format = format;
        }
    }

    public HlsMasterPlaylist(String baseUri, List<HlsUrl> variants, List<HlsUrl> audios, List<HlsUrl> subtitles, Format muxedAudioFormat, List<Format> muxedCaptionFormats) {
        super(baseUri);
        this.variants = Collections.unmodifiableList(variants);
        this.audios = Collections.unmodifiableList(audios);
        this.subtitles = Collections.unmodifiableList(subtitles);
        this.muxedAudioFormat = muxedAudioFormat;
        this.muxedCaptionFormats = Collections.unmodifiableList(muxedCaptionFormats);
    }

    public static HlsMasterPlaylist createSingleVariantMasterPlaylist(String variantUri) {
        List<HlsUrl> variant = Collections.singletonList(HlsUrl.createMediaPlaylistHlsUrl(variantUri));
        List<HlsUrl> emptyList = Collections.emptyList();
        return new HlsMasterPlaylist(null, variant, emptyList, emptyList, null, Collections.emptyList());
    }
}
