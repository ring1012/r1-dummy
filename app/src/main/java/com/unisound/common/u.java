package com.unisound.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/* loaded from: classes.dex */
public class u {
    public static String a(String str) {
        return a(str, 30000);
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 4 */
    public static String a(String str, int i) throws Throwable {
        BufferedReader bufferedReader;
        InputStream inputStream;
        String str2;
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream2 = null;
        httpURLConnection = null;
        httpURLConnection = null;
        httpURLConnection = null;
        try {
            try {
                HttpURLConnection httpURLConnection2 = (HttpURLConnection) new URL(str).openConnection();
                try {
                    httpURLConnection2.setConnectTimeout(i);
                    httpURLConnection2.setRequestMethod("GET");
                    httpURLConnection2.connect();
                    if (httpURLConnection2.getResponseCode() == 200) {
                        inputStream = httpURLConnection2.getInputStream();
                        try {
                            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, com.unisound.b.f.b));
                            str2 = "";
                            while (true) {
                                try {
                                    String line = bufferedReader.readLine();
                                    if (line == null) {
                                        break;
                                    }
                                    str2 = str2 + line;
                                } catch (MalformedURLException e) {
                                    httpURLConnection = httpURLConnection2;
                                    e = e;
                                    e.printStackTrace();
                                    if (inputStream != null) {
                                        try {
                                            inputStream.close();
                                        } catch (IOException e2) {
                                            e2.printStackTrace();
                                            return str2;
                                        }
                                    }
                                    if (bufferedReader != null) {
                                        bufferedReader.close();
                                    }
                                    if (httpURLConnection != null) {
                                        httpURLConnection.disconnect();
                                    }
                                    return str2;
                                } catch (IOException e3) {
                                    httpURLConnection = httpURLConnection2;
                                    e = e3;
                                    e.printStackTrace();
                                    if (inputStream != null) {
                                        try {
                                            inputStream.close();
                                        } catch (IOException e4) {
                                            e4.printStackTrace();
                                            return str2;
                                        }
                                    }
                                    if (bufferedReader != null) {
                                        bufferedReader.close();
                                    }
                                    if (httpURLConnection != null) {
                                        httpURLConnection.disconnect();
                                    }
                                    return str2;
                                } catch (Throwable th) {
                                    httpURLConnection = httpURLConnection2;
                                    th = th;
                                    if (inputStream != null) {
                                        try {
                                            inputStream.close();
                                        } catch (IOException e5) {
                                            e5.printStackTrace();
                                            throw th;
                                        }
                                    }
                                    if (bufferedReader != null) {
                                        bufferedReader.close();
                                    }
                                    if (httpURLConnection != null) {
                                        httpURLConnection.disconnect();
                                    }
                                    throw th;
                                }
                            }
                            inputStream2 = inputStream;
                        } catch (MalformedURLException e6) {
                            bufferedReader = null;
                            httpURLConnection = httpURLConnection2;
                            e = e6;
                            str2 = "";
                        } catch (IOException e7) {
                            bufferedReader = null;
                            httpURLConnection = httpURLConnection2;
                            e = e7;
                            str2 = "";
                        } catch (Throwable th2) {
                            bufferedReader = null;
                            httpURLConnection = httpURLConnection2;
                            th = th2;
                        }
                    } else {
                        str2 = "{}";
                        bufferedReader = null;
                    }
                    if (inputStream2 != null) {
                        try {
                            inputStream2.close();
                        } catch (IOException e8) {
                            e8.printStackTrace();
                        }
                    }
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                    if (httpURLConnection2 != null) {
                        httpURLConnection2.disconnect();
                    }
                } catch (MalformedURLException e9) {
                    bufferedReader = null;
                    inputStream = null;
                    httpURLConnection = httpURLConnection2;
                    e = e9;
                    str2 = "";
                } catch (IOException e10) {
                    bufferedReader = null;
                    inputStream = null;
                    httpURLConnection = httpURLConnection2;
                    e = e10;
                    str2 = "";
                } catch (Throwable th3) {
                    bufferedReader = null;
                    inputStream = null;
                    httpURLConnection = httpURLConnection2;
                    th = th3;
                }
            } catch (MalformedURLException e11) {
                e = e11;
                str2 = "";
                bufferedReader = null;
                inputStream = null;
            } catch (IOException e12) {
                e = e12;
                str2 = "";
                bufferedReader = null;
                inputStream = null;
            } catch (Throwable th4) {
                th = th4;
                bufferedReader = null;
                inputStream = null;
            }
            return str2;
        } catch (Throwable th5) {
            th = th5;
        }
    }

    public static String a(String str, String str2) {
        return a(str, str2.getBytes());
    }

    public static String a(String str, byte[] bArr) {
        return a(str, bArr, 30000);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v11 */
    /* JADX WARN: Type inference failed for: r0v12 */
    /* JADX WARN: Type inference failed for: r0v4, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r0v5, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r1v15, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r1v19 */
    /* JADX WARN: Type inference failed for: r1v20, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r1v25 */
    /* JADX WARN: Type inference failed for: r4v1, types: [java.lang.StringBuilder] */
    public static String a(String str, byte[] bArr, int i) throws Throwable {
        ?? r0;
        HttpURLConnection httpURLConnection;
        HttpURLConnection httpURLConnection2 = null;
        HttpURLConnection httpURLConnection3 = null;
        try {
            try {
                HttpURLConnection httpURLConnection4 = (HttpURLConnection) new URL(str).openConnection();
                try {
                    httpURLConnection4.setRequestProperty("Accept-Charset", "UTF-8");
                    httpURLConnection4.setRequestMethod("POST");
                    httpURLConnection4.setDoInput(true);
                    httpURLConnection4.setDoOutput(true);
                    httpURLConnection4.setConnectTimeout(i);
                    OutputStream outputStream = httpURLConnection4.getOutputStream();
                    outputStream.write(bArr);
                    outputStream.flush();
                    outputStream.close();
                    if (httpURLConnection4.getResponseCode() == 200) {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection4.getInputStream(), com.unisound.b.f.b));
                        ?? r1 = "";
                        while (true) {
                            String line = bufferedReader.readLine();
                            if (line == null) {
                                break;
                            }
                            r1 = r1 + line;
                        }
                        bufferedReader.close();
                        httpURLConnection = r1;
                    } else {
                        httpURLConnection = "{}";
                    }
                    if (httpURLConnection4 != null) {
                        httpURLConnection4.disconnect();
                        r0 = httpURLConnection;
                        httpURLConnection2 = httpURLConnection;
                    } else {
                        r0 = httpURLConnection;
                        httpURLConnection2 = httpURLConnection;
                    }
                } catch (Exception e) {
                    httpURLConnection3 = httpURLConnection4;
                    e = e;
                    e.printStackTrace();
                    r0 = "Error";
                    httpURLConnection2 = httpURLConnection3;
                    if (httpURLConnection3 != null) {
                        httpURLConnection3.disconnect();
                        httpURLConnection2 = httpURLConnection3;
                    }
                    return r0;
                } catch (Throwable th) {
                    httpURLConnection2 = httpURLConnection4;
                    th = th;
                    if (httpURLConnection2 != null) {
                        httpURLConnection2.disconnect();
                    }
                    throw th;
                }
            } catch (Exception e2) {
                e = e2;
            }
            return r0;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static int b(String str, String str2) {
        return b(str, str2.getBytes(), 30000);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:23:0x005c A[PHI: r1 r2
  0x005c: PHI (r1v2 int) = (r1v0 int), (r1v6 int) binds: [B:13:0x0045, B:8:0x003a] A[DONT_GENERATE, DONT_INLINE]
  0x005c: PHI (r2v4 ??) = (r2v3 ??), (r2v14 ??) binds: [B:13:0x0045, B:8:0x003a] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Type inference failed for: r2v0 */
    /* JADX WARN: Type inference failed for: r2v1 */
    /* JADX WARN: Type inference failed for: r2v14 */
    /* JADX WARN: Type inference failed for: r2v15 */
    /* JADX WARN: Type inference failed for: r2v16 */
    /* JADX WARN: Type inference failed for: r2v17 */
    /* JADX WARN: Type inference failed for: r2v18 */
    /* JADX WARN: Type inference failed for: r2v2, types: [java.net.HttpURLConnection] */
    /* JADX WARN: Type inference failed for: r2v3, types: [java.net.HttpURLConnection] */
    /* JADX WARN: Type inference failed for: r2v4 */
    /* JADX WARN: Type inference failed for: r2v5 */
    /* JADX WARN: Type inference failed for: r2v6 */
    /* JADX WARN: Type inference failed for: r2v8 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int b(java.lang.String r5, byte[] r6, int r7) throws java.lang.Throwable {
        /*
            r2 = 0
            r1 = -1
            java.net.URL r0 = new java.net.URL     // Catch: java.lang.Exception -> L41 java.lang.Throwable -> L4c
            r0.<init>(r5)     // Catch: java.lang.Exception -> L41 java.lang.Throwable -> L4c
            java.net.URLConnection r0 = r0.openConnection()     // Catch: java.lang.Exception -> L41 java.lang.Throwable -> L4c
            java.net.HttpURLConnection r0 = (java.net.HttpURLConnection) r0     // Catch: java.lang.Exception -> L41 java.lang.Throwable -> L4c
            java.lang.String r2 = "Accept-Charset"
            java.lang.String r3 = "UTF-8"
            r0.setRequestProperty(r2, r3)     // Catch: java.lang.Throwable -> L53 java.lang.Exception -> L57
            java.lang.String r2 = "POST"
            r0.setRequestMethod(r2)     // Catch: java.lang.Throwable -> L53 java.lang.Exception -> L57
            r2 = 1
            r0.setDoInput(r2)     // Catch: java.lang.Throwable -> L53 java.lang.Exception -> L57
            r2 = 1
            r0.setDoOutput(r2)     // Catch: java.lang.Throwable -> L53 java.lang.Exception -> L57
            r0.setConnectTimeout(r7)     // Catch: java.lang.Throwable -> L53 java.lang.Exception -> L57
            java.io.OutputStream r2 = r0.getOutputStream()     // Catch: java.lang.Throwable -> L53 java.lang.Exception -> L57
            r2.write(r6)     // Catch: java.lang.Throwable -> L53 java.lang.Exception -> L57
            r2.flush()     // Catch: java.lang.Throwable -> L53 java.lang.Exception -> L57
            r2.close()     // Catch: java.lang.Throwable -> L53 java.lang.Exception -> L57
            int r1 = r0.getResponseCode()     // Catch: java.lang.Throwable -> L53 java.lang.Exception -> L57
            r2 = 200(0xc8, float:2.8E-43)
            if (r1 != r2) goto L3a
            r1 = 0
        L3a:
            if (r0 == 0) goto L5c
            r0.disconnect()
            r0 = r1
        L40:
            return r0
        L41:
            r0 = move-exception
        L42:
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L4c
            if (r2 == 0) goto L5c
            r2.disconnect()
            r0 = r1
            goto L40
        L4c:
            r0 = move-exception
        L4d:
            if (r2 == 0) goto L52
            r2.disconnect()
        L52:
            throw r0
        L53:
            r1 = move-exception
            r2 = r0
            r0 = r1
            goto L4d
        L57:
            r2 = move-exception
            r4 = r2
            r2 = r0
            r0 = r4
            goto L42
        L5c:
            r0 = r1
            goto L40
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unisound.common.u.b(java.lang.String, byte[], int):int");
    }
}
