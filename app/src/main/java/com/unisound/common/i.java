package com.unisound.common;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class i {

    /* renamed from: a, reason: collision with root package name */
    private static final Pattern f261a = Pattern.compile("[零一二三四五六七八九十][一二三四五六七八九十零百千万亿]*(点[零一二三四五六七八九]+)?");
    private static final char[] b = {38646, 19968, 20108, 19977, 22235, 20116, 20845, 19971, 20843, 20061};
    private static Map<Character, Integer> c;
    private static Map<Character, Integer> d;
    private static Map<Character, Integer> e;

    static {
        a();
    }

    public static String a(String str) {
        int i;
        if (f261a.matcher(str).matches()) {
            if (e(str)) {
                String[] strArrSplit = str.split("点");
                return strArrSplit.length == 1 ? c(strArrSplit[0]) : String.format("%s.%s", c(strArrSplit[0]), d(strArrSplit[1]));
            }
            String[] strArrSplit2 = str.split("点");
            return strArrSplit2.length == 1 ? d(strArrSplit2[0]) : String.format("%s.%s", d(strArrSplit2[0]), d(strArrSplit2[1]));
        }
        char[] charArray = str.toCharArray();
        int length = charArray.length;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                i2 = -1;
                break;
            }
            if (f261a.matcher(String.valueOf(charArray[i2])).matches()) {
                break;
            }
            i2++;
        }
        if (i2 == -1) {
            return str;
        }
        int i3 = length - 1;
        while (true) {
            if (i3 < 0) {
                i = -1;
                break;
            }
            if (f261a.matcher(String.valueOf(charArray[i3])).matches()) {
                int i4 = i3;
                while (true) {
                    if (i3 >= charArray.length) {
                        i3 = i4;
                        break;
                    }
                    if (i3 + 2 <= charArray.length) {
                        if (!f261a.matcher(str.substring(i3, i3 + 2)).matches()) {
                            break;
                        }
                        i4 = i3 + 1;
                    }
                    i3++;
                }
                i = i3;
            } else {
                i3--;
            }
        }
        if (i2 == 0 && i == length - 1) {
            int i5 = -1;
            for (int i6 = i2; i6 < i; i6++) {
                if (!f261a.matcher(String.valueOf(charArray[i6])).matches()) {
                    i5 = i6 + 1;
                    if (i6 < length && !f261a.matcher(str.substring(0, i5 + 1)).matches()) {
                        break;
                    }
                }
            }
            if (i5 != -1) {
                return a(str.substring(0, i5)) + a(str.substring(i5));
            }
        }
        return i2 == 0 ? a(str.substring(i2, i + 1)) + str.substring(i + 1) : i == length + (-1) ? str.substring(0, i2) + a(str.substring(i2, i + 1)) : str.substring(0, i2) + a(str.substring(i2, i + 1)) + str.substring(i + 1);
    }

    public static void a() {
        c = new HashMap();
        for (int i = 0; i < b.length; i++) {
            c.put(Character.valueOf(b[i]), Integer.valueOf(i));
        }
        d = new HashMap();
        d.put((char) 21313, 10);
        d.put((char) 30334, 100);
        d.put((char) 21315, 1000);
        e = new HashMap();
        e.put((char) 19975, 10000);
        e.put((char) 20159, 100000000);
    }

    public static String b(String str) {
        if (!f261a.matcher(str).matches()) {
            throw new IllegalArgumentException(str + " is not a valid chinese number");
        }
        String[] strArrSplit = str.split("点");
        return strArrSplit.length == 1 ? c(strArrSplit[0]) : String.format("%s.%s", c(strArrSplit[0]), d(strArrSplit[1]));
    }

    private static String c(String str) {
        boolean z = false;
        int iIntValue = 1;
        int i = 0;
        int i2 = 0;
        for (char c2 : str.toCharArray()) {
            if (c.containsKey(Character.valueOf(c2))) {
                iIntValue = c.get(Character.valueOf(c2)).intValue();
                z = true;
            } else if (d.containsKey(Character.valueOf(c2))) {
                int iIntValue2 = d.get(Character.valueOf(c2)).intValue();
                int i3 = (iIntValue == 0 && iIntValue2 == 10) ? 1 : iIntValue;
                i += i3 * iIntValue2;
                iIntValue = i3;
                z = false;
            } else if (e.containsKey(Character.valueOf(c2))) {
                if (z) {
                    i += iIntValue;
                }
                int iIntValue3 = (e.get(Character.valueOf(c2)).intValue() * i) + i2;
                i = 0;
                i2 = iIntValue3;
                z = false;
            }
        }
        if (z) {
            i += iIntValue;
        }
        return Integer.toString(i2 + i);
    }

    private static String d(String str) {
        StringBuilder sb = new StringBuilder();
        for (char c2 : str.toCharArray()) {
            sb.append(Integer.toString(c.get(Character.valueOf(c2)).intValue()));
        }
        return sb.toString();
    }

    private static boolean e(String str) {
        return str != null && (str.contains("十") || str.contains("百") || str.contains("千") || str.contains("万") || str.contains("亿"));
    }
}
