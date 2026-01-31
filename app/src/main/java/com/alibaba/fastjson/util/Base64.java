package com.alibaba.fastjson.util;

import java.util.Arrays;

/* loaded from: classes.dex */
public class Base64 {
    public static final char[] CA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
    public static final int[] IA = new int[256];

    static {
        Arrays.fill(IA, -1);
        int iS = CA.length;
        for (int i = 0; i < iS; i++) {
            IA[CA[i]] = i;
        }
        IA[61] = 0;
    }

    public static final byte[] decodeFast(char[] chars, int offset, int charsLen) {
        int pad;
        int sepCnt;
        int sIx;
        if (charsLen == 0) {
            return new byte[0];
        }
        int sIx2 = offset;
        int eIx = (offset + charsLen) - 1;
        while (sIx2 < eIx && IA[chars[sIx2]] < 0) {
            sIx2++;
        }
        while (eIx > 0 && IA[chars[eIx]] < 0) {
            eIx--;
        }
        if (chars[eIx] == '=') {
            pad = chars[eIx + (-1)] == '=' ? 2 : 1;
        } else {
            pad = 0;
        }
        int cCnt = (eIx - sIx2) + 1;
        if (charsLen > 76) {
            sepCnt = (chars[76] == '\r' ? cCnt / 78 : 0) << 1;
        } else {
            sepCnt = 0;
        }
        int len = (((cCnt - sepCnt) * 6) >> 3) - pad;
        byte[] bytes = new byte[len];
        int cc = 0;
        int eLen = (len / 3) * 3;
        int d = 0;
        int sIx3 = sIx2;
        while (d < eLen) {
            int sIx4 = sIx3 + 1;
            int i = IA[chars[sIx3]] << 18;
            int sIx5 = sIx4 + 1;
            int i2 = i | (IA[chars[sIx4]] << 12);
            int sIx6 = sIx5 + 1;
            int i3 = i2 | (IA[chars[sIx5]] << 6);
            int sIx7 = sIx6 + 1;
            int i4 = i3 | IA[chars[sIx6]];
            int d2 = d + 1;
            bytes[d] = (byte) (i4 >> 16);
            int d3 = d2 + 1;
            bytes[d2] = (byte) (i4 >> 8);
            int d4 = d3 + 1;
            bytes[d3] = (byte) i4;
            if (sepCnt <= 0 || (cc = cc + 1) != 19) {
                sIx = sIx7;
            } else {
                sIx = sIx7 + 2;
                cc = 0;
            }
            d = d4;
            sIx3 = sIx;
        }
        if (d < len) {
            int i5 = 0;
            int j = 0;
            while (sIx3 <= eIx - pad) {
                i5 |= IA[chars[sIx3]] << (18 - (j * 6));
                j++;
                sIx3++;
            }
            int r = 16;
            while (d < len) {
                bytes[d] = (byte) (i5 >> r);
                r -= 8;
                d++;
            }
        }
        return bytes;
    }

