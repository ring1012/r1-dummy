package org.eclipse.paho.client.mqttv3.a.a;

import org.eclipse.paho.client.mqttv3.a.b.u;

/* loaded from: classes.dex */
public class b {

    /* renamed from: a, reason: collision with root package name */
    private static final String f470a = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final char[] b = f470a.toCharArray();

    private static final long a(byte[] bArr, int i, int i2) {
        int i3 = 0;
        long j = 0;
        while (i2 > 0) {
            i2--;
            int i4 = i + 1;
            byte b2 = bArr[i];
            long j2 = b2 == 47 ? 1L : 0L;
            if (b2 >= 48 && b2 <= 57) {
                j2 = (b2 + 2) - 48;
            }
            if (b2 >= 65 && b2 <= 90) {
                j2 = (b2 + u.m) - 65;
            }
            if (b2 >= 97 && b2 <= 122) {
                j2 = (b2 + 38) - 97;
            }
            j += j2 << i3;
            i3 += 6;
            i = i4;
        }
        return j;
    }

    private static final String a(long j, int i) {
        StringBuffer stringBuffer = new StringBuffer(i);
        while (i > 0) {
            i--;
            stringBuffer.append(b[(int) (63 & j)]);
            j >>= 6;
        }
        return stringBuffer.toString();
    }

    public static String a(byte[] bArr) {
        int length = bArr.length;
        StringBuffer stringBuffer = new StringBuffer(((length + 2) / 3) * 4);
        int i = 0;
        while (length >= 3) {
            stringBuffer.append(a(((bArr[i] & 255) << 16) | ((bArr[i + 1] & 255) << 8) | (bArr[i + 2] & 255), 4));
            i += 3;
            length -= 3;
        }
        if (length == 2) {
            stringBuffer.append(a(((bArr[i] & 255) << 8) | (bArr[i + 1] & 255), 3));
        }
        if (length == 1) {
            stringBuffer.append(a(bArr[i] & 255, 2));
        }
        return stringBuffer.toString();
    }

    public static byte[] a(String str) {
        int i;
        byte[] bytes = str.getBytes();
        int length = bytes.length;
        byte[] bArr = new byte[(length * 3) / 4];
        int i2 = 0;
        int i3 = length;
        int i4 = 0;
        while (true) {
            i = i2;
            if (i3 < 4) {
                break;
            }
            long jA = a(bytes, i4, 4);
            i3 -= 4;
            i4 += 4;
            for (int i5 = 2; i5 >= 0; i5--) {
                bArr[i + i5] = (byte) (255 & jA);
                jA >>= 8;
            }
            i2 = i + 3;
        }
        if (i3 == 3) {
            long jA2 = a(bytes, i4, 3);
            for (int i6 = 1; i6 >= 0; i6--) {
                bArr[i + i6] = (byte) (255 & jA2);
                jA2 >>= 8;
            }
        }
        if (i3 == 2) {
            bArr[i] = (byte) (a(bytes, i4, 2) & 255);
        }
        return bArr;
    }
}
