package com.unisound.b;

import cn.yunzhisheng.asr.JniUscClient;
import java.lang.reflect.Type;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/* loaded from: classes.dex */
public class h {
    public static double a(JSONObject jSONObject, String str, double d) {
        if (jSONObject == null || !jSONObject.has(str)) {
            return d;
        }
        try {
            return jSONObject.getDouble(str);
        } catch (JSONException e) {
            e.printStackTrace();
            return d;
        }
    }

    public static int a(JSONObject jSONObject, String str, int i) {
        if (jSONObject == null || !jSONObject.has(str)) {
            return i;
        }
        try {
            return jSONObject.getInt(str);
        } catch (JSONException e) {
            e.printStackTrace();
            return i;
        }
    }

    public static <T> Object a(String str, Class<T> cls) {
        return null;
    }

    public static Object a(String str, Type type) {
        return null;
    }

    public static String a(Object obj) {
        return null;
    }

    public static String a(JSONObject jSONObject, String str, String str2) throws JSONException {
        if (jSONObject == null || !jSONObject.has(str)) {
            return str2;
        }
        try {
            String string = jSONObject.getString(str);
            return (string == null || string.equals("")) ? str2 : string.equals(JniUscClient.az) ? str2 : string;
        } catch (JSONException e) {
            e.printStackTrace();
            return str2;
        }
    }

    public static JSONObject a(String str) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            Object objNextValue = new JSONTokener(str).nextValue();
            return (objNextValue == null || !(objNextValue instanceof JSONObject)) ? jSONObject : (JSONObject) objNextValue;
        } catch (Exception e) {
            return jSONObject;
        }
    }

    public static JSONObject a(JSONArray jSONArray, int i) {
        if (jSONArray == null) {
            return null;
        }
        try {
            if (i < jSONArray.length()) {
                return jSONArray.getJSONObject(i);
            }
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject a(JSONObject jSONObject, String str) {
        if (jSONObject == null || !jSONObject.has(str)) {
            return null;
        }
        try {
            return jSONObject.getJSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void a(JSONObject jSONObject, String str, Object obj) throws JSONException {
        if (obj != null) {
            try {
                if ("".equals(obj)) {
                    return;
                }
                jSONObject.put(str, obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean a(JSONObject jSONObject, String str, boolean z) {
        if (jSONObject == null || !jSONObject.has(str)) {
            return z;
        }
        try {
            return jSONObject.getBoolean(str);
        } catch (JSONException e) {
            e.printStackTrace();
            return z;
        }
    }

    public static String b(JSONObject jSONObject, String str) {
        if (jSONObject != null && jSONObject.has(str)) {
            try {
                return jSONObject.getString(str);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x002f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static org.json.JSONArray b(java.lang.String r3) throws org.json.JSONException {
        /*
            org.json.JSONArray r1 = new org.json.JSONArray
            r1.<init>()
            if (r3 == 0) goto L25
            java.lang.String r0 = ""
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L25
            org.json.JSONTokener r0 = new org.json.JSONTokener
            r0.<init>(r3)
            java.lang.Object r0 = r0.nextValue()     // Catch: java.lang.Exception -> L2d
            if (r0 == 0) goto L2f
            boolean r2 = r0 instanceof org.json.JSONObject     // Catch: java.lang.Exception -> L2d
            if (r2 == 0) goto L26
            org.json.JSONObject r0 = (org.json.JSONObject) r0     // Catch: java.lang.Exception -> L2d
            r1.put(r0)     // Catch: java.lang.Exception -> L2d
            r0 = r1
        L24:
            r1 = r0
        L25:
            return r1
        L26:
            boolean r2 = r0 instanceof org.json.JSONArray     // Catch: java.lang.Exception -> L2d
            if (r2 == 0) goto L2f
            org.json.JSONArray r0 = (org.json.JSONArray) r0     // Catch: java.lang.Exception -> L2d
            goto L24
        L2d:
            r0 = move-exception
            goto L25
        L2f:
            r0 = r1
            goto L24
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unisound.b.h.b(java.lang.String):org.json.JSONArray");
    }

    public static void b(JSONObject jSONObject, String str, String str2) throws JSONException {
        if (str2 != null) {
            try {
                if ("".equals(str2)) {
                    return;
                }
                jSONObject.put(str, str2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static JSONArray c(JSONObject jSONObject, String str) {
        if (jSONObject != null && jSONObject.has(str)) {
            try {
                return jSONObject.getJSONArray(str);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
