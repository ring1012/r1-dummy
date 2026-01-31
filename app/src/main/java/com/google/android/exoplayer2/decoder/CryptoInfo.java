package com.google.android.exoplayer2.decoder;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import com.google.android.exoplayer2.util.Util;

/* loaded from: classes.dex */
public final class CryptoInfo {
    private final MediaCodec.CryptoInfo frameworkCryptoInfo;
    public byte[] iv;
    public byte[] key;
    public int mode;
    public int[] numBytesOfClearData;
    public int[] numBytesOfEncryptedData;
    public int numSubSamples;
    public int patternBlocksToEncrypt;
    public int patternBlocksToSkip;
    private final PatternHolderV24 patternHolder;

    public CryptoInfo() {
        byte b = 0;
        this.frameworkCryptoInfo = Util.SDK_INT >= 16 ? newFrameworkCryptoInfoV16() : null;
        this.patternHolder = Util.SDK_INT >= 24 ? new PatternHolderV24(this.frameworkCryptoInfo) : null;
    }

    public void set(int numSubSamples, int[] numBytesOfClearData, int[] numBytesOfEncryptedData, byte[] key, byte[] iv, int mode) {
        this.numSubSamples = numSubSamples;
        this.numBytesOfClearData = numBytesOfClearData;
        this.numBytesOfEncryptedData = numBytesOfEncryptedData;
        this.key = key;
        this.iv = iv;
        this.mode = mode;
        this.patternBlocksToEncrypt = 0;
        this.patternBlocksToSkip = 0;
        if (Util.SDK_INT >= 16) {
            updateFrameworkCryptoInfoV16();
        }
    }

    public void setPattern(int patternBlocksToEncrypt, int patternBlocksToSkip) {
        this.patternBlocksToEncrypt = patternBlocksToEncrypt;
        this.patternBlocksToSkip = patternBlocksToSkip;
        if (Util.SDK_INT < 24) {
            return;
        }
        this.patternHolder.set(patternBlocksToEncrypt, patternBlocksToSkip);
    }

    @TargetApi(16)
    public MediaCodec.CryptoInfo getFrameworkCryptoInfoV16() {
        return this.frameworkCryptoInfo;
    }

    @TargetApi(16)
    private MediaCodec.CryptoInfo newFrameworkCryptoInfoV16() {
        return new MediaCodec.CryptoInfo();
    }

    @TargetApi(16)
    private void updateFrameworkCryptoInfoV16() {
        this.frameworkCryptoInfo.numSubSamples = this.numSubSamples;
        this.frameworkCryptoInfo.numBytesOfClearData = this.numBytesOfClearData;
        this.frameworkCryptoInfo.numBytesOfEncryptedData = this.numBytesOfEncryptedData;
        this.frameworkCryptoInfo.key = this.key;
        this.frameworkCryptoInfo.iv = this.iv;
        this.frameworkCryptoInfo.mode = this.mode;
        if (Util.SDK_INT < 24) {
            return;
        }
        this.patternHolder.set(this.patternBlocksToEncrypt, this.patternBlocksToSkip);
    }

    @TargetApi(24)
    private static final class PatternHolderV24 {
        private final MediaCodec.CryptoInfo frameworkCryptoInfo;
        private final MediaCodec.CryptoInfo.Pattern pattern;

        private PatternHolderV24(MediaCodec.CryptoInfo frameworkCryptoInfo) {
            this.frameworkCryptoInfo = frameworkCryptoInfo;
            this.pattern = new MediaCodec.CryptoInfo.Pattern(0, 0);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void set(int blocksToEncrypt, int blocksToSkip) {
            this.pattern.set(blocksToEncrypt, blocksToSkip);
            this.frameworkCryptoInfo.setPattern(this.pattern);
        }
    }
}
