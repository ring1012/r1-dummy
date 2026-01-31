package com.google.android.exoplayer2.upstream;

/* loaded from: classes.dex */
public interface TransferListener<S> {
    void onBytesTransferred(S s, int i);

    void onTransferEnd(S s);

    void onTransferStart(S s, DataSpec dataSpec);
}
