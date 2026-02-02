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

    public static String a(String str, int timeout)  {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        InputStream is = null;

        try {
            URL url = new URL(str);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(timeout);
            conn.setRequestMethod("GET");
            conn.connect();

            String result;

            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                reader = new BufferedReader(
                        new InputStreamReader(is, com.unisound.b.f.b)
                );

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                result = sb.toString();
            } else {
                result = "{}";
            }

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }


    public static String a(String str, String str2) {
        return a(str, str2.getBytes());
    }

    public static String a(String str, byte[] bArr) {
        return a(str, bArr, 30000);
    }

    public static String a(String str, byte[] bArr, int timeout)  {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(str);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(timeout);

            OutputStream os = conn.getOutputStream();
            os.write(bArr);
            os.flush();
            os.close();

            String result;
            if (conn.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), com.unisound.b.f.b)
                );
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();
                result = sb.toString();
            } else {
                result = "{}";
            }

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
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
    public static int b(java.lang.String r5, byte[] r6, int r7) {
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
