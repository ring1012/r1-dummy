package com.google.android.exoplayer2.source.hls;

import android.net.Uri;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSourceInputStream;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: classes.dex */
final class Aes128DataSource implements DataSource {
    private CipherInputStream cipherInputStream;
    private final byte[] encryptionIv;
    private final byte[] encryptionKey;
    private final DataSource upstream;

    public Aes128DataSource(DataSource upstream, byte[] encryptionKey, byte[] encryptionIv) {
        this.upstream = upstream;
        this.encryptionKey = encryptionKey;
        this.encryptionIv = encryptionIv;
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public long open(DataSpec dataSpec) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, InvalidAlgorithmParameterException {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            Key cipherKey = new SecretKeySpec(this.encryptionKey, "AES");
            AlgorithmParameterSpec cipherIV = new IvParameterSpec(this.encryptionIv);
            try {
                cipher.init(2, cipherKey, cipherIV);
                this.cipherInputStream = new CipherInputStream(new DataSourceInputStream(this.upstream, dataSpec), cipher);
                return -1L;
            } catch (InvalidAlgorithmParameterException | InvalidKeyException e) {
                throw new RuntimeException(e);
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e2) {
            throw new RuntimeException(e2);
        }
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public void close() throws IOException {
        this.cipherInputStream = null;
        this.upstream.close();
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public int read(byte[] buffer, int offset, int readLength) throws IOException {
        Assertions.checkState(this.cipherInputStream != null);
        int bytesRead = this.cipherInputStream.read(buffer, offset, readLength);
        if (bytesRead < 0) {
            return -1;
        }
        return bytesRead;
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public Uri getUri() {
        return this.upstream.getUri();
    }
}
