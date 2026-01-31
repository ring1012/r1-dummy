package com.unisound.vui.util;

import android.text.TextUtils;
import java.lang.Character;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class StringUtils {
    private StringUtils() {
    }

    public static String MD5(String str) throws NoSuchAlgorithmException {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            char[] charArray = str.toCharArray();
            byte[] bArr = new byte[charArray.length];
            for (int i = 0; i < charArray.length; i++) {
                bArr[i] = (byte) charArray[i];
            }
            byte[] bArrDigest = messageDigest.digest(bArr);
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : bArrDigest) {
                int i2 = b & 255;
                if (i2 < 16) {
                    stringBuffer.append("0");
                }
                stringBuffer.append(Integer.toHexString(i2));
            }
            return stringBuffer.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int getSubString(String str, String key) {
        int length = 0;
        int i = 0;
        while (true) {
            int iIndexOf = str.indexOf(key, length);
            if (iIndexOf == -1) {
                return i;
            }
            length = iIndexOf + key.length();
            i++;
        }
    }

    public static boolean hasChinese(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isChinese(char c) {
        Character.UnicodeBlock unicodeBlockOf = Character.UnicodeBlock.of(c);
        return unicodeBlockOf == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || unicodeBlockOf == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || unicodeBlockOf == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || unicodeBlockOf == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B || unicodeBlockOf == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || unicodeBlockOf == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || unicodeBlockOf == Character.UnicodeBlock.GENERAL_PUNCTUATION;
    }

    public static boolean isTrue(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return String.valueOf(true).equals(str);
    }

    public static boolean listStringEquals(List<String> oldList, List<String> newList) {
        if (oldList == null || newList == null) {
            return false;
        }
        if (oldList.size() != newList.size()) {
            return false;
        }
        Iterator<String> it = oldList.iterator();
        while (it.hasNext()) {
            if (!newList.contains(it.next())) {
                return false;
            }
        }
        return true;
    }

    public static String matchFind(String response, String result) {
        String strGroup = "";
        Matcher matcher = Pattern.compile(result).matcher(response);
        while (matcher.find()) {
            strGroup = matcher.group(1);
        }
        return strGroup;
    }
}
