package cn.kuwo.autosdk;

/* loaded from: classes.dex */
public class o {
    public static int a(byte[] bArr, boolean z) {
        return (int) a(bArr, 4, z);
    }

    public static long a(byte[] bArr, int i, boolean z) {
        long j;
        if (bArr == null || bArr.length == 0) {
            throw new IllegalArgumentException("byte array is null or empty!");
        }
        int iMin = Math.min(i, bArr.length);
        if (z) {
            j = 0;
            int i2 = 0;
            while (i2 < iMin) {
                long j2 = (bArr[i2] & 255) | (j << 8);
                i2++;
                j = j2;
            }
        } else {
            j = 0;
            int i3 = iMin - 1;
            while (i3 >= 0) {
                long j3 = (bArr[i3] & 255) | (j << 8);
                i3--;
                j = j3;
            }
        }
        return j;
    }
}
