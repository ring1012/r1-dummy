package com.baidu.b.a;

import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
class a {
    static String a(int i, String str) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("status", i);
            jSONObject.put("message", str);
        } catch (JSONException e) {
        }
        return jSONObject.toString();
    }

    static String a(String str) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("status", -1);
            jSONObject.put("message", str);
        } catch (JSONException e) {
        }
        return jSONObject.toString();
    }
}