    public static final byte[] decodeFast(String chars, int offset, int charsLen) {
        int pad;
        int sepCnt;
        int sIx;
        if (charsLen == 0) {
            return new byte[0];
        }
        int sIx2 = offset;
        int eIx = (offset + charsLen) - 1;
        while (sIx2 < eIx && IA[chars.charAt(sIx2)] < 0) {
            sIx2++;
        }
        while (eIx > 0 && IA[chars.charAt(eIx)] < 0) {
            eIx--;
        }
        if (chars.charAt(eIx) == '=') {
            pad = chars.charAt(eIx + (-1)) == '=' ? 2 : 1;
        } else {
            pad = 0;
        }
        int cCnt = (eIx - sIx2) + 1;
        if (charsLen > 76) {
            sepCnt = (chars.charAt(76) == '\r' ? cCnt / 78 : 0) << 1;
        } else {
            sepCnt = 0;
        }
        int len = (((cCnt - sepCnt) * 6) >> 3) - pad;
        byte[] bytes = new byte[len];
        int cc = 0;
        int eLen = (len / 3) * 3;
        int d = 0;
        int sIx3 = sIx2;
        while (d < eLen) {
            int sIx4 = sIx3 + 1;
            int i = IA[chars.charAt(sIx3)] << 18;
            int sIx5 = sIx4 + 1;
            int i2 = i | (IA[chars.charAt(sIx4)] << 12);
            int sIx6 = sIx5 + 1;
            int i3 = i2 | (IA[chars.charAt(sIx5)] << 6);
            int sIx7 = sIx6 + 1;
            int i4 = i3 | IA[chars.charAt(sIx6)];
            int d2 = d + 1;
            bytes[d] = (byte) (i4 >> 16);
            int d3 = d2 + 1;
            bytes[d2] = (byte) (i4 >> 8);
            int d4 = d3 + 1;
            bytes[d3] = (byte) i4;
            if (sepCnt <= 0 || (cc = cc + 1) != 19) {
                sIx = sIx7;
            } else {
                sIx = sIx7 + 2;
                cc = 0;
            }
            d = d4;
            sIx3 = sIx;
        }
        if (d < len) {
            int i5 = 0;
            int j = 0;
            while (sIx3 <= eIx - pad) {
                i5 |= IA[chars.charAt(sIx3)] << (18 - (j * 6));
                j++;
                sIx3++;
            }
            int r = 16;
            while (d < len) {
                bytes[d] = (byte) (i5 >> r);
                r -= 8;
                d++;
            }
        }
        return bytes;
    }

    public static final byte[] decodeFast(String s) {
        int pad;
        int sepCnt;
        int sIx;
        int sLen = s.length();
        if (sLen == 0) {
            return new byte[0];
        }
        int sIx2 = 0;
        int eIx = sLen - 1;
        while (sIx2 < eIx && IA[s.charAt(sIx2) & 255] < 0) {
            sIx2++;
        }
        while (eIx > 0 && IA[s.charAt(eIx) & 255] < 0) {
            eIx--;
        }
        if (s.charAt(eIx) == '=') {
            pad = s.charAt(eIx + (-1)) == '=' ? 2 : 1;
        } else {
            pad = 0;
        }
        int cCnt = (eIx - sIx2) + 1;
        if (sLen > 76) {
            sepCnt = (s.charAt(76) == '\r' ? cCnt / 78 : 0) << 1;
        } else {
            sepCnt = 0;
        }
        int len = (((cCnt - sepCnt) * 6) >> 3) - pad;
        byte[] dArr = new byte[len];
        int cc = 0;
        int eLen = (len / 3) * 3;
        int d = 0;
        int sIx3 = sIx2;
        while (d < eLen) {
            int sIx4 = sIx3 + 1;
            int i = IA[s.charAt(sIx3)] << 18;
            int sIx5 = sIx4 + 1;
            int i2 = i | (IA[s.charAt(sIx4)] << 12);
            int sIx6 = sIx5 + 1;
            int i3 = i2 | (IA[s.charAt(sIx5)] << 6);
            int sIx7 = sIx6 + 1;
            int i4 = i3 | IA[s.charAt(sIx6)];
            int d2 = d + 1;
            dArr[d] = (byte) (i4 >> 16);
            int d3 = d2 + 1;
            dArr[d2] = (byte) (i4 >> 8);
            int d4 = d3 + 1;
            dArr[d3] = (byte) i4;
            if (sepCnt <= 0 || (cc = cc + 1) != 19) {
                sIx = sIx7;
            } else {
                sIx = sIx7 + 2;
                cc = 0;
            }
            d = d4;
            sIx3 = sIx;
        }
        if (d < len) {
            int i5 = 0;
            int j = 0;
            while (sIx3 <= eIx - pad) {
                i5 |= IA[s.charAt(sIx3)] << (18 - (j * 6));
                j++;
                sIx3++;
            }
            int r = 16;
            while (d < len) {
                dArr[d] = (byte) (i5 >> r);
                r -= 8;
                d++;
            }
        }
        return dArr;
    }
}
