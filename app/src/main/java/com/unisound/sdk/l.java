package com.unisound.sdk;

import android.text.TextUtils;
import cn.yunzhisheng.common.PinyinConverter;
import cn.yunzhisheng.nlu.OfflineNlu;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/* loaded from: classes.dex */
public class l {
    public String d;
    public float e;
    public OfflineNlu h;
    private String i;

    /* renamed from: a, reason: collision with root package name */
    public List<String> f347a = new ArrayList();
    public boolean b = true;
    public boolean c = false;
    public boolean f = false;
    public String g = "";

    public static String a(float f, String str) {
        String[] strArrSplit = str.split("\n");
        int length = strArrSplit.length - 1;
        return length > 0 ? f > b(strArrSplit[length]) ? "" : str.replace(strArrSplit[length], "").trim() : str;
    }

    private String a(String str, String str2) {
        com.unisound.common.y.c("RecognizeResult", "rmIgnoreTag : result before ignoreTag :=", str2);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<").append(str).append(">");
        StringBuffer stringBuffer2 = new StringBuffer();
        stringBuffer2.append("</").append(str).append(">");
        String strReplaceAll = Pattern.compile(stringBuffer2.toString()).matcher(Pattern.compile(stringBuffer.toString()).matcher(str2).replaceAll("")).replaceAll("");
        com.unisound.common.y.c("RecognizeResult", "rmIgnoreTag : result end ignoreTag :=", strReplaceAll);
        return strReplaceAll;
    }

    public static float b(String str) {
        try {
            return Float.parseFloat(str);
        } catch (Exception e) {
            e.printStackTrace();
            return -100.0f;
        }
    }

    public static float d(String str) {
        String[] strArrSplit = str.split("\n");
        int length = strArrSplit.length - 1;
        if (length > 0) {
            return b(strArrSplit[length]);
        }
        return -25.0f;
    }

    public static String e(String str) {
        if (str.indexOf("<s>") >= 0) {
            str = str.substring(str.indexOf("<s>"));
        }
        String strReplaceAll = str.replaceAll(PinyinConverter.PINYIN_SEPARATOR, "");
        Pattern patternCompile = Pattern.compile("<s>|</s>");
        return Pattern.compile("<[\\w]*>").matcher(Pattern.compile("<[\\w]*>").matcher(patternCompile.matcher(strReplaceAll).replaceAll("")).replaceAll("").replaceAll(MqttTopic.TOPIC_LEVEL_SEPARATOR, "")).replaceAll("").split("\n")[0].trim();
    }

    public String a() {
        Iterator<String> it = this.f347a.iterator();
        return it.hasNext() ? it.next() : "";
    }

    public boolean a(String str) {
        this.d = "";
        if (str == null || str.length() == 0) {
            return false;
        }
        String[] strArrSplit = Pattern.compile("<[\\w]*>").matcher(Pattern.compile("<[\\w]*>").matcher(Pattern.compile("<s>|</s>").matcher(str).replaceAll("")).replaceAll("\n").replaceAll(MqttTopic.TOPIC_LEVEL_SEPARATOR, "").replaceAll("\n\n", "\n")).replaceAll("").split("\n");
        this.e = -25.0f;
        int length = strArrSplit.length - 1;
        if (length <= 0) {
            return false;
        }
        this.e = b(strArrSplit[length]);
        if (length == 1) {
            this.d = strArrSplit[0];
        } else {
            this.d = strArrSplit[0] + strArrSplit[length - 1];
        }
        return true;
    }

    public boolean a(String str, boolean z, String str2) {
        int i = 0;
        this.f347a.clear();
        if (str == null || str.length() == 0) {
            return false;
        }
        if (str.split("\n").length < 2) {
            com.unisound.common.y.a("FixrecognizeResult -> setResultList: RecognitionResult error for lessing than two lines!");
            return false;
        }
        if (str.indexOf("<s>") >= 0) {
            str = str.substring(str.indexOf("<s>"));
        }
        if (this.f && !z) {
            if (this.g.equals("") || this.h == null) {
                com.unisound.common.y.e("RecognizeResult", "setResultList : nluConfigFile didn't exists or OfflineNlu is null");
                return false;
            }
            String[] strArrSplit = str.split("\n");
            this.e = -25.0f;
            int length = strArrSplit.length - 1;
            if (length <= 0) {
                return false;
            }
            this.e = b(strArrSplit[length]);
            while (i < length) {
                this.f347a.add(this.h.a("[" + strArrSplit[i] + "]", ""));
                i++;
            }
            return true;
        }
        if (this.c) {
            if (!TextUtils.isEmpty(str2)) {
                str = a(str2, str);
            }
            String[] strArrSplit2 = str.split("\n");
            this.e = -25.0f;
            int length2 = strArrSplit2.length - 1;
            com.unisound.common.y.c("RecognizeResult", "setResultList : arrayOfstring.length =", Integer.valueOf(strArrSplit2.length));
            if (length2 <= 0) {
                return false;
            }
            this.e = b(strArrSplit2[length2]);
            for (int i2 = 0; i2 < length2; i2++) {
                try {
                    this.f347a.add(com.unisound.common.ai.b(strArrSplit2[i2], this.e, this.i + "result.xml").toString().replace(PinyinConverter.PINYIN_SEPARATOR, "").replace("_", ""));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        if (!this.b) {
            String[] strArrSplit3 = str.split("\n");
            this.e = -25.0f;
            int length3 = strArrSplit3.length - 1;
            if (length3 <= 0) {
                return false;
            }
            this.e = b(strArrSplit3[length3]);
            while (i < length3) {
                this.f347a.add(strArrSplit3[i]);
                i++;
            }
            return true;
        }
        String[] strArrSplit4 = Pattern.compile("<[\\w]*>").matcher(Pattern.compile("<[\\w]*>").matcher(Pattern.compile("<s>|</s>").matcher(str).replaceAll("")).replaceAll("").replaceAll(MqttTopic.TOPIC_LEVEL_SEPARATOR, "")).replaceAll("").split("\n");
        this.e = -25.0f;
        int length4 = strArrSplit4.length - 1;
        if (length4 <= 0) {
            return false;
        }
        this.e = b(strArrSplit4[length4]);
        while (i < length4) {
            this.f347a.add(strArrSplit4[i]);
            i++;
        }
        return true;
    }

    public void c(String str) {
        this.i = str;
    }

    public String toString() {
        return (this.d == null || this.d.length() <= 0) ? "" : this.d + "\n" + this.e;
    }
}
