package com.google.android.exoplayer2.upstream.crypto;

import android.net.Uri;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import java.io.IOException;

/* loaded from: classes.dex */
public final class AesCipherDataSource implements DataSource {
    private AesFlushingCipher cipher;
    private final byte[] secretKey;
    private final DataSource upstream;

    public AesCipherDataSource(byte[] secretKey, DataSource upstream) {
        this.upstream = upstream;
        this.secretKey = secretKey;
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public long open(DataSpec dataSpec) throws IOException {
        long dataLength = this.upstream.open(dataSpec);
        long nonce = CryptoUtil.getFNV64Hash(dataSpec.key);
        this.cipher = new AesFlushingCipher(2, this.secretKey, nonce, dataSpec.absoluteStreamPosition);
        return dataLength;
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public int read(byte[] data, int offset, int readLength) throws IOException {
        if (readLength == 0) {
            return 0;
        }
        int read = this.upstream.read(data, offset, readLength);
        if (read == -1) {
            return -1;
        }
        this.cipher.updateInPlace(data, offset, read);
        return read;
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public void close() throws IOException {
        this.cipher = null;
        this.upstream.close();
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public Uri getUri() {
        return this.upstream.getUri();
    }
}
