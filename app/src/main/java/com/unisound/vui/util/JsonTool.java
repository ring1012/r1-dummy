package com.unisound.vui.util;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import java.lang.reflect.Type;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/* loaded from: classes.dex */
public class JsonTool {
    public static final String TAG = "JsonTool";

    private JsonTool() {
    }

    public static <T> Object fromJson(JsonArray json, Type type) {
        return new Gson().fromJson(json, type);
    }

    public static <T> Object fromJson(JsonObject json, Class<T> typeOfT) {
        return new Gson().fromJson((JsonElement) json, (Class) typeOfT);
    }

    public static <T> Object fromJson(JsonObject json, Type type) {
        return new Gson().fromJson(json, type);
    }

    public static <T> Object fromJson(String json, Class<T> classOfT) {
        return new Gson().fromJson(json, (Type) classOfT);
    }

    public static <T> T fromJson(String str, Type type) {
        return (T) new Gson().fromJson(str, type);
    }

    public static Object fromJsonWithFastJson(String json, Type typeOfT) {
        return JSON.parseObject(json, typeOfT, new Feature[0]);
    }

    public static JSONObject getJSONObject(JSONArray jsonArr, int index) {
        if (jsonArr == null) {
            return null;
        }
        try {
            if (index < jsonArr.length()) {
                return jsonArr.getJSONObject(index);
            }
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject getJSONObject(JSONObject jsonObj, String name) {
        if (jsonObj == null || !jsonObj.has(name)) {
            return null;
        }
        try {
            return jsonObj.getJSONObject(name);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONArray getJsonArray(JSONObject jsonObj, String key) {
        if (jsonObj != null && jsonObj.has(key)) {
            try {
                return jsonObj.getJSONArray(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static double getJsonDoubleValue(JSONObject json, String key) {
        if (json != null && json.has(key)) {
            try {
                return json.getDouble(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return -1.0d;
    }

    public static int getJsonIntValue(JSONObject json, String key) {
        if (json != null && json.has(key)) {
            try {
                return json.getInt(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public static double getJsonValue(JSONObject json, String key, double defValue) {
        if (json == null || !json.has(key)) {
            return defValue;
        }
        try {
            return json.getDouble(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return defValue;
        }
    }

    public static int getJsonValue(JSONObject json, String key, int defValue) {
        if (json == null || !json.has(key)) {
            return defValue;
        }
        try {
            return json.getInt(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return defValue;
        }
    }

    public static String getJsonValue(JSONObject json, String key) {
        if (json != null && json.has(key)) {
            try {
                return json.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String getJsonValue(JSONObject json, String key, String defValue) throws JSONException {
        if (json == null || !json.has(key)) {
            return defValue;
        }
        try {
            String defValue2 = json.getString(key);
            return TextUtils.isEmpty(defValue2) ? defValue : defValue2;
        } catch (JSONException e) {
            e.printStackTrace();
            return defValue;
        }
    }

    public static boolean getJsonValue(JSONObject json, String key, boolean defValue) {
        if (json == null || !json.has(key)) {
            return defValue;
        }
        try {
            return json.getBoolean(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return defValue;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x002f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static org.json.JSONArray parseToJSONOArray(java.lang.String r3) throws org.json.JSONException {
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
        throw new UnsupportedOperationException("Method not decompiled: com.unisound.vui.util.JsonTool.parseToJSONOArray(java.lang.String):org.json.JSONArray");
    }

    public static JSONObject parseToJSONObject(String json) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            Object objNextValue = new JSONTokener(json).nextValue();
            return (objNextValue == null || !(objNextValue instanceof JSONObject)) ? jSONObject : (JSONObject) objNextValue;
        } catch (Exception e) {
            return jSONObject;
        }
    }

    public static void putJsonObjecValue(JSONObject objc, String key, Object value) throws JSONException {
        try {
            objc.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String toJson(Object src) {
        Gson gson = new Gson();
        return src == null ? gson.toJson((JsonElement) JsonNull.INSTANCE) : gson.toJson(src);
    }

    public static String toJsonWithFastJson(Object object) {
        return JSON.toJSONString(object);
    }
}
