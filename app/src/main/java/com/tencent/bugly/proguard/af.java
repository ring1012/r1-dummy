package com.tencent.bugly.proguard;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class af implements ag {

    /* renamed from: a, reason: collision with root package name */
    private String f175a = null;

    @Override // com.tencent.bugly.proguard.ag
    public final byte[] a(byte[] bArr) throws Exception {
        if (this.f175a == null || bArr == null) {
            return null;
        }
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(2, SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(this.f175a.getBytes("UTF-8"))), new IvParameterSpec(this.f175a.getBytes("UTF-8")));
        return cipher.doFinal(bArr);
    }

    @Override // com.tencent.bugly.proguard.ag
    public final byte[] b(byte[] bArr) throws Exception {
        if (this.f175a == null || bArr == null) {
            return null;
        }
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(1, SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(this.f175a.getBytes("UTF-8"))), new IvParameterSpec(this.f175a.getBytes("UTF-8")));
        return cipher.doFinal(bArr);
    }

    @Override // com.tencent.bugly.proguard.ag
    public final void a(String str) {
        if (str != null) {
            this.f175a = str;
        }
    }
}
