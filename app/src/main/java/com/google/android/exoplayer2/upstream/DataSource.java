package com.google.android.exoplayer2.upstream;

import android.net.Uri;
import java.io.IOException;

/* loaded from: classes.dex */
public interface DataSource {

    public interface Factory {
        DataSource createDataSource();
    }

    void close() throws IOException;

    Uri getUri();

    long open(DataSpec dataSpec) throws IOException;

    int read(byte[] bArr, int i, int i2) throws IOException;
}
