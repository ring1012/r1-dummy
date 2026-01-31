package com.unisound.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public class az {
    private static File a(String str) throws IOException {
        File file = new File(str);
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            y.c("VprHttpClient : ", "create filedir failure!");
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x0081  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x008b A[Catch: IOException -> 0x008f, TRY_LEAVE, TryCatch #6 {IOException -> 0x008f, blocks: (B:38:0x0086, B:40:0x008b), top: B:63:0x0086 }] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0086 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean a(java.lang.String r9, java.lang.String r10) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 187
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unisound.common.az.a(java.lang.String, java.lang.String):boolean");
    }

    public static byte[] a(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[1024];
        while (true) {
            int i = inputStream.read(bArr);
            if (i == -1) {
                inputStream.close();
                return byteArrayOutputStream.toByteArray();
            }
            byteArrayOutputStream.write(bArr, 0, i);
        }
    }
}
