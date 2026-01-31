package com.google.android.exoplayer2.upstream.crypto;

/* loaded from: classes.dex */
final class CryptoUtil {
    private CryptoUtil() {
    }

    public static long getFNV64Hash(String input) {
        if (input == null) {
            return 0L;
        }
        long hash = 0;
        for (int i = 0; i < input.length(); i++) {
            long hash2 = hash ^ input.charAt(i);
            hash = hash2 + (hash2 << 1) + (hash2 << 4) + (hash2 << 5) + (hash2 << 7) + (hash2 << 8) + (hash2 << 40);
        }
        return hash;
    }
}
