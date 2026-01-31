package com.unisound.b;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes.dex */
public class f {
    public static final String b = "utf-8";
    public static HashMap<String, String> c = new HashMap<>();
    private static final int d = 10000;

    /* renamed from: a, reason: collision with root package name */
    public String f221a = null;

    /* JADX WARN: Removed duplicated region for block: B:42:0x00d1 A[Catch: IOException -> 0x00da, TryCatch #1 {IOException -> 0x00da, blocks: (B:40:0x00cc, B:42:0x00d1, B:44:0x00d6), top: B:62:0x00cc }] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00d6 A[Catch: IOException -> 0x00da, TRY_LEAVE, TryCatch #1 {IOException -> 0x00da, blocks: (B:40:0x00cc, B:42:0x00d1, B:44:0x00d6), top: B:62:0x00cc }] */
    /* JADX WARN: Removed duplicated region for block: B:62:0x00cc A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.unisound.b.l a(java.lang.String r11, java.util.Map<java.lang.String, java.lang.String> r12, int r13) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 258
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unisound.b.f.a(java.lang.String, java.util.Map, int):com.unisound.b.l");
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0077 A[Catch: Exception -> 0x00bd, TRY_LEAVE, TryCatch #8 {Exception -> 0x00bd, blocks: (B:16:0x0072, B:18:0x0077), top: B:74:0x0072 }] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x007c  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00af A[Catch: Exception -> 0x00b8, TRY_LEAVE, TryCatch #3 {Exception -> 0x00b8, blocks: (B:32:0x00aa, B:34:0x00af), top: B:68:0x00aa }] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00b4  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00cc A[Catch: Exception -> 0x00d5, TRY_LEAVE, TryCatch #5 {Exception -> 0x00d5, blocks: (B:44:0x00c7, B:46:0x00cc), top: B:70:0x00c7 }] */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00d1  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x00aa A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:70:0x00c7 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0072 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:87:? A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String a(java.lang.String r9, java.lang.String r10, java.lang.String r11) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 260
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unisound.b.f.a(java.lang.String, java.lang.String, java.lang.String):java.lang.String");
    }

    private static String a(Map<String, String> map) throws UnsupportedEncodingException {
        String strEncode;
        StringBuilder sb = new StringBuilder();
        String str = "";
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (true) {
            String str2 = str;
            if (!it.hasNext()) {
                i.d("requestData : POST params is " + sb.toString());
                return sb.toString();
            }
            Map.Entry<String, String> next = it.next();
            sb.append(str2);
            sb.append(next.getKey());
            sb.append("=");
            try {
                strEncode = URLEncoder.encode(next.getValue(), "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
                i.b("encode error, key = " + next.getKey());
                strEncode = "";
            }
            sb.append(strEncode);
            str = "&";
        }
    }

    public static void a(HashMap<String, String> map) {
        c = map;
    }
}
