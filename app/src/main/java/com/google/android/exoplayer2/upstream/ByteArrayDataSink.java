package com.google.android.exoplayer2.upstream;

import com.google.android.exoplayer2.util.Assertions;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/* loaded from: classes.dex */
public final class ByteArrayDataSink implements DataSink {
    private ByteArrayOutputStream stream;

    @Override // com.google.android.exoplayer2.upstream.DataSink
    public void open(DataSpec dataSpec) throws IOException {
        if (dataSpec.length == -1) {
            this.stream = new ByteArrayOutputStream();
        } else {
            Assertions.checkArgument(dataSpec.length <= 2147483647L);
            this.stream = new ByteArrayOutputStream((int) dataSpec.length);
        }
    }

    @Override // com.google.android.exoplayer2.upstream.DataSink
    public void close() throws IOException {
        this.stream.close();
    }

    @Override // com.google.android.exoplayer2.upstream.DataSink
    public void write(byte[] buffer, int offset, int length) throws IOException {
        this.stream.write(buffer, offset, length);
    }

    public byte[] getData() {
        if (this.stream == null) {
            return null;
        }
        return this.stream.toByteArray();
    }
}
