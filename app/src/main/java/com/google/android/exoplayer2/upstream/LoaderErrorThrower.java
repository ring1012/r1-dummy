package com.google.android.exoplayer2.upstream;

import java.io.IOException;

/* loaded from: classes.dex */
public interface LoaderErrorThrower {
    void maybeThrowError() throws IOException;

    void maybeThrowError(int i) throws IOException;

    public static final class Dummy implements LoaderErrorThrower {
        @Override // com.google.android.exoplayer2.upstream.LoaderErrorThrower
        public void maybeThrowError() throws IOException {
        }

        @Override // com.google.android.exoplayer2.upstream.LoaderErrorThrower
        public void maybeThrowError(int minRetryCount) throws IOException {
        }
    }
}
