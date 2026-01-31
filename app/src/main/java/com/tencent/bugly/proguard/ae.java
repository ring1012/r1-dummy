package com.tencent.bugly.proguard;

import cn.yunzhisheng.common.PinyinConverter;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class ae implements ag {

    /* renamed from: a, reason: collision with root package name */
    private String f174a = null;

    @Override // com.tencent.bugly.proguard.ag
    public final byte[] a(byte[] bArr) throws Exception {
        if (this.f174a == null || bArr == null) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : bArr) {
            stringBuffer.append(((int) b) + PinyinConverter.PINYIN_SEPARATOR);
        }
        SecretKeySpec secretKeySpec = new SecretKeySpec(this.f174a.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(2, secretKeySpec, new IvParameterSpec(this.f174a.getBytes()));
        byte[] bArrDoFinal = cipher.doFinal(bArr);
        StringBuffer stringBuffer2 = new StringBuffer();
        for (byte b2 : bArrDoFinal) {
            stringBuffer2.append(((int) b2) + PinyinConverter.PINYIN_SEPARATOR);
        }
        return bArrDoFinal;
    }

    @Override // com.tencent.bugly.proguard.ag
    public final byte[] b(byte[] bArr) throws Exception {
        if (this.f174a == null || bArr == null) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : bArr) {
            stringBuffer.append(((int) b) + PinyinConverter.PINYIN_SEPARATOR);
        }
        SecretKeySpec secretKeySpec = new SecretKeySpec(this.f174a.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(1, secretKeySpec, new IvParameterSpec(this.f174a.getBytes()));
        byte[] bArrDoFinal = cipher.doFinal(bArr);
        StringBuffer stringBuffer2 = new StringBuffer();
        for (byte b2 : bArrDoFinal) {
            stringBuffer2.append(((int) b2) + PinyinConverter.PINYIN_SEPARATOR);
        }
        return bArrDoFinal;
    }

    @Override // com.tencent.bugly.proguard.ag
    public final void a(String str) {
        if (str != null) {
            for (int length = str.length(); length < 16; length++) {
                str = str + "0";
            }
            this.f174a = str.substring(0, 16);
        }
    }
}
