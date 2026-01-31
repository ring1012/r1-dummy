package com.unisound.common;

import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class af {

    /* renamed from: a, reason: collision with root package name */
    private static final String f237a = "v";
    private static final String b = "ak";
    private static final String c = "udid";
    private static final String d = "curw";
    private static final String e = "neww";
    private static final String f = "reqID";
    private String g = "1.0";
    private String h;
    private String i;
    private Set<String> j;
    private Set<String> k;
    private String l;

    public String a() {
        return this.g;
    }

    public void a(String str) {
        this.g = str;
    }

    public void a(Set<String> set) {
        this.j = set;
    }

    public String b() {
        return this.h;
    }

    public void b(String str) {
        this.h = str;
    }

    public void b(Set<String> set) {
        this.k = set;
    }

    public String c() {
        return this.i;
    }

    public void c(String str) {
        this.i = str;
        this.l = aa.a(str + String.valueOf(System.currentTimeMillis()));
    }

    public Set<String> d() {
        return this.j;
    }

    public Set<String> e() {
        return this.k;
    }

    public String f() {
        return "v:" + this.g + " ; " + b + ":" + this.h + " ; udid:" + this.i + " ; " + d + ":" + this.j.toString() + " ; " + e + ":" + this.k.toString() + " ; " + f + ":" + this.l;
    }

    public String g() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        JSONArray jSONArray = new JSONArray();
        if (this.k != null) {
            for (Object obj : this.k.toArray()) {
                jSONArray.put(obj.toString());
            }
        }
        JSONArray jSONArray2 = new JSONArray();
        if (this.j != null) {
            for (Object obj2 : this.j.toArray()) {
                jSONArray2.put(obj2.toString());
            }
        }
        try {
            jSONObject.put(f237a, this.g);
            jSONObject.put(b, this.h);
            jSONObject.put("udid", this.i);
            jSONObject.put(f, this.l);
            jSONObject.put(d, jSONArray2);
            jSONObject.put(e, jSONArray);
        } catch (JSONException e2) {
            y.a("OneshotVO JSONException");
            e2.printStackTrace();
        }
        return jSONObject.toString();
    }
}
