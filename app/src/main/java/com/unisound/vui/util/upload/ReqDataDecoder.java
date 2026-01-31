package com.unisound.vui.util.upload;

import android.annotation.SuppressLint;
import com.unisound.vui.util.LogMgr;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/* loaded from: classes.dex */
public class ReqDataDecoder {
    private static final String TAG = ReqDataDecoder.class.getSimpleName();
    private static final int TOTAL_LEN_FIELD_LEN = 4;

    private ReqDataDecoder() {
    }

    @SuppressLint({"NewApi"})
    public static DecodeResult decode(byte[] bArr) {
        byte b = 0;
        if (bArr == null || bArr.length == 0) {
            LogMgr.e(TAG, "bad input");
            return null;
        }
        byte[] bArr2 = new byte[4];
        System.arraycopy(bArr, 0, bArr2, 0, 4);
        int i = ByteBuffer.wrap(bArr2).getInt();
        int i2 = bArr[4];
        int i3 = ((i - 4) - 1) - i2;
        int length = bArr.length;
        LogMgr.e(TAG, "[IN DECODE]totalLen: " + i + ", userIDLen: " + i2 + ", leftLen: " + i3 + ", inputLen: " + length);
        if (i >= length) {
            return null;
        }
        byte[] bArr3 = new byte[i2];
        int i4 = 5 + i2;
        System.arraycopy(bArr, 5, bArr3, 0, i2);
        byte b2 = bArr3[0];
        byte[] bArr4 = new byte[i3];
        int i5 = 0;
        while (i5 != i3) {
            byte b3 = bArr3[i5 % i2];
            byte b4 = bArr[i4 + i5];
            bArr4[i5] = (byte) ((b ^ b4) ^ b3);
            i5++;
            b = b4;
        }
        DecodeResult decodeResult = new DecodeResult();
        decodeResult.setUserID(new String(bArr3, Charset.forName("UTF-8")));
        decodeResult.setOther(bArr4);
        return decodeResult;
    }
}
