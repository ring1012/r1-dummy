package com.unisound.passport.a;

import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes.dex */
public class a {
    /* JADX WARN: Removed duplicated region for block: B:55:0x00a9 A[Catch: IOException -> 0x00b2, TryCatch #7 {IOException -> 0x00b2, blocks: (B:53:0x00a4, B:55:0x00a9, B:57:0x00ae), top: B:87:0x00a4 }] */
    /* JADX WARN: Removed duplicated region for block: B:57:0x00ae A[Catch: IOException -> 0x00b2, TRY_LEAVE, TryCatch #7 {IOException -> 0x00b2, blocks: (B:53:0x00a4, B:55:0x00a9, B:57:0x00ae), top: B:87:0x00a4 }] */
    /* JADX WARN: Removed duplicated region for block: B:87:0x00a4 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String a(java.lang.String r8, int r9) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 240
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unisound.passport.a.a.a(java.lang.String, int):java.lang.String");
    }

    /* JADX WARN: Removed duplicated region for block: B:51:0x00c0 A[Catch: IOException -> 0x00c9, TryCatch #4 {IOException -> 0x00c9, blocks: (B:49:0x00bb, B:51:0x00c0, B:53:0x00c5), top: B:77:0x00bb }] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00c5 A[Catch: IOException -> 0x00c9, TRY_LEAVE, TryCatch #4 {IOException -> 0x00c9, blocks: (B:49:0x00bb, B:51:0x00c0, B:53:0x00c5), top: B:77:0x00bb }] */
    /* JADX WARN: Removed duplicated region for block: B:77:0x00bb A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String a(java.lang.String r7, java.util.Map<java.lang.String, java.lang.String> r8, int r9) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 232
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unisound.passport.a.a.a(java.lang.String, java.util.Map, int):java.lang.String");
    }

    public static String a(Map<String, String> map) throws UnsupportedEncodingException {
        String strEncode;
        StringBuilder sb = new StringBuilder();
        String str = "";
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (true) {
            String str2 = str;
            if (!it.hasNext()) {
                Log.e("TEMPLOG", "POST params is " + sb.toString());
                return sb.toString();
            }
            Map.Entry<String, String> next = it.next();
            sb.append(str2);
            sb.append(next.getKey());
            sb.append("=");
            try {
                strEncode = URLEncoder.encode(next.getValue(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                strEncode = "";
            }
            sb.append(strEncode);
            str = "&";
        }
    }
}
