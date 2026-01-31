package com.baidu.a.a.a.a;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: classes.dex */
public final class a {
    public static byte[] a(String str, String str2, byte[] bArr) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(str2.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(1, secretKeySpec, new IvParameterSpec(str.getBytes()));
        return cipher.doFinal(bArr);
    }

    public static byte[] b(String str, String str2, byte[] bArr) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(str2.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(2, secretKeySpec, new IvParameterSpec(str.getBytes()));
        return cipher.doFinal(bArr);
    }
}
