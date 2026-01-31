package com.unisound.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/* loaded from: classes.dex */
public class aj {
    public static String a() {
        return UUID.randomUUID().toString().toUpperCase(Locale.getDefault());
    }

    public static String a(String str, String str2) {
        try {
            return new String(str.getBytes(str2), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String a(String str, Map<String, String> map) throws UnsupportedEncodingException {
        String strEncode;
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("?");
        String str2 = "";
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (true) {
            String str3 = str2;
            if (!it.hasNext()) {
                return sb.toString();
            }
            Map.Entry<String, String> next = it.next();
            sb.append(str3);
            sb.append(next.getKey());
            sb.append("=");
            try {
                strEncode = URLEncoder.encode(next.getValue(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                strEncode = "";
            }
            sb.append(strEncode);
            str2 = "&";
        }
    }

    public static String a(Map<String, String> map, String str) {
        return a(map, null, str);
    }

    public static String a(Map<String, String> map, List<String> list, String str) {
        try {
            StringBuilder sb = new StringBuilder();
            ArrayList<String> arrayList = new ArrayList(map.size());
            arrayList.addAll(map.keySet());
            if (list != null && list.size() > 0) {
                Iterator<String> it = list.iterator();
                while (it.hasNext()) {
                    arrayList.remove(it.next());
                }
            }
            Collections.sort(arrayList);
            sb.append(str);
            sb.append("&");
            for (String str2 : arrayList) {
                sb.append(str2).append("=").append(map.get(str2)).append("&");
            }
            sb.append(str);
            return a(a(sb.toString()));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String a(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() == 1) {
                sb.append("0");
            }
            sb.append(hexString.toUpperCase(Locale.getDefault()));
        }
        return sb.toString();
    }

    private static byte[] a(String str) throws Exception {
        try {
            return MessageDigest.getInstance("SHA-1").digest(str.getBytes("UTF-8"));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
