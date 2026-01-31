package com.unisound.passport.a;

import java.util.UUID;

/* loaded from: classes.dex */
public class c {

    /* renamed from: a, reason: collision with root package name */
    private static final char[] f281a = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_".toCharArray();

    public static String a() {
        UUID uuidRandomUUID = UUID.randomUUID();
        return a(uuidRandomUUID.getMostSignificantBits()) + a(uuidRandomUUID.getLeastSignificantBits());
    }

    private static String a(long j) {
        char[] charArray = "00000000000".toCharArray();
        int i = 11;
        do {
            i--;
            charArray[i] = f281a[(int) (j & 63)];
            j >>>= 6;
        } while (j != 0);
        return new String(charArray);
    }
}
