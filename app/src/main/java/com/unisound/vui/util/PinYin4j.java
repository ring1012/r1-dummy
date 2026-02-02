package com.unisound.vui.util;

import cn.yunzhisheng.common.PinyinConverter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class PinYin4j {
    private PinYin4j() {
    }

    public static String converterToSpell(String chines) {
        StringBuffer stringBuffer = new StringBuffer();
        char[] charArray = chines.toCharArray();
        a.a.first.a2.b bVar = new a.a.first.a2.b();
        bVar.a(a.a.a.a.a.b);
        bVar.a(a.a.a.a.c.b);
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] > 128) {
                try {
                    String[] strArrA = a.a.a.c.a(charArray[i], bVar);
                    if (strArrA != null) {
                        for (int i2 = 0; i2 < strArrA.length; i2++) {
                            stringBuffer.append(strArrA[i2]);
                            if (i2 != strArrA.length - 1) {
                                stringBuffer.append(",");
                            }
                        }
                    }
                } catch (a.a.first.a2.a1.a e) {
                    e.printStackTrace();
                }
            } else {
                stringBuffer.append(charArray[i]);
            }
            stringBuffer.append(PinyinConverter.PINYIN_SEPARATOR);
        }
        return parseTheChineseByObject(discountTheChinese(stringBuffer.toString()));
    }

    private static List<Map<String, Integer>> discountTheChinese(String theStr) {
        ArrayList arrayList = new ArrayList();
        for (String str : theStr.split(PinyinConverter.PINYIN_SEPARATOR)) {
            Hashtable hashtable = new Hashtable();
            for (String str2 : str.split(",")) {
                Integer num = (Integer) hashtable.get(str2);
                if (num == null) {
                    hashtable.put(str2, new Integer(1));
                } else {
                    hashtable.remove(str2);
                    hashtable.put(str2, Integer.valueOf(num.intValue() + 1));
                }
            }
            arrayList.add(hashtable);
        }
        return arrayList;
    }

    private static String parseTheChineseByObject(List<Map<String, Integer>> list) {
        String str;
        Hashtable hashtable = null;
        int i = 0;
        while (i < list.size()) {
            Hashtable hashtable2 = new Hashtable();
            if (hashtable != null) {
                for (String str2 : hashtable.keySet()) {
                    Iterator<String> it = list.get(i).keySet().iterator();
                    while (it.hasNext()) {
                        hashtable2.put(str2 + it.next(), 1);
                    }
                }
                if (hashtable2 != null && hashtable2.size() > 0) {
                    hashtable.clear();
                }
            } else {
                Iterator<String> it2 = list.get(i).keySet().iterator();
                while (it2.hasNext()) {
                    hashtable2.put(it2.next(), 1);
                }
            }
            i++;
            hashtable = (hashtable2 == null || hashtable2.size() <= 0) ? hashtable : hashtable2;
        }
        String str3 = "";
        if (hashtable != null) {
            Iterator it3 = hashtable.keySet().iterator();
            while (true) {
                str = str3;
                if (!it3.hasNext()) {
                    break;
                }
                str3 = str + ((String) it3.next()) + ",";
            }
        } else {
            str = "";
        }
        return str.length() > 0 ? str.substring(0, str.length() - 1) : str;
    }
}
