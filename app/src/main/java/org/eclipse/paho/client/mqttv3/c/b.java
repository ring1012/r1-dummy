package org.eclipse.paho.client.mqttv3.c;

/* loaded from: classes.dex */
public final class b {

    /* renamed from: a, reason: collision with root package name */
    private static final int f508a = -1;

    private b() {
    }

    private static int a(CharSequence charSequence, CharSequence charSequence2, int i) {
        return charSequence.toString().indexOf(charSequence2.toString(), i);
    }

    public static boolean a(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }

    public static boolean a(CharSequence charSequence, CharSequence charSequence2) {
        if (charSequence2 == null) {
            return false;
        }
        return a(charSequence, b(charSequence2));
    }

    public static boolean a(CharSequence charSequence, char[] cArr) {
        if (a(charSequence) || a(cArr)) {
            return false;
        }
        int length = charSequence.length();
        int length2 = cArr.length;
        int i = length - 1;
        int i2 = length2 - 1;
        for (int i3 = 0; i3 < length; i3++) {
            char cCharAt = charSequence.charAt(i3);
            for (int i4 = 0; i4 < length2; i4++) {
                if (cArr[i4] == cCharAt) {
                    if (!Character.isHighSurrogate(cCharAt) || i4 == i2) {
                        return true;
                    }
                    if (i3 < i && cArr[i4 + 1] == charSequence.charAt(i3 + 1)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean a(CharSequence charSequence, CharSequence[] charSequenceArr) {
        boolean z = charSequence == null && charSequenceArr == null;
        if (charSequenceArr == null) {
            return z;
        }
        boolean z2 = z;
        for (CharSequence charSequence2 : charSequenceArr) {
            z2 = z2 || charSequence2.equals(charSequence);
        }
        return z2;
    }

    private static boolean a(char[] cArr) {
        return cArr == null || cArr.length == 0;
    }

    public static int b(CharSequence charSequence, CharSequence charSequence2) {
        int length = 0;
        if (a(charSequence) || a(charSequence2)) {
            return 0;
        }
        int i = 0;
        while (true) {
            int iA = a(charSequence, charSequence2, length);
            if (iA == -1) {
                return i;
            }
            i++;
            length = iA + charSequence2.length();
        }
    }

    private static char[] b(CharSequence charSequence) {
        if (charSequence instanceof String) {
            return ((String) charSequence).toCharArray();
        }
        int length = charSequence.length();
        char[] cArr = new char[charSequence.length()];
        for (int i = 0; i < length; i++) {
            cArr[i] = charSequence.charAt(i);
        }
        return cArr;
    }
}
