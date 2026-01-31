package com.unisound.common;

import android.text.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class VoiceprintResult {

    /* renamed from: a, reason: collision with root package name */
    private String f231a;
    private int b = 0;
    private String c = "";
    private float d = 0.0f;

    public VoiceprintResult(String str) {
        this.f231a = str;
        a(str);
    }

    private boolean a(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        y.c(str);
        try {
            JSONObject jSONObject = new JSONObject(b(str));
            if (jSONObject != null) {
                this.b = Integer.valueOf(jSONObject.has("status") ? jSONObject.getInt("status") : 0).intValue();
                this.c = jSONObject.has("username") ? jSONObject.getString("username") : "";
                this.d = Float.valueOf(jSONObject.has("score") ? jSONObject.getString("score") : "0").floatValue();
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String b(String str) {
        if (!str.contains("}{")) {
            return str;
        }
        String strSubstring = str.substring(str.indexOf("}") + 1);
        y.a("jsonStr: " + strSubstring);
        return strSubstring;
    }

    public float getScore() {
        return this.d;
    }

    public int getStatus() {
        return this.b;
    }

    public String getString() {
        return this.f231a;
    }

    public String getUserName() {
        return this.c;
    }
}
