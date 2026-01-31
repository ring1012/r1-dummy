package com.tencent.bugly.proguard;

import android.content.Context;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class s {
    private static s b;

    /* renamed from: a, reason: collision with root package name */
    public Map<String, String> f201a = null;
    private Context c;

    private s(Context context) {
        this.c = context;
    }

    public static s a(Context context) {
        if (b == null) {
            b = new s(context);
        }
        return b;
    }

    /* JADX WARN: Removed duplicated region for block: B:74:0x0177 A[Catch: all -> 0x0189, TRY_LEAVE, TryCatch #5 {all -> 0x0189, blocks: (B:22:0x009e, B:24:0x00a6, B:27:0x00b6, B:30:0x00c1, B:47:0x00e3, B:49:0x00eb, B:58:0x0116, B:60:0x012f, B:63:0x0151, B:72:0x0171, B:74:0x0177), top: B:107:0x009e }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final byte[] a(java.lang.String r19, byte[] r20, com.tencent.bugly.proguard.v r21, java.util.Map<java.lang.String, java.lang.String> r22) {
        /*
            Method dump skipped, instructions count: 467
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.s.a(java.lang.String, byte[], com.tencent.bugly.proguard.v, java.util.Map):byte[]");
    }

    private static Map<String, String> a(HttpURLConnection httpURLConnection) {
        HashMap map = new HashMap();
        Map<String, List<String>> headerFields = httpURLConnection.getHeaderFields();
        if (headerFields == null || headerFields.size() == 0) {
            return null;
        }
        for (String str : headerFields.keySet()) {
            List<String> list = headerFields.get(str);
            if (list.size() > 0) {
                map.put(str, list.get(0));
            }
        }
        return map;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [java.io.BufferedInputStream] */
    /* JADX WARN: Type inference failed for: r0v10 */
    /* JADX WARN: Type inference failed for: r0v6, types: [byte[]] */
    /* JADX WARN: Type inference failed for: r0v8 */
    /* JADX WARN: Type inference failed for: r0v9 */
    private static byte[] b(HttpURLConnection httpURLConnection) throws Throwable {
        BufferedInputStream bufferedInputStream;
        byte[] byteArray = 0;
        byteArray = 0;
        byteArray = 0;
        byteArray = 0;
        try {
            if (httpURLConnection != null) {
                try {
                    bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                } catch (Throwable th) {
                    th = th;
                    if (0 != 0) {
                        try {
                            byteArray.close();
                        } catch (Throwable th2) {
                            th2.printStackTrace();
                        }
                    }
                    throw th;
                }
                try {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] bArr = new byte[1024];
                    while (true) {
                        int i = bufferedInputStream.read(bArr);
                        if (i <= 0) {
                            break;
                        }
                        byteArrayOutputStream.write(bArr, 0, i);
                    }
                    byteArrayOutputStream.flush();
                    byteArray = byteArrayOutputStream.toByteArray();
                    try {
                        bufferedInputStream.close();
                    } catch (Throwable th3) {
                        th3.printStackTrace();
                    }
                } catch (Throwable th4) {
                    th = th4;
                    if (!x.a(th)) {
                        th.printStackTrace();
                    }
                    if (bufferedInputStream != null) {
                        try {
                            bufferedInputStream.close();
                        } catch (Throwable th5) {
                            th5.printStackTrace();
                        }
                    }
                    return byteArray;
                }
            }
            return byteArray;
        } catch (Throwable th6) {
            th = th6;
        }
    }

    private HttpURLConnection a(String str, byte[] bArr, String str2, Map<String, String> map) {
        if (str == null) {
            x.e("destUrl is null.", new Object[0]);
            return null;
        }
        HttpURLConnection httpURLConnectionA = a(str2, str);
        if (httpURLConnectionA == null) {
            x.e("Failed to get HttpURLConnection object.", new Object[0]);
            return null;
        }
        try {
            httpURLConnectionA.setRequestProperty("wup_version", "3.0");
            if (map != null && map.size() > 0) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    httpURLConnectionA.setRequestProperty(entry.getKey(), URLEncoder.encode(entry.getValue(), com.unisound.b.f.b));
                }
            }
            httpURLConnectionA.setRequestProperty("A37", URLEncoder.encode(str2, com.unisound.b.f.b));
            httpURLConnectionA.setRequestProperty("A38", URLEncoder.encode(str2, com.unisound.b.f.b));
            OutputStream outputStream = httpURLConnectionA.getOutputStream();
            if (bArr == null) {
                outputStream.write(0);
            } else {
                outputStream.write(bArr);
            }
            return httpURLConnectionA;
        } catch (Throwable th) {
            if (!x.a(th)) {
                th.printStackTrace();
            }
            x.e("Failed to upload, please check your network.", new Object[0]);
            return null;
        }
    }

    private static HttpURLConnection a(String str, String str2) {
        HttpURLConnection httpURLConnection;
        try {
            URL url = new URL(str2);
            if (str != null && str.toLowerCase(Locale.US).contains("wap")) {
                httpURLConnection = (HttpURLConnection) url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(System.getProperty("http.proxyHost"), Integer.parseInt(System.getProperty("http.proxyPort")))));
            } else {
                httpURLConnection = (HttpURLConnection) url.openConnection();
            }
            httpURLConnection.setConnectTimeout(30000);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setInstanceFollowRedirects(false);
            return httpURLConnection;
        } catch (Throwable th) {
            if (!x.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }
}
