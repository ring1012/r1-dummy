package cn.kuwo.autosdk;

import org.apache.commons.io.IOUtils;

/* loaded from: classes.dex */
public final class m {

    /* renamed from: a, reason: collision with root package name */
    private static char[] f16a = new char[64];
    private static byte[] b;

    static {
        char c = 'A';
        int i = 0;
        while (c <= 'Z') {
            f16a[i] = c;
            c = (char) (c + 1);
            i++;
        }
        char c2 = 'a';
        while (c2 <= 'z') {
            f16a[i] = c2;
            c2 = (char) (c2 + 1);
            i++;
        }
        char c3 = '0';
        while (c3 <= '9') {
            f16a[i] = c3;
            c3 = (char) (c3 + 1);
            i++;
        }
        int i2 = i + 1;
        f16a[i] = '+';
        int i3 = i2 + 1;
        f16a[i2] = IOUtils.DIR_SEPARATOR_UNIX;
        b = new byte[128];
        for (int i4 = 0; i4 < b.length; i4++) {
            b[i4] = -1;
        }
        for (int i5 = 0; i5 < 64; i5++) {
            b[f16a[i5]] = (byte) i5;
        }
    }

    public static char[] a(byte[] bArr, int i) {
        return a(bArr, i, null);
    }

    public static char[] a(byte[] bArr, int i, String str) {
        int i2;
        int i3;
        if (str != null && !str.equals("")) {
            byte[] bytes = str.getBytes();
            int i4 = 0;
            while (i4 < bArr.length) {
                int i5 = 0;
                while (i5 < bytes.length && i4 < bArr.length) {
                    bArr[i4] = (byte) (bArr[i4] ^ bytes[i5]);
                    i5++;
                    i4++;
                }
            }
        }
        int i6 = ((i * 4) + 2) / 3;
        char[] cArr = new char[((i + 2) / 3) * 4];
        int i7 = 0;
        int i8 = 0;
        while (i8 < i) {
            int i9 = i8 + 1;
            int i10 = bArr[i8] & 255;
            if (i9 < i) {
                i2 = bArr[i9] & 255;
                i9++;
            } else {
                i2 = 0;
            }
            if (i9 < i) {
                i8 = i9 + 1;
                i3 = bArr[i9] & 255;
            } else {
                i8 = i9;
                i3 = 0;
            }
            int i11 = i10 >>> 2;
            int i12 = ((i10 & 3) << 4) | (i2 >>> 4);
            int i13 = ((i2 & 15) << 2) | (i3 >>> 6);
            int i14 = i3 & 63;
            int i15 = i7 + 1;
            cArr[i7] = f16a[i11];
            int i16 = i15 + 1;
            cArr[i15] = f16a[i12];
            cArr[i16] = i16 < i6 ? f16a[i13] : '=';
            int i17 = i16 + 1;
            cArr[i17] = i17 < i6 ? f16a[i14] : '=';
            i7 = i17 + 1;
        }
        return cArr;
    }
}
