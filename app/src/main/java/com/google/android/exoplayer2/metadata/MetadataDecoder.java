package com.google.android.exoplayer2.metadata;

/* loaded from: classes.dex */
public interface MetadataDecoder {
    Metadata decode(MetadataInputBuffer metadataInputBuffer) throws MetadataDecoderException;
}
