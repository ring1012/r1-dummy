package com.unisound.sdk;

import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class cn {

    /* renamed from: a, reason: collision with root package name */
    private String f334a;
    private String b = "";
    private String c = "";
    private String d = "";

    public cn(String str) throws JSONException {
        this.f334a = "";
        this.f334a = str;
        b(str);
    }

    private boolean a(String str) {
        return str == null || str.trim().length() == 0;
    }

    private boolean b(String str) throws JSONException {
        boolean z = false;
        if (a(str)) {
            return false;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject == null) {
                return false;
            }
            this.d = jSONObject.has("history") ? jSONObject.getString("history") : "";
            this.c = jSONObject.has("text") ? jSONObject.getString("text") : "";
            if (!jSONObject.has(i.i)) {
                return false;
            }
            JSONObject jSONObject2 = jSONObject.getJSONObject(i.i);
            this.b = jSONObject2.has("text") ? jSONObject2.getString("text") : "";
            z = true;
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return z;
        }
    }

    public String a() {
        return this.f334a;
    }

    public String b() {
        return this.b;
    }

    public String c() {
        return this.d;
    }

    public String d() {
        return this.c;
    }
}
