package com.unisound.common;

import android.os.Environment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: classes.dex */
public class r {
    public static void a(String str) {
        int iLastIndexOf;
        if (str != null && (iLastIndexOf = str.lastIndexOf(47)) >= 0) {
            new File(str.substring(0, iLastIndexOf)).mkdirs();
        }
    }

    public static boolean a() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    public static boolean a(InputStream inputStream, OutputStream outputStream) throws IOException {
        if (inputStream == null || outputStream == null) {
            return false;
        }
        byte[] bArr = new byte[1024];
        while (true) {
            try {
                try {
                    int i = inputStream.read(bArr);
                    if (i <= 0) {
                        try {
                            outputStream.close();
                            inputStream.close();
                            return true;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                    outputStream.write(bArr, 0, i);
                } catch (IOException e2) {
                    e2.printStackTrace();
                    try {
                        return false;
                    } catch (IOException e3) {
                        return false;
                    }
                }
            } finally {
                try {
                    outputStream.close();
                    inputStream.close();
                } catch (IOException e32) {
                    e32.printStackTrace();
                }
            }
        }
    }

    public static boolean a(String str, String str2, boolean z) {
        File file = new File(str);
        File file2 = new File(str2);
        if (!file.isFile() || !file2.isFile()) {
            return false;
        }
        try {
            file2.getParentFile().mkdirs();
            return a(new FileInputStream(file), new FileOutputStream(file2, false));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public static String b() {
        String path = Environment.getExternalStorageDirectory().getPath();
        return (path == null || path.endsWith(File.separator)) ? path : path + File.separator;
    }
}
