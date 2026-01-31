package com.google.android.exoplayer2.upstream.crypto;

import com.google.android.exoplayer2.util.Assertions;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/* loaded from: classes.dex */
public final class AesFlushingCipher {
    private final int blockSize;
    private final Cipher cipher;
    private final byte[] flushedBlock;
    private int pendingXorBytes;
    private final byte[] zerosBlock;

    public AesFlushingCipher(int mode, byte[] secretKey, long nonce, long offset) throws InvalidKeyException, InvalidAlgorithmParameterException {
        try {
            this.cipher = Cipher.getInstance("AES/CTR/NoPadding");
            this.blockSize = this.cipher.getBlockSize();
            this.zerosBlock = new byte[this.blockSize];
            this.flushedBlock = new byte[this.blockSize];
            long counter = offset / this.blockSize;
            int startPadding = (int) (offset % this.blockSize);
            this.cipher.init(mode, new SecretKeySpec(secretKey, this.cipher.getAlgorithm().split(MqttTopic.TOPIC_LEVEL_SEPARATOR)[0]), new IvParameterSpec(getInitializationVector(nonce, counter)));
            if (startPadding != 0) {
                updateInPlace(new byte[startPadding], 0, startPadding);
            }
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateInPlace(byte[] data, int offset, int length) {
        update(data, offset, length, data, offset);
    }

    public void update(byte[] in, int inOffset, int length, byte[] out, int outOffset) {
        while (this.pendingXorBytes > 0) {
            out[outOffset] = (byte) (in[inOffset] ^ this.flushedBlock[this.blockSize - this.pendingXorBytes]);
            outOffset++;
            inOffset++;
            this.pendingXorBytes--;
            length--;
            if (length == 0) {
                return;
            }
        }
        int written = nonFlushingUpdate(in, inOffset, length, out, outOffset);
        if (length != written) {
            int bytesToFlush = length - written;
            Assertions.checkState(bytesToFlush < this.blockSize);
            int outOffset2 = outOffset + written;
            this.pendingXorBytes = this.blockSize - bytesToFlush;
            Assertions.checkState(nonFlushingUpdate(this.zerosBlock, 0, this.pendingXorBytes, this.flushedBlock, 0) == this.blockSize);
            int i = 0;
            int outOffset3 = outOffset2;
            while (i < bytesToFlush) {
                out[outOffset3] = this.flushedBlock[i];
                i++;
                outOffset3++;
            }
        }
    }

    private int nonFlushingUpdate(byte[] in, int inOffset, int length, byte[] out, int outOffset) {
        try {
            return this.cipher.update(in, inOffset, length, out, outOffset);
        } catch (ShortBufferException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] getInitializationVector(long nonce, long counter) {
        return ByteBuffer.allocate(16).putLong(nonce).putLong(counter).array();
    }
}
