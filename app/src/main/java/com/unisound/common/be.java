package com.unisound.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/* loaded from: classes.dex */
public class be {

    /* renamed from: a, reason: collision with root package name */
    public short f256a = 1;
    public short b = 16000;
    private static char[] d = {'R', 'I', 'F', 'F'};
    private static char[] e = {'W', 'A', 'V', 'E'};
    private static char[] f = {'f', 'm', 't', ' '};
    private static int g = 16;
    private static short h = 1;
    public static short c = 16;
    private static char[] i = {'d', 'a', 't', 'a'};

    private static void a(ByteArrayOutputStream byteArrayOutputStream, int i2) {
        byteArrayOutputStream.write(new byte[]{(byte) ((i2 << 24) >> 24), (byte) ((i2 << 16) >> 24), (byte) ((i2 << 8) >> 24), (byte) (i2 >> 24)});
    }

    private static void a(ByteArrayOutputStream byteArrayOutputStream, short s) {
        byteArrayOutputStream.write(new byte[]{(byte) ((s << 8) >> 8), (byte) (s >> 8)});
    }

    private static void a(ByteArrayOutputStream byteArrayOutputStream, char[] cArr) {
        for (char c2 : cArr) {
            byteArrayOutputStream.write(c2);
        }
    }

    public static byte[] a(int i2, int i3, int i4) throws IOException {
        byte[] byteArray;
        IOException e2;
        ByteArrayOutputStream byteArrayOutputStream;
        try {
            short s = (short) ((c * i3) / 8);
            byteArrayOutputStream = new ByteArrayOutputStream();
            a(byteArrayOutputStream, d);
            a(byteArrayOutputStream, i2 + 36);
            a(byteArrayOutputStream, e);
            a(byteArrayOutputStream, f);
            a(byteArrayOutputStream, g);
            a(byteArrayOutputStream, h);
            a(byteArrayOutputStream, (short) i3);
            a(byteArrayOutputStream, i4);
            a(byteArrayOutputStream, s * i4);
            a(byteArrayOutputStream, s);
            a(byteArrayOutputStream, c);
            a(byteArrayOutputStream, i);
            a(byteArrayOutputStream, i2);
            byteArrayOutputStream.flush();
            byteArray = byteArrayOutputStream.toByteArray();
        } catch (IOException e3) {
            byteArray = null;
            e2 = e3;
        }
        try {
            byteArrayOutputStream.close();
        } catch (IOException e4) {
            e2 = e4;
            e2.printStackTrace();
            return byteArray;
        }
        return byteArray;
    }
}
