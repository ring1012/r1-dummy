package com.unisound.common;

import com.unisound.sdk.cn;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import nluparser.scheme.ASR;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class v {

    /* renamed from: a, reason: collision with root package name */
    public static final String f271a = "partial";
    public static final String b = "full";
    public static final String c = "change";
    private static final String d = "asr_recongize";
    private static final String e = "retTag";
    private static final String f = "nlu";
    private static ArrayList<String> g = new ArrayList<>();

    public static cn a(String str, boolean z, com.unisound.sdk.w wVar) {
        try {
            if (str.contains(d)) {
                if (str.contains("-changeable-")) {
                    str = str.replace("-changeable-", "");
                }
                JSONObject jSONObject = new JSONObject(str);
                if (jSONObject.has(e)) {
                    if (jSONObject.getString(e).equals(f)) {
                        jSONObject.remove(e);
                        if (jSONObject.has(d)) {
                            jSONObject.remove(d);
                            if (jSONObject.has("nluProcessTime")) {
                                wVar.y(jSONObject.getString("nluProcessTime"));
                            } else {
                                wVar.y("0");
                            }
                            return new cn(jSONObject.toString().replace("\\/", MqttTopic.TOPIC_LEVEL_SEPARATOR));
                        }
                    }
                } else if (!z && jSONObject.has(d)) {
                    jSONObject.remove(d);
                    if (jSONObject.has("nluProcessTime")) {
                        wVar.y(jSONObject.getString("nluProcessTime"));
                    } else {
                        wVar.y("0");
                    }
                    return new cn(jSONObject.toString().replace("\\/", MqttTopic.TOPIC_LEVEL_SEPARATOR));
                }
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return new cn(null);
    }

    public static String a(int i, String str, String str2, String str3, Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7) throws JSONException {
        if (str2 == null) {
            return "";
        }
        JSONObject jSONObject = new JSONObject();
        try {
            if (i == 1) {
                jSONObject.put("engine_mode", ASR.NET);
            } else if (i == 0) {
                jSONObject.put("engine_mode", ASR.MIX);
            } else if (i == 2) {
                jSONObject.put("engine_mode", ASR.LOCAL);
            } else if (i == 1000) {
                jSONObject.put("engine_mode", "wakeup");
            }
            if (str != null) {
                jSONObject.put(com.unisound.passport.c.l, str);
            }
            if (obj != null) {
                jSONObject.put("last_result", obj);
            }
            if (str2 != null) {
                jSONObject.put("recognition_result", str2);
            }
            if (str3 != null) {
                jSONObject.put(x.c, str3);
            }
            if (obj2 != null) {
                jSONObject.put("score", obj2);
            }
            if (obj3 != null) {
                jSONObject.put("utteranceTime", obj3);
            }
            if (obj4 != null) {
                jSONObject.put("outRecordingTime", obj4);
            }
            if (obj5 != null) {
                jSONObject.put("delayTime", obj5);
            }
            if (obj6 != null) {
                jSONObject.put("utteranceStartTime", obj6);
            }
            if (obj7 != null) {
                jSONObject.put("utteranceEndTime", obj7);
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return jSONObject.toString().replace("\\/", MqttTopic.TOPIC_LEVEL_SEPARATOR);
    }

    public static String a(com.unisound.sdk.b bVar) {
        com.unisound.sdk.am amVarE = bVar.a().e();
        return amVarE == com.unisound.sdk.am.LOCAL ? b(bVar) : amVarE == com.unisound.sdk.am.NET ? c(bVar) : "";
    }

    private String a(String str) {
        try {
            if (str.contains(d) && str.contains(e)) {
                if (str.contains("-changeable-")) {
                    str = str.replace("-changeable-", "");
                }
                JSONObject jSONObject = new JSONObject(str);
                if (jSONObject.has(e) && jSONObject.getString(e).equals(f) && jSONObject.has(d)) {
                    return jSONObject.getString(d);
                }
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return "";
    }

    private static String a(String str, String str2) throws JSONException {
        String string = "";
        try {
            if (!str.contains(str2)) {
                return str;
            }
            JSONObject jSONObject = new JSONObject(str);
            if (!jSONObject.has(str2)) {
                return "";
            }
            string = jSONObject.getString(str2);
            return string;
        } catch (JSONException e2) {
            e2.printStackTrace();
            return string;
        }
    }

    public static String a(ArrayList<String> arrayList, ArrayList<String> arrayList2, ArrayList<String> arrayList3) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        if (arrayList != null) {
            new JSONObject();
            JSONArray jSONArray = new JSONArray();
            for (int i = 0; i < arrayList.size(); i++) {
                try {
                    jSONArray.put(new JSONObject(arrayList.get(i)));
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
            }
            jSONObject.put(ASR.LOCAL_ASR, jSONArray);
        }
        if (arrayList2 != null) {
            new JSONObject();
            JSONArray jSONArray2 = new JSONArray();
            for (int i2 = 0; i2 < arrayList2.size(); i2++) {
                try {
                    jSONArray2.put(new JSONObject(arrayList2.get(i2)));
                } catch (JSONException e3) {
                    e3.printStackTrace();
                }
            }
            jSONObject.put(ASR.NET_ASR, jSONArray2);
        }
        if (arrayList3 != null && arrayList3.size() > 0) {
            new JSONObject();
            JSONArray jSONArray3 = new JSONArray();
            for (int i3 = 0; i3 < arrayList3.size(); i3++) {
                try {
                    if (arrayList3.get(i3) != null) {
                        jSONArray3.put(new JSONObject(arrayList3.get(i3)));
                    }
                } catch (JSONException e4) {
                    e4.printStackTrace();
                }
            }
            jSONObject.put("net_nlu", jSONArray3);
        }
        return jSONObject.toString().replace("\\/", MqttTopic.TOPIC_LEVEL_SEPARATOR);
    }

    public static Map<Integer, Object> a(String str, Map<String, Integer> map) {
        HashMap map2 = new HashMap();
        try {
            JSONObject jSONObject = new JSONObject(str);
            for (String str2 : map.keySet()) {
                map2.put(map.get(str2), jSONObject.opt(str2));
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return map2;
    }

    public static void a() {
        y.c("clearTotalASRResult");
        if (g != null) {
            g.clear();
        }
    }

    public static String b() {
        if (g == null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= g.size()) {
                return new String(stringBuffer);
            }
            stringBuffer.append(g.get(i2));
            i = i2 + 1;
        }
    }

    private static String b(com.unisound.sdk.b bVar) throws JSONException {
        com.unisound.sdk.al alVarA = bVar.a();
        int iB = bVar.b();
        boolean zC = bVar.c();
        String strA = alVarA.a();
        boolean zD = alVarA.d();
        float fH = alVarA.h();
        int i = alVarA.i();
        int iF = alVarA.f();
        int iG = alVarA.g();
        long jK = alVarA.k();
        long j = alVarA.j();
        JSONObject jSONObject = new JSONObject();
        try {
            if (iB == 1) {
                jSONObject.put("engine_mode", ASR.NET);
            } else if (iB == 0) {
                jSONObject.put("engine_mode", ASR.MIX);
            } else if (iB == 2) {
                jSONObject.put("engine_mode", ASR.LOCAL);
            } else if (iB == 1000) {
                jSONObject.put("engine_mode", "wakeup");
            }
            if (zC) {
                jSONObject.put(com.unisound.passport.c.l, "full");
            } else if (zD) {
                jSONObject.put(com.unisound.passport.c.l, c);
            } else {
                jSONObject.put(com.unisound.passport.c.l, "partial");
            }
            jSONObject.put("recognition_result", strA);
            jSONObject.put("score", fH);
            jSONObject.put("utteranceTime", i);
            jSONObject.put("outRecordingTime", j);
            jSONObject.put("delayTime", jK);
            jSONObject.put("utteranceStartTime", iF);
            jSONObject.put("utteranceEndTime", iG);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return jSONObject.toString().replace("\\/", MqttTopic.TOPIC_LEVEL_SEPARATOR);
    }

    private static void b(String str) {
        y.c("addToTotalASRResult=" + str);
        if (g != null) {
            g.add(str);
        }
    }

    private static String c(com.unisound.sdk.b bVar) throws JSONException {
        com.unisound.sdk.al alVarA = bVar.a();
        int iB = bVar.b();
        boolean zC = bVar.c();
        boolean zD = bVar.d();
        String strB = alVarA.b();
        String strA = alVarA.a();
        if (zD) {
            y.b("originFormat onResult -> result = ", strA);
        } else {
            strA = a(strA, d);
            if (!alVarA.d()) {
                b(strA);
            }
            y.b("format onResult -> result = ", strA);
        }
        boolean zD2 = alVarA.d();
        boolean zC2 = alVarA.c();
        JSONObject jSONObject = new JSONObject();
        try {
            if (iB == 1) {
                jSONObject.put("engine_mode", ASR.NET);
            } else if (iB == 0) {
                jSONObject.put("engine_mode", ASR.MIX);
            } else if (iB == 2) {
                jSONObject.put("engine_mode", ASR.LOCAL);
            } else if (iB == 1000) {
                jSONObject.put("engine_mode", "wakeup");
            }
            if (zC) {
                jSONObject.put(com.unisound.passport.c.l, "full");
            } else if (zD2) {
                jSONObject.put(com.unisound.passport.c.l, c);
            } else {
                jSONObject.put(com.unisound.passport.c.l, "partial");
            }
            jSONObject.put("last_result", zC2);
            jSONObject.put("recognition_result", strA);
            jSONObject.put(x.c, strB);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return jSONObject.toString().replace("\\/", MqttTopic.TOPIC_LEVEL_SEPARATOR);
    }
}
