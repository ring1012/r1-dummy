package com.unisound.passport.a;

import android.util.Log;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public class b {
    public static String a(String str) {
        try {
            return a(MessageDigest.getInstance("SHA-1").digest(str.getBytes("UTF-8")));
        } catch (Exception e) {
            Log.e("PubUtil", e.getMessage());
            return null;
        }
    }

    public static String a(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        Collections.sort(list);
        StringBuilder sb = new StringBuilder();
        for (String str : list) {
            if (str == null) {
                str = "";
            }
            sb.append(str);
        }
        return a(sb.toString());
    }

    private static String a(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() == 1) {
                sb.append("0");
            }
            sb.append(hexString.toUpperCase());
        }
        return sb.toString();
    }
}
