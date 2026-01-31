package com.unisound.common;

import android.content.Context;
import android.text.TextUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;

/* loaded from: classes.dex */
public class j {

    /* renamed from: a, reason: collision with root package name */
    public static byte[] f262a = null;

    private static boolean a(Context context, String str, File file) throws Throwable {
        FileOutputStream fileOutputStream;
        InputStream inputStreamOpen;
        boolean z = false;
        InputStream inputStream = null;
        d(file.getAbsolutePath());
        try {
            inputStreamOpen = context.getAssets().open(str);
            try {
                fileOutputStream = new FileOutputStream(file);
                try {
                    byte[] bArr = new byte[10240];
                    while (true) {
                        int i = inputStreamOpen.read(bArr, 0, 10240);
                        if (i == -1) {
                            break;
                        }
                        fileOutputStream.write(bArr, 0, i);
                    }
                    inputStreamOpen.close();
                    InputStream inputStream2 = null;
                    try {
                        fileOutputStream.close();
                        OutputStream outputStream = null;
                        z = true;
                        if (0 != 0) {
                            try {
                                inputStream2.close();
                            } catch (IOException e) {
                            }
                        }
                        if (0 != 0) {
                            try {
                                outputStream.close();
                            } catch (IOException e2) {
                            }
                        }
                    } catch (Exception e3) {
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e4) {
                            }
                        }
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (IOException e5) {
                            }
                        }
                        return z;
                    } catch (Throwable th) {
                        th = th;
                        inputStreamOpen = null;
                        if (inputStreamOpen != null) {
                            try {
                                inputStreamOpen.close();
                            } catch (IOException e6) {
                            }
                        }
                        if (fileOutputStream == null) {
                            throw th;
                        }
                        try {
                            fileOutputStream.close();
                            throw th;
                        } catch (IOException e7) {
                            throw th;
                        }
                    }
                } catch (Exception e8) {
                    inputStream = inputStreamOpen;
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Exception e9) {
                fileOutputStream = null;
                inputStream = inputStreamOpen;
            } catch (Throwable th3) {
                th = th3;
                fileOutputStream = null;
            }
        } catch (Exception e10) {
            fileOutputStream = null;
        } catch (Throwable th4) {
            th = th4;
            fileOutputStream = null;
            inputStreamOpen = null;
        }
        return z;
    }

    public static boolean a(Context context, String str, String str2, String str3) {
        File file = new File(str2);
        if (file.exists() && aa.a(str3, file)) {
            return true;
        }
        return a(context, str, file);
    }

    public static boolean a(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return f(str);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v10 */
    /* JADX WARN: Type inference failed for: r2v11 */
    /* JADX WARN: Type inference failed for: r2v12 */
    /* JADX WARN: Type inference failed for: r2v13 */
    /* JADX WARN: Type inference failed for: r2v14 */
    /* JADX WARN: Type inference failed for: r2v15 */
    /* JADX WARN: Type inference failed for: r2v16 */
    /* JADX WARN: Type inference failed for: r2v6 */
    /* JADX WARN: Type inference failed for: r2v8 */
    /* JADX WARN: Type inference failed for: r2v9 */
    public static boolean a(String str, int i, int i2) throws Throwable {
        RandomAccessFile randomAccessFile;
        boolean z = false;
        File file = new File(str);
        RandomAccessFile randomAccessFileExists = file.exists();
        try {
            if (randomAccessFileExists != 0) {
                try {
                    randomAccessFile = new RandomAccessFile(file, "rw");
                } catch (IOException e) {
                    e = e;
                    randomAccessFile = null;
                } catch (Throwable th) {
                    th = th;
                    randomAccessFileExists = 0;
                    if (randomAccessFileExists != 0) {
                        try {
                            randomAccessFileExists.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                    throw th;
                }
                try {
                    int length = (int) randomAccessFile.length();
                    randomAccessFile.seek(0L);
                    byte[] bArrA = be.a(length, i, i2);
                    randomAccessFileExists = randomAccessFile;
                    if (bArrA != null) {
                        randomAccessFile.write(bArrA);
                        z = true;
                        randomAccessFileExists = randomAccessFile;
                        if (randomAccessFile != null) {
                            try {
                                randomAccessFile.close();
                                randomAccessFileExists = randomAccessFile;
                            } catch (IOException e3) {
                                e3.printStackTrace();
                                randomAccessFileExists = randomAccessFile;
                            }
                        }
                    } else if (randomAccessFile != null) {
                        try {
                            randomAccessFile.close();
                            randomAccessFileExists = randomAccessFile;
                        } catch (IOException e4) {
                            e4.printStackTrace();
                            randomAccessFileExists = randomAccessFile;
                        }
                    }
                } catch (IOException e5) {
                    e = e5;
                    e.printStackTrace();
                    randomAccessFileExists = randomAccessFile;
                    if (randomAccessFile != null) {
                        try {
                            randomAccessFile.close();
                            randomAccessFileExists = randomAccessFile;
                        } catch (IOException e6) {
                            e6.printStackTrace();
                            randomAccessFileExists = randomAccessFile;
                        }
                    }
                    return z;
                }
            }
            return z;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:42:0x0054 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean a(boolean r4, int r5, java.lang.String r6) throws java.lang.Throwable {
        /*
            if (r6 == 0) goto L49
            java.lang.String r0 = ""
            boolean r0 = r0.equals(r6)
            if (r0 != 0) goto L49
            boolean r0 = a(r6)
            if (r0 == 0) goto L49
            d(r6)
            r2 = 0
            if (r4 == 0) goto L33
            short r0 = (short) r5
            byte[] r0 = a(r0)
        L1b:
            java.io.RandomAccessFile r1 = new java.io.RandomAccessFile     // Catch: java.lang.Exception -> L3f java.lang.Throwable -> L50
            java.lang.String r3 = "rw"
            r1.<init>(r6, r3)     // Catch: java.lang.Exception -> L3f java.lang.Throwable -> L50
            long r2 = r1.length()     // Catch: java.lang.Throwable -> L5d java.lang.Exception -> L5f
            r1.seek(r2)     // Catch: java.lang.Throwable -> L5d java.lang.Exception -> L5f
            r1.write(r0)     // Catch: java.lang.Throwable -> L5d java.lang.Exception -> L5f
            r0 = 1
            if (r1 == 0) goto L32
            r1.close()     // Catch: java.io.IOException -> L3a
        L32:
            return r0
        L33:
            int r0 = -r5
            short r0 = (short) r0
            byte[] r0 = a(r0)
            goto L1b
        L3a:
            r1 = move-exception
            r1.printStackTrace()
            goto L32
        L3f:
            r0 = move-exception
            r1 = r2
        L41:
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L5d
            if (r1 == 0) goto L49
            r1.close()     // Catch: java.io.IOException -> L4b
        L49:
            r0 = 0
            goto L32
        L4b:
            r0 = move-exception
            r0.printStackTrace()
            goto L49
        L50:
            r0 = move-exception
            r1 = r2
        L52:
            if (r1 == 0) goto L57
            r1.close()     // Catch: java.io.IOException -> L58
        L57:
            throw r0
        L58:
            r1 = move-exception
            r1.printStackTrace()
            goto L57
        L5d:
            r0 = move-exception
            goto L52
        L5f:
            r0 = move-exception
            goto L41
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unisound.common.j.a(boolean, int, java.lang.String):boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x0055 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean a(boolean r4, java.lang.String r5) {
        /*
            if (r5 == 0) goto L4a
            java.lang.String r0 = ""
            boolean r0 = r0.equals(r5)
            if (r0 != 0) goto L4a
            boolean r0 = a(r5)
            if (r0 == 0) goto L4a
            d(r5)
            r2 = 0
            if (r4 == 0) goto L34
            r0 = 32767(0x7fff, float:4.5916E-41)
            byte[] r0 = a(r0)
        L1c:
            java.io.RandomAccessFile r1 = new java.io.RandomAccessFile     // Catch: java.lang.Exception -> L40 java.lang.Throwable -> L51
            java.lang.String r3 = "rw"
            r1.<init>(r5, r3)     // Catch: java.lang.Exception -> L40 java.lang.Throwable -> L51
            long r2 = r1.length()     // Catch: java.lang.Throwable -> L5e java.lang.Exception -> L60
            r1.seek(r2)     // Catch: java.lang.Throwable -> L5e java.lang.Exception -> L60
            r1.write(r0)     // Catch: java.lang.Throwable -> L5e java.lang.Exception -> L60
            r0 = 1
            if (r1 == 0) goto L33
            r1.close()     // Catch: java.io.IOException -> L3b
        L33:
            return r0
        L34:
            r0 = -32767(0xffffffffffff8001, float:NaN)
            byte[] r0 = a(r0)
            goto L1c
        L3b:
            r1 = move-exception
            r1.printStackTrace()
            goto L33
        L40:
            r0 = move-exception
            r1 = r2
        L42:
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L5e
            if (r1 == 0) goto L4a
            r1.close()     // Catch: java.io.IOException -> L4c
        L4a:
            r0 = 0
            goto L33
        L4c:
            r0 = move-exception
            r0.printStackTrace()
            goto L4a
        L51:
            r0 = move-exception
            r1 = r2
        L53:
            if (r1 == 0) goto L58
            r1.close()     // Catch: java.io.IOException -> L59
        L58:
            throw r0
        L59:
            r1 = move-exception
            r1.printStackTrace()
            goto L58
        L5e:
            r0 = move-exception
            goto L53
        L60:
            r0 = move-exception
            goto L42
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unisound.common.j.a(boolean, java.lang.String):boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:35:0x0046 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean a(byte[] r4, java.lang.String r5) {
        /*
            if (r5 == 0) goto L3b
            java.lang.String r0 = ""
            boolean r0 = r0.equals(r5)
            if (r0 != 0) goto L3b
            boolean r0 = a(r5)
            if (r0 == 0) goto L3b
            d(r5)
            r2 = 0
            java.io.RandomAccessFile r1 = new java.io.RandomAccessFile     // Catch: java.lang.Exception -> L31 java.lang.Throwable -> L42
            java.lang.String r0 = "rw"
            r1.<init>(r5, r0)     // Catch: java.lang.Exception -> L31 java.lang.Throwable -> L42
            long r2 = r1.length()     // Catch: java.lang.Throwable -> L4f java.lang.Exception -> L51
            r1.seek(r2)     // Catch: java.lang.Throwable -> L4f java.lang.Exception -> L51
            r1.write(r4)     // Catch: java.lang.Throwable -> L4f java.lang.Exception -> L51
            r0 = 1
            if (r1 == 0) goto L2b
            r1.close()     // Catch: java.io.IOException -> L2c
        L2b:
            return r0
        L2c:
            r1 = move-exception
            r1.printStackTrace()
            goto L2b
        L31:
            r0 = move-exception
            r1 = r2
        L33:
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L4f
            if (r1 == 0) goto L3b
            r1.close()     // Catch: java.io.IOException -> L3d
        L3b:
            r0 = 0
            goto L2b
        L3d:
            r0 = move-exception
            r0.printStackTrace()
            goto L3b
        L42:
            r0 = move-exception
            r1 = r2
        L44:
            if (r1 == 0) goto L49
            r1.close()     // Catch: java.io.IOException -> L4a
        L49:
            throw r0
        L4a:
            r1 = move-exception
            r1.printStackTrace()
            goto L49
        L4f:
            r0 = move-exception
            goto L44
        L51:
            r0 = move-exception
            goto L33
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unisound.common.j.a(byte[], java.lang.String):boolean");
    }

    public static byte[] a(Context context) {
        if (f262a == null) {
            f262a = Arrays.copyOfRange(a(context, "empty"), 0, 6400);
        }
        return f262a;
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0021 A[ORIG_RETURN, RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0040  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x003c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static byte[] a(android.content.Context r8, java.lang.String r9) throws java.lang.Throwable {
        /*
            r6 = 6400(0x1900, float:8.968E-42)
            r4 = 0
            r2 = 0
            android.content.res.AssetManager r0 = r8.getAssets()     // Catch: java.lang.Exception -> L27 java.lang.Throwable -> L38
            java.io.InputStream r3 = r0.open(r9)     // Catch: java.lang.Exception -> L27 java.lang.Throwable -> L38
            r0 = 6400(0x1900, float:8.968E-42)
            byte[] r1 = new byte[r0]     // Catch: java.lang.Throwable -> L45 java.lang.Exception -> L4a
            r0 = 0
            int r5 = r1.length     // Catch: java.lang.Throwable -> L45 java.lang.Exception -> L4e
            int r0 = r3.read(r1, r0, r5)     // Catch: java.lang.Throwable -> L45 java.lang.Exception -> L4e
            r3 = 0
            if (r2 == 0) goto L54
            r3.close()     // Catch: java.io.IOException -> L22
            r7 = r1
            r1 = r0
            r0 = r7
        L1f:
            if (r1 <= 0) goto L40
        L21:
            return r0
        L22:
            r2 = move-exception
            r7 = r1
            r1 = r0
            r0 = r7
            goto L1f
        L27:
            r0 = move-exception
            r1 = r2
        L29:
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L47
            if (r2 == 0) goto L51
            r2.close()     // Catch: java.io.IOException -> L34
            r0 = r1
            r1 = r4
            goto L1f
        L34:
            r0 = move-exception
            r0 = r1
            r1 = r4
            goto L1f
        L38:
            r0 = move-exception
            r3 = r2
        L3a:
            if (r3 == 0) goto L3f
            r3.close()     // Catch: java.io.IOException -> L43
        L3f:
            throw r0
        L40:
            byte[] r0 = new byte[r6]
            goto L21
        L43:
            r1 = move-exception
            goto L3f
        L45:
            r0 = move-exception
            goto L3a
        L47:
            r0 = move-exception
            r3 = r2
            goto L3a
        L4a:
            r0 = move-exception
            r1 = r2
            r2 = r3
            goto L29
        L4e:
            r0 = move-exception
            r2 = r3
            goto L29
        L51:
            r0 = r1
            r1 = r4
            goto L1f
        L54:
            r7 = r1
            r1 = r0
            r0 = r7
            goto L1f
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unisound.common.j.a(android.content.Context, java.lang.String):byte[]");
    }

    public static byte[] a(short s) {
        byte[] bArr = new byte[2];
        for (int i = 0; i < 2; i++) {
            bArr[i] = (byte) ((s >>> 8) & 255);
        }
        return bArr;
    }

    public static boolean b(String str) {
        return e(str) == k.WAV_EXTENSION_NAME;
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x0055 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean b(boolean r4, java.lang.String r5) throws java.lang.Throwable {
        /*
            if (r5 == 0) goto L4a
            java.lang.String r0 = ""
            boolean r0 = r0.equals(r5)
            if (r0 != 0) goto L4a
            boolean r0 = a(r5)
            if (r0 == 0) goto L4a
            d(r5)
            r2 = 0
            if (r4 == 0) goto L34
            r0 = 28000(0x6d60, float:3.9236E-41)
            byte[] r0 = a(r0)
        L1c:
            java.io.RandomAccessFile r1 = new java.io.RandomAccessFile     // Catch: java.lang.Exception -> L40 java.lang.Throwable -> L51
            java.lang.String r3 = "rw"
            r1.<init>(r5, r3)     // Catch: java.lang.Exception -> L40 java.lang.Throwable -> L51
            long r2 = r1.length()     // Catch: java.lang.Throwable -> L5e java.lang.Exception -> L60
            r1.seek(r2)     // Catch: java.lang.Throwable -> L5e java.lang.Exception -> L60
            r1.write(r0)     // Catch: java.lang.Throwable -> L5e java.lang.Exception -> L60
            r0 = 1
            if (r1 == 0) goto L33
            r1.close()     // Catch: java.io.IOException -> L3b
        L33:
            return r0
        L34:
            r0 = -28000(0xffffffffffff92a0, float:NaN)
            byte[] r0 = a(r0)
            goto L1c
        L3b:
            r1 = move-exception
            r1.printStackTrace()
            goto L33
        L40:
            r0 = move-exception
            r1 = r2
        L42:
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L5e
            if (r1 == 0) goto L4a
            r1.close()     // Catch: java.io.IOException -> L4c
        L4a:
            r0 = 0
            goto L33
        L4c:
            r0 = move-exception
            r0.printStackTrace()
            goto L4a
        L51:
            r0 = move-exception
            r1 = r2
        L53:
            if (r1 == 0) goto L58
            r1.close()     // Catch: java.io.IOException -> L59
        L58:
            throw r0
        L59:
            r1 = move-exception
            r1.printStackTrace()
            goto L58
        L5e:
            r0 = move-exception
            goto L53
        L60:
            r0 = move-exception
            goto L42
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unisound.common.j.b(boolean, java.lang.String):boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:35:0x0046 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean b(byte[] r4, java.lang.String r5) throws java.lang.Throwable {
        /*
            if (r5 == 0) goto L3b
            java.lang.String r0 = ""
            boolean r0 = r0.equals(r5)
            if (r0 != 0) goto L3b
            boolean r0 = a(r5)
            if (r0 == 0) goto L3b
            d(r5)
            r2 = 0
            java.io.RandomAccessFile r1 = new java.io.RandomAccessFile     // Catch: java.lang.Exception -> L31 java.lang.Throwable -> L42
            java.lang.String r0 = "rw"
            r1.<init>(r5, r0)     // Catch: java.lang.Exception -> L31 java.lang.Throwable -> L42
            long r2 = r1.length()     // Catch: java.lang.Throwable -> L4f java.lang.Exception -> L51
            r1.seek(r2)     // Catch: java.lang.Throwable -> L4f java.lang.Exception -> L51
            r1.write(r4)     // Catch: java.lang.Throwable -> L4f java.lang.Exception -> L51
            r0 = 1
            if (r1 == 0) goto L2b
            r1.close()     // Catch: java.io.IOException -> L2c
        L2b:
            return r0
        L2c:
            r1 = move-exception
            r1.printStackTrace()
            goto L2b
        L31:
            r0 = move-exception
            r1 = r2
        L33:
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L4f
            if (r1 == 0) goto L3b
            r1.close()     // Catch: java.io.IOException -> L3d
        L3b:
            r0 = 0
            goto L2b
        L3d:
            r0 = move-exception
            r0.printStackTrace()
            goto L3b
        L42:
            r0 = move-exception
            r1 = r2
        L44:
            if (r1 == 0) goto L49
            r1.close()     // Catch: java.io.IOException -> L4a
        L49:
            throw r0
        L4a:
            r1 = move-exception
            r1.printStackTrace()
            goto L49
        L4f:
            r0 = move-exception
            goto L44
        L51:
            r0 = move-exception
            goto L33
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unisound.common.j.b(byte[], java.lang.String):boolean");
    }

    public static boolean c(String str) {
        if (new File(str).exists()) {
            return f(str);
        }
        return false;
    }

    private static void d(String str) {
        int iLastIndexOf;
        if (str != null && (iLastIndexOf = str.lastIndexOf(47)) >= 0) {
            new File(str.substring(0, iLastIndexOf)).mkdirs();
        }
    }

    private static k e(String str) {
        int iLastIndexOf;
        if (TextUtils.isEmpty(str) || (iLastIndexOf = str.lastIndexOf(".")) <= 0) {
            return null;
        }
        String strSubstring = str.substring(iLastIndexOf + 1);
        return TextUtils.isEmpty(strSubstring) ? k.NO_EXTENSION_NAME : strSubstring.equalsIgnoreCase(com.unisound.sdk.c.b) ? k.PCM_EXTENSION_NAME : strSubstring.equalsIgnoreCase("wav") ? k.WAV_EXTENSION_NAME : k.OTHER_EXTENSION_NAME;
    }

    private static boolean f(String str) {
        k kVarE = e(str);
        if (kVarE == k.PCM_EXTENSION_NAME || kVarE == k.WAV_EXTENSION_NAME) {
            return true;
        }
        y.a("fileName illegal");
        return false;
    }
}
