package com.baidu.a.a.a.a;

/* loaded from: classes.dex */
public final class b {

    /* renamed from: a, reason: collision with root package name */
    private static final byte[] f33a = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};

    public static String a(byte[] bArr, String str) {
        int i;
        int length = (bArr.length * 4) / 3;
        byte[] bArr2 = new byte[length + (length / 76) + 3];
        int length2 = bArr.length - (bArr.length % 3);
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < length2; i4 += 3) {
            int i5 = i3 + 1;
            bArr2[i3] = f33a[(bArr[i4] & 255) >> 2];
            int i6 = i5 + 1;
            bArr2[i5] = f33a[((bArr[i4] & 3) << 4) | ((bArr[i4 + 1] & 255) >> 4)];
            int i7 = i6 + 1;
            bArr2[i6] = f33a[((bArr[i4 + 1] & 15) << 2) | ((bArr[i4 + 2] & 255) >> 6)];
            int i8 = i7 + 1;
            bArr2[i7] = f33a[bArr[i4 + 2] & 63];
            if ((i8 - i2) % 76 != 0 || i8 == 0) {
                i3 = i8;
            } else {
                i3 = i8 + 1;
                bArr2[i8] = 10;
                i2++;
            }
        }
        switch (bArr.length % 3) {
            case 1:
                int i9 = i3 + 1;
                bArr2[i3] = f33a[(bArr[length2] & 255) >> 2];
                int i10 = i9 + 1;
                bArr2[i9] = f33a[(bArr[length2] & 3) << 4];
                int i11 = i10 + 1;
                bArr2[i10] = 61;
                i = i11 + 1;
                bArr2[i11] = 61;
                break;
            case 2:
                int i12 = i3 + 1;
                bArr2[i3] = f33a[(bArr[length2] & 255) >> 2];
                int i13 = i12 + 1;
                bArr2[i12] = f33a[((bArr[length2] & 3) << 4) | ((bArr[length2 + 1] & 255) >> 4)];
                int i14 = i13 + 1;
                bArr2[i13] = f33a[(bArr[length2 + 1] & 15) << 2];
                i = i14 + 1;
                bArr2[i14] = 61;
                break;
            default:
                i = i3;
                break;
        }
        return new String(bArr2, 0, i, str);
    }

    public static byte[] a(byte[] bArr) {
        return a(bArr, bArr.length);
    }

    public static byte[] a(byte[] bArr, int i) {
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7 = (i / 4) * 3;
        if (i7 == 0) {
            return new byte[0];
        }
        byte[] bArr2 = new byte[i7];
        int i8 = 0;
        while (true) {
            byte b = bArr[i - 1];
            if (b != 10 && b != 13 && b != 32 && b != 9) {
                if (b != 61) {
                    break;
                }
                i8++;
            }
            i--;
        }
        int i9 = 0;
        int i10 = 0;
        int i11 = 0;
        int i12 = 0;
        while (i9 < i) {
            byte b2 = bArr[i9];
            if (b2 == 10 || b2 == 13 || b2 == 32) {
                i2 = i10;
                i3 = i12;
                i4 = i11;
            } else if (b2 == 9) {
                i2 = i10;
                i3 = i12;
                i4 = i11;
            } else {
                if (b2 >= 65 && b2 <= 90) {
                    i5 = b2 - 65;
                } else if (b2 >= 97 && b2 <= 122) {
                    i5 = b2 - 71;
                } else if (b2 >= 48 && b2 <= 57) {
                    i5 = b2 + 4;
                } else if (b2 == 43) {
                    i5 = 62;
                } else {
                    if (b2 != 47) {
                        return null;
                    }
                    i5 = 63;
                }
                int i13 = (i10 << 6) | ((byte) i5);
                if (i11 % 4 == 3) {
                    int i14 = i12 + 1;
                    bArr2[i12] = (byte) ((16711680 & i13) >> 16);
                    int i15 = i14 + 1;
                    bArr2[i14] = (byte) ((65280 & i13) >> 8);
                    i6 = i15 + 1;
                    bArr2[i15] = (byte) (i13 & 255);
                } else {
                    i6 = i12;
                }
                i4 = i11 + 1;
                i3 = i6;
                i2 = i13;
            }
            i9++;
            i11 = i4;
            i12 = i3;
            i10 = i2;
        }
        if (i8 > 0) {
            int i16 = i10 << (i8 * 6);
            int i17 = i12 + 1;
            bArr2[i12] = (byte) ((16711680 & i16) >> 16);
            if (i8 == 1) {
                i12 = i17 + 1;
                bArr2[i17] = (byte) ((65280 & i16) >> 8);
            } else {
                i12 = i17;
            }
        }
        byte[] bArr3 = new byte[i12];
        System.arraycopy(bArr2, 0, bArr3, 0, i12);
        return bArr3;
    }
}
